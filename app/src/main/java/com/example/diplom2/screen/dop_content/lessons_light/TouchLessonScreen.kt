package com.example.diplom2.screen.dop_content.lessons_light

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
fun TouchLessonScreen(navController: NavController, userId: Long) {
    var step by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var feedback by remember { mutableStateOf("") }
    var longPressDetected by remember { mutableStateOf(false) }
    var swipeDetected by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    if (step == 0) {
        // ТЕОРИЯ – подробное руководство по сенсорному экрану
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
                    Text("🖐️ Сенсорный экран: как управлять телефоном", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5A2B))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Сенсорный экран реагирует на прикосновения. Вот основные жесты:\n\n" +
                                "📌 **Короткое касание (тап)**\n" +
                                "• Одно короткое нажатие пальцем.\n" +
                                "• Используется для: открытия приложений, выбора пунктов меню, нажатия кнопок.\n" +
                                "• Как тренироваться: нажимайте на иконки, кнопки, ссылки в интернете.\n\n" +
                                "📌 **Долгое нажатие (лонг тап)**\n" +
                                "• Нажмите и удерживайте палец на экране 1-2 секунды.\n" +
                                "• Используется для: вызова дополнительного меню, перемещения иконок, удаления приложений.\n" +
                                "• Примеры: зажмите иконку приложения → появится меню «Удалить» или «Информация».\n\n" +
                                "📌 **Свайп (проведение пальцем)**\n" +
                                "• Быстро проведите пальцем по экрану вверх, вниз, влево или вправо.\n" +
                                "• Используется для: листания между экранами, прокрутки списков, открытия шторки.\n" +
                                "• Примеры: свайп вверх – выход из приложения (на некоторых телефонах), свайп влево – удаление уведомления.\n\n" +
                                "📌 **Прокрутка (скролл)**\n" +
                                "• Медленное проведение пальцем вверх/вниз по списку.\n" +
                                "• Используется для: просмотра длинных страниц, лент новостей, списка контактов.\n" +
                                "• Совет: если список длинный, можно несколько раз провести пальцем.\n\n" +
                                "📌 **Зум (приближение и отдаление)**\n" +
                                "• Разведите два пальца в стороны – увеличить изображение.\n" +
                                "• Сведите два пальца вместе – уменьшить.\n" +
                                "• Используется в: картах, фото, браузере.\n\n" +
                                "📌 **Прокрутка с инерцией**\n" +
                                "• Резко проведите пальцем и отпустите – список будет прокручиваться по инерции.\n" +
                                "• Чтобы остановить, коснитесь экрана.\n\n" +
                                "📌 **Что делать, если экран не реагирует?**\n" +
                                "• Протрите экран мягкой тканью (иногда мешают грязь или капли воды).\n" +
                                "• Снимите защитную плёнку (если она мешает).\n" +
                                "• Перезагрузите телефон.\n" +
                                "• Высушите руки.\n\n" +
                                "🎯 **Задание:** выполните все жесты в симуляторе.",
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
                Text("Начать практику жестов", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 1: Короткое касание
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
                Text(
                    text = "1️⃣ Короткое касание (тап)\nНажмите на квадрат один раз.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color(0xFFC4D7DB), RoundedCornerShape(16.dp))
                    .clickable {
                        step = 2
                        score += 10
                        feedback = "✅ Короткое касание – выбор элемента."
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("Нажми", color = Color(0xFF8B5A2B), fontSize = 18.sp)
            }

            Text(feedback, fontSize = 12.sp, color = Color(0xFF8B5A2B))
            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF8B5A2B))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 2: Долгое нажатие
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
                Text(
                    text = "2️⃣ Долгое нажатие\nЗажмите квадрат на 1 секунду.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color(0xFFC4D7DB), RoundedCornerShape(16.dp))
                    .pointerInput(Unit) {
                        detectTapGestures(onLongPress = {
                            if (!longPressDetected) {
                                longPressDetected = true
                                step = 3
                                score += 15
                                feedback = "✅ Долгое нажатие – открывает доп. меню."
                            }
                        })
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("Зажми", color = Color(0xFF8B5A2B), fontSize = 18.sp)
            }

            Text(feedback, fontSize = 12.sp, color = Color(0xFF8B5A2B))
            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF8B5A2B))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 3: Свайп вправо
    if (step == 3) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFEFE3D3)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
            ) {
                Text(
                    text = "3️⃣ Свайп вправо\nПроведите пальцем по квадрату слева направо.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(200.dp, 100.dp)
                    .background(Color(0xFFC4D7DB), RoundedCornerShape(16.dp))
                    .pointerInput(Unit) {
                        detectDragGestures { change, _ ->
                            change.consume()
                            if (!swipeDetected) {
                                swipeDetected = true
                                step = 4
                                score += 20
                                feedback = "✅ Свайп – листание между экранами."
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("Свайпни вправо", color = Color(0xFF8B5A2B), fontSize = 18.sp)
            }

            Text(feedback, fontSize = 12.sp, color = Color(0xFF8B5A2B))
            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF8B5A2B))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 4: Прокрутка списка
    if (step == 4) {
        var scrolled by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFEFE3D3)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
            ) {
                Text(
                    text = "4️⃣ Прокрутка списка\nПотяните список вверх и вниз.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .height(200.dp)
                    .background(Color(0xFFC4D7DB), RoundedCornerShape(16.dp)),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items((1..15).toList()) { index ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Text(
                            "Элемент списка $index",
                            modifier = Modifier.padding(12.dp),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    scrolled = true
                    step = 5
                    score += 25
                    feedback = "✅ Прокрутка – список можно листать."
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
            ) {
                Text("Я полистал(а)", color = Color.White)
            }

            Text(feedback, fontSize = 12.sp, color = Color(0xFF8B5A2B))
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
                Text("🎉 Поздравляем! Вы освоили сенсорный экран.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Вы заработали $score очков.", color = Color(0xFF8B5A2B), fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "💡 ИТОГОВЫЕ СОВЕТЫ ПО СЕНСОРНОМУ ЭКРАНУ:\n" +
                            "• Для точных нажатий используйте подушечку пальца, а не ноготь.\n" +
                            "• Если экран не реагирует – перезагрузите телефон.\n" +
                            "• Чувствительность экрана можно настроить в специальных возможностях.\n" +
                            "• Для людей с тремором пальцев есть режим «Задержка нажатия».\n" +
                            "• Не нажимайте на экран мокрыми руками – это может вызвать ложные срабатывания.\n" +
                            "• Для скролла длинных страниц используйте быстрый свайп.\n" +
                            "• Значок лупы 🔍 вверху экрана означает, что можно провести пальцем и найти нужное (поиск по буквам).",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "game_touch", true)
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
                ) { Text("Завершить", color = Color.White) }
            }
        }
    }
}