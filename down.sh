#!/bin/bash
# This script is used to bring down the network and containers for the testing of the chaincode
docker rm -f arrienda-db
docker rm -f arrienda-api
docker network rm arrienda-network
