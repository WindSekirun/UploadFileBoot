@Library('jenkins-shared-library')_
pipeline {
    environment {
        registry = "windsekirun/uploadfileboot-test"
        registryCredential = 'DockerHub'
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
        stage('docker image build') {
            steps {
                sh 'docker build -t $registry:$BUILD_NUMBER .'
            }
        }
        stage('deploy docker image') {
            steps {
                withDockerRegistry([ credentialsId: registryCredential, url: "" ]) {
                    sh 'docker push $registry:$BUILD_NUMBER'
                }
            }
        }
        stage('clean image') {
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
