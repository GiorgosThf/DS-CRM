# Use the official Jenkins LTS image from Docker Hub
FROM jenkins/jenkins:lts

# Switch to the root user to install dependencies
USER root

# Install Maven
RUN apt-get update && apt-get install -y maven && apt-get clean

# Install Docker (so Jenkins can run Docker commands, if necessary for your CI/CD pipeline)
RUN apt-get update && apt-get install -y apt-transport-https ca-certificates curl gnupg2 software-properties-common && apt-get clean
RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -
RUN add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable"
RUN apt-get update && apt-get install -y docker-ce-cli && apt-get clean

# Switch back to the Jenkins user
USER jenkins

# Expose the default Jenkins port
EXPOSE 8080