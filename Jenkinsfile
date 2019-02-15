pipeline {
  agent any
  stages {
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
        sh 'docker build .'
      }
    }
  }
}