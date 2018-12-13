FROM openjdk:8-jre
MAINTAINER Carsten Buchberger <c.buchberger@witcom.de>
ENTRYPOINT ["/usr/bin/java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]
# Add Maven dependencies (not shaded into the artifact; Docker-cached)
ADD target/lib           /app/lib
# Add the service itself
ARG JAR_FILE
ADD target/${JAR_FILE} /app/app.jar