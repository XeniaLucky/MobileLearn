package com.example.diplom2.screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import bd.AppDatabase
import kotlinx.coroutines.launch
import repository.GameRepository
import table.Game
import androidx.compose.runtime.rememberCoroutineScope
import com.example.diplom2.R
import com.example.diplom2.screen.dop_content.*
import com.example.diplom2.screen.dop_content.lessons_medium.*

// Активный урок (Medium)
fun setActiveLessonMedium(context: Context, userId: Long, lessonKey: String) {
    val p = context.getSharedPreferences("progress_medium_$userId", Context.MODE_PRIVATE)
    p.edit().putString("active_lesson_key", lessonKey).apply()
    p.edit().putFloat("active_lesson_progress", 0f).apply()
}

fun updateLessonProgressMedium(context: Context, userId: Long, progress: Float) {
    val p = context.getSharedPreferences("progress_medium_$userId", Context.MODE_PRIVATE)
    p.edit().putFloat("active_lesson_progress", progress.coerceIn(0f, 1f)).apply()
}

fun getActiveLessonMedium(context: Context, userId: Long): String? {
    val p = context.getSharedPreferences("progress_medium_$userId", Context.MODE_PRIVATE)
    return p.getString("active_lesson_key", null)
}

fun getActiveProgressMedium(context: Context, userId: Long): Float {
    val p = context.getSharedPreferences("progress_medium_$userId", Context.MODE_PRIVATE)
    return p.getFloat("active_lesson_progress", 0f)
}
fun saveLastStepMedium(context: Context, userId: Long, lessonKey: String, step: Int) {
    val p = context.getSharedPreferences("progress_medium_$userId", Context.MODE_PRIVATE)
    p.edit().putInt("last_step_$lessonKey", step).apply()
}

fun getLastStepMedium(context: Context, userId: Long, lessonKey: String): Int {
    val p = context.getSharedPreferences("progress_medium_$userId", Context.MODE_PRIVATE)
    return p.getInt("last_step_$lessonKey", 0)
}

fun activateLessonMedium(context: Context, userId: Long, lessonKey: String) {
    val p = context.getSharedPreferences("progress_medium_$userId", Context.MODE_PRIVATE)
    val currentActive = p.getString("active_lesson_key", null)
    if (currentActive != lessonKey) {
        p.edit()
            .putString("active_lesson_key", lessonKey)
            .putFloat("active_lesson_progress", 0f)
            .putInt("last_step_$lessonKey", 0)
            .apply()
    }
}
// Вспомогательные классы
data class MediumLessonItem(val title: String, val icon: ImageVector, val route: String, val progressKey: String)
data class FaqItem1(val question: String, val answer: String)
data class LessonProgress(val title: String, var completed: Boolean = false, var currentPage: Int = 0, val totalPages: Int = 3)
data class Achievement(val title: String, val description: String, val icon: String, val isUnlocked: () -> Boolean)

// Функция сохранения прогресса (не composable)
fun saveLessonProgress(context: Context, userId: Long, lessonKey: String, completed: Boolean) {
    val prefs = context.getSharedPreferences("progress_medium_$userId", Context.MODE_PRIVATE)
    prefs.edit().putBoolean("${lessonKey}_completed", completed).apply()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediumScreen(userId: Long) {
    val navController = rememberNavController()
    val backgroundColor = Color(0xFFC4D7DB)
    val accentColor = Color(0xFF2C5F6E)

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White.copy(alpha = 0.95f),
                tonalElevation = 0.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.School, contentDescription = "Курсы") },
                    label = { Text("Курсы") },
                    selected = navController.currentDestination?.route == "home",
                    onClick = { navController.navigate("home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = accentColor,
                        selectedTextColor = accentColor,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Games, contentDescription = "Игры") },
                    label = { Text("Игры") },
                    selected = navController.currentDestination?.route == "games",
                    onClick = { navController.navigate("games") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = accentColor,
                        selectedTextColor = accentColor,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Награды") },
                    label = { Text("Награды") },
                    selected = navController.currentDestination?.route == "rewards",
                    onClick = { navController.navigate("rewards") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = accentColor,
                        selectedTextColor = accentColor,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Профиль") },
                    label = { Text("Профиль") },
                    selected = navController.currentDestination?.route == "profile",
                    onClick = { navController.navigate("profile") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = accentColor,
                        selectedTextColor = accentColor,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {
                    MediumHomeScreen(userId = userId, accentColor = accentColor, navController = navController)
                }
                composable("games") {
                    MediumGamesScreen(navController = navController, userId = userId, accentColor = accentColor)
                }
                composable("rewards") {
                    MediumRewardsScreen(accentColor = accentColor, userId = userId)
                }
                composable("profile") {
                    MediumProfileScreen(userId = userId, accentColor = accentColor)
                }
                composable("family_plan") {
                    MediumFamilyPlanScreen(userId = userId, accentColor = accentColor)
                }
                // Бесплатные игры
                composable("game_taprun") {
                    TapRunGame(navController = navController, onBack = { navController.popBackStack() })
                }
                composable("game_swipequiz") {
                    SwipeQuizGame(navController = navController, onBack = { navController.popBackStack() })
                }
                composable("game_settingspuzzle") {
                    SettingsPuzzleGame(navController = navController, onBack = { navController.popBackStack() })
                }
                // Платные игры
                composable("premium_game_pro_photographer") {
                    ProPhotographerGame(navController = navController, onBack = { navController.popBackStack() })
                }
                composable("premium_game_cyber_detective") {
                    CyberDetectiveGame(navController = navController, onBack = { navController.popBackStack() })
                }
                composable("premium_game_gestures") {
                    GesturesGame(navController = navController, onBack = { navController.popBackStack() })
                }
                // НОВЫЕ УРОКИ (6 основных)
                composable("game_notifications") { LessonNotificationsScreen(navController, userId) }
                composable("game_memory") { LessonMemoryCleanScreen(navController, userId) }
                composable("game_battery") { LessonBatterySaverScreen(navController, userId) }
                composable("game_safeapps") { LessonSafeAppsScreen(navController, userId) }
                composable("game_datatransfer") { LessonDataTransferScreen(navController, userId) }
                composable("game_recovery") { LessonPasswordRecoveryScreen(navController, userId) }
                // Дополнительные уроки (для кнопки "Ещё уроки")
                composable("game_wifidiag") { LessonWifiDiagnosticScreen(navController, userId) }
                composable("game_digitalwellbeing") { LessonDigitalWellbeingScreen(navController, userId) }
                // Экран "Ещё уроки" (список дополнительных)
                composable("extra_lessons") { ExtraLessonsScreen(navController, userId, accentColor) }
                // Экран FAQ
                composable("faq") { FaqScreen(navController, accentColor) }
            }
        }
    }
}

// ---------- ГЛАВНЫЙ ЭКРАН (6 новых уроков) ----------
@Composable
fun MediumHomeScreen(userId: Long, accentColor: Color, navController: NavController) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("progress_medium_$userId", Context.MODE_PRIVATE)

    val mainLessons = listOf(
        MediumLessonItem("Управление уведомлениями", Icons.Default.Notifications, "game_notifications", "game_notifications"),
        MediumLessonItem("Оптимизация памяти", Icons.Default.Memory, "game_memory", "game_memory"),
        MediumLessonItem("Экономия батареи", Icons.Default.BatteryFull, "game_battery", "game_battery"),
        MediumLessonItem("Безопасность приложений", Icons.Default.Security, "game_safeapps", "game_safeapps"),
        MediumLessonItem("Перенос данных", Icons.Default.Sync, "game_datatransfer", "game_datatransfer"),
        MediumLessonItem("Восстановление пароля", Icons.Default.LockReset, "game_recovery", "game_recovery")
    )

    val activeLessonKey = getActiveLessonMedium(context, userId)
    val activeProgress = getActiveProgressMedium(context, userId)
    val activeLessonTitle = mainLessons.find { it.route == activeLessonKey }?.title ?: ""

    val totalProgress = mainLessons.count { l ->
        prefs.getBoolean("${l.progressKey}_completed", false)
    }.toFloat() / mainLessons.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Приветствие
        Text("Добро пожаловать!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1C2F3F))
        Text("Твоя серия: 🔥5 дней", fontSize = 16.sp, color = Color(0xFF1C2F3F).copy(0.7f), modifier = Modifier.padding(bottom = 24.dp))

        // Карточка «Активный курс»
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Активный курс", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1C2F3F))
                Text(
                    text = if (activeLessonKey != null) activeLessonTitle else "Начните урок",
                    fontSize = 14.sp,
                    color = Color(0xFF1C2F3F).copy(0.7f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = if (activeLessonKey != null) activeProgress else totalProgress,
                    modifier = Modifier.fillMaxWidth(),
                    color = accentColor,
                    trackColor = Color(0xFFE0E0E0)
                )
                Text(
                    "Прогресс ${(if (activeLessonKey != null) activeProgress * 100 else totalProgress * 100).toInt()}%",
                    fontSize = 12.sp,
                    color = Color(0xFF1C2F3F).copy(0.7f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        val target = activeLessonKey
                            ?: mainLessons.firstOrNull {
                                !prefs.getBoolean("${it.progressKey}_completed", false)
                            }?.route
                            ?: mainLessons.first().route
                        if (activeLessonKey == null) {
                            setActiveLessonMedium(context, userId, target)
                        }
                        navController.navigate(target)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Продолжить обучение", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Модули", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1C2F3F))

        // Сетка уроков (без LazyVerticalGrid)
        val chunkedLessons = mainLessons.chunked(2)
        Column {
            chunkedLessons.forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { lesson ->
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(160.dp)
                                .clickable { navController.navigate(lesson.route) },
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(lesson.icon, contentDescription = null, modifier = Modifier.size(48.dp), tint = accentColor)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(lesson.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = Color(0xFF1C2F3F))
                                val isCompleted = prefs.getBoolean("${lesson.progressKey}_completed", false)
                                Text(
                                    if (isCompleted) "✅ Пройдено" else "Интерактивный урок",
                                    fontSize = 12.sp,
                                    color = if (isCompleted) Color.Green else Color(0xFF757575),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    if (rowItems.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("extra_lessons") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = accentColor)
        ) {
            Text("Ещё уроки", color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { navController.navigate("faq") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = accentColor)
        ) {
            Text("FAQ", color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
@OptIn(ExperimentalMaterial3Api::class)
// ---------- ЭКРАН "ЕЩЁ УРОКИ" ----------
@Composable
fun ExtraLessonsScreen(navController: NavController, userId: Long, accentColor: Color) {
    val extraLessons = listOf(
        MediumLessonItem("Диагностика Wi-Fi", Icons.Default.Wifi, "game_wifidiag", "game_wifidiag"),
        MediumLessonItem("Цифровое благополучие", Icons.Default.HealthAndSafety, "game_digitalwellbeing", "game_digitalwellbeing")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Дополнительные уроки") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = accentColor, titleContentColor = Color.White)
            )
        },
        containerColor = Color(0xFFC4D7DB)
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(extraLessons) { lesson ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(lesson.route) },
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(lesson.icon, contentDescription = null, modifier = Modifier.size(64.dp), tint = accentColor)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(lesson.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = Color(0xFF1C2F3F))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Интерактивный урок", fontSize = 12.sp, color = Color(0xFF757575), textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

// ---------- ЭКРАН FAQ ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqScreen(navController: NavController, accentColor: Color) {
    val faqItems = listOf(
        FaqItem1("Как выбрать смартфон?", "Для игр: мощный процессор и ОЗУ ≥6 ГБ. Для фото: хорошая камера (количество мегапикселей не главное, важнее матрица). Для всех: ёмкая батарея (от 4000 мАч) и быстрая зарядка."),
        FaqItem1("Какие обновления скачивать?", "Все официальные обновления системы и приложений из Play Маркет – безопасны. Не скачивайте обновления из подозрительных источников."),
        FaqItem1("Как пользоваться ChatGPT?", "Установите приложение ChatGPT из Play Маркет или зайдите на сайт chat.openai.com. Зарегистрируйтесь и задавайте вопросы."),
        FaqItem1("Где найти нейросети в России?", "Midjourney – через бота в Telegram. ChatGPT – через VPN или аналоги (YandexGPT, Kandinsky). Nananana – поищите в интернете."),
        FaqItem1("Как писать промты?", "Описывайте задачу подробно: роль, контекст, формат, тон. Пример: «Ты – опытный преподаватель. Объясни, как очистить кэш телефона, простыми словами»."),
        FaqItem1("Какие настройки можно менять?", "Яркость, звук, уведомления, обои – безопасно. Не меняйте настройки разработчика, если не знаете, что делаете.")
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FAQ") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = accentColor, titleContentColor = Color.White)
            )
        },
        containerColor = Color(0xFFC4D7DB)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(faqItems) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(item.question, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = accentColor)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(item.answer, fontSize = 14.sp, color = Color(0xFF616161))
                    }
                }
            }
        }
    }
}

// ---------- ЭКРАН ИГР ----------
@Composable
fun MediumGamesScreen(navController: NavController, userId: Long, accentColor: Color) {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val gameRepo = GameRepository(db.gameDao(), db.userPurchaseDao())
    var games by remember { mutableStateOf<List<Game>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            games = gameRepo.getAvailableGames(userId, 2)
        }
    }

    val freeGames = games.filter { it.price == 0 }
    val premiumGames = games.filter { it.price > 0 }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        item {
            Text("Игры для обучения", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1C2F3F))
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Text("Бесплатные", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1C2F3F))
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(freeGames) { game ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .height(140.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = getGameIcon(game.gameKey),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = accentColor
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            game.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1C2F3F),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            game.description,
                            fontSize = 14.sp,
                            color = Color(0xFF1C2F3F).copy(0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text("Рекорд: 0 очков", fontSize = 12.sp, color = accentColor)
                    }
                    Button(
                        onClick = {
                            when (game.gameKey) {
                                "tap_run" -> navController.navigate("game_taprun")
                                "swipe_quiz" -> navController.navigate("game_swipequiz")
                                "settings_puzzle" -> navController.navigate("game_settingspuzzle")
                                else -> {}
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                    ) {
                        Text("Играть", color = Color.White)
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Премиум 🏆", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1C2F3F))
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(premiumGames) { game ->
            var isPurchased by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                isPurchased = gameRepo.isGamePurchased(userId, game.id)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .height(140.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = getGameIcon(game.gameKey),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = accentColor
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            game.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1C2F3F),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            game.description,
                            fontSize = 14.sp,
                            color = Color(0xFF1C2F3F).copy(0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (isPurchased) {
                        Button(
                            onClick = {
                                when (game.gameKey) {
                                    "pro_photographer" -> navController.navigate("premium_game_pro_photographer")
                                    "cyber_detective" -> navController.navigate("premium_game_cyber_detective")
                                    "gestures" -> navController.navigate("premium_game_gestures")
                                    else -> {}
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                        ) {
                            Text("Играть", color = Color.White)
                        }
                    } else {
                        Button(
                            onClick = {
                                scope.launch {
                                    gameRepo.purchaseGame(userId, game.id)
                                    isPurchased = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                        ) {
                            Text("Купить за ${game.price} ₽", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getGameIcon(gameKey: String): ImageVector {
    return when (gameKey) {
        "tap_run" -> Icons.Default.TouchApp
        "swipe_quiz" -> Icons.Default.Swipe
        "settings_puzzle" -> Icons.Default.Settings
        "pro_photographer" -> Icons.Default.Camera
        "cyber_detective" -> Icons.Default.Security
        "gestures" -> Icons.Default.Gesture
        else -> Icons.Default.Games
    }
}

// ---------- НАГРАДЫ (ДОСТИЖЕНИЯ) ----------
@Composable
fun MediumRewardsScreen(accentColor: Color, userId: Long) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("progress_medium_$userId", Context.MODE_PRIVATE)
    val achievements = listOf(
        Achievement("Первые шаги", "Пройдите первый урок", "🏆", { prefs.getBoolean("game_notifications_completed", false) }),
        Achievement("Игроман", "Сыграйте в 3 игры", "🎮", { false }),
        Achievement("Мастер настроек", "Пройдите все уроки", "⚙️", {
            listOf("game_notifications", "game_memory", "game_battery", "game_safeapps", "game_datatransfer", "game_recovery").all {
                prefs.getBoolean("${it}_completed", false)
            }
        })
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Достижения", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1C2F3F))
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(achievements) { achievement ->
                val isUnlocked = achievement.isUnlocked()
                Card(
                    modifier = Modifier.fillMaxWidth().height(140.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isUnlocked) Color(0xFFFFD700) else Color.White.copy(alpha = 0.7f)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(achievement.icon, fontSize = 48.sp)
                        Text(achievement.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = if (isUnlocked) Color.Black else Color.Gray)
                        Text(achievement.description, fontSize = 12.sp, color = if (isUnlocked) Color.DarkGray else Color.Gray, textAlign = TextAlign.Center)
                        if (!isUnlocked) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("🔒 Не разблокировано", fontSize = 10.sp, color = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

// ---------- ПРОФИЛЬ ----------
@Composable
fun MediumProfileScreen(userId: Long, accentColor: Color) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("progress_medium_$userId", Context.MODE_PRIVATE)
    val lessonsCompleted = listOf("game_notifications", "game_memory", "game_battery", "game_safeapps", "game_datatransfer", "game_recovery").count {
        prefs.getBoolean("${it}_completed", false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC4D7DB))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.9f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(60.dp), tint = accentColor)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Анна Петровна", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1C2F3F))
        Text("annap@example.com", fontSize = 14.sp, color = Color(0xFF1C2F3F).copy(0.7f))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Ваш уровень: Уверенный", fontSize = 14.sp, color = accentColor, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatisticCard("Пройдено уроков", "$lessonsCompleted/6", accentColor)
            StatisticCard("Сыграно игр", "0", accentColor)
            StatisticCard("Достижений", "0", accentColor)
        }
        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Открыть семейный тариф */ },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.family),
                    contentDescription = "Семейный тариф",
                    modifier = Modifier.size(48.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Семейный тариф", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                    Text("До 5 человек, скидка 30% →", fontSize = 14.sp, color = Color(0xFF616161))
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { /* Редактировать */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = accentColor)
        ) {
            Text("Редактировать профиль", fontSize = 16.sp)
        }
    }
}

@Composable
fun StatisticCard(label: String, value: String, accentColor: Color) {
    Card(
        modifier = Modifier.width(100.dp).height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = accentColor)
            Text(label, fontSize = 12.sp, textAlign = TextAlign.Center, color = Color(0xFF1C2F3F))
        }
    }
}

// ---------- СЕМЕЙНЫЙ ТАРИФ ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediumFamilyPlanScreen(userId: Long, accentColor: Color) {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Семейный тариф") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = accentColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFC4D7DB)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Box(
                modifier = Modifier.size(100.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Group, contentDescription = null, modifier = Modifier.size(60.dp), tint = accentColor)
            }
            Text("Учитесь всей семьёй", fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = Color(0xFF1C2F3F))
            Text("До 5 аккаунтов, общий прогресс, скидка 30%", fontSize = 16.sp, color = Color(0xFF1C2F3F).copy(0.7f), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(32.dp))
            MediumFamilyPlanFeature("👨‍👩‍👧‍👦 До 5 участников", accentColor)
            MediumFamilyPlanFeature("📊 Общий прогресс", accentColor)
            MediumFamilyPlanFeature("🎥 Видеоуроки без рекламы", accentColor)
            MediumFamilyPlanFeature("💰 Скидка 30%", accentColor)
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { /* Покупка подписки */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Подключить за 299 ₽/мес", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun MediumFamilyPlanFeature(text: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "•", fontSize = 20.sp, modifier = Modifier.width(24.dp), color = color)
        Text(text = text, fontSize = 16.sp, color = Color(0xFF1C2F3F))
    }
}