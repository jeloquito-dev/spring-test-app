#FROM maven:3.8.6-ibmjava-8 as app-package
#
#COPY src/ src/
#COPY pom.xml pom.xml
#RUN mvn -DskipTests=true clean package

FROM openjdk:17-jdk-alpine
#COPY --from=app-package /target/spring-test-app.jar ./
COPY /target/spring-test-app-2.4.3.jar ./
CMD ["java", "-jar", "spring-test-app-2.4.3.jar"]
