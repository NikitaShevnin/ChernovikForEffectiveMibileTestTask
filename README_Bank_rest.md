# Система управления банковскими картами

REST API для работы с банковскими картами. Сервис написан на Java (Spring Boot) и использует PostgreSQL. Контейнер с БД запускается через Docker Compose, миграции выполняются Liquibase, безопасность реализована с помощью JWT.

## Запуск приложения

1. Установите Docker и Docker Compose.
2. Поднимите базу данных командой:
   ```bash
   docker-compose up -d
   ```
3. Соберите и запустите приложение:
   ```bash
   mvn spring-boot:run
   ```
   При старте автоматически выполняются Liquibase‑миграции.
4. Откройте [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) для просмотра документации API.

## Получение JWT токена

1. Зарегистрируйте пользователя:
   ```bash
   curl -X POST http://localhost:8080/api/auth/register \
        -H 'Content-Type: application/json' \
        -d '{"username":"user","password":"pass"}'
   ```
2. Выполните вход:
   ```bash
   curl -X POST 'http://localhost:8080/api/auth/login?username=user&password=pass'
   ```
   В ответе будет JSON вида `{"token":"<JWT>"}`. Передавайте значение в заголовке:

   ```
   Authorization: Bearer <JWT>
   ```

## Основные эндпоинты

- `POST /api/cards` – создание карты (только администратор).
- `GET /api/cards` – список всех карт (администратор).
- `GET /api/cards/my` – ваши карты. Поддерживает параметры `page` и `size`.
- `GET /api/cards/{id}` – информация о карте.
- `POST /api/cards/{id}/deposit` – пополнение.
- `POST /api/cards/{id}/withdraw` – списание.
- `POST /api/cards/{id}/block` – запрос на блокировку.
- `POST /api/cards/{id}/activate` – активация карты (администратор).
- `DELETE /api/cards/{id}` – удаление (администратор).
- `GET /api/cards/{id}/balance` – текущий баланс.
- `POST /api/cards/transfer` – перевод между собственными картами.
- Управление пользователями (для администратора): `GET/POST /api/users`, `PUT/DELETE /api/users/{id}`.

Полная спецификация находится в файле [`docs/openapi.yaml`](docs/openapi.yaml) и доступна в Swagger UI.

## Конфигурация

Параметры базы данных можно изменить с помощью переменных окружения:

- `DB_HOST`, `DB_PORT`, `DB_NAME`
- `DB_USER`, `DB_PASSWORD`

По умолчанию приложение слушает порт `8080`.

## Передача секретов

Пароль базы данных и ключ JWT можно передать через переменные окружения или Docker Secrets.

**Через переменные окружения**:

```bash
export POSTGRES_PASSWORD=strongpass
export DB_PASSWORD=$POSTGRES_PASSWORD
export JWT_SECRET=my_jwt_secret
docker-compose up -d
mvn spring-boot:run
```

**Через Docker Secrets** (Docker Swarm):

```bash
echo "strongpass" > db_password.txt
echo "my_jwt_secret" > jwt_secret.txt
docker secret create db_password db_password.txt
docker secret create jwt_secret jwt_secret.txt
POSTGRES_PASSWORD=$(cat db_password.txt) DB_PASSWORD=$(cat db_password.txt) JWT_SECRET=$(cat jwt_secret.txt) \
  docker-compose up -d
mvn spring-boot:run
```

