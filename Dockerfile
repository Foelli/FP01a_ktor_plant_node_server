# ---- Build stage ----
FROM gradle:8-jdk21 AS build
WORKDIR /home/gradle/project

# Copy wrapper + build scripts first for better caching
COPY --chown=gradle:gradle gradlew .
COPY --chown=gradle:gradle gradle ./gradle
COPY --chown=gradle:gradle settings.gradle* build.gradle* gradle.properties ./

RUN chmod +x gradlew
RUN ./gradlew --no-daemon dependencies || true

# Now copy the rest
COPY --chown=gradle:gradle . .
RUN ./gradlew clean installDist --no-daemon

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /home/gradle/project/build/install /app/install
EXPOSE 8080
CMD ["sh", "-lc", "APP_DIR=$(find /app/install -maxdepth 1 -mindepth 1 -type d | head -n 1) && exec \"$APP_DIR/bin/$(basename \"$APP_DIR\")\""]
