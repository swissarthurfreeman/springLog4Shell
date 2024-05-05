FROM maven:3.5.4-jdk-8

RUN mkdir /app
WORKDIR /app
COPY ./pom.xml /app
ADD ./src/ /app/src

RUN mvn package

EXPOSE 8389
EXPOSE 8080

CMD ["mvn", "spring-boot:run"]