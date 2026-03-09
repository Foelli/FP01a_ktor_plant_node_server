# ---------- Build stage ----------
FROM gradle:8.10-jdk21-alpine AS build

WORKDIR /home/gradle/src

# copy gradle files first for cache
COPY build.gradle gradle.properties settings.gradle* ./
COPY gradle ./gradle
COPY gradlew ./

RUN chmod +x gradlew

# download dependencies
RUN ./gradlew dependencies --no-daemon || true

# copy project
COPY . .

# build fat jar
RUN ./gradlew buildFatJar --no-daemon

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S ktor && adduser -S ktor -G ktor
USER ktor

EXPOSE 9292

COPY --from=build /home/gradle/src/build/libs/*-all.jar app.jar

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Dfile.encoding=UTF-8"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]