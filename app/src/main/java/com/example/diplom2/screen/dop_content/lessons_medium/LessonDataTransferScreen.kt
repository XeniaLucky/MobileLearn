package com.example.diplom2.screen.dop_content.lessons_medium

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
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
fun LessonDataTransferScreen(navController: NavController, userId: Long) {
    var selectedMethod by remember { mutableStateOf<String?>(null) }
    var googleSubStep by remember { mutableIntStateOf(0) }
    var usbSubStep by remember { mutableIntStateOf(0) }
    var appSubStep by remember { mutableIntStateOf(0) }
    var backupEnabled by remember { mutableStateOf(false) }
    var usbMode by remember { mutableStateOf(false) }
    var appInstalled by remember { mutableStateOf(false) }
    var permissionsGranted by remember { mutableStateOf(false) }
    var transferStarted by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
        val lessonKey = "game_datatransfer"
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
    // ТЕОРИЯ + ВЫБОР СПОСОБА
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
                    Text("🔄 Перенос данных на новый телефон", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "При покупке нового телефона нужно перенести контакты, фото, приложения, пароли. Есть 3 основных способа.\n\n" +
                                "1️⃣ **Google аккаунт (облако)** – самый удобный. Переносит контакты, приложения, пароли, настройки, SMS (на некоторых телефонах).\n" +
                                "2️⃣ **USB-кабель** – только фото, видео, музыка, документы.\n" +
                                "3️⃣ **Приложение производителя** (Samsung Smart Switch, Mi Mover, Clone Phone) – копирует почти всё, включая историю звонков и сообщения.\n\n" +
                                "✅ **Задание:** выберите способ и пройдите все шаги интерактивно.",
                        fontSize = 16.sp,
                        lineHeight = 22.sp
                    )
                }
            }
            Button(onClick = { step = 1 }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))) {
                Text("Выбрать способ", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ВЫБОР СПОСОБА ПЕРЕНОСА
    if (step == 1) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                Text("Как вы хотите перенести данные?", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { selectedMethod = "google"; score += 10; step = 2 }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))) {
                Text("Google аккаунт (облако)", color = Color.White)
            }
            Button(onClick = { selectedMethod = "usb"; score += 5; step = 3 }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))) {
                Text("USB-кабель", color = Color.White)
            }
            Button(onClick = { selectedMethod = "app"; score += 5; step = 4 }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))) {
                Text("Приложение производителя", color = Color.White)
            }
            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ==================== СПОСОБ 1: GOOGLE АККАУНТ ====================
    if (step == 2 && selectedMethod == "google") {
        when (googleSubStep) {
            0 -> {
                Column(
                    modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ШАГ 1: Включите резервное копирование", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("📱 На СТАРОМ телефоне:", fontSize = 14.sp)
                            Text("Настройки → Google → Резервное копирование → Включить", fontSize = 14.sp, color = Color(0xFF2C5F6E))
                            Spacer(modifier = Modifier.height(12.dp))
                            Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF2C5F6E))
                            Text("⬆️ Найдите значок «Настройки» на главном экране или в списке приложений", fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center)
                        }
                    }
                    Switch(checked = backupEnabled, onCheckedChange = { backupEnabled = it })
                    Text(if (backupEnabled) "✅ Включено" else "❌ Включите резервное копирование", color = if (backupEnabled) Color.Green else Color.Red)
                    Button(
                        onClick = { if (backupEnabled) { score += 15; googleSubStep = 1 } else { score = (score - 5).coerceAtLeast(0) } },
                        enabled = backupEnabled,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                    ) { Text("Далее", color = Color.White) }
                    Text("Очки: $score")
                }
            }
            1 -> {
                Column(
                    modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ШАГ 2: Настройте Google Фото", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("📸 Чтобы перенести фото и видео:", fontSize = 14.sp)
                            Text("Google Фото → Настройки → Резервное копирование → Включить", fontSize = 14.sp, color = Color(0xFF2C5F6E))
                            Spacer(modifier = Modifier.height(12.dp))
                            Icon(Icons.Default.Storage, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF2C5F6E))
                            Text("Все фото будут сохранены в облаке", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    Button(
                        onClick = { score += 10; googleSubStep = 2 },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                    ) { Text("Понятно", color = Color.White) }
                    Text("Очки: $score")
                }
            }
            2 -> {
                Column(
                    modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ШАГ 3: Восстановление на новом телефоне", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("🆕 При первом включении нового телефона:", fontSize = 14.sp)
                            Text("Выберите «Восстановить данные из резервной копии»", fontSize = 14.sp, color = Color(0xFF2C5F6E))
                            Text("Войдите в тот же Google аккаунт → выберите дату копии → Ждите", fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Icon(Icons.Default.PhoneAndroid, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF2C5F6E))
                            Text("Телефон сам скачает все приложения и настройки", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    Button(
                        onClick = { score += 20; googleSubStep = 3 },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                    ) { Text("Восстановить", color = Color.White) }
                    Text("Очки: $score")
                }
            }
            3 -> {
                Column(
                    modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("✅ Перенос через Google завершён!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text("Вы заработали $score очков.", color = Color(0xFF2C5F6E))
                            Button(onClick = { step = 5 }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))) {
                                Text("Завершить перенос", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
        return
    }

    // ==================== СПОСОБ 2: USB ====================
    if (step == 3 && selectedMethod == "usb") {
        when (usbSubStep) {
            0 -> {
                Column(
                    modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ШАГ 1: Подключите телефон к компьютеру", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("🔌 Используйте оригинальный USB-кабель", fontSize = 14.sp)
                            Text("Подключите телефон к компьютеру → На телефоне появится уведомление", fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("⚠️ Если компьютер не видит телефон, попробуйте другой кабель или порт USB", fontSize = 12.sp, color = Color.Red)
                        }
                    }
                    Row {
                        Button(onClick = { usbMode = true; score += 10; usbSubStep = 1 }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))) {
                            Text("Подключил", color = Color.White)
                        }
                        Button(onClick = { score = (score - 5).coerceAtLeast(0) }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                            Text("Не подключал", color = Color.White)
                        }
                    }
                    Text("Очки: $score")
                }
            }
            1 -> {
                Column(
                    modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ШАГ 2: Выберите режим передачи файлов", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("📱 На телефоне нажмите на уведомление «Зарядка через USB»", fontSize = 14.sp)
                            Text("Выберите «Передача файлов (MTP)» (НЕ «Только зарядка»!)", fontSize = 14.sp, color = Color(0xFF2C5F6E))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("После этого на компьютере откроется папка телефона", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    Button(
                        onClick = { score += 10; usbSubStep = 2 },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                    ) { Text("Выбрал MTP", color = Color.White) }
                    Text("Очки: $score")
                }
            }
            2 -> {
                Column(
                    modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ШАГ 3: Скопируйте нужные папки", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("📁 На компьютере откройте папку телефона", fontSize = 14.sp)
                            Text("Скопируйте на компьютер:", fontSize = 14.sp)
                            Text("• DCIM – фото и видео", fontSize = 14.sp)
                            Text("• Music – музыка", fontSize = 14.sp)
                            Text("• Documents – документы", fontSize = 14.sp)
                            Text("• Download – загрузки", fontSize = 14.sp)
                        }
                    }
                    Button(
                        onClick = { score += 15; usbSubStep = 3 },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                    ) { Text("Скопировал", color = Color.White) }
                    Text("Очки: $score")
                }
            }
            3 -> {
                Column(
                    modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ШАГ 4: Перенесите файлы на новый телефон", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("🔄 Подключите НОВЫЙ телефон к компьютеру", fontSize = 14.sp)
                            Text("Выберите режим MTP (как в шаге 2)", fontSize = 14.sp)
                            Text("Скопируйте папки с компьютера обратно в телефон", fontSize = 14.sp)
                        }
                    }
                    Button(
                        onClick = { score += 10; usbSubStep = 4 },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                    ) { Text("Перенёс", color = Color.White) }
                    Text("Очки: $score")
                }
            }
            4 -> {
                Column(
                    modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("✅ Перенос через USB завершён!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text("Вы заработали $score очков.", color = Color(0xFF2C5F6E))
                            Button(onClick = { step = 5 }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))) {
                                Text("Завершить перенос", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
        return
    }

    // ==================== СПОСОБ 3: ПРИЛОЖЕНИЕ ПРОИЗВОДИТЕЛЯ ====================
    if (step == 4 && selectedMethod == "app") {
        when (appSubStep) {
            0 -> {
                Column(
                    modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ШАГ 1: Установите приложение", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("📱 Какое приложение нужно?", fontSize = 14.sp)
                            Text("• Samsung → Smart Switch", fontSize = 14.sp)
                            Text("• Xiaomi → Mi Mover", fontSize = 14.sp)
                            Text("• Huawei → Clone Phone", fontSize = 14.sp)
                            Text("• Для других телефонов → Google Files", fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Ищите в Google Play Маркет. Приложение должно быть на ОБОИХ телефонах!", fontSize = 12.sp, color = Color.Red)
                        }
                    }
                    Switch(checked = appInstalled, onCheckedChange = { appInstalled = it })
                    Text(if (appInstalled) "✅ Приложение установлено" else "❌ Установите приложение", color = if (appInstalled) Color.Green else Color.Red)
                    Button(
                        onClick = { if (appInstalled) { score += 15; appSubStep = 1 } else { score = (score - 5).coerceAtLeast(0) } },
                        enabled = appInstalled,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                    ) { Text("Далее", color = Color.White) }
                    Text("Очки: $score")
                }
            }
            1 -> {
                Column(
                    modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ШАГ 2: Дайте разрешения", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("🔓 При первом запуске приложение попросит доступ к:", fontSize = 14.sp)
                            Text("• Контактам", fontSize = 14.sp)
                            Text("• Фото и файлам", fontSize = 14.sp)
                            Text("• SMS и звонкам", fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Нажмите «РАЗРЕШИТЬ» везде, иначе перенос не сработает!", fontSize = 12.sp, color = Color.Red)
                        }
                    }
                    Switch(checked = permissionsGranted, onCheckedChange = { permissionsGranted = it })
                    Text(if (permissionsGranted) "✅ Разрешения даны" else "❌ Дайте все разрешения", color = if (permissionsGranted) Color.Green else Color.Red)
                    Button(
                        onClick = { if (permissionsGranted) { score += 10; appSubStep = 2 } else { score = (score - 5).coerceAtLeast(0) } },
                        enabled = permissionsGranted,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                    ) { Text("Далее", color = Color.White) }
                    Text("Очки: $score")
                }
            }
            2 -> {
                Column(
                    modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ШАГ 3: Запустите передачу", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("📲 На СТАРОМ телефоне выберите «ОТПРАВИТЬ» (Send)", fontSize = 14.sp)
                            Text("📱 На НОВОМ телефоне выберите «ПОЛУЧИТЬ» (Receive)", fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Приложение само найдёт другое устройство через Wi-Fi Direct", fontSize = 12.sp, color = Color.Gray)
                            Text("Убедитесь, что Wi-Fi включен на обоих телефонах!", fontSize = 12.sp, color = Color.Red)
                        }
                    }
                    Button(
                        onClick = { score += 10; appSubStep = 3 },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                    ) { Text("Выбрал режим", color = Color.White) }
                    Text("Очки: $score")
                }
            }
            3 -> {
                Column(
                    modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ШАГ 4: Выберите данные для переноса", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("✅ Отметьте что переносить:", fontSize = 14.sp)
                            Text("• Контакты", fontSize = 14.sp)
                            Text("• Сообщения", fontSize = 14.sp)
                            Text("• Фото и видео", fontSize = 14.sp)
                            Text("• Приложения", fontSize = 14.sp)
                            Text("• Настройки", fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Нажмите «ДАЛЕЕ» или «ПЕРЕНЕСТИ»", fontSize = 12.sp, color = Color(0xFF2C5F6E))
                        }
                    }
                    Switch(checked = transferStarted, onCheckedChange = { transferStarted = it })
                    Text(if (transferStarted) "✅ Перенос запущен" else "❌ Запустите перенос", color = if (transferStarted) Color.Green else Color.Red)
                    Button(
                        onClick = { if (transferStarted) { score += 20; appSubStep = 4 } else { score = (score - 5).coerceAtLeast(0) } },
                        enabled = transferStarted,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                    ) { Text("Готово", color = Color.White) }
                    Text("Очки: $score")
                }
            }
            4 -> {
                Column(
                    modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("✅ Перенос через приложение завершён!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text("Вы заработали $score очков.", color = Color(0xFF2C5F6E))
                            Button(onClick = { step = 5 }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))) {
                                Text("Завершить перенос", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
        return
    }

    // ФИНАЛ (сброс старого телефона)
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🎉 Поздравляем! Данные перенесены.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("❗ ОБЯЗАТЕЛЬНО сделайте сброс старого телефона!", fontSize = 14.sp, color = Color.Red, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Настройки → Сброс → Удаление всех данных (Factory reset)", fontSize = 14.sp, textAlign = TextAlign.Center)
                Text("Перед сбросом убедитесь, что всё нужное перенесено!", fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    scope.launch {
                        saveLessonProgress(context, userId, "game_datatransfer", true)
                        navController.popBackStack()
                    }
                }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))) {
                    Text("Завершить", color = Color.White)
                }
            }
        }
    }
}