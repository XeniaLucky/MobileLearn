package com.example.diplom2

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bd.AppDatabase
import com.example.diplom2.screen.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import repository.UserRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartphoneLearningApp()
        }
    }
}

@Composable
fun SmartphoneLearningApp() {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    var isLoggedIn by remember { mutableStateOf(prefs.getLong("userId", -1) != -1L) }
    var userId by remember { mutableStateOf(prefs.getLong("userId", -1)) }
    var showOnboarding by remember { mutableStateOf(!prefs.getBoolean("onboarding_completed", false)) }
    var selectedLevel by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Загрузка уровня пользователя из БД при входе
    LaunchedEffect(userId) {
        if (userId != -1L) {
            val db = AppDatabase.getInstance(context)
            val userRepo = UserRepository(db.userDao())
            val user = withContext(Dispatchers.IO) {
                userRepo.getUser(userId)
            }
            user?.let {
                selectedLevel = when (it.levelId) {
                    1 -> "beginner"
                    2 -> "intermediate"
                    3 -> "expert"
                    else -> null
                }
            }
        }
    }

    if (!isLoggedIn) {
        AuthScreen(onLoginSuccess = { id ->
            userId = id
            prefs.edit().putLong("userId", id).apply()
            isLoggedIn = true
            showOnboarding = !prefs.getBoolean("onboarding_completed", false)
            // После входа загрузим уровень
            scope.launch {
                val db = AppDatabase.getInstance(context)
                val userRepo = UserRepository(db.userDao())
                val user = withContext(Dispatchers.IO) {
                    userRepo.getUser(id)
                }
                user?.let {
                    selectedLevel = when (it.levelId) {
                        1 -> "beginner"
                        2 -> "intermediate"
                        3 -> "expert"
                        else -> null
                    }
                }
            }
        })
        return
    }

    if (showOnboarding) {
        OnboardingScreen(onFinish = {
            showOnboarding = false
            prefs.edit().putBoolean("onboarding_completed", true).apply()
        })
        return
    }

    if (selectedLevel == null) {
        UserLevelSelectionScreen(onLevelSelected = { level ->
            selectedLevel = level
            // Сохраняем уровень пользователя в БД
            if (userId != -1L) {
                scope.launch {
                    val db = AppDatabase.getInstance(context)
                    val userRepo = UserRepository(db.userDao())
                    val user = withContext(Dispatchers.IO) {
                        userRepo.getUser(userId)
                    }
                    user?.let {
                        val newLevel = when (level) {
                            "beginner" -> 1
                            "intermediate" -> 2
                            "expert" -> 3
                            else -> 1
                        }
                        val updatedUser = it.copy(levelId = newLevel)
                        withContext(Dispatchers.IO) {
                            userRepo.updateUser(updatedUser)
                        }
                    }
                }
            }
        })
        return
    }

    // Функция выхода из аккаунта
    fun handleLogout() {
        prefs.edit().remove("userId").apply()
        prefs.edit().putBoolean("onboarding_completed", false).apply()
        isLoggedIn = false
        userId = -1
        selectedLevel = null
        showOnboarding = true
    }

    when (selectedLevel) {
        "beginner" -> {
            val navController = androidx.navigation.compose.rememberNavController()
            LightScreen(userId = userId, onLogout = { handleLogout() })
        }
        "intermediate" -> {
            MediumScreen(userId = userId, onLogout = { handleLogout() })
        }
        "expert" -> {
            ExpertScreen(userId = userId, onLogout = { handleLogout() })
        }
    }
}

// Остальные функции (OnboardingScreen, UserLevelSelectionScreen, LevelOption) остаются без изменений
// ---------- ОНБОРДИНГ ----------
@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pages = listOf(
        OnboardingPageData(
            title = "<Привет, пользователь...>",
            description = "Научим пользоваться смартфоном легко и без стресса.",
            imageRes = null
        ),
        OnboardingPageData(
            title = "Простые уроки",
            description = "От звонков до приложений – всё по шагам с картинками.",
            imageRes = R.drawable.start_2
        ),
        OnboardingPageData(
            title = "Ваш темп",
            description = "Занимайтесь в удобное время, повторяйте сколько нужно.",
            imageRes = R.drawable.start_3
        )
    )

    var currentPage by remember { mutableIntStateOf(0) }

    fun goNext() {
        if (currentPage < pages.size - 1) currentPage++
        else onFinish()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .paint(
                        painterResource(id=R.drawable.fon_start),
                        contentScale = ContentScale.FillBounds)


            ) {
                val page = pages[currentPage]
                if (page.imageRes != null) {
                    Image(
                        painter = painterResource(id = page.imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }

                Text(
                    text = page.title,
                    fontSize = if (currentPage == 0) 36.sp else 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = page.description,
                    fontSize = 18.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pages.size) { index ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .padding(horizontal = 4.dp)
                        .background(
                            color = if (currentPage == index) Color(0xFF4CAF50) else Color.White.copy(alpha = 0.7f),
                            shape = CircleShape
                        )
                )
            }
        }

        Button(
            onClick = { goNext() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 8.dp)
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCD9E75)),
            shape = RoundedCornerShape(26.dp)
        ) {
            Text(
                text = if (currentPage < pages.size - 1) "Далее" else "Начать",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C323F)
            )
        }

        TextButton(
            onClick = onFinish,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 4.dp)
                .height(48.dp)
        ) {
            Text(
                text = "Пропустить",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

data class OnboardingPageData(
    val title: String,
    val description: String,
    val imageRes: Int? = null
)

// ---------- ВЫБОР УРОВНЯ ----------
@Composable
fun UserLevelSelectionScreen(onLevelSelected: (String) -> Unit) {
    var selected by remember { mutableStateOf<String?>(null) }
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF505FAC)).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Кто вы?", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Выберите свой уровень подготовки", fontSize = 18.sp, color = Color.White.copy(0.9f))
        Spacer(Modifier.height(48.dp))
        LevelOption("Полный ноль", "Не умею звонить, путаюсь в кнопках", selected == "beginner") { selected = "beginner" }
        LevelOption("Уверенный", "Хочу больше фишек и игр", selected == "intermediate") { selected = "intermediate" }
        LevelOption("Мастер", "Рутирование, ADB, тонкие настройки", selected == "expert") { selected = "expert" }
        Spacer(Modifier.weight(1f))
        Button(onClick = { if (selected != null) onLevelSelected(selected!!) }, enabled = selected != null,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCD9E75), disabledContainerColor = Color(0xFFCD9E75).copy(0.5f)),
            shape = RoundedCornerShape(28.dp)
        ) { Text("Продолжить", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1C323F)) }
    }
}

@Composable
fun LevelOption(title: String, description: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFFCD9E75) else Color.White.copy(0.2f))
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = isSelected, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFCD9E75)))
            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color(0xFF1C323F) else Color.White)
                Text(description, fontSize = 14.sp, color = if (isSelected) Color(0xFF1C323F).copy(0.8f) else Color.White.copy(0.8f))
            }
        }
    }
}