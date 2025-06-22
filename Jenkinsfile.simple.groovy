pipeline {
    agent {
        label 'docker'
    }


    stage('Source') {
        steps {
            git 'https://github.com/jhonnyalx/unir-cicd.git'
        }
    }
    
    stages {        
        stage('Build') {
            steps {
                sh 'make build || (echo "Build failed. Checking Docker logs..." && docker logs calculator-app || true)'
            }
        }
        
        stage('Unit Tests') {
            steps {
                sh 'make test-unit'
                junit 'results/unit_result.xml'
            }
        }
        
        stage('API Tests') {
            steps {
                sh 'make test-api'
                junit 'results/api_result.xml'
            }
        }
        
        stage('E2E Tests') {
            steps {
                sh 'make test-e2e'
                junit 'results/cypress_result.xml'
                archiveArtifacts artifacts: 'results/screenshots/**, results/videos/**', fingerprint: true
            }
        }

        stage('Coverage') {
            steps {
                publishHTML(target: [
                    reportName: 'Coverage Report',
                    reportDir: 'results/coverage',
                    reportFiles: 'index.html',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: false
                ])
            }
        }
    }
    
    post {
        always {
            sh 'make clean-all || true'
            cleanWs()
        }
    }
}