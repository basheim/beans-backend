FROM gradle:7.5.1-jdk17-alpine as build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon -x test

FROM openjdk:17-jdk-alpine

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
COPY scripts/run-application.sh /app/run-application.sh

RUN addgroup usergroup
RUN adduser --no-create-home --disabled-password --ingroup usergroup appuser
RUN chown -R appuser:usergroup /app
USER appuser

EXPOSE 8080

ENTRYPOINT ["sh", "/app/run-application.sh"]