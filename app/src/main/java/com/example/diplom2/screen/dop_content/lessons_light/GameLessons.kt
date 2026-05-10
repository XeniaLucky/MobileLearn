package com.example.diplom2.screen.dop_content.lessons_light

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
fun PowerLessonScreen(navController: NavController, userId: Long, onComplete: () -> Unit = {}) {
    var step by remember { mutableIntStateOf(0) }
    var holdProgress by remember { mutableFloatStateOf(0f) }
    var score by remember { mutableIntStateOf(0) }
    var restartAction by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(step) {
        if (step == 1) {
            while (holdProgress < 1f && step == 1) {
                delay(30)
                holdProgress += 0.01f
            }
            if (holdProgress >= 1f) {
                step = 2
                score += 10
            }
        }
    }

    if (step == 0) {
        // ТЕОРИЯ – подробное руководство по включению/выключению телефона
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
                    Text("🔋 Включение и выключение телефона: полное руководство", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5A2B))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Управление питанием телефона – первое, что нужно освоить. Вот подробная инструкция:\n\n" +
                                "📌 **Как включить телефон?**\n" +
                                "• Найдите кнопку питания (обычно на правом боку или сзади).\n" +
                                "• Нажмите и удерживайте её 3-5 секунд.\n" +
                                "• Когда экран загорится, отпустите кнопку.\n" +
                                "• Дождитесь полной загрузки (появится рабочий стол).\n\n" +
                                "📌 **Где находится кнопка питания?**\n" +
                                "• На большинстве телефонов – справа (иногда слева).\n" +
                                "• На некоторых старых моделях – на верхнем торце.\n" +
                                "• На iPhone – на правом боку.\n" +
                                "• Узнать точно: посмотрите в инструкции или поищите «кнопка питания» в интернете по модели телефона.\n\n" +
                                "📌 **Как выключить телефон?**\n" +
                                "• Нажмите и удерживайте кнопку питания 3-5 секунд.\n" +
                                "• На экране появится меню: «Выключить», «Перезагрузить», «Режим полёта».\n" +
                                "• Нажмите «Выключить».\n" +
                                "• Подтвердите действие (если спросит).\n" +
                                "• Телефон выключится через несколько секунд.\n\n" +
                                "📌 **Как перезагрузить телефон?**\n" +
                                "• Способ 1: удерживайте кнопку питания → выберите «Перезагрузить» (или «Restart»).\n" +
                                "• Способ 2: выключите телефон, а затем снова включите.\n" +
                                "• Зачем перезагружать: помогает, если телефон завис, тормозит или не работает какая-то функция.\n\n" +
                                "📌 **Принудительная перезагрузка (если телефон завис и не реагирует)**\n" +
                                "• Нажмите и удерживайте кнопку питания и кнопку уменьшения громкости одновременно 10-15 секунд.\n" +
                                "• Телефон вибрирует и перезагружается.\n" +
                                "• На некоторых телефонах: только кнопка питания (долгое удержание 10-20 секунд).\n" +
                                "• Это безопасно и не удаляет данные.\n\n" +
                                "📌 **Что делать, если телефон не включается?**\n" +
                                "• Подключите зарядное устройство (возможно, сел аккумулятор).\n" +
                                "• Подождите 10-15 минут, затем попробуйте включить.\n" +
                                "• Если не помогает – попробуйте другой кабель и адаптер.\n" +
                                "• Проверьте, не нажата ли кнопка питания «залипает» – аккуратно почистите.\n" +
                                "• Если ничего не помогает – обратитесь в сервисный центр.\n\n" +
                                "📌 **Что делать, если телефон не выключается?**\n" +
                                "• Удерживайте кнопку питания дольше (10-15 секунд) – телефон принудительно выключится.\n" +
                                "• Если не помогает – сделайте принудительную перезагрузку (см. выше).\n" +
                                "• В крайнем случае – дождитесь разряда батареи.\n\n" +
                                "🎯 **Задание:** пройдите симуляцию включения, выключения и перезагрузки телефона.",
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
                Text("Начать симуляцию", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 1: Удержание кнопки питания (включение)
    if (step == 1) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFEFE3D3)).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
            ) {
                Text(
                    text = "🔘 Нажмите и удерживайте кнопку питания 3 секунды, чтобы включить телефон.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Анимированная кнопка питания
            Button(
                onClick = { /* Удержание симулируется автоматически */ },
                modifier = Modifier
                    .size(120.dp)
                    .rotate(if (step == 1) 10f else 0f),
                shape = RoundedCornerShape(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
            ) {
                Icon(
                    imageVector = Icons.Default.PowerSettingsNew,
                    contentDescription = "Кнопка питания",
                    modifier = Modifier.size(60.dp),
                    tint = Color.White
                )
            }

            // Прогресс-бар удержания
            LinearProgressIndicator(
                progress = holdProgress,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                color = Color(0xFF8B5A2B)
            )
            Text("Удерживайте... ${(holdProgress * 3).toInt()} сек", fontSize = 12.sp)

            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF8B5A2B))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 2: Выбор действия (выключить или перезагрузить)
    if (step == 2) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFEFE3D3)).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
            ) {
                Text(
                    text = "📱 Телефон включён. Что вы хотите сделать?",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        restartAction = "shutdown"
                        score += 15
                        step = 3
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
                ) {
                    Icon(Icons.Default.PowerOff, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Выключить")
                }
                Button(
                    onClick = {
                        restartAction = "restart"
                        score += 15
                        step = 3
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B).copy(alpha = 0.8f))
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Перезагрузить")
                }
            }

            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF8B5A2B))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 3: Подтверждение действия
    if (step == 3) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFEFE3D3)).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (restartAction == "shutdown") "Вы уверены, что хотите выключить телефон?" else "Вы уверены, что хотите перезагрузить телефон?",
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Все несохранённые данные будут потеряны.", fontSize = 12.sp, color = Color.Red)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { step = 4 },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
                ) {
                    Text("Да")
                }
                Button(
                    onClick = { step = 2 },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Нет")
                }
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
                Text("🎉 Поздравляем! Вы освоили управление питанием телефона.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Вы заработали $score очков.", color = Color(0xFF8B5A2B), fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "💡 ИТОГОВЫЕ СОВЕТЫ:\n" +
                            "• Регулярно перезагружайте телефон (раз в 2-3 дня) – это помогает избежать зависаний.\n" +
                            "• Не выключайте телефон принудительно, если он работает нормально – это не вредно, но и не полезно.\n" +
                            "• Если экран замёрз, а телефон не реагирует – сделайте принудительную перезагрузку (кнопка питания + громкость вниз).\n" +
                            "• Держите кнопку питания в чистоте – от грязи она может залипать.\n" +
                            "• Если телефон выключается сам – возможно, проблема с батареей или кнопкой.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "game_power", true)
                            onComplete()
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
                ) { Text("Завершить", color = Color.White) }
            }
        }
    }
}