FROM openjdk:8

ADD ./target/homeIncomeCalculator-1.0-SNAPSHOT.jar app.jar
ADD ./target/classes /target/classes
Add local-config.conf local-config.conf

RUN apt-get update
RUN apt-get upgrade
RUN apt install sqlite3


RUN mkdir SQL_DB
RUN cd SQL_DB
RUN sqlite3 homeCalDB.db

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]