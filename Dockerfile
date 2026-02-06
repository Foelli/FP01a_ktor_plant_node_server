# ---- Build stage ----
FROM gradle:8-jdk21 AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean installDist --no-daemon

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/install /app/install
EXPOSE 8080
CMD ["sh", "-lc", "APP_DIR=$(find /app/install -maxdepth 1 -mindepth 1 -type d | head -n 1) && exec \"$APP_DIR/bin/$(basename \"$APP_DIR\")\""]
