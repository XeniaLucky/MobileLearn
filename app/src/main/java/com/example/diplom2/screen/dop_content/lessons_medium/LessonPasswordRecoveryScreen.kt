package com.example.diplom2.screen.dop_content.lessons_medium

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.diplom2.screen.saveLessonProgress
import com.example.diplom2.screen.setActiveLessonMedium
import com.example.diplom2.screen.updateLessonProgressMedium
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import kotlinx.coroutines.launch
import com.example.diplom2.screen.saveLessonProgress
import com.example.diplom2.screen.saveLastStepMedium
import com.example.diplom2.screen.getLastStepMedium
import com.example.diplom2.screen.activateLessonMedium
import com.example.diplom2.screen.getActiveLessonMedium
import com.example.diplom2.screen.updateLessonProgressMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonPasswordRecoveryScreen(navController: NavController, userId: Long) {
    var email by remember { mutableStateOf("") }
    var recoveryEmail by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var canResendCode by remember { mutableStateOf(true) }
    var resendTimer by remember { mutableIntStateOf(0) }
    var passwordStrength by remember { mutableStateOf("") }
    var use2FA by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
        val lessonKey = "game_recovery"
        val totalSteps = 6

    // Восстанавливаем шаг, если урок уже был активен
    var step by remember {
        val activeLesson = getActiveLessonMedium(context, userId)
        if (activeLesson == lessonKey) {
            mutableIntStateOf(getLastStepMedium(context, userId, lessonKey))
        } else {
            mutableIntStateOf(0)
        }
    }

    var brightness by remember { mutableFloatStateOf(0.8f) }
    var sync by remember { mutableStateOf(true) }
    var powerSaver by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Активируем урок (без сброса, если он уже активен)
    LaunchedEffect(Unit) {
        activateLessonMedium(context, userId, lessonKey)
    }

    // Обновляем прогресс и сохраняем шаг при каждом переходе
    LaunchedEffect(step) {
        val progress = if (step >= totalSteps - 1) 1f else step.toFloat() / (totalSteps - 1)
        updateLessonProgressMedium(context, userId, progress)
        saveLastStepMedium(context, userId, lessonKey, step)
    }
    LaunchedEffect(resendTimer) {
        if (resendTimer > 0) {
            delay(1000)
            resendTimer--
        } else {
            canResendCode = true
        }
    }

    fun evaluatePasswordStrength(pwd: String) {
        passwordStrength = when {
            pwd.length < 6 -> "Слишком короткий"
            pwd.length >= 12 && pwd.any { it.isDigit() } && pwd.any { it.isLetter() } && pwd.any { !it.isLetterOrDigit() } -> "Очень сильный"
            pwd.length >= 10 && pwd.any { it.isDigit() } && pwd.any { it.isLetter() } && pwd.any { !it.isLetterOrDigit() } -> "Сильный"
            pwd.length >= 8 && pwd.any { it.isDigit() } && pwd.any { it.isLetter() } -> "Средний"
            else -> "Слабый"
        }
    }

    if (step == 0) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🔐 Восстановление пароля: полное руководство", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Забыли пароль? Не паникуйте! Вот пошаговая инструкция.\n\n" +
                                "📌 **Где хранятся пароли?**\n" +
                                "• В телефоне: настройки → Google → Управление аккаунтом → Безопасность → Пароли.\n" +
                                "• В браузере Chrome: ⋮ → Настройки → Пароли.\n" +
                                "• В специальных приложениях-менеджерах паролей (Bitwarden, 1Password, Google Password Manager).\n\n" +
                                "🔍 **Как найти сохранённый пароль?**\n" +
                                "1. Откройте Настройки → Google → Управление аккаунтом.\n" +
                                "2. Нажмите «Безопасность» → «Пароли».\n" +
                                "3. Подтвердите вход (отпечаток, PIN-код или пароль устройства).\n" +
                                "4. Выберите нужный сайт/приложение и посмотрите пароль.\n\n" +
                                "🔄 **Что делать, если пароль потерян и не сохранился?**\n" +
                                "• Воспользуйтесь функцией «Забыли пароль?» на экране входа.\n" +
                                "• Введите резервный email или номер телефона, указанные при регистрации.\n" +
                                "• Получите код подтверждения и задайте новый пароль.\n\n" +
                                "📧 **Если нет доступа к почте или телефону?**\n" +
                                "• Попробуйте восстановить доступ через службу поддержки сервиса.\n" +
                                "• Для Google: accounts.google.com/signin/recovery (нужно ответить на вопросы).\n" +
                                "• Для соцсетей: обратитесь в поддержку, предоставив данные, подтверждающие владение.\n\n" +
                                "🔐 **Двухфакторная аутентификация (2FA)**\n" +
                                "• Это дополнительный код из приложения-аутентификатора (Google Authenticator, Microsoft Authenticator).\n" +
                                "• ОБЯЗАТЕЛЬНО сохраните резервные коды в надёжном месте (бумажный блокнот, шифрованный файл).\n" +
                                "• Без резервных кодов и доступа к телефону восстановить 2FA почти невозможно.\n\n" +
                                "🗄️ **Менеджеры паролей**\n" +
                                "• Bitwarden (бесплатный, кроссплатформенный)\n" +
                                "• Google Password Manager (встроен в Android и Chrome)\n" +
                                "• 1Password (платный, но очень удобный)\n" +
                                "• Самый безопасный способ: ни один пароль не повторяется, вы запоминаете только один мастер-пароль.\n\n" +
                                "✅ **Задание:** пройдите симуляцию восстановления пароля ниже.",
                        fontSize = 16.sp,
                        lineHeight = 22.sp
                    )
                }
            }
            Button(
                onClick = { step = 1 },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Начать симуляцию", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    if (step == 1) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📧 Укажите данные для восстановления", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Чтобы восстановить пароль, система должна знать, куда отправить код. Укажите резервный email или номер телефона, привязанные к аккаунту.", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(40.dp), tint = Color(0xFF2C5F6E))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = recoveryEmail,
                onValueChange = { recoveryEmail = it; errorMessage = null },
                label = { Text("Резервный email (для восстановления)") },
                isError = errorMessage != null && errorMessage!!.contains("email"),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
            )
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it; errorMessage = null },
                label = { Text("Номер телефона (например, +7 912 345-67-89)") },
                isError = errorMessage != null && errorMessage!!.contains("телефон"),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
            )

            if (errorMessage != null && (errorMessage!!.contains("email") || errorMessage!!.contains("телефон"))) {
                Text(errorMessage!!, color = Color.Red, fontSize = 12.sp)
            }

            Button(
                onClick = {
                    if (recoveryEmail.isBlank() && phoneNumber.isBlank()) {
                        errorMessage = "Укажите хотя бы один контакт (email или телефон)"
                    } else if (recoveryEmail.isNotBlank() && !Pattern.compile(android.util.Patterns.EMAIL_ADDRESS.pattern()).matcher(recoveryEmail).matches()) {
                        errorMessage = "Введите корректный email (например, name@domain.com)"
                    } else if (phoneNumber.isNotBlank() && !phoneNumber.replace(Regex("[^0-9]"), "").matches(Regex("\\d{10,11}"))) {
                        errorMessage = "Введите корректный номер телефона (10-11 цифр)"
                    } else {
                        score += 15
                        errorMessage = null
                        step = 2
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
            ) { Text("Далее", color = Color.White) }

            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(8.dp))
        }
        return
    }

    if (step == 2) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📲 Введите код подтверждения", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("На указанный контакт пришёл код. В реальной жизни он приходит по SMS или email. У нас тестовый код: 123456", fontSize = 14.sp)
                    Icon(Icons.Default.Sms, contentDescription = null, modifier = Modifier.size(40.dp), tint = Color(0xFF2C5F6E))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = code,
                onValueChange = { code = it; errorMessage = null },
                label = { Text("Код подтверждения") },
                isError = errorMessage != null && errorMessage!!.contains("код"),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Verified, contentDescription = null) }
            )

            if (errorMessage != null && errorMessage!!.contains("код")) {
                Text(errorMessage!!, color = Color.Red, fontSize = 12.sp)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        if (code == "123456") {
                            score += 20
                            errorMessage = null
                            step = 3
                        } else {
                            errorMessage = "Неверный код. Попробуйте ещё раз."
                            score = (score - 5).coerceAtLeast(0)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                ) { Text("Подтвердить", color = Color.White) }

                Button(
                    onClick = {
                        if (canResendCode) {
                            canResendCode = false
                            resendTimer = 30
                            Toast.makeText(context, "Новый код отправлен на ${if (recoveryEmail.isNotBlank()) recoveryEmail else phoneNumber}", Toast.LENGTH_SHORT).show()
                        } else {
                            errorMessage = "Подождите ${resendTimer} секунд перед повторной отправкой"
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E).copy(alpha = 0.7f))
                ) { Text(if (canResendCode) "Отправить код повторно" else "Повторно через ${resendTimer}с", color = Color.White) }
            }

            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(8.dp))
        }
        return
    }

    if (step == 3) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🔐 Придумайте новый пароль", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Пароль должен быть:\n• Длиной не менее 8 символов (лучше 12+)\n• Содержать буквы (большие и маленькие), цифры и спецсимволы (!@#$%)\n• Отличаться от старых паролей\n• Не содержать личную информацию (дату рождения, имя)", fontSize = 14.sp)
                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(40.dp), tint = Color(0xFF2C5F6E))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    evaluatePasswordStrength(it)
                    errorMessage = null
                },
                label = { Text("Новый пароль") },
                isError = errorMessage != null && errorMessage!!.contains("пароль"),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) }
            )
            if (passwordStrength.isNotEmpty()) {
                Text(
                    "Сложность пароля: $passwordStrength",
                    color = when (passwordStrength) {
                        "Очень сильный", "Сильный" -> Color.Green
                        "Средний" -> Color(0xFFFFA500)
                        else -> Color.Red
                    },
                    fontSize = 12.sp
                )
            }

            if (errorMessage != null && errorMessage!!.contains("пароль")) {
                Text(errorMessage!!, color = Color.Red, fontSize = 12.sp)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        if (newPassword.length < 6) {
                            errorMessage = "Пароль слишком короткий (минимум 6 символов)"
                        } else if (newPassword.length < 10 && !newPassword.any { it.isDigit() }) {
                            errorMessage = "Пароль должен содержать хотя бы одну цифру"
                        } else if (newPassword.length >= 10 && !newPassword.any { it.isLetter() && it.isUpperCase() }) {
                            errorMessage = "Добавьте хотя бы одну заглавную букву"
                        } else if (newPassword.length >= 12 && passwordStrength != "Очень сильный") {
                            errorMessage = "Для максимальной безопасности используйте спецсимволы (!@#$%)"
                        } else {
                            val bonus = when (passwordStrength) {
                                "Очень сильный" -> 40
                                "Сильный" -> 30
                                "Средний" -> 15
                                else -> 5
                            }
                            score += bonus
                            step = 4
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                ) { Text("Сохранить пароль", color = Color.White) }

                Button(
                    onClick = { step = 2 },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E).copy(alpha = 0.7f))
                ) { Text("Назад", color = Color.White) }
            }

            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(8.dp))
        }
        return
    }

    if (step == 4) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🔐 Двухфакторная аутентификация (2FA)", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("2FA значительно повышает безопасность. Даже если кто-то узнает пароль, без второго фактора (кода из приложения) войти не сможет.\n\nКак включить 2FA для Google аккаунта:\n1. myaccount.google.com → Безопасность → Двухэтапная аутентификация → Включить.\n2. Установите Google Authenticator (или Microsoft Authenticator) на телефон.\n3. Отсканируйте QR-код и введите полученный код.\n4. Обязательно сохраните резервные коды (на бумаге или в надёжном хранилище)!\n\nЧто делать, если телефон с 2FA потерян?\n• Используйте резервные коды (каждый код действует один раз).\n• Если кодов нет – обращайтесь в поддержку сервиса (долго и сложно).\n• Поэтому резервные коды нужно хранить в нескольких местах.\n\nВключите 2FA в симуляции ниже:", fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Включить двухфакторную аутентификацию", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Switch(checked = use2FA, onCheckedChange = { use2FA = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E)))
            }

            if (use2FA) {
                Text("✅ Отлично! Теперь ваш аккаунт защищён намного лучше.", color = Color.Green)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { step = 5 },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                ) { Text("Готово", color = Color.White) }
            } else {
                Text("❌ Включите 2FA, чтобы продолжить", color = Color.Red)
            }

            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(8.dp))
        }
        return
    }

    // Финальный экран
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🎉 Поздравляем! Вы научились восстанавливать пароль и защищать аккаунт.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Вы заработали $score очков.", color = Color(0xFF2C5F6E), fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "💡 ИТОГОВЫЕ СОВЕТЫ:\n" +
                            "• Храните пароли в менеджере паролей (Bitwarden, Google Password Manager).\n" +
                            "• Включите двухфакторную аутентификацию везде, где это возможно.\n" +
                            "• Запишите резервные коды от 2FA на бумаге и храните в сейфе.\n" +
                            "• Не используйте один пароль для разных сервисов.\n" +
                            "• Регулярно проверяйте, какие устройства имеют доступ к вашему аккаунту (в настройках безопасности).\n" +
                            "• Если потеряли телефон – срочно поменяйте пароли и отзовите сеансы в настройках аккаунта.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "game_recovery", true)
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                ) { Text("Завершить", color = Color.White) }
            }
        }
    }
}