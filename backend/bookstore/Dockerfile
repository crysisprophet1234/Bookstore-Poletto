FROM openjdk:17-jdk-slim

WORKDIR /bookstore

COPY target/*.jar /bookstore/bookstore.jar

COPY src/main/resources/keystore /bookstore/ssl/

EXPOSE 8443

ENV ACTIVE_PROFILE=prod

CMD ["java", "-jar", "bookstore.jar"]