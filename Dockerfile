FROM openjdk:8-jdk

RUN mkdir -p /usr/src/app
COPY ./languagetool-standalone/target/LanguageTool-3.8-SNAPSHOT/LanguageTool-3.8-SNAPSHOT/ /usr/src/app/

WORKDIR /usr/src/app
CMD ["java", "-cp", "languagetool-server.jar", "org.languagetool.server.HTTPServer", "--port", "8081", "--public"]
