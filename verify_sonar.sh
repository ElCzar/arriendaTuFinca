#!/bin/bash

# Decrypt the .env file
sops --decrypt --age $(cat $SOPS_AGE_KEY_FILE | grep -oP "public key: \K(.*)") -i .env

# Get the SONAR_KEY
SONAR_KEY=$(cat .env | grep -oP "SONAR_KEY=\K.*")

# Encrypt the .env file
sops --encrypt --age $(cat $SOPS_AGE_KEY_FILE | grep -oP "public key: \K(.*)") -i .env

# Run Maven clean verify and sonar
mvn clean verify sonar:sonar -DSONAR_KEY=$SONAR_KEY

# Clean the variable
unset SONAR_KEY

