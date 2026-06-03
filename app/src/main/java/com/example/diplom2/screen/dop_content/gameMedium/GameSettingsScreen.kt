package com.example.diplom2.screen.dop_content.gameMedium

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
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
import com.example.diplom2.screen.AchievementManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlin.math.roundToInt

// ------------------------------- Модели данных -------------------------------
data class MenuItem(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val description: String = "",
    val children: List<MenuItem> = emptyList(),
    val parentId: String? = null
)

data class SettingsTask(
    val id: Int,
    val description: String,
    val pathIds: List<String>,
    val hint: String,
    val bonusTip: String? = null
)

// ------------------------------- Данные меню и заданий -------------------------------
val rootMenuItems = listOf(
    MenuItem("basics", "Основы", Icons.Default.Settings, "Здесь настраивается язык, время и память телефона."),
    MenuItem("security", "Безопасность", Icons.Default.Security, "Защита телефона: пароли, отпечатки и тихий режим."),
    MenuItem("display_sound", "Экран и звук", Icons.Default.DisplaySettings, "Яркость экрана, обои и громкость звонка."),
    MenuItem("apps", "Приложения", Icons.Default.Apps, "Управление установленными программами и их уведомлениями."),
    MenuItem("hidden", "Скрытые возможности", Icons.Default.MoreVert, "Настройки для продвинутых пользователей.")
)

val subMenuItems = mapOf(
    "basics" to listOf(
        MenuItem("about_phone", "О телефоне", Icons.Default.Info, "Модель, версия системы и память.", parentId = "basics"),
        MenuItem("language", "Язык и ввод", Icons.Default.Language, "Смена языка и настройка клавиатуры.", parentId = "basics"),
        MenuItem("date_time", "Дата и время", Icons.Default.Schedule, "Автоматическая или ручная настройка времени.", parentId = "basics"),
        MenuItem("storage", "Хранилище", Icons.Default.Storage, "Сколько места занято и что можно удалить.", parentId = "basics"),
        MenuItem("reset", "Сброс настроек", Icons.Default.Restore, "Возврат телефона к заводским настройкам.", parentId = "basics")
    ),
    "security" to listOf(
        MenuItem("screen_lock", "Блокировка экрана", Icons.Default.Lock, "Пароль, графический ключ или пин-код.", parentId = "security"),
        MenuItem("fingerprint", "Сканер отпечатков", Icons.Default.Fingerprint, "Разблокировка телефона пальцем.", parentId = "security"),
        MenuItem("dnd", "Не беспокоить", Icons.Default.VolumeOff, "Отключение всех звуков и вибрации.", parentId = "security"),
        MenuItem("app_permissions", "Разрешения приложений", Icons.Default.Shield, "Какие программы могут использовать камеру или микрофон.", parentId = "security"),
        MenuItem("find_device", "Найти устройство", Icons.Default.LocationOn, "Поиск телефона, если он потерялся.", parentId = "security")
    ),
    "display_sound" to listOf(
        MenuItem("brightness", "Яркость", Icons.Default.BrightnessMedium, "Насколько ярко светит экран.", parentId = "display_sound"),
        MenuItem("wallpaper", "Обои", Icons.Default.Wallpaper, "Картинка на главном экране.", parentId = "display_sound"),
        MenuItem("refresh_rate", "Частота обновления", Icons.Default.Speed, "Плавность картинки на экране.", parentId = "display_sound"),
        MenuItem("ringtone", "Рингтон", Icons.Default.MusicNote, "Мелодия входящего звонка.", parentId = "display_sound"),
        MenuItem("vibration", "Вибрация", Icons.Default.Vibration, "Будет ли телефон вибрировать при звонке.", parentId = "display_sound")
    ),
    "apps" to listOf(
        MenuItem("default_apps", "Приложения по умолчанию", Icons.Default.Apps, "Какая программа открывает ссылки или почту.", parentId = "apps"),
        MenuItem("app_notifications", "Уведомления приложений", Icons.Default.Notifications, "Запрет надоедливых сообщений от программ.", parentId = "apps"),
        MenuItem("clear_cache", "Очистка кэша", Icons.Default.CleaningServices, "Удаление временных файлов для освобождения места.", parentId = "apps"),
        MenuItem("install_unknown", "Установка из неизвестных источников", Icons.Default.Download, "Разрешение на установку программ не из магазина.", parentId = "apps"),
        MenuItem("app_usage", "Использование приложений", Icons.Default.Analytics, "Статистика: сколько времени вы тратите на программы.", parentId = "apps")
    ),
    "hidden" to listOf(
        MenuItem("developer_options", "Для разработчиков", Icons.Default.Code, "Специальные настройки для программистов.", parentId = "hidden"),
        MenuItem("demo_mode", "Демо-режим", Icons.Default.TheaterComedy, "Режим витрины: скрывает личные данные на экране.", parentId = "hidden"),
        MenuItem("gpu_rendering", "Профилирование GPU", Icons.Default.Speed, "Графики производительности экрана.", parentId = "hidden"),
        MenuItem("force_rtl", "Принудительное RTL", Icons.Default.FormatTextdirectionRToL, "Чтение текста справа налево.", parentId = "hidden"),
        MenuItem("animation_speed", "Скорость анимации", Icons.Default.Animation, "Ускорение или замедление переходов в меню.", parentId = "hidden")
    )
)

val allMenuItems = rootMenuItems + subMenuItems.values.flatten()

val settingsTasks = listOf(
    SettingsTask(1, "Включите режим «Не беспокоить»", listOf("security", "dnd"), "Этот режим отключает звуки и вибрацию. Ищите в разделе безопасности.", "В режиме «Не беспокоить» можно добавить исключения для важных контактов."),
    SettingsTask(2, "Установите частоту обновления экрана 90 Гц", listOf("display_sound", "refresh_rate"), "Настройки экрана и звука. Параметр отвечает за плавность картинки.", "Высокая частота обновления делает интерфейс более плавным, но увеличивает расход батареи."),
    SettingsTask(3, "Очистите кэш приложений", listOf("apps", "clear_cache"), "В разделе приложений есть возможность освободить место без удаления данных.", "Очистка кэша освобождает место без потери личных данных."),
    SettingsTask(4, "Активируйте сканер отпечатков пальцев", listOf("security", "fingerprint"), "Биометрическая защита. Находится в настройках безопасности.", "Отпечаток пальца – быстрый и безопасный способ разблокировки."),
    SettingsTask(5, "Включите демо-режим (скрытые возможности)", listOf("hidden", "demo_mode"), "Скрытые возможности – там, где обычно не заглядывают. Режим для витрин.", "Демо-режим показывает интерфейс как в магазине, полезно для презентаций."),
    SettingsTask(6, "Настройте автоматическую синхронизацию даты и времени", listOf("basics", "date_time"), "Основные настройки. Позволяет не думать о точном времени.", "Автосинхронизация гарантирует точное время без ручной настройки."),
    SettingsTask(7, "Разрешите установку приложений из неизвестных источников", listOf("apps", "install_unknown"), "В разделе приложений есть опция, разрешающая установку не из магазина.", "Будьте осторожны: это может повысить риск вредоносного ПО."),
    SettingsTask(8, "Смените обои", listOf("display_sound", "wallpaper"), "Настройки экрана и звука. Там можно выбрать картинку для рабочего стола.", "Вы можете выбрать готовые обои или установить своё фото."),
    SettingsTask(9, "Проверьте использование приложений", listOf("apps", "app_usage"), "Раздел приложений. Позволяет узнать, что и сколько работает.", "Статистика покажет, какие приложения тратят больше всего времени и батареи."),
    SettingsTask(10, "Активируйте режим «Для разработчиков»", listOf("hidden", "developer_options"), "Скрытые возможности. Нужно нажать несколько раз на определённый пункт в настройках.", "Режим открывает расширенные настройки, но требует осторожности.")
)

// ------------------------------- Модели для туториала (с уникальными именами) -------------------------------
data class SettingsTutorialStep(
    val targetKey: String,
    val title: String,
    val text: String,
    val icon: ImageVector
)

val settingsTutorialSteps = listOf(
    TutorialStep(
        "task_card",
        "Задание",
        "Здесь написано, что именно нужно найти. Читайте внимательно!",
        Icons.Default.Flag
    ),
    TutorialStep(
        "hint_button",
        "Подсказка",
        "Если не знаете, что делать, нажмите на лампочку вверху для подсказки!",
        Icons.Default.Lightbulb
    ),
    TutorialStep(
        "search_button",
        "Поиск",
        "Не знаете, где искать? Нажмите на лупу и введите название настройки!",
        Icons.Default.Search
    ),
    TutorialStep(
        "reset_button",
        "Перезапуск игры",
        "Эта кнопка начинает игру заново. Сбрасывает очки и прогресс.",
        Icons.Default.Refresh
    ),
    TutorialStep(
        "menu_area",
        "Меню настроек",
        "Нажимайте на разделы, чтобы зайти внутрь и найти нужную настройку.",
        Icons.Default.List
    ),
    TutorialStep(
        "check_button",
        "Проверить путь",
        "Когда вы собрали правильный путь, нажмите эту кнопку, чтобы получить очки.",
        Icons.Default.CheckCircle
    )
)

// ------------------------------- ViewModel -------------------------------
class SettingsViewModel(context: Context) : ViewModel() {
    private val prefs = context.getSharedPreferences("settings_game", Context.MODE_PRIVATE)
    private val _currentTaskIndex = MutableStateFlow(prefs.getInt("current_task", 0))
    val currentTaskIndex: StateFlow<Int> = _currentTaskIndex.asStateFlow()

    private val _score = MutableStateFlow(prefs.getInt("score", 0))
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _completedTasks = MutableStateFlow(prefs.getInt("completed_tasks", 0))
    val completedTasks: StateFlow<Int> = _completedTasks.asStateFlow()

    private val _showBonusTip = MutableStateFlow<String?>(null)
    val showBonusTip: StateFlow<String?> = _showBonusTip.asStateFlow()

    private val _lastMessage = MutableStateFlow<Pair<String, Boolean>?>(null)
    val lastMessage: StateFlow<Pair<String, Boolean>?> = _lastMessage.asStateFlow()

    private val _pathHistory = MutableStateFlow<List<MenuItem>>(emptyList())
    val pathHistory: StateFlow<List<MenuItem>> = _pathHistory.asStateFlow()

    private val _introShown = MutableStateFlow(prefs.getBoolean("intro_shown", false))
    val introShown: StateFlow<Boolean> = _introShown.asStateFlow()

    fun navigateTo(item: MenuItem) {
        val currentPath = _pathHistory.value.toMutableList()
        if (currentPath.isNotEmpty() && currentPath.last().id == item.id) return
        _pathHistory.value = currentPath + item
    }

    fun resetPath() {
        _pathHistory.value = emptyList()
    }

    fun checkAnswer(): Boolean {
        val task = settingsTasks[_currentTaskIndex.value]
        val currentPathIds = _pathHistory.value.map { it.id }
        val isCorrect = currentPathIds == task.pathIds
        if (isCorrect) {
            val newScore = _score.value + 10
            _score.value = newScore
            val newCompleted = _completedTasks.value + 1
            _completedTasks.value = newCompleted

            if ((newCompleted % 5 == 0) && task.bonusTip != null) {
                _showBonusTip.value = task.bonusTip
            }

            if (_currentTaskIndex.value + 1 < settingsTasks.size) {
                _currentTaskIndex.value += 1
                resetPath()
            } else {
                _showBonusTip.value = "Поздравляем! Вы прошли все задания!"
            }
            saveProgress()
        } else {
            _lastMessage.value = Pair("❌ Неправильный путь. Попробуйте снова.", true)
        }
        return isCorrect
    }

    fun showHint() {
        val task = settingsTasks[_currentTaskIndex.value]
        _lastMessage.value = Pair("💡 Подсказка: ${task.hint}", false)
    }

    fun resetGame() {
        _currentTaskIndex.value = 0
        _score.value = 0
        _completedTasks.value = 0
        _showBonusTip.value = null
        _lastMessage.value = null
        resetPath()
        saveProgress()
    }

    fun setIntroShown() {
        _introShown.value = true
        prefs.edit().putBoolean("intro_shown", true).apply()
    }

    fun clearMessage() {
        _lastMessage.value = null
    }

    fun clearBonusTip() {
        _showBonusTip.value = null
    }

    private fun saveProgress() {
        prefs.edit()
            .putInt("current_task", _currentTaskIndex.value)
            .putInt("score", _score.value)
            .putInt("completed_tasks", _completedTasks.value)
            .putBoolean("intro_shown", _introShown.value)
            .apply()
    }
}

class SettingsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// ------------------------------- UI -------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSettingsScreen(navController: NavController, userId: Long) {
    val context = LocalContext.current
    val viewModel: SettingsViewModel = viewModel(factory = SettingsViewModelFactory(context))
    val currentTaskIndex by viewModel.currentTaskIndex.collectAsStateWithLifecycle()
    val score by viewModel.score.collectAsStateWithLifecycle()
    val completedTasks by viewModel.completedTasks.collectAsStateWithLifecycle()
    val lastMessage by viewModel.lastMessage.collectAsStateWithLifecycle()
    val showBonusTip by viewModel.showBonusTip.collectAsStateWithLifecycle()
    val pathHistory by viewModel.pathHistory.collectAsStateWithLifecycle()
    val currentTask = if (currentTaskIndex < settingsTasks.size) settingsTasks[currentTaskIndex] else null
    val isGameFinished = completedTasks >= settingsTasks.size
    var showSearchDialog by remember { mutableStateOf(false) }
    var showIntroDialog by remember { mutableStateOf(true) }

    var tutorialActive by remember { mutableStateOf(false) }
    var currentTutorialStep by remember { mutableIntStateOf(0) }
    val elementBounds = remember { mutableStateMapOf<String, Rect>() }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(lastMessage) {
        if (lastMessage != null) {
            delay(5000)
            viewModel.clearMessage()
        }
    }

    LaunchedEffect(showBonusTip) {
        if (showBonusTip != null) {
            delay(5000)
            viewModel.clearBonusTip()
        }
    }

    LaunchedEffect(tutorialActive) {
        if (tutorialActive) delay(600)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Тайны настроек", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = Color(0xFFE040FB))
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.showHint() },
                        modifier = Modifier.onGloballyPositioned { coords ->
                            elementBounds["hint_button"] = coords.boundsInWindow()
                        }
                    ) {
                        Icon(Icons.Default.Lightbulb, contentDescription = "Подсказка", tint = Color(0xFFE040FB))
                    }
                    IconButton(
                        onClick = { showSearchDialog = true },
                        modifier = Modifier.onGloballyPositioned { coords ->
                            elementBounds["search_button"] = coords.boundsInWindow()
                        }
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Поиск", tint = Color(0xFFE040FB))
                    }
                    IconButton(
                        onClick = { viewModel.resetGame() },
                        modifier = Modifier.onGloballyPositioned { coords ->
                            elementBounds["reset_button"] = coords.boundsInWindow() // ✅ Теперь сохраняет координаты
                        }
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Сброс", tint = Color(0xFFE040FB))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E1A2F),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding).background(Color(0xFF1E1A2F))) {
            if (isGameFinished) {
                LaunchedEffect(Unit) {
                    val prefs = context.getSharedPreferences("game_medium_$userId", Context.MODE_PRIVATE)
                    prefs.edit().putBoolean("settings_game_completed", true).apply()
                    AchievementManager.init(context)
                    val achievements = AchievementManager.getAllAchievements()
                    val settingsAchievement = achievements.find { it.id == "settings_expert" }
                    settingsAchievement?.let {
                        AchievementManager.checkAndUnlock(context, userId, it)
                    }
                }
                SettingsCertificateScreen(onRestart = { viewModel.resetGame(); navController.popBackStack() })
            } else if (currentTask != null) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2438)),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("$score", color = Color(0xFFFFD700), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                            Surface(shape = RoundedCornerShape(30.dp), color = Color(0xFF9C27B0), modifier = Modifier.padding(horizontal = 8.dp)) {
                                Text(
                                    text = "${currentTaskIndex + 1}/${settingsTasks.size}",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .onGloballyPositioned { coords ->
                                elementBounds["task_card"] = coords.boundsInWindow()
                            },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2438)),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "📋 ЗАДАНИЕ:",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE1BEE7)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(currentTask.description, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White, lineHeight = 22.sp)
                        }
                    }

                    if (pathHistory.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2438)),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    pathHistory.forEachIndexed { index, item ->
                                        Text(
                                            item.name,
                                            color = Color(0xFF9C27B0),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.clickable {
                                                val newPath = pathHistory.take(index + 1)
                                                viewModel.resetPath()
                                                newPath.forEach { viewModel.navigateTo(it) }
                                            }
                                        )
                                        if (index < pathHistory.size - 1) {
                                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                                        }
                                    }
                                }
                                IconButton(onClick = { viewModel.resetPath() }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Сбросить путь", tint = Color(0xFF9C27B0), modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .onGloballyPositioned { coords ->
                                elementBounds["menu_area"] = coords.boundsInWindow()
                            }
                    ) {
                        SettingsMenuTree(
                            currentPath = pathHistory,
                            onItemClick = { item -> viewModel.navigateTo(item) }
                        )
                    }

                    lastMessage?.let { (msg, isError) ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + slideInVertically(),
                            exit = fadeOut() + slideOutVertically()
                        ) {
                            Card(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                colors = CardDefaults.cardColors(containerColor = if (isError) Color(0xFFB00020) else Color(0xFF4CAF50)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(msg, modifier = Modifier.padding(12.dp), color = Color.White, fontSize = 14.sp)
                            }
                        }
                    }

                    showBonusTip?.let { tip ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + scaleIn(),
                            exit = fadeOut() + scaleOut()
                        ) {
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFF9800).copy(alpha = 0.9f)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(tip, color = Color.White, fontSize = 13.sp, modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }

                    if (pathHistory.isNotEmpty()) {
                        Button(
                            onClick = { viewModel.checkAnswer() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp, vertical = 16.dp)
                                .onGloballyPositioned { coords ->
                                    elementBounds["check_button"] = coords.boundsInWindow()
                                },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text("Проверить путь", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            } else {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        if (tutorialActive && currentTutorialStep < settingsTutorialSteps.size) {
            SettingsTutorialOverlay(
                steps = settingsTutorialSteps,
                currentStep = currentTutorialStep,
                elementBounds = elementBounds,
                onNext = {
                    if (currentTutorialStep + 1 < settingsTutorialSteps.size) {
                        currentTutorialStep += 1
                    } else {
                        tutorialActive = false
                    }
                },
                onSkip = { tutorialActive = false }
            )
        }
    }

    if (showSearchDialog) {
        var query by remember { mutableStateOf("") }
        val filteredItems = if (query.isBlank()) emptyList()
        else allMenuItems.filter { it.name.contains(query, ignoreCase = true) }

        AlertDialog(
            onDismissRequest = { showSearchDialog = false },
            containerColor = Color(0xFF1E1A2F),
            titleContentColor = Color.White,
            textContentColor = Color.White,
            title = { Text("🔎 Поиск настройки", color = Color(0xFF9C27B0), fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color(0xFF2A2438),
                            unfocusedContainerColor = Color(0xFF2A2438),
                            cursorColor = Color(0xFF9C27B0),
                            focusedBorderColor = Color(0xFF9C27B0),
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color(0xFF9C27B0),
                            unfocusedLabelColor = Color.LightGray,
                            focusedPlaceholderColor = Color.LightGray,
                            unfocusedPlaceholderColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(16.dp),
                        placeholder = { Text("Введите название", color = Color.LightGray) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    if (filteredItems.isEmpty() && query.isNotBlank()) {
                        Text("❌ Ничего не найдено", color = Color(0xFFB0BEC5), modifier = Modifier.padding(8.dp))
                    } else {
                        LazyColumn(modifier = Modifier.heightIn(max = 300.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(filteredItems) { item ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().clickable {
                                        val fullPath = buildPathToMenuItem(item.id)
                                        if (fullPath.isNotEmpty()) {
                                            viewModel.resetPath()
                                            fullPath.forEach { menuItem -> viewModel.navigateTo(menuItem) }
                                        }
                                        query = ""
                                        showSearchDialog = false
                                    },
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2438)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(item.icon, contentDescription = null, tint = Color(0xFF9C27B0), modifier = Modifier.size(24.dp))
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(item.name, color = Color.White, fontWeight = FontWeight.Medium)
                                            Text(
                                                text = item.parentId?.let { parent -> allMenuItems.find { it.id == parent }?.name ?: "Корень" } ?: "Корень",
                                                fontSize = 11.sp,
                                                color = Color(0xFFB0BEC5)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSearchDialog = false }) {
                    Text("Закрыть", color = Color(0xFF9C27B0))
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }

    if (showIntroDialog) {
        AlertDialog(
            onDismissRequest = { },
            containerColor = Color(0xFF1E1A2F),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFE0E0E0),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.School, contentDescription = null, tint = Color(0xFF9C27B0), modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Как играть", fontWeight = FontWeight.Bold, color = Color(0xFFE1BEE7), fontSize = 20.sp)
                }
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text("• Нажимайте на пункты меню, чтобы построить путь к нужной настройке.", color = Color(0xFFE0E0E0), fontSize = 16.sp, lineHeight = 22.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("• Правильный путь – это последовательность: раздел → подраздел → искомая настройка.", color = Color(0xFFE0E0E0), fontSize = 16.sp, lineHeight = 22.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("• Когда путь собран, нажмите «Проверить путь» внизу экрана.", color = Color(0xFFE0E0E0), fontSize = 16.sp, lineHeight = 22.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("• За каждый правильный ответ вы получаете +10 очков.", color = Color(0xFFE0E0E0), fontSize = 16.sp, lineHeight = 22.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF9C27B0).copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF90CAF9), modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Не знаете, где искать настройку? Нажмите на лупу 🔍 вверху экрана и просто введите её название!",
                                color = Color(0xFF90CAF9),
                                fontSize = 15.sp,
                                lineHeight = 21.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Сейчас мы проведем небольшую экскурсию по экрану, чтобы показать, где что находится.",
                        color = Color(0xFFB0BEC5),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showIntroDialog = false
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

// ------------------------------- Вспомогательные функции -------------------------------
fun buildPathToMenuItem(targetId: String): List<MenuItem> {
    val target = allMenuItems.find { it.id == targetId } ?: return emptyList()
    val path = mutableListOf<MenuItem>()
    var current: MenuItem? = target
    while (current != null) {
        path.add(0, current)
        val parentId = current.parentId
        current = if (parentId != null) allMenuItems.find { it.id == parentId } else null
    }
    return path
}

@Composable
fun SettingsMenuTree(currentPath: List<MenuItem>, onItemClick: (MenuItem) -> Unit) {
    val listState = rememberLazyListState()
    val currentLevelItems: List<MenuItem> = if (currentPath.isEmpty()) {
        rootMenuItems
    } else {
        val lastItem = currentPath.last()
        subMenuItems[lastItem.id] ?: emptyList()
    }
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(currentLevelItems, key = { it.id }) { item ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(300)) + slideInHorizontally()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { onItemClick(item) },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2438)),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(item.icon, contentDescription = null, tint = Color(0xFF9C27B0), modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(item.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.weight(1f))
                            if (subMenuItems.containsKey(item.id)) {
                                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
                            }
                        }
                        if (item.description.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                item.description,
                                color = Color(0xFFB0BEC5),
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                modifier = Modifier.padding(start = 40.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsCertificateScreen(onRestart: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.padding(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2438)),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.EmojiEvents, contentDescription = null, modifier = Modifier.size(80.dp), tint = Color(0xFFFFD700))
                Text("Сертификат", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9C27B0))
                Text("Мастер настроек", fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Вы успешно прошли все задания!", color = Color.LightGray, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onRestart, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), shape = RoundedCornerShape(12.dp)) {
                    Text("Вернуться в меню", color = Color.White)
                }
            }
        }
    }
}

// ==================== КОМПОНЕНТЫ ТУТОРИАЛА (УНИКАЛЬНЫЕ ИМЕНА) ====================
@Composable
fun SettingsTutorialOverlay(
    steps: List<TutorialStep>, // ✅ ИСПРАВЛЕНО: было List<SettingsTutorialStep>
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(100f)
            .background(Color.Black.copy(alpha = 0.75f))
            .clickable(enabled = false) {}
    ) {
        if (targetBounds != null) {
            val paddingPx = with(density) { 8.dp.toPx() }
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
                        width = 3.dp,
                        brush = Brush.horizontalGradient(listOf(Color(0xFF9C27B0), Color(0xFFE040FB))),
                        shape = RoundedCornerShape(12.dp)
                    )
            )
        }

        SettingsCoachMarkCard(
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

@Composable
fun SettingsCoachMarkCard(
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
    val cardWidthPx = with(density) { 280.dp.toPx() }
    val cardHeightPx = with(density) { 120.dp.toPx() }
    val arrowSizePx = with(density) { 12.dp.toPx() }
    val gapPx = with(density) { 12.dp.toPx() }
    val showAbove: Boolean
    val xOffsetPx: Float
    val yOffsetPx: Float

    if (targetBounds != null) {
        val targetCenterX = (targetBounds.left + targetBounds.right) / 2
        val targetCenterY = (targetBounds.top + targetBounds.bottom) / 2

        xOffsetPx = (targetCenterX - cardWidthPx / 2).coerceIn(16f, screenWidthPx - cardWidthPx - 16f)

        val spaceAbove = targetBounds.top
        val spaceBelow = screenHeightPx - targetBounds.bottom

        if (spaceBelow >= cardHeightPx + arrowSizePx + gapPx + 16f) {
            showAbove = false
            yOffsetPx = targetBounds.bottom + gapPx + arrowSizePx
        } else if (spaceAbove >= cardHeightPx + arrowSizePx + gapPx + 16f) {
            showAbove = true
            yOffsetPx = targetBounds.top - gapPx - arrowSizePx - cardHeightPx
        } else {
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

    val arrowXOffsetPx = if (targetBounds != null) {
        val targetCenterX = (targetBounds.left + targetBounds.right) / 2
        (targetCenterX - xOffsetPx).coerceIn(24f, cardWidthPx - 24f)
    } else {
        cardWidthPx / 2
    }

    Box(
        modifier = Modifier.offset { IntOffset(xOffsetPx.roundToInt(), yOffsetPx.roundToInt()) }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (!showAbove) {
                Box(
                    modifier = Modifier
                        .offset { IntOffset(arrowXOffsetPx.roundToInt() - arrowSizePx.roundToInt(), 0) }
                        .size(0.dp)
                        .drawSettingsArrowDown(arrowSizePx, Color(0xFF2A2438))
                )
                Spacer(modifier = Modifier.height(arrowSizePx.dp))
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2438)),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(12.dp),
                modifier = Modifier.width(with(density) { cardWidthPx.toDp() })
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(step.icon, contentDescription = null, tint = Color(0xFFE040FB), modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(step.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.weight(1f))
                        Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFF9C27B0)) {
                            Text(
                                "${stepIndex + 1}/$totalSteps",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(step.text, fontSize = 14.sp, color = Color(0xFFE0E0E0), lineHeight = 20.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = onSkip, contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp), modifier = Modifier.height(32.dp)) {
                            Text("Пропустить", color = Color(0xFF90A4AE), fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Button(
                            onClick = onNext,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(if (stepIndex + 1 < totalSteps) "Далее →" else "Начать!", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            if (showAbove) {
                Spacer(modifier = Modifier.height(arrowSizePx.dp))
                Box(
                    modifier = Modifier
                        .offset { IntOffset(arrowXOffsetPx.roundToInt() - arrowSizePx.roundToInt(), 0) }
                        .size(0.dp)
                        .drawSettingsArrowUp(arrowSizePx, Color(0xFF2A2438))
                )
            }
        }
    }
}

fun Modifier.drawSettingsArrowDown(sizePx: Float, color: Color): Modifier = this.then(
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

fun Modifier.drawSettingsArrowUp(sizePx: Float, color: Color): Modifier = this.then(
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