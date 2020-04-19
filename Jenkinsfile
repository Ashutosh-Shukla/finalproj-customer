pipeline {
  agent any
    environment {
        DOCKER_CONFIG = "${WORKSPACE}/docker.config"
    }
  stages {
    stage('Git Clone') {
      steps {
          checkout scm
      }
    }
    
    stage('Build package') {
     agent {
            docker {
            image 'twalter/maven-docker'
            args '-v /var/run/docker.sock:/var/run/docker.sock'
            }
      }
      steps {
          sh 'cd ${WORKSPACE}'
          sh 'mvn clean install'
      }
    }

    
    stage('Build & Push image') {
    agent {
        docker {
        image 'twalter/maven-docker'
        args '-v /var/run/docker.sock:/var/run/docker.sock'
        }

    }
      steps {
          sh '''
          env && docker build -t ${BACKEND_IMAGE_NAME}:${GIT_COMMIT} .
          pwd
          sudo docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}
          sudo docker push ${BACKEND_IMAGE_NAME}:${GIT_COMMIT}
          '''
      }
    }

    stage('List pods') {
    agent { docker 'lachlanevenson/k8s-kubectl:v1.14.0' }
    steps {
      sh '''
      kubectl -n api set image deployment/backend f19-backend=${BACKEND_IMAGE_NAME}:${GIT_COMMIT} --record
      '''
    }
  }
  }
}