FROM openjdk:alpine
MAINTAINER simondmansson@gmail.com
COPY /src/main/resources/db.txt /home/src/main/resources/db.txt
COPY /target/bradgardenAPI-1.0-SNAPSHOT.jar /home/bradgardenAPI-1.0-SNAPSHOT.jar
RUN java -jar /home/bradgardenAPI-1.0-SNAPSHOT.jar
