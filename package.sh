#!/bin/bash
# This script is used to package the application for distribution.
# It will create the war file and run the docker build command.
# Finally, it will push the image to the Docker Hub.
# It receives the version number as an argument,
# the SPRING_DATASOURCE_URL and SPRING_DATASOURCE_PASSWORD
# Example: ./package.sh 1.0.0 jdbc:mysql://localhost:3306/arrienda_tu_finca password

# Check if the version number was informed
if [ -z "$1" ]; then
  echo "You must inform the version number"
  exit 1
fi

# Check if the SPRING_DATASOURCE_URL was informed if not set default value
if [ -z "$2" ]; then
  SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/arrienda_tu_finca?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
else
  SPRING_DATASOURCE_URL=$2
fi

# Check if the SPRING_DATASOURCE_PASSWORD was informed if not set default value
if [ -z "$3" ]; then
  SPRING_DATASOURCE_PASSWORD="rootpassword"
else
  SPRING_DATASOURCE_PASSWORD=$3
fi

# Create the war file
SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL SPRING_DATASOURCE_PASSWORD=$SPRING_DATASOURCE_PASSWORD ./mvnw clean package

# Build the Docker image
docker build -t elczar/arrienda_tu_finca:$1 -t elczar/arrienda_tu_finca:latest .

# Push the Docker image to the Docker Hub
docker push elczar/arrienda_tu_finca:$1
docker push elczar/arrienda_tu_finca:latest