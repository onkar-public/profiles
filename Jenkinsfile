pipeline {
    agent any
    stages {
        stage('Build The Image') {
            steps {
                sh 'mvn install -Ddocker'
            }
        }

        stage('Push The Image') {
            steps {
                sh 'docker tag teamteach/profiles:latest 333490196116.dkr.ecr.ap-south-1.amazonaws.com/teamteach-profiles'
                sh '$(aws ecr get-login --no-include-email --region ap-south-1)'
                sh 'docker push 333490196116.dkr.ecr.ap-south-1.amazonaws.com/teamteach-profiles'
            }
        }

        stage('Pull and Run (ssh to ec2)') {
            steps {
                sh 'ssh ec2-user@ms.digisherpa.ai \'$(aws ecr get-login --no-include-email --region ap-south-1) ; docker pull 333490196116.dkr.ecr.ap-south-1.amazonaws.com/teamteach-profiles:latest; docker stop teamteach-profiles ; docker rm teamteach-profiles; docker run --net=host -d --name teamteach-profiles 333490196116.dkr.ecr.ap-south-1.amazonaws.com/teamteach-profiles:latest \''
            }
        }

    }
}
