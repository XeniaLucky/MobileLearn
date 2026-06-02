package GameExpert

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Star
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

data class FirmwarePart(
    val name: String,
    val compatibility: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBuilderGame(navController: NavController) {

    var purchased by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val glow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    if (!purchased) {

        Scaffold(

            containerColor = Color(0xFF09020A),

            topBar = {

                TopAppBar(

                    title = {
                        Text(
                            "Твой кастом",
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {

                item {

                    ElevatedCard(
                        shape = RoundedCornerShape(30.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = Color(0xFF181325)
                        )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    Icons.Default.Memory,
                                    null,
                                    tint = Color(0xFFB388FF),
                                    modifier = Modifier.size(48.dp)
                                )

                                Spacer(modifier = Modifier.width(14.dp))

                                Text(
                                    "Твой кастом",
                                    color = Color.White,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                "Создай идеальную Android-прошивку своей мечты.",
                                color = Color.LightGray,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                "Возможности:",
                                color = Color(0xFFB388FF),
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            listOf(
                                "Выбор ядра",
                                "Настройка GApps",
                                "Моды и твики",
                                "Совместимость компонентов",
                                "Оценка стабильности",
                                "Симуляция прошивки"
                            ).forEach {

                                Text(
                                    "• $it",
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
                                    "Купить за 499 ₽",
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }

    } else {

        val phones = listOf(
            "Pixel 8",
            "Xiaomi 13",
            "Galaxy S24"
        )

        val kernels = listOf(
            FirmwarePart(
                "Stock Kernel",
                listOf("Pixel 8", "Galaxy S24")
            ),
            FirmwarePart(
                "Franco Kernel",
                listOf("Pixel 8")
            ),
            FirmwarePart(
                "DarkCore",
                listOf("Xiaomi 13")
            )
        )

        val gapps = listOf(
            FirmwarePart(
                "OpenGApps Full",
                phones
            ),
            FirmwarePart(
                "MindTheGApps",
                listOf("Pixel 8", "Galaxy S24")
            ),
            FirmwarePart(
                "NikGApps",
                listOf("Xiaomi 13")
            )
        )

        val mods = listOf(
            FirmwarePart(
                "Dolby Atmos",
                listOf("Galaxy S24")
            ),
            FirmwarePart(
                "ViperFX",
                phones
            ),
            FirmwarePart(
                "Pixel Launcher",
                listOf("Pixel 8")
            )
        )

        var selectedPhone by remember {
            mutableStateOf(phones[0])
        }

        var selectedKernel by remember {
            mutableStateOf(kernels[0])
        }

        var selectedGapps by remember {
            mutableStateOf(gapps[0])
        }

        var selectedMod by remember {
            mutableStateOf(mods[0])
        }

        var flashed by remember {
            mutableStateOf(false)
        }

        var stars by remember {
            mutableIntStateOf(0)
        }

        var stabilityText by remember {
            mutableStateOf("")
        }

        Scaffold(

            containerColor = Color(0xFF09020A),

            topBar = {

                TopAppBar(

                    title = {
                        Text(
                            "Твой кастом",
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
                                        Color(0xFF2A1459),
                                        Color(0xFF12091F)
                                    )
                                ),
                                RoundedCornerShape(30.dp)
                            )
                            .padding(24.dp)
                    ) {

                        Column {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    Icons.Default.PhoneAndroid,
                                    null,
                                    tint = Color(0xFFB388FF),
                                    modifier = Modifier.size(46.dp)
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    "Конструктор прошивки",
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                "Модель устройства",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row {

                                phones.forEach { phone ->

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

                            fun CompatibilityText(part: FirmwarePart): Pair<String, Color> {

                                return if (
                                    part.compatibility.contains(selectedPhone)
                                ) {
                                    "Совместимо" to Color(0xFF2E8058)
                                } else {
                                    "Несовместимо" to Color( 0xFF9B0C3F)
                                }
                            }

                            @Composable
                            fun FirmwareSelector(
                                title: String,
                                parts: List<FirmwarePart>,
                                selected: FirmwarePart,
                                onSelect: (FirmwarePart) -> Unit
                            ) {

                                Text(
                                    title,
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                parts.forEach { part ->

                                    val compatibility = CompatibilityText(part)

                                    ElevatedCard(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp),

                                        colors = CardDefaults.elevatedCardColors(
                                            containerColor = if (
                                                selected == part
                                            ) {
                                                Color(0xFF372063)
                                            } else {
                                                Color(0xFF181325)
                                            }
                                        ),

                                        onClick = {
                                            onSelect(part)
                                        }
                                    ) {

                                        Column(
                                            modifier = Modifier.padding(16.dp)
                                        ) {

                                            Text(
                                                part.name,
                                                color = Color.White,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold
                                            )

                                            Spacer(modifier = Modifier.height(6.dp))

                                            Text(
                                                compatibility.first,
                                                color = compatibility.second,
                                                fontSize = 15.sp
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))
                            }

                            FirmwareSelector(
                                "Ядро",
                                kernels,
                                selectedKernel
                            ) {
                                selectedKernel = it
                            }

                            FirmwareSelector(
                                "GApps",
                                gapps,
                                selectedGapps
                            ) {
                                selectedGapps = it
                            }

                            FirmwareSelector(
                                "Моды",
                                mods,
                                selectedMod
                            ) {
                                selectedMod = it
                            }

                            Button(

                                onClick = {

                                    flashed = true

                                    val compatible =
                                        selectedKernel.compatibility.contains(selectedPhone)
                                                &&
                                                selectedGapps.compatibility.contains(selectedPhone)
                                                &&
                                                selectedMod.compatibility.contains(selectedPhone)

                                    if (compatible) {

                                        stars = (4..5).random()
                                        stabilityText =
                                            "Прошивка стабильна. Система работает идеально."

                                    } else {

                                        stars = (1..2).random()
                                        stabilityText =
                                            "Обнаружены конфликты компонентов."
                                    }
                                },

                                modifier = Modifier.fillMaxWidth(),

                                shape = RoundedCornerShape(18.dp),

                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF7C3AED)
                                )

                            ) {

                                Text(
                                    "Прошить устройство",
                                    fontSize = 18.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            AnimatedVisibility(
                                visible = flashed,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {

                                    CircularProgressIndicator(
                                        color = Color(0xFFB388FF),
                                        modifier = Modifier.alpha(glow)
                                    )

                                    Spacer(modifier = Modifier.height(24.dp))

                                    Text(
                                        "Сборка завершена",
                                        color = Color.White,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(18.dp))

                                    Row {

                                        repeat(stars) {

                                            Icon(
                                                Icons.Default.Star,
                                                null,
                                                tint = Color.Yellow,
                                                modifier = Modifier.size(34.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(18.dp))

                                    Text(
                                        stabilityText,
                                        color = if (stars >= 4)
                                            Color(0xFF2E8058)
                                        else
                                            Color( 0xFF9B0C3F),
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
}