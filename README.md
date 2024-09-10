
# DS-CRM Project

## Overview
DS-CRM is a Spring Boot CRM application integrated with Jenkins, SonarQube, Nginx, Prometheus, and Grafana. It includes a comprehensive CI/CD pipeline and monitoring system, allowing for smooth development, deployment, and observability.

This documentation covers all the components of the project and provides detailed information about the tools and plugins used.

---

## Table of Contents
- [Services Overview](#services-overview)
- [Jenkins & SonarQube Setup](#jenkins--sonarqube-setup)
    - [Jenkins Plugins](#jenkins-plugins)
    - [SonarQube Plugins](#sonarqube-plugins)
- [Nginx Configuration](#nginx-configuration)
- [Prometheus & Grafana Monitoring](#prometheus--grafana-monitoring)
- [Docker Compose Setup](#docker-compose-setup)
- [Spring Boot Application Configuration](#spring-boot-application-configuration)
- [How to Run](#how-to-run)
- [CI/CD Pipeline](#cicd-pipeline)
- [Monitoring Setup](#monitoring-setup)
- [Plugins Breakdown](#plugins-breakdown)

---

## Services Overview

The DS-CRM application is composed of several core services integrated using Docker Compose:
- **Spring Boot CRM**: Core CRM functionality.
- **Jenkins**: Automation server for building, testing, and deploying applications.
- **SonarQube**: Code quality and security analysis.
- **Nginx**: Web server and reverse proxy for load balancing and SSL.
- **Prometheus**: Time-series monitoring.
- **Grafana**: Data visualization and monitoring dashboard.

---

## Jenkins & SonarQube Setup

Jenkins manages the continuous integration and deployment (CI/CD) pipeline, while SonarQube performs static code analysis to ensure code quality.

### Jenkinsfile

```groovy
pipeline {
    agent any

    environment {
        SONAR_SCANNER_HOME = tool 'SonarQubeScanner'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeServer') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Allure Report') {
            steps {
                allure([
                        includeProperties: false,
                        reportBuildPolicy: 'ALWAYS',
                        results: [[path: "target/allure-results"]]
                ])
            }
        }
    }
}
```

### Jenkins Plugins

Jenkins uses the following plugins to extend functionality:

- **SonarQube Scanner Plugin**: Integrates SonarQube analysis into the Jenkins pipeline.
- **Allure Jenkins Plugin**: Generates Allure test reports within Jenkins.
- **Pipeline**: The pipeline plugin enables Jenkins to define the build process with code.
- **Git Plugin**: Allows Jenkins to interact with Git repositories for source code management.
- **Docker Pipeline Plugin**: Enables Jenkins to work with Docker containers as part of the pipeline.

### SonarQube Plugins

SonarQube is extended with plugins for better analysis and quality control:

- **SonarJava**: Plugin for analyzing Java projects, providing static analysis, bug detection, and code smells.
- **SonarPython**: Helps analyze Python code if required.
- **SonarJS**: Analyzes JavaScript and TypeScript code in the project.
- **FindBugs Plugin**: Helps identify potential bugs by scanning the code base.
- **CheckStyle Plugin**: Enforces coding standards, formats, and rules within the project.

---

## Nginx Configuration

Nginx acts as a reverse proxy for routing traffic to the Spring Boot application, handling SSL termination for secure communication.

### Nginx Configuration Example

```nginx
server {
    listen 443 ssl;
    server_name localhost;

    ssl_certificate /etc/nginx/ssl/nginx.crt;
    ssl_certificate_key /etc/nginx/ssl/nginx.key;

    location / {
        proxy_pass http://host.docker.internal:8081;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

---

## Prometheus & Grafana Monitoring

Prometheus is responsible for collecting metrics from the Spring Boot application, while Grafana is used to visualize the collected metrics and monitor the system health.

### Prometheus Configuration

```yaml
scrape_configs:
  - job_name: 'ds-crm'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['host.docker.internal:8081']
```

### Grafana Configuration

Grafana is connected to Prometheus as its data source, allowing dashboards to be created for visualizing system metrics.

---

## Docker Compose Setup

The following services are integrated and run via Docker Compose:

```yaml
version: '3.8'
services:
  crm-app:
    build: .
    container_name: crm-app
    ports:
      - "8081:8081"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://crm-db:3306/dscrm
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: rootpassword
    networks:
      - crm-network

  crm-db:
    image: mysql:8.0
    container_name: crm-db
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: dscrm
    ports:
      - "3306:3306"
    networks:
      - crm-network
networks:
  crm-network:
    driver: bridge
```
```yaml
version: '3.8'
services:
  prometheus:
    container_name: prometheus-crm
    image: prom/prometheus:v2.35.0
    ports:
      - "9090:9090"
    networks:
      - crm-monitoring-net

  grafana:
    container_name: grafana-crm
    image: grafana/grafana:latest
    ports:
      - "3000:3000"
    environment:
      GF_SECURITY_ADMIN_PASSWORD: mysecurepassword
    networks:
      - crm-monitoring-net

networks:
  crm-network:
  crm-monitoring-net:
    driver: bridge
```

---

## Spring Boot Application Configuration

The Spring Boot application is configured to run on port `8081`, and it connects to a MySQL database for persistent storage.

- **Database**: MySQL 8.0
- **Metrics**: Actuator exposes metrics at `/actuator/prometheus` for Prometheus to scrape.
- **Spring Data JPA**: Used for database operations.

---

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/user/repo.git
   cd repo
   ```

2. Start the services using Docker Compose:
   ```bash
   docker-compose up -d
   ```

3. Access the services:
    - Jenkins: `http://localhost:8080`
    - SonarQube: `http://localhost:9000`
    - Prometheus: `http://localhost:9090`
    - Grafana: `http://localhost:3000`
    - DS-CRM: `http://localhost:8081`

---

## CI/CD Pipeline

The CI/CD pipeline manages the entire build, test, and deploy process using Jenkins. The pipeline runs SonarQube analysis to check code quality and integrates Allure for generating test reports.

---

## Monitoring Setup

Metrics are collected using Prometheus and visualized with Grafana. The Spring Boot application's actuator exposes metrics that Prometheus scrapes, and Grafana uses those metrics to provide insightful dashboards.

---

## Plugins Breakdown

Here is a summary of all the key plugins used:

### Jenkins Plugins

- **Pipeline**: Defines the stages of CI/CD pipelines in code.
- **SonarQube Scanner Plugin**: Runs SonarQube code quality analysis as part of the pipeline.
- **Allure Plugin**: Generates Allure test reports.
- **Git Plugin**: Interacts with Git repositories for code checkout.
- **Docker Pipeline Plugin**: Allows Jenkins to use Docker containers in the pipeline.

### SonarQube Plugins

- **SonarJava**: Analyzes Java code for bugs, vulnerabilities, and code smells.
- **SonarPython**: For analyzing Python code, if applicable.
- **SonarJS**: Analyzes JavaScript and TypeScript code.
- **FindBugs Plugin**: Finds common bugs in Java code.
- **CheckStyle Plugin**: Enforces coding style and best practices.

### Grafana Plugins

- **Prometheus Data Source Plugin**: Connects Grafana to Prometheus for metric visualization.
- **Grafana Clock Panel**: Adds a clock panel to Grafana dashboards.

---

## Conclusion

The DS-CRM project integrates multiple services and tools to provide a complete CI/CD and monitoring solution. Jenkins automates the build process, SonarQube ensures code quality, and Prometheus and Grafana provide real-time monitoring and visualization. The setup ensures the project is well-managed and easy to deploy and monitor.
