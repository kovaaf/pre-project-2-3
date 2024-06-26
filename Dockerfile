FROM maven as MAVEN_BUILD

ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD pom.xml $HOME
RUN mvn verify --fail-never
ADD . $HOME
RUN mvn package


FROM bellsoft/liberica-openjdk-alpine:11.0.23

ARG JAR_FILE=usr/app/target/*.jar

COPY --from=MAVEN_BUILD ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]