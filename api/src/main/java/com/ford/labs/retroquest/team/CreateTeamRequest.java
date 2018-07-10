/*
 * Copyright (c) 2018 Ford Motor Company
 *  All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.ford.labs.retroquest.team;

import com.ford.labs.retroquest.team.validation.CaptchaConstraint;
import com.ford.labs.retroquest.team.validation.PasswordConstraint;
import com.ford.labs.retroquest.team.validation.TeamNameConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeamRequest {

    @TeamNameConstraint
    private String name;

    @PasswordConstraint
    private String password;

    @CaptchaConstraint
    private String captchaResponse;
}