package com.example.diplom2.screen.dop_content.lessons_medium

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
fun LessonBatterySaverScreen(navController: NavController, userId: Long) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lessonKey = "game_battery"
    val totalSteps = 3   // шаги 0,1,2

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
        // ТЕОРЕТИЧЕСКАЯ ЧАСТЬ (расширенная)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFC4D7DB))
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "🔋 Экономия батареи: продлеваем жизнь телефона",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C5F6E)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Аккумулятор – сердце смартфона. Чтобы он работал дольше без подзарядки и не изнашивался раньше времени, следуйте этим правилам:\n\n" +
                                "✅ **Что делать:**\n" +
                                "• Уменьшить яркость экрана (автояркость не всегда оптимальна).\n" +
                                "• Выключить ненужные беспроводные модули (Wi-Fi, Bluetooth, GPS, NFC).\n" +
                                "• Отключить автосинхронизацию данных (почта, облако, приложения).\n" +
                                "• Включить режим энергосбережения (ограничивает фоновые процессы).\n" +
                                "• Закрывать неиспользуемые приложения (но не все – систему это не ускоряет).\n" +
                                "• Использовать тёмную тему (особенно на AMOLED-экранах).\n" +
                                "• Установить короткое время автоматической блокировки экрана.\n" +
                                "• Отключить вибрацию и звуки нажатия клавиш.\n" +
                                "• Не допускать полного разряда (оптимально 20–80%).\n\n" +
                                "❌ **Чего избегать:**\n" +
                                "• Держать телефон на солнце или в нагретом месте (батарея деградирует).\n" +
                                "• Использовать дешёвые зарядные устройства.\n" +
                                "• Оставлять приложения, использующие GPS, в фоне.\n" +
                                "• Постоянно держать яркость на 100%.\n\n" +
                                "🎯 **Задание:** настройте параметры симулятора ниже так, чтобы максимально сэкономить заряд.",
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
                Text("Приступить к настройке", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    if (step == 1) {
        // ИНТЕРАКТИВНАЯ НАСТРОЙКА (проверка обязательных действий)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFC4D7DB))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
            ) {
                Text(
                    text = "Настройте параметры для экономии заряда. Обязательно сделайте три вещи:\n" +
                            "1️⃣ Яркость ≤ 40%\n" +
                            "2️⃣ Выключите автосинхронизацию\n" +
                            "3️⃣ Включите режим энергосбережения\n\n" +
                            "Только после этого можно переходить к проверке.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Яркость
            Text("Яркость экрана: ${(brightness * 100).toInt()}%", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Slider(
                value = brightness,
                onValueChange = { brightness = it },
                valueRange = 0.2f..1f,
                colors = SliderDefaults.colors(thumbColor = Color(0xFF2C5F6E), activeTrackColor = Color(0xFF2C5F6E))
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Автосинхронизация
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Автосинхронизация данных", fontSize = 16.sp)
                Switch(checked = sync, onCheckedChange = { sync = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E)))
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Режим энергосбережения
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Режим энергосбережения", fontSize = 16.sp)
                Switch(checked = powerSaver, onCheckedChange = { powerSaver = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E)))
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage != null) {
                Text(errorMessage!!, color = Color.Red, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
            }

            Button(
                onClick = {
                    // Проверяем, выполнены ли все условия
                    if (brightness > 0.4f) {
                        errorMessage = "❌ Снизьте яркость до 40% или ниже (сейчас ${(brightness * 100).toInt()}%)"
                    } else if (sync) {
                        errorMessage = "❌ Отключите автосинхронизацию (выключите переключатель)"
                    } else if (!powerSaver) {
                        errorMessage = "❌ Включите режим энергосбережения"
                    } else {
                        // Все условия выполнены – начисляем очки и переходим к следующему шагу
                        score += 30
                        errorMessage = null
                        step = 2
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
            ) {
                Text("Проверить настройки", color = Color.White)
            }

            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(8.dp))
        }
        return
    }

    if (step == 2) {
        // ФИНАЛЬНЫЙ ЭКРАН (экологичные советы)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFC4D7DB))
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎉 Поздравляем! Вы освоили экономию батареи.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Вы заработали $score очков.", color = Color(0xFF2C5F6E), fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "💡 Дополнительные советы:\n" +
                                "• Используйте тёмную тему и живой фон с низким энергопотреблением.\n" +
                                "• Отключайте вибрацию и звуки касаний.\n" +
                                "• Не ставьте телефон на зарядку на ночь – это ускоряет износ батареи.\n" +
                                "• Калибруйте батарею раз в 3 месяца (разряд до 0% и полная зарядка до 100%).",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                saveLessonProgress(context, userId, "game_battery", true)
                                navController.popBackStack()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                    ) {
                        Text("Завершить", color = Color.White)
                    }
                }
            }
        }
        return
    }
}