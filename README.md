# Beans Backend

## Summary
A basic Spring service that manages all plant, stock, blog, and project data.

## Operation
The service is managed on EC2 using the shared [Release Scripts](https://github.com/basheim/release-scripts) with the
-b identifier.

To build and push a new image, run `sh ./scripts/image_build.sh`.

### Local Run

Environment Variables Required:
* DB_USER
* DB_PASSWORD
* DB_HOST
* DB_PORT
* DB_NAME
* AWS_ACCESS_KEY_ID
* AWS_SECRET_ACCESS_KEY 
* JWT_PUBLIC_PATH
* JWT_PRIVATE_PATH
* JWT_PUB_KEY
* JWT_PRI_KEY

Command line: run `./gradlew bootRun`

Better to set up an intellij gradle configuration to do the same thing with debugging.