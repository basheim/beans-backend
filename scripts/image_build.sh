#!/bin/bash

docker build . -t basheim/beans-backend
docker push basheim/beans-backend:latest