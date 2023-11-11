#!/bin/sh

docker-compose stop composite
docker-compose stop user
docker-compose stop project

docker rm teamone-composite
docker rm teamone-user
docker rm teamone-project

docker rmi teamone-back-composite:latest
docker rmi teamone-back-user:latest
docker rmi teamone-back-project:latest

./gradlew clean
./gradlew build

docker-compose up -d