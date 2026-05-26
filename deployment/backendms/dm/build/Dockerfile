FROM openjdk:16-alpine3.13

RUN mkdir /app

COPY ./devicemicroservice.jar /app
WORKDIR /app

CMD ["java" , "-jar", "devicemicroservice.jar"]
