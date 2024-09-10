
# CRM Jenkins SonarQube CI/CD Pipeline

## Overview

This project integrates Jenkins, SonarQube, and Docker to implement a CI/CD pipeline for building, testing, and analyzing a CRM application. The pipeline automates code quality checks, builds, and generates reports, making it easier to manage the software development lifecycle efficiently.

## Table of Contents

1. [Technologies Used](#technologies-used)
2. [CI/CD Pipeline Workflow](#cicd-pipeline-workflow)
3. [Services](#services)
    1. [Jenkins](#jenkins)
    2. [SonarQube](#sonarqube)
    3. [PostgreSQL](#postgresql)
4. [Docker Setup](#docker-setup)
5. [Environment Setup](#environment-setup)
6. [How to Use](#how-to-use)

## Technologies Used

- **Jenkins**: Jenkins is an open-source automation server used to automate parts of the software development process. It is commonly used for CI/CD to automate testing, building, and deploying applications.
- **SonarQube**: SonarQube is an open-source platform for continuous inspection of code quality. It performs automatic reviews with static analysis of code to detect bugs, code smells, and security vulnerabilities.
- **PostgreSQL**: PostgreSQL is an open-source relational database used by SonarQube to store analysis results.
- **Docker**: Docker is used to containerize Jenkins and SonarQube, making the services portable and easy to deploy.

## CI/CD Pipeline Workflow

### 1. Code Checkout

The pipeline starts by checking out the latest version of the code from the GitHub repository. It uses the credentials specified in Jenkins.

### 2. Build and Test

Once the code is checked out, the pipeline builds the application using Maven, ensuring that any compilation issues are caught early. Unit tests are run to validate the code.

### 3. SonarQube Analysis

The SonarQube analysis step evaluates the quality of the code. It identifies bugs, code smells, and potential security vulnerabilities, providing feedback to the developer.

### 4. Report Generation

After code analysis, a report is generated using Allure to display the test results, making it easier for developers to review the build status.

## Services

### Jenkins

- Jenkins is used to automate parts of the development workflow, such as testing and deploying.
- The Jenkins service is configured to run on port `8080`.
- It connects with SonarQube for code analysis and generates Allure reports.

### SonarQube

- SonarQube analyzes the code for potential bugs, vulnerabilities, and code smells.
- The SonarQube service is exposed on port `9000`.
- SonarQube uses a PostgreSQL database to store results of code analysis.

### PostgreSQL

- PostgreSQL is used as the database for SonarQube.
- It stores the results of the analysis in the `sonar` database.

## Docker Setup

The Docker Compose setup contains three main services: Jenkins, SonarQube, and PostgreSQL. Here's a breakdown of the configuration:

```yaml
version: '3.8'

services:
  jenkins:
    build:
      context: .
      dockerfile: Dockerfile
    image: jenkins/jenkins:lts
    container_name: crm-jenkins
    ports:
      - "50000:50000"
      - "8080:8080"
    networks:
      - jenkins_sonarqube_network
    volumes:
      - jenkins_home:/var/jenkins_home
    environment:
      - SONAR_HOST_URL=http://sonarqube:9000
      - SONAR_LOGIN=admin
      - SONAR_PASSWORD=admin

  sonarqube:
    image: sonarqube:latest
    container_name: crm-sonarqube
    ports:
      - "9000:9000"
    environment:
      - SONARQUBE_JDBC_URL=jdbc:postgresql://sonarqube-db:5432/sonar
      - SONAR_JDBC_USERNAME=sonar
      - SONAR_JDBC_PASSWORD=sonar
    networks:
      - jenkins_sonarqube_network
    volumes:
      - sonarqube_data:/opt/sonarqube/data
      - sonarqube_logs:/opt/sonarqube/logs
      - sonarqube_extensions:/opt/sonarqube/extensions

  sonarqube-db:
    image: postgres:12-alpine
    container_name: crm-sonarqube-db
    environment:
      POSTGRES_USER: sonar
      POSTGRES_PASSWORD: sonar
      POSTGRES_DB: sonar
    networks:
      - jenkins_sonarqube_network
    volumes:
      - postgresql_data:/var/lib/postgresql/data

networks:
  jenkins_sonarqube_network:
    driver: bridge

volumes:
  jenkins_home:
  sonarqube_data:
  sonarqube_logs:
  sonarqube_extensions:
  postgresql_data:
```

### Dockerfile for Jenkins

The Jenkins Dockerfile is configured with Maven and Docker CLI to allow Jenkins to build and manage Docker-based projects:

```dockerfile
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
EXPOSE 8080
```

## Environment Setup

1. Clone the repository.
2. Make sure Docker is installed.
3. Run `docker-compose up --build` to start Jenkins, SonarQube, and PostgreSQL services.

## How to Use

### Jenkins

1. Access Jenkins via `http://localhost:8080`.
2. Create a pipeline job and use the provided `Jenkinsfile`.
3. Monitor builds and SonarQube analysis from the Jenkins dashboard.

### SonarQube

1. Access SonarQube via `http://localhost:9000`.
2. Use the default login credentials: `admin` / `admin`.
3. Review the code quality reports from the SonarQube dashboard.


## Conclusion

This setup automates the CI/CD process using Jenkins and ensures code quality through SonarQube, all while leveraging the power of Docker for containerization. It provides an easy-to-use and scalable development pipeline for your CRM system.
