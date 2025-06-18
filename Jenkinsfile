pipeline {
    agent any

    tools {
        maven 'Maven_3'       // Name must match the Maven tool you configured in Jenkins
        jdk 'JDK_17'          // Name must match the JDK you configured
    }

    environment {
        JAR_NAME = 'employee-service.jar'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/vish-tester/EmployeeApp.git'  // or use local folder
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                echo 'Running the Spring Boot application...'
                sh 'nohup java -jar target/*.jar &'
            }
        }
    }

    post {
        success {
            echo '🚀 Application deployed successfully!'
        }
        failure {
            echo '❌ Build or deployment failed.'
        }
    }
}
