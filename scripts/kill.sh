#!/bin/bash
CONTAINER_ID=$(docker ps --filter "name=beans-backend" -q)
docker stop "${CONTAINER_ID}"
docker rm "${CONTAINER_ID}"