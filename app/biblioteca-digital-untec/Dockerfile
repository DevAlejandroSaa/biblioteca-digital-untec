# =========================
# Stage 1: Maven + Java 8
# =========================
FROM maven:3.6.3-jdk-8 AS maven

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# =========================
# Stage 2: Tomcat 9 + Java 8
# =========================
FROM tomcat:9.0-jdk8

COPY --from=maven /app/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war

WORKDIR /usr/local/tomcat

EXPOSE 8080

CMD ["catalina.sh", "run"]