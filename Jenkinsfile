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

        // 🔥 DB ENV (MATCH YOUR JAVA CODE)
        DB_URL = "jdbc:mariadb://localhost:3307/shopping_cart_localization"
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
                docker run -d \
                  --name mariadb-test \
                  -e MYSQL_ROOT_PASSWORD=group7 \
                  -e MYSQL_DATABASE=shopping_cart_localization \
                  -p 3307:3306 \
                  mariadb:11

                sleep 15
                '''
            }
        }

        stage('Seed Database') {
            steps {
                sh '''
                docker exec -i mariadb-test \
                mysql -uroot -pgroup7 shopping_cart_localization < schema.sql
                '''
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
                        docker image rm $DOCKERHUB_REPO:$DOCKER_IMAGE_TAG || true
                        docker image prune -f || true
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