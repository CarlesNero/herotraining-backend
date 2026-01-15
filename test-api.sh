#!/bin/bash

# Script para probar el API de Base Login Backend
# Cambiar la URL según tu servidor

API_URL="http://localhost:8082"
CONTENT_TYPE="Content-Type: application/json"

echo "======================================"
echo "Base Login Backend - API Test Script"
echo "======================================"

# 1. Health Check
echo -e "\n1. Verificar salud del servidor..."
curl -X GET "$API_URL/api/public/health" \
  -H "$CONTENT_TYPE" \
  -w "\nHTTP Status: %{http_code}\n"

# 2. Welcome
echo -e "\n\n2. Obtener mensaje de bienvenida..."
curl -X GET "$API_URL/api/public/welcome" \
  -H "$CONTENT_TYPE" \
  -w "\nHTTP Status: %{http_code}\n"

# 3. Registro de usuario
echo -e "\n\n3. Registrar nuevo usuario..."
REGISTER_RESPONSE=$(curl -X POST "$API_URL/api/auth/register" \
  -H "$CONTENT_TYPE" \
  -d '{
    "username": "johndoe",
    "email": "john@example.com",
    "password": "password123",
    "confirmPassword": "password123",
    "firstName": "John",
    "lastName": "Doe"
  }' \
  -w "\nHTTP Status: %{http_code}\n")

echo "$REGISTER_RESPONSE"

# Extraer el token del registro (opcional para pruebas posteriores)
# TOKEN=$(echo "$REGISTER_RESPONSE" | grep -o '"token":"[^"]*' | cut -d'"' -f4)

# 4. Login
echo -e "\n\n4. Login con usuario..."
LOGIN_RESPONSE=$(curl -X POST "$API_URL/api/auth/login" \
  -H "$CONTENT_TYPE" \
  -d '{
    "usernameOrEmail": "johndoe",
    "password": "password123"
  }' \
  -w "\nHTTP Status: %{http_code}\n")

echo "$LOGIN_RESPONSE"

# Extraer el token para las siguientes pruebas
# Nota: Esto es un ejemplo, en producción usar herramientas como jq
TOKEN=$(echo "$LOGIN_RESPONSE" | grep -oP '"token":"\K[^"]*' | head -1)

if [ -z "$TOKEN" ]; then
  echo "No se pudo extraer el token. Usando valor de ejemplo..."
  TOKEN="example-token"
fi

# 5. Validar token
echo -e "\n\n5. Validar token JWT..."
curl -X GET "$API_URL/api/auth/validate" \
  -H "$CONTENT_TYPE" \
  -H "Authorization: Bearer $TOKEN" \
  -w "\nHTTP Status: %{http_code}\n"

# 6. Intentar registro con usuario duplicado
echo -e "\n\n6. Intentar registrar usuario duplicado (debe fallar)..."
curl -X POST "$API_URL/api/auth/register" \
  -H "$CONTENT_TYPE" \
  -d '{
    "username": "johndoe",
    "email": "john2@example.com",
    "password": "password123",
    "confirmPassword": "password123",
    "firstName": "Jane",
    "lastName": "Doe"
  }' \
  -w "\nHTTP Status: %{http_code}\n"

# 7. Intentar login con contraseña incorrecta
echo -e "\n\n7. Intentar login con contraseña incorrecta (debe fallar)..."
curl -X POST "$API_URL/api/auth/login" \
  -H "$CONTENT_TYPE" \
  -d '{
    "usernameOrEmail": "johndoe",
    "password": "wrongpassword"
  }' \
  -w "\nHTTP Status: %{http_code}\n"

echo -e "\n\n======================================"
echo "Pruebas completadas"
echo "======================================"
