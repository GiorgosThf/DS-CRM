FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY pom.xml ./

RUN mvn dependency:go-offline --fail-never

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17

WORKDIR /app

COPY --from=build /app/target/DS-CRM-1.0.0-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]