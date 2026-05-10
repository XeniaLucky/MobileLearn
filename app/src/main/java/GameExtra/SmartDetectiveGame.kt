package GameExpert

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class DetectiveCase(
    val title: String,
    val client: String,
    val description: String,
    val solutionTrigger: String,
    val solutionAction: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartDetectiveGame(navController: NavController) {

    var purchased by remember { mutableStateOf(false) }

    var selectedCase by remember {
        mutableStateOf<DetectiveCase?>(null)
    }

    var startedInvestigation by remember {
        mutableStateOf(false)
    }

    var solved by remember {
        mutableStateOf(false)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val glow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    val detectiveCases = listOf(

        DetectiveCase(
            title = "Дело №1: Пропавшие фото",
            client = "Алина",
            description = "После разрядки телефона исчезают фото из галереи.",
            solutionTrigger = "Батарея < 15%",
            solutionAction = "Скрыть папку DCIM"
        ),

        DetectiveCase(
            title = "Дело №2: Кража аккаунта",
            client = "Максим",
            description = "Кто-то автоматически отправляет SMS с кодами подтверждения.",
            solutionTrigger = "Новое SMS",
            solutionAction = "Переслать SMS злоумышленнику"
        ),

        DetectiveCase(
            title = "Дело №3: Шпионский Wi-Fi",
            client = "Егор",
            description = "После подключения к Wi-Fi появляются подозрительные скриншоты.",
            solutionTrigger = "Подключение Wi-Fi",
            solutionAction = "Сделать скриншот"
        ),

        DetectiveCase(
            title = "Дело №4: Ночная слежка",
            client = "София",
            description = "Телефон ночью передаёт геолокацию неизвестному серверу.",
            solutionTrigger = "Геолокация",
            solutionAction = "Отправить координаты"
        ),

        DetectiveCase(
            title = "Дело №5: Автоматический вирус",
            client = "Дмитрий",
            description = "После установки APK автоматически запускается вредоносный скрипт.",
            solutionTrigger = "Установка APK",
            solutionAction = "Запустить скрытый процесс"
        )
    )

    val triggers = listOf(
        "Батарея < 15%",
        "Новое SMS",
        "Подключение Wi-Fi",
        "Геолокация",
        "Установка APK"
    )

    val actions = listOf(
        "Скрыть папку DCIM",
        "Переслать SMS злоумышленнику",
        "Сделать скриншот",
        "Отправить координаты",
        "Запустить скрытый процесс"
    )

    var selectedTrigger by remember {
        mutableStateOf("")
    }

    var selectedAction by remember {
        mutableStateOf("")
    }

    Scaffold(

        containerColor = Color(0xFF07010D),

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        "Smart·Detective",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
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

        if (selectedCase == null) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {

                item {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF31135E),
                                        Color(0xFF12091F)
                                    )
                                ),
                                RoundedCornerShape(32.dp)
                            )
                            .border(
                                1.dp,
                                Color(0xFF7C3AED),
                                RoundedCornerShape(32.dp)
                            )
                            .padding(24.dp)
                    ) {

                        Column {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    Icons.Default.Search,
                                    null,
                                    tint = Color.Cyan,
                                    modifier = Modifier.size(50.dp)
                                )

                                Spacer(modifier = Modifier.width(14.dp))

                                Column {

                                    Text(
                                        "Smart·Detective",
                                        color = Color.White,
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        "Киберпанк-детектив",
                                        color = Color.Cyan,
                                        fontSize = 16.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                "Расследуй цифровые преступления через автоматизацию Android.",
                                color = Color.LightGray,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(26.dp))

                            ElevatedCard(
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = Color(0xFF1A1030)
                                )
                            ) {

                                Column(
                                    modifier = Modifier.padding(20.dp)
                                ) {

                                    Text(
                                        "Доступно бесплатно:",
                                        color = Color(0xFFB388FF),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        "• Дело №1: Пропавшие фото",
                                        color = Color.White,
                                        fontSize = 17.sp
                                    )

                                    Spacer(modifier = Modifier.height(18.dp))

                                    Text(
                                        "Premium включает ещё 4 сложных дела.",
                                        color = Color.LightGray,
                                        fontSize = 16.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(28.dp))

                            Button(

                                onClick = {
                                    selectedCase = detectiveCases[0]
                                },

                                modifier = Modifier.fillMaxWidth(),

                                shape = RoundedCornerShape(18.dp),

                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF7C3AED)
                                )

                            ) {

                                Text(
                                    "Попробовать бесплатное дело",
                                    fontSize = 18.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(

                                onClick = {
                                    purchased = true
                                },

                                modifier = Modifier.fillMaxWidth(),

                                shape = RoundedCornerShape(18.dp),

                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4C1D95)
                                )

                            ) {

                                Text(
                                    if (purchased)
                                        "Premium активирован"
                                    else
                                        "Купить полную версию — 399 ₽",
                                    fontSize = 18.sp
                                )
                            }

                            if (purchased) {

                                Spacer(modifier = Modifier.height(30.dp))

                                Text(
                                    "Все дела",
                                    color = Color.Cyan,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                detectiveCases.forEachIndexed { index, case ->

                                    ElevatedCard(

                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),

                                        colors = CardDefaults.elevatedCardColors(
                                            containerColor = Color(0xFF181325)
                                        ),

                                        onClick = {
                                            selectedCase = case
                                        }

                                    ) {

                                        Column(
                                            modifier = Modifier.padding(18.dp)
                                        ) {

                                            Text(
                                                case.title,
                                                color = Color.White,
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text(
                                                case.description,
                                                color = Color.LightGray,
                                                fontSize = 16.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } else {

            if (!startedInvestigation) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFF181325),
                                RoundedCornerShape(30.dp)
                            )
                            .padding(24.dp)
                    ) {

                        Column {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Surface(
                                    color = Color(0xFF7C3AED),
                                    shape = RoundedCornerShape(50)
                                ) {

                                    Box(
                                        modifier = Modifier.size(56.dp),
                                        contentAlignment = Alignment.Center
                                    ) {

                                        Text(
                                            selectedCase!!.client.first().toString(),
                                            color = Color.White,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {

                                    Text(
                                        selectedCase!!.client,
                                        color = Color.White,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        "Клиент",
                                        color = Color.Cyan
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                selectedCase!!.title,
                                color = Color(0xFFB388FF),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                selectedCase!!.description,
                                color = Color.White,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(30.dp))

                            Button(

                                onClick = {
                                    startedInvestigation = true
                                },

                                modifier = Modifier.fillMaxWidth(),

                                shape = RoundedCornerShape(18.dp),

                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF7C3AED)
                                )

                            ) {

                                Text(
                                    "Начать расследование",
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }

            } else {

                if (solved) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .background(Color(0xFF07010D)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            Icons.Default.Verified,
                            null,
                            tint = Color.Cyan,
                            modifier = Modifier
                                .size(100.dp)
                                .alpha(glow)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            "Дело раскрыто",
                            color = Color.White,
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            "Звание: Cyber Detective",
                            color = Color.Cyan,
                            fontSize = 22.sp
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(

                            onClick = {

                                selectedCase = null
                                startedInvestigation = false
                                solved = false
                                selectedTrigger = ""
                                selectedAction = ""
                            },

                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF7C3AED)
                            )

                        ) {

                            Text("Вернуться к делам")
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
                                selectedCase!!.title,
                                color = Color(0xFFB388FF),
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            ElevatedCard(
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = Color(0xFF181325)
                                ),
                                shape = RoundedCornerShape(26.dp)
                            ) {

                                Column(
                                    modifier = Modifier.padding(20.dp)
                                ) {

                                    Text(
                                        "УЛИКИ",
                                        color = Color.Cyan,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    listOf(
                                        "📂 Журнал файлов",
                                        "📩 Подозрительные SMS",
                                        "🌐 Сетевые события",
                                        "⚡ Логи автоматизации"
                                    ).forEach {

                                        Text(
                                            it,
                                            color = Color.White,
                                            fontSize = 17.sp,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(28.dp))

                            Text(
                                "Триггеры",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            LazyRow {

                                items(triggers.size) { index ->

                                    val trigger = triggers[index]

                                    FilterChip(

                                        selected = selectedTrigger == trigger,

                                        onClick = {
                                            selectedTrigger = trigger
                                        },

                                        label = {
                                            Text(trigger)
                                        },

                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(28.dp))

                            Text(
                                "Действия",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            LazyRow {

                                items(actions.size) { index ->

                                    val action = actions[index]

                                    FilterChip(

                                        selected = selectedAction == action,

                                        onClick = {
                                            selectedAction = action
                                        },

                                        label = {
                                            Text(action)
                                        },

                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(34.dp))

                            ElevatedCard(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = Color(0xFF24163A)
                                ),
                                shape = RoundedCornerShape(28.dp)
                            ) {

                                Column(
                                    modifier = Modifier.padding(24.dp)
                                ) {

                                    Text(
                                        "Схема автоматизации",
                                        color = Color.Cyan,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {

                                        Surface(
                                            shape = RoundedCornerShape(18.dp),
                                            color = Color(0xFF31135E)
                                        ) {

                                            Text(
                                                if (selectedTrigger.isEmpty())
                                                    "Триггер"
                                                else
                                                    selectedTrigger,
                                                color = Color.White,
                                                modifier = Modifier.padding(16.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Icon(
                                            Icons.Default.ArrowForward,
                                            null,
                                            tint = Color.Cyan,
                                            modifier = Modifier.alpha(glow)
                                        )

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Surface(
                                            shape = RoundedCornerShape(18.dp),
                                            color = Color(0xFF31135E)
                                        ) {

                                            Text(
                                                if (selectedAction.isEmpty())
                                                    "Действие"
                                                else
                                                    selectedAction,
                                                color = Color.White,
                                                modifier = Modifier.padding(16.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(34.dp))

                            Button(

                                onClick = {

                                    if (
                                        selectedTrigger ==
                                        selectedCase!!.solutionTrigger
                                        &&
                                        selectedAction ==
                                        selectedCase!!.solutionAction
                                    ) {

                                        solved = true
                                    }
                                },

                                modifier = Modifier.fillMaxWidth(),

                                shape = RoundedCornerShape(18.dp),

                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF7C3AED)
                                )

                            ) {

                                Text(
                                    "Запустить расследование",
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}