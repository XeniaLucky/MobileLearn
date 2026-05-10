package com.example.diplom2.screen.dop_content.lessons_medium

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.diplom2.screen.saveLessonProgress
import com.example.diplom2.screen.setActiveLessonMedium
import com.example.diplom2.screen.updateLessonProgressMedium
import kotlinx.coroutines.launch
import kotlinx.coroutines.launch
import com.example.diplom2.screen.saveLessonProgress
import com.example.diplom2.screen.saveLastStepMedium
import com.example.diplom2.screen.getLastStepMedium
import com.example.diplom2.screen.activateLessonMedium
import com.example.diplom2.screen.getActiveLessonMedium
import com.example.diplom2.screen.updateLessonProgressMedium


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDigitalWellbeingScreen(navController: NavController, userId: Long) {
    var selectedHours by remember { mutableStateOf<Int?>(null) }
    var nightModeEnabled by remember { mutableStateOf(false) }
    var reminderSet by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
        val lessonKey = "game_digitalwellbeing"
        val totalSteps = 5

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
    if (step == 0) {
        // ТЕОРИЯ
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🧘 Цифровое благополучие: как не утонуть в телефоне", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Телефон — отличный помощник, но он же может красть наше время и внимание. Цифровое благополучие помогает:\n\n" +
                                "✅ **Что делать:**\n" +
                                "• Установить лимиты на приложения (соцсети, игры, новости).\n" +
                                "• Включить «Ночной режим» – экран становится тёплым и не бьёт по глазам.\n" +
                                "• Настроить «Не беспокоить» на время сна.\n" +
                                "• Использовать таймер перерывов напоминания.\n" +
                                "• Отключать уведомления от ненужных приложений.\n\n" +
                                "❌ **Чего избегать:**\n" +
                                "• Не сидеть в телефоне перед сном (синий свет мешает заснуть).\n" +
                                "• Не проверять соцсети каждые 5 минут.\n" +
                                "• Не игнорировать напоминания о перерывах.\n\n" +
                                "🎯 **Задание:** настройте параметры ниже.",
                        fontSize = 16.sp,
                        lineHeight = 22.sp
                    )
                }
            }
            Button(
                onClick = { step = 1 },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
            ) { Text("Настроить", color = Color.White) }
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    if (step == 1) {
        // ШАГ 1: Установка лимита времени
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("⏰ Установите лимит времени на приложения", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Сколько часов в день вы хотите проводить в соцсетях и играх?", fontSize = 14.sp)
                    Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF2C5F6E))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { selectedHours = 1; score += 20; step = 2 },
                    colors = ButtonDefaults.buttonColors(containerColor = if (selectedHours == 1) Color(0xFF2C5F6E) else Color(0xFF2C5F6E).copy(alpha = 0.6f))
                ) { Text("1 час", color = Color.White) }
                Button(
                    onClick = { selectedHours = 2; score += 15; step = 2 },
                    colors = ButtonDefaults.buttonColors(containerColor = if (selectedHours == 2) Color(0xFF2C5F6E) else Color(0xFF2C5F6E).copy(alpha = 0.6f))
                ) { Text("2 часа", color = Color.White) }
                Button(
                    onClick = { selectedHours = 3; score += 10; step = 2 },
                    colors = ButtonDefaults.buttonColors(containerColor = if (selectedHours == 3) Color(0xFF2C5F6E) else Color(0xFF2C5F6E).copy(alpha = 0.6f))
                ) { Text("3 часа", color = Color.White) }
            }
            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF2C5F6E))
            if (selectedHours == null) {
                Text("⚠️ Выберите лимит времени, чтобы продолжить", fontSize = 12.sp, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    if (step == 2) {
        // ШАГ 2: Включение ночного режима
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🌙 Включите «Ночной режим»", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Ночной режим делает экран теплее и снижает нагрузку на глаза. Включается в настройках: Дисплей → Ночной режим.", fontSize = 14.sp)
                    Icon(Icons.Default.Nightlight, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF2C5F6E))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Ночной режим", fontSize = 16.sp)
                Switch(checked = nightModeEnabled, onCheckedChange = { nightModeEnabled = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E)))
            }
            if (nightModeEnabled) {
                Text("✅ Ночной режим включён", color = Color.Green)
                Button(
                    onClick = { score += 25; step = 3 },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                ) { Text("Далее", color = Color.White) }
            } else {
                Text("❌ Включите ночной режим, чтобы продолжить", color = Color.Red)
            }
            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    if (step == 3) {
        // ШАГ 3: Напоминание о перерыве
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("⌛ Установите напоминание о перерыве", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Напоминания о перерыве здорово помогают отвлекаться от телефона. Настройте их в Цифровом благополучии.", fontSize = 14.sp)
                    Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF2C5F6E))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Включить напоминания о перерывах", fontSize = 16.sp)
                Switch(checked = reminderSet, onCheckedChange = { reminderSet = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E)))
            }
            if (reminderSet) {
                Text("✅ Напоминания включены", color = Color.Green)
                Button(
                    onClick = { score += 20; step = 4 },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                ) { Text("Далее", color = Color.White) }
            } else {
                Text("❌ Включите напоминания, чтобы продолжить", color = Color.Red)
            }
            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ФИНАЛЬНЫЙ ЭКРАН
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
                Text("🎉 Поздравляем! Вы настроили цифровое благополучие.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Вы заработали $score очков.", color = Color(0xFF2C5F6E), fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "💡 Дополнительные советы:\n" +
                            "• Удалите приложения, которыми не пользуетесь – меньше соблазнов.\n" +
                            "• Включите чёрно-белый режим экрана (Монохромность) – он снижает привлекательность телефона.\n" +
                            "• Отключите уведомления для всех приложений, кроме самых важных.\n" +
                            "• Проводите «цифровой детокс» – хотя бы час без телефона перед сном.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "game_digitalwellbeing", true)
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                ) { Text("Завершить", color = Color.White) }
            }
        }
    }
}