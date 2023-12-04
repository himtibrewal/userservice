# Use Java 17
FROM openjdk:17-oracle

#COPY JAR FILE
COPY target/userservice-0.0.1-SNAPSHOT.jar userservice-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/userservice-0.0.1-SNAPSHOT.jar"]