pipeline {
    agent {
        label 'docker'
    }

    stages {
        stage('Source') {
            steps {
                git 'https://github.com/jhonnyalx/unir-cicd.git'
            }
        }
        
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
            post {
                always {
                    archiveArtifacts artifacts: 'results/unit_result.xml', fingerprint: true
                }
            }
        }
        
        stage('API Tests') {
            steps {
                sh 'make test-api'
                junit 'results/api_result.xml'
            }
            post {
                always {
                    archiveArtifacts artifacts: 'results/api_result.xml', fingerprint: true
                    publishHTML(target: [
                        reportName: 'API Test Report',
                        reportDir: 'results',
                        reportFiles: 'api_result.xml',
                        keepAll: true,
                        alwaysLinkToLastBuild: true,
                        allowMissing: false
                    ])
                }
            }
        }
        
        stage('E2E Tests') {
            steps {
                sh 'make test-e2e'
                junit 'results/cypress_result.xml'
                archiveArtifacts artifacts: 'results/screenshots/**, results/videos/**', fingerprint: true
            }
            post {
                always {
                    archiveArtifacts artifacts: 'results/cypress_result.xml', fingerprint: true
                    publishHTML(target: [
                        reportName: 'E2E Test Report',
                        reportDir: 'results',
                        reportFiles: 'cypress_result.xml',
                        keepAll: true,
                        alwaysLinkToLastBuild: true,
                        allowMissing: false
                    ])
                }
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
        
        stage('Archive Test Results') {
            steps {
                script {
                    // Archivar todos los archivos XML de pruebas
                    archiveArtifacts artifacts: 'results/*.xml', fingerprint: true, allowEmptyArchive: true
                    
                    // Generar reporte consolidado de pruebas
                    publishTestResults testResultsPattern: 'results/*.xml'
                }
            }
        }
    }
    
    post {
        always {
            sh 'make clean-all || true'
            cleanWs()
        }
        failure {
            emailext (
                subject: "Pipeline FAILED: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                body: """
                <h2>Pipeline Execution Failed</h2>
                <p><strong>Job Name:</strong> ${env.JOB_NAME}</p>
                <p><strong>Build Number:</strong> ${env.BUILD_NUMBER}</p>
                <p><strong>Build URL:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                <p><strong>Timestamp:</strong> ${new Date()}</p>
                
                <h3>Build Details:</h3>
                <ul>
                    <li>Duration: ${currentBuild.durationString}</li>
                    <li>Result: ${currentBuild.result}</li>
                    <li>Started by: ${currentBuild.getBuildCauses('hudson.model.Cause$UserIdCause')[0]?.userId ?: 'System'}</li>
                </ul>
                
                <p>Please check the build logs for more details.</p>
                """,
                mimeType: 'text/html',
                to: "${env.CHANGE_AUTHOR_EMAIL ?: env.BUILD_USER_EMAIL ?: 'admin@company.com'}"
            )
        }
    }
}