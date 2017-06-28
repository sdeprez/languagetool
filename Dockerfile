FROM openjdk:8-jdk

COPY ./ ./

CMD ["java", "-cp", "languagetool-server.jar", "org.languagetool.server.HTTPServer", "--port", "8081", "--public"]
