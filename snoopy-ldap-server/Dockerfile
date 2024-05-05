FROM maven:3.5.4-jdk-8

RUN mkdir /app
WORKDIR /app

COPY pom.xml /app
ADD src /app/src

RUN mvn clean compile assembly:single

EXPOSE 1389

CMD ["java", "-jar", "target/biscuits-ldap-server-1.0-SNAPSHOT-jar-with-dependencies.jar"]