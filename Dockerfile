# Build stage
FROM maven:3.8.2-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -Pprod -DskipTests

# Package stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/marketzen-0.1.jar app.jar

# Set environment variables
ENV SPRING_DATASOURCE_URL=jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12750584
ENV SPRING_DATASOURCE_USERNAME=sql12750584
ENV SPRING_DATASOURCE_PASSWORD=5fIL6HRuCb
ENV SPRING_MAIL_HOST=smtp.gmail.com
ENV SPRING_MAIL_PORT=587
ENV SPRING_MAIL_USERNAME=foodchecklistapplication@gmail.com
ENV SPRING_MAIL_PASSWORD=lfbkcyldonasjtjx
ENV SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH=true
ENV SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE=true
ENV RAZORPAY_API_KEY=rzp_test_g7Asu6R25KR73A
ENV RAZORPAY_API_SECRET=TFTVyFpDEjJCbPM5B7RRxGRg
ENV STRIPE_API_KEY=test
ENV COINGECKO_API_KEY=CG-QGao1fbgLokHSJEEPGjC7sRB
ENV GEMINI_API_KEY=AIzaSyD6dAnP4U9GIPwRszeES_rC95YjkC-3OSc


# Expose the port the app runs on
EXPOSE 5454

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
