# Пример работы с REST API

В этом файле приведены базовые запросы к сервису управления банковскими картами.

## Получение токена

```bash
curl -X POST http://localhost:8080/api/auth/register \
     -H 'Content-Type: application/json' \
     -d '{"username":"user","password":"pass"}'

curl -X POST "http://localhost:8080/api/auth/login?username=user&password=pass"
```

В ответе придёт JSON с токеном. Передавайте его в заголовке `Authorization: Bearer <JWT>`.

## Основные эндпоинты

- `POST /api/cards` – создание карты (администратор);
- `GET /api/cards` – список карт (администратор);
- `GET /api/cards/my` – ваши карты, параметры `page` и `size`;
- `GET /api/cards/{id}` – информация о карте;
- `POST /api/cards/{id}/deposit` – пополнение;
- `POST /api/cards/{id}/withdraw` – списание;
- `POST /api/cards/{id}/block` – запрос блокировки;
- `POST /api/cards/{id}/activate` – активация карты (администратор);
- `DELETE /api/cards/{id}` – удаление (администратор);
- `GET /api/cards/{id}/balance` – текущий баланс;
- `POST /api/cards/transfer` – перевод между собственными картами;
- Управление пользователями: `GET/POST /api/users`, `PUT/DELETE /api/users/{id}`.

Более детальная документация доступна в Swagger UI и в файле `docs/openapi.yaml`.
