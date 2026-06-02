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

private const val TAG = "LessonMemoryClean"
private const val PREFS_NAME = "memory_clean_progress"

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

private fun saveRevealedSteps(context: Context, userId: Long, steps: Set<Int>) {
    try { getPrefs(context, userId).edit().putString("revealed_steps", steps.joinToString(",")).apply() }
    catch (e: Exception) { Log.e(TAG, "Ошибка сохранения revealedSteps: ${e.message}") }
}

private fun getRevealedSteps(context: Context, userId: Long): Set<Int> {
    return try {
        val str = getPrefs(context, userId).getString("revealed_steps", "")
        if (str.isNullOrEmpty()) emptySet() else str.split(",").mapNotNull { it.toIntOrNull() }.toSet()
    } catch (e: Exception) { emptySet() }
}

private fun saveDeletedApps(context: Context, userId: Long, deletedIndices: Set<Int>) {
    try { getPrefs(context, userId).edit().putString("deleted_apps", deletedIndices.joinToString(",")).apply() }
    catch (e: Exception) { Log.e(TAG, "Ошибка сохранения deletedApps: ${e.message}") }
}

private fun getDeletedApps(context: Context, userId: Long): Set<Int> {
    return try {
        val str = getPrefs(context, userId).getString("deleted_apps", "")
        if (str.isNullOrEmpty()) emptySet() else str.split(",").mapNotNull { it.toIntOrNull() }.toSet()
    } catch (e: Exception) { emptySet() }
}

private fun saveCacheCleared(context: Context, userId: Long, cleared: Boolean) {
    try { getPrefs(context, userId).edit().putBoolean("cache_cleared", cleared).apply() }
    catch (e: Exception) { Log.e(TAG, "Ошибка сохранения cacheCleared: ${e.message}") }
}

private fun getCacheCleared(context: Context, userId: Long): Boolean {
    return try { getPrefs(context, userId).getBoolean("cache_cleared", false) }
    catch (e: Exception) { false }
}

private fun saveShowTip(context: Context, userId: Long, showTip: Boolean) {
    try { getPrefs(context, userId).edit().putBoolean("show_tip", showTip).apply() }
    catch (e: Exception) { Log.e(TAG, "Ошибка сохранения showTip: ${e.message}") }
}

private fun getShowTip(context: Context, userId: Long): Boolean {
    return try { getPrefs(context, userId).getBoolean("show_tip", true) }
    catch (e: Exception) { true }
}

private fun saveLessonCompleted(context: Context, userId: Long, completed: Boolean) {
    try { getPrefs(context, userId).edit().putBoolean("completed", completed).apply() }
    catch (e: Exception) { Log.e(TAG, "Ошибка сохранения статуса урока: ${e.message}") }
}

private fun isLessonCompleted(context: Context, userId: Long): Boolean {
    return try { getPrefs(context, userId).getBoolean("completed", false) }
    catch (e: Exception) { false }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonMemoryCleanScreen(navController: NavController, userId: Long) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lessonKey = "game_memory"
    val totalSteps = 5

    val activeLesson = getActiveLessonMedium(context, userId)
    val savedStep = if (activeLesson == lessonKey) getLastStepMedium(context, userId, lessonKey) else 0
    var step by remember { mutableIntStateOf(savedStep) }
    var score by remember { mutableIntStateOf(getScore(context, userId)) }

    // Сброс очков и прогресса при новом прохождении (если урок ранее был завершён)
    LaunchedEffect(Unit) {
        if (savedStep == 0 && isLessonCompleted(context, userId)) {
            score = 0
            saveScore(context, userId, 0)
            saveRevealedSteps(context, userId, emptySet())
            saveDeletedApps(context, userId, emptySet())
            saveCacheCleared(context, userId, false)
            saveShowTip(context, userId, true)
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

    // Восстановленные состояния
    var revealedSteps by rememberSaveable { mutableStateOf(getRevealedSteps(context, userId)) }
    var deletedAppsSet by rememberSaveable { mutableStateOf(getDeletedApps(context, userId)) }
    var cacheCleared by rememberSaveable { mutableStateOf(getCacheCleared(context, userId)) }
    var showTip by rememberSaveable { mutableStateOf(getShowTip(context, userId)) }

    // Автосохранение состояний при изменении
    LaunchedEffect(revealedSteps) { saveRevealedSteps(context, userId, revealedSteps) }
    LaunchedEffect(deletedAppsSet) { saveDeletedApps(context, userId, deletedAppsSet) }
    LaunchedEffect(cacheCleared) { saveCacheCleared(context, userId, cacheCleared) }
    LaunchedEffect(showTip) { saveShowTip(context, userId, showTip) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Очистка памяти", color = Color.White, fontSize = 18.sp) },
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
                1 -> MemoryReasonsScreen(
                    score = score,
                    onScoreUpdate = { newScore -> score = newScore },
                    revealedSteps = revealedSteps,
                    onRevealedStepsChange = { revealedSteps = it },
                    onComplete = { step = 2 }
                )
                2 -> DeleteAppsScreen(
                    score = score,
                    onScoreUpdate = { newScore -> score = newScore },
                    deletedAppsSet = deletedAppsSet,
                    onDeletedAppsSetChange = { deletedAppsSet = it },
                    showTip = showTip,
                    onShowTipChange = { showTip = it },
                    onComplete = { step = 3 }
                )
                3 -> ClearCacheScreen(
                    score = score,
                    onScoreUpdate = { newScore -> score = newScore },
                    cacheCleared = cacheCleared,
                    onCacheClearedChange = { cacheCleared = it },
                    onComplete = { step = 4 }
                )
                4 -> FinalScreen(
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
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("🧹 Оптимизация памяти телефона", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Со временем телефон может замедляться, тормозить и зависать. Почему это происходит?\n\n" +
                                "❌ **Основные причины:**\n" +
                                "• Ненужные приложения, которые вы установили и забыли.\n" +
                                "• Кэш – временные файлы (реклама, превью, обновления).\n" +
                                "• Много фото, видео, сообщений.\n" +
                                "• Загрузки (папка Download) – старые PDF, APK.\n\n" +
                                "✅ **Что делать:**\n" +
                                "1. Удалите приложения, которыми не пользуетесь.\n" +
                                "2. Очистите кэш приложений (не удаляет логины).\n" +
                                "3. Перенесите фото в облако (Google Фото) или на карту памяти.\n" +
                                "4. Используйте встроенную очистку (Настройки → Память).\n\n" +
                                "🚫 **Чего НЕ делать:**\n" +
                                "• Не удаляйте системные файлы.\n" +
                                "• Не пользуйтесь сомнительными «ускорителями».\n\n" +
                                "📌 **Дальше вас ждут практические задания – начнём!**",
                        fontSize = 16.sp, lineHeight = 24.sp
                    )
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

// ==================== ШАГ 1: ПРИЧИНЫ НЕХВАТКИ ПАМЯТИ (с кнопкой Далее) ====================
@Composable
private fun MemoryReasonsScreen(
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    revealedSteps: Set<Int>,
    onRevealedStepsChange: (Set<Int>) -> Unit,
    onComplete: () -> Unit
) {
    var localScore by remember(score) { mutableStateOf(score) }
    val reasons = listOf(
        "📦 Ненужные приложения" to "Вы установили и забыли. Занимают место и могут работать в фоне.",
        "🗑️ Кэш приложений" to "Временные файлы, которые накапливаются (реклама, миниатюры). Их можно безопасно удалить.",
        "📸 Фото и видео" to "Семейные альбомы, скриншоты – часто весят десятки гигабайт.",
        "📂 Загрузки и старые файлы" to "PDF, APK, документы, которые давно не нужны."
    )

    fun revealReason(index: Int) {
        if (index !in revealedSteps) {
            val newRevealed = revealedSteps + index
            onRevealedStepsChange(newRevealed)
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
                    Text("🔍 Что «съедает» память?", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Нажмите на каждый пункт, чтобы узнать подробности и получить очки.",
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = revealedSteps.size.toFloat() / reasons.size,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF2C5F6E)
                    )
                    Text("Изучено: ${revealedSteps.size} из ${reasons.size}", fontSize = 14.sp, color = Color(0xFF2C5F6E))
                }
            }
        }

        items(reasons.size) { index ->
            val (title, description) = reasons[index]
            Card(
                modifier = Modifier.fillMaxWidth().clickable { revealReason(index) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (index in revealedSteps) Color(0xFFC8E6C9) else Color.White
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        AnimatedVisibility(visible = index in revealedSteps) {
                            Column {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(description, fontSize = 15.sp, color = Color.DarkGray)
                            }
                        }
                    }
                    if (index in revealedSteps) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E8058), modifier = Modifier.size(28.dp))
                    } else {
                        Icon(Icons.Default.ArrowForward, contentDescription = "Узнать", tint = Color(0xFF2C5F6E))
                    }
                }
            }
        }

        item {
            Button(
                onClick = onComplete,
                enabled = revealedSteps.size == reasons.size,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Далее", fontSize = 18.sp, color = Color.White)
            }
            if (revealedSteps.size != reasons.size) {
                Text("Изучите все причины, чтобы продолжить (каждая даёт +15 очков)", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("🏆 Очки: $localScore", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==================== ШАГ 2: УДАЛЕНИЕ ПРИЛОЖЕНИЙ ====================
@Composable
private fun DeleteAppsScreen(
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    deletedAppsSet: Set<Int>,
    onDeletedAppsSetChange: (Set<Int>) -> Unit,
    showTip: Boolean,
    onShowTipChange: (Boolean) -> Unit,
    onComplete: () -> Unit
) {
    val apps = listOf(
        AppForDelete("Facebook", "1.2 ГБ", "Редко заходите? Удалите – освободит много места."),
        AppForDelete("Неиспользуемая игра", "2.5 ГБ", "Игры очень тяжелые. Если не играете месяц – удалите."),
        AppForDelete("Instagram", "800 МБ", "Можно очистить кэш, но если не пользуетесь – удалите."),
        AppForDelete("YouTube", "500 МБ", "Смотрите видео в браузере? Тогда приложение не нужно.")
    )
    var localScore by remember(score) { mutableStateOf(score) }

    fun deleteApp(index: Int) {
        if (index !in deletedAppsSet) {
            val newSet = deletedAppsSet + index
            onDeletedAppsSetChange(newSet)
            localScore += 15
            onScoreUpdate(localScore)
            if (newSet.size == apps.size) onComplete()
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
                    Text("🗑️ Удалите ненужные приложения", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Нажмите на корзину 🗑️ рядом с каждым приложением, которое вы хотите удалить. " +
                                "Важно: при удалении приложения освобождается место, но ваши фотографии и документы останутся.",
                        fontSize = 16.sp, lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (showTip) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Lightbulb, contentDescription = "Совет", tint = Color(0xFFE65100))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("💡 Советы:", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFFE65100))
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    "• Не бойтесь удалять приложения – вы всегда сможете установить их заново из Google Play.\n" +
                                            "• После удаления приложения его данные (файлы, которые вы сохранили) останутся на телефоне.\n" +
                                            "• Если сомневаетесь, сначала прочитайте отзывы об этом приложении.",
                                    fontSize = 15.sp, lineHeight = 22.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { onShowTipChange(false) },
                                    modifier = Modifier.align(Alignment.End),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100))
                                ) {
                                    Text("Понятно, спасибо!", fontSize = 14.sp, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }

        items(apps.size) { index ->
            val app = apps[index]
            val isDeleted = index in deletedAppsSet
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = if (isDeleted) Color(0xFFC8E6C9) else Color.White)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(app.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(app.size, fontSize = 14.sp, color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(app.hint, fontSize = 14.sp, color = Color(0xFF2C5F6E))
                    }
                    if (!isDeleted) {
                        IconButton(onClick = { deleteApp(index) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = Color(0xFF2C5F6E), modifier = Modifier.size(32.dp))
                        }
                    } else {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Удалено", tint = Color(0xFF2E8058), modifier = Modifier.size(32.dp))
                    }
                }
            }
        }

        item {
            Text(
                if (deletedAppsSet.size < apps.size) "Осталось удалить: ${apps.size - deletedAppsSet.size} приложений"
                else "✅ Все приложения удалены! Молодец!",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (deletedAppsSet.size == apps.size) Color(0xFF2E8058) else Color( 0xFF9B0C3F),
                modifier = Modifier.padding(8.dp)
            )
            Text("🏆 Очки: $localScore", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==================== ШАГ 3: ОЧИСТКА КЭША ====================
@Composable
private fun ClearCacheScreen(
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    cacheCleared: Boolean,
    onCacheClearedChange: (Boolean) -> Unit,
    onComplete: () -> Unit
) {
    var localScore by remember(score) { mutableStateOf(score) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("🧹 Очистка кэша", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Кэш – это временные файлы, которые приложения сохраняют для ускорения работы. " +
                                "Например, картинки в интернете, превью видео, реклама.\n\n" +
                                "⚠️ Кэш можно БЕЗОПАСНО очистить – личные данные (логины, пароли, сообщения) не удаляются.\n\n" +
                                "Как очистить кэш на реальном телефоне:\n" +
                                "• Откройте Настройки → Приложения → YouTube → Хранилище → Очистить кэш.\n" +
                                "• Можно использовать встроенную функцию «Очистка» в приложении «Файлы» от Google.\n\n" +
                                "Нажмите кнопку ниже, чтобы очистить кэш YouTube в симуляторе.",
                        fontSize = 16.sp, lineHeight = 24.sp
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = if (cacheCleared) Color(0xFFC8E6C9) else Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Очистить кэш YouTube", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    if (!cacheCleared) {
                        Button(
                            onClick = {
                                onCacheClearedChange(true)
                                localScore += 30
                                onScoreUpdate(localScore)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Очистить", fontSize = 18.sp, color = Color.White)
                        }
                    } else {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Очищено", tint = Color(0xFF2E8058), modifier = Modifier.size(32.dp))
                    }
                }
            }
        }

        if (cacheCleared) {
            item {
                Text("✅ Кэш очищен! Отлично, вы освободили место.", fontSize = 18.sp, color = Color(0xFF2E8058), fontWeight = FontWeight.Bold)
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
        } else {
            item {
                Text("❌ Нажмите «Очистить», чтобы продолжить", fontSize = 16.sp, color = Color( 0xFF9B0C3F))
            }
        }

        item {
            Text("🏆 Очки: $localScore", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E), modifier = Modifier.padding(8.dp))
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
                    Text("Вы успешно очистили память телефона!", fontSize = 18.sp, textAlign = TextAlign.Center)
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
                                "• Раз в месяц удаляйте неиспользуемые приложения.\n" +
                                        "• Переносите фото в облако (Google Фото) и очищайте галерею.\n" +
                                        "• Очищайте кэш приложений, которыми редко пользуетесь.\n" +
                                        "• Не храните большие файлы в мессенджерах – скачивайте и удаляйте.\n" +
                                        "• Включите автоматическую очистку в настройках телефона, если есть.",
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

private data class AppForDelete(val name: String, val size: String, val hint: String)