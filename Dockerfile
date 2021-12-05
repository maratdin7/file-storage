FROM gradle:jdk11 AS build
ENV APP_HOME=/usr/file-storage
WORKDIR $APP_HOME
COPY . $APP_HOME/
RUN gradle bootJar

FROM openjdk:11
ENV APP_HOME=/usr/file-storage
ENV JAR_NAME=file-storage-0.0.2.jar
COPY --from=build $APP_HOME/build/libs/$JAR_NAME .
VOLUME /downloads
EXPOSE 8080
CMD java -jar $JAR_NAME 2>log

