FROM openjdk:16-alpine3.13

RUN mkdir /app

COPY ./humanmicroservice.jar /app
WORKDIR /app

CMD ["java" , "-jar", "humanmicroservice.jar"]
