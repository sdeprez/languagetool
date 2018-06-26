FROM openjdk:8-jdk

COPY ./ ./

EXPOSE 8081/tcp

CMD ["java", "-cp", "languagetool-server.jar", "org.languagetool.server.HTTPServer", "--port", "8081", "--public", "--config", "config.properties"]
