# Fitness Tracker API

## Завдання

Цей проєкт реалізує **Завдання 3: Веб-фітнес трекер (Spring Boot + DB)** - бекенд застосунок на Spring Boot з REST API для ведення обліку тренувань, фітнес-цілей та перегляду статистики.

## Технології

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA (Hibernate)
- Spring Security
- **MySQL**
- BCryptPasswordEncoder
- Lombok
- JUnit 5
- Maven

## Архітектура

Застосунок побудовано за стандартною **трирівневою архітектурою**:
- `Controller` - REST API
- `Service` - бізнес-логіка
- `Repository` - робота з базою даних через JPA

## Моделі (Entities)

- **User** — користувачі (автентифікація, тренування, цілі)
- **Workout** — тренування (тип, тривалість, калорії, дата)
- **Goal** — фітнес-цілі користувача (тип, цільове значення, статус)

## Безпека

- Реалізована базова аутентифікація/авторизація через **Spring Security**
- Реєстрація користувачів
- Захист усіх API-ендпоінтів
- **Паролі шифруються за допомогою BCrypt**

## REST API

### Користувачі `/api/users`

- `POST /api/users/register` - реєстрація нового користувача
- `GET /api/users/{id}` - перегляд профілю користувача
- `GET /api/users/me` - перегляд профілю поточного користувача

### Тренування `/api/workouts`

- `GET /api/workouts` - список усіх тренувань для поточного користувача
- `GET /api/workouts/type/{type}` - список усіх тренувань за типом
- `GET /api/workouts/{id}` - конкретне тренування за id
- `POST /api/workouts` - створити нове тренування
- `PUT /api/workouts/{id}` - оновити тренування за id
- `DELETE /api/workouts/{id}` - видалити тренування за id

### Цілі `/api/goals`

- `GET /api/goals` - список усіх цілей для поточного користувача
- `GET /api/goals/{id}` - конкретна ціль за id
- `POST /api/goals` - створити нову ціль
- `PUT /api/goals/{id}` - оновити ціль за id
- `DELETE /api/goals/{id}` - видалити ціль за id

### Статистика `/api/stats`

- `GET /api/stats/workouts/by-type` - статистика тренувань за типом (тип, кількість, калорії, тривалість)
- `GET /api/stats/progress/calories/days` - статистика по спалених калоріях за днями
- `GET /api/stats/progress/calories/weeks` - статистика по спалених калоріях за тижнями
- `GET /api/stats/progress/calories/months` - статистика по спалених калоріях за місяцями

## Обробка помилок

- Реалізовано глобальну обробку винятків через `@ControllerAdvice`
- API повертає коректні JSON-помилки (наприклад, 404 Not Found, 401 Unauthorized)

## Тестування

- Написано **JUnit тести** для контролерів та сервісів

## Налаштування MySQL

У `application.properties` потрібно вказати доступ до вашої бази:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/fitness_tracker_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```
