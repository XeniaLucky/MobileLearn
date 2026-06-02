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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class RootStep(
    val title: String,
    val description: String,
    val warning: String,
    val successText: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootConstructorGame(navController: NavController) {

    var purchased by remember { mutableStateOf(false) }

    val phoneModels = listOf(
        "Samsung",
        "Xiaomi",
        "Google Pixel"
    )

    var selectedPhone by remember {
        mutableStateOf(phoneModels[0])
    }

    val steps = listOf(

        RootStep(
            "Разблокировка загрузчика",
            "Включите OEM Unlock и подтвердите разблокировку bootloader.",
            "Неправильная разблокировка может удалить все данные.",
            "Bootloader успешно разблокирован."
        ),

        RootStep(
            "Fastboot Mode",
            "Переведите устройство в fastboot режим.",
            "Ошибка fastboot может привести к bootloop.",
            "Fastboot режим активирован."
        ),

        RootStep(
            "Установка Recovery",
            "Прошейте TWRP Recovery через fastboot.",
            "Неправильный recovery может привести к кирпичу.",
            "Recovery успешно установлено."
        ),

        RootStep(
            "Установка Magisk",
            "Прошейте Magisk ZIP для получения root.",
            "Неправильный Magisk может повредить систему.",
            "Root успешно получен."
        ),

        RootStep(
            "Проверка Root",
            "Проверьте root-права через Root Checker.",
            "Не выдавайте root подозрительным приложениям.",
            "Root подтверждён."
        )
    )

    var currentStep by remember { mutableIntStateOf(0) }

    var actionMessage by remember {
        mutableStateOf("")
    }

    var isSuccess by remember {
        mutableStateOf(false)
    }

    var completed by remember {
        mutableStateOf(false)
    }

    Scaffold(

        containerColor = Color(0xFF09020A),

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        "Root-конструктор",
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

        if (!purchased) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {

                item {

                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = Color(0xFF181325)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    Icons.Default.Bolt,
                                    null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(40.dp)
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    "Root-конструктор",
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                "Пошаговый квест по рутированию смартфона.",
                                color = Color.LightGray,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                "Что входит:",
                                color = Color(0xFFB388FF),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            steps.forEach {

                                Text(
                                    "• ${it.title}",
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(30.dp))

                            Button(

                                onClick = {
                                    purchased = true
                                },

                                modifier = Modifier.fillMaxWidth(),

                                shape = RoundedCornerShape(18.dp),

                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF7C3AED)
                                )

                            ) {

                                Text(
                                    "Купить за 349 ₽",
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            }

        } else {

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
                        "🏆 Root-мастер",
                        color = Color(0xFFFFD700),
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        "Сертификат успешно получен",
                        color = Color.White,
                        fontSize = 22.sp
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

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {

                    Text(
                        "Устройство",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row {

                        phoneModels.forEach { phone ->

                            FilterChip(

                                selected = selectedPhone == phone,

                                onClick = {
                                    selectedPhone = phone
                                },

                                label = {
                                    Text(phone)
                                },

                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    LinearProgressIndicator(
                        progress = { (currentStep + 1) / steps.size.toFloat() },
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF8B5CF6),
                        trackColor = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = Color(0xFF181325)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {

                            Text(
                                "Шаг ${currentStep + 1}",
                                color = Color(0xFFB388FF),
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                steps[currentStep].title,
                                color = Color.White,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                steps[currentStep].description,
                                color = Color.LightGray,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    Icons.Default.Warning,
                                    null,
                                    tint = Color( 0xFF9B0C3F)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    steps[currentStep].warning,
                                    color = Color( 0xFF9B0C3F),
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(

                        onClick = {

                            isSuccess = true
                            actionMessage = steps[currentStep].successText

                            if (currentStep == steps.lastIndex) {
                                completed = true
                            } else {
                                currentStep++
                            }
                        },

                        modifier = Modifier.fillMaxWidth(),

                        shape = RoundedCornerShape(18.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7C3AED)
                        )

                    ) {

                        Text(
                            "Выполнить действие",
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    AnimatedVisibility(
                        visible = actionMessage.isNotEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {

                        Text(
                            actionMessage,
                            color = if (isSuccess) Color(0xFF2E8058) else Color( 0xFF9B0C3F),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}