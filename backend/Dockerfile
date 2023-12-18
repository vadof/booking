FROM openjdk:17

WORKDIR /app

COPY target/backend-0.0.1-SNAPSHOT.jar /app/backend.jar

#ENTRYPOINT ["java", "-jar", "backend.jar"]
#ENTRYPOINT ["java", "-jar", "backend.jar"]

CMD java -Dspring.config.location=classpath:/application.properties, file:../app/application.properties -jar backend.jar