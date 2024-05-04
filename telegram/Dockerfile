FROM openjdk:17
RUN mkdir /app
COPY /target/telegram-0.0.2-SNAPSHOT.jar /app/telegram.jar
WORKDIR /app
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "telegram.jar"]