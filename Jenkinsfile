@Library('jenkins-shared-library')_
pipeline {
    environment {
        registry = "windsekirun/uploadfileboot-test"
        registryCredential = 'dockerhub'
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
                sh 'docker build -t $registry:$BUILD_NUMBER .'
            }
        }
        stage('deploy') {
            steps {
                withDockerRegistry([ credentialsId: registryCredential, url: "" ]) {
                    sh 'docker push $registry:$BUILD_NUMBER'
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
