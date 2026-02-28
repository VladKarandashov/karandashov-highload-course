# Учебный проект для курса Highload Architect

Студент: Карандашов Владислав

vlad.karandashov.tech@gmail.com

## Используемый стек

При разработке сделан акцент на ручной работе с базой данных без использования ORM (JPA/Hibernate).
Но при этом использован классический стэк spring boot, чтобы в следующих лабораторных проводить релевантные замеры.

- Gradle 9
- Java 21
- Spring Boot 4
- JDBC для работы с БД
- HikariCP - connection pool
- PostgreSQL 18
- Liquibase - миграции БД

## Реализованные API

Реализован весь требуемый функционал по спецификации [openapi.json](openapi.json):

- `POST /user/register` — Регистрация нового пользователя.
- `POST /login` — Аутентификация пользователя (получение токена).
- `POST /me` — Проверка авторизации и получение профиля текущего пользователя (требует `Authorization: Bearer <token>`).
- `GET /user/get/{id}` — Получение анкеты пользователя по его идентификатору.
- `GET /user/search` — Префиксный поиск анкет по имени и фамилии (параметры `first_name` и `last_name`).

## Как собрать с помощью Gradle

Для сборки проекта (компиляция кода, запуск тестов, создание JAR-файла) выполните команду из корня проекта:

```bash
./gradlew clean build
```

## Как запустить в Docker Compose

Для запуска приложения вместе с базой данных PostgreSQL используйте Docker Compose.
Сначала соберите проект с помощью gradle (нужно для создания JAR-файла).

В папке [local](local) выполните:
```bash
docker compose -f docker-compose.yml up -d --build
```

- Приложение будет доступно на [http://localhost:8080](http://localhost:8080)
- Настройки подключения к базе данных находятся в файле [local.env](local/local.env)
- Инициализационный скрипт базы данных — [init.sql](local/init.sql)

## Как протестировать

### С помощью Bash-скрипта

В проекте есть готовый скрипт для быстрой проверки основных сценариев (регистрация, логин, получение профиля, поиск):

[test-curl.sh](local/test/test-curl.sh)

```bash
chmod +x test-curl.sh
./test-curl.sh
```

### С помощью Postman коллекции

Вы можете импортировать файл коллекции 
[karandashov-social-network.postman_collection.json](local/test/karandashov-social-network.postman_collection.json) 
в Postman для ручного тестирования API.