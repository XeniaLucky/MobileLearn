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
import kotlinx.coroutines.launch

private const val TAG = "LessonDigitalWellbeing"
private const val PREFS_NAME = "digital_wellbeing_progress"

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
fun LessonDigitalWellbeingScreen(navController: NavController, userId: Long) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lessonKey = "game_digitalwellbeing"
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

    var selectedHours by rememberSaveable { mutableStateOf<Int?>(null) }
    var nightModeEnabled by rememberSaveable { mutableStateOf(false) }
    var reminderSet by rememberSaveable { mutableStateOf(false) }
    var checklistCompleted by rememberSaveable { mutableStateOf(setOf<Int>()) }

    var theory1Revealed by rememberSaveable { mutableStateOf(false) }
    var theory2Revealed by rememberSaveable { mutableStateOf(false) }
    var theory3Revealed by rememberSaveable { mutableStateOf(false) }
    var theoryBonusAdded by rememberSaveable { mutableStateOf(setOf<Int>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Цифровое благополучие", color = Color.White, fontSize = 18.sp) },
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
                0 -> TheoryScreen(
                    theory1Revealed = theory1Revealed,
                    theory2Revealed = theory2Revealed,
                    theory3Revealed = theory3Revealed,
                    theoryBonusAdded = theoryBonusAdded,
                    onReveal1 = {
                        if (!theory1Revealed) {
                            theory1Revealed = true
                            if (0 !in theoryBonusAdded) { score += 15; theoryBonusAdded = theoryBonusAdded + 0 }
                        }
                    },
                    onReveal2 = {
                        if (!theory2Revealed) {
                            theory2Revealed = true
                            if (1 !in theoryBonusAdded) { score += 15; theoryBonusAdded = theoryBonusAdded + 1 }
                        }
                    },
                    onReveal3 = {
                        if (!theory3Revealed) {
                            theory3Revealed = true
                            if (2 !in theoryBonusAdded) { score += 15; theoryBonusAdded = theoryBonusAdded + 2 }
                        }
                    },
                    onComplete = { step = 1 }
                )
                1 -> LimitSettingsScreen(
                    score = score,
                    onScoreUpdate = { newScore -> score = newScore },
                    selectedHours = selectedHours,
                    onSelectedHoursChange = { selectedHours = it },
                    onComplete = { step = 2 }
                )
                2 -> NightModeScreen(
                    score = score,
                    onScoreUpdate = { newScore -> score = newScore },
                    nightModeEnabled = nightModeEnabled,
                    onNightModeChange = { nightModeEnabled = it },
                    onComplete = { step = 3 }
                )
                3 -> ReminderScreen(
                    score = score,
                    onScoreUpdate = { newScore -> score = newScore },
                    reminderSet = reminderSet,
                    onReminderChange = { reminderSet = it },
                    onComplete = { step = 4 }
                )
                4 -> ChecklistScreen(
                    score = score,
                    onScoreUpdate = { newScore -> score = newScore },
                    checklistCompleted = checklistCompleted,
                    onChecklistCompletedChange = { checklistCompleted = it },
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

// ==================== ШАГ 0: ИНТЕРАКТИВНАЯ ТЕОРИЯ ====================
@Composable
private fun TheoryScreen(
    theory1Revealed: Boolean,
    theory2Revealed: Boolean,
    theory3Revealed: Boolean,
    theoryBonusAdded: Set<Int>,
    onReveal1: () -> Unit,
    onReveal2: () -> Unit,
    onReveal3: () -> Unit,
    onComplete: () -> Unit
) {
    val allRevealed = theory1Revealed && theory2Revealed && theory3Revealed
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
                    Text("🧘 Цифровое благополучие", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Телефон – отличный помощник, но он может красть наше время и внимание. Научитесь управлять своим экранным временем, чтобы телефон служил вам, а не отвлекал.",
                        fontSize = 16.sp, lineHeight = 24.sp
                    )
                }
            }
        }
        item {
            TheoryCard(
                title = "⚠️ Почему телефон отвлекает?",
                description = "Социальные сети, игры, новости – всё это создано, чтобы привлекать ваше внимание. Уведомления, яркие цвета, бесконечная лента – всё это «крадёт» время. Вы замечали, что зашли в телефон на минуту, а прошёл час? Это не случайно. Настройки цифрового благополучия помогут взять контроль обратно.",
                revealed = theory1Revealed,
                bonusAdded = 0 in theoryBonusAdded,
                onReveal = onReveal1
            )
        }
        item {
            TheoryCard(
                title = "📊 Что такое цифровое благополучие?",
                description = "Это встроенная в Android функция (Настройки → Цифровое благополучие). Она показывает, сколько времени вы проводите в каждом приложении, позволяет устанавливать таймеры, включить ночной режим, режим «Не беспокоить» и напоминания о перерывах. Всё это помогает сбалансировать жизнь с телефоном.",
                revealed = theory2Revealed,
                bonusAdded = 1 in theoryBonusAdded,
                onReveal = onReveal2
            )
        }
        item {
            TheoryCard(
                title = "🛠️ С чего начать?",
                description = "1. Установите лимиты на соцсети (например, 30 минут в день).\n2. Включите ночной режим – он снижает нагрузку на глаза.\n3. Включите напоминания о перерывах.\n4. Отключите уведомления от ненужных приложений.\n5. Используйте режим «Не беспокоить» во время сна.",
                revealed = theory3Revealed,
                bonusAdded = 2 in theoryBonusAdded,
                onReveal = onReveal3
            )
        }
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
                Text("Изучите все карточки, чтобы продолжить (каждая даёт +15 очков)", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TheoryCard(title: String, description: String, revealed: Boolean, bonusAdded: Boolean, onReveal: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(enabled = !revealed) { if (!revealed) onReveal() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (revealed) Color(0xFFC8E6C9) else Color.White)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                if (revealed) Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2E8058), modifier = Modifier.size(28.dp))
                else Icon(Icons.Default.ArrowForward, null, tint = Color(0xFF2C5F6E))
            }
            if (revealed) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(description, fontSize = 16.sp, lineHeight = 24.sp)
                if (!bonusAdded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("✅ +15 очков за изучение!", fontSize = 14.sp, color = Color(0xFF2E8058))
                }
            }
        }
    }
}

// ==================== ШАГ 1: ВЫБОР ЛИМИТА ВРЕМЕНИ ====================
@Composable
private fun LimitSettingsScreen(
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    selectedHours: Int?,
    onSelectedHoursChange: (Int?) -> Unit,
    onComplete: () -> Unit
) {
    var localScore by remember(score) { mutableStateOf(score) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("⏰ Установите лимит времени", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Сколько часов в день вы хотите тратить на социальные сети и игры?\n\n" +
                                "✅ **Маленький лимит (1 час)** – больше свободного времени, меньше усталости глаз.\n" +
                                "✅ **Средний лимит (2 часа)** – хороший баланс.\n" +
                                "⚠️ **Большой лимит (3 часа)** – лучше уменьшить, чтобы не привыкать.\n\n" +
                                "Выберите вариант:",
                        fontSize = 16.sp, lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Icon(Icons.Default.Timer, null, modifier = Modifier.size(56.dp), tint = Color(0xFF2C5F6E))
                }
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LimitButton(
                    text = "1 час",
                    isSelected = selectedHours == 1,
                    onClick = {
                        onSelectedHoursChange(1)
                        if (selectedHours != 1) {
                            localScore += 20
                            onScoreUpdate(localScore)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                LimitButton(
                    text = "2 часа",
                    isSelected = selectedHours == 2,
                    onClick = {
                        onSelectedHoursChange(2)
                        if (selectedHours != 2) {
                            localScore += 15
                            onScoreUpdate(localScore)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                LimitButton(
                    text = "3 часа",
                    isSelected = selectedHours == 3,
                    onClick = {
                        onSelectedHoursChange(3)
                        if (selectedHours != 3) {
                            localScore += 10
                            onScoreUpdate(localScore)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        if (errorMessage != null) {
            item { Text(errorMessage!!, fontSize = 14.sp, color = Color( 0xFF9B0C3F)) }
        }
        item {
            Button(
                onClick = {
                    if (selectedHours == null) {
                        errorMessage = "Пожалуйста, выберите лимит времени"
                    } else {
                        onComplete()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Далее", fontSize = 18.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("🏆 Очки: $localScore", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun LimitButton(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF2C5F6E) else Color(0xFF2C5F6E).copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text, fontSize = 18.sp, color = Color.White)
    }
}

// ==================== ШАГ 2: НОЧНОЙ РЕЖИМ ====================
@Composable
private fun NightModeScreen(
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    nightModeEnabled: Boolean,
    onNightModeChange: (Boolean) -> Unit,
    onComplete: () -> Unit
) {
    var localScore by remember(score) { mutableStateOf(score) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("🌙 Ночной режим", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Ночной режим делает экран теплее (желтоватым), убирая синий свет. Синий свет подавляет выработку мелатонина – гормона сна. Поэтому использование телефона перед сном мешает заснуть.\n\n" +
                                "✅ Включите ночной режим, чтобы:\n" +
                                "• Глаза меньше уставали\n" +
                                "• Легче засыпать вечером\n" +
                                "• Снизить риск головной боли\n\n" +
                                "📌 В реальном телефоне: Настройки → Дисплей → Ночной режим → Включить.\n\n" +
                                "Включите его в симуляторе ниже:",
                        fontSize = 16.sp, lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Icon(Icons.Default.Nightlight, null, modifier = Modifier.size(56.dp), tint = Color(0xFF2C5F6E))
                }
            }
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Включить ночной режим", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    Switch(
                        checked = nightModeEnabled,
                        onCheckedChange = {
                            onNightModeChange(it)
                            if (it && !nightModeEnabled) {
                                localScore += 25
                                onScoreUpdate(localScore)
                            }
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E))
                    )
                }
            }
        }
        if (nightModeEnabled) {
            item {
                Text("✅ Ночной режим включён! Здоровье глаз и сон будут в порядке.", fontSize = 16.sp, color = Color(0xFF2E8058))
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onComplete,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Далее", fontSize = 18.sp, color = Color.White)
                }
            }
        } else {
            item {
                Text("❌ Включите ночной режим, чтобы продолжить (+25 очков)", fontSize = 16.sp, color = Color( 0xFF9B0C3F))
            }
        }
        item {
            Text("🏆 Очки: $localScore", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E), modifier = Modifier.padding(8.dp))
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==================== ШАГ 3: НАПОМИНАНИЯ О ПЕРЕРЫВАХ ====================
@Composable
private fun ReminderScreen(
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    reminderSet: Boolean,
    onReminderChange: (Boolean) -> Unit,
    onComplete: () -> Unit
) {
    var localScore by remember(score) { mutableStateOf(score) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("⌛ Напоминания о перерывах", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Если вы проводите в телефоне много времени подряд, полезно делать короткие перерывы: встать, размяться, отвлечься.\n\n" +
                                "✅ **Что дают напоминания:**\n" +
                                "• Снижают усталость глаз\n" +
                                "• Улучшают концентрацию\n" +
                                "• Напоминают о физической активности\n\n" +
                                "📌 В реальном телефоне: Настройки → Цифровое благополучие → Напоминания о перерывах → Включить.\n\n" +
                                "Включите напоминания в симуляторе:",
                        fontSize = 16.sp, lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Icon(Icons.Default.Notifications, null, modifier = Modifier.size(56.dp), tint = Color(0xFF2C5F6E))
                }
            }
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Включить напоминания о перерывах", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    Switch(
                        checked = reminderSet,
                        onCheckedChange = {
                            onReminderChange(it)
                            if (it && !reminderSet) {
                                localScore += 20
                                onScoreUpdate(localScore)
                            }
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E))
                    )
                }
            }
        }
        if (reminderSet) {
            item {
                Text("✅ Напоминания включены! Телефон будет заботиться о вашем здоровье.", fontSize = 16.sp, color = Color(0xFF2E8058))
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onComplete,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Далее", fontSize = 18.sp, color = Color.White)
                }
            }
        } else {
            item {
                Text("❌ Включите напоминания, чтобы продолжить (+20 очков)", fontSize = 16.sp, color = Color( 0xFF9B0C3F))
            }
        }
        item {
            Text("🏆 Очки: $localScore", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E), modifier = Modifier.padding(8.dp))
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==================== ШАГ 4: ЧЕК-ЛИСТ ====================
@Composable
private fun ChecklistScreen(
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    checklistCompleted: Set<Int>,
    onChecklistCompletedChange: (Set<Int>) -> Unit,
    onComplete: () -> Unit
) {
    var localScore by remember(score) { mutableStateOf(score) }
    val items = listOf(
        "Отключить уведомления для соцсетей и игр (оставить только важные)",
        "Удалить приложения, которыми не пользуетесь больше месяца",
        "Включить режим «Не беспокоить» на время сна",
        "Использовать чёрно-белый режим экрана (Монохромность) для снижения привлекательности"
    )
    val allChecked = checklistCompleted.size == items.size

    fun toggleItem(index: Int) {
        val newSet = if (index in checklistCompleted) checklistCompleted - index else checklistCompleted + index
        onChecklistCompletedChange(newSet)
        if (index !in checklistCompleted) {
            localScore += 10
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
                    Text("✅ Чек-лист здоровых привычек", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Отметьте действия, которые вы готовы выполнять (каждая галочка даёт +10 очков). После этого вы сможете завершить настройку.",
                        fontSize = 16.sp, lineHeight = 24.sp
                    )
                }
            }
        }
        items(items.size) { index ->
            Card(
                modifier = Modifier.fillMaxWidth().clickable { toggleItem(index) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = if (index in checklistCompleted) Color(0xFFC8E6C9) else Color.White)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        if (index in checklistCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        null,
                        tint = if (index in checklistCompleted) Color(0xFF2E8058) else Color(0xFF2C5F6E),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(items[index], fontSize = 16.sp)
                }
            }
        }
        item {
            Button(
                onClick = onComplete,
                enabled = allChecked,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (allChecked) Color(0xFF2C5F6E) else Color.Gray),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Завершить настройку", fontSize = 18.sp, color = Color.White)
            }
            if (!allChecked) {
                Text("Отметьте все пункты, чтобы продолжить (каждый даёт +10 очков)", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("🏆 Очки: $localScore", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
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
                    Text("Вы освоили цифровое благополучие!", fontSize = 20.sp, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Заработано очков: $score", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(24.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("💡 Памятка на будущее", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "• Раз в месяц проверяйте экранное время в Цифровом благополучии.\n" +
                                        "• Устанавливайте лимиты на самые «залипательные» приложения.\n" +
                                        "• Включайте ночной режим за час до сна.\n" +
                                        "• Используйте режим «Не беспокоить» на время отдыха.\n" +
                                        "• Не забывайте делать перерывы каждые 30-40 минут.\n" +
                                        "• Проводите «цифровой детокс» – час без телефона перед сном.",
                                fontSize = 16.sp, lineHeight = 24.sp
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