package com.example.diplom2.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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
import com.example.diplom2.screen.dop_content.lessons_light.*
import kotlin.coroutines.jvm.internal.CompletedContinuation.context

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LightScreen(userId: Long) {
    val navController = rememberNavController()
    val backgroundColor = Color(0xFFEFE3D3)
    var showTutorial by remember {
        mutableStateOf(prefs.getBoolean("light_tutorial_done", false) == false)
    }
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
                composable("profile") { ProfileScreen(navController = navController, backgroundColor = backgroundColor) }
                composable("family_plan") { FamilyPlanScreen(navController = navController, backgroundColor = backgroundColor) }
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

// ---------- ГЛАВНЫЙ ЭКРАН СО СПИСКОМ УРОКОВ (ИГР) ----------
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

data class FaqItem(val question: String, val answer: String)

// ---------- ПРОФИЛЬ ----------
@Composable
fun ProfileScreen(navController: NavController, backgroundColor: Color) {
    Column(modifier = Modifier.fillMaxSize().background(backgroundColor).padding(24.dp)) {
        Box(modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally)) {
            Image(painter = painterResource(id = R.drawable.avatarka_light), contentDescription = "Аватар", modifier = Modifier.size(100.dp).clip(CircleShape), contentScale = ContentScale.Crop)
            Image(painter = painterResource(id = R.drawable.crown_light), contentDescription = "Корона", modifier = Modifier.size(70.dp).align(Alignment.TopCenter).offset(y = (-19).dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Анна Петровна", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121), modifier = Modifier.align(Alignment.CenterHorizontally))
        Text("Уровень 5", fontSize = 16.sp, color = Color(0xFF8B5A2B), fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(24.dp))
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)), elevation = CardDefaults.cardElevation(4.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Статистика", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatItem("Всего XP", "2,450")
                    StatItem("Уроков пройдено", "24")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatItem("Игр сыграно", "15")
                    StatItem("Дней подряд", "7 🔥")
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Card(modifier = Modifier.fillMaxWidth().clickable { navController.navigate("family_plan") }, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)), elevation = CardDefaults.cardElevation(4.dp)) {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.family), contentDescription = "Семейный тариф", modifier = Modifier.size(48.dp), contentScale = ContentScale.Fit)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Семейный тариф", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                    Text("До 5 человек, скидка 30% →", fontSize = 14.sp, color = Color(0xFF616161))
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(label, fontSize = 14.sp, color = Color(0xFF616161))
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5A2B))
    }
}

// ---------- СЕМЕЙНЫЙ ТАРИФ ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyPlanScreen(navController: NavController, backgroundColor: Color) {
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
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(32.dp))
            Box(modifier = Modifier.size(100.dp).clip(CircleShape).background(Color(0xFFE8F5E9).copy(alpha = 0.9f)), contentAlignment = Alignment.Center) {
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
            Button(onClick = { /* Переход к оплате */ }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B)), shape = RoundedCornerShape(16.dp)) {
                Text("Подключить за 299 ₽/мес", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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