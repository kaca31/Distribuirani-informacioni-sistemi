pipeline {
    agent any

    environment {
        DOCKER_COMPOSE_DIR = "${WORKSPACE}" // radni direktorijum gde je docker-compose.yml
    }

    stages {
        stage('Build & Test with Maven') {
            agent {
                docker { image 'maven:3.9.2-eclipse-temurin-17' }
            }
            steps {
                echo "🔹 Running Maven build and tests"
                sh 'mvn clean install -DskipTests' // build bez testova
                sh 'mvn test'                       // pokreni unit i integracione testove
            }
        }

        stage('Build Docker Images') {
            steps {
                echo "🔹 Building Docker images for microservices"
                dir("${DOCKER_COMPOSE_DIR}") {
                    sh 'docker-compose build'
                }
            }
        }

        stage('Deploy Services') {
            steps {
                echo "🔹 Deploying microservices"
                dir("${DOCKER_COMPOSE_DIR}") {
                    sh 'docker-compose up -d'
                }
            }
        }
    }

    post {
        success {
            echo '✅ SUCCESS - System is up!'
        }
        failure {
            echo '❌ FAILED - Check logs for details'
        }
    }
}
