FROM openjdk:8-jdk-alpine

ADD build/libs/*.jar uploadfileboot.jar

ENTRYPOINT ["java", "-jar", "uploadfileboot.jar"]