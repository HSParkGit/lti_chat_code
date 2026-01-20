# Build stage
FROM amazoncorretto:17 AS builder

WORKDIR /app

# Timezone setup (optional)
# RUN yum install -y tzdata && \
#     ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
#     echo Asia/Seoul > /etc/timezone

COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

# Run stage
FROM amazoncorretto:17

WORKDIR /app

# Copy built JAR
COPY --from=builder /app/build/libs/*.jar app.jar

# Copy .env file into container
COPY .env.prod .env

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
