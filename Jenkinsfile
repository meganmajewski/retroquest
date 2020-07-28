pipeline {
    agent {
        label 'chrome-jdk8'
    }
    options {
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '1', artifactNumToKeepStr: '1'))
    }
    stages {
        stage('Setup Environment') {
            steps {
                container('chrome') {
                    sh 'echo "systemProp.http.proxyHost=$GRADLE_HTTP_PROXY" >> gradle.properties'
                    sh 'echo "systemProp.https.proxyHost=$GRADLE_HTTP_PROXY" >> gradle.properties'
                    sh 'echo "systemProp.http.proxyPort=$GRADLE_HTTP_PROXY" >> gradle.properties'
                    sh 'echo "systemProp.https.proxyPort=$PROXY_PORT" >> gradle.properties'
                    sh 'echo "systemProp.http.proxyHost=$PROXY_PORT" >> gradle.properties'
                    sh 'echo "systemProp.http.nonProxyHosts=$NO_PROXY_HOSTS" >> gradle.properties'
                    sh 'echo "systemProp.https.nonProxyHosts=$NO_PROXY_HOSTS" >> gradle.properties'
                }
            }
        }
        stage('Frontend Tests') {
            steps {
                container('chrome') {
                    sh './gradlew uiUnitTests'
                }
            }
        }
        stage('Lint SCSS') {
            steps {
                container('chrome') {
                    sh './gradlew uiLintSCSS'
                }
            }
        }
        stage('Lint Typescript') {
            steps {
                container('chrome') {
                    sh './gradlew uiLintTypeScript'
                }
            }
        }
        stage('Build UI Prod Package') {
            steps {
                container('chrome') {
                    sh './gradlew buildProdPackage'
                }
            }
        }
        stage('Build API') {
            steps {
                container('chrome') {
                    sh './gradlew  build'
                }
            }
        }
        stage('API Tests') {
            steps {
                container('chrome') {
                    sh './gradlew  apiTest'
                }
            }
        }
        stage('Deploy Dev') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'pcf-pe-prod', usernameVariable: 'CF_CCUSER', passwordVariable: 'CF_CCPASSWORD')]) {
                    container('cf-cli') {
                        sh 'echo Logging in to Cloud Foundry'
                        sh 'cf login -u $CF_CCUSER -p $CF_CCPASSWORD -a https://api.sys.pd01.edc1.cf.ford.com -s Platform-Enablement-prod'
                        sh 'echo Blue-Green push to Cloud Foundry'
                        sh 'cf blue-green-deploy dev-retroquest --delete-old-apps'
                    }
                }
            }
        }
    }
}

