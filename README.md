# User Passport Boot Application

Spring Boot приложение для управления пользователями и их паспортными данными.  
Реализована связь один-к-одному между сущностями User и Passport.

---

##  Возможности

- CRUD операции для пользователей и паспортов  
- Связь User ↔ Passport (One-to-One)  
- Валидация и хранение данных в БД  
 - Возможность запуска через Docker  

---

##  Структура проекта

- src/main/java — код приложения (контроллеры, сервисы, репозитории, модели)  
- src/main/resources — конфигурации (`application.properties`, шаблоны, статические ресурсы)  
- Dockerfile — описание сборки контейнера  
- pom.xml — зависимости Maven  

---

##  Технологии

- Java 17+  
- Maven 3+  
- MySQL   
- Docker  
- Spring 6
---



mvn clean install
mvn spring-boot:run
