package com.example.diplom2.screen.dop_content.lessons_light

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.diplom2.screen.saveLessonProgress
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraLessonScreen(navController: NavController, userId: Long) {
    var step by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var photoTaken by remember { mutableStateOf(false) }
    var selectedMode by remember { mutableStateOf("photo") }
    var flashEnabled by remember { mutableStateOf(false) }
    var hdrEnabled by remember { mutableStateOf(false) }
    var zoomLevel by remember { mutableFloatStateOf(1f) }
    var photoSaved by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    if (step == 0) {
        // ТЕОРИЯ – подробное руководство по камере
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFEFE3D3)).padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📸 Камера: как фотографировать, смотреть и управлять", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5A2B))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Камера – одна из самых важных функций телефона. Вот полное руководство:\n\n" +
                                "📌 **Как открыть камеру?**\n" +
                                "• Нажмите на значок камеры на главном экране (обычно 📸 или 🎥).\n" +
                                "• Быстрый способ: дважды нажмите кнопку питания (на некоторых телефонах).\n" +
                                "• Из блокированного экрана: проведите пальцем по значку камеры.\n\n" +
                                "📌 **Как сделать фото?**\n" +
                                "1. Наведите камеру на объект.\n" +
                                "2. Дождитесь фокусировки (зелёный квадрат или точки).\n" +
                                "3. Нажмите на круглую кнопку спуска (внизу экрана).\n" +
                                "4. Фото автоматически сохранится в галерею.\n\n" +
                                "📌 **Кнопка громкости как спуск**\n" +
                                "• Нажмите кнопку уменьшения громкости – сделает фото.\n" +
                                "• Удерживайте кнопку громкости – снимет серию фото.\n\n" +
                                "📌 **Режимы съёмки**\n" +
                                "• Фото – обычный режим.\n" +
                                "• Видео – запись роликов.\n" +
                                "• Портрет – размытие фона.\n" +
                                "• Ночной – для тёмного времени суток (телефон делает несколько снимков и объединяет их).\n" +
                                "• Панорама – широкоформатные фото.\n" +
                                "• Замедленная съёмка (Slow Mo) – красивые эффекты.\n\n" +
                                "📌 **Настройки камеры**\n" +
                                "• Вспышка – авто, всегда включена, выключена.\n" +
                                "• HDR – улучшает детали в тени и на свету (лучше держать включённым).\n" +
                                "• Таймер – 3, 5 или 10 секунд (чтобы успеть встать в кадр).\n" +
                                "• Соотношение сторон – 4:3 (максимальное качество), 16:9 (широкоэкранное).\n" +
                                "• Качество видео – 1080p (HD) или 4K (Ultra HD).\n\n" +
                                "📌 **Зум (приближение)**\n" +
                                "• Разведите два пальца на экране – увеличит.\n" +
                                "• Сведите пальцы – уменьшит.\n" +
                                "• На некоторых телефонах есть отдельная кнопка зума.\n\n" +
                                "📌 **Где смотреть фото?**\n" +
                                "• После съёмки нажмите на миниатюру фото в углу экрана.\n" +
                                "• Или откройте приложение «Галерея» или «Google Фото».\n" +
                                "• В галерее фото можно: увеличить пальцами, отправить, удалить, редактировать.\n\n" +
                                "📌 **Редактирование фото**\n" +
                                "• Обрезка – изменить размер и повернуть.\n" +
                                "• Цвета – яркость, контраст, насыщенность.\n" +
                                "• Фильтры – готовые эффекты.\n" +
                                "• Ретушь – убрать недостатки.\n\n" +
                                "📌 **Что делать, если камера не работает?**\n" +
                                "• Перезагрузите телефон.\n" +
                                "• Проверьте, не закрыто ли приложение камеры.\n" +
                                "• Очистите объектив мягкой тканью.\n" +
                                "• Дайте разрешение камере (Настройки → Приложения → Камера → Разрешения).\n\n" +
                                "🎯 **Задание:** пройдите симуляцию фотосъёмки, настраивая параметры.",
                        fontSize = 16.sp,
                        lineHeight = 22.sp
                    )
                }
            }
            Button(
                onClick = { step = 1 },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Начать симуляцию камеры", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 1: Симулятор камеры – настройки и съёмка
    if (step == 1) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFEFE3D3)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📸 Симулятор камеры", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Выберите настройки, затем сделайте фото.", fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Видоискатель
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color(0xFFC4D7DB), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(80.dp), tint = Color(0xFF8B5A2B))
                    Text("Здесь будет ваше фото", fontSize = 12.sp, color = Color(0xFF8B5A2B))
                    Text("Наведите на объект и нажмите спуск", fontSize = 12.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Настройки камеры
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Вспышка
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        if (flashEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                        contentDescription = "Вспышка",
                        modifier = Modifier.size(32.dp).clickable { flashEnabled = !flashEnabled },
                        tint = if (flashEnabled) Color(0xFFFFD700) else Color.Gray
                    )
                    Text(if (flashEnabled) "Вкл" else "Выкл", fontSize = 10.sp)
                }
                // HDR
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        if (hdrEnabled) Icons.Default.Tune else Icons.Default.Tune,
                        contentDescription = "HDR",
                        modifier = Modifier.size(32.dp).clickable { hdrEnabled = !hdrEnabled },
                        tint = if (hdrEnabled) Color(0xFF8B5A2B) else Color.Gray
                    )
                    Text("HDR", fontSize = 10.sp)
                }
                // Режим съёмки
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        when (selectedMode) {
                            "photo" -> Icons.Default.PhotoCamera
                            "video" -> Icons.Default.Videocam
                            "portrait" -> Icons.Default.Person
                            else -> Icons.Default.PhotoCamera
                        },
                        contentDescription = "Режим",
                        modifier = Modifier.size(32.dp).clickable {
                            selectedMode = when (selectedMode) {
                                "photo" -> "video"
                                "video" -> "portrait"
                                else -> "photo"
                            }
                        },
                        tint = Color(0xFF8B5A2B)
                    )
                    Text(when (selectedMode) {
                        "photo" -> "Фото"
                        "video" -> "Видео"
                        "portrait" -> "Портрет"
                        else -> "Фото"
                    }, fontSize = 10.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка спуска
            Button(
                onClick = {
                    photoTaken = true
                    score += 25
                    step = 2
                },
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
            ) {
                Icon(Icons.Default.Lens, contentDescription = "Спуск", modifier = Modifier.size(48.dp), tint = Color.White)
            }

            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF8B5A2B))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 2: Просмотр фото в галерее
    if (step == 2) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFEFE3D3)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🖼️ ШАГ 2: Просмотр фото в галерее", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Нажмите на миниатюру фото (в углу экрана), чтобы открыть снимок.", fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Имитация миниатюры и галереи
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { photoSaved = true },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFC4D7DB))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = "Галерея", modifier = Modifier.size(48.dp), tint = Color(0xFF8B5A2B))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Последнее фото", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Дата: сегодня, время: сейчас", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (photoSaved) {
                Text("✅ Фото открыто! Вы можете увеличить его пальцами, отправить или удалить.", color = Color(0xFF2E8058))
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { step = 3; score += 15 },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
                ) { Text("Далее", color = Color.White) }
            } else {
                Text("❌ Нажмите на фото, чтобы открыть галерею", color = Color( 0xFF9B0C3F))
            }

            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF8B5A2B))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ФИНАЛЬНЫЙ ЭКРАН
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFEFE3D3)).padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🎉 Поздравляем! Вы научились пользоваться камерой.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Вы заработали $score очков.", color = Color(0xFF8B5A2B), fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "💡 ИТОГОВЫЕ СОВЕТЫ ПО КАМЕРЕ:\n" +
                            "• Держите телефон двумя руками – снимки будут чёткими.\n" +
                            "• Протирайте объектив перед съёмкой (мягкой тканью).\n" +
                            "• Используйте HDR для контрастных сцен (солнце и тени).\n" +
                            "• Ночной режим требует неподвижности – держите телефон крепко или используйте штатив.\n" +
                            "• Фото сохраняются в папке DCIM – вы можете перенести их на компьютер.\n" +
                            "• В галерее фото можно отправить по WhatsApp, Telegram, по почте и т.д.\n" +
                            "• Не храните тысячи фото в телефоне – переносите их в облако (Google Фото) или на компьютер.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "game_camera", true)
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
                ) { Text("Завершить", color = Color.White) }
            }
        }
    }
}