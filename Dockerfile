# Используем официальный образ Java как базовый
FROM openjdk:17-jdk-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Создаем директорию для хранения изображений внутри контейнера
RUN mkdir -p /app/images

# Копируем JAR-файл вашего проекта в контейнер
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт, на котором работает ваше приложение
EXPOSE 8080

# Команда для запуска JAR-файла
ENTRYPOINT ["java", "-jar", "app.jar"]
