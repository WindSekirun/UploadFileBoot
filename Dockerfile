FROM openjdk:8-jdk-alpine

RUN apk update && apk add bash
ADD build/libs/*.jar uploadfileboot.jar
ENTRYPOINT ["java", "-jar", "uploadfileboot.jar"]