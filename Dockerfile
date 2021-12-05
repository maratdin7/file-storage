FROM gradle:jdk11 AS build

ENV APP_HOME=/usr/file-storage
WORKDIR $APP_HOME
COPY . $APP_HOME/
#COPY gradle $APP_HOME/gradle

RUN gradle build

FROM openjdk:11
ENV APP_HOME=/usr/file-storage
COPY --from=build /usr/file-storage/build/libs/file-storage-0.0.1-plain.jar .
ENTRYPOINT ["java","-jar","file-storage-0.0.1-plain.jar"]
