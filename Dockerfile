FROM openjdk:8-jre
MAINTAINER Carsten Buchberger <c.buchberger@witcom.de>
ENTRYPOINT ["/usr/bin/java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]
USER root
COPY src/main/docker/certs/witca.pem /etc/ssl/certs/java/witca.pem
COPY src/main/docker/certs/intermediateca.pem /etc/ssl/certs/java/intermediateca.pem
RUN \
    cd /etc/ssl/certs/java \
    && keytool -keystore cacerts -storepass changeit -noprompt -trustcacerts -importcert -alias witca -file witca.pem \
    && keytool -keystore cacerts -storepass changeit -noprompt -trustcacerts -importcert -alias intermediateca -file intermediateca.pem
# Add the service itself
ARG JAR_FILE
ADD target/${JAR_FILE} /app/app.jar
