ARG RUNTIME_IMG="bellsoft/liberica-openjre-alpine:17"

FROM ${RUNTIME_IMG}
EXPOSE 8080
COPY build/libs/currency-service-0.0.1.jar currency-service-0.0.1.jar
ENTRYPOINT ["java", "-jar", "/currency-service-0.0.1.jar"]