package GameExpert

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class CyberScenario(
    val title: String,
    val fakeNotification: String,
    val options: List<String>,
    val correct: Int,
    val explanation: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CyberShieldGame(navController: NavController) {

    val scenarios = listOf(

        CyberScenario(
            "Подозрительное SMS",
            "Ваш банк: подтвердите перевод по ссылке",
            listOf(
                "Перейти по ссылке",
                "Проверить банк через официальное приложение",
                "Сообщить код из SMS"
            ),
            1,
            "Никогда не переходите по ссылкам из SMS."
        ),

        CyberScenario(
            "Опасный APK",
            "Скачайте MOD Premium бесплатно",
            listOf(
                "Установить APK",
                "Проверить разрешения приложения",
                "Отключить Play Protect"
            ),
            1,
            "Подозрительные APK могут содержать вирусы."
        ),

        CyberScenario(
            "Фишинговый Email",
            "Ваш аккаунт будет удалён через 24 часа",
            listOf(
                "Ввести пароль",
                "Проверить отправителя",
                "Скачать вложение"
            ),
            1,
            "Всегда проверяйте адрес отправителя."
        ),

        CyberScenario(
            "Поддельное обновление",
            "Срочно обновите Android",
            listOf(
                "Скачать APK",
                "Проверить обновления в настройках",
                "Отключить защиту"
            ),
            1,
            "Обновления Android устанавливаются через систему."
        ),

        CyberScenario(
            "Странное разрешение",
            "Фонарик просит доступ к SMS",
            listOf(
                "Разрешить",
                "Удалить приложение",
                "Игнорировать"
            ),
            1,
            "Приложения не должны запрашивать лишние разрешения."
        )

    )

    var currentIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var message by remember { mutableStateOf("") }
    var success by remember { mutableStateOf(false) }
    var completed by remember { mutableStateOf(false) }

    val scenario = scenarios[currentIndex]

    Scaffold(
        containerColor = Color(0xFF09020A),

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        "КиберЩит",
                        color = Color.White
                    )
                },

                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            null,
                            tint = Color.White
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF12091F)
                )
            )
        }

    ) { padding ->

        if (completed) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFF09020A)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    "🛡 КиберЗащитник",
                    color = Color(0xFF2E8058),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "Очки: $score",
                    color = Color.White,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7C3AED)
                    )
                ) {
                    Text("Вернуться")
                }
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {

                item {

                    Text(
                        "Сценарий ${currentIndex + 1}/${scenarios.size}",
                        color = Color(0xFFB388FF),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = Color(0xFF181325)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {

                            Text(
                                scenario.title,
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF24163A)
                            ) {

                                Text(
                                    scenario.fakeNotification,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    scenario.options.forEachIndexed { index, option ->

                        Button(

                            onClick = {

                                if (index == scenario.correct) {

                                    success = true
                                    score += 10
                                    message = scenario.explanation

                                    if (currentIndex == scenarios.lastIndex) {
                                        completed = true
                                    } else {
                                        currentIndex++
                                    }

                                } else {

                                    success = false
                                    message = "Это опасно! ${scenario.explanation}"
                                }
                            },

                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),

                            shape = RoundedCornerShape(16.dp),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF7C3AED)
                            )

                        ) {
                            Text(
                                option,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    AnimatedVisibility(
                        visible = message.isNotEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {

                        Text(
                            message,
                            color = if (success) Color(0xFF2E8058) else Color( 0xFF9B0C3F),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}