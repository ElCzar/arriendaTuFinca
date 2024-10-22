#!/bin/bash
# This script is used to package the application for distribution.
# It will create the war file and run the docker build command.
# Finally, it will push the image to the Docker Hub.
# It receives the version number as an argument,
# the SPRING_DATASOURCE_URL and SPRING_DATASOURCE_PASSWORD
# Example: ./package.sh 1.0.0 jdbc:mysql://localhost:3306/arrienda_tu_finca password

# Check if the version number was informed if not just says is a test
if [ -z "$1" ]; then
  VERSION="test"
else
  VERSION=$1
fi

# Check if the SPRING_DATASOURCE_URL was informed if not set default value
if [ -z "$2" ]; then
  SPRING_DATASOURCE_URL="jdbc:mysql://arrienda-db:3306/arrienda_tu_finca?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
else
  SPRING_DATASOURCE_URL=$2
fi

# Check if the SPRING_DATASOURCE_PASSWORD was informed if not set default value
if [ -z "$3" ]; then
  SPRING_DATASOURCE_PASSWORD="rootpassword"
else
  SPRING_DATASOURCE_PASSWORD=$3
fi

# Check if the SPRING_DATASOURCE_USERNAME was given
if [ -z "$4" ]; then
  SPRING_DATASOURCE_USERNAME="root"
else
  SPRING_DATASOURCE_USERNAME=$4
fi

# Build the Docker image
if VERSION="test"; then
  docker build -t elczar/arrienda_tu_finca:$VERSION .
  docker network create arrienda-network
  docker run --name arrienda-db --network arrienda-network -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=$SPRING_DATASOURCE_PASSWORD -e MYSQL_DATABASE=arrienda_tu_finca mysql:8
  docker run --name arrienda-api --network arrienda-network -d -p 8081:8080 -e SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL -e SPRING_DATASOURCE_USERNAME=$SPRING_DATASOURCE_USERNAME -e SPRING_DATASOURCE_PASSWORD=$SPRING_DATASOURCE_PASSWORD elczar/arrienda_tu_finca:$VERSION
else
  docker build -t elczar/arrienda_tu_finca:$VERSION -t elczar/arrienda_tu_finca:latest .
fi

# Push the Docker image to the Docker Hub
#docker push elczar/arrienda_tu_finca:$1
#docker push elczar/arrienda_tu_finca:latest