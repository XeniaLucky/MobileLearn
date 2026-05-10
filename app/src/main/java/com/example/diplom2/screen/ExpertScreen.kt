package com.example.diplom2.screen

import GameExpert.AdbCommandoGame
import GameExpert.CyberShieldGame
import GameExpert.RootConstructorGame
import GameExpert.CustomBuilderGame
import GameExpert.SmartDetectiveGame
import android.widget.Toast
import androidx.compose.ui.text.style.TextOverflow
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.diplom2.R
import com.example.diplom2.screen.dop_content.lessons_expert.*
import com.example.diplom2.screen.saveLessonProgress

// ── Функции для активного урока (Expert) ──
// ── Дополнительные функции для сохранения/восстановления шага ──
fun saveLastStepExpert(context: Context, userId: Long, lessonKey: String, step: Int) {
    val p = context.getSharedPreferences("progress_expert_$userId", Context.MODE_PRIVATE)
    p.edit().putInt("last_step_$lessonKey", step).apply()
}

fun getLastStepExpert(context: Context, userId: Long, lessonKey: String): Int {
    val p = context.getSharedPreferences("progress_expert_$userId", Context.MODE_PRIVATE)
    return p.getInt("last_step_$lessonKey", 0)
}

// Заменяет setActiveLessonExpert – активирует урок, не сбрасывая прогресс/шаг, если урок уже активен
fun activateLessonExpert(context: Context, userId: Long, lessonKey: String) {
    val p = context.getSharedPreferences("progress_expert_$userId", Context.MODE_PRIVATE)
    val currentActive = p.getString("active_lesson_key", null)
    if (currentActive != lessonKey) {
        p.edit()
            .putString("active_lesson_key", lessonKey)
            .putFloat("active_lesson_progress", 0f)
            .putInt("last_step_$lessonKey", 0)    // начинаем заново только если урок сменился
            .apply()
    }
    // Если урок тот же – ничего не меняем (оставляем прогресс и шаг)
}
fun setActiveLessonExpert(context: Context, userId: Long, lessonKey: String) {
    val p = context.getSharedPreferences("progress_expert_$userId", Context.MODE_PRIVATE)
    p.edit().putString("active_lesson_key", lessonKey).apply()
    p.edit().putFloat("active_lesson_progress", 0f).apply()
}

fun updateLessonProgressExpert(context: Context, userId: Long, progress: Float) {
    val p = context.getSharedPreferences("progress_expert_$userId", Context.MODE_PRIVATE)
    p.edit().putFloat("active_lesson_progress", progress.coerceIn(0f, 1f)).apply()
}

fun getActiveLessonExpert(context: Context, userId: Long): String? {
    val p = context.getSharedPreferences("progress_expert_$userId", Context.MODE_PRIVATE)
    return p.getString("active_lesson_key", null)
}

fun getActiveProgressExpert(context: Context, userId: Long): Float {
    val p = context.getSharedPreferences("progress_expert_$userId", Context.MODE_PRIVATE)
    return p.getFloat("active_lesson_progress", 0f)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpertScreen(userId: Long) {
    val navController = rememberNavController()
    val backgroundColor = Color(0xFF09020A)
    val accentColor = Color(0xFFD4AF37)

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1A1A2E),
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
                        unselectedIconColor = Color.White.copy(0.6f),
                        unselectedTextColor = Color.White.copy(0.6f)
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
                        unselectedIconColor = Color.White.copy(0.6f),
                        unselectedTextColor = Color.White.copy(0.6f)
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
                        unselectedIconColor = Color.White.copy(0.6f),
                        unselectedTextColor = Color.White.copy(0.6f)
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
                        unselectedIconColor = Color.White.copy(0.6f),
                        unselectedTextColor = Color.White.copy(0.6f)
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
                    ExpertHomeScreen(navController = navController, accentColor = accentColor, userId = userId)
                }
                composable("games") {
                    ExpertGamesScreen(navController = navController, accentColor = accentColor)
                }
                composable("game_adb_commando") {
                    AdbCommandoGame(navController)
                }

                composable("game_cybershield") {
                    CyberShieldGame(navController)
                }

                composable("game_root_constructor") {
                    RootConstructorGame(navController)
                }

                composable("game_custom_builder") {
                    CustomBuilderGame(navController)
                }

                composable("game_smart_detective") {
                    SmartDetectiveGame(navController)
                }

                composable("extra_lessons") {
                    ExpertExtraLessonsScreen(navController = navController, accentColor = accentColor, userId = userId)
                }
                // 6 основных уроков
                composable("lesson_adb") { LessonAdbScreen(navController, userId) }
                composable("lesson_root") { LessonRootScreen(navController, userId) }
                composable("lesson_custom_roms") { LessonCustomRomsScreen(navController, userId) }
                composable("lesson_optimization") { LessonOptimizationScreen(navController, userId) }
                composable("lesson_scripts") { LessonScriptsScreen(navController, userId) }
                composable("lesson_security") { LessonSecurityScreen(navController, userId) }
                // Дополнительные уроки (4 штуки)
                composable("lesson_logging") { LessonLoggingScreen(navController, userId) }
                composable("lesson_filesystem") { LessonFilesystemScreen(navController, userId) }
                composable("lesson_networking") { LessonNetworkingScreen(navController, userId) }
                composable("lesson_emulation") { LessonEmulationScreen(navController, userId) }
            }
        }
    }
}

@Composable
fun ExpertHomeScreen(navController: NavController, accentColor: Color, userId: Long) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("progress_expert_$userId", Context.MODE_PRIVATE)

    val mainLessons = listOf(
        ExpertLessonItem("ADB Shell", Icons.Default.Terminal, "lesson_adb", "lesson_adb"),
        ExpertLessonItem("Root-доступ", Icons.Default.Lock, "lesson_root", "lesson_root"),
        ExpertLessonItem("Кастомные прошивки", Icons.Default.Smartphone, "lesson_custom_roms", "lesson_custom_roms"),
        ExpertLessonItem("Оптимизация", Icons.Default.Speed, "lesson_optimization", "lesson_optimization"),
        ExpertLessonItem("Скрипты", Icons.Default.Code, "lesson_scripts", "lesson_scripts"),
        ExpertLessonItem("Безопасность", Icons.Default.Security, "lesson_security", "lesson_security")
    )

    // Используем expert‑функции
    val activeLessonKey = getActiveLessonExpert(context, userId)
    val activeProgress = getActiveProgressExpert(context, userId)
    val activeLessonTitle = mainLessons.find { it.route == activeLessonKey }?.title ?: ""

    // Общий прогресс (fallback)
    val totalProgress = mainLessons.count { l ->
        prefs.getBoolean("${l.progressKey}_completed", false)
    }.toFloat() / mainLessons.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Добро пожаловать, Мастер!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Твоя серия: 🔥15 дней", fontSize = 16.sp, color = Color.White.copy(0.7f), modifier = Modifier.padding(bottom = 24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Продвинутый курс", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(
                    text = if (activeLessonKey != null) activeLessonTitle else "ADB, рутирование, кастомизация",
                    fontSize = 14.sp,
                    color = Color.White.copy(0.7f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = if (activeLessonKey != null) activeProgress else totalProgress,
                    modifier = Modifier.fillMaxWidth(),
                    color = accentColor,
                    trackColor = Color.White.copy(0.2f)
                )
                Text(
                    "Прогресс ${(if (activeLessonKey != null) activeProgress * 100 else totalProgress * 100).toInt()}%",
                    fontSize = 12.sp,
                    color = Color.White.copy(0.7f)
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
                            setActiveLessonExpert(context, userId, target)  // <-- expert-функция
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
        Text("Модули", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)

        // Обычная сетка (без LazyVerticalGrid, чтобы избежать конфликтов со скроллом)
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
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(lesson.icon, contentDescription = null, modifier = Modifier.size(48.dp), tint = accentColor)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(lesson.title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.White, textAlign = TextAlign.Center)
                                val isCompleted = prefs.getBoolean("${lesson.progressKey}_completed", false)
                                if (isCompleted) {
                                    Text("✅ Пройдено", fontSize = 10.sp, color = Color.Green)
                                }
                            }
                        }
                    }
                    // Если в строке не хватает карточки, добавляем Spacer
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
            Text("Ещё уроки", color = Color.White)  // <-- белый текст, как у среднего
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
// ---------- ЭКРАН "ЕЩЁ УРОКИ" ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpertExtraLessonsScreen(navController: NavController, accentColor: Color, userId: Long) {
    val extraLessons = listOf(
        ExpertLessonItem("Отладка и логи", Icons.Default.BugReport, "lesson_logging", "lesson_logging"),
        ExpertLessonItem("Файловая система", Icons.Default.Folder, "lesson_filesystem", "lesson_filesystem"),
        ExpertLessonItem("Сеть и серверы", Icons.Default.Router, "lesson_networking", "lesson_networking"),
        ExpertLessonItem("Эмуляция", Icons.Default.Devices, "lesson_emulation", "lesson_emulation")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Дополнительные уроки") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = accentColor, titleContentColor = Color.Black)
            )
        },
        containerColor = Color(0xFF09020A)
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(extraLessons) { lesson ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { navController.navigate(lesson.route) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(lesson.icon, contentDescription = null, modifier = Modifier.size(48.dp), tint = accentColor)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(lesson.title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.White, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}
@Composable
fun ExpertGamesScreen(navController: NavController, accentColor: Color) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("expert_purchases", Context.MODE_PRIVATE) }

    // Состояние покупок: key = route игры
    val purchased = remember {
        mutableStateMapOf<String, Boolean>().apply {
            listOf("game_root_constructor", "game_custom_builder", "game_smart_detective").forEach { route ->
                put(route, prefs.getBoolean(route, false))
            }
        }
    }

    // Модели игр
    val allGames = listOf(
        GameExpertModel(
            name = "ADB-Командо",
            description = "Тренируй команды ADB от простых до сложных",
            price = 0,
            route = "game_adb_commando",
            isPurchased = true,
            icon = Icons.Default.Terminal
        ),

        GameExpertModel(
            name = "КиберЩит",
            description = "Распознай фишинг, вирусы и опасные разрешения",
            price = 0,
            route = "game_cybershield",
            isPurchased = true,
            icon = Icons.Default.Security
        ),

        GameExpertModel(
            name = "Root-конструктор",
            description = "Пошаговый квест по рутированию телефона",
            price = 349,
            route = "game_root_constructor",
            isPurchased = false,
            icon = Icons.Default.Bolt
        ),

        GameExpertModel(
            name = "Твой кастом",
            description = "Собери идеальную кастомную прошивку",
            price = 499,
            route = "game_custom_builder",
            isPurchased = false,
            icon = Icons.Default.Smartphone
        ),

        GameExpertModel(
            name = "Smart·Detective",
            description = "Раскрывай преступления с помощью автоматизации Android",
            price = 399,
            route = "game_smart_detective",
            isPurchased = false,
            icon = Icons.Default.Search
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            "Тренировки для экспертов",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Секция "Бесплатные"
        Text(
            "Бесплатные",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(alpha = 0.9f),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        allGames.filter { it.price == 0 }.forEach { game ->
            GameCard(
                game = game,
                isPurchased = true, // бесплатные всегда доступны
                accentColor = accentColor,
                onPlay = { navController.navigate(game.route) },
                onBuy = {} // не используется
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Секция "Премиум"
        Text(
            "Премиум",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = accentColor,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        allGames.filter { it.price > 0 }.forEach { game ->
            val isPurchased = purchased[game.route] ?: false
            GameCard(
                game = game,
                isPurchased = isPurchased,
                accentColor = accentColor,
                onPlay = { navController.navigate(game.route) },
                onBuy = {
                    // Имитация покупки: сохраняем состояние и показываем Toast
                    purchased[game.route] = true
                    prefs.edit().putBoolean(game.route, true).apply()
                    Toast.makeText(context, "Игра куплена!", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
private fun GameCard(
    game: GameExpertModel,
    isPurchased: Boolean,
    accentColor: Color,
    onPlay: () -> Unit,
    onBuy: () -> Unit
) {
    val cardColors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
    val buttonColors = ButtonDefaults.buttonColors(containerColor = accentColor)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(enabled = isPurchased || game.price == 0) { onPlay() },
        shape = RoundedCornerShape(14.dp),
        colors = cardColors,
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка игры с фоном
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = accentColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    game.icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    game.name,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 17.sp
                )
                Text(
                    game.description,
                    color = Color.White.copy(alpha = 0.65f),
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                // Цена или статус
                if (game.price == 0) {
                    Text(
                        "Бесплатно",
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    )
                } else {
                    Text(
                        if (isPurchased) "Приобретено" else "${game.price} ₽",
                        color = if (isPurchased) Color.White.copy(alpha = 0.8f) else accentColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }

            // Кнопка действия
            if (game.price == 0 || isPurchased) {
                Button(
                    onClick = onPlay,
                    colors = buttonColors,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text("Играть", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = onBuy,
                    colors = buttonColors,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text("Купить", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
data class GameExpertModel(
    val name: String,
    val description: String,
    val price: Int,
    val route: String,
    val isPurchased: Boolean,
    val icon: ImageVector
)
@Composable
fun ExpertRewardsScreen(accentColor: Color) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Эксклюзивные награды для Мастеров", fontSize = 18.sp, color = accentColor)
    }
}

@Composable
fun ExpertProfileScreen(navController: NavController, accentColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF09020A))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFF1A1A2E)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(60.dp), tint = accentColor)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Алексей Мастер", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("alex.master@example.com", fontSize = 14.sp, color = Color.White.copy(0.7f))
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = { navController.navigate("family_plan") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Group, contentDescription = null, modifier = Modifier.size(24.dp), tint = Color.Black)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Семейный тариф", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpertFamilyPlanScreen(navController: NavController, accentColor: Color) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Семейный тариф") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = accentColor, titleContentColor = Color.Black, navigationIconContentColor = Color.Black)
            )
        },
        containerColor = Color(0xFF09020A)
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
                modifier = Modifier.size(100.dp).clip(CircleShape).background(Color(0xFF1A1A2E)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Group, contentDescription = null, modifier = Modifier.size(60.dp), tint = accentColor)
            }
            Text("Учитесь всей семьёй", fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = Color.White)
            Text("До 5 аккаунтов, общий прогресс, скидка 30%", fontSize = 16.sp, color = Color.White.copy(0.7f), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(32.dp))
            ExpertFamilyPlanFeature("👨‍👩‍👧‍👦 До 5 участников", accentColor)
            ExpertFamilyPlanFeature("📊 Общий прогресс", accentColor)
            ExpertFamilyPlanFeature("🎥 Видеоуроки без рекламы", accentColor)
            ExpertFamilyPlanFeature("💰 Скидка 30%", accentColor)
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { /* оплата */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Подключить за 499 ₽/мес", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}

@Composable
fun ExpertFamilyPlanFeature(text: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "•", fontSize = 20.sp, modifier = Modifier.width(24.dp), color = color)
        Text(text = text, fontSize = 16.sp, color = Color.White)
    }
}

@Composable
fun ExpertGameDetailScreen(gameId: String, navController: NavController, accentColor: Color) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF09020A)), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Игра: $gameId", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }, colors = ButtonDefaults.buttonColors(containerColor = accentColor)) {
                Text("Назад", color = Color.Black)
            }
        }
    }
}

@Composable
fun ExpertPremiumGamesScreen(navController: NavController, accentColor: Color) {
    ExpertGamesScreen(navController, accentColor)
}

data class PremiumGameExpert(val name: String, val description: String, val price: Int, val id: String)
data class ExpertLessonItem(val title: String, val icon: ImageVector, val route: String, val progressKey: String)