FROM gradle:jdk21-alpine AS build
COPY --chown=gradle:gradle . /src
WORKDIR /src
RUN gradle build

FROM openjdk:21
RUN mkdir /app
COPY --from=build /src/build/libs/hits-internship-1.jar /app/hits-internship-1.jar

ENTRYPOINT ["java", "-jar", "/app/hits-internship-1.jar"]