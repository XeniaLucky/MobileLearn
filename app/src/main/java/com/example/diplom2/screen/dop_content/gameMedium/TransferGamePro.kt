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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import com.example.diplom2.screen.AchievementManager

data class TransferTask(
    val id: Int,
    val description: String,
    val fileType: String,
    val correctMethod: String,
    val hint: String
)

data class TransferMethod(
    val name: String,
    val speed: String,
    val needInternet: Boolean,
    val difficulty: String,
    val devices: String,
    val pros: String,
    val cons: String
)

data class Receiver(
    val name: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val methods: List<String>
)

val methodsKnowledge = listOf(
    TransferMethod("Bluetooth", "~2 МБ/с", false, "Легко", "Все",
        "Не требует интернета, работает везде", "Медленный, не для больших файлов"),
    TransferMethod("Wi-Fi Direct", "до 200 МБ/с", false, "Средне", "Android-Android",
        "Быстро, без интернета", "Требуется настройка, не все устройства поддерживают"),
    TransferMethod("Nearby Share", "~50 МБ/с", false, "Легко", "Android/Chromebook",
        "Просто, быстро, встроено в Android", "Только для Android / Chromebook"),
    TransferMethod("USB-кабель", "~400 МБ/с", false, "Средне", "Android-ПК",
        "Очень быстро, стабильно", "Нужен кабель и компьютер"),
    TransferMethod("Облако (Drive)", "зависит от тарифа", true, "Легко", "Все",
        "Доступно отовсюду, не нужен кабель", "Требует интернет, размер хранилища ограничен"),
    TransferMethod("Telegram Saved", "быстро", true, "Легко", "Все",
        "Просто, работает на любых устройствах", "Нужен интернет и аккаунт"),
    TransferMethod("QR-код (snapdrop)", "~10 МБ/с", false, "Средне", "Android-любой",
        "Без интернета, через браузер", "Чуть медленнее, нужен QR-сканер"),
    TransferMethod("Send Anywhere", "~20 МБ/с", false, "Легко", "Android-iOS",
        "Кроссплатформенный, простой", "Реклама, требует установки приложения"),
    TransferMethod("Miracast / Chromecast", "до 50 МБ/с", false, "Средне", "Android-ТВ",
        "Для экрана, без проводов", "Только для видео/экрана, не для файлов"),
    TransferMethod("Google Drive автосинхр.", "~50 МБ/с", true, "Легко", "Android-Облако",
        "Автоматическая синхронизация", "Требует интернет, настройка")
)

val allMethods = methodsKnowledge.map { it.name }

val tasks = listOf(
    TransferTask(1, "Передать фото на другой телефон через Bluetooth", "photo", "Bluetooth", "Bluetooth — стандартный способ, не требует интернета."),
    TransferTask(2, "Отправить видео на устройство с другой ОС через Telegram", "video", "Telegram Saved", "Telegram работает везде, где есть интернет."),
    TransferTask(3, "Скопировать контакт на ноутбук через QR-код", "contact", "QR-код (snapdrop)", "Сайт snapdrop.net создаёт временный Wi-Fi Direct канал."),
    TransferTask(4, "Передать приложение (.apk) через Wi-Fi Direct", "apk", "Wi-Fi Direct", "Wi-Fi Direct быстрее Bluetooth для больших файлов."),
    TransferTask(5, "Синхронизировать папку с Google Drive", "folder", "Google Drive автосинхр.", "Облако автоматически сохраняет изменения."),
    TransferTask(6, "Отправить большой файл (>1 ГБ) через USB", "bigfile", "USB-кабель", "USB даёт максимальную скорость и стабильность."),
    TransferTask(7, "Расшарить экран телефона на телевизор", "screen", "Miracast / Chromecast", "Miracast или Chromecast — для зеркалирования."),
    TransferTask(8, "Передать несколько фото через Nearby Share", "photo", "Nearby Share", "Nearby Share удобен для передачи между устройствами на одной системе."),
    TransferTask(9, "Отправить голосовое сообщение через Bluetooth-гарнитуру", "voice", "Bluetooth", "Сначала сопрягаем гарнитуру, затем передаём."),
    TransferTask(10, "Настроить автоматическую синхронизацию камеры с облаком", "photo", "Google Drive автосинхр.", "В Google Drive есть функция автосохранения фото.")
)

val receiversList = listOf(
    Receiver("Ноутбук", Icons.Default.Computer, listOf(
        "USB-кабель", "QR-код (snapdrop)", "Bluetooth", "Google Drive автосинхр.", "Telegram Saved", "Send Anywhere"
    )),
    Receiver("Другой телефон", Icons.Default.PhoneAndroid, listOf(
        "Bluetooth", "USB-кабель", "Wi-Fi Direct", "Nearby Share", "Send Anywhere", "Telegram Saved", "QR-код (snapdrop)", "Google Drive автосинхр."
    )),
    Receiver("Планшет", Icons.Default.Tablet, listOf(
        "Bluetooth", "USB-кабель", "Wi-Fi Direct", "Nearby Share", "Send Anywhere", "Telegram Saved", "QR-код (snapdrop)", "Google Drive автосинхр."
    )),
    Receiver("Телевизор", Icons.Default.Tv, listOf("Miracast / Chromecast", "Bluetooth")),
    Receiver("Умная колонка", Icons.Default.Speaker, listOf("Bluetooth", "Wi-Fi Direct"))
)

// ----- ViewModel -----
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

    fun checkAnswer(methodName: String): Boolean {
        val task = tasks[_currentTaskIndex.value]
        val isCorrect = task.correctMethod == methodName
        if (isCorrect) {
            val newScore = _score.value + 10
            _score.value = newScore
            val newCompleted = _completedTasks.value + 1
            _completedTasks.value = newCompleted
            _lastMessage.value = Pair("✅ Верно! +10 очков. ${task.hint}", false)

            if (_currentTaskIndex.value + 1 < tasks.size) {
                _currentTaskIndex.value += 1
            } else {
                _showCertificate.value = true
            }
            saveProgress()
        } else {
            _lastMessage.value = Pair("❌ Неверно. Подсказка: ${task.hint}", true)
        }
        return isCorrect
    }

    fun resetGame() {
        _currentTaskIndex.value = 0
        _score.value = 0
        _completedTasks.value = 0
        _showCertificate.value = false
        _lastMessage.value = null
        saveProgress()
    }

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

// ----- UI -----
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTransferScreen(navController: NavController, userId: Long) {
    val context = LocalContext.current
    val viewModel: TransferViewModel = viewModel(factory = TransferViewModelFactory(context))
    val currentTaskIndex by viewModel.currentTaskIndex.collectAsState()
    val score by viewModel.score.collectAsState()
    val completedTasks by viewModel.completedTasks.collectAsState()
    val showCertificate by viewModel.showCertificate.collectAsState()
    val lastMessage by viewModel.lastMessage.collectAsState()
    val introShown by viewModel.introShown.collectAsState()
    val currentTask = if (currentTaskIndex < tasks.size) tasks[currentTaskIndex] else null
    val isGameFinished = completedTasks >= tasks.size || showCertificate

    var showHelpDialog by remember { mutableStateOf(false) }
    var showMethodDialog by remember { mutableStateOf(false) }
    var selectedReceiver by remember { mutableStateOf<Receiver?>(null) }
    var animateTransfer by remember { mutableStateOf(false) }
    var showIntroDialog by remember { mutableStateOf(!introShown) }
    val coroutineScope = rememberCoroutineScope()

    var isDragging by remember { mutableStateOf(false) }
    var dragPosition by remember { mutableStateOf(Offset.Zero) }
    var receiverBounds by remember { mutableStateOf<Map<String, LayoutCoordinates>>(emptyMap()) }
    var dropTarget by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(lastMessage) {
        if (lastMessage != null) {
            delay(5000)
            viewModel.clearMessage()
        }
    }

    LaunchedEffect(currentTask) {
        receiverBounds = emptyMap()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Обменник PRO") },
                actions = {
                    IconButton(onClick = { viewModel.resetGame() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Сброс")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E1A2F),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
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
                    // --- Верхняя панель ---
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2438)),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = Color(0xFFFFD700)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "$score",
                                    color = Color(0xFFFFD700),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color(0xFF9C27B0)
                            ) {
                                Text(
                                    text = "${currentTaskIndex + 1}/${tasks.size}",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    // --- Карточка задания и подсказки ---
                    val borderBrush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF9C27B0), Color(0xFFE040FB))
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2438)),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color(0xFF1E1A2F), Color(0xFF2A2438))
                                    )
                                )
                                .border(
                                    width = 1.dp,
                                    brush = borderBrush,
                                    shape = RoundedCornerShape(20.dp)
                                )
                        ) {
                            // Задание
                            Column(
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Flag,
                                        contentDescription = null,
                                        tint = Color(0xFF9C27B0),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "ЗАДАНИЕ",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF9C27B0),
                                        letterSpacing = 1.5.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = currentTask.description,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 26.sp
                                )
                            }

                            Divider(
                                color = Color(0xFF9C27B0).copy(alpha = 0.3f),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            // Подсказка
                            Row(
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("💡", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = currentTask.hint,
                                    fontSize = 13.sp,
                                    color = Color(0xFFB0BEC5),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Игровое поле
                    Row(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Top
                    ) {
                        DraggableFile(
                            fileType = currentTask.fileType,
                            onDragStart = { absoluteOffset ->
                                isDragging = true
                                dragPosition = absoluteOffset
                            },
                            onDrag = { absoluteOffset ->
                                dragPosition = absoluteOffset
                                var newDropTarget: String? = null
                                receiverBounds.forEach { (name, coords) ->
                                    val position = coords.positionInWindow()
                                    val size = coords.size
                                    val rect = androidx.compose.ui.geometry.Rect(
                                        position.x, position.y,
                                        position.x + size.width, position.y + size.height
                                    )
                                    if (rect.contains(absoluteOffset)) {
                                        newDropTarget = name
                                    }
                                }
                                dropTarget = newDropTarget
                            },
                            onDragEnd = {
                                dropTarget?.let { targetName ->
                                    val receiver = receiversList.find { it.name == targetName }
                                    if (receiver != null) {
                                        selectedReceiver = receiver
                                        showMethodDialog = true
                                    }
                                }
                                isDragging = false
                                dragPosition = Offset.Zero
                                dropTarget = null
                            }
                        )

                        Column(
                            modifier = Modifier.fillMaxHeight().width(180.dp).padding(end = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            receiversList.forEach { receiver ->
                                DropReceiver(
                                    receiver = receiver,
                                    isHighlighted = dropTarget == receiver.name,
                                    onCoordinatesReady = { coords ->
                                        receiverBounds = receiverBounds + (receiver.name to coords)
                                    }
                                )
                            }
                        }
                    }

                    if (animateTransfer) {
                        TransferAnimation(
                            onAnimationEnd = {
                                animateTransfer = false
                            }
                        )
                    }

                    lastMessage?.let { (msg, isError) ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
                                initialOffsetY = { it },
                                animationSpec = tween(300)
                            ),
                            exit = fadeOut(animationSpec = tween(500)) + slideOutVertically(
                                targetOffsetY = { it },
                                animationSpec = tween(500)
                            )
                        ) {
                            Card(
                                modifier = Modifier.padding(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isError) Color(0xFFB00020) else Color(0xFF4CAF50)
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(msg, modifier = Modifier.padding(12.dp), color = Color.White, fontSize = 14.sp)
                            }
                        }
                    }
                }

                // Плавающая иконка при перетаскивании
                if (isDragging && dragPosition != Offset.Zero) {
                    val density = LocalDensity.current
                    val iconSize = 80.dp
                    val iconSizePx = with(density) { iconSize.toPx() }
                    val halfSize = (iconSizePx / 2).roundToInt()
                    val verticalCorrection = (iconSizePx * 1.0).roundToInt()

                    val icon = when (currentTask.fileType) {
                        "photo" -> Icons.Default.PhotoCamera
                        "video" -> Icons.Default.VideoCameraBack
                        "contact" -> Icons.Default.Contacts
                        "apk" -> Icons.Default.Apps
                        "folder" -> Icons.Default.Folder
                        "bigfile" -> Icons.Default.Storage
                        "screen" -> Icons.Default.ScreenRotation
                        "voice" -> Icons.Default.Mic
                        else -> Icons.Default.FileCopy
                    }
                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    (dragPosition.x - halfSize).roundToInt(),
                                    (dragPosition.y - halfSize - verticalCorrection).roundToInt()
                                )
                            }
                            .size(iconSize)
                            .shadow(12.dp, CircleShape)
                            .background(Color(0xFF9C27B0).copy(alpha = 0.8f), CircleShape)
                            .padding(12.dp)
                    ) {
                        Icon(icon, contentDescription = null, modifier = Modifier.fillMaxSize(), tint = Color.White)
                    }
                }
            } else {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }

    // Диалог выбора способа передачи
    if (showMethodDialog && selectedReceiver != null) {
        val availableMethods = selectedReceiver!!.methods
        AlertDialog(
            onDismissRequest = { showMethodDialog = false },
            containerColor = Color(0xFF1E1A2F),
            titleContentColor = Color.White,
            textContentColor = Color.White,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF9C27B0))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Выберите способ передачи", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            text = {
                Column {
                    Text("Для устройства «${selectedReceiver!!.name}» подходят:", fontSize = 14.sp, color = Color(0xFFB0BEC5))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "⬇️ Листайте вниз, чтобы увидеть все способы ⬇️",
                        fontSize = 12.sp,
                        color = Color(0xFFD18AE7),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 350.dp)
                    ) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(availableMethods) { methodName ->
                                val method = methodsKnowledge.find { it.name == methodName }
                                if (method != null) {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2438)),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = methodName,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF9C27B0),
                                                    fontSize = 15.sp
                                                )
                                                Surface(
                                                    shape = RoundedCornerShape(20.dp),
                                                    color = if (method.needInternet) Color(0xFFFF9800) else Color(0xFF4CAF50)
                                                ) {
                                                    Text(
                                                        text = if (method.needInternet) "🌐 Интернет" else "📶 Офлайн",
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                                        fontSize = 10.sp,
                                                        color = Color.White
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text("⚡ ${method.speed} | 🎯 ${method.difficulty}", fontSize = 11.sp, color = Color(0xFFB0BEC5))
                                            Text("✅ Плюсы: ${method.pros}", fontSize = 11.sp, color = Color(0xFFA5D6A7))
                                            Text("❌ Минусы: ${method.cons}", fontSize = 11.sp, color = Color(0xFFEF9A9A))
                                            Button(
                                                onClick = {
                                                    val isCorrect = viewModel.checkAnswer(methodName)
                                                    if (isCorrect) {
                                                        vibrate(context)
                                                        coroutineScope.launch {
                                                            animateTransfer = true
                                                            delay(800)
                                                        }
                                                    }
                                                    showMethodDialog = false
                                                    selectedReceiver = null
                                                },
                                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Text("Выбрать ${methodName}", color = Color.White, fontSize = 12.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp)
                                .align(Alignment.BottomCenter)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color(0xFF1E1A2F))
                                    )
                                )
                        )
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showMethodDialog = false; selectedReceiver = null }) {
                    Text("Отмена", color = Color(0xFFB0BEC5))
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }

    // Инструкция при первом запуске
    if (showIntroDialog) {
        AlertDialog(
            onDismissRequest = {
                showIntroDialog = false
                viewModel.setIntroShown()
            },
            containerColor = Color(0xFF1E1A2F),
            titleContentColor = Color.White,
            textContentColor = Color.White,
            title = { Text("🎮 Как играть в Обменник PRO", fontWeight = FontWeight.Bold, color = Color(0xFF9C27B0)) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text("• Перетащите файл на любое устройство справа.", color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Выберите способ передачи – для каждого показаны плюсы, минусы.", color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Если способ правильный – +10 очков и новое задание.", color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Если нет – читайте подсказку и пробуйте снова.", color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Вы можете выбрать любой способ, даже если он не рекомендуется – игра проверит правильность по заданию!", fontWeight = FontWeight.Bold, color = Color(0xFF9C27B0))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("💡 Совет: используйте справку (?), чтобы детально изучить все способы.", color = Color(0xFFB0BEC5))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showIntroDialog = false
                        viewModel.setIntroShown()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Понятно, начать!", color = Color.White)
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
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
        "apk" -> Icons.Default.Apps
        "folder" -> Icons.Default.Folder
        "bigfile" -> Icons.Default.Storage
        "screen" -> Icons.Default.ScreenRotation
        "voice" -> Icons.Default.Mic
        else -> Icons.Default.FileCopy
    }

    var viewPosition by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .size(100.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.horizontalGradient(listOf(Color(0xFF12101F), Color(0xFF1E1A2F))))
            .onGloballyPositioned { coordinates ->
                viewPosition = coordinates.positionInWindow()
            }
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
                    onDragEnd = {
                        onDragEnd()
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF9C27B0))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Файл: $fileType", color = Color.White, fontSize = 12.sp)
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
            .onGloballyPositioned { coordinates ->
                onCoordinatesReady(coordinates)
            },
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlighted) Color(0xFF9C27B0).copy(alpha = 0.3f) else Color(0xFF2A2438)
        ),
        elevation = CardDefaults.cardElevation(if (isHighlighted) 12.dp else 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                receiver.icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = if (isHighlighted) Color(0xFF9C27B0) else Color(0xFF9C27B0).copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(receiver.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                Text(
                    "Любой способ",
                    fontSize = 10.sp,
                    color = Color.LightGray,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun TransferAnimation(onAnimationEnd: () -> Unit) {
    var progress by remember { mutableFloatStateOf(0f) }
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(Unit) {
        progress = 1f
        delay(600)
        onAnimationEnd()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = alpha * 0.5f))
    ) {
        Text(
            text = "📡 Передача...",
            modifier = Modifier.align(Alignment.Center),
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
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
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.EmojiEvents,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = Color(0xFFFFD700)
                )
                Text("Сертификат", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9C27B0))
                Text("Мастер передачи данных", fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Вы успешно прошли все 10 заданий!", color = Color.LightGray)
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onRestart,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Вернуться в меню")
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