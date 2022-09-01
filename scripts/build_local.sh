#!/bin/bash
touch .env
env | grep AWS_ACCESS_KEY_ID > .env
env | grep AWS_SECRET_ACCESS_KEY >> .env
env | grep DB_PASSWORD >> .env
env | grep DB_USER >> .env
env | grep API_PASSWORD >> .env

docker build . -t beans-backend