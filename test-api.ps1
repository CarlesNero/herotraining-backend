#!/usr/bin/env pwsh

# Script de prueba del API - Base Login Backend

$baseUrl = "http://localhost:8082"

Write-Host "======================================"
Write-Host "Base Login Backend - API Test"
Write-Host "=====================================" -ForegroundColor Green

# 1. Health Check
Write-Host "`n1. Testing Health Endpoint..."
$healthResponse = Invoke-WebRequest -Uri "$baseUrl/api/public/health" -Method GET
Write-Host "✅ Health Check:" $healthResponse.StatusCode
Write-Host $healthResponse.Content | ConvertFrom-Json | ConvertTo-Json

# 2. Welcome
Write-Host "`n2. Testing Welcome Endpoint..."
$welcomeResponse = Invoke-WebRequest -Uri "$baseUrl/api/public/welcome" -Method GET
Write-Host "✅ Welcome:" $welcomeResponse.StatusCode
Write-Host $welcomeResponse.Content

# 3. Register User
Write-Host "`n3. Registering User..."
$registerBody = @{
    username = "johndoe"
    email = "john@example.com"
    password = "password123"
    confirmPassword = "password123"
    firstName = "John"
    lastName = "Doe"
} | ConvertTo-Json

try {
    $registerResponse = Invoke-WebRequest -Uri "$baseUrl/api/auth/register" `
        -Method POST `
        -Headers @{"Content-Type"="application/json"} `
        -Body $registerBody
    
    Write-Host "✅ Registration:" $registerResponse.StatusCode
    $registerData = $registerResponse.Content | ConvertFrom-Json
    Write-Host $registerResponse.Content | ConvertFrom-Json | ConvertTo-Json
    
    # Extract token
    $token = $registerData.data.token
} catch {
    Write-Host "❌ Registration Failed:" $_.Exception.Response.StatusCode
    Write-Host $_.Exception.Response.Content
}

# 4. Login
Write-Host "`n4. Testing Login..."
$loginBody = @{
    usernameOrEmail = "johndoe"
    password = "password123"
} | ConvertTo-Json

try {
    $loginResponse = Invoke-WebRequest -Uri "$baseUrl/api/auth/login" `
        -Method POST `
        -Headers @{"Content-Type"="application/json"} `
        -Body $loginBody
    
    Write-Host "✅ Login:" $loginResponse.StatusCode
    $loginData = $loginResponse.Content | ConvertFrom-Json
    Write-Host $loginResponse.Content | ConvertFrom-Json | ConvertTo-Json
    
    # Extract token
    $token = $loginData.data.token
} catch {
    Write-Host "❌ Login Failed:" $_.Exception.Response.StatusCode
}

# 5. Validate Token
if ($token) {
    Write-Host "`n5. Validating Token..."
    try {
        $validateResponse = Invoke-WebRequest -Uri "$baseUrl/api/auth/validate" `
            -Method GET `
            -Headers @{"Authorization"="Bearer $token"}
        
        Write-Host "✅ Token Validation:" $validateResponse.StatusCode
        Write-Host $validateResponse.Content | ConvertFrom-Json | ConvertTo-Json
    } catch {
        Write-Host "❌ Token Validation Failed:" $_.Exception.Response.StatusCode
    }
} else {
    Write-Host "`n5. ⚠️  No token available for validation"
}

Write-Host "`n======================================"
Write-Host "Tests Completed" -ForegroundColor Green
Write-Host "======================================"
