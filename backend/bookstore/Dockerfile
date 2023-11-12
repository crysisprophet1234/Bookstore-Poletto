FROM maven:3.8.4 AS builder

WORKDIR /bookstore

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src src

RUN mvn package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /bookstore

COPY --from=builder /bookstore/target/*.jar /bookstore/bookstore.jar

COPY src/main/resources/keystore /bookstore/ssl/

EXPOSE 8443

ENV ACTIVE_PROFILE=prod

CMD ["java", "-jar", "bookstore.jar"]