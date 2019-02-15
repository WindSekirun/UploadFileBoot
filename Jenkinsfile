@Library('jenkins-shared-library')_
pipeline {
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
  }
  post {
      always {
        sendNotifications currentBuild.result
      }
  }
}