FROM adoptopenjdk/openjdk11:alpine-jre

EXPOSE 8080

COPY target/cloudservice-0.0.1-SNAPSHOT.jar cloudservice.jar

CMD ["java", "-jar", "cloudservice.jar"]