package com.example.diplom2.screen.dop_content.lessons_medium

import android.content.Context
import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.diplom2.screen.*
import kotlinx.coroutines.launch

private const val TAG = "LessonNotifications"
private const val PREFS_NAME = "notifications_progress"

// ==================== ФУНКЦИИ СОХРАНЕНИЯ ====================
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonNotificationsScreen(navController: NavController, userId: Long) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lessonKey = "game_notifications"
    val totalSteps = 4

    val activeLesson = getActiveLessonMedium(context, userId)
    val savedStep = if (activeLesson == lessonKey) getLastStepMedium(context, userId, lessonKey) else 0
    var step by remember { mutableIntStateOf(savedStep) }
    var score by remember { mutableIntStateOf(getScore(context, userId)) }

    LaunchedEffect(Unit) { activateLessonMedium(context, userId, lessonKey) }
    LaunchedEffect(step) {
        val progress = if (step >= totalSteps - 1) 1f else step.toFloat() / (totalSteps - 1)
        updateLessonProgressMedium(context, userId, progress)
        saveLastStepMedium(context, userId, lessonKey, step)
    }
    LaunchedEffect(score) { saveScore(context, userId, score) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Управление уведомлениями", color = Color.White, fontSize = 18.sp) },
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
                0 -> TheoryScreen(onStart = { step = 1 })
                1 -> DisableNotificationsScreen(
                    score = score,
                    onScoreUpdate = { newScore -> score = newScore },
                    onComplete = { step = 2 }
                )
                2 -> DoNotDisturbScreen(
                    score = score,
                    onScoreUpdate = { newScore -> score = newScore },
                    onComplete = { step = 3 }
                )
                3 -> FinalScreen(
                    score = score,
                    onFinish = {
                        scope.launch {
                            saveLessonProgress(context, userId, lessonKey, true)
                            navController.popBackStack()
                        }
                    }
                )
            }
        }
    }
}

// ==================== ШАГ 0: ТЕОРИЯ (уменьшенные шрифты) ====================
@Composable
private fun TheoryScreen(onStart: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("📱 Управление уведомлениями", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("❓ Что такое уведомления?", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Уведомления — это сообщения, которые приходят на экран телефона. Например, когда кто-то пишет в WhatsApp или приходит смс.\n\n" +
                                "✅ **Полезные уведомления** (их нужно оставлять):\n" +
                                "   • Сообщения от родных и друзей\n" +
                                "   • Напоминания о лекарствах / встречах\n" +
                                "   • СМС от банка о платежах\n\n" +
                                "❌ **Вредные уведомления** (их лучше отключить):\n" +
                                "   • Реклама игр и магазинов\n" +
                                "   • Постоянные новости\n" +
                                "   • Предложения скидок и акций\n\n" +
                                "⚙️ **Зачем настраивать уведомления?**\n" +
                                "   • Телефон не будет отвлекать вас по пустякам\n" +
                                "   • Вы не пропустите важные звонки и сообщения\n" +
                                "   • Батарея будет держать заряд дольше",
                        fontSize = 16.sp, lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(20.dp))

                    Text("🛠️ Как навести порядок в уведомлениях (пошаговая инструкция)", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)), shape = RoundedCornerShape(14.dp)) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("1️⃣ Зайдите в **Настройки** телефона (шестерёнка ⚙️)", fontSize = 16.sp)
                            Text("2️⃣ Найдите раздел **«Уведомления»**", fontSize = 16.sp)
                            Text("3️⃣ Выберите приложение, которое надоедает (например, игра или новости)", fontSize = 16.sp)
                            Text("4️⃣ Нажмите **«Отключить уведомления»**", fontSize = 16.sp)
                            Text("5️⃣ Включите **«Не беспокоить»** на время сна или важных дел", fontSize = 16.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("💡 **Совет:** Не отключайте уведомления полностью для всех приложений. Оставьте сообщения и звонки.", fontSize = 16.sp, color = Color(0xFF2C5F6E), fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
        item {
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("▶️ Начать практику", fontSize = 18.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ==================== ШАГ 1: ОТКЛЮЧЕНИЕ УВЕДОМЛЕНИЙ ====================
@Composable
private fun DisableNotificationsScreen(
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    onComplete: () -> Unit
) {
    val appsData = listOf(
        AppData("Сообщения (СМС от банка, родных)", isImportant = true, "ОСТАВИТЬ – важные"),
        AppData("Telegram / WhatsApp (чаты с семьёй)", isImportant = true, "ОСТАВИТЬ – близкие"),
        AppData("Реклама игр", isImportant = false, "ОТКЛЮЧИТЬ – не нужно"),
        AppData("Новости (все подряд)", isImportant = false, "ОТКЛЮЧИТЬ – отвлекают"),
        AppData("Магазины со скидками", isImportant = false, "ОТКЛЮЧИТЬ – неважно"),
        AppData("Погода", isImportant = false, "ОТКЛЮЧИТЬ – посмотрите сами")
    )

    var remainingApps by remember { mutableStateOf(appsData.toMutableList()) }
    var localScore by remember(score) { mutableStateOf(score) }
    val distractingCount = remainingApps.count { !it.isImportant }

    fun removeApp(index: Int) {
        remainingApps = remainingApps.toMutableList().apply { removeAt(index) }
        localScore += 10
        onScoreUpdate(localScore)
        if (remainingApps.filter { !it.isImportant }.isEmpty()) onComplete()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f)),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🔕 Задание: убрать шумные уведомления", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Нажмите на красный крестик ✖️ рядом с раздражающим приложением. Важные не трогайте.",
                        fontSize = 16.sp, lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        if (distractingCount == 0) "✅ Все лишние отключены! Молодец!"
                        else "Осталось отключить: $distractingCount",
                        fontSize = 16.sp, fontWeight = FontWeight.Bold,
                        color = if (distractingCount == 0) Color(0xFF2E8058) else Color( 0xFF9B0C3F)
                    )
                }
            }
        }

        items(remainingApps.size) { index ->
            val app = remainingApps[index]
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(
                            if (app.isImportant) Icons.Default.CheckCircle else Icons.Default.Notifications,
                            contentDescription = null,
                            tint = if (app.isImportant) Color(0xFF2C5F6E) else Color.Gray,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(app.name, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            Text(app.hint, fontSize = 13.sp, color = if (app.isImportant) Color(0xFF2C5F6E) else Color( 0xFF9B0C3F))
                        }
                    }
                    if (!app.isImportant) {
                        IconButton(onClick = { removeApp(index) }) {
                            Icon(Icons.Default.Close, contentDescription = "Отключить", tint = Color( 0xFF9B0C3F), modifier = Modifier.size(28.dp))
                        }
                    } else {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Важно", tint = Color(0xFF2C5F6E), modifier = Modifier.size(28.dp))
                    }
                }
            }
        }

        item {
            Text("🏆 Очки: $localScore", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E), modifier = Modifier.padding(8.dp))
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==================== ШАГ 2: РЕЖИМ «НЕ БЕСПОКОИТЬ» (с прокруткой, уменьшенные размеры) ====================
@Composable
private fun DoNotDisturbScreen(
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    onComplete: () -> Unit
) {
    var enabled by remember { mutableStateOf(false) }
    var localScore by remember(score) { mutableStateOf(score) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("🔕 Режим «Не беспокоить»", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Этот режим выключает все звуки и вибрацию. Телефон не будет пищать.\n\n" +
                                "✅ **Когда включать?**\n" +
                                "• Ночью – чтобы сон не прерывали\n" +
                                "• На важных встречах\n" +
                                "• Когда нужно сосредоточиться\n\n" +
                                "⚠️ Звонки от родных и будильник продолжат работать.\n\n" +
                                "Нажмите кнопку, чтобы включить режим.",
                        fontSize = 16.sp, lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    if (!enabled) {
                        Button(
                            onClick = {
                                enabled = true
                                localScore += 30
                                onScoreUpdate(localScore)
                            },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("🔇 Включить «Не беспокоить»", fontSize = 18.sp, color = Color.White)
                        }
                    } else {
                        Text("✅ Режим «Не беспокоить» АКТИВИРОВАН!", fontSize = 18.sp, color = Color(0xFF2E8058), fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onComplete,
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Готово", fontSize = 18.sp, color = Color.White)
                        }
                    }
                }
            }
        }
        item {
            Text("🏆 Очки: $localScore", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E), modifier = Modifier.padding(8.dp))
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==================== ШАГ 3: ФИНАЛЬНЫЙ ЭКРАН (уменьшенные размеры) ====================
@Composable
private fun FinalScreen(score: Int, onFinish: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎉 ПОЗДРАВЛЯЕМ!", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Вы успешно настроили уведомления!", fontSize = 18.sp, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Заработано очков: $score", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("💡 Памятка на будущее", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "• Раз в месяц проверяйте список приложений, которые шумят.\n" +
                                        "• На ночь включайте «Не беспокоить» – выспитесь лучше.\n" +
                                        "• Важные приложения не отключайте полностью.\n" +
                                        "• Не бойтесь экспериментировать.",
                                fontSize = 16.sp, lineHeight = 24.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onFinish,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Завершить", fontSize = 18.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

private data class AppData(val name: String, val isImportant: Boolean, val hint: String)