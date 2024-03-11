FROM openjdk:17
RUN mkdir /app
COPY /target/crud-0.0.2-SNAPSHOT.jar /app/crud.jar
WORKDIR /app
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "crud.jar"]