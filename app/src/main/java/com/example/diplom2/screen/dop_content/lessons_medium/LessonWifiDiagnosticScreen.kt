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

private const val TAG = "LessonWifiDiagnostic"
private const val PREFS_NAME = "wifi_diagnostic_progress"

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
fun LessonWifiDiagnosticScreen(navController: NavController, userId: Long) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lessonKey = "game_wifidiag"
    val totalSteps = 6   // 0-теория,1-авиарежим,2-роутер,3-wi-fi,4-экономия,5-финал

    val activeLesson = getActiveLessonMedium(context, userId)
    val savedStep = if (activeLesson == lessonKey) getLastStepMedium(context, userId, lessonKey) else 0
    var step by remember { mutableIntStateOf(savedStep) }
    var score by remember { mutableIntStateOf(getScore(context, userId)) }

    // Сброс при повторном прохождении
    LaunchedEffect(Unit) {
        if (savedStep == 0 && isLessonCompleted(context, userId)) {
            score = 0
            saveScore(context, userId, 0)
            saveLessonCompleted(context, userId, false)
            // Сброс сохранённых состояний переключателей
            getPrefs(context, userId).edit().clear().apply()
        }
        activateLessonMedium(context, userId, lessonKey)
    }

    LaunchedEffect(step) {
        val progress = if (step >= totalSteps - 1) 1f else step.toFloat() / (totalSteps - 1)
        updateLessonProgressMedium(context, userId, progress)
        saveLastStepMedium(context, userId, lessonKey, step)
    }
    LaunchedEffect(score) { saveScore(context, userId, score) }

    // Состояния шагов (сохраняются в rememberSaveable)
    var theoryRevealed by rememberSaveable { mutableStateOf(setOf<Int>()) }
    var airplaneModeChecked by rememberSaveable { mutableStateOf(false) }
    var routerRestarted by rememberSaveable { mutableStateOf(false) }
    var wifiToggled by rememberSaveable { mutableStateOf(false) }
    var passwordEntered by rememberSaveable { mutableStateOf(false) }
    var dataSaverChecked by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Диагностика Wi-Fi", color = Color.White, fontSize = 18.sp) },
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
                    revealed = theoryRevealed,
                    onReveal = { index ->
                        if (index !in theoryRevealed) {
                            theoryRevealed = theoryRevealed + index
                            score += 5
                        }
                    },
                    onComplete = { step = 1 }
                )
                1 -> AirplaneModeScreen(
                    score = score,
                    onScoreUpdate = { newScore -> score = newScore },
                    checked = airplaneModeChecked,
                    onCheckedChange = { airplaneModeChecked = it },
                    onComplete = { step = 2 }
                )
                2 -> RouterRestartScreen(
                    score = score,
                    onScoreUpdate = { newScore -> score = newScore },
                    checked = routerRestarted,
                    onCheckedChange = { routerRestarted = it },
                    onComplete = { step = 3 }
                )
                3 -> WifiSettingsScreen(
                    score = score,
                    onScoreUpdate = { newScore -> score = newScore },
                    wifiToggled = wifiToggled,
                    onWifiToggledChange = { wifiToggled = it },
                    passwordEntered = passwordEntered,
                    onPasswordEnteredChange = { passwordEntered = it },
                    onComplete = { step = 4 }
                )
                4 -> DataSaverScreen(
                    score = score,
                    onScoreUpdate = { newScore -> score = newScore },
                    checked = dataSaverChecked,
                    onCheckedChange = { dataSaverChecked = it },
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
private fun TheoryScreen(revealed: Set<Int>, onReveal: (Int) -> Unit, onComplete: () -> Unit) {
    val theories = listOf(
        "✈️ Режим «В самолёте»" to "Отключает все беспроводные модули (Wi-Fi, Bluetooth, сотовая связь). Если он включён, Wi-Fi не будет работать даже при активном переключателе. Проверьте шторку уведомлений – значок самолётика должен быть серым.",
        "🔄 Перезагрузка роутера" to "Роутер – это маленький компьютер. Он может зависать. Выключение из розетки на 30 секунд очищает его память. В 80% случаев это решает проблему. После включения подождите 2-3 минуты, пока загорятся все индикаторы.",
        "📶 Включение Wi-Fi на телефоне" to "Иногда Wi-Fi случайно выключают. Проверьте: Настройки → Сеть и интернет → Wi-Fi → включите. Телефон должен увидеть список доступных сетей.",
        "🔑 Ввод пароля" to "Если сеть защищена паролем, его нужно ввести. Посмотреть пароль можно на обратной стороне роутера (обычно наклейка) или спросить у того, кто настраивал интернет.",
        "📉 Экономия данных" to "Режим экономии данных может блокировать фоновую передачу через Wi-Fi. Отключайте его при проблемах. Настройки → Сеть и интернет → Экономия данных → выключить.",
        "📱 Проверка других устройств" to "Если интернет не работает и на ноутбуке, телевизоре или другом телефоне – проблема у провайдера. Позвоните в поддержку (номер в договоре).",
        "🗑️ Забыть сеть и подключиться заново" to "Нажмите на свою сеть в списке Wi-Fi → «Забыть». Затем выберите сеть снова и введите пароль. Это сбрасывает возможные ошибки соединения.",
        "⚙️ Сброс сетевых настроек" to "Крайняя мера. Настройки → Система → Сброс → Сброс сетевых настроек. ВНИМАНИЕ: после этого телефон забудет все пароли Wi-Fi и Bluetooth-устройства."
    )
    val allRevealed = revealed.size == theories.size

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
                    Text("📡 Почему нет интернета? Полное руководство", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Проблемы с Wi-Fi случаются у всех. Ниже – 8 основных причин и их решение. Нажмите на каждую карточку, чтобы узнать подробности. Каждая карточка даёт +5 очков.",
                        fontSize = 16.sp, lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = revealed.size.toFloat() / theories.size,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF2C5F6E)
                    )
                    Text("Изучено: ${revealed.size} из ${theories.size}", fontSize = 14.sp, color = Color(0xFF2C5F6E))
                }
            }
        }
        items(theories.size) { index ->
            val (title, description) = theories[index]
            Card(
                modifier = Modifier.fillMaxWidth().clickable { onReveal(index) },
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
                                Text(description, fontSize = 16.sp, lineHeight = 24.sp, color = Color.DarkGray)
                            }
                        }
                    }
                    if (index in revealed) {
                        Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2E8058), modifier = Modifier.size(28.dp))
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
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Начать диагностику", fontSize = 18.sp, color = Color.White)
            }
            if (!allRevealed) {
                Text("Изучите все причины, чтобы продолжить (каждая даёт +5 очков)", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==================== ШАГ 1: РЕЖИМ «В САМОЛЁТЕ» ====================
@Composable
private fun AirplaneModeScreen(
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
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
                    Text("✈️ Шаг 1: Режим «В самолёте»", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Режим «В самолёте» отключает все беспроводные сигналы: Wi-Fi, Bluetooth, мобильную сеть. Если он включён, Wi-Fi не будет работать, даже если переключатель Wi-Fi активен.\n\n" +
                                "✅ **Как проверить:** Опустите шторку уведомлений (смахните сверху вниз). Найдите значок самолётика. Если он подсвечен (синий/оранжевый) – режим включён. Нажмите на него, чтобы выключить.\n\n" +
                                "📌 **Почему это важно?** Многие забывают про этот режим и думают, что сломался телефон.\n\n" +
                                "✅ **Подтвердите, что вы проверили и выключили режим «В самолёте»:**",
                        fontSize = 16.sp, lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.AirplanemodeActive, contentDescription = null, modifier = Modifier.size(56.dp), tint = Color(0xFF2C5F6E))
                    }
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Режим «В самолёте» выключен", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                        Text("(проверьте в шторке уведомлений)", fontSize = 14.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = checked,
                        onCheckedChange = {
                            onCheckedChange(it)
                            if (it && !checked) {
                                localScore += 15
                                onScoreUpdate(localScore)
                            }
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E))
                    )
                }
            }
        }
        if (checked) {
            item {
                Text("✅ Отлично! Режим полёта не мешает Wi-Fi. +15 очков", fontSize = 16.sp, color = Color(0xFF2E8058))
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
                Text("❌ Подтвердите, что вы выключили режим «В самолёте», чтобы продолжить", fontSize = 16.sp, color = Color( 0xFF9B0C3F))
            }
        }
        item {
            Text("🏆 Очки: $localScore", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E), modifier = Modifier.padding(8.dp))
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==================== ШАГ 2: ПЕРЕЗАГРУЗКА РОУТЕРА ====================
@Composable
private fun RouterRestartScreen(
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
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
                    Text("🔄 Шаг 2: Перезагрузите роутер", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Роутер – это маленький компьютер. Он может зависнуть из-за перегрева, перегрузки или ошибок в прошивке. Перезагрузка очищает временную память и часто решает проблему.\n\n" +
                                "✅ **Как перезагрузить:**\n" +
                                "1. Выключите роутер из розетки.\n" +
                                "2. Подождите 30 секунд (можно посчитать до 30).\n" +
                                "3. Включите роутер обратно.\n" +
                                "4. Подождите 2-3 минуты, пока загорятся все индикаторы (питание, интернет, Wi-Fi).\n\n" +
                                "📌 **Почему это важно?** Это сбрасывает временные ошибки и освобождает память роутера.\n\n" +
                                "✅ **Подтвердите, что вы перезагрузили роутер:**",
                        fontSize = 16.sp, lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.SettingsRemote, contentDescription = null, modifier = Modifier.size(56.dp), tint = Color(0xFF2C5F6E))
                    }
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Я перезагрузил роутер", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                        Text("(выключил из розетки на 30 секунд)", fontSize = 14.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = checked,
                        onCheckedChange = {
                            onCheckedChange(it)
                            if (it && !checked) {
                                localScore += 20
                                onScoreUpdate(localScore)
                            }
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E))
                    )
                }
            }
        }
        if (checked) {
            item {
                Text("✅ Отлично! Роутер перезагружен. +20 очков", fontSize = 16.sp, color =Color(0xFF2E8058))
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
                Text("❌ Перезагрузите роутер, чтобы продолжить", fontSize = 16.sp, color = Color( 0xFF9B0C3F))
            }
        }
        item {
            Text("🏆 Очки: $localScore", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E), modifier = Modifier.padding(8.dp))
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==================== ШАГ 3: НАСТРОЙКИ WI-FI ====================
@Composable
private fun WifiSettingsScreen(
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    wifiToggled: Boolean,
    onWifiToggledChange: (Boolean) -> Unit,
    passwordEntered: Boolean,
    onPasswordEnteredChange: (Boolean) -> Unit,
    onComplete: () -> Unit
) {
    var localScore by remember(score) { mutableStateOf(score) }
    var wifiBonusAdded by remember { mutableStateOf(false) }
    var passwordBonusAdded by remember { mutableStateOf(false) }

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
                    Text("📶 Шаг 3: Проверьте настройки Wi-Fi", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Иногда Wi-Fi может быть выключен, или телефон «забыл» пароль. Нужно проверить два действия:\n\n" +
                                "✅ **1. Включите Wi-Fi:**\n" +
                                "Настройки → Сеть и интернет → Wi-Fi → переключатель в положение ВКЛ.\n\n" +
                                "✅ **2. Выберите сеть и введите пароль:**\n" +
                                "Из списка доступных сетей нажмите на свою. Если требуется пароль – введите его. Посмотреть пароль можно на обратной стороне роутера (обычно наклейка с надписью «Wi-Fi Password»).\n\n" +
                                "📌 **Почему это важно?** Без активного Wi-Fi и правильного пароля подключение невозможно.\n\n" +
                                "✅ **Подтвердите оба действия:**",
                        fontSize = 16.sp, lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Wifi, contentDescription = null, modifier = Modifier.size(56.dp), tint = Color(0xFF2C5F6E))
                    }
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Я включил Wi-Fi", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                        Text("(переключатель в настройках)", fontSize = 14.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = wifiToggled,
                        onCheckedChange = {
                            onWifiToggledChange(it)
                            if (it && !wifiBonusAdded) {
                                wifiBonusAdded = true
                                localScore += 15
                                onScoreUpdate(localScore)
                            }
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E))
                    )
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Я ввёл пароль от своей сети", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                        Text("(если потребовалось)", fontSize = 14.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = passwordEntered,
                        onCheckedChange = {
                            onPasswordEnteredChange(it)
                            if (it && !passwordBonusAdded) {
                                passwordBonusAdded = true
                                localScore += 10
                                onScoreUpdate(localScore)
                            }
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E))
                    )
                }
            }
        }
        if (wifiToggled && passwordEntered) {
            item {
                Text("✅ Отлично! Wi-Fi включён, пароль введён. +25 очков", fontSize = 16.sp, color =Color(0xFF2E8058))
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
                Text("❌ Выполните оба действия, чтобы продолжить", fontSize = 16.sp, color = Color( 0xFF9B0C3F))
            }
        }
        item {
            Text("🏆 Очки: $localScore", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E), modifier = Modifier.padding(8.dp))
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==================== ШАГ 4: ЭКОНОМИЯ ДАННЫХ ====================
@Composable
private fun DataSaverScreen(
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
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
                    Text("📉 Шаг 4: Режим экономии данных", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Режим экономии данных предназначен для мобильного интернета, но иногда он может блокировать и Wi-Fi, особенно фоновую активность приложений.\n\n" +
                                "✅ **Как проверить:**\n" +
                                "Настройки → Сеть и интернет → Экономия данных → переключатель в положение ВЫКЛ (если включён).\n\n" +
                                "📌 **Почему это важно?** Некоторые телефоны ограничивают передачу данных через Wi-Fi, если включён режим экономии.\n\n" +
                                "✅ **Подтвердите, что вы проверили и отключили экономию данных (если она была включена):**",
                        fontSize = 16.sp, lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.DataSaverOff, contentDescription = null, modifier = Modifier.size(56.dp), tint = Color(0xFF2C5F6E))
                    }
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Экономия данных отключена", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                        Text("(проверьте в настройках)", fontSize = 14.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = checked,
                        onCheckedChange = {
                            onCheckedChange(it)
                            if (it && !checked) {
                                localScore += 15
                                onScoreUpdate(localScore)
                            }
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E))
                    )
                }
            }
        }
        if (checked) {
            item {
                Text("✅ Отлично! Экономия данных не мешает Wi-Fi. +15 очков", fontSize = 16.sp, color = Color(0xFF2E8058))
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onComplete,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Завершить диагностику", fontSize = 18.sp, color = Color.White)
                }
            }
        } else {
            item {
                Text("❌ Подтвердите, что экономия данных отключена, чтобы продолжить", fontSize = 16.sp, color = Color( 0xFF9B0C3F))
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
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🎉 ПОЗДРАВЛЯЕМ!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Вы научились диагностировать Wi-Fi!", fontSize = 20.sp, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Заработано очков: $score", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(24.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("💡 Памятка по диагностике Wi-Fi", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "1️⃣ Сначала проверьте режим «В самолёте».\n" +
                                        "2️⃣ Перезагрузите роутер – это решает 80% проблем.\n" +
                                        "3️⃣ Убедитесь, что Wi-Fi на телефоне включён.\n" +
                                        "4️⃣ Правильно введите пароль (смотрите на роутере).\n" +
                                        "5️⃣ Отключите экономию данных.\n" +
                                        "6️⃣ Проверьте другие устройства – если у всех нет интернета, звоните провайдеру.\n" +
                                        "7️⃣ Забудьте сеть и подключитесь заново.\n" +
                                        "8️⃣ Сброс сетевых настроек – крайняя мера.\n\n" +
                                        "📌 Запомните: Wi-Fi можно починить в 9 случаях из 10 без вызова мастера!",
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