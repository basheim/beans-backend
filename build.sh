#!/bin/bash
touch .env
env | grep AWS_ACCESS_KEY_ID > .env
env | grep AWS_SECRET_ACCESS_KEY >> .env
env | grep DB_PASSWORD >> .env
env | grep DB_USER >> .env

docker build . -t 796569311964.dkr.ecr.us-west-2.amazonaws.com/beans-backend