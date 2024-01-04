ARG RUNTIME_IMG="bellsoft/liberica-openjre-alpine:17"

FROM ${RUNTIME_IMG}
EXPOSE 8080
COPY build/libs/cb-rf-integration-service-0.0.1.jar cb-rf-integration-service-0.0.1.jar
ENTRYPOINT ["java", "-jar", "/cb-rf-integration-service-0.0.1.jar"]