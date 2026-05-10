package com.example.diplom2.screen.dop_content.lessons_expert

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.diplom2.screen.updateLessonProgressExpert
import kotlinx.coroutines.launch
import kotlinx.coroutines.launch
import com.example.diplom2.screen.saveLastStepExpert
import com.example.diplom2.screen.getLastStepExpert
import com.example.diplom2.screen.activateLessonExpert
import com.example.diplom2.screen.getActiveLessonExpert
import com.example.diplom2.screen.updateLessonProgressExpert


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonLoggingScreen(navController: NavController, userId: Long) {
    var adbLoggingChecked by remember { mutableStateOf(false) }
    var logcatCommand by remember { mutableStateOf(false) }
    var filterLearned by remember { mutableStateOf(false) }
    var logReading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
        val lessonKey = "lesson_logging"
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
    // ШАГ 0: ВСТУПЛЕНИЕ (уже LazyColumn, добавляем отступ)
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
                        Text("📋 ОТЛАДКА И ЛОГИ – ПОЛНОЕ РУКОВОДСТВО ДЛЯ ЭКСПЕРТА", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("📌 **ЧТО ТАКОЕ ЛОГИ И ЗАЧЕМ ОНИ НУЖНЫ?**", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Логи – это записи всех событий, происходящих в системе Android. " +
                                    "Они помогают понять, почему вылетает приложение, что потребляет батарею, и где ошибка.\n\n" +
                                    "⚡ **ЧТО МОЖНО УЗНАТЬ ИЗ ЛОГОВ?**\n" +
                                    "• Почему приложение вылетает (Force Close)\n" +
                                    "• Почему телефон тормозит или греется\n" +
                                    "• Какое приложение разряжает батарею в фоне\n" +
                                    "• Что происходит при включении/выключении телефона\n" +
                                    "• Ошибки драйверов и ядра\n" +
                                    "• Проблемы с сетью, Wi-Fi, Bluetooth\n" +
                                    "• Действия приложений с root-доступом\n\n" +
                                    "🎯 **ЭТОТ УРОК НАУЧИТ:**\n" +
                                    "1️⃣ Включать отладку по USB и смотреть логи через ADB\n" +
                                    "2️⃣ Использовать logcat для чтения логов в реальном времени\n" +
                                    "3️⃣ Фильтровать логи по тегам, приоритетам, приложениям\n" +
                                    "4️⃣ Сохранять логи в файл и делиться ими\n" +
                                    "5️⃣ Понимать, что означают ошибки и как их исправлять\n\n" +
                                    "📋 **ЧТО ВАМ ПОНАДОБИТСЯ?**\n" +
                                    "• Компьютер с ADB\n" +
                                    "• Включённая отладка по USB на телефоне\n" +
                                    "• Приложение Logcat Reader (для чтения логов на телефоне)\n" +
                                    "• Терпение и желание разобраться в потоке сообщений\n\n" +
                                    "⚠️ **ВАЖНО:**\n" +
                                    "• Логи могут содержать личную информацию (адреса, логины).\n" +
                                    "• Перед отправкой логов разработчику проверьте, не попали ли туда пароли.\n" +
                                    "• Нефильтрованный logcat выводит сотни сообщений в секунду.\n" +
                                    "• Для поиска ошибок используйте фильтрацию.",
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
                ) { Text("НАЧАТЬ РАБОТУ С ЛОГАМИ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black) }
                Spacer(modifier = Modifier.height(32.dp)) // ДОБАВЛЕНО: отступ снизу
            }
        }
        return
    }

    // ШАГ 1: Настройка ADB и проверка доступа к логам (ИСПРАВЛЕН - добавлен скролл)
    if (step == 1) {
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
                        Text("🔧 ШАГ 1: НАСТРОЙКА ADB И ПРОВЕРКА ДОСТУПА К ЛОГАМ", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "ADB не только управляет телефоном, но и позволяет читать логи в реальном времени.\n\n" +
                                    "🔹 **ГДЕ ВЗЯТЬ ADB?**\n" +
                                    "• Если прошли урок ADB – он уже установлен\n" +
                                    "• Если нет, скачайте Platform Tools с developer.android.com\n" +
                                    "• Для Linux: sudo apt install adb\n" +
                                    "• Для macOS: brew install android-platform-tools\n\n" +
                                    "🔹 **НАСТРОЙКА TELEФОНА:**\n\n" +
                                    "1️⃣ Включите режим разработчика (7 нажатий по номеру сборки)\n" +
                                    "2️⃣ Включите отладку по USB\n" +
                                    "3️⃣ Подключите телефон к компьютеру\n" +
                                    "4️⃣ Подтвердите разрешение отладки на телефоне\n" +
                                    "5️⃣ Проверьте подключение: adb devices\n\n" +
                                    "🔹 **ГДЕ ХРАНЯТСЯ ЛОГИ?**\n\n" +
                                    "• Основной лог Android – logcat (кольцевой буфер)\n" +
                                    "• Системные логи – /dev/log/ (требуют root)\n" +
                                    "• Логи приложений – /data/data/package.name/logs/ (требуют root)\n" +
                                    "• Ядро – /proc/kmsg (требует root)\n" +
                                    "• Батарея – /sys/class/power_supply/battery/ (требует root)\n\n" +
                                    "🔹 **СМОТРИМ ЛОГИ В РЕАЛЬНОМ ВРЕМЕНИ:**\n\n" +
                                    "• **Базовая команда:**\n" +
                                    "   adb logcat\n\n" +
                                    "• **Очистить буфер логов:**\n" +
                                    "   adb logcat -c\n\n" +
                                    "• **Сохранить логи в файл:**\n" +
                                    "   adb logcat -d > log.txt\n\n" +
                                    "• **Логи только с ошибками:**\n" +
                                    "   adb logcat *:E\n\n" +
                                    "• **Завершить просмотр логов:** Ctrl + C\n\n" +
                                    "🔹 **ПРИЛОЖЕНИЯ ДЛЯ ЧТЕНИЯ ЛОГОВ НА ТЕЛЕФОНЕ:**\n\n" +
                                    "• **Logcat Reader** – простое приложение с фильтрацией\n" +
                                    "• **MatLog** – Material Design, поиск, фильтр\n" +
                                    "• **Logs Explorer** – для рутованных, полный доступ к логам\n" +
                                    "• **CatLog** – старый, но надёжный выбор\n\n" +
                                    "💡 **СОВЕТ:** Начните с ADB – так вы увидите логи всех приложений. " +
                                    "Приложения на телефоне показывают только свои логи.",
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
                    Text("Я настроил ADB и проверил доступ к логам", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = adbLoggingChecked,
                        onCheckedChange = { adbLoggingChecked = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (adbLoggingChecked) {
                item {
                    Text("✅ Отлично! Теперь можно смотреть логи.", color = Color.Green, fontSize = 14.sp)
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
                    Text("❌ Подтвердите настройку ADB", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 2: Фильтрация логов logcat (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("📝 ШАГ 2: ФИЛЬТРАЦИЯ ЛОГОВ LOGCAT", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "logcat выводит сотни сообщений в секунду. Нужно уметь находить нужные.\n\n" +
                                    "🔹 **УРОВНИ ПРИОРИТЕТА (степень важности):**\n\n" +
                                    "• **V** (Verbose) – самый подробный (всё подряд)\n" +
                                    "• **D** (Debug) – отладочные сообщения\n" +
                                    "• **I** (Info) – информационные сообщения\n" +
                                    "• **W** (Warning) – предупреждения\n" +
                                    "• **E** (Error) – ошибки (самое важное)\n" +
                                    "• **F** (Fatal) – фатальные ошибки (обычно ведущие к крашу)\n" +
                                    "• **S** (Silent) – ничего не выводить\n\n" +
                                    "🔹 **ФИЛЬТРАЦИЯ ПО ПРИОРИТЕТУ:**\n\n" +
                                    "• Только ошибки:\n" +
                                    "   adb logcat *:E\n\n" +
                                    "• Предупреждения и выше:\n" +
                                    "   adb logcat *:W\n\n" +
                                    "• Всё, кроме Verbose:\n" +
                                    "   adb logcat *:D\n\n" +
                                    "🔹 **ФИЛЬТРАЦИЯ ПО ПРИЛОЖЕНИЮ (PID):**\n\n" +
                                    "1️⃣ Узнайте PID приложения:\n" +
                                    "   adb shell ps | grep com.example.app\n" +
                                    "2️⃣ Смотрите логи только этого PID:\n" +
                                    "   adb logcat --pid=12345\n\n" +
                                    "🔹 **ФИЛЬТРАЦИЯ ПО ТЕГУ:**\n\n" +
                                    "• Приложения пишут логи с определёнными тегами.\n" +
                                    "• Пример: тег «ActivityManager» – управление активностями.\n" +
                                    "• Фильтр по тегу:\n" +
                                    "   adb logcat -s ActivityManager\n" +
                                    "• Несколько тегов:\n" +
                                    "   adb logcat -s ActivityManager System\n" +
                                    "• Тег с уровнем важности:\n" +
                                    "   adb logcat ActivityManager:E\n" +
                                    "   (только ошибки ActivityManager)\n\n" +
                                    "🔹 **РЕГУЛЯРНЫЕ ВЫРАЖЕНИЯ (продвинутая фильтрация):**\n\n" +
                                    "• Только строки, содержащие «Error» или «FATAL»:\n" +
                                    "   adb logcat | grep -E \"Error|FATAL\"\n" +
                                    "• Исключить строки с «DEBUG»:\n" +
                                    "   adb logcat | grep -v DEBUG\n\n" +
                                    "🔹 **СОХРАНЕНИЕ ЛОГОВ:**\n\n" +
                                    "• Весь лог с момента включения:\n" +
                                    "   adb logcat -d > log_all.txt\n" +
                                    "• Только ошибки за последние 5 минут:\n" +
                                    "   adb logcat -d -T \"5 minutes ago\" *:E > errors.txt\n" +
                                    "• Прочитать логи за определённое время:\n" +
                                    "   adb logcat -v time\n\n" +
                                    "🔹 **ЧТО ОЗНАЧАЮТ ОШИБКИ?**\n\n" +
                                    "• **NullPointerException** – приложение пытается использовать объект, которого нет\n" +
                                    "• **OutOfMemoryError** – приложению не хватает памяти\n" +
                                    "• **SecurityException** – приложение пытается сделать что-то без разрешения\n" +
                                    "• **IllegalArgumentException** – передан неправильный аргумент\n" +
                                    "• **RuntimeException** – общая ошибка выполнения, нужно смотреть стек вызовов\n\n" +
                                    "💡 **СОВЕТ:** При поиске ошибок всегда смотрите стек вызовов " +
                                    "(строки, начинающиеся с «at com.package...»). Там указано, в каком файле и строке произошла ошибка.",
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
                    Text("Я изучил команды logcat и фильтрацию", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = logcatCommand,
                        onCheckedChange = { logcatCommand = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (logcatCommand) {
                item {
                    Text("✅ Logcat освоен!", color = Color.Green, fontSize = 14.sp)
                }
                item {
                    Button(
                        onClick = { score += 30; step = 3 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                    ) {
                        Text("ДАЛЕЕ →", color = Color.Black)
                    }
                }
            } else {
                item {
                    Text("❌ Подтвердите изучение фильтрации", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 3: Анализ логов и поиск ошибок (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("📊 ШАГ 3: АНАЛИЗ ЛОГОВ И ПОИСК ОШИБОК", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Умение читать логи отличает новичка от эксперта. Разберём реальные примеры.\n\n" +
                                    "🔹 **ПРИМЕР 1: ПРИЛОЖЕНИЕ ВЫЛЕТАЕТ**\n\n" +
                                    "Вот типичная ошибка в логах:\n" +
                                    "--------- beginning of crash\n" +
                                    "E/AndroidRuntime(12345): FATAL EXCEPTION: main\n" +
                                    "E/AndroidRuntime(12345): Process: com.example.app, PID: 12345\n" +
                                    "E/AndroidRuntime(12345): java.lang.NullPointerException\n" +
                                    "E/AndroidRuntime(12345): at com.example.app.MainActivity.onCreate(MainActivity.java:42)\n" +
                                    "E/AndroidRuntime(12345): ...\n\n" +
                                    "🔍 **Что здесь важно:**\n" +
                                    "• **FATAL EXCEPTION** – приложение крашнулось\n" +
                                    "• **Process: com.example.app** – какое приложение\n" +
                                    "• **NullPointerException** – тип ошибки\n" +
                                    "• **MainActivity.java:42** – файл и строка, где ошибка\n" +
                                    "• Стек вызовов – последовательность вызовов, приведшая к ошибке\n\n" +
                                    "✅ **Как исправить:**\n" +
                                    "• Проверить, что объект не равен null перед использованием\n" +
                                    "• Добавить проверку if (object != null)\n" +
                                    "• Инициализировать объект до использования\n\n" +
                                    "🔹 **ПРИМЕР 2: ПРИЛОЖЕНИЕ ТОРМОЗИТ**\n\n" +
                                    "I/Choreographer(12345): Skipped 120 frames! The application may be doing too much work on its main thread.\n\n" +
                                    "🔍 **Что означает:**\n" +
                                    "• Приложение пропустило 120 кадров (2 секунды при 60 FPS)\n" +
                                    "• Главный поток занят тяжёлыми вычислениями\n" +
                                    "• Пользователь видит лаги и зависания\n\n" +
                                    "✅ **Как исправить:**\n" +
                                    "• Вынести тяжёлые операции в фоновый поток\n" +
                                    "• Использовать AsyncTask, Coroutines, RxJava\n" +
                                    "• Оптимизировать работу с базой данных или сетью\n\n" +
                                    "🔹 **ПРИМЕР 3: БАТАРЕЯ БЫСТРО САДИТСЯ**\n\n" +
                                    "E/WakeLock(12345): held for 5m30s: com.example.app\n\n" +
                                    "🔍 **Что означает:**\n" +
                                    "• Приложение удерживает Wakelock (телефон не спит)\n" +
                                    "• 5 минут 30 секунд – очень много для обычного приложения\n" +
                                    "• Это может быть из-за ошибки в коде\n\n" +
                                    "✅ **Как исправить:**\n" +
                                    "• Проверить, что release() вызывается для каждого acquire()\n" +
                                    "• Использовать Wakelock только когда действительно нужно\n" +
                                    "• Пересмотреть фоновую логику приложения\n\n" +
                                    "🔹 **ПРИМЕР 4: ПРОБЛЕМЫ С СЕТЬЮ**\n\n" +
                                    "W/System.err(12345): java.net.SocketTimeoutException: timeout\n" +
                                    "W/System.err(12345): at com.example.app.Network.downloadData(Network.java:56)\n\n" +
                                    "🔍 **Что означает:**\n" +
                                    "• Таймаут соединения – сервер не ответил вовремя\n" +
                                    "• Плохой интернет, либо сервер перегружен\n" +
                                    "• Неправильно настроен таймаут в приложении\n\n" +
                                    "✅ **Как исправить:**\n" +
                                    "• Увеличить таймаут в приложении\n" +
                                    "• Добавить повторные попытки (retry)\n" +
                                    "• Проверить, что сервер работает\n\n" +
                                    "🔹 **ПРИМЕР 5: НЕ ХВАТАЕТ ПАМЯТИ**\n\n" +
                                    "E/AndroidRuntime(12345): java.lang.OutOfMemoryError\n" +
                                    "E/AndroidRuntime(12345): Failed to allocate a 16777216 byte allocation\n\n" +
                                    "🔍 **Что означает:**\n" +
                                    "• Приложению нужно 16 МБ памяти, но нет свободной\n" +
                                    "• Утечка памяти или слишком большие объекты\n\n" +
                                    "✅ **Как исправить:**\n" +
                                    "• Освобождать ресурсы когда они не нужны\n" +
                                    "• Уменьшить размер загружаемых изображений\n" +
                                    "• Использовать более эффективные структуры данных\n\n" +
                                    "🔹 **ИНСТРУМЕНТЫ ДЛЯ АНАЛИЗА:**\n\n" +
                                    "• **Logcat Reader** – поиск по логам на телефоне\n" +
                                    "• **PID Cat** – логи конкретного процесса\n" +
                                    "• **MatLog** – Material дизайн, удобные фильтры\n" +
                                    "• **Notepin** – с сохранением логов в облако\n\n" +
                                    "💡 **СОВЕТ:** При поиске ошибки сначала смотрите самые свежие логи, " +
                                    "ищите слова «FATAL», «Exception», «Error». " +
                                    "Стек вызовов даст вам понять, где именно произошла ошибка.",
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
                    Text("Я научился анализировать логи и находить ошибки", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = filterLearned,
                        onCheckedChange = { filterLearned = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (filterLearned) {
                item {
                    Text("✅ Анализ логов освоен!", color = Color.Green, fontSize = 14.sp)
                }
                item {
                    Button(
                        onClick = { score += 30; step = 4 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                    ) {
                        Text("ДАЛЕЕ →", color = Color.Black)
                    }
                }
            } else {
                item {
                    Text("❌ Подтвердите, что вы изучили анализ логов", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 4: Расширенный анализ (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("🔬 ШАГ 4: РАСШИРЕННЫЙ АНАЛИЗ – LOGPIPE, DMESG, KMSG", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Для глубокой диагностики нужны не только логи приложений, но и системные логи.\n\n" +
                                    "🔹 **DMESG (ЛОГ ЯДРА)** – для опытных с root\n\n" +
                                    "• Команда:\n" +
                                    "   adb shell dmesg\n" +
                                    "   adb shell dmesg | grep -i error\n" +
                                    "   adb shell dmesg | grep -i fail\n" +
                                    "   adb shell dmesg > kernel.log\n\n" +
                                    "🔍 **Что можно найти:**\n" +
                                    "• Ошибки драйверов (камера, Wi-Fi, Bluetooth)\n" +
                                    "• Проблемы с питанием и батареей\n" +
                                    "• Падения ядра (kernel panic)\n" +
                                    "• Ошибки при загрузке (boot)\n\n" +
                                    "🔹 **LOGCAT БУФЕРЫ (разные источники)**\n\n" +
                                    "• adb logcat -b main – основной буфер приложений\n" +
                                    "• adb logcat -b system – системные сообщения\n" +
                                    "• adb logcat -b events – события (ActivityManager, PowerManager)\n" +
                                    "• adb logcat -b crash – только ошибки крашей\n" +
                                    "• adb logcat -b all – всё вместе\n\n" +
                                    "🔹 **АНАЛИЗ БАТАРЕИ**\n\n" +
                                    "• Статистика батареи не через лог, но можно посмотреть:\n" +
                                    "   adb shell dumpsys batterystats\n" +
                                    "   adb shell dumpsys batterystats > battery.txt\n" +
                                    "• Сбросить статистику: adb shell dumpsys batterystats --reset\n\n" +
                                    "🔹 **АНАЛИЗ ПАМЯТИ**\n\n" +
                                    "• Информация о памяти:\n" +
                                    "   adb shell dumpsys meminfo\n" +
                                    "   adb shell dumpsys meminfo com.example.app (конкретное приложение)\n" +
                                    "   adb shell procrank (требует root)\n\n" +
                                    "🔹 **АНАЛИЗ ПРОЦЕССОРА**\n\n" +
                                    "• Загрузка CPU:\n" +
                                    "   adb shell top\n" +
                                    "   adb shell top -n 1 -d 1\n" +
                                    "   adb shell cat /proc/stat\n\n" +
                                    "🔹 **АНАЛИЗ СЕТИ**\n\n" +
                                    "• Сетевые соединения:\n" +
                                    "   adb shell netstat\n" +
                                    "   adb shell netstat -tulpn (требует root)\n" +
                                    "   adb shell ifconfig\n" +
                                    "   adb shell ip route\n\n" +
                                    "🔹 **АНАЛИЗ DISPLAY**\n\n" +
                                    "• Информация об экране:\n" +
                                    "   adb shell dumpsys display\n" +
                                    "   adb shell dumpsys window displays\n" +
                                    "   adb shell wm size\n" +
                                    "   adb shell wm density\n\n" +
                                    "🔹 **ГОТОВЫЕ СКРИПТЫ ДЛЯ АНАЛИЗА**\n\n" +
                                    "• Сохраните этот скрипт как collect_logs.sh:\n" +
                                    "   #!/bin/bash\n" +
                                    "   echo \"Собираем логи...\"\n" +
                                    "   adb logcat -d > logcat.txt\n" +
                                    "   adb shell dmesg > dmesg.txt\n" +
                                    "   adb shell dumpsys batterystats > battery.txt\n" +
                                    "   adb shell dumpsys meminfo > meminfo.txt\n" +
                                    "   adb shell top -n 1 > top.txt\n" +
                                    "   adb shell netstat > netstat.txt\n" +
                                    "   echo \"Готово!\"\n\n" +
                                    "💡 **СОВЕТ:** При обращении в поддержку разработчика приложения " +
                                    "всегда прикладывайте logcat с ошибкой (только нужные строки). " +
                                    "Это повысит шанс на быстрое решение проблемы.",
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
                    Text("Я изучил расширенные источники логов", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = logReading,
                        onCheckedChange = { logReading = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (logReading) {
                item {
                    Text("✅ Работа с логами освоена на уровне эксперта!", color = Color.Green, fontSize = 14.sp)
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
                    Text("❌ Подтвердите, что вы изучили расширенные логи", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ФИНАЛЬНЫЙ ЭКРАН (ИСПРАВЛЕН - добавлен скролл и отступ)
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
                Text("🎉 ВЫ СТАЛИ ЭКСПЕРТОМ ПО ОТЛАДКЕ И ЛОГАМ!", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Вы заработали $score очков.", fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "📚 **ЧТО ДАЛЬШЕ?**\n\n" +
                            "• Практикуйтесь – чем больше логов вы увидите, тем быстрее научитесь их анализировать\n" +
                            "• Изучите устройство Android: /proc, /sys, /dev\n" +
                            "• Настройте автоматический сбор логов при падении приложений\n" +
                            "• Следите за обновлениями Android – логи меняются\n" +
                            "• Переходите к следующим урокам: Файловая система, Сеть и серверы, Эмуляция\n\n" +
                            "🔧 **ПОЛЕЗНЫЕ ПРИЛОЖЕНИЯ ДЛЯ ЛОГОВ:**\n" +
                            "• Logcat Reader – простой просмотр логов\n" +
                            "• MatLog – Material Design, фильтрация\n" +
                            "• Logs Explorer – для рутованных\n" +
                            "• Process Monitor – логи конкретного приложения\n" +
                            "• Battery Historian – анализ батареи от Google\n" +
                            "• Systrace – анализ производительности\n\n" +
                            "💡 **ПОСЛЕДНИЙ СОВЕТ:**\n" +
                            "Логи – ваш главный инструмент при поиске проблем. " +
                            "Научитесь читать их быстро, и вы сможете диагностировать " +
                            "почти любую неисправность без посторонней помощи.",
                    fontSize = 15.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "lesson_logging", true)
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