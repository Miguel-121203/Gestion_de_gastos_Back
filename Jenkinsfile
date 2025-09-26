pipeline {
    agent any

    environment {
        DOCKER_HUB_REPO = 'miguel1203/gestion-back'
        IMAGE_TAG = 'dev'
        DOCKER_USER = 'miguel1203'
        DOCKER_PASS = 'dckr_pat_5CQzKCl99YfG_0nuHQMiuxUw6AY'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code...'
                checkout scm
            }
        }

        stage('Deploy Database') {
            steps {
                echo 'Deploying PostgreSQL DEV...'
                dir('DB') {
                    sh '''
                        echo "🚀 Desplegando PostgreSQL DEV..."
                        docker compose -f docker-compose-dev.yml -p expense-db-dev up -d
                        sleep 30
                    '''
                }
            }
        }

        stage('Build Application') {
            steps {
                echo 'Building Spring Boot application...'
                dir('Back/ms_expense/ms_expense') {
                    sh 'chmod +x ./mvnw'
                    sh './mvnw clean package -DskipTests'
                }
            }
        }

        stage('Copy JAR to Dockerfile location') {
            steps {
                echo 'Copying JAR to Dockerfile location...'
                sh '''
                    mkdir -p ./Back/ms_expense/target
                    cp ./Back/ms_expense/ms_expense/target/*.jar ./Back/ms_expense/target/
                '''
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                echo 'Building and pushing Docker image...'
                dir('Back/ms_expense') {
                    script {
                        sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'

                        def image = docker.build("${DOCKER_HUB_REPO}:${IMAGE_TAG}")
                        image.push()
                    }
                }
            }
        }

        stage('Deploy Application') {
            steps {
                echo 'Deploying application DEV...'
                sh '''
                    echo "🚀 Desplegando aplicación DEV..."
                    docker stop gestion-back-dev || true
                    docker rm gestion-back-dev || true

                    docker run -d \
                      --name gestion-back-dev \
                      --network expense-db-dev_default \
                      -p 8080:8080 \
                      -e SPRING_PROFILES_ACTIVE=dev \
                      -e SPRING_DATASOURCE_URL="jdbc:postgresql://postgres-expense-dev:5432/expense_db_dev" \
                      -e SPRING_DATASOURCE_USERNAME=postgres \
                      -e SPRING_DATASOURCE_PASSWORD=dev123456 \
                      ${DOCKER_HUB_REPO}:${IMAGE_TAG}
                '''
            }
        }

    
    }

    post {
        success {
            echo '''
            ========================================
            ✅ DEPLOYMENT SUCCESSFUL!
            ========================================
            🌐 Application: http://localhost:8080
            🔍 Health Check: http://localhost:8080/actuator/health
            📊 Metrics: http://localhost:8080/actuator/metrics
            🗄️ Database: localhost:5432
            ========================================
            '''
        }
        failure {
            echo 'Deployment failed! Checking logs...'
            sh '''
                echo "Application logs:"
                docker logs gestion-back-dev || echo "No application logs available"
                echo "Database logs:"
                docker logs postgres-expense-dev || echo "No database logs available"
            '''
        }
        always {
            echo 'Pipeline execution completed.'
        }
    }
}
