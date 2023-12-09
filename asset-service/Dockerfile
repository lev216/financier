ARG RUNTIME_IMG="bellsoft/liberica-openjre-alpine:17"

FROM ${RUNTIME_IMG}
EXPOSE 8080
COPY build/libs/asset-service-0.0.1.jar asset-service-0.0.1.jar
ENTRYPOINT ["java", "-jar", "/asset-service-0.0.1.jar"]