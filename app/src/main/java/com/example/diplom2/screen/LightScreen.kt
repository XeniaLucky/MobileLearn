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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import bd.AppDatabase
import com.example.diplom2.R
import com.example.diplom2.screen.dop_content.lessons_light.*
import kotlinx.coroutines.launch
import repository.UserRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.diplom2.screen.AiChatScreen

@Composable
fun TutorialOverlay(
    steps: List<String>,
    onFinish: () -> Unit
) {
    var currentStep by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        Card(modifier = Modifier.padding(24.dp)) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = steps[currentStep], fontSize = 20.sp)
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = {
                    if (currentStep < steps.lastIndex) currentStep++
                    else onFinish()
                }) {
                    Text("Далее")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LightScreen(userId: Long, onLogout: () -> Unit, onLevelChange: (String) -> Unit) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val navController = rememberNavController()
    val backgroundColor = Color(0xFFEFE3D3)
    var showTutorial by remember { mutableStateOf(!prefs.getBoolean("light_tutorial_done", false)) }

    if (showTutorial) {
        TutorialOverlay(
            steps = listOf(
                "Это раздел «Уроки» – здесь ты учишься пользоваться телефоном.",
                "В «Справочнике» находятся ответы на частые вопросы.",
                "В «Профиле» ты можешь изменить свои данные или сменить уровень."
            ),
            onFinish = {
                showTutorial = false
                context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    .edit().putBoolean("light_tutorial_done", true).apply()
            }
        )
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFEFE3D3).copy(alpha = 0.95f),
                tonalElevation = 0.dp
            ) {
                NavigationBarItem(
                    icon = { Image(painter = painterResource(id = R.drawable.home_light), contentDescription = "Главная", modifier = Modifier.size(24.dp)) },
                    label = { Text("Главная") },
                    selected = navController.currentDestination?.route == "lessons",
                    onClick = { navController.navigate("lessons") }
                )
                NavigationBarItem(
                    icon = { Image(painter = painterResource(id = R.drawable.spravochnik), contentDescription = "Справочник", modifier = Modifier.size(24.dp)) },
                    label = { Text("Справочник") },
                    selected = navController.currentDestination?.route == "guide",
                    onClick = { navController.navigate("guide") }
                )
                NavigationBarItem(
                    icon = { Image(painter = painterResource(id = R.drawable.profile_light), contentDescription = "Профиль", modifier = Modifier.size(24.dp)) },
                    label = { Text("Профиль") },
                    selected = navController.currentDestination?.route == "profile",
                    onClick = { navController.navigate("profile") }
                )
            }
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(navController = navController, startDestination = "lessons") {
                composable("lessons") { LessonsScreen(navController = navController, backgroundColor = backgroundColor) }
                composable("guide") { GuideScreen(navController = navController, backgroundColor = backgroundColor) }
                composable("ai_chat") {
                    AiChatScreen(navController = navController, accentColor = Color(0xFFD5C29B))
                }
                composable("profile") {
                    UniversalProfileScreen(
                        navController = navController,
                        userId = userId,
                        levelPrefix = "light_",
                        accentColor = Color(0xFF8B5A2B),
                        backgroundColor = backgroundColor,
                        onLogout = onLogout,
                        onLevelChange = onLevelChange
                    )
                }
                composable("family_plan") {
                    FamilyPlanScreen(navController, backgroundColor, userId)
                }
                // Игровые уроки
                composable("game_power") { PowerLessonScreen(navController = navController, userId = userId) }
                composable("game_call") { CallLessonScreen(navController = navController, userId = userId) }
                composable("game_touch") { TouchLessonScreen(navController = navController, userId = userId) }
                composable("game_contacts") { ContactsLessonScreen(navController = navController, userId = userId) }
                composable("game_messages") { MessagesLessonScreen(navController = navController, userId = userId) }
                composable("game_camera") { CameraLessonScreen(navController = navController, userId = userId) }
                composable("game_wifi") { WifiLessonScreen(navController = navController, userId = userId) }
                composable("game_apps") { AppsLessonScreen(navController = navController, userId = userId) }
            }
        }
    }
}

// ---------- ГЛАВНЫЙ ЭКРАН СО СПИСКОМ УРОКОВ ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonsScreen(navController: NavController, backgroundColor: Color) {
    val gameLessons = listOf(
        Triple("Включение телефона", Icons.Default.PowerSettingsNew, "game_power"),
        Triple("Ответ на звонок", Icons.Default.Phone, "game_call"),
        Triple("Сенсорный экран", Icons.Default.TouchApp, "game_touch"),
        Triple("Контакты", Icons.Default.Contacts, "game_contacts"),
        Triple("Отправка сообщений", Icons.AutoMirrored.Filled.Message, "game_messages"),
        Triple("Камера и фото", Icons.Default.CameraAlt, "game_camera"),
        Triple("Интернет и Wi-Fi", Icons.Default.Wifi, "game_wifi"),
        Triple("Приложения", Icons.Default.Apps, "game_apps")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Уроки • Начинающий") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF8B5A2B), titleContentColor = Color.White)
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(gameLessons) { (title, icon, route) ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { navController.navigate(route) },
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFC4D7DB).copy(alpha = 0.9f))
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(icon, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color(0xFF8B5A2B))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = Color(0xFF212121))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Пройдите интерактивный урок", fontSize = 12.sp, color = Color(0xFF757575), textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

// ---------- ЭКРАН "СПРАВОЧНИК" ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideScreen(navController: NavController, backgroundColor: Color) {
    val faqItems = listOf(
        FaqItem("Не могу никому позвонить, сеть пропала", "Проверьте, включён ли режим «В самолёте». Зайдите в настройки → Сеть → отключите режим полёта. Также проверьте, есть ли деньги на счёте или активна ли SIM-карта."),
        FaqItem("Экран не реагирует на нажатия", "Попробуйте перезагрузить телефон (зажмите кнопку питания на 10 секунд). Если не помогает – возможно, требуется чистка экрана или обращение в сервис."),
        FaqItem("Как подключить Wi-Fi?", "Настройки → Сеть и интернет → Wi-Fi → включите и выберите нужную сеть, введите пароль."),
        FaqItem("Что делать, если сел аккумулятор?", "Подключите зарядное устройство. Если телефон не заряжается – попробуйте другой кабель или проверьте разъём."),
        FaqItem("Как сделать скриншот?", "Одновременно зажмите кнопку питания и кнопку уменьшения громкости на 1-2 секунды.")
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Справочник") }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF8B5A2B), titleContentColor = Color.White)) },
        containerColor = backgroundColor
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Text("Часто задаваемые вопросы", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121), modifier = Modifier.padding(bottom = 8.dp))
            }
            items(faqItems) { faq ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(faq.question, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5A2B))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(faq.answer, fontSize = 14.sp, color = Color(0xFF616161))
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

    data class FaqItem1(val question: String, val answer: String)
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
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate("ai_chat") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Android, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Задать вопрос AI-помощнику", color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
data class FaqItem(val question: String, val answer: String)

// ---------- СЕМЕЙНЫЙ ТАРИФ ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyPlanScreen(navController: NavController, backgroundColor: Color, userId: Long) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = AppDatabase.getInstance(context)
    val userRepo = UserRepository(db.userDao())
    var isSubscribing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Семейный тариф") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF8B5A2B), titleContentColor = Color.White, navigationIconContentColor = Color.White)
            )
        },
        containerColor = backgroundColor
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
                modifier = Modifier.size(100.dp).clip(CircleShape).background(Color(0xFFE8F5E9).copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Group, contentDescription = null, modifier = Modifier.size(60.dp), tint = Color(0xFF8B5A2B))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Учитесь всей семьёй", fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = Color(0xFF212121))
            Text("До 5 аккаунтов, общий прогресс, скидка 30%", fontSize = 16.sp, color = Color(0xFF616161), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(32.dp))
            LightFamilyPlanFeature("👨‍👩‍👧‍👦 До 5 участников", Color(0xFF8B5A2B))
            LightFamilyPlanFeature("📊 Общий прогресс", Color(0xFF8B5A2B))
            LightFamilyPlanFeature("🎥 Видеоуроки без рекламы", Color(0xFF8B5A2B))
            LightFamilyPlanFeature("💰 Скидка 30%", Color(0xFF8B5A2B))
            Spacer(modifier = Modifier.weight(1f))

            if (isSubscribing) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        isSubscribing = true
                        scope.launch {
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val expiryDate = dateFormat.format(Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000))
                            userRepo.activateFamilySubscription(userId, expiryDate)
                            // Уведомляем профиль об активации
                            navController.previousBackStackEntry?.savedStateHandle?.set("subscription_activated", true)
                            isSubscribing = false
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Подключить за 299 ₽/мес", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun LightFamilyPlanFeature(text: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "•", fontSize = 20.sp, modifier = Modifier.width(24.dp), color = color)
        Text(text = text, fontSize = 16.sp, color = Color(0xFF212121))
    }
}