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


@Composable
fun LessonWifiDiagnosticScreen(navController: NavController, userId: Long) {
    var airplaneModeChecked by remember { mutableStateOf(false) }
    //var wifiEnabled by remember { mutableStateOf(false) }
    var routerRestarted by remember { mutableStateOf(false) }
    var dataSaverChecked by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
        val lessonKey = "game_wifidiag"
        val totalSteps = 6

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
        // ТЕОРИЯ – максимально подробная
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
                    Text("📡 Диагностика Wi-Fi: почему нет интернета и что делать", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Проблемы с Wi-Fi случаются у всех. Вот пошаговый план диагностики и устранения неполадок:\n\n" +
                                "🔍 **ШАГ 1: Проверьте режим «В самолёте»**\n" +
                                "• Режим «В самолёте» отключает все беспроводные модули (Wi-Fi, Bluetooth, мобильную сеть).\n" +
                                "• Где найти: опустите шторку уведомлений → нажмите на значок самолётика.\n" +
                                "• Если он включён (оранжевый/синий), выключите его. Если не помогло – перезагрузите телефон.\n\n" +
                                "🔍 **ШАГ 2: Перезагрузите роутер**\n" +
                                "• Выключите роутер из розетки на 30 секунд, затем включите обратно.\n" +
                                "• Подождите 2-3 минуты, пока роутер загрузится.\n" +
                                "• Почему помогает: роутер, как и телефон, может зависать. Перезагрузка очищает временную память.\n\n" +
                                "🔍 **ШАГ 3: Проверьте настройки телефона**\n" +
                                "• Wi-Fi включён? Настройки → Сеть и интернет → Wi-Fi → включить.\n" +
                                "• Телефон видит сеть? Если нет – попробуйте отключить и включить Wi-Fi.\n" +
                                "• Выберите свою сеть, введите пароль (если забыли – посмотрите на обратной стороне роутера).\n\n" +
                                "🔍 **ШАГ 4: Проверьте другие устройства**\n" +
                                "• Если интернет не работает и на других устройствах (ноутбук, телевизор) – проблема у провайдера.\n" +
                                "• Позвоните провайдеру (номер есть в договоре) и уточните, ведутся ли работы.\n\n" +
                                "🔍 **ШАГ 5: Ограничения на телефоне**\n" +
                                "• Режим экономии данных (Настройки → Сеть → Экономия данных) может блокировать Wi-Fi.\n" +
                                "• Режим энергосбережения иногда отключает фоновую передачу данных.\n" +
                                "• VPN может мешать соединению – отключите его временно.\n\n" +
                                "🔍 **ШАГ 6: Забыть сеть и подключиться заново**\n" +
                                "• Настройки → Wi-Fi → нажмите на свою сеть → «Забыть».\n" +
                                "• Затем выберите сеть снова и введите пароль.\n\n" +
                                "🔍 **ШАГ 7: Сброс сетевых настроек**\n" +
                                "• Настройки → Система → Сброс → Сброс настроек сети.\n" +
                                "• ВНИМАНИЕ: после этого телефон забудет все сохранённые Wi-Fi пароли и Bluetooth-устройства.\n\n" +
                                "🔍 **ШАГ 8: Крайние меры**\n" +
                                "• Обновите прошивку телефона.\n" +
                                "• Сделайте полный сброс телефона (после резервного копирования).\n" +
                                "• Обратитесь в сервисный центр (возможно, проблема с Wi-Fi модулем).\n\n" +
                                "🎯 **Задание:** пройдите симуляцию диагностики Wi-Fi, выполняя все шаги.",
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
                Text("Начать диагностику", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 1: Проверка режима полёта
    if (step == 1) {
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
                    Text("✈️ ШАГ 1: Проверьте режим «В самолёте»", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Опустите шторку уведомлений. Найдите значок самолётика. Если он горит (оранжевый/синий) – значит режим включён. Выключите его нажатием.", fontSize = 14.sp)
                    Icon(Icons.Default.AirplanemodeActive, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF2C5F6E))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Я проверил – режим «В самолёте» выключен", fontSize = 14.sp)
                Switch(checked = airplaneModeChecked, onCheckedChange = { airplaneModeChecked = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E)))
            }
            if (airplaneModeChecked) {
                Text("✅ Отлично! Режим полёта не мешает Wi-Fi.", color = Color.Green)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { score += 15; step = 2 },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                ) { Text("Далее", color = Color.White) }
            } else {
                Text("❌ Подтвердите, что вы проверили режим полёта", color = Color.Red)
            }
            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 2: Перезагрузка роутера
    if (step == 2) {
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
                    Text("🔄 ШАГ 2: Перезагрузите роутер", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Выключите роутер из розетки на 30 секунд, затем включите обратно. Подождите 2-3 минуты, пока загорятся все индикаторы.", fontSize = 14.sp)
                    Icon(Icons.Default.SettingsRemote, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF2C5F6E))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Я перезагрузил роутер", fontSize = 14.sp)
                Switch(checked = routerRestarted, onCheckedChange = { routerRestarted = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E)))
            }
            if (routerRestarted) {
                Text("✅ Хорошо! Роутер перезагружен. Теперь проверьте, появился ли Wi-Fi.", color = Color.Green)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { score += 20; step = 3 },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                ) { Text("Далее", color = Color.White) }
            } else {
                Text("❌ Перезагрузите роутер, чтобы продолжить", color = Color.Red)
            }
            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 3: Включение Wi-Fi и подключение
    if (step == 3) {
        var wifiToggled by remember { mutableStateOf(false) }
        var passwordEntered by remember { mutableStateOf(false) }

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
                    Text("📶 ШАГ 3: Проверьте настройки Wi-Fi на телефоне", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("1. Откройте Настройки → Сеть и интернет → Wi-Fi.\n2. Включите Wi-Fi (если выключен).\n3. Выберите свою сеть.\n4. Введите пароль (если требуется).", fontSize = 14.sp)
                    Icon(Icons.Default.Wifi, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF2C5F6E))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Я включил Wi-Fi и выбрал свою сеть", fontSize = 14.sp)
                Switch(checked = wifiToggled, onCheckedChange = { wifiToggled = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E)))
            }
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Я ввёл пароль (если потребовалось)", fontSize = 14.sp)
                Switch(checked = passwordEntered, onCheckedChange = { passwordEntered = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E)))
            }

            if (wifiToggled && passwordEntered) {
                Text("✅ Wi-Fi настроен! Теперь проверьте, работает ли интернет.", color = Color.Green)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { score += 25; step = 4 },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                ) { Text("Далее", color = Color.White) }
            } else {
                Text("❌ Выполните оба действия: включите Wi-Fi и введите пароль", color = Color.Red)
            }
            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 4: Проверка экономии данных
    if (step == 4) {
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
                    Text("📉 ШАГ 4: Проверьте режим экономии данных", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Режим экономии данных может блокировать передачу данных через Wi-Fi. Проверьте: Настройки → Сеть и интернет → Экономия данных → отключите, если включена.", fontSize = 14.sp)
                    Icon(Icons.Default.DataSaverOff, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF2C5F6E))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Я проверил – экономия данных отключена", fontSize = 14.sp)
                Switch(checked = dataSaverChecked, onCheckedChange = { dataSaverChecked = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E)))
            }

            if (dataSaverChecked) {
                Text("✅ Отлично! Экономия данных не мешает Wi-Fi.", color = Color.Green)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { score += 15; step = 5 },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                ) { Text("Далее", color = Color.White) }
            } else {
                Text("❌ Проверьте настройки экономии данных", color = Color.Red)
            }
            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 5: Финальный экран – интернет работает
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
                Text("🎉 Поздравляем! Вы восстановили интернет!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Вы заработали $score очков.", color = Color(0xFF2C5F6E), fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "💡 ИТОГОВЫЕ СОВЕТЫ ПО ДИАГНОСТИКЕ Wi-Fi:\n" +
                            "• Всегда начинайте с проверки режима «В самолёте».\n" +
                            "• Перезагрузка роутера решает 80% проблем.\n" +
                            "• Если не помогает – перезагрузите телефон.\n" +
                            "• Проверьте другие устройства – если у всех нет интернета, проблема у провайдера.\n" +
                            "• Запомните пароль от Wi-Fi или запишите его.\n" +
                            "• Обновите прошивку телефона, если проблемы повторяются.\n" +
                            "• Сброс сетевых настроек – крайняя мера, но часто помогает.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "game_wifidiag", true)
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                ) { Text("Завершить", color = Color.White) }
            }
        }
    }
}