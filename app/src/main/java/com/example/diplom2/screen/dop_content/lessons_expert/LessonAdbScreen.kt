package com.example.diplom2.screen.dop_content.lessons_expert

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.diplom2.screen.setActiveLessonExpert
import com.example.diplom2.screen.setActiveLessonMedium
import com.example.diplom2.screen.updateLessonProgressExpert
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.diplom2.screen.saveLastStepExpert
import com.example.diplom2.screen.getLastStepExpert
import com.example.diplom2.screen.activateLessonExpert
import com.example.diplom2.screen.getActiveLessonExpert
import com.example.diplom2.screen.updateLessonProgressExpert

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonAdbScreen(navController: NavController, userId: Long) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lessonKey = "lesson_adb"
    val totalSteps = 6

    // Инициализируем стартовый шаг из сохранённого, если урок активен
    var step by remember {
        val activeLesson = getActiveLessonExpert(context, userId)
        if (activeLesson == lessonKey) {
            mutableIntStateOf(getLastStepExpert(context, userId, lessonKey))
        } else {
            mutableIntStateOf(0)
        }
    }

    var score by remember { mutableIntStateOf(0) }
    var adbInstalled by remember { mutableStateOf(false) }
    var usbDebugging by remember { mutableStateOf(false) }
    var deviceConnected by remember { mutableStateOf(false) }
    var commandExecuted by remember { mutableStateOf(false) }

    // Активируем урок (без сброса прогресса, если он уже активен)
    LaunchedEffect(Unit) {
        activateLessonExpert(context, userId, lessonKey)
    }

    // Обновляем прогресс и сохраняем шаг при каждом изменении step
    LaunchedEffect(step) {
        val progress = if (step >= totalSteps - 1) 1f else step / (totalSteps - 1).toFloat()
        updateLessonProgressExpert(context, userId, progress)
        saveLastStepExpert(context, userId, lessonKey, step)
    }
    // ШАГ 0: ВСТУПЛЕНИЕ (скролл уже есть - LazyColumn)
    if (step == 0) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF09020A))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("🔧 ADB Shell – ПОЛНОЕ РУКОВОДСТВО ДЛЯ ЭКСПЕРТА", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("📌 **ЧТО ТАКОЕ ADB?**", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "ADB (Android Debug Bridge) – это мост между вашим компьютером и Android-устройством. " +
                                    "Он позволяет отправлять команды телефону прямо с компьютера, даже если экран разбит или телефон не загружается.\n\n" +
                                    "⚡ **ДЛЯ ЧЕГО ЭТО НУЖНО?**\n" +
                                    "• Спасти данные с разбитого телефона (экран не работает, а данные нужны)\n" +
                                    "• Установить приложения без Play Маркет\n" +
                                    "• Удалить встроенный «мусор» (приложения оператора, производителя)\n" +
                                    "• Посмотреть логи системы (почему вылетает приложение или быстро садится батарея)\n" +
                                    "• Сделать полный бэкап приложений с данными\n" +
                                    "• Управлять телефоном когда он в рекавери или даже в bootloop-е\n" +
                                    "• Разблокировать загрузчик и устанавливать кастомные прошивки\n" +
                                    "• Рутировать телефон\n\n" +
                                    "🎯 **КОГДА ЭТО МОЖЕТ ПРИГОДИТЬСЯ?**\n" +
                                    "• Вы уронили телефон, экран разбит, но нужно вытащить фото и контакты\n" +
                                    "• Вам надоели предустановленные приложения, которые нельзя удалить обычным способом\n" +
                                    "• Вы хотите сделать телефон максимально чистым и быстрым\n" +
                                    "• Вы разрабатываете приложения и нужно тестировать их на реальном устройстве\n" +
                                    "• Ваш телефон завис на логотипе (bootloop), а вы не хотите терять данные\n" +
                                    "• Вы хотите получить root-доступ и полный контроль над системой\n\n" +
                                    "⚠️ **ВАЖНЫЕ ПРЕДУПРЕЖДЕНИЯ:**\n" +
                                    "• ADB требует включённой отладки по USB. Если телефон не включается или экран разбит, " +
                                    "вы должны были включить её заранее! Иначе ADB не поможет.\n" +
                                    "• Некоторые команды могут повредить систему, если вы не знаете, что делаете.\n" +
                                    "• При отключении отладки по USB ADB перестаёт работать.\n" +
                                    "• Для работы ADB нужен оригинальный или качественный USB-кабель.\n\n" +
                                    "🎮 **КАК ЭТОТ УРОК ПОСТРОЕН?**\n" +
                                    "Вы пройдёте 4 шага:\n" +
                                    "1️⃣ Установка ADB на компьютер\n" +
                                    "2️⃣ Включение отладки по USB на телефоне\n" +
                                    "3️⃣ Подключение телефона к компьютеру\n" +
                                    "4️⃣ Выполнение базовых команд ADB\n\n" +
                                    "Каждый шаг требует вашего подтверждения – так вы не пропустите ничего важного. " +
                                    "В конце вы получите очки и сертификат (в виде прогресса) о прохождении урока.",
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("📋 **ЧТО ВАМ ПОНАДОБИТСЯ?**", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Text(
                            text = "• Компьютер (Windows, macOS или Linux)\n" +
                                    "• USB-кабель (желательно оригинальный)\n" +
                                    "• Телефон с Android (желательно версии 5.0+)\n" +
                                    "• 15-20 минут свободного времени\n" +
                                    "• Немного терпения, если вы делаете это впервые",
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
            item {
                Button(
                    onClick = { step = 1 },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37)),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("НАЧАТЬ ПРАКТИКУ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black) }
                Spacer(modifier = Modifier.height(32.dp)) // ДОБАВЛЕНО: отступ снизу
            }
        }
        return
    }

    // ШАГ 1: Установка ADB (исправлен - добавлен скролл)
    if (step == 1) {
        LazyColumn( // ЗАМЕНА: Column на LazyColumn для скролла
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF09020A))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("⚙️ ШАГ 1: УСТАНОВКА ADB НА КОМПЬЮТЕР", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "🔹 **ДЛЯ WINDOWS:**\n" +
                                    "1. Скачайте Platform Tools с официального сайта Google:\n" +
                                    "   https://developer.android.com/studio/releases/platform-tools\n" +
                                    "2. Распакуйте архив в папку, например, C:\\adb\n" +
                                    "3. Откройте папку, зажмите Shift и нажмите правую кнопку мыши → «Открыть окно PowerShell здесь»\n" +
                                    "4. Для удобства можно добавить путь к ADB в переменную PATH (чтобы вызывать из любой папки)\n\n" +
                                    "🔹 **ДЛЯ macOS:**\n" +
                                    "1. Скачайте Platform Tools\n" +
                                    "2. Распакуйте в папку /Users/ваше_имя/adb\n" +
                                    "3. Откройте Терминал и перейдите в папку: cd /Users/ваше_имя/adb\n" +
                                    "4. Для удобства можно добавить путь к ADB в переменную окружения Path (чтобы вызывать из любой папки)\n\n" +
                                    "🔹 **ДЛЯ Linux (Ubuntu):**\n" +
                                    "1. Откройте терминал и выполните:\n" +
                                    "   sudo apt update && sudo apt install adb\n" +
                                    "2. ADB установится автоматически в системный путь\n\n" +
                                    "✅ **ПРОВЕРКА УСТАНОВКИ:**\n" +
                                    "Откройте терминал/командную строку и введите:\n" +
                                    "   adb version\n" +
                                    "Если вы видите версию ADB – всё установлено правильно!",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "💡 Совет: если вы часто пользуетесь ADB, добавьте папку с adb.exe в переменную PATH. " +
                                    "Тогда команду можно будет вызывать из любого места, не переходя в папку.",
                            fontSize = 14.sp,
                            color = Color(0xFFD4AF37).copy(alpha = 0.7f)
                        )
                    }
                }
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Я установил ADB и проверил командой adb version", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = adbInstalled,
                        onCheckedChange = { adbInstalled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (adbInstalled) {
                item {
                    Text("✅ Отлично! Переходим к следующему шагу.", color = Color.Green, fontSize = 14.sp)
                }
                item {
                    Button(
                        onClick = { score += 20; step = 2 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                    ) {
                        Text("ДАЛЕЕ →", color = Color.Black)
                    }
                }
            } else {
                item {
                    Text("❌ Подтвердите, что вы установили ADB", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp)) // ДОБАВЛЕНО: отступ снизу
            }
        }
        return
    }

    // ШАГ 2: Включение отладки (исправлен - добавлен скролл)
    if (step == 2) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF09020A))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("🔓 ШАГ 2: ВКЛЮЧЕНИЕ ОТЛАДКИ ПО USB", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "⚠️ **ВАЖНО:** Этот шаг нужно сделать НА ТЕЛЕФОНЕ! Без него ADB не увидит устройство.\n\n" +
                                    "📱 **ИНСТРУКЦИЯ ДЛЯ ANDROID:**\n\n" +
                                    "1️⃣ **Включите режим разработчика:**\n" +
                                    "   • Зайдите в Настройки → О телефоне\n" +
                                    "   • Найдите строку «Номер сборки» (Build number)\n" +
                                    "   • Нажимайте на неё 7 раз подряд\n" +
                                    "   • Появится сообщение: «Вы стали разработчиком!»\n\n" +
                                    "2️⃣ **Включите отладку по USB:**\n" +
                                    "   • Вернитесь в главное меню Настроек\n" +
                                    "   • Зайдите в «Для разработчиков» (внизу списка)\n" +
                                    "   • Найдите пункт «Отладка по USB» и включите его\n" +
                                    "   • Подтвердите действие, если спросит\n\n" +
                                    "3️⃣ **Дополнительные настройки (рекомендуется):**\n" +
                                    "   • Включите «Отладка по USB (безопасные настройки)» – это позволит выполнять больше команд\n" +
                                    "   • Если будете использовать ADB при выключенном экране, включите «Не выключать экран»\n\n" +
                                    "⚠️ **ОСТОРОЖНО!**\n" +
                                    "• Не включайте режим разработчика, если не знаете, за что отвечают другие настройки.\n" +
                                    "• Отладка по USB потребляет чуть больше заряда батареи, но это незаметно.\n" +
                                    "• После завершения работы с ADB можно отключить отладку по USB для безопасности.",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Я включил отладку по USB на телефоне", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = usbDebugging,
                        onCheckedChange = { usbDebugging = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (usbDebugging) {
                item {
                    Text("✅ Отлично! Теперь телефон готов к подключению.", color = Color.Green, fontSize = 14.sp)
                }
                item {
                    Button(
                        onClick = { score += 20; step = 3 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                    ) {
                        Text("ДАЛЕЕ →", color = Color.Black)
                    }
                }
            } else {
                item {
                    Text("❌ Подтвердите, что вы включили отладку по USB", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 3: Подключение телефона (исправлен - добавлен скролл)
    if (step == 3) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF09020A))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("🔌 ШАГ 3: ПОДКЛЮЧЕНИЕ ТЕЛЕФОНА К КОМПЬЮТЕРУ", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "🔹 **ФИЗИЧЕСКОЕ ПОДКЛЮЧЕНИЕ:**\n" +
                                    "1. Подключите телефон к компьютеру качественным USB-кабелем\n" +
                                    "2. На телефоне появится запрос: «Разрешить отладку по USB?»\n" +
                                    "3. Нажмите «ОК» и поставьте галочку «Всегда разрешать для этого компьютера»\n\n" +
                                    "💡 Если запрос не появился, попробуйте:\n" +
                                    "   • Переподключить кабель\n" +
                                    "   • Использовать другой порт USB\n" +
                                    "   • Включить и выключить отладку заново\n\n" +
                                    "🔹 **ПРОВЕРКА ПОДКЛЮЧЕНИЯ:**\n" +
                                    "Откройте терминал/командную строку и выполните:\n" +
                                    "   adb devices\n" +
                                    "✅ Если вы видите список устройств с надписью 'device' – всё работает!\n" +
                                    "❌ Если видите 'unauthorized' – вы не нажали «ОК» на телефоне\n" +
                                    "❌ Если список пуст – проверьте драйверы или кабель",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "💡 СОВЕТ ПРОФИ: Если команда adb devices не работает, попробуйте:\n" +
                                    "   • Перезапустить ADB: adb kill-server && adb start-server\n" +
                                    "   • На телефоне: Настройки → Для разработчиков → Отладка по USB → выключить и включить\n" +
                                    "   • Установить драйверы для вашей модели телефона (особенно для Samsung, Xiaomi, Huawei)",
                            fontSize = 14.sp,
                            color = Color(0xFFD4AF37).copy(alpha = 0.7f)
                        )
                    }
                }
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Телефон подключён и виден в adb devices", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = deviceConnected,
                        onCheckedChange = { deviceConnected = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (deviceConnected) {
                item {
                    Text("✅ Супер! Теперь можно выполнять команды.", color = Color.Green, fontSize = 14.sp)
                }
                item {
                    Button(
                        onClick = { score += 20; step = 4 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                    ) {
                        Text("ДАЛЕЕ →", color = Color.Black)
                    }
                }
            } else {
                item {
                    Text("❌ Подтвердите, что телефон подключён", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 4: Базовые команды ADB (исправлен - добавлен скролл)
    if (step == 4) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF09020A))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("⌨️ ШАГ 4: БАЗОВЫЕ КОМАНДЫ ADB", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Теперь самое интересное – научимся управлять телефоном через компьютер!\n\n" +
                                    "📌 **ПОЛЕЗНЫЕ КОМАНДЫ (запомните их):**\n\n" +
                                    "1️⃣ **Просмотр установленных приложений:**\n" +
                                    "   adb shell pm list packages\n\n" +
                                    "2️⃣ **Установка приложения из APK-файла:**\n" +
                                    "   adb install путь_к_файлу.apk\n\n" +
                                    "3️⃣ **Удаление приложения:**\n" +
                                    "   adb uninstall com.example.app\n\n" +
                                    "4️⃣ **Копирование файла с телефона на компьютер:**\n" +
                                    "   adb pull /sdcard/DCIM/camera_photo.jpg\n\n" +
                                    "5️⃣ **Копирование файла с компьютера на телефон:**\n" +
                                    "   adb push файл.txt /sdcard/Download/\n\n" +
                                    "6️⃣ **Открыть shell (командную строку телефона):**\n" +
                                    "   adb shell\n\n" +
                                    "7️⃣ **Перезагрузка телефона:**\n" +
                                    "   adb reboot\n\n" +
                                    "8️⃣ **Перезагрузка в рекавери:**\n" +
                                    "   adb reboot recovery\n\n" +
                                    "9️⃣ **Перезагрузка в загрузчик (bootloader):**\n" +
                                    "   adb reboot bootloader\n\n" +
                                    "🔟 **Сделать скриншот:**\n" +
                                    "   adb shell screencap /sdcard/screenshot.png\n" +
                                    "   adb pull /sdcard/screenshot.png\n\n" +
                                    "🎯 **ЗАДАНИЕ:** выполните команду adb devices в своём терминале и нажмите кнопку ниже.",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Я выполнил adb devices и увидел своё устройство", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = commandExecuted,
                        onCheckedChange = { commandExecuted = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (commandExecuted) {
                item {
                    Text("✅ Поздравляю! Вы освоили основы ADB!", color = Color.Green, fontSize = 14.sp)
                }
                item {
                    Button(
                        onClick = { score += 40; step = 5 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                    ) {
                        Text("ЗАВЕРШИТЬ УРОК", color = Color.Black)
                    }
                }
            } else {
                item {
                    Text("❌ Выполните команду adb devices и подтвердите", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ФИНАЛЬНЫЙ ЭКРАН (уже был с Column - оставляем, но добавляем скролл на случай длинного контента)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF09020A))
            .verticalScroll(rememberScrollState()) // ДОБАВЛЕН скролл
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
        ) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🎉 ПОЗДРАВЛЯЮ! ВЫ ОСВОИЛИ ADB SHELL!", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Вы заработали $score очков.", fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "📚 **ЧТО ДАЛЬШЕ?**\n\n" +
                            "• Потренируйтесь с разными командами – это лучший способ запомнить\n" +
                            "• Переходите к следующим урокам: Root, Кастомные прошивки, Скрипты\n" +
                            "• Помните: ADB – это основа для всего продвинутого управления Android\n\n" +
                            "💡 **СОВЕТ НА БУДУЩЕЕ:**\n" +
                            "Сохраните шпаргалку с командами ADB в закладках или распечатайте. " +
                            "Они пригодятся не раз!",
                    fontSize = 15.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "lesson_adb", true)
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                ) { Text("ЗАВЕРШИТЬ", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black) }
            }
        }
        Spacer(modifier = Modifier.height(32.dp)) // ДОБАВЛЕНО: отступ снизу
    }
}