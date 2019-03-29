@Library('jenkins-shared-library')_
pipeline {
    environment {
        registry = "windsekirun/uploadfileboot-test"
        registryCredential = 'dockerhub'
        dockerImage = ''
    }
    agent any
    stages {
        stage ('start') {
            steps {
                sendNotifications 'STARTED'
            }
        }
        stage('environment') {
            parallel {
                stage('chmod') {
                    steps {
                        sh 'chmod 755 ./gradlew'
                    }
                }
                stage('display') {
                    steps {
                        sh 'ls -la'
                    }
                }
            }
        }
        stage('build') {
            steps {
                sh './gradlew build'
            }
        }
        stage('dockerize') {
            steps {
                dockerImage = docker.build registry + ":$BUILD_NUMBER"
            }
        }
        stage('deploy') {
            steps {
                script {
                    docker.withRegistry( '', registryCredential ) {
                        dockerImage.push()
                    }
                }
            }
        }
        stage('clean') {
            steps{
                sh "docker rmi $registry:$BUILD_NUMBER"
            }
        }
     }
    post {
        always {
            sendNotifications currentBuild.result
        }
    }
}
