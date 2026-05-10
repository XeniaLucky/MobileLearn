package com.example.diplom2.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import bd.AppDatabase
import kotlinx.coroutines.launch
import repository.UserRepository

@Composable
fun AuthScreen(onLoginSuccess: (Long) -> Unit) {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val userRepo = UserRepository(db.userDao())
    var isLoginMode by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Создаём тестового пользователя, если его нет (для удобства)
    LaunchedEffect(Unit) {
        scope.launch {
            if (userRepo.login("test@test.com", "123") == null) {
                userRepo.register("test@test.com", "123", "Тестовый", 1)
            }
        }
    }

    // НОВЫЙ ФОН
    val backgroundColor = Color(0xFF505FAC) // фиолетовый
    val contentColor = Color.White
    val accentColor = Color(0xFFCD9E75)    // золотистый (как в других экранах)
    val errorColor = Color(0xFFFFF9C4)     // светло‑жёлтый для ошибок

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)   // фиолетовый фон
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isLoginMode) "Вход" else "Регистрация",
            style = MaterialTheme.typography.headlineMedium,
            color = contentColor            // белый цвет
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = contentColor) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = contentColor,
                unfocusedTextColor = contentColor,
                cursorColor = contentColor,
                focusedBorderColor = contentColor,
                unfocusedBorderColor = contentColor.copy(alpha = 0.5f),
                focusedLabelColor = contentColor,
                unfocusedLabelColor = contentColor.copy(alpha = 0.7f)
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль", color = contentColor) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = contentColor,
                unfocusedTextColor = contentColor,
                cursorColor = contentColor,
                focusedBorderColor = contentColor,
                unfocusedBorderColor = contentColor.copy(alpha = 0.5f),
                focusedLabelColor = contentColor,
                unfocusedLabelColor = contentColor.copy(alpha = 0.7f)
            )
        )
        if (!isLoginMode) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Имя", color = contentColor) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = contentColor,
                    unfocusedTextColor = contentColor,
                    cursorColor = contentColor,
                    focusedBorderColor = contentColor,
                    unfocusedBorderColor = contentColor.copy(alpha = 0.5f),
                    focusedLabelColor = contentColor,
                    unfocusedLabelColor = contentColor.copy(alpha = 0.7f)
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (error != null) {
            Text(
                text = error!!,
                color = errorColor,          // светло‑жёлтый для ошибок
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                scope.launch {
                    try {
                        if (isLoginMode) {
                            val user = userRepo.login(email, password)
                            if (user != null) {
                                onLoginSuccess(user.id)
                            } else {
                                error = "Неверный email или пароль"
                            }
                        } else {
                            if (email.isNotBlank() && password.isNotBlank() && name.isNotBlank()) {
                                val userId = userRepo.register(email, password, name, 1)
                                if (userId != -1L) {
                                    onLoginSuccess(userId)
                                } else {
                                    error = "Ошибка регистрации. Возможно, email уже занят."
                                }
                            } else {
                                error = "Заполните все поля"
                            }
                        }
                    } catch (e: Exception) {
                        error = "Ошибка: ${e.message}"
                        e.printStackTrace()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = accentColor,   // золотистая кнопка
                contentColor = Color(0xFF1C323F) // тёмный текст на кнопке
            )
        ) {
            Text(if (isLoginMode) "Войти" else "Зарегистрироваться")
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { isLoginMode = !isLoginMode }) {
            Text(
                text = if (isLoginMode) "Нет аккаунта? Зарегистрироваться" else "Уже есть аккаунт? Войти",
                color = contentColor.copy(alpha = 0.9f) // белый с лёгкой прозрачностью
            )
        }
    }
}