#!/bin/bash
docker run --env-file .env -dp 8080:8080 beans-backend:latest