FROM openjdk:17-ea-slim-buster
WORKDIR /algafood-api
COPY target/*.jar algafood-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/algafood-api/algafood-api.jar"]