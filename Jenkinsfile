pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK21'
    }

    environment {
        PATH = "${env.PATH}:/usr/local/bin"

        DOCKERHUB_CREDENTIALS_ID = 'DockerHub_ID'
        DOCKERHUB_REPO = '218468/shopping-cart'
        DOCKER_IMAGE_TAG = "${BUILD_NUMBER}"

        DB_URL = "jdbc:mariadb://localhost:3308/shopping_cart_localization"
        DB_USER = "root"
        DB_PASSWORD = "group7"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/GamAungLahpai/shopping-card-localization'
            }
        }

        stage('Start Database') {
            steps {
                sh '''
                docker rm -f mariadb-test || true

                docker run -d \
                  --name mariadb-test \
                  -e MYSQL_ROOT_PASSWORD=group7 \
                  -e MYSQL_DATABASE=shopping_cart_localization \
                  -p 3308:3306 \
                  mariadb:11

                sleep 15
                '''
            }
        }

        stage('Seed Database') {
            steps {
                sh '''
                docker exec -i mariadb-test \
                mariadb -uroot -pgroup7 shopping_cart_localization < schema.sql
                '''
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean verify'
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
                '''
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(
                        credentialsId: DOCKERHUB_CREDENTIALS_ID,
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push $DOCKERHUB_REPO:$DOCKER_IMAGE_TAG
                        docker tag $DOCKERHUB_REPO:$DOCKER_IMAGE_TAG $DOCKERHUB_REPO:latest
                        docker push $DOCKERHUB_REPO:latest
                        docker logout || true
                    '''
                }
            }
        }
    }

    post {
        always {
            sh 'docker rm -f mariadb-test || true'
        }
    }
}