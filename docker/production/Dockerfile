FROM clojure:openjdk-14-alpine AS builder

COPY . /app
WORKDIR /app

RUN lein build

FROM openjdk:14

WORKDIR /app

COPY --from=builder /app/target/kajappka-game-service-0.1.0-SNAPSHOT-standalone.jar /app/kajappka-game-service.jar

CMD ["java", "-jar", "kajappka-game-service.jar"]
