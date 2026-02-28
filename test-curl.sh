#!/bin/bash

# честно нанейроненный скрипт для тестирования сервиса

# Настройки
BASE_URL="http://localhost:8080"
CONTENT_TYPE="Content-Type: application/json"

echo "=== Тестирование API монолита ==="

# 1. Регистрация пользователя
echo -e "\n1. Регистрация пользователя (/user/register)..."
REGISTER_DATA='{
  "first_name": "Konstantin",
  "second_name": "Osipov",
  "birthdate": "1980-01-01",
  "biography": "Highload developer",
  "city": "Moscow",
  "password": "secret-password"
}'

# Выполняем curl и получаем тело и код (код на последней строке)
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/user/register" -H "$CONTENT_TYPE" -d "$REGISTER_DATA")
BODY=$(echo "$RESPONSE" | sed '$d')
CODE=$(echo "$RESPONSE" | tail -n 1 | tr -d '\r')

echo "Ответ: $BODY"
echo "Код: $CODE"

if [ "$CODE" -ne 200 ]; then
    echo "Ошибка при регистрации!"
    exit 1
fi

# Извлекаем user_id с помощью sed
USER_ID=$(echo "$BODY" | sed -n 's/.*"user_id"[[:space:]]*:[[:space:]]*"\([^"]*\)".*/\1/p')
echo "Получен USER_ID: $USER_ID"

# 2. Логин
echo -e "\n2. Аутентификация (/login)..."
LOGIN_DATA="{\"id\": \"$USER_ID\", \"password\": \"secret-password\"}"

RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/login" -H "$CONTENT_TYPE" -d "$LOGIN_DATA")
BODY=$(echo "$RESPONSE" | sed '$d')
CODE=$(echo "$RESPONSE" | tail -n 1 | tr -d '\r')

echo "Ответ: $BODY"
echo "Код: $CODE"

if [ "$CODE" -ne 200 ]; then
    echo "Ошибка при входе!"
    exit 1
fi

# Извлекаем token с помощью sed
TOKEN=$(echo "$BODY" | sed -n 's/.*"token"[[:space:]]*:[[:space:]]*"\([^"]*\)".*/\1/p')
echo "Получен токен: $TOKEN"

# 3. Проверка /me
echo -e "\n3. Проверка текущего пользователя (/me)..."
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/me" -H "Authorization: Bearer $TOKEN")
BODY=$(echo "$RESPONSE" | sed '$d')
CODE=$(echo "$RESPONSE" | tail -n 1 | tr -d '\r')

echo "Ответ: $BODY"
echo "Код: $CODE"

if [ "$CODE" -ne 200 ]; then
    echo "Ошибка /me!"
    exit 1
fi

# 4. Получение анкеты по ID
echo -e "\n4. Получение анкеты по ID (/user/get/$USER_ID)..."
RESPONSE=$(curl -s -w "\n%{http_code}" -X GET "$BASE_URL/user/get/$USER_ID")
BODY=$(echo "$RESPONSE" | sed '$d')
CODE=$(echo "$RESPONSE" | tail -n 1 | tr -d '\r')

echo "Ответ: $BODY"
echo "Код: $CODE"

if [ "$CODE" -ne 200 ]; then
    echo "Ошибка получения анкеты!"
    exit 1
fi

# 5. Поиск анкет
echo -e "\n5. Поиск анкет (/user/search)..."
RESPONSE=$(curl -s -w "\n%{http_code}" -G "$BASE_URL/user/search" \
  --data-urlencode "first_name=Konst" \
  --data-urlencode "last_name=Osi")
BODY=$(echo "$RESPONSE" | sed '$d')
CODE=$(echo "$RESPONSE" | tail -n 1 | tr -d '\r')

echo "Ответ: $BODY"
echo "Код: $CODE"

if [ "$CODE" -ne 200 ]; then
    echo "Ошибка поиска!"
    exit 1
fi

echo -e "\n=== Тестирование завершено ==="
