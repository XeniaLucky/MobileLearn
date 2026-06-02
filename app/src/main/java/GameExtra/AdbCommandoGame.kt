package GameExpert

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class AdbMission(
    val level: Int,
    val title: String,
    val task: String,
    val command: String,
    val difficulty: String,
    val reward: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdbCommandoGame(navController: NavController) {

    val missions = listOf(

        AdbMission(
            1,
            "Подключение устройства",
            "Показать список подключённых устройств",
            "adb devices",
            "Easy",
            10
        ),

        AdbMission(
            2,
            "Перезагрузка",
            "Перезагрузить устройство",
            "adb reboot",
            "Easy",
            10
        ),

        AdbMission(
            3,
            "Fastboot",
            "Перезагрузить в bootloader",
            "adb reboot bootloader",
            "Medium",
            15
        ),

        AdbMission(
            4,
            "APK Install",
            "Установить приложение app.apk",
            "adb install app.apk",
            "Medium",
            20
        ),

        AdbMission(
            5,
            "Удаление APK",
            "Удалить пакет com.app.test",
            "adb uninstall com.app.test",
            "Hard",
            25
        ),

        AdbMission(
            6,
            "Logcat",
            "Открыть системные логи",
            "adb logcat",
            "Hard",
            30
        ),

        AdbMission(
            7,
            "Screenshot",
            "Сделать скриншот",
            "adb shell screencap /sdcard/screen.png",
            "Expert",
            35
        ),

        AdbMission(
            8,
            "Battery Stats",
            "Показать статистику батареи",
            "adb shell dumpsys battery",
            "Expert",
            40
        )
    )

    var currentMission by remember {
        mutableIntStateOf(0)
    }

    var score by remember {
        mutableIntStateOf(0)
    }

    var level by remember {
        mutableIntStateOf(1)
    }

    var input by remember {
        mutableStateOf("")
    }

    var terminalOutput by remember {
        mutableStateOf("ADB Terminal Initialized...")
    }

    var completed by remember {
        mutableStateOf(false)
    }

    var showHint by remember {
        mutableStateOf(false)
    }

    val mission = missions[currentMission]

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Scaffold(

        containerColor = Color(0xFF07010D),

        topBar = {

            TopAppBar(

                title = {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            Icons.Default.Terminal,
                            null,
                            tint = Color(0xFF00E5FF)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            "ADB-Командо",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
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
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF12091F),
                                Color(0xFF07010D)
                            )
                        )
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    Icons.Default.Verified,
                    null,
                    tint = Color.Cyan,
                    modifier = Modifier
                        .size(120.dp)
                        .alpha(pulse)
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    "Вы мастер ADB",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    "Финальный счёт: $score",
                    color = Color.Cyan,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(40.dp))

                Button(

                    onClick = {
                        navController.popBackStack()
                    },

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7C3AED)
                    ),

                    shape = RoundedCornerShape(18.dp)

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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        ElevatedCard(
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = Color(0xFF181325)
                            )
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Text(
                                    "Уровень",
                                    color = Color.LightGray
                                )

                                Text(
                                    "$level",
                                    color = Color.Cyan,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        ElevatedCard(
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = Color(0xFF181325)
                            )
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Text(
                                    "Очки",
                                    color = Color.LightGray
                                )

                                Text(
                                    "$score",
                                    color = Color(0xFFFFD700),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    LinearProgressIndicator(
                        progress = {
                            (currentMission + 1).toFloat() / missions.size
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        color = Color.Cyan,
                        trackColor = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF22113F),
                                        Color(0xFF12091F)
                                    )
                                ),
                                RoundedCornerShape(30.dp)
                            )
                            .border(
                                1.dp,
                                Color(0xFF7C3AED),
                                RoundedCornerShape(30.dp)
                            )
                            .padding(24.dp)
                    ) {

                        Column {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    Icons.Default.Memory,
                                    null,
                                    tint = Color.Cyan,
                                    modifier = Modifier.size(44.dp)
                                )

                                Spacer(modifier = Modifier.width(14.dp))

                                Column {

                                    Text(
                                        mission.title,
                                        color = Color.White,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        mission.difficulty,
                                        color = when (mission.difficulty) {

                                            "Easy" -> Color(0xFF2E8058)
                                            "Medium" -> Color.Yellow
                                            "Hard" -> Color( 0xFF9B0C3F)
                                            else -> Color.Cyan
                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                mission.task,
                                color = Color.LightGray,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(26.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Color.Black,
                                        RoundedCornerShape(20.dp)
                                    )
                                    .padding(18.dp)
                            ) {

                                Column {

                                    Text(
                                        terminalOutput,
                                        color = Color(0xFF00FF9C),
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 15.sp
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    OutlinedTextField(

                                        value = input,

                                        onValueChange = {
                                            input = it
                                        },

                                        modifier = Modifier.fillMaxWidth(),

                                        label = {
                                            Text(
                                                "Введите ADB-команду"
                                            )
                                        },

                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color.Cyan,
                                            unfocusedBorderColor = Color.DarkGray,
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White
                                        ),

                                        shape = RoundedCornerShape(16.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier.height(170.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {

                                items(
                                    listOf(
                                        "adb",
                                        "devices",
                                        "reboot",
                                        "bootloader",
                                        "install",
                                        "uninstall",
                                        "logcat",
                                        "shell"
                                    )
                                ) { block ->

                                    ElevatedCard(

                                        onClick = {

                                            input += if (
                                                input.isEmpty()
                                            ) {
                                                block
                                            } else {
                                                " $block"
                                            }
                                        },

                                        colors = CardDefaults.elevatedCardColors(
                                            containerColor = Color(0xFF2B174A)
                                        )

                                    ) {

                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {

                                            Text(
                                                block,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Row {

                                Button(

                                    onClick = {

                                        if (
                                            input.trim() ==
                                            mission.command
                                        ) {

                                            terminalOutput =
                                                "✔ Command executed successfully"

                                            score += mission.reward

                                            level++

                                            if (
                                                currentMission ==
                                                missions.lastIndex
                                            ) {

                                                completed = true

                                            } else {

                                                currentMission++
                                            }

                                            input = ""

                                        } else {

                                            terminalOutput =
                                                "✖ Syntax error. Try again."
                                        }
                                    },

                                    modifier = Modifier.weight(1f),

                                    shape = RoundedCornerShape(18.dp),

                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF7C3AED)
                                    )

                                ) {

                                    Icon(
                                        Icons.Default.PlayArrow,
                                        null
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text("Запустить")
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Button(

                                    onClick = {
                                        showHint = !showHint
                                    },

                                    modifier = Modifier.weight(1f),

                                    shape = RoundedCornerShape(18.dp),

                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF31135E)
                                    )

                                ) {

                                    Icon(
                                        Icons.Default.Lightbulb,
                                        null
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text("Подсказка")
                                }
                            }

                            AnimatedVisibility(
                                visible = showHint,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {

                                Column {

                                    Spacer(modifier = Modifier.height(20.dp))

                                    Surface(
                                        shape = RoundedCornerShape(18.dp),
                                        color = Color(0xFF24163A)
                                    ) {

                                        Text(
                                            "💡 Команда начинается с adb",
                                            color = Color.Cyan,
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}