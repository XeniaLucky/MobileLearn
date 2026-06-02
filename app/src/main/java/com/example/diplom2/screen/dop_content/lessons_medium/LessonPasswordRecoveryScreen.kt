package com.example.diplom2.screen.dop_content.lessons_medium

import android.content.Context
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.diplom2.screen.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "LessonPassword"
private const val PREFS_NAME = "password_progress"

// ========== ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ ==========
private fun getPrefs(context: Context, userId: Long) =
    context.getSharedPreferences("${PREFS_NAME}_$userId", Context.MODE_PRIVATE)

private fun saveScore(context: Context, userId: Long, score: Int) {
    try { getPrefs(context, userId).edit().putInt("score", score).apply() }
    catch (e: Exception) { Log.e(TAG, "Ошибка сохранения очков: ${e.message}") }
}

private fun getScore(context: Context, userId: Long): Int {
    return try { getPrefs(context, userId).getInt("score", 0) }
    catch (e: Exception) { Log.e(TAG, "Ошибка чтения очков: ${e.message}"); 0 }
}

private fun saveLessonCompleted(context: Context, userId: Long, completed: Boolean) {
    try { getPrefs(context, userId).edit().putBoolean("completed", completed).apply() }
    catch (e: Exception) { Log.e(TAG, "Ошибка сохранения статуса: ${e.message}") }
}

private fun isLessonCompleted(context: Context, userId: Long): Boolean {
    return try { getPrefs(context, userId).getBoolean("completed", false) }
    catch (e: Exception) { false }
}

private fun isValidEmail(email: String): Boolean {
    return email.contains("@") && email.substringAfter("@", "").contains(".")
}

private fun evaluatePasswordStrength(pwd: String): Pair<String, Float> {
    return when {
        pwd.length >= 12 && pwd.any { it.isDigit() } && pwd.any { it.isLetter() } && pwd.any { !it.isLetterOrDigit() } -> "Очень сильный" to 1f
        pwd.length >= 10 && pwd.any { it.isDigit() } && pwd.any { it.isLetter() } && pwd.any { !it.isLetterOrDigit() } -> "Сильный" to 0.8f
        pwd.length >= 8 && pwd.any { it.isDigit() } && pwd.any { it.isLetter() } -> "Средний" to 0.5f
        pwd.length >= 6 -> "Слабый" to 0.3f
        else -> "Слишком короткий" to 0f
    }
}

// ========== ОСНОВНОЙ ЭКРАН ==========
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonPasswordRecoveryScreen(navController: NavController, userId: Long) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lessonKey = "game_recovery"
    val totalSteps = 9

    val activeLesson = getActiveLessonMedium(context, userId)
    val savedStep = if (activeLesson == lessonKey) getLastStepMedium(context, userId, lessonKey) else 0
    var step by remember { mutableIntStateOf(savedStep) }
    var score by remember { mutableIntStateOf(getScore(context, userId)) }

    LaunchedEffect(Unit) {
        if (savedStep == 0 && isLessonCompleted(context, userId)) {
            score = 0
            saveScore(context, userId, 0)
            saveLessonCompleted(context, userId, false)
        }
        activateLessonMedium(context, userId, lessonKey)
    }

    LaunchedEffect(step) {
        val progress = if (step >= totalSteps - 1) 1f else step.toFloat() / (totalSteps - 1)
        updateLessonProgressMedium(context, userId, progress)
        saveLastStepMedium(context, userId, lessonKey, step)
    }
    LaunchedEffect(score) { saveScore(context, userId, score) }

    var savedRecoveryEmail by rememberSaveable { mutableStateOf("") }
    var savedPhoneNumber by rememberSaveable { mutableStateOf("") }
    var savedCode by rememberSaveable { mutableStateOf("") }
    var savedNewPassword by rememberSaveable { mutableStateOf("") }
    var savedUse2FA by rememberSaveable { mutableStateOf(false) }

    var theory1Revealed by rememberSaveable { mutableStateOf(false) }
    var theory2Revealed by rememberSaveable { mutableStateOf(false) }
    var theory3Revealed by rememberSaveable { mutableStateOf(false) }
    var theory1BonusAdded by rememberSaveable { mutableStateOf(false) }
    var theory2BonusAdded by rememberSaveable { mutableStateOf(false) }
    var theory3BonusAdded by rememberSaveable { mutableStateOf(false) }

    var situation1Revealed by rememberSaveable { mutableStateOf(false) }
    var situation2Revealed by rememberSaveable { mutableStateOf(false) }
    var situation3Revealed by rememberSaveable { mutableStateOf(false) }
    var situation4Revealed by rememberSaveable { mutableStateOf(false) }

    var canResendCode by remember { mutableStateOf(true) }
    var resendTimer by remember { mutableIntStateOf(0) }
    LaunchedEffect(resendTimer) {
        if (resendTimer > 0) {
            delay(1000)
            resendTimer--
        } else {
            canResendCode = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Восстановление пароля", color = Color.White, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (step > 0) step--
                        else navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2C5F6E))
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (step) {
                0 -> TheoryPart1Screen(
                    revealed = theory1Revealed,
                    bonusAdded = theory1BonusAdded,
                    onReveal = {
                        if (!theory1Revealed) {
                            theory1Revealed = true
                            if (!theory1BonusAdded) { score += 15; theory1BonusAdded = true }
                        }
                    },
                    onComplete = { step = 1 }
                )
                1 -> TheoryPart2Screen(
                    revealed = theory2Revealed,
                    bonusAdded = theory2BonusAdded,
                    onReveal = {
                        if (!theory2Revealed) {
                            theory2Revealed = true
                            if (!theory2BonusAdded) { score += 15; theory2BonusAdded = true }
                        }
                    },
                    onComplete = { step = 2 }
                )
                2 -> TheoryPart3Screen(
                    revealed = theory3Revealed,
                    bonusAdded = theory3BonusAdded,
                    onReveal = {
                        if (!theory3Revealed) {
                            theory3Revealed = true
                            if (!theory3BonusAdded) { score += 15; theory3BonusAdded = true }
                        }
                    },
                    onComplete = { step = 3 }
                )
                3 -> DifficultSituationsScreen(
                    situation1 = situation1Revealed,
                    situation2 = situation2Revealed,
                    situation3 = situation3Revealed,
                    situation4 = situation4Revealed,
                    onReveal1 = { if (!situation1Revealed) { situation1Revealed = true; score += 10 } },
                    onReveal2 = { if (!situation2Revealed) { situation2Revealed = true; score += 10 } },
                    onReveal3 = { if (!situation3Revealed) { situation3Revealed = true; score += 10 } },
                    onReveal4 = { if (!situation4Revealed) { situation4Revealed = true; score += 10 } },
                    onComplete = { step = 4 }
                )
                4 -> RecoveryDataScreen(
                    recoveryEmail = savedRecoveryEmail,
                    onRecoveryEmailChange = { savedRecoveryEmail = it },
                    phoneNumber = savedPhoneNumber,
                    onPhoneNumberChange = { savedPhoneNumber = it },
                    onNext = { score += 15; step = 5 }
                )
                5 -> CodeVerificationScreen(
                    code = savedCode,
                    onCodeChange = { savedCode = it },
                    canResendCode = canResendCode,
                    resendTimer = resendTimer,
                    onResendCode = {
                        canResendCode = false
                        resendTimer = 30
                    },
                    onVerify = { isSuccess ->
                        if (isSuccess) { score += 20; step = 6 }
                        else { score = (score - 5).coerceAtLeast(0) }
                    }
                )
                6 -> NewPasswordScreen(
                    newPassword = savedNewPassword,
                    onNewPasswordChange = { savedNewPassword = it },
                    onSave = { bonus -> score += bonus; step = 7 }
                )
                7 -> TwoFactorScreen(
                    use2FA = savedUse2FA,
                    onUse2FAChange = { savedUse2FA = it },
                    onComplete = {
                        if (savedUse2FA) score += 25
                        step = 8
                    }
                )
                8 -> FinalScreen(
                    score = score,
                    onFinish = {
                        scope.launch {
                            saveLessonProgress(context, userId, lessonKey, true)
                            saveLessonCompleted(context, userId, true)
                            navController.popBackStack()
                        }
                    }
                )
            }
        }
    }
}

// ========== ШАГ 0: ТЕОРИЯ 1 – Зачем нужны пароли ==========
@Composable
private fun TheoryPart1Screen(
    revealed: Boolean,
    bonusAdded: Boolean,
    onReveal: () -> Unit,
    onComplete: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("🔐 Почему важен надёжный пароль?", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Пароль – это ключ к вашему цифровому дому. Если он слишком простой или потерян, мошенники могут украсть личные данные, фотографии, доступ к банку.\n\n" +
                                "✅ **Хороший пароль:** длинный (от 10 символов), содержит буквы (заглавные и строчные), цифры и спецсимволы (!@#$%).\n" +
                                "❌ **Плохие пароли:** 123456, qwerty, password, дата рождения.\n" +
                                "🔁 **Никогда не используйте один пароль везде!**",
                        fontSize = 17.sp, lineHeight = 26.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedVisibility(visible = revealed) {
                        Column {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    "🔍 **Почему «123456» – плохой пароль?**\n" +
                                            "Хакеры используют специальные программы, которые за секунды перебирают самые частые комбинации. «123456» и «password» находятся в топе списка самых популярных (и самых ненадёжных) паролей.\n\n" +
                                            "🔐 **Как придумать сложный, но запоминающийся пароль?**\n" +
                                            "• Возьмите фразу, например: «Моя кошка любит рыбу!»\n" +
                                            "• Превратите её в пароль: MoyaKoshkaLybitRibu!\n" +
                                            "• Добавьте цифры и символы: M0yaK0shkaLybitRibu!",
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("✅ +15 очков за изучение!", fontSize = 14.sp, color = Color(0xFF2E8058))
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (!revealed) {
                            Button(
                                onClick = onReveal,
                                modifier = Modifier.weight(1f).height(52.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("📖 Узнать больше", fontSize = 18.sp, color = Color.White)
                            }
                        }
                        Button(
                            onClick = onComplete,
                            modifier = Modifier.weight(1f).height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)), // всегда яркий
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Далее", fontSize = 18.sp, color = Color.White)
                        }
                    }
                    if (!revealed) {
                        Text("Можно сразу перейти дальше, но за изучение +15 очков", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
        }
    }
}

// ========== ШАГ 1: ТЕОРИЯ 2 – Как восстановить пароль ==========
@Composable
private fun TheoryPart2Screen(
    revealed: Boolean,
    bonusAdded: Boolean,
    onReveal: () -> Unit,
    onComplete: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("🔄 Как восстановить забытый пароль?", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Если вы забыли пароль – не паникуйте! В большинстве сервисов есть функция «Забыли пароль?». Вот что нужно делать:\n\n" +
                                "1️⃣ Нажмите «Забыли пароль?» на экране входа.\n" +
                                "2️⃣ Введите резервный email или номер телефона, привязанные к аккаунту.\n" +
                                "3️⃣ Получите код подтверждения (по SMS или email).\n" +
                                "4️⃣ Введите код и задайте новый надёжный пароль.\n\n" +
                                "⚠️ **Если у вас включена двухфакторная аутентификация (2FA), потребуется ещё и код из приложения-аутентификатора.**",
                        fontSize = 17.sp, lineHeight = 26.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedVisibility(visible = revealed) {
                        Column {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    "🔧 **Что делать, если вы не помните, какой email или телефон привязан?**\n" +
                                            "• Попробуйте все свои почтовые ящики и номера телефонов, которые могли быть у вас раньше.\n" +
                                            "• Зайдите в настройки телефона → Google → Управление аккаунтом → Безопасность → Пароли (здесь могут быть сохранены пароли от сайтов).\n" +
                                            "• Если не получается – обратитесь в службу поддержки сервиса, предоставив документы, подтверждающие вашу личность.",
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("✅ +15 очков за изучение!", fontSize = 14.sp, color = Color(0xFF2E8058))
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (!revealed) {
                            Button(
                                onClick = onReveal,
                                modifier = Modifier.weight(1f).height(52.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("📖 Узнать больше", fontSize = 18.sp, color = Color.White)
                            }
                        }
                        Button(
                            onClick = onComplete,
                            modifier = Modifier.weight(1f).height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Далее", fontSize = 18.sp, color = Color.White)
                        }
                    }
                    if (!revealed) {
                        Text("Можно сразу перейти дальше, но за изучение +15 очков", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
        }
    }
}

// ========== ШАГ 2: ТЕОРИЯ 3 – Двухфакторная аутентификация ==========
@Composable
private fun TheoryPart3Screen(
    revealed: Boolean,
    bonusAdded: Boolean,
    onReveal: () -> Unit,
    onComplete: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("🔐 Что такое двухфакторная аутентификация (2FA)?", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "2FA – это дополнительный уровень защиты. Даже если злоумышленник узнает ваш пароль, он не сможет войти без второго фактора – обычно это одноразовый код из приложения (Google Authenticator, Microsoft Authenticator) или SMS.\n\n" +
                                "✅ **Как включить 2FA в Google:**\n" +
                                "• Зайдите в myaccount.google.com → Безопасность → Двухэтапная аутентификация.\n" +
                                "• Установите приложение-аутентификатор на телефон.\n" +
                                "• Отсканируйте QR-код.\n" +
                                "• **Обязательно сохраните резервные коды на бумаге!**",
                        fontSize = 17.sp, lineHeight = 26.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedVisibility(visible = revealed) {
                        Column {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    "⚠️ **Что делать, если вы потеряли телефон с аутентификатором?**\n" +
                                            "• Используйте резервные коды, которые вы сохранили.\n" +
                                            "• Если кодов нет – обратитесь в службу поддержки, но это может занять несколько дней.\n" +
                                            "• Поэтому резервные коды нужно хранить в надёжном месте (бумажный блокнот, шифрованный файл).\n\n" +
                                            "🔐 **Какие бывают вторые факторы?**\n" +
                                            "• Код из приложения (Google Authenticator) – самый безопасный.\n" +
                                            "• СМС на телефон – удобно, но менее надёжно (сим-карту могут подделать).\n" +
                                            "• Физический ключ (YubiKey) – самый надёжный, но требует покупки.\n" +
                                            "• Отпечаток пальца или Face ID – удобно, но тоже может быть взломано.",
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("✅ +15 очков за изучение!", fontSize = 14.sp, color = Color(0xFF2E8058))
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (!revealed) {
                            Button(
                                onClick = onReveal,
                                modifier = Modifier.weight(1f).height(52.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("📖 Узнать больше", fontSize = 18.sp, color = Color.White)
                            }
                        }
                        Button(
                            onClick = onComplete,
                            modifier = Modifier.weight(1f).height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Далее", fontSize = 18.sp, color = Color.White)
                        }
                    }
                    if (!revealed) {
                        Text("Можно сразу перейти дальше, но за изучение +15 очков", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
        }
    }
}

// ========== ШАГ 3: СЛОЖНЫЕ СИТУАЦИИ (ИСПРАВЛЕНО) ==========
@Composable
private fun DifficultSituationsScreen(
    situation1: Boolean,
    situation2: Boolean,
    situation3: Boolean,
    situation4: Boolean,
    onReveal1: () -> Unit,
    onReveal2: () -> Unit,
    onReveal3: () -> Unit,
    onReveal4: () -> Unit,
    onComplete: () -> Unit
) {
    val allRevealed = situation1 && situation2 && situation3 && situation4
    val situations = listOf(
        "Забыл пароль от почты / телефона" to
                "Если вы не помните пароль от резервной почты или у вас сменился номер телефона:\n" +
                "• Попробуйте восстановить доступ к почте через сервис восстановления (например, ответы на секретные вопросы).\n" +
                "• Обратитесь в поддержку сервиса, к которому хотите получить доступ, с паспортом.\n" +
                "• Если вы привязали несколько контактов, используйте тот, который помните.\n" +
                "• Для Google: accounts.google.com/signin/recovery – нужно ответить на вопросы о вашем аккаунте (когда создали, какие сервисы использовали).",
        "Потерял телефон с двухфакторной аутентификацией" to
                "Если вы потеряли телефон, на котором установлен аутентификатор (Google Authenticator):\n" +
                "• Используйте резервные коды, которые вы сохранили при включении 2FA.\n" +
                "• Если кодов нет, попробуйте восстановить доступ через резервный email или телефон.\n" +
                "• В крайнем случае – обращайтесь в поддержку сервиса (Google, почта, соцсети).\n" +
                "• В будущем храните резервные коды в нескольких местах: бумажный блокнот, зашифрованный файл на компьютере.",
        "Нет доступа к резервным контактам (email и телефон)" to
                "Если у вас нет доступа ни к резервной почте, ни к телефону:\n" +
                "• Попробуйте вспомнить старые пароли – иногда сервисы просят ввести последний запомненный пароль.\n" +
                "• Для Google: используйте форму восстановления (нужно будет ответить на вопросы: дата регистрации, часто используемые сервисы, имена в контактах).\n" +
                "• Для других сервисов – свяжитесь с поддержкой, предоставьте скан паспорта.\n" +
                "• Важно: заранее привязывайте несколько способов восстановления (два email, телефон друга).",
        "Аккаунт взломали и сменили пароль" to
                "Если вы заметили, что не можете войти в аккаунт (пароль изменён):\n" +
                "• Немедленно используйте функцию «Забыли пароль?» – часто сервис отправляет ссылку на резервный email или телефон.\n" +
                "• Если злоумышленник сменил и резервные контакты, обратитесь в поддержку с документами.\n" +
                "• Для Google: accounts.google.com/signin/recovery – укажите, что аккаунт взломан.\n" +
                "• После восстановления сразу включите двухфакторную аутентификацию и смените все пароли, которые были схожими."
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("⚠️ Что делать в сложных ситуациях?", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Бывает, что стандартные способы восстановления не работают. Нажмите на каждую ситуацию, чтобы узнать, как поступить.",
                        fontSize = 17.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = listOf(situation1, situation2, situation3, situation4).count { it }.toFloat() / 4,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF2C5F6E)
                    )
                    Text("Изучено ситуаций: ${listOf(situation1, situation2, situation3, situation4).count { it }} из 4", fontSize = 16.sp, color = Color(0xFF2C5F6E))
                }
            }
        }
        item { SituationCard(title = situations[0].first, description = situations[0].second, revealed = situation1, onReveal = onReveal1) }
        item { SituationCard(title = situations[1].first, description = situations[1].second, revealed = situation2, onReveal = onReveal2) }
        item { SituationCard(title = situations[2].first, description = situations[2].second, revealed = situation3, onReveal = onReveal3) }
        item { SituationCard(title = situations[3].first, description = situations[3].second, revealed = situation4, onReveal = onReveal4) }
        item {
            Button(
                onClick = onComplete,
                enabled = allRevealed,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Далее", fontSize = 18.sp, color = Color.White)
            }
            if (!allRevealed) {
                Text("Изучите все ситуации, чтобы продолжить (каждая даёт +10 очков)", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SituationCard(title: String, description: String, revealed: Boolean, onReveal: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { if (!revealed) onReveal() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (revealed) Color(0xFFC8E6C9) else Color.White)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                if (revealed) Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2E8058), modifier = Modifier.size(28.dp))
                else Icon(Icons.Default.ArrowForward, null, tint = Color(0xFF2C5F6E))
            }
            if (revealed) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(description, fontSize = 15.sp, lineHeight = 22.sp)
            }
        }
    }
}

// ========== ШАГ 4: ВВОД ДАННЫХ ДЛЯ ВОССТАНОВЛЕНИЯ ==========
@Composable
private fun RecoveryDataScreen(
    recoveryEmail: String,
    onRecoveryEmailChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    onNext: () -> Unit
) {
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("📧 Укажите резервные контакты", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Для восстановления пароля сервис должен знать, куда отправить код. Укажите email или номер телефона, привязанные к аккаунту.",
                    fontSize = 16.sp, lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF2C5F6E))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = recoveryEmail,
            onValueChange = { onRecoveryEmailChange(it); errorMessage = null },
            label = { Text("Резервный email (например, ivan@mail.ru)") },
            isError = errorMessage != null && errorMessage!!.contains("email"),
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { onPhoneNumberChange(it); errorMessage = null },
            label = { Text("Номер телефона (например, +79123456789)") },
            isError = errorMessage != null && errorMessage!!.contains("телефон"),
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )
        if (errorMessage != null) {
            Text(errorMessage!!, fontSize = 14.sp, color = Color( 0xFF9B0C3F))
        }

        Button(
            onClick = {
                when {
                    recoveryEmail.isBlank() && phoneNumber.isBlank() ->
                        errorMessage = "Укажите хотя бы один контакт (email или телефон)"
                    recoveryEmail.isNotBlank() && !isValidEmail(recoveryEmail) ->
                        errorMessage = "Введите корректный email (например, name@mail.ru)"
                    phoneNumber.isNotBlank() && !phoneNumber.replace(Regex("[^0-9]"), "").matches(Regex("\\d{10,11}")) ->
                        errorMessage = "Введите корректный номер телефона (10-11 цифр)"
                    else -> onNext()
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Далее", fontSize = 18.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ========== ШАГ 5: КОД ПОДТВЕРЖДЕНИЯ ==========
@Composable
private fun CodeVerificationScreen(
    code: String,
    onCodeChange: (String) -> Unit,
    canResendCode: Boolean,
    resendTimer: Int,
    onResendCode: () -> Unit,
    onVerify: (Boolean) -> Unit
) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("📲 Введите код подтверждения", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "На указанный контакт пришёл 6-значный код. В реальной жизни он приходит по SMS или email.\n\n**Тестовый код: 123456**",
                    fontSize = 16.sp, lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Icon(Icons.Default.Sms, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF2C5F6E))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = code,
            onValueChange = { onCodeChange(it); errorMessage = null },
            label = { Text("Код из сообщения") },
            isError = errorMessage != null,
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Verified, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )
        if (errorMessage != null) {
            Text(errorMessage!!, fontSize = 14.sp, color = Color( 0xFF9B0C3F))
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    if (code == "123456") {
                        onVerify(true)
                    } else {
                        errorMessage = "Неверный код. Попробуйте ещё раз."
                        onVerify(false)
                    }
                },
                modifier = Modifier.weight(1f).height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                shape = RoundedCornerShape(16.dp)
            ) { Text("Подтвердить", fontSize = 18.sp, color = Color.White) }

            Button(
                onClick = {
                    if (canResendCode) {
                        onResendCode()
                        android.widget.Toast.makeText(context, "Новый код отправлен", android.widget.Toast.LENGTH_SHORT).show()
                    } else {
                        errorMessage = "Подождите ${resendTimer} секунд"
                    }
                },
                modifier = Modifier.weight(1f).height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E).copy(alpha = 0.7f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    if (canResendCode) "Отправить повторно" else "Повторно через ${resendTimer}с",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ========== ШАГ 6: НОВЫЙ ПАРОЛЬ С ИНДИКАТОРОМ СЛОЖНОСТИ ==========
@Composable
private fun NewPasswordScreen(
    newPassword: String,
    onNewPasswordChange: (String) -> Unit,
    onSave: (Int) -> Unit
) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val (strengthText, strengthProgress) = evaluatePasswordStrength(newPassword)
    val bonus = when (strengthText) {
        "Очень сильный" -> 40
        "Сильный" -> 30
        "Средний" -> 15
        else -> 5
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("🔐 Придумайте новый пароль", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Пароль должен быть:\n• Не менее 8 символов (лучше 12+)\n• Содержать буквы (заглавные и строчные), цифры и спецсимволы (!@#$%)\n• Отличаться от предыдущих паролей",
                    fontSize = 16.sp, lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF2C5F6E))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = newPassword,
            onValueChange = { onNewPasswordChange(it); errorMessage = null },
            label = { Text("Новый пароль") },
            isError = errorMessage != null,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )
        if (newPassword.isNotEmpty()) {
            Column {
                LinearProgressIndicator(
                    progress = strengthProgress,
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = when (strengthText) {
                        "Очень сильный", "Сильный" -> Color(0xFF2E8058)
                        "Средний" -> Color(0xFFFFA500)
                        else -> Color( 0xFF9B0C3F)
                    }
                )
                Text(
                    "Сложность: $strengthText (бонус +$bonus очков)",
                    fontSize = 14.sp,
                    color = when (strengthText) {
                        "Очень сильный", "Сильный" -> Color(0xFF2E8058)
                        "Средний" -> Color(0xFFFFA500)
                        else -> Color( 0xFF9B0C3F)
                    }
                )
            }
        }
        if (errorMessage != null) {
            Text(errorMessage!!, fontSize = 14.sp, color = Color( 0xFF9B0C3F))
        }

        Button(
            onClick = {
                when {
                    newPassword.length < 6 -> errorMessage = "Пароль слишком короткий (минимум 6 символов)"
                    newPassword.length < 10 && !newPassword.any { it.isDigit() } -> errorMessage = "Пароль должен содержать хотя бы одну цифру"
                    newPassword.length >= 10 && !newPassword.any { it.isLetter() && it.isUpperCase() } -> errorMessage = "Добавьте хотя бы одну заглавную букву"
                    newPassword.length >= 12 && strengthText != "Очень сильный" -> errorMessage = "Для максимальной безопасности используйте спецсимволы (!@#$%)"
                    else -> onSave(bonus)
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Сохранить пароль", fontSize = 18.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ========== ШАГ 7: ДВУХФАКТОРНАЯ АУТЕНТИФИКАЦИЯ ==========
@Composable
private fun TwoFactorScreen(use2FA: Boolean, onUse2FAChange: (Boolean) -> Unit, onComplete: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("🔐 Двухфакторная аутентификация (2FA)", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "2FA добавляет второй уровень защиты. Даже если пароль украдут, без кода из приложения войти не смогут.\n\n" +
                            "✅ **Включите 2FA в настройках аккаунта (Google, Mail, VK и др.).**\n" +
                            "⚠️ **Обязательно сохраните резервные коды на бумаге!**\n" +
                            "Если потеряете телефон, они помогут восстановить доступ.",
                    fontSize = 16.sp, lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Icon(Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF2C5F6E))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Включить двухфакторную аутентификацию", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Switch(
                checked = use2FA,
                onCheckedChange = onUse2FAChange,
                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E))
            )
        }
        if (use2FA) {
            Text("✅ Отлично! Теперь ваш аккаунт защищён надёжнее.", fontSize = 16.sp, color = Color(0xFF2E8058))
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onComplete,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Завершить настройку", fontSize = 18.sp, color = Color.White)
            }
        } else {
            Text("❌ Включите 2FA, чтобы продолжить", fontSize = 16.sp, color = Color( 0xFF9B0C3F))
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ========== ФИНАЛЬНЫЙ ЭКРАН ==========
@Composable
private fun FinalScreen(score: Int, onFinish: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🎉 ПОЗДРАВЛЯЕМ!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Вы научились восстанавливать пароль и защищать аккаунт!", fontSize = 20.sp, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Заработано очков: $score", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("💡 Памятка по безопасности", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "• Используйте менеджер паролей (Bitwarden, Google Password Manager).\n" +
                                        "• Включите двухфакторную аутентификацию везде, где возможно.\n" +
                                        "• Храните резервные коды 2FA на бумаге.\n" +
                                        "• Никогда не повторяйте пароли на разных сайтах.\n" +
                                        "• Регулярно проверяйте список устройств в аккаунте.\n" +
                                        "• Если потеряли телефон – немедленно смените пароли.",
                                fontSize = 17.sp, lineHeight = 26.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = onFinish,
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Завершить", fontSize = 20.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}