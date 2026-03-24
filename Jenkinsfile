pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK21'
    }

    environment {
        PATH = "/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/Applications/Docker.app/Contents/Resources/bin"
        DOCKERHUB_CREDENTIALS_ID = 'DockerHub_ID'
        DOCKERHUB_REPO = '218468/shopping-cart'
        DOCKER_IMAGE_TAG = "${env.BUILD_NUMBER}"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/GamAungLahpai/shopping-card-localization'
            }
        }

        stage('Build') {
            steps {
                sh 'java -version'
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Code Coverage') {
            steps {
                sh 'mvn jacoco:report'
            }
        }

        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Publish Coverage Report') {
            steps {
                publishHTML target: [
                        reportDir  : 'target/site/jacoco',
                        reportFiles: 'index.html',
                        reportName : 'JaCoCo Coverage Report',
                        keepAll    : true
                ]
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                    docker build --pull -t $DOCKERHUB_REPO:$DOCKER_IMAGE_TAG .
                    docker images | head
                '''
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: DOCKERHUB_CREDENTIALS_ID, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push $DOCKERHUB_REPO:$DOCKER_IMAGE_TAG
                        docker tag $DOCKERHUB_REPO:$DOCKER_IMAGE_TAG $DOCKERHUB_REPO:latest
                        docker push $DOCKERHUB_REPO:latest
                        docker image rm $DOCKERHUB_REPO:$DOCKER_IMAGE_TAG || true
                        docker image prune -f || true
                    '''
                }
            }
        }
    }
}