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