FROM openjdk:17
RUN mkdir /app
COPY target/filesExporter-0.0.2-SNAPSHOT.jar app/filesExporter.jar
WORKDIR /app
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "filesExporter.jar"]