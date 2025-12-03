# ===============================
# ETAPA 1 — BUILD COM MAVEN (JAVA 17)
# ===============================
FROM maven:3.9.6-eclipse-temurin-17 AS build

ENV APP_ENV=docker

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM tomcat:10.1-jdk17

RUN apt-get update && \
    apt-get install -y locales && \
    sed -i 's/# pt_BR.UTF-8 UTF-8/pt_BR.UTF-8 UTF-8/' /etc/locale.gen && \
    locale-gen pt_BR.UTF-8

ENV LANG=pt_BR.UTF-8
ENV LANGUAGE=pt_BR:pt
ENV LC_ALL=pt_BR.UTF-8

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Diretório do banco SQLite
RUN mkdir -p /usr/local/tomcat/data && \
    chmod -R 777 /usr/local/tomcat/data

EXPOSE 8080

CMD ["catalina.sh", "run"]

