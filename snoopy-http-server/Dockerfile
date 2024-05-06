FROM maven:3.5.4-jdk-8 AS build

RUN mkdir /app
WORKDIR /app

COPY Exploit.java /app
RUN javac Exploit.java

COPY ExploitRCE.java /app
RUN javac ExploitRCE.java

FROM python:3.10.12-slim-bullseye as run

RUN mkdir /app
WORKDIR /app
COPY --from=build /app/Exploit.class /app
COPY --from=build /app/ExploitRCE.class /app

COPY ./server.py /app
EXPOSE 8000

CMD ["python3", "server.py"]