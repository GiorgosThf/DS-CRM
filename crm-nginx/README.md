# Nginx Configuration with Caching and Performance Optimization

## Overview

This configuration uses **Nginx** to optimize the performance of an application by leveraging caching, buffering, and
connection handling features. The Nginx server is configured to handle multiple upstream environments (e.g., staging and
production for different regions), provide SSL/TLS encryption, and efficiently handle high traffic by utilizing caching
mechanisms.

### Key Features:

- **Proxy Caching**: Reduce load on upstream servers by caching responses.
- **SSL/TLS**: Secure the application with SSL certificates.
- **HTTP/2**: Faster and more efficient HTTP/2 protocol.
- **Gzip Compression**: Compress responses for faster client delivery.
- **Connection Management**: Efficient handling of many concurrent connections.

---

## Configuration

---
### SSL Setup

To secure your services, you can generate SSL certificates using OpenSSL. Here is how you can create self-signed certificates:

1. Open a command prompt or if you don't have openssl installed you can do it through Git Bash.
2. Navigate to the directory where you would like your certificates to be generated.
3. Generate the certificates using the following command:

```bash
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout ./nginx/ssl/nginx.key -out ./nginx/ssl/nginx.crt

```

This will create the `nginx.crt` and `nginx.key` files, which can be used for SSL setup in NGINX.

---
### Nginx Server Settings

#### Worker and Event Settings

The worker processes and event handling settings are optimized for high-performance environments:

```nginx
worker_processes auto;  # Automatically set based on available CPU cores

events {
    worker_connections 4096;  # Increase connections for high-traffic environments
    use epoll;  # Use epoll for Linux for efficient event handling
    multi_accept on;  # Accept multiple connections at once
}
```

#### HTTP Block Settings

This section includes basic HTTP server settings for logging, connection handling, caching, compression, and security.

```nginx
http {
    include       mime.types;
    default_type  application/octet-stream;

    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    sendfile        on;  # Enable efficient file transfers
    keepalive_timeout 65;
    keepalive_requests 1000;  # Allow 1000 requests per connection before closing

    # Gzip Compression for faster delivery
    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml application/xml+rss text/javascript;
    gzip_min_length 1000;
    gzip_comp_level 5;
    gzip_vary on;

    client_max_body_size 10M;  # Limit upload file size to 10MB

    # Rate limiting per IP
    limit_req_zone $binary_remote_addr zone=one:10m rate=30r/s;
    limit_conn_zone $binary_remote_addr zone=addr:10m;

    # Proxy Cache Settings
    proxy_cache_path /var/cache/nginx levels=1:2 keys_zone=my_cache:10m max_size=1g inactive=60m use_temp_path=off;
    proxy_buffering on;
    proxy_buffers 16 4k;
    proxy_buffer_size 8k;
}
```

### Proxy Cache

Nginx is configured to cache upstream server responses, reducing the load and improving response times. The cache stores
valid responses for a specified duration.

```nginx
proxy_cache_path /var/cache/nginx levels=1:2 keys_zone=my_cache:10m max_size=1g inactive=60m use_temp_path=off;
```

- **`levels=1:2`**: Cache stored in subdirectories for efficient storage.
- **`keys_zone=my_cache:10m`**: Shared memory for cache keys (10MB).
- **`max_size=1g`**: Cache limited to 1GB.
- **`inactive=60m`**: Entries not accessed in 60 minutes are removed.

### SSL/TLS Configuration

The application is served securely via HTTPS, with SSL certificates placed in `/etc/nginx/ssl/`:

```nginx
server {
    listen 443 ssl http2;  # Enable HTTP/2 for improved performance
    server_name localhost;

    ssl_certificate /etc/nginx/ssl/nginx.crt;
    ssl_certificate_key /etc/nginx/ssl/nginx.key;
}
```

### Proxy Settings for Upstream Services

The application forwards requests to different upstream services (staging and production environments) for various
regions. Caching is enabled for responses, and the cache status is provided via the `X-Cache-Status` header.

#### Example Location Block for Staging (GR):

```nginx
location /staging-gr/ {
    rewrite ^/staging-gr(/.*)$ $1 break;  # Strip prefix
    proxy_pass http://host.docker.internal:8081/;
    proxy_cache my_cache;
    proxy_cache_valid 200 1h;  # Cache successful responses for 1 hour
    proxy_cache_valid 404 1m;  # Cache 404 responses for 1 minute
    add_header X-Cache-Status $upstream_cache_status;
    proxy_cache_bypass $http_cache_control;  # Bypass cache if Cache-Control header exists
}
```

#### Similar Configuration for Other Locations:

```nginx
location /production-gr/ { ... }
location /staging-cy/ { ... }
location /production-cy/ { ... }
```

---

## Performance Features

### Proxy Buffering and Caching

- **`proxy_buffering on`**: Buffers responses to reduce load on upstream servers and optimize client delivery.
- **`proxy_cache`**: Caches upstream responses, improving response times and reducing repeated requests to upstream
  servers.

### Gzip Compression

- **`gzip on`**: Compresses HTTP responses for faster delivery to clients.
- **`gzip_comp_level 5`**: A moderate compression level (higher compression levels increase CPU usage).

### Rate Limiting and Connection Limits

- **`limit_req_zone`**: Limits requests per second (30 requests/second per IP).
- **`limit_conn_zone`**: Limits the number of concurrent connections per IP.

---

## How to Use

1. **Edit Nginx Configuration**:
    - Place your SSL certificates in `/etc/nginx/ssl/`.
    - Adjust proxy settings (e.g., upstream server addresses) as needed for your environment.

2. **Test Configuration**:
   Test the Nginx configuration to ensure there are no syntax errors:
   ```bash
   sudo nginx -t
   ```

3. **Reload Nginx**:
   After editing the configuration, reload Nginx to apply changes:
   ```bash
   sudo nginx -s reload
   ```

---

## Troubleshooting

### Viewing Cache Status

To see if a response is served from the cache, check the `X-Cache-Status` header in the HTTP response:

- **HIT**: The response was served from the cache.
- **MISS**: The response was fetched from the upstream server.

### Logs

Access logs and error logs can be found in the default log location (`/var/log/nginx/`):

- **Access Log**: `/var/log/nginx/access.log`
- **Error Log**: `/var/log/nginx/error.log`

---

## Dockerfile for Nginx

```Dockerfile
# Use the official NGINX image
FROM nginx:alpine

# Create the directory for SSL certificates inside the container
RUN mkdir -p /etc/nginx/ssl

# Copy SSL certificates from the local machine to the image
COPY ./ssl/nginx.crt /etc/nginx/ssl/nginx.crt
COPY ./ssl/nginx.key /etc/nginx/ssl/nginx.key

# Copy custom Nginx configuration if needed
COPY nginx.conf /etc/nginx/nginx.conf

# Expose port 443 for HTTPS traffic
EXPOSE 443

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
```

## Docker Compose Configuration

```yaml
version: '3.8'
services:
   nginx:
      image: nginx-proxy
      container_name: nginx-crm-proxy
      build:
         context: .
         dockerfile: Dockerfile
      ports:
         - "443:443"
      networks:
         - crm-network
      restart: always

networks:
   crm-network:
      driver: bridge
```

---

