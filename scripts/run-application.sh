#!/bin/bash

echo "-----BEGIN PUBLIC KEY-----" > /app/app.pub
echo "${JWT_PUB_KEY}" | base64 -d  >> /app/app.pub
echo "-----END PUBLIC KEY-----" >> /app/app.pub

echo "-----BEGIN PRIVATE KEY-----" > /app/app.key
echo "${JWT_PRI_KEY}" | base64 -d  >> /app/app.key
echo "-----END PRIVATE KEY-----" >> /app/app.key


export JWT_PUB_KEY="XXXXXXXXXXXXXXXXXXX"
export JWT_PRI_KEY="XXXXXXXXXXXXXXXXXXX"

java -jar /app/spring-boot-application.jar