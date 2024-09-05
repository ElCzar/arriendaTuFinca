#!/bin/bash

# Decrypt the .env file
sops --decrypt --age $(cat $SOPS_AGE_KEY_FILE | grep -oP "public key: \K(.*)") .env > .decrypted.env

# Get the SONAR_KEY
SONAR_KEY=$(cat .decrypted.env | grep -oP "SONAR_KEY=\K.*")

# Remove the decrypted file
rm .decrypted.env

# Run Maven clean verify and sonar
mvn clean verify sonar:sonar -DSONAR_KEY=$SONAR_KEY

# Clean the variable
unset SONAR_KEY

