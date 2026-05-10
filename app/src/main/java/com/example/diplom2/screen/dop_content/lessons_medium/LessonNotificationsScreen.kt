package com.example.diplom2.screen.dop_content.lessons_medium

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
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
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.example.diplom2.screen.setActiveLessonMedium
import com.example.diplom2.screen.updateLessonProgressMedium
import kotlinx.coroutines.launch
import com.example.diplom2.screen.saveLessonProgress
import com.example.diplom2.screen.saveLastStepMedium
import com.example.diplom2.screen.getLastStepMedium
import com.example.diplom2.screen.activateLessonMedium
import com.example.diplom2.screen.getActiveLessonMedium
import com.example.diplom2.screen.updateLessonProgressMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonNotificationsScreen(navController: NavController, userId: Long) {
    var disabledAppsCount by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
        val lessonKey = "game_notifications"
        val totalSteps = 4

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
    // ТЕОРЕТИЧЕСКАЯ ЧАСТЬ (расширенная)
    if (step == 0) {
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
                    Text("📱 Управление уведомлениями", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Уведомления — это сообщения от приложений. Они помогают не пропустить важное, но могут отвлекать и снижать концентрацию.\n\n" +
                                "✅ **Полезные уведомления:** сообщения от близких, напоминания из календаря, важные события, уведомления банка.\n" +
                                "❌ **Вредные:** реклама, новости, игры, соцсети, постоянные «предложения» и скидки.\n\n" +
                                "📌 **Как навести порядок в уведомлениях:**\n" +
                                "1️⃣ Откройте «Настройки» → «Уведомления».\n" +
                                "2️⃣ Выберите приложение и отключите ненужные категории (например, для новостей оставьте только «Важное»).\n" +
                                "3️⃣ Включите режим «Не беспокоить» на время работы, учёбы или сна.\n" +
                                "4️⃣ Настройте «Умные уведомления» — некоторые телефоны умеют группировать и фильтровать.\n\n" +
                                "⚠️ **Чего избегать:**\n" +
                                "• Полного отключения всех уведомлений — можете пропустить важный звонок.\n" +
                                "• Разрешать уведомления всем приложениям подряд — быстро устанете от шума.\n" +
                                "• Игнорировать настройки — несколько минут настройки сэкономят часы отвлечений.\n\n" +
                                "🎯 **Задание:** настройте уведомления в симуляторе ниже.",
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
                Text("Настроить уведомления", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 1: Отключение ненужных уведомлений (нужно отключить ВСЕ раздражающие)
    if (step == 1) {
        val apps = listOf(
            "Сообщения (важно)" to true,
            "Facebook (реклама)" to false,
            "Telegram (чаты)" to true,
            "Игры (уведомления)" to false,
            "Новости" to false,
            "Скидки Магнит" to false,
            "Погода" to false
        )
        var remainingApps by remember { mutableStateOf(apps.toMutableList()) }
        val distractingCount = remainingApps.count { !it.second }

        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
            ) {
                Text(
                    text = "🔕 Отключите уведомления для ВСЕХ раздражающих приложений (реклама, новости, игры).\n" +
                            "Важные (Сообщения, Telegram) должны остаться включёнными.\n\n" +
                            "Нажмите на крестик 🔴 рядом с каждым ненужным приложением.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(remainingApps.size) { index ->
                    val (appName, isImportant) = remainingApps[index]
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Notifications,
                                    contentDescription = null,
                                    tint = if (isImportant) Color(0xFF2C5F6E) else Color.Gray
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(appName, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                                    if (isImportant) {
                                        Text("Оставьте включённым", fontSize = 11.sp, color = Color(0xFF2C5F6E))
                                    } else {
                                        Text("Отключите, чтобы не отвлекало", fontSize = 11.sp, color = Color.Gray)
                                    }
                                }
                            }
                            if (!isImportant) {
                                IconButton(onClick = {
                                    remainingApps = remainingApps.toMutableList().apply { removeAt(index) }
                                    score += 10
                                    if (remainingApps.filter { !it.second }.isEmpty()) {
                                        step = 2
                                    }
                                }) {
                                    Icon(Icons.Default.Close, contentDescription = "Отключить", tint = Color.Red)
                                }
                            } else {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Важно", tint = Color(0xFF2C5F6E))
                            }
                        }
                    }
                }
            }

            if (distractingCount > 0) {
                Text("Осталось отключить: $distractingCount раздражающих приложений", fontSize = 12.sp, color = Color.Red)
            } else {
                Text("✅ Все раздражающие уведомления отключены! Теперь переходим к режиму «Не беспокоить».", fontSize = 14.sp, color = Color.Green)
            }
            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 2: Включение режима "Не беспокоить"
    if (step == 2) {
        var doNotDisturbEnabled by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🔕 Режим «Не беспокоить»", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Режим «Не беспокоить» отключает все звуки и вибрацию, кроме важных звонков (которые вы разрешите).\n\n" +
                                "Как включить на реальном телефоне:\n" +
                                "• Быстро: опустите шторку уведомлений → нажмите на значок «Не беспокоить».\n" +
                                "• В настройках: Настройки → Звук → Не беспокоить → Включить.\n\n" +
                                "Нажмите кнопку ниже, чтобы активировать режим в симуляторе.",
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (!doNotDisturbEnabled) {
                Button(
                    onClick = {
                        doNotDisturbEnabled = true
                        score += 30
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                ) {
                    Text("Включить режим «Не беспокоить»", color = Color.White)
                }
            } else {
                Text("✅ Режим «Не беспокоить» активирован!", color = Color.Green, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { step = 3 },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                ) { Text("Готово", color = Color.White) }
            }

            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ФИНАЛЬНЫЙ ЭКРАН с итоговыми советами
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
                Text("🎉 Поздравляем! Вы навели порядок в уведомлениях.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Вы заработали $score очков.", color = Color(0xFF2C5F6E), fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "💡 Дополнительные советы:\n" +
                            "• Раз в месяц проверяйте список приложений, которым вы дали разрешение на уведомления.\n" +
                            "• Настройте «Тихие уведомления» для неважных чатов.\n" +
                            "• Используйте будильник и таймер «Не беспокоить» на ночь.\n" +
                            "• Некоторые приложения (например, банки) позволяют выбрать, какие именно уведомления получать (операции, акции).",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "game_notifications", true)
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                ) { Text("Завершить", color = Color.White) }
            }
        }
    }
}