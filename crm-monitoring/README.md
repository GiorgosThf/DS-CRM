# Monitoring Setup with Prometheus and Grafana

## Overview

This setup leverages **Prometheus** for scraping metrics from your services and **Grafana** for visualizing those
metrics. The configuration includes a Docker Compose setup with Grafana and Prometheus containers, along with
instructions to monitor Spring Boot applications through Prometheus' `/actuator/prometheus` endpoint.

### What is Prometheus?

**Prometheus** is an open-source monitoring and alerting toolkit designed to collect and store time-series data. It is
mainly used for monitoring system health and performance by scraping metrics from services or applications at specified
intervals. Prometheus provides powerful query language support (PromQL) and can trigger alerts when certain conditions
are met.

### What is Grafana?

**Grafana** is an open-source platform for monitoring and observability that enables you to visualize, query, and
analyze data from various data sources, including Prometheus. Grafana provides an intuitive dashboard system that allows
you to create rich visualizations, alerts, and custom reports based on your data.

---

## Docker Compose Configuration

### Services

#### Grafana

The Grafana service provides the web interface for visualizing metrics collected by Prometheus.

```yaml
  grafana:
    container_name: grafana-crm
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "3000:3000"
    environment:
      GF_SECURITY_ADMIN_PASSWORD: mysecurepassword
      GF_USERS_ALLOW_SIGN_UP: "false"
    volumes:
      - grafana-storage:/var/lib/grafana
    restart: always
    networks:
      - crm-monitoring-net
      - crm-network
```

- **Ports**: Exposes Grafana on port `3000`.
- **Environment Variables**:
    - `GF_SECURITY_ADMIN_PASSWORD`: Set the Grafana admin password.
    - `GF_USERS_ALLOW_SIGN_UP`: Disable sign-ups for security.
- **Volumes**:
    - `grafana-storage`: Persists Grafana's data.
- **Restart Policy**: Always restart the container if it stops.

#### Prometheus

The Prometheus service is responsible for scraping and collecting metrics from configured services.

```yaml
  prometheus:
    container_name: prometheus-crm
    image: prom/prometheus:v2.35.0
    volumes:
      - prometheus-data:/prometheus
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
    ports:
      - "9090:9090"
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'
    restart: always
    networks:
      - crm-monitoring-net
      - crm-network
```

- **Ports**: Exposes Prometheus on port `9090`.
- **Volumes**:
    - `prometheus-data`: Persists Prometheus data.
    - Mounts the `prometheus.yml` configuration file.
- **Command**: Prometheus is started with the custom configuration file (`/etc/prometheus/prometheus.yml`).

### Volumes

The following volumes are used to persist data for both services:

```yaml
volumes:
  grafana-storage:   # Persists Grafana data
  prometheus-data:   # Persists Prometheus data
```

### Networks

Both services are connected to the same network, allowing them to communicate with each other:

```yaml
networks:
  crm-monitoring-net:
    driver: bridge
  crm-network:
    driver: bridge
```

---

## Prometheus Configuration

The `prometheus.yml` configuration file defines which targets Prometheus should scrape metrics from:

```yaml
scrape_configs:
  - job_name: 'ds-crm'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 15s
    static_configs:
      - targets: [ 'host.docker.internal:8081' ]
```

- **job_name**: The name of the job, in this case, `ds-crm`.
- **metrics_path**: The path to the Prometheus metrics endpoint (`/actuator/prometheus`).
- **scrape_interval**: Defines how frequently Prometheus scrapes the metrics (set to every 15 seconds).
- **static_configs**: Defines the targets, which are the internal Docker host addresses for each service.

---

## Spring Boot Configuration for Prometheus

To expose Spring Boot application metrics for Prometheus, you need to include the following dependencies and
configuration:

### Maven Dependencies

#### Micrometer Registry for Prometheus

```xml

<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

- **Micrometer** is an application metrics library that allows you to instrument your application code to generate
  metrics. It provides a simple way to collect metrics like request timing, memory usage, etc. The **Micrometer Registry
  for Prometheus** connects Micrometer to Prometheus, so Prometheus can scrape the metrics your application exposes.

#### Spring Boot Actuator

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

- **Spring Boot Actuator** provides various production-ready features for Spring Boot applications, including metrics,
  health checks, and monitoring. By enabling the Actuator and adding the `micrometer-registry-prometheus` dependency,
  your application can expose Prometheus-compatible metrics at `/actuator/prometheus`.

### Spring Boot Application Properties

Add the following configurations to your `application.properties` or `application.yml` file to expose the necessary
metrics:

```properties
management.endpoints.web.exposure.include=health,info,metrics,env,beans,prometheus
management.endpoint.health.show-details=always
management.endpoint.env.show-values=always
management.endpoint.metrics.enabled=true
management.metrics.export.prometheus.enabled=true
```

- **`management.endpoints.web.exposure.include`**: Exposes Prometheus metrics along with other endpoints like health,
  info, etc.
- **`management.metrics.export.prometheus.enabled`**: Ensures that Prometheus scraping is enabled.

---

## How to Use

1. **Build and Start the Docker Containers**:
   Run the following command to build and start the containers for Grafana and Prometheus:

   ```bash
   docker-compose up -d
   ```

2. **Access Grafana**:
   Grafana is accessible at [http://localhost:3000](http://localhost:3000).
    - Default username: `admin`
    - Password: `mysecurepassword` (or change as per your configuration)

3. **Access Prometheus**:
   Prometheus can be accessed at [http://localhost:9090](http://localhost:9090).

4. **Add a Data Source in Grafana**:
   Once Grafana is running, add Prometheus as a data source:
    - URL: `http://prometheus:9090`

5. **Create Dashboards**:
   You can now create custom Grafana dashboards to visualize the metrics collected from your Spring Boot applications.

---