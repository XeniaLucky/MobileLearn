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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.diplom2.screen.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "LessonBattery"
private const val PREFS_NAME = "battery_progress"

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonBatterySaverScreen(navController: NavController, userId: Long) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lessonKey = "game_battery"
    val totalSteps = 6

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

    var revealedDrainers by rememberSaveable { mutableStateOf(setOf<Int>()) }
    var completedTips by rememberSaveable { mutableStateOf(setOf<Int>()) }
    var mythAnswers by rememberSaveable { mutableStateOf(mutableMapOf<Int, Boolean>()) }
    var savedBrightness by rememberSaveable { mutableFloatStateOf(0.8f) }
    var savedSync by rememberSaveable { mutableStateOf(true) }
    var savedPowerSaver by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Экономия батареи", color = Color.White, fontSize = 18.sp) },
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
                1 -> BatteryDrainersScreen(
                    score = score,
                    onScoreUpdate = { newScore -> score = newScore },
                    revealed = revealedDrainers,
                    onRevealedChange = { revealedDrainers = it },
                    onComplete = { step = 2 }
                )
                2 -> ChargingTipsScreen(
                    score = score,
                    onScoreUpdate = { newScore -> score = newScore },
                    completed = completedTips,
                    onCompletedChange = { completedTips = it },
                    onComplete = { step = 3 }
                )
                3 -> BatteryMythsScreen(
                    score = score,
                    onScoreUpdate = { newScore -> score = newScore },
                    answers = mythAnswers,
                    onAnswersChange = { mythAnswers = it },
                    onComplete = { step = 4 }
                )
                4 -> BatterySettingsScreen(
                    score = score,
                    onScoreUpdate = { newScore -> score = newScore },
                    brightness = savedBrightness,
                    onBrightnessChange = { savedBrightness = it },
                    sync = savedSync,
                    onSyncChange = { savedSync = it },
                    powerSaver = savedPowerSaver,
                    onPowerSaverChange = { savedPowerSaver = it },
                    onComplete = { step = 5 }
                )
                5 -> FinalScreen(
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

// ==================== ШАГ 0: ТЕОРИЯ ====================
@Composable
private fun TheoryScreen(onStart: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("🔋 Экономия батареи", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Аккумулятор – сердце телефона. Чтобы он дольше работал без подзарядки и не изнашивался, запомните простые правила:\n\n" +
                                "✅ **Что помогает экономить заряд:**\n" +
                                "• Уменьшить яркость экрана (чем темнее, тем дольше).\n" +
                                "• Выключить Wi-Fi, Bluetooth, GPS, если они не нужны.\n" +
                                "• Отключить автосинхронизацию (почта, облако).\n" +
                                "• Включить режим энергосбережения (находится в настройках).\n" +
                                "• Закрывать приложения, которыми не пользуетесь.\n" +
                                "• Включить тёмную тему (особенно на новых экранах).\n" +
                                "• Установить короткое время блокировки экрана.\n\n" +
                                "❌ **Чего делать НЕ стоит:**\n" +
                                "• Держать телефон на солнце (батарея перегревается).\n" +
                                "• Использовать дешёвые зарядки.\n" +
                                "• Оставлять яркость на максимуме.\n" +
                                "• Допускать полный разряд (лучше заряжать от 20% до 80%).\n\n" +
                                "📌 **Дальше – практика! Узнаем, что сильнее всего «съедает» батарею.**",
                        fontSize = 17.sp,
                        lineHeight = 26.sp
                    )
                }
            }
        }
        item {
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("▶️ Начать практику", fontSize = 20.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// ==================== ШАГ 1: «ВРАГИ БАТАРЕИ» ====================
@Composable
private fun BatteryDrainersScreen(
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    revealed: Set<Int>,
    onRevealedChange: (Set<Int>) -> Unit,
    onComplete: () -> Unit
) {
    var localScore by remember(score) { mutableStateOf(score) }
    val drainers = listOf(
        "Яркий экран" to "Яркость на 100% – главный пожиратель заряда. Уменьшите до 40–50% – батарея будет жить на 2–3 часа дольше!",
        "Автосинхронизация" to "Почта, облако, приложения постоянно обновляются в фоне. Отключите – и телефон будет реже просить зарядку.",
        "Wi-Fi, Bluetooth, GPS" to "Эти модули ищут сети и устройства, даже когда вы их не используете. Выключайте их из шторки уведомлений.",
        "Фоновые приложения" to "Социальные сети, игры, карты – работают в фоне и расходуют заряд. Закрывайте их полностью."
    )
    val allRevealed = revealed.size == drainers.size

    fun reveal(index: Int) {
        if (index !in revealed) {
            val newRevealed = revealed + index
            onRevealedChange(newRevealed)
            localScore += 20
            onScoreUpdate(localScore)
        }
    }

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
                    Text("⚡ Что разряжает телефон быстрее всего?", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Нажмите на каждый пункт, чтобы узнать подробности и получить очки.", fontSize = 17.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = revealed.size.toFloat() / drainers.size,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF2C5F6E)
                    )
                    Text("Изучено: ${revealed.size} из ${drainers.size}", fontSize = 16.sp, color = Color(0xFF2C5F6E))
                }
            }
        }
        items(drainers.size) { index ->
            val (title, description) = drainers[index]
            Card(
                modifier = Modifier.fillMaxWidth().clickable { reveal(index) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (index in revealed) Color(0xFFC8E6C9) else Color.White
                )
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        AnimatedVisibility(visible = index in revealed) {
                            Column {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(description, fontSize = 16.sp, color = Color.DarkGray)
                            }
                        }
                    }
                    if (index in revealed) {
                        Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2E8058), modifier = Modifier.size(32.dp))
                    } else {
                        Icon(Icons.Default.ArrowForward, "Узнать", tint = Color(0xFF2C5F6E))
                    }
                }
            }
        }
        item {
            Button(
                onClick = onComplete,
                enabled = allRevealed,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Далее", fontSize = 20.sp, color = Color.White)
            }
            if (!allRevealed) {
                Text("Изучите все пункты, чтобы продолжить (каждый даёт +20 очков)", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("🏆 Очки: $localScore", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==================== ШАГ 2: СОВЕТЫ ПО ЗАРЯДКЕ ====================
@Composable
private fun ChargingTipsScreen(
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    completed: Set<Int>,
    onCompletedChange: (Set<Int>) -> Unit,
    onComplete: () -> Unit
) {
    var localScore by remember(score) { mutableStateOf(score) }
    val tips = listOf(
        "Не заряжайте до 100% каждый день" to "Лучше держать заряд между 20% и 80%. Это продлевает срок службы батареи в 2 раза!",
        "Не оставляйте на зарядке на ночь" to "Постоянное подключение к сети изнашивает аккумулятор. Заряжайте днём, когда можете отключить вовремя.",
        "Используйте оригинальное зарядное устройство" to "Дешёвые зарядки могут подавать нестабильное напряжение – батарея портится быстрее.",
        "Не допускайте полного разряда" to "Если телефон выключился от 0% – это стресс для батареи. Ставьте на зарядку при 20–30%."
    )
    val allCompleted = completed.size == tips.size

    fun completeTip(index: Int) {
        if (index !in completed) {
            val newCompleted = completed + index
            onCompletedChange(newCompleted)
            localScore += 15
            onScoreUpdate(localScore)
        }
    }

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
                    Text("🔌 Как правильно заряжать телефон?", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Нажмите на каждый совет, чтобы узнать секрет долгой жизни батареи.", fontSize = 17.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = completed.size.toFloat() / tips.size,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF2C5F6E)
                    )
                    Text("Освоено советов: ${completed.size} из ${tips.size}", fontSize = 16.sp, color = Color(0xFF2C5F6E))
                }
            }
        }
        items(tips.size) { index ->
            val (title, description) = tips[index]
            Card(
                modifier = Modifier.fillMaxWidth().clickable { completeTip(index) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (index in completed) Color(0xFFC8E6C9) else Color.White
                )
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        AnimatedVisibility(visible = index in completed) {
                            Column {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(description, fontSize = 16.sp, color = Color.DarkGray)
                            }
                        }
                    }
                    if (index in completed) {
                        Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2E8058), modifier = Modifier.size(32.dp))
                    } else {
                        Icon(Icons.Default.BatteryFull, "Узнать", tint = Color(0xFF2C5F6E))
                    }
                }
            }
        }
        item {
            Button(
                onClick = onComplete,
                enabled = allCompleted,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Далее", fontSize = 20.sp, color = Color.White)
            }
            if (!allCompleted) {
                Text("Изучите все советы, чтобы продолжить (каждый даёт +15 очков)", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("🏆 Очки: $localScore", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==================== ШАГ 3: МИФЫ И ПРАВДА ====================
@Composable
private fun BatteryMythsScreen(
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    answers: MutableMap<Int, Boolean>,
    onAnswersChange: (MutableMap<Int, Boolean>) -> Unit,
    onComplete: () -> Unit
) {
    var localScore by remember(score) { mutableStateOf(score) }
    val myths = listOf(
        "Нужно полностью разряжать телефон перед зарядкой" to false,
        "Дешёвые зарядки так же хороши, как оригинальные" to false,
        "Тёмная тема экономит заряд на любом экране" to false,
        "Режим полёта отключает все радиомодули и экономит заряд" to true
    )
    val explanations = listOf(
        "Миф! Современные литий-ионные батареи не имеют «эффекта памяти». Частичная зарядка даже полезнее.",
        "Миф! Нестабильное напряжение может повредить контроллер заряда и сократить жизнь батареи.",
        "Не совсем: тёмная тема экономит только на AMOLED-экранах, где чёрные пиксели отключаются.",
        "Правда! Режим полёта отключает Wi-Fi, Bluetooth и сотовую связь – телефон тратит энергию только на локальные задачи."
    )
    val allAnswered = answers.size == myths.size

    fun setAnswer(index: Int, answer: Boolean) {
        val current = answers[index]
        if (current != answer) {
            val newAnswers = answers.toMutableMap()
            newAnswers[index] = answer
            onAnswersChange(newAnswers)
            if (answer == myths[index].second) {
                localScore += 10
                onScoreUpdate(localScore)
            } else if (current != null && current != answer) {
                if (answer == myths[index].second && current != myths[index].second) {
                    localScore += 10
                    onScoreUpdate(localScore)
                }
            }
        }
    }

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
                    Text("🤔 Правда или миф?", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Выберите верный ответ для каждого утверждения. За каждый правильный ответ – +10 очков.", fontSize = 17.sp)
                    if (allAnswered) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("✅ Вы ответили на все вопросы!", fontSize = 16.sp, color = Color(0xFF2E8058))
                    }
                }
            }
        }
        items(myths.size) { index ->
            val (statement, isTrue) = myths[index]
            val userAnswer = answers[index]
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(statement, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        FilterChip(
                            selected = userAnswer == true,
                            onClick = { setAnswer(index, true) },
                            label = { Text("Правда", fontSize = 16.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF2C5F6E).copy(alpha = 0.2f)
                            )
                        )
                        FilterChip(
                            selected = userAnswer == false,
                            onClick = { setAnswer(index, false) },
                            label = { Text("Миф", fontSize = 16.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF2C5F6E).copy(alpha = 0.2f)
                            )
                        )
                    }
                    if (userAnswer != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (userAnswer == isTrue) "✅ Правильно! " else "❌ Неправильно. " + explanations[index],
                            fontSize = 14.sp,
                            color = if (userAnswer == isTrue) Color(0xFF2E8058) else Color( 0xFF9B0C3F)
                        )
                    }
                }
            }
        }
        item {
            if (allAnswered) {
                Button(
                    onClick = onComplete,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Далее", fontSize = 20.sp, color = Color.White)
                }
            } else {
                Text("Ответьте на все вопросы, чтобы продолжить", fontSize = 16.sp, color = Color( 0xFF9B0C3F), modifier = Modifier.padding(8.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("🏆 Очки: $localScore", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==================== ШАГ 4: НАСТРОЙКИ ЭКОНОМИИ ЗАРЯДА ====================
@Composable
private fun BatterySettingsScreen(
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    brightness: Float,
    onBrightnessChange: (Float) -> Unit,
    sync: Boolean,
    onSyncChange: (Boolean) -> Unit,
    powerSaver: Boolean,
    onPowerSaverChange: (Boolean) -> Unit,
    onComplete: () -> Unit
) {
    var localScore by remember(score) { mutableStateOf(score) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("🔧 Настройте экономию заряда", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Выполните три обязательных действия:\n" +
                                "1️⃣ Яркость ≤ 40%\n" +
                                "2️⃣ Отключите автосинхронизацию\n" +
                                "3️⃣ Включите режим энергосбережения\n\n" +
                                "После этого нажмите «Проверить».",
                        fontSize = 17.sp, lineHeight = 24.sp
                    )
                }
            }
        }
        item {
            Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.BrightnessHigh, null, tint = Color(0xFF2C5F6E))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Яркость экрана", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Текущее значение: ${(brightness * 100).toInt()}%", fontSize = 18.sp)
                    Slider(
                        value = brightness,
                        onValueChange = onBrightnessChange,
                        valueRange = 0.2f..1f,
                        colors = SliderDefaults.colors(thumbColor = Color(0xFF2C5F6E), activeTrackColor = Color(0xFF2C5F6E))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        if (brightness <= 0.4f) "✅ Хорошо! Яркость оптимальна."
                        else "❌ Слишком ярко – батарея будет быстро садиться. Уменьшите до 40%.",
                        fontSize = 16.sp, color = if (brightness <= 0.4f) Color(0xFF2E8058) else Color( 0xFF9B0C3F)
                    )
                    Text("💡 Совет: при яркости 30–40% экран всё ещё отлично виден в помещении.", fontSize = 15.sp, color = Color(0xFF2C5F6E))
                }
            }
        }
        item {
            Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Sync, null, tint = Color(0xFF2C5F6E))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Автосинхронизация", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("Включена", fontSize = 18.sp)
                        Switch(checked = sync, onCheckedChange = onSyncChange, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E)))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        if (!sync) "✅ Отлично! Автосинхронизация отключена – экономия заряда."
                        else "❌ Автосинхронизация расходует батарею в фоне. Выключите переключатель.",
                        fontSize = 16.sp, color = if (!sync) Color(0xFF2E8058) else Color( 0xFF9B0C3F)
                    )
                    Text("💡 Совет: синхронизацию можно включать вручную раз в день, чтобы проверить почту.", fontSize = 15.sp, color = Color(0xFF2C5F6E))
                }
            }
        }
        item {
            Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.BatteryFull, null, tint = Color(0xFF2C5F6E))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Режим энергосбережения", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("Включён", fontSize = 18.sp)
                        Switch(checked = powerSaver, onCheckedChange = onPowerSaverChange, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E)))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        if (powerSaver) "✅ Режим включён! Телефон будет работать дольше."
                        else "❌ Включите режим энергосбережения – он ограничивает фоновые процессы.",
                        fontSize = 16.sp, color = if (powerSaver) Color(0xFF2E8058) else Color( 0xFF9B0C3F)
                    )
                    Text("💡 Совет: в режиме энергосбережения вы всё равно сможете звонить и писать сообщения.", fontSize = 15.sp, color = Color(0xFF2C5F6E))
                }
            }
        }
        if (errorMessage != null) {
            item { Text(errorMessage!!, fontSize = 16.sp, color = Color( 0xFF9B0C3F), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }
        }
        item {
            Button(
                onClick = {
                    if (brightness > 0.4f) {
                        errorMessage = "❌ Снизьте яркость до 40% (сейчас ${(brightness * 100).toInt()}%)"
                    } else if (sync) {
                        errorMessage = "❌ Отключите автосинхронизацию (переключатель влево)"
                    } else if (!powerSaver) {
                        errorMessage = "❌ Включите режим энергосбережения"
                    } else {
                        localScore += 40
                        onScoreUpdate(localScore)
                        onComplete()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("✅ Проверить настройки", fontSize = 20.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("🏆 Очки: $localScore", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==================== ФИНАЛЬНЫЙ ЭКРАН ====================
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
                    Text("Вы научились экономить заряд батареи!", fontSize = 20.sp, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Заработано очков: $score", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(24.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("💡 Полезные советы на каждый день", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "• Включите тёмную тему – на AMOLED-экранах экономит до 30% заряда.\n" +
                                        "• Не держите телефон на солнце и не перегревайте.\n" +
                                        "• Заряжайте батарею от 20% до 80% – так она прослужит дольше.\n" +
                                        "• Отключайте ненужные уведомления – они тоже расходуют заряд.\n" +
                                        "• Используйте режим энергосбережения в поездках.",
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