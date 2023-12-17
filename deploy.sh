#!/bin/sh

docker-compose stop composite
docker-compose stop user
docker-compose stop project
docker-compose stop chat
docker-compose stop gateway

docker rm teamone-composite
docker rm teamone-user
docker rm teamone-project
docker rm teamone-chat
docker rm teamone-gateway

docker rmi teamone-back-composite:latest
docker rmi teamone-back-user:latest
docker rmi teamone-back-project:latest
docker rmi teamone-back-chat:latest
docker rmi teamone-back-gateway:latest

./gradlew clean
./gradlew build

docker-compose up -d