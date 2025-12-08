pipeline {
    agent any
    
    tools {
        maven 'Maven_3.8'
        jdk 'JDK_11'
    }

    environment {
        // Variables d'environnement
        PROJECT_NAME = 'FinanceApp'
        SONAR_PROJECT_KEY = 'finance-refactoring'
        VERSION = '1.0.0'
    }

    stages {
        // ÉTAPE 1 : Récupération du code
        stage('Checkout') {
            steps {
                echo '📦 Récupération du code source...'
                git branch: 'main',
                    url: 'https://github.com/Ahlam-amz/FinanceApp-Refactoring.git'

                script {
                    // Afficher les informations du commit
                    def commitHash = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                    def commitMessage = sh(returnStdout: true, script: 'git log -1 --pretty=%B').trim()
                    echo "🔍 Commit: ${commitHash} - ${commitMessage}"
                    currentBuild.displayName = "#${BUILD_NUMBER} - ${commitHash}"
                }
            }
        }

        // ÉTAPE 2 : Compilation et tests
        stage('Build & Tests') {
            steps {
                echo '🔨 Compilation et tests...'
                sh 'mvn clean compile test'

                // Génération du rapport JaCoCo pour la couverture
                sh 'mvn jacoco:report'
            }
            post {
                always {
                    echo '📊 Publication des rapports de test...'
                    junit 'target/surefire-reports/*.xml'

                    // Archive du rapport JaCoCo
                    publishHTML([
                        target: [
                            allowMissing: false,
                            alwaysLinkToLastBuild: true,
                            keepAll: true,
                            reportDir: 'target/site/jacoco',
                            reportFiles: 'index.html',
                            reportName: 'JaCoCo Coverage Report'
                        ]
                    ])
                }
                success {
                    echo '✅ Tests réussis!'
                    // Vérification de la couverture
                    script {
                        def coverage = sh(returnStdout: true, script: '''
                            grep -o "lineRate=\"[0-9.]*\"" target/site/jacoco/jacoco.xml | \
                            head -1 | cut -d'"' -f2 | awk '{printf "%.1f", $1*100}'
                        ''').trim()
                        echo "📈 Couverture de code: ${coverage}%"

                        // Marquer comme instable si couverture < 80%
                        if (coverage.toFloat() < 80.0) {
                            currentBuild.result = 'UNSTABLE'
                            echo '⚠️ Couverture insuffisante (< 80%)'
                        }
                    }
                }
                failure {
                    echo '❌ Échec des tests'
                }
            }
        }

        // ÉTAPE 3 : Analyse de qualité (SonarQube)
        stage('Code Quality') {
            steps {
                echo '📈 Analyse SonarQube...'
                // Vérifier si le fichier de configuration Sonar existe
                script {
                    if (fileExists('sonar-project.properties')) {
                        sh 'mvn sonar:sonar'
                    } else {
                        echo '⚠️ Fichier sonar-project.properties non trouvé, création automatique...'
                        writeFile file: 'sonar-project.properties', text: """
                            sonar.projectKey=${SONAR_PROJECT_KEY}
                            sonar.projectName=Finance App Refactoring
                            sonar.projectVersion=${VERSION}
                            sonar.sources=src/main/java
                            sonar.tests=src/test/java
                            sonar.java.binaries=target/classes
                            sonar.junit.reportsPath=target/surefire-reports
                            sonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                            sonar.exclusions=**/Main.java
                        """
                        sh 'mvn sonar:sonar'
                    }
                }
            }
            post {
                failure {
                    echo '❌ Échec de l\'analyse SonarQube'
                }
            }
        }

        // ÉTAPE 4 : Packaging
        stage('Packaging') {
            steps {
                echo '📦 Création du package JAR...'
                sh 'mvn package -DskipTests'

                // Vérification que le JAR a été créé
                script {
                    def jarFiles = findFiles(glob: 'target/*.jar')
                    if (jarFiles) {
                        echo "✅ JAR généré: ${jarFiles[0].name}"
                        // Archivage de l'artifact
                        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                    } else {
                        error '❌ Aucun fichier JAR trouvé dans target/'
                    }
                }
            }
            post {
                failure {
                    echo '❌ Échec du packaging'
                }
            }
        }

        // ÉTAPE 5 : Validation finale (optionnel)
        stage('Validation') {
            steps {
                echo '✅ Validation finale du build...'
                script {
                    sh '''
                        echo "=== RÉSUMÉ DU BUILD ==="
                        echo "Projet: ${PROJECT_NAME}"
                        echo "Version: ${VERSION}"
                        echo "Build: #${BUILD_NUMBER}"
                        echo "Date: $(date)"
                        echo "Répertoire: $(pwd)"
                        echo "=== FIN DU RÉSUMÉ ==="
                    '''
                }
            }
        }
    }

    post {
        always {
            echo '🏁 Build terminé avec statut: ${currentBuild.result}'
            echo '⏱️ Durée: ${currentBuild.durationString}'

            // Nettoyage (optionnel)
            // cleanWs()
        }

        success {
            echo '🎉 BUILD RÉUSSI!'
        }

        failure {
            echo '💥 BUILD ÉCHOUÉ!'
        }

        unstable {
            echo '⚠️ BUILD INSTABLE (couverture < 80%)'
        }

        // Email notification comme demandé
        always {
            emailext (
                attachLog: true,
                subject: "Build ${currentBuild.result}: ${JOB_NAME} #${BUILD_NUMBER}",
                to: 'a.amziane9670@uca.ac.ma',
                body: """Build ${currentBuild.result} - ${PROJECT_NAME}

📋 DÉTAILS:
- Projet: ${PROJECT_NAME}
- Build: #${BUILD_NUMBER}
- Statut: ${currentBuild.result}
- Durée: ${currentBuild.durationString}
- Commit: ${sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()}

🔗 LIENS:
- Build: ${env.BUILD_URL}
- Console: ${env.BUILD_URL}console
- Rapports: ${env.BUILD_URL}testReport/

${currentBuild.result == 'SUCCESS' ? '✅ Le projet est prêt pour la livraison!' : '🚨 Des actions sont nécessaires.'}
"""
            )
        }
    }
}