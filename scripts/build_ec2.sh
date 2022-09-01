#!/bin/bash
touch .env

echo "AWS_ACCESS_KEY_ID=$(aws secretsmanager get-secret-value --secret-id ec2/linuxuser --query SecretString | jq fromjson | jq -r .key)" > .env
echo "AWS_SECRET_ACCESS_KEY=$(aws secretsmanager get-secret-value --secret-id ec2/linuxuser --query SecretString | jq fromjson | jq -r .secret_key)" >> .env
echo "DB_PASSWORD=$(aws secretsmanager get-secret-value --secret-id prod/beansql --query SecretString | jq fromjson | jq -r .password)" >> .env
echo "DB_USER=$(aws secretsmanager get-secret-value --secret-id prod/beansql --query SecretString | jq fromjson | jq -r .username)" >> .env
echo "API_PASSWORD=$(aws secretsmanager get-secret-value --secret-id prod/api-password --query SecretString | jq fromjson | jq -r .password)" >> .env

docker build . -t beans-backend
docker run --env-file .env -dp 8080:8080 beans-backend:latest