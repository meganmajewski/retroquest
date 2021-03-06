package com.ford.labs.retroquest.api.authorization;

import com.ford.labs.retroquest.team.Team;
import com.ford.labs.retroquest.users.User;
import com.ford.labs.retroquest.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ApiAuthorizationTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ApiAuthorization apiAuthorization;
    private Set<Team> teams;

    @BeforeEach
    public void setUp() {
        teams = new HashSet<>();
    }

    @Test
    public void given_team_id_equals_the_authenticated_principal_when_request_authorized_return_true() {
        final String teamId = "bob";
        given(authentication.getPrincipal()).willReturn(teamId);

        assertThat(apiAuthorization.requestIsAuthorized(authentication, teamId)).isTrue();

    }

    @Test
    public void given_logged_in_user_belongs_to_a_team_when_request_authorized_return_true() {
        final String teamName = "team";
        String loggedInUser = "bob";
        String teamId = "/team/uri";
        Team team = Team.builder()
                .name(teamName)
                .uri(teamId)
                .build();

        teams.add(team);

        User user = User.builder()
                .teams(teams)
                .build();

        given(authentication.getPrincipal()).willReturn(loggedInUser);
        Optional<User> expectedUser = Optional.ofNullable(user);
        given(userRepository.findByName(loggedInUser)).willReturn(expectedUser);

        assertThat(apiAuthorization.requestIsAuthorized(authentication, teamId)).isTrue();

    }

    @Test
    public void given_a_teamId_that_is_not_authorized_a_when_request_authorized_return_false() {
        final String unauthorizedUser = "notAuthorized";
        String authorizedUser = "authorizedUser";
        given(authentication.getPrincipal()).willReturn(authorizedUser);
        Team authorizedTeam = Team.builder()
                .name(unauthorizedUser)
                .uri("/auth/team")
                .build();
        Set<Team> authorizedTeams = new HashSet<>();
        authorizedTeams.add(authorizedTeam);
        User user = User
                .builder()
                .teams(authorizedTeams)
                .build();
        given(userRepository.findByName(authorizedUser)).willReturn(Optional.ofNullable(user));

        assertThat(apiAuthorization.requestIsAuthorized(authentication, unauthorizedUser)).isFalse();
    }

}
