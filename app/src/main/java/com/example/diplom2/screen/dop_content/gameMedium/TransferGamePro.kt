package com.example.diplom2.screen.dop_content.gameMedium

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import com.example.diplom2.screen.AchievementManager
import androidx.compose.foundation.ExperimentalFoundationApi

data class TransferTask(
    val id: Int,
    val situation: String,
    val constraints: String,
    val fileType: String,
    val correctMethod: String,
    val hint: String,
    val whyCorrect: String
)

enum class SpeedLevel(val displayName: String, val color: Color) {
    LOW("Низкая", Color(0xFFFF9800)),
    MEDIUM("Средняя", Color(0xFFFFD700)),
    HIGH("Высокая", Color(0xFF4CAF50))
}

data class TransferMethod(
    val name: String,
    val speedLevel: SpeedLevel,
    val needInternet: Boolean,
    val icon: ImageVector,
    val color: Color,
    val description: String,
    val bestFor: String,
    val pros: String,
    val cons: String,
    val compatibleDevices: String
)

data class Receiver(
    val name: String,
    val icon: ImageVector,
    val methods: List<String>
)

data class TutorialStep(
    val targetKey: String,
    val title: String,
    val text: String,
    val icon: ImageVector
)

// ===== ЧЕЛОВЕКО-ЧИТАЕМЫЕ ИМЕНА СПОСОБОВ =====

fun getMethodDisplayName(name: String): String = when (name) {
    "Bluetooth" -> "Блютуз"
    "Wi-Fi Direct" -> "Прямой Wi-Fi"
    "Nearby Share" -> "Быстрая передача"
    "USB-кабель" -> "Кабель USB"
    "Облако (Drive)" -> "Облако Google"
    "Telegram" -> "Телеграм"
    "QR-код" -> "QR-код"
    "Send Anywhere" -> "Send Anywhere"
    "Трансляция экрана" -> "Трансляция на ТВ"
    "Google Drive" -> "Google Диск"
    else -> name
}

// ===== БАЗА ЗНАНИЙ =====

val methodsKnowledge = listOf(
    TransferMethod("Bluetooth", SpeedLevel.LOW, false, Icons.Default.Bluetooth, Color(0xFF2196F3),
        "Беспроводная связь на короткие расстояния",
        "Маленькие файлы, устройства рядом, нет интернета",
        "Работает везде, не требует интернета",
        "Медленный, только для небольших файлов",
        "Любые устройства с Bluetooth"),
    TransferMethod("Wi-Fi Direct", SpeedLevel.HIGH, false, Icons.Default.Wifi, Color(0xFF4CAF50),
        "Прямое соединение между устройствами через Wi-Fi",
        "Большие файлы между Android-устройствами без интернета",
        "Очень быстро, не требует интернета",
        "Нужна настройка, не все устройства поддерживают",
        "Только Android"),
    TransferMethod("Nearby Share", SpeedLevel.MEDIUM, false, Icons.Default.Share, Color(0xFF9C27B0),
        "Встроенная функция быстрой передачи",
        "Передача между Android-устройствами рядом",
        "Просто, быстро, встроено в Android",
        "Только для Android",
        "Только Android"),
    TransferMethod("USB-кабель", SpeedLevel.HIGH, false, Icons.Default.Usb, Color(0xFFFF9800),
        "Проводное соединение с компьютером",
        "Очень большие файлы, важна скорость",
        "Максимальная скорость, стабильно",
        "Нужен кабель и компьютер",
        "Android ↔ Компьютер"),
    TransferMethod("Облако (Drive)", SpeedLevel.MEDIUM, true, Icons.Default.Cloud, Color(0xFF00BCD4),
        "Онлайн-хранилище с синхронизацией",
        "Автосохранение, доступ с любого устройства",
        "Доступно отовсюду, автоматическая синхронизация",
        "Нужен интернет, ограниченное место",
        "Любые устройства с интернетом"),
    TransferMethod("Telegram", SpeedLevel.MEDIUM, true, Icons.Default.Send, Color(0xFF03A9F4),
        "Мессенджер с возможностью отправки файлов",
        "Передача между разными устройствами",
        "Работает везде, файлы до 2 ГБ",
        "Нужен интернет и аккаунт",
        "Любые устройства с Telegram"),
    TransferMethod("QR-код", SpeedLevel.LOW, false, Icons.Default.QrCode2, Color(0xFF607D8B),
        "Передача через сканирование QR-кода",
        "Быстрая передача без установки программ",
        "Не нужно ничего устанавливать",
        "Чуть медленнее, нужен QR-сканер",
        "Любые устройства с камерой"),
    TransferMethod("Send Anywhere", SpeedLevel.MEDIUM, false, Icons.Default.SendToMobile, Color(0xFFE91E63),
        "Приложение для передачи файлов",
        "Передача между Android и iPhone",
        "Работает между разными системами",
        "Нужно устанавливать приложение",
        "Android ↔ iPhone ↔ Компьютер"),
    TransferMethod("Трансляция экрана", SpeedLevel.MEDIUM, false, Icons.Default.Tv, Color(0xFF795548),
        "Беспроводная трансляция экрана на телевизор",
        "Показ фото/видео на большом экране",
        "Без проводов, поддержка большинством ТВ",
        "Только для показа экрана",
        "Android ↔ Smart TV"),
    TransferMethod("Google Drive", SpeedLevel.MEDIUM, true, Icons.Default.CloudUpload, Color(0xFF4CAF50),
        "Облачный сервис Google с автосинхронизацией",
        "Автоматическое сохранение фото",
        "Автосинхронизация, доступ с любого устройства",
        "Нужен интернет, 15 ГБ бесплатно",
        "Любые устройства с Google-аккаунтом")
)

val tasks = listOf(
    TransferTask(1, "Вы в парке с другом. Нужно отправить ему одно фото.", "Нет интернета, друг стоит рядом.",
        "photo", "Bluetooth",
        "Устройства рядом, интернета нет, файл маленький. Какой самый простой способ связи на короткое расстояние?",
        "Bluetooth работает без интернета на расстоянии до 10 метров. Идеально для одного фото."),
    TransferTask(2, "Нужно отправить видео другу.", "У вас Android, у друга iPhone. У обоих есть интернет.",
        "video", "Telegram",
        "Разные телефоны — нужен способ, который работает везде. Что установлено почти у всех?",
        "Telegram есть на Android и iPhone. Работает через интернет и принимает файлы до 2 ГБ."),
    TransferTask(3, "Нужно быстро передать контакт коллеге на компьютер.", "Нельзя устанавливать новые программы на его компьютер.",
        "contact", "QR-код",
        "Нужен способ без установки программ. Что можно показать и считать камерой?",
        "QR-код считывается камерой, данные передаются через браузер — ничего устанавливать не нужно."),
    TransferTask(4, "Нужно передать большое приложение другу.", "Интернет очень медленный, но вы рядом.",
        "app", "Wi-Fi Direct",
        "Файл большой, интернет плохой, но устройства рядом. Какая технология даёт высокую скорость без интернета?",
        "Wi-Fi Direct создаёт прямое быстрое соединение между устройствами, не используя интернет."),
    TransferTask(5, "Хотите, чтобы все новые фото с телефона автоматически появлялись на компьютере.", "Ничего не хотите делать вручную после настройки.",
        "folder", "Google Drive",
        "Нужна АВТОМАТИЗАЦИЯ. Какой сервис сам синхронизирует файлы между устройствами?",
        "Google Drive автоматически загружает фото и делает их доступными на всех устройствах."),
    TransferTask(6, "Нужно передать файл размером 5 ГБ на компьютер.", "Важна максимальная скорость и стабильность.",
        "bigfile", "USB-кабель",
        "Файл огромный. Какой способ даёт САМУЮ высокую скорость и не зависит от качества связи?",
        "USB-кабель даёт самую высокую скорость — самый быстрый и стабильный способ для больших файлов."),
    TransferTask(7, "Хотите показать фото с телефона всем гостям.", "Нужно вывести изображение на большой экран телевизора.",
        "screen", "Трансляция экрана",
        "Нужно не передать файл, а показать ЭКРАН телефона на телевизоре. Какая технология для этого?",
        "Трансляция экрана показывает содержимое телефона на телевизоре без проводов."),
    TransferTask(8, "Нужно быстро отправить 20 фото другу.", "У обоих Android, вы сидите рядом.",
        "photo", "Nearby Share",
        "Оба на Android, вы рядом. Какая встроенная функция создана специально для таких случаев?",
        "Nearby Share — встроенная функция Android для быстрой передачи между устройствами рядом."),
    TransferTask(9, "Вы подключили беспроводные наушники к телефону.", "Нужно понять, по какой технологии передаётся звук.",
        "voice", "Bluetooth",
        "Беспроводные наушники работают на коротком расстоянии без интернета. Какая это технология?",
        "Все беспроводные наушники используют Bluetooth для передачи звука."),
    TransferTask(10, "Хотите, чтобы все фото с камеры автоматически сохранялись в интернете.", "Чтобы не потерять их при поломке телефона.",
        "photo", "Google Drive",
        "Нужно облачное хранилище с АВТОМАТИЧЕСКОЙ загрузкой фото. Какой сервис от Google для этого?",
        "Google Drive автоматически сохраняет все снимки в облако.")
)

val receiversList = listOf(
    Receiver("Компьютер", Icons.Default.Computer, listOf("USB-кабель", "QR-код", "Bluetooth", "Google Drive", "Telegram", "Send Anywhere")),
    Receiver("Другой телефон", Icons.Default.PhoneAndroid, listOf("Bluetooth", "USB-кабель", "Wi-Fi Direct", "Nearby Share", "Send Anywhere", "Telegram", "QR-код", "Google Drive")),
    Receiver("Планшет", Icons.Default.Tablet, listOf("Bluetooth", "USB-кабель", "Wi-Fi Direct", "Nearby Share", "Send Anywhere", "Telegram", "QR-код", "Google Drive")),
    Receiver("Телевизор", Icons.Default.Tv, listOf("Трансляция экрана", "Bluetooth")),
    Receiver("Колонка", Icons.Default.Speaker, listOf("Bluetooth", "Wi-Fi Direct"))
)

val tutorialSteps = listOf(
    TutorialStep("reset_button", "Перезапуск", "Эта кнопка начинает игру заново.", Icons.Default.Refresh),
    TutorialStep("score_card", "Счёт", "Здесь видны ваши очки и номер задания.", Icons.Default.Star),
    TutorialStep("task_card", "Задание", "Внимательно прочитайте ситуацию и условия.", Icons.Default.Flag),
    TutorialStep("hint_block", "Подсказка", "Если не знаете ответ — загляните сюда.", Icons.Default.Lightbulb),
    TutorialStep("draggable_file", "Файл", "Перетащите его пальцем на нужное устройство.", Icons.Default.DragHandle),
    TutorialStep("receivers_list", "Устройства", "Список устройств, которым можно отправить файл.", Icons.Default.Devices)
)

// ===== VIEWMODEL =====

class TransferViewModel(context: Context) : ViewModel() {
    private val prefs = context.getSharedPreferences("transfer_game", Context.MODE_PRIVATE)

    private val _currentTaskIndex = MutableStateFlow(prefs.getInt("current_task", 0))
    val currentTaskIndex: StateFlow<Int> = _currentTaskIndex.asStateFlow()

    private val _score = MutableStateFlow(prefs.getInt("score", 0))
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _completedTasks = MutableStateFlow(prefs.getInt("completed_tasks", 0))
    val completedTasks: StateFlow<Int> = _completedTasks.asStateFlow()

    private val _showCertificate = MutableStateFlow(prefs.getBoolean("show_certificate", false))
    val showCertificate: StateFlow<Boolean> = _showCertificate.asStateFlow()

    private val _lastMessage = MutableStateFlow<Pair<String, Boolean>?>(null)
    val lastMessage: StateFlow<Pair<String, Boolean>?> = _lastMessage.asStateFlow()

    private val _introShown = MutableStateFlow(prefs.getBoolean("intro_shown", false))
    val introShown: StateFlow<Boolean> = _introShown.asStateFlow()

    private val _wrongAttempts = MutableStateFlow<Set<String>>(emptySet())
    val wrongAttempts: StateFlow<Set<String>> = _wrongAttempts.asStateFlow()

    private val _correctAnswer = MutableStateFlow<String?>(null)
    val correctAnswer: StateFlow<String?> = _correctAnswer.asStateFlow()

    fun checkAnswer(methodName: String): Pair<Boolean, String> {
        val task = tasks[_currentTaskIndex.value]
        val isCorrect = task.correctMethod == methodName

        if (isCorrect) {
            _correctAnswer.value = methodName
            _score.value += 10
            _completedTasks.value += 1
            _lastMessage.value = Pair("✅ Верно! +10 очков", false)

            if (_currentTaskIndex.value + 1 < tasks.size) {
                _currentTaskIndex.value += 1
                _wrongAttempts.value = emptySet()
                _correctAnswer.value = null
            } else {
                _showCertificate.value = true
            }
            saveProgress()
            return Pair(true, task.whyCorrect)
        } else {
            _wrongAttempts.value = _wrongAttempts.value + methodName
            _lastMessage.value = Pair("❌ Неверно. Попробуйте другой способ.", true)
            return Pair(false, task.hint)
        }
    }

    fun resetGame() {
        _currentTaskIndex.value = 0
        _score.value = 0
        _completedTasks.value = 0
        _showCertificate.value = false
        _lastMessage.value = null
        _wrongAttempts.value = emptySet()
        _correctAnswer.value = null
        saveProgress()
    }

    // ✅ ЭТОТ МЕТОД БЫЛ ПОТЕРЯН — ВОТ ОН СНОВА
    fun setIntroShown() {
        _introShown.value = true
        prefs.edit().putBoolean("intro_shown", true).apply()
    }

    fun clearMessage() {
        _lastMessage.value = null
    }

    private fun saveProgress() {
        prefs.edit()
            .putInt("current_task", _currentTaskIndex.value)
            .putInt("score", _score.value)
            .putInt("completed_tasks", _completedTasks.value)
            .putBoolean("show_certificate", _showCertificate.value)
            .apply()
    }
}

class TransferViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransferViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransferViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// ===== ОСНОВНОЙ ЭКРАН =====
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTransferScreen(navController: NavController, userId: Long) {
    val context = LocalContext.current
    val viewModel: TransferViewModel = viewModel(factory = TransferViewModelFactory(context))

    val currentTaskIndex by viewModel.currentTaskIndex.collectAsStateWithLifecycle()
    val score by viewModel.score.collectAsStateWithLifecycle()
    val completedTasks by viewModel.completedTasks.collectAsStateWithLifecycle()
    val showCertificate by viewModel.showCertificate.collectAsStateWithLifecycle()
    val lastMessage by viewModel.lastMessage.collectAsStateWithLifecycle()
    val wrongAttempts by viewModel.wrongAttempts.collectAsStateWithLifecycle()
    val correctAnswer by viewModel.correctAnswer.collectAsStateWithLifecycle()

    val currentTask = if (currentTaskIndex < tasks.size) tasks[currentTaskIndex] else null
    val isGameFinished = completedTasks >= tasks.size || showCertificate

    // ✅ Диалоги и состояния
    var showIntroDialog by remember { mutableStateOf(true) } // ВСЕГДА показывать при входе
    var showMethodDialog by remember { mutableStateOf(false) }
    var isTaskExpanded by remember { mutableStateOf(false) }
    var selectedReceiver by remember { mutableStateOf<Receiver?>(null) }
    var animateTransfer by remember { mutableStateOf(false) }
    var lastExplanation by remember { mutableStateOf<String?>(null) }
    var showExplanationDialog by remember { mutableStateOf(false) }

    // ✅ НОВОЕ: Состояние туториала
    var tutorialActive by remember { mutableStateOf(false) }
    var currentTutorialStep by remember { mutableIntStateOf(0) }
    val elementBounds = remember { mutableStateMapOf<String, Rect>() }

    // Перетаскивание
    var isDragging by remember { mutableStateOf(false) }
    var dragPosition by remember { mutableStateOf(Offset.Zero) }
    var receiverBounds by remember { mutableStateOf<Map<String, LayoutCoordinates>>(emptyMap()) }
    var dropTarget by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(lastMessage) {
        if (lastMessage != null) {
            delay(4000)
            viewModel.clearMessage()
        }
    }

    LaunchedEffect(currentTask) {
        receiverBounds = emptyMap()
        isTaskExpanded = true
    }

    LaunchedEffect(tutorialActive) {
        if (tutorialActive) {
            delay(800)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Обменник PRO", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("Задание ${currentTaskIndex + 1} из ${tasks.size}", fontSize = 12.sp, color = Color(0xFFB0BEC5))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.resetGame() },
                        modifier = Modifier.onGloballyPositioned { coords ->
                            elementBounds["reset_button"] = coords.boundsInWindow() // ✅ Строго Window!
                        }
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Сброс", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E1A2F),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        // ✅ КОРНЕВОЙ BOX БЕЗ PADDING (чтобы оверлей совпадал с boundsInWindow)
        Box(modifier = Modifier.fillMaxSize()) {

            // BOX С ИГРОВЫМ КОНТЕНТОМ (с padding)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Brush.verticalGradient(listOf(Color(0xFF1E1A2F), Color(0xFF12101F))))
            ) {
                if (isGameFinished) {
                    CertificateScreen(
                        onRestart = { viewModel.resetGame(); navController.popBackStack() },
                        userId = userId,
                        context = context
                    )
                } else if (currentTask != null) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Прогресс-бар
                        LinearProgressIndicator(
                            progress = (currentTaskIndex + 1).toFloat() / tasks.size,
                            modifier = Modifier.fillMaxWidth().height(4.dp),
                            color = Color(0xFF9C27B0),
                            trackColor = Color(0xFF2A2438)
                        )

                        // ✅ 2. Карточка счёта
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .onGloballyPositioned { coords ->
                                    elementBounds["score_card"] = coords.boundsInWindow() // Строго Window!
                                },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2438)),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color(0xFFFFD700))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("$score", color = Color(0xFFFFD700), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                }
                                Surface(shape = RoundedCornerShape(20.dp), color = Color(0xFF9C27B0)) {
                                    Text(
                                        "${currentTaskIndex + 1}/${tasks.size}",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }

                        // ✅ 3. Карточка задания
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .onGloballyPositioned { coords ->
                                    elementBounds["task_card"] = coords.boundsInWindow() // Строго Window!
                                }
                                .clickable { isTaskExpanded = !isTaskExpanded },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2438)),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("ЗАДАНИЕ", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9C27B0))
                                    Icon(
                                        imageVector = if (isTaskExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = null,
                                        tint = Color(0xFF9C27B0),
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                if (isTaskExpanded) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(currentTask.situation, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White, lineHeight = 26.sp)
                                    Spacer(modifier = Modifier.height(16.dp))

                                    // ✅ 4. Блок подсказки
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .onGloballyPositioned { coords ->
                                                elementBounds["hint_block"] = coords.boundsInWindow() // Строго Window!
                                            },
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFF9800).copy(alpha = 0.15f)),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text("⚠️", fontSize = 20.sp)
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text("Условия", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFB74D))
                                            }
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(currentTask.constraints, fontSize = 15.sp, color = Color(0xFFFFCC80), lineHeight = 21.sp)
                                            Spacer(modifier = Modifier.height(12.dp))
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text("💡", fontSize = 20.sp)
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text("Подсказка", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFCE93D8))
                                            }
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(currentTask.hint, fontSize = 15.sp, color = Color(0xFFE1BEE7), lineHeight = 21.sp)
                                        }
                                    }
                                } else {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(currentTask.situation, fontSize = 16.sp, color = Color.White, maxLines = 2, lineHeight = 22.sp)
                                    Text("Нажмите, чтобы увидеть подробности", fontSize = 13.sp, color = Color(0xFFB0BEC5), modifier = Modifier.padding(top = 6.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Игровое поле
                        Row(
                            modifier = Modifier.fillMaxWidth().weight(1f).padding(start = 16.dp, end = 16.dp, bottom = 80.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            // ✅ 5. Перетаскиваемый файл
                            Box(
                                modifier = Modifier.onGloballyPositioned { coords ->
                                    elementBounds["draggable_file"] = coords.boundsInWindow() // Строго Window!
                                }
                            ) {
                                DraggableFile(
                                    fileType = currentTask.fileType,
                                    onDragStart = { absoluteOffset ->
                                        if (!tutorialActive) { isDragging = true; dragPosition = absoluteOffset }
                                    },
                                    onDrag = { absoluteOffset ->
                                        if (!tutorialActive) {
                                            dragPosition = absoluteOffset
                                            var newDropTarget: String? = null
                                            receiverBounds.forEach { (name, coords) ->
                                                val position = coords.positionInWindow()
                                                val rect = Rect(position.x, position.y, position.x + coords.size.width, position.y + coords.size.height)
                                                if (rect.contains(absoluteOffset)) newDropTarget = name
                                            }
                                            dropTarget = newDropTarget
                                        }
                                    },
                                    onDragEnd = {
                                        if (!tutorialActive) {
                                            dropTarget?.let { targetName ->
                                                val receiver = receiversList.find { it.name == targetName }
                                                if (receiver != null) { selectedReceiver = receiver; showMethodDialog = true }
                                            }
                                            isDragging = false; dragPosition = Offset.Zero; dropTarget = null
                                        }
                                    }
                                )
                            }

                            val scrollState = rememberScrollState()
                            val density = LocalDensity.current

                            // ✅ 6. Список устройств
                            Box(
                                modifier = Modifier
                                    .width(240.dp)
                                    .fillMaxSize()
                                    .padding(start = 16.dp)
                                    .onGloballyPositioned { coords ->
                                        elementBounds["receivers_list"] = coords.boundsInWindow() // Строго Window!
                                    }
                            ) {
                                Column(
                                    modifier = Modifier.matchParentSize().verticalScroll(scrollState).padding(end = 10.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    receiversList.forEach { receiver ->
                                        DropReceiver(
                                            receiver = receiver,
                                            isHighlighted = dropTarget == receiver.name,
                                            onCoordinatesReady = { coords -> receiverBounds = receiverBounds + (receiver.name to coords) }
                                        )
                                    }
                                }

                                // Градиент и индикатор прокрутки (без изменений, они работают отлично)
                                if (scrollState.value > 0) {
                                    Box(modifier = Modifier.fillMaxWidth().height(25.dp).align(Alignment.TopCenter).background(Brush.verticalGradient(listOf(Color(0xFF1E1A2F).copy(alpha = 0.95f), Color.Transparent))))
                                }
                                if (scrollState.value < scrollState.maxValue) {
                                    Box(modifier = Modifier.fillMaxWidth().height(35.dp).align(Alignment.BottomCenter).background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xFF1E1A2F).copy(alpha = 0.95f))))) {
                                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Прокрутите вниз", tint = Color(0xFF9C27B0).copy(alpha = 0.8f), modifier = Modifier.align(Alignment.BottomCenter).size(22.dp))
                                    }
                                }
                                if (scrollState.maxValue > 0) {
                                    Box(modifier = Modifier.align(Alignment.CenterEnd).width(4.dp).fillMaxHeight().padding(vertical = 4.dp)) {
                                        Box(modifier = Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(2.dp)))
                                        val indicatorHeightPx = with(density) { 40.dp.toPx() }
                                        val trackHeightPx = scrollState.viewportSize.toFloat()
                                        val maxScrollPx = scrollState.maxValue.toFloat()
                                        val scrollFraction = if (maxScrollPx > 0) scrollState.value.toFloat() / maxScrollPx else 0f
                                        val maxOffsetPx = trackHeightPx - indicatorHeightPx
                                        val offsetYPx = scrollFraction * maxOffsetPx
                                        Box(modifier = Modifier.fillMaxWidth().height(40.dp).offset { IntOffset(0, offsetYPx.roundToInt()) }.background(Color(0xFF9C27B0).copy(alpha = 0.8f), RoundedCornerShape(2.dp)))
                                    }
                                }
                            }
                        }

                        if (animateTransfer) TransferAnimation(onAnimationEnd = { animateTransfer = false })

                        lastMessage?.let { (msg, isError) ->
                            AnimatedVisibility(visible = true, enter = fadeIn(tween(300)) + slideInVertically(initialOffsetY = { it }, animationSpec = tween(300)), exit = fadeOut(tween(500)) + slideOutVertically(targetOffsetY = { it }, animationSpec = tween(500))) {
                                Card(modifier = Modifier.padding(8.dp), colors = CardDefaults.cardColors(containerColor = if (isError) Color(0xFFB00020) else Color(0xFF4CAF50)), shape = RoundedCornerShape(12.dp)) {
                                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(if (isError) Icons.Default.Error else Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(msg, color = Color.White, fontSize = 13.sp, modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }

                    // Плавающая иконка при перетаскивании
                    if (isDragging && dragPosition != Offset.Zero) {
                        val density = LocalDensity.current
                        val iconSize = 70.dp
                        val iconSizePx = with(density) { iconSize.toPx() }
                        val halfSize = (iconSizePx / 2).roundToInt()
                        val verticalCorrection = (iconSizePx * 1.0).roundToInt()
                        val icon = when (currentTask.fileType) {
                            "photo" -> Icons.Default.PhotoCamera
                            "video" -> Icons.Default.VideoCameraBack
                            "contact" -> Icons.Default.Contacts
                            "app" -> Icons.Default.Apps
                            "folder" -> Icons.Default.Folder
                            "bigfile" -> Icons.Default.Storage
                            "screen" -> Icons.Default.ScreenRotation
                            "voice" -> Icons.Default.Mic
                            else -> Icons.Default.FileCopy
                        }
                        Box(
                            modifier = Modifier.offset { IntOffset((dragPosition.x - halfSize).roundToInt(), (dragPosition.y - halfSize - verticalCorrection).roundToInt()) }
                                .size(iconSize).shadow(12.dp, CircleShape).background(Color(0xFF9C27B0).copy(alpha = 0.9f), CircleShape).padding(12.dp)
                        ) {
                            Icon(icon, contentDescription = null, modifier = Modifier.fillMaxSize(), tint = Color.White)
                        }
                    }
                } else {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } // Конец Box с padding
        } // Конец Box с padding (внутри Scaffold)
    } // Конец Scaffold
    if (tutorialActive && currentTutorialStep < tutorialSteps.size) {
        TutorialOverlay(
            steps = tutorialSteps,
            currentStep = currentTutorialStep,
            elementBounds = elementBounds,
            onNext = {
                if (currentTutorialStep + 1 < tutorialSteps.size) {
                    currentTutorialStep += 1
                } else {
                    tutorialActive = false
                }
            },
            onSkip = { tutorialActive = false }
        )
    }

    // ✅ Диалог выбора метода (с новым заголовком)
    if (showMethodDialog && selectedReceiver != null) {
        MethodSelectionDialog(
            receiver = selectedReceiver!!,
            wrongAttempts = wrongAttempts,
            correctAnswer = correctAnswer,
            onSelectMethod = { methodName ->
                val (isCorrect, explanation) = viewModel.checkAnswer(methodName)
                if (isCorrect) {
                    vibrate(context)
                    lastExplanation = explanation
                    showExplanationDialog = true
                    coroutineScope.launch {
                        delay(1500)
                        animateTransfer = true
                        delay(800)
                        showMethodDialog = false
                        selectedReceiver = null
                        showExplanationDialog = false
                    }
                }
            },
            onDismiss = {
                showMethodDialog = false
                selectedReceiver = null
            }
        )
    }

    // Диалог с объяснением правильного ответа
    if (showExplanationDialog && lastExplanation != null) {
        AlertDialog(
            onDismissRequest = { showExplanationDialog = false },
            containerColor = Color(0xFF1E1A2F),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Почему это верно?", color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)
                }
            },
            text = { Text(lastExplanation ?: "", color = Color.White, fontSize = 14.sp, lineHeight = 20.sp) },
            confirmButton = {},
            shape = RoundedCornerShape(16.dp)
        )
    }

    // ✅ Интро-диалог (показывается КАЖДЫЙ РАЗ при входе)
    if (showIntroDialog) {
        AlertDialog(
            onDismissRequest = { /* Нельзя закрыть без кнопки */ },
            containerColor = Color(0xFF1E1A2F),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.School, contentDescription = null, tint = Color(0xFF9C27B0), modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Как играть", fontWeight = FontWeight.Bold, color = Color(0xFF9C27B0), fontSize = 20.sp)
                }
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text("📖 В каждом задании — жизненная ситуация с условиями.", color = Color.White, fontSize = 15.sp, lineHeight = 21.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("🎯 Ваша задача — выбрать правильный способ передачи файла.", color = Color.White, fontSize = 15.sp, lineHeight = 21.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("👆 Перетащите файл на устройство, затем выберите метод из списка.", color = Color.White, fontSize = 15.sp, lineHeight = 21.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("💡 Читайте подсказку — она направит вас к правильному ответу.", color = Color(0xFFCE93D8), fontSize = 15.sp, lineHeight = 21.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF9C27B0).copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "После этого окна вас ждёт небольшая экскурсия по интерфейсу — облачка с подсказками покажут, что и как работает.",
                            modifier = Modifier.padding(12.dp),
                            color = Color(0xFFE1BEE7),
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showIntroDialog = false
                        viewModel.setIntroShown()
                        tutorialActive = true
                        currentTutorialStep = 0
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Понятно, начать!", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun TutorialOverlay(
    steps: List<TutorialStep>,
    currentStep: Int,
    elementBounds: Map<String, Rect>,
    onNext: () -> Unit,
    onSkip: () -> Unit
) {
    if (currentStep >= steps.size) return
    val step = steps[currentStep]
    val targetBounds = elementBounds[step.targetKey]
    val density = LocalDensity.current
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val screenWidthPx = displayMetrics.widthPixels.toFloat()
    val screenHeightPx = displayMetrics.heightPixels.toFloat()

    // Затемнение фона (✅ ДОБАВЛЕНО zIndex, чтобы быть поверх хедера)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(100f)
            .background(Color.Black.copy(alpha = 0.7f))
            .clickable(enabled = false) {}
    ) {
        // Подсветка целевого элемента (рамка)
        if (targetBounds != null) {
            val paddingPx = with(density) { 8.dp.toPx() } // Чуть увеличили отступ рамки
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (targetBounds.left - paddingPx).roundToInt(),
                            (targetBounds.top - paddingPx).roundToInt()
                        )
                    }
                    .width(with(density) { (targetBounds.width + paddingPx * 2).toDp() })
                    .height(with(density) { (targetBounds.height + paddingPx * 2).toDp() })
                    .border(
                        width = 3.dp, // Чуть толще, чтобы лучше было видно
                        brush = Brush.horizontalGradient(listOf(Color(0xFF9C27B0), Color(0xFFE040FB))),
                        shape = RoundedCornerShape(12.dp)
                    )
            )
        }

        // Облачко с подсказкой
        CoachMarkCard(
            step = step,
            stepIndex = currentStep,
            totalSteps = steps.size,
            targetBounds = targetBounds,
            screenWidthPx = screenWidthPx,
            screenHeightPx = screenHeightPx,
            onNext = onNext,
            onSkip = onSkip
        )
    }
}

// ===== КОМПАКТНОЕ ОБЛАЧКО С УКАЗАТЕЛЕМ =====
@Composable
fun CoachMarkCard(
    step: TutorialStep,
    stepIndex: Int,
    totalSteps: Int,
    targetBounds: Rect?,
    screenWidthPx: Float,
    screenHeightPx: Float,
    onNext: () -> Unit,
    onSkip: () -> Unit
) {
    val density = LocalDensity.current
    val cardWidthPx = with(density) { 260.dp.toPx() }
    val cardHeightPx = with(density) { 110.dp.toPx() } // ✅ Компактная высота
    val arrowSizePx = with(density) { 10.dp.toPx() }
    val gapPx = with(density) { 8.dp.toPx() } // ✅ Минимальный отступ

    // ✅ ЛОГИКА ПОЗИЦИОНИРОВАНИЯ: облачко ВСЕГДА указывает на элемент
    val showAbove: Boolean
    val xOffsetPx: Float
    val yOffsetPx: Float

    if (targetBounds != null) {
        val targetCenterX = (targetBounds.left + targetBounds.right) / 2
        val targetCenterY = (targetBounds.top + targetBounds.bottom) / 2

        // Центрируем по горизонтали относительно элемента
        xOffsetPx = (targetCenterX - cardWidthPx / 2).coerceIn(12f, screenWidthPx - cardWidthPx - 12f)

        // Решаем, где разместить: над или под элементом
        val spaceAbove = targetBounds.top
        val spaceBelow = screenHeightPx - targetBounds.bottom

        // Выбираем сторону с БОЛЬШЕ места
        if (spaceBelow >= cardHeightPx + arrowSizePx + gapPx + 12f) {
            // Размещаем ПОД элементом
            showAbove = false
            yOffsetPx = targetBounds.bottom + gapPx + arrowSizePx
        } else if (spaceAbove >= cardHeightPx + arrowSizePx + gapPx + 12f) {
            // Размещаем НАД элементом
            showAbove = true
            yOffsetPx = targetBounds.top - gapPx - arrowSizePx - cardHeightPx
        } else {
            // Не хватает места — по центру экрана
            showAbove = targetCenterY > screenHeightPx / 2
            yOffsetPx = if (showAbove) {
                targetBounds.top - gapPx - arrowSizePx - cardHeightPx
            } else {
                targetBounds.bottom + gapPx + arrowSizePx
            }
        }
    } else {
        showAbove = false
        xOffsetPx = (screenWidthPx - cardWidthPx) / 2
        yOffsetPx = (screenHeightPx - cardHeightPx) / 2
    }

    // Позиция стрелки по X (всегда указывает на центр элемента)
    val arrowXOffsetPx = if (targetBounds != null) {
        val targetCenterX = (targetBounds.left + targetBounds.right) / 2
        (targetCenterX - xOffsetPx).coerceIn(20f, cardWidthPx - 20f)
    } else {
        cardWidthPx / 2
    }

    Box(
        modifier = Modifier.offset {
            IntOffset(xOffsetPx.roundToInt(), yOffsetPx.roundToInt())
        }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Стрелка СВЕРХУ (если облачко ПОД элементом)
            if (!showAbove) {
                Box(
                    modifier = Modifier
                        .offset { IntOffset(arrowXOffsetPx.roundToInt() - arrowSizePx.roundToInt(), 0) }
                        .size(0.dp)
                        .drawArrowDown(arrowSizePx, Color(0xFF2A2438))
                )
                Spacer(modifier = Modifier.height(arrowSizePx.dp))
            }

            // Само облачко
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2438)),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                modifier = Modifier.width(with(density) { cardWidthPx.toDp() })
            ) {
                // ✅ КОМПАКТНОЕ содержимое
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            step.icon,
                            contentDescription = null,
                            tint = Color(0xFFE040FB),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            step.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF9C27B0)
                        ) {
                            Text(
                                "${stepIndex + 1}/$totalSteps",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        step.text,
                        fontSize = 12.5.sp,
                        color = Color(0xFFE0E0E0),
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = onSkip,
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text("Пропустить", color = Color(0xFF90A4AE), fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Button(
                            onClick = onNext,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text(
                                if (stepIndex + 1 < totalSteps) "Далее →" else "Начать!",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Стрелка СНИЗУ (если облачко НАД элементом)
            if (showAbove) {
                Spacer(modifier = Modifier.height(arrowSizePx.dp))
                Box(
                    modifier = Modifier
                        .offset { IntOffset(arrowXOffsetPx.roundToInt() - arrowSizePx.roundToInt(), 0) }
                        .size(0.dp)
                        .drawArrowUp(arrowSizePx, Color(0xFF2A2438))
                )
            }
        }
    }
}

// ===== РИСОВАНИЕ СТРЕЛОК =====
fun Modifier.drawArrowDown(sizePx: Float, color: Color): Modifier = this.then(
    Modifier.drawBehind {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(sizePx, 0f)
            lineTo(sizePx / 2, sizePx)
            close()
        }
        drawPath(path, color)
    }
)

fun Modifier.drawArrowUp(sizePx: Float, color: Color): Modifier = this.then(
    Modifier.drawBehind {
        val path = Path().apply {
            moveTo(sizePx / 2, 0f)
            lineTo(sizePx, sizePx)
            lineTo(0f, sizePx)
            close()
        }
        drawPath(path, color)
    }
)

@OptIn(ExperimentalFoundationApi::class) // ✅ Разрешаем использование официального скроллбара
@Composable
fun MethodSelectionDialog(
    receiver: Receiver,
    wrongAttempts: Set<String>,
    correctAnswer: String?,
    onSelectMethod: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1A2F),
        title = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(receiver.icon, contentDescription = null, tint = Color(0xFF9C27B0), modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "Как передать на ${receiver.name.lowercase()}?",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF9C27B0).copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("👆", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Посмотрите на описание способов и нажмите на подходящий",
                            fontSize = 13.sp,
                            color = Color(0xFFCE93D8),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        },
        text = {
            val scrollState = rememberLazyListState()
            val density = LocalDensity.current

            Box(modifier = Modifier.heightIn(max = 500.dp)) {
                // ✅ ДОБАВЛЕНО: flingBehavior для более плавного и естественного скролла
                LazyColumn(
                    state = scrollState,
                    flingBehavior = ScrollableDefaults.flingBehavior(),
                    modifier = Modifier.fillMaxWidth().padding(end = 12.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(receiver.methods) { methodName ->
                        val method = methodsKnowledge.find { it.name == methodName }
                        if (method != null) {
                            val isWrong = wrongAttempts.contains(methodName)
                            val isCorrect = correctAnswer == methodName

                            MethodCard(
                                method = method,
                                isWrong = isWrong,
                                isCorrect = isCorrect,
                                onClick = {
                                    if (!isWrong && correctAnswer == null) {
                                        onSelectMethod(methodName)
                                    }
                                }
                            )
                            // Отступ между карточками
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }

                // Ручная полоса прокрутки (осталась без изменений, но теперь работает с плавным скроллом)
                val layoutInfo = scrollState.layoutInfo
                val totalItems = layoutInfo.totalItemsCount
                val visibleItems = layoutInfo.visibleItemsInfo.size

                if (totalItems > visibleItems) {
                    val firstVisible = layoutInfo.visibleItemsInfo.firstOrNull()?.index ?: 0
                    val scrollFraction = if (totalItems > visibleItems) {
                        firstVisible.toFloat() / (totalItems - visibleItems).toFloat()
                    } else 0f

                    val trackHeightDp = 500.dp
                    val thumbHeightRatio = visibleItems.toFloat() / totalItems.toFloat()
                    val thumbHeightDp = trackHeightDp * thumbHeightRatio
                    val maxOffsetDp = trackHeightDp - thumbHeightDp
                    val thumbOffsetDp = maxOffsetDp * scrollFraction

                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .width(6.dp)
                            .fillMaxHeight()
                            .padding(vertical = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(3.dp))
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(thumbHeightDp.coerceAtLeast(30.dp))
                                .offset(y = thumbOffsetDp)
                                .background(Color(0xFF9C27B0).copy(alpha = 0.8f), RoundedCornerShape(3.dp))
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена", color = Color(0xFFB0BEC5), fontSize = 16.sp)
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun MethodCard(
    method: TransferMethod,
    isWrong: Boolean,
    isCorrect: Boolean,
    onClick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    // ✅ ПОНЯТНЫЕ ФОРМУЛИРОВКИ ДЛЯ НОВИЧКОВ
    val speedText = when (method.speedLevel) {
        SpeedLevel.LOW -> "Подходит для 1-2 фото или текста"
        SpeedLevel.MEDIUM -> "Подходит для нескольких фото"
        SpeedLevel.HIGH -> "Подходит для видео и больших файлов"
    }

    val internetText = if (method.needInternet) {
        "Требуется подключение к интернету"
    } else {
        "Работает без интернета (устройства должны быть рядом)"
    }

    val backgroundColor = when {
        isCorrect -> Color(0xFF4CAF50).copy(alpha = 0.15f)
        isWrong -> Color(0xFFB00020).copy(alpha = 0.12f)
        else -> Color(0xFF2A2438)
    }

    val borderColor = when {
        isCorrect -> Color(0xFF4CAF50)
        isWrong -> Color(0xFFB00020)
        else -> Color(0xFF9C27B0).copy(alpha = 0.3f)
    }

    val displayName = getMethodDisplayName(method.name)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, borderColor, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Заголовок (кликабельный для раскрытия)
            Row(
                modifier = Modifier.fillMaxWidth().clickable { isExpanded = !isExpanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(method.icon, contentDescription = null, tint = Color(0xFFCE93D8), modifier = Modifier.size(36.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(displayName, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)
                    Text(method.description, fontSize = 13.sp, color = Color(0xFFB0BEC5), lineHeight = 18.sp)
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFFCE93D8),
                    modifier = Modifier.size(24.dp)
                )
            }

            // ✅ РАСКРЫТАЯ ИНФОРМАЦИЯ (Максимально упрощена)
            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))

                // Главная характеристика (вместо сложной таблицы)
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1A2F)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("💡", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(speedText, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFE0E0E0))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(internetText, fontSize = 13.sp, color = Color(0xFFB0BEC5))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Совместимость простым языком
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📱", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Работает с: ${method.compatibleDevices}", fontSize = 13.sp, color = Color(0xFFB0BEC5), lineHeight = 18.sp, modifier = Modifier.weight(1f))
                }

                // ✅ КНОПКА ВЫБОРА (УМЕНЬШЕН ШРИФТ)
                if (!isWrong && !isCorrect) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = onClick,
                        modifier = Modifier.fillMaxWidth().height(44.dp), // Чуть ниже высота
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            "Выбрать: $displayName",
                            color = Color.White,
                            fontSize = 13.sp, // ✅ УМЕНЬШЕН ШРИФТ
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Статус ошибки/успеха
            if (isWrong) {
                Spacer(modifier = Modifier.height(10.dp))
                Text("❌ Этот способ не подойдет для текущей задачи", fontSize = 13.sp, color = Color(0xFFEF9A9A), fontWeight = FontWeight.Medium)
            }
            if (isCorrect) {
                Spacer(modifier = Modifier.height(10.dp))
                Text("✅ Отличный выбор!", fontSize = 13.sp, color = Color(0xFFA5D6A7), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun DraggableFile(
    fileType: String,
    onDragStart: (Offset) -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit
) {
    val icon = when (fileType) {
        "photo" -> Icons.Default.PhotoCamera
        "video" -> Icons.Default.VideoCameraBack
        "contact" -> Icons.Default.Contacts
        "app" -> Icons.Default.Apps
        "folder" -> Icons.Default.Folder
        "bigfile" -> Icons.Default.Storage
        "screen" -> Icons.Default.ScreenRotation
        "voice" -> Icons.Default.Mic
        else -> Icons.Default.FileCopy
    }
    var viewPosition by remember { mutableStateOf(Offset.Zero) }

    Card(
        modifier = Modifier
            .size(100.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .onGloballyPositioned { coordinates -> viewPosition = coordinates.positionInWindow() }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { startOffset ->
                        val absoluteStart = viewPosition + startOffset
                        onDragStart(absoluteStart)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val newPosition = viewPosition + change.position
                        onDrag(newPosition)
                    },
                    onDragEnd = { onDragEnd() }
                )
            },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2438)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(40.dp), tint = Color(0xFF9C27B0))
            Spacer(modifier = Modifier.height(6.dp))
            Text(fileType, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun DropReceiver(
    receiver: Receiver,
    isHighlighted: Boolean,
    onCoordinatesReady: (LayoutCoordinates) -> Unit
) {
    val scale by animateFloatAsState(targetValue = if (isHighlighted) 1.05f else 1f, label = "scale")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .scale(scale)
            .onGloballyPositioned { coordinates -> onCoordinatesReady(coordinates) },
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlighted) Color(0xFF9C27B0).copy(alpha = 0.3f) else Color(0xFF2A2438)
        ),
        elevation = CardDefaults.cardElevation(if (isHighlighted) 12.dp else 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(receiver.icon, contentDescription = null, modifier = Modifier.size(32.dp), tint = Color(0xFF9C27B0))
            Spacer(modifier = Modifier.width(12.dp))
            Text(receiver.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp, maxLines = 1)
        }
    }
}

@Composable
fun TransferAnimation(onAnimationEnd: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 0f,
        animationSpec = infiniteRepeatable(animation = tween(500), repeatMode = RepeatMode.Reverse)
    )
    LaunchedEffect(Unit) {
        delay(600)
        onAnimationEnd()
    }
    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = alpha * 0.5f))) {
        Text(text = "📡 Передача...", modifier = Modifier.align(Alignment.Center), color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CertificateScreen(onRestart: () -> Unit, userId: Long, context: Context) {
    LaunchedEffect(Unit) {
        val prefs = context.getSharedPreferences("game_medium_$userId", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("transfer_game_completed", true).apply()
        AchievementManager.init(context)
        val achievements = AchievementManager.getAllAchievements()
        val transferAchievement = achievements.find { it.id == "transfer_pro" }
        transferAchievement?.let {
            AchievementManager.checkAndUnlock(context, userId, it)
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.padding(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2438)),
            elevation = CardDefaults.cardElevation(12.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.EmojiEvents, contentDescription = null, modifier = Modifier.size(80.dp), tint = Color(0xFFFFD700))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Поздравляем!", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                Text("Мастер передачи данных", fontSize = 16.sp, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Вы успешно прошли все 10 заданий!", color = Color.LightGray, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onRestart,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Вернуться в меню", color = Color.White)
                }
            }
        }
    }
}

private fun vibrate(context: Context) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        vibrator?.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator?.vibrate(50)
    }
}