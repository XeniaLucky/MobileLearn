package com.example.diplom2.screen.dop_content.lessons_medium

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Memory
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
fun LessonMemoryCleanScreen(navController: NavController, userId: Long) {
    var deletedAppsCount by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
        val lessonKey = "game_memory"
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
                    Text("🧹 Оптимизация памяти телефона", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Со временем телефон может замедляться, тормозить и даже зависать. Почему это происходит?\n\n" +
                                "❌ **Причины нехватки памяти:**\n" +
                                "• Ненужные приложения, которые вы установили и забыли.\n" +
                                "• Кэш приложений – временные файлы, которые накапливаются.\n" +
                                "• Десятки гигабайт фото, видео, сообщений.\n" +
                                "• Загрузки (папка Download) – там часто лежат старые файлы.\n\n" +
                                "✅ **Что делать для очистки:**\n" +
                                "1️⃣ Удалите приложения, которыми не пользуетесь (Настройки → Приложения → Выбрать → Удалить).\n" +
                                "2️⃣ Очистите кэш каждого приложения (там же – «Очистить кэш»).\n" +
                                "3️⃣ Перенесите фото и видео на карту памяти или в облако (Google Фото, Яндекс Диск).\n" +
                                "4️⃣ Используйте встроенные средства очистки: «Файлы» от Google (очищает мусор, дубликаты).\n" +
                                "5️⃣ Удалите старые чаты и медиа из мессенджеров (Telegram, WhatsApp).\n\n" +
                                "🚫 **Чего НЕ делать:**\n" +
                                "• Не удаляйте системные файлы, если не знаете, за что они отвечают.\n" +
                                "• Не пользуйтесь «ускорителями» из неизвестных источников.\n" +
                                "• Не очищайте кэш приложений, которыми активно пользуетесь (например, карты, браузер) – это замедлит их.\n\n" +
                                "🎯 **Задание:** удалите ненужные приложения, затем очистите кэш. Выполняйте действия ниже.",
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
                Text("Начать очистку", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 1: Удаление приложений (нужно удалить ВСЕ, иначе не перейти)
    if (step == 1) {
        val apps = listOf(
            "Facebook (1.2 ГБ)" to "Редко используете? Удалите!",
            "Неиспользуемая игра (2.5 ГБ)" to "Занимает много места",
            "Instagram (800 МБ)" to "Можно очистить кэш позже",
            "YouTube (500 МБ)" to "Смотрите видео в браузере"
        )
        var remainingApps by remember { mutableStateOf(apps.toMutableList()) }

        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
            ) {
                Text(
                    text = "🗑️ Удалите ВСЕ приложения из списка. Нажмите на корзину рядом с каждым.\n" +
                            "Подсказка: в реальном телефоне зайдите в Настройки → Приложения → выберите приложение → Удалить.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(remainingApps.size) { index ->
                    val (appName, hint) = remainingApps[index]
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
                            Column(modifier = Modifier.weight(1f)) {
                                Text(appName, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                                Text(hint, fontSize = 12.sp, color = Color.Gray)
                            }
                            IconButton(onClick = {
                                remainingApps = remainingApps.toMutableList().apply { removeAt(index) }
                                score += 10
                                if (remainingApps.isEmpty()) {
                                    step = 2
                                }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = Color(0xFF2C5F6E))
                            }
                        }
                    }
                }
            }

            if (remainingApps.isNotEmpty()) {
                Text("Осталось удалить: ${remainingApps.size} приложений", fontSize = 12.sp, color = Color.Red)
            } else {
                Text("✅ Все приложения удалены! Теперь можно переходить к очистке кэша.", fontSize = 14.sp, color = Color.Green)
            }
            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 2: Очистка кэша (интерактивная)
    if (step == 2) {
        var cacheCleared by remember { mutableStateOf(false) }

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
                    Text("🧹 Очистка кэша", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Кэш – это временные файлы, которые приложения сохраняют для ускорения работы. Например, кэш YouTube – это превью видео.\n\n" +
                                "Как очистить кэш на реальном телефоне:\n" +
                                "Настройки → Приложения → YouTube → Хранилище → Очистить кэш.\n\n" +
                                "⚠️ ВНИМАНИЕ: кэш можно безопасно очистить, это не удалит личные данные (логины, пароли, сообщения).",
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth().clickable(enabled = !cacheCleared) { cacheCleared = true },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (cacheCleared) Color(0xFFC4D7DB) else Color.White
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Очистить кэш YouTube", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    if (!cacheCleared) {
                        Button(
                            onClick = { cacheCleared = true; score += 25 },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                        ) { Text("Очистить", color = Color.White) }
                    } else {
                        Icon(Icons.Default.Memory, contentDescription = null, tint = Color.Green)
                    }
                }
            }

            if (cacheCleared) {
                Text("✅ Кэш очищен! Молодец.", color = Color.Green)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { step = 3 },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                ) { Text("Готово", color = Color.White) }
            } else {
                Text("❌ Нажмите «Очистить», чтобы продолжить", color = Color.Red)
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
                Text("🎉 Поздравляем! Вы освободили память телефона.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Вы заработали $score очков.", color = Color(0xFF2C5F6E), fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "💡 Дополнительные советы:\n" +
                            "• Удаляйте неиспользуемые приложения раз в месяц.\n" +
                            "• Переносите фото в облако (Google Фото, Яндекс Диск) и очищайте память телефона.\n" +
                            "• Не храните большие файлы в мессенджерах – скачивайте их и удаляйте.\n" +
                            "• Включите автоматическую очистку кэша в настройках телефона (если есть).",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "game_memory", true)
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                ) { Text("Завершить", color = Color.White) }
            }
        }
    }
}