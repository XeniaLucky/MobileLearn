package com.example.diplom2.screen.dop_content.gameMedium.GameCleaner

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val BgColor = Color(0xFF1E1A2F)
val CardColor = Color(0xFF2A2438)
val AccentColor = Color(0xFF9C27B0)
val TextPrimary = Color.White
val TextSecondary = Color(0xFFB0BEC5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameCleanerScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: CleanerViewModel = viewModel(factory = CleanerViewModelFactory(context))
    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    val density = LocalDensity.current

    val currentTaskIndex by viewModel.currentTaskIndex.collectAsStateWithLifecycle()
    val score by viewModel.score.collectAsStateWithLifecycle()
    val currentFile by viewModel.currentFile.collectAsStateWithLifecycle()
    val robotMessage by viewModel.robotMessage.collectAsStateWithLifecycle()
    val isGameFinished by viewModel.isGameFinished.collectAsStateWithLifecycle()
    val levelFiles by viewModel.levelFiles.collectAsStateWithLifecycle()
    val activeZones by viewModel.activeZones.collectAsStateWithLifecycle()
    val isExiting by viewModel.isExiting.collectAsStateWithLifecycle()

    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    var isDragging by remember { mutableStateOf(false) }
    var draggedOverZone by remember { mutableStateOf<DropZoneType?>(null) }
    var showIntroDialog by remember { mutableStateOf(true) }

    val navigationBarHeight = with(density) { WindowInsets.navigationBars.getBottom(density).toDp() }

    LaunchedEffect(robotMessage) {
        if (robotMessage != null) {
            delay(3500)
            viewModel.clearRobotMessage()
        }
    }

    LaunchedEffect(currentFile?.id) {
        offsetX.snapTo(0f)
        offsetY.snapTo(0f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("🧹 Уровень $currentTaskIndex", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Файлов осталось: ${levelFiles.size}", fontSize = 13.sp, color = TextSecondary)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = AccentColor)
                    }
                },
                actions = {
                    Text(
                        "⭐ $score",
                        color = Color(0xFFFFD700),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    IconButton(onClick = { viewModel.resetGame() }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Перезапустить игру",
                            tint = TextPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgColor, titleContentColor = TextPrimary)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(bottom = navigationBarHeight)
                .background(BgColor)
        ) {
            if (isGameFinished) {
                CleanerCertificateScreen(onRestart = { viewModel.resetGame(); navController.popBackStack() })
            } else if (currentFile != null) {
                DragAndDropWorkspace(
                    file = currentFile!!,
                    isDragging = isDragging,
                    isExiting = isExiting,
                    offsetX = offsetX.value,
                    offsetY = offsetY.value,
                    draggedOverZone = draggedOverZone,
                    activeZones = activeZones,
                    onDragStart = {
                        isDragging = true
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            offsetX.snapTo(offsetX.value + dragAmount.x)
                            offsetY.snapTo(offsetY.value + dragAmount.y)

                            draggedOverZone = when {
                                offsetY.value > 150 && offsetX.value < -60 -> activeZones.getOrNull(0)?.type
                                offsetY.value > 150 && offsetX.value > 60 -> activeZones.getOrNull(1)?.type
                                offsetY.value > 150 -> activeZones.getOrNull(0)?.type
                                else -> null
                            }
                        }
                    },
                    onDragEnd = {
                        isDragging = false
                        val zone = draggedOverZone
                        draggedOverZone = null

                        if (zone != null) {
                            val success = viewModel.processDrop(currentFile!!, zone)
                            if (!success) {
                                scope.launch {
                                    offsetX.animateTo(0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                                    offsetY.animateTo(0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                                }
                            }
                        } else {
                            scope.launch {
                                offsetX.animateTo(0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                                offsetY.animateTo(0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                            }
                        }
                    },
                    onRobotClick = { viewModel.showHint(currentFile!!) },
                    robotMessage = robotMessage
                )
            } else {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = AccentColor)
            }

            // Инструкция при открытии игры
            if (showIntroDialog) {
                AlertDialog(
                    onDismissRequest = { },
                    containerColor = Color(0xFF1E1A2F),
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.School, contentDescription = null, tint = AccentColor, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Как играть", fontWeight = FontWeight.Bold, color = AccentColor, fontSize = 20.sp)
                        }
                    },
                    text = {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            Text("🧹 Перетаскивайте файлы в корзину или другие зоны.", color = TextPrimary, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("✅ Правильное действие принесёт +10 очков.", color = TextPrimary, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("❌ Ошибка отнимет 5 очков.", color = TextPrimary, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("💡 Нажмите на робота, если нужна подсказка.", color = TextSecondary, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Card(
                                colors = CardDefaults.cardColors(containerColor = AccentColor.copy(alpha = 0.2f)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    "Цель каждого уровня — очистить память, удалив мусор или выполнив нужное действие. Следуйте подсказкам робота!",
                                    modifier = Modifier.padding(12.dp),
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = { showIntroDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentColor),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Text("Понятно, начать!", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    },
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }
    }
}

@Composable
fun DragAndDropWorkspace(
    file: StorageItem,
    isDragging: Boolean,
    isExiting: Boolean,
    offsetX: Float,
    offsetY: Float,
    draggedOverZone: DropZoneType?,
    activeZones: List<ActiveZone>,
    onDragStart: () -> Unit,
    onDrag: (change: androidx.compose.ui.input.pointer.PointerInputChange, dragAmount: androidx.compose.ui.geometry.Offset) -> Unit,
    onDragEnd: () -> Unit,
    onRobotClick: () -> Unit,
    robotMessage: RobotMessage?
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.weight(0.65f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(72.dp).clip(CircleShape).background(AccentColor).clickable { onRobotClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Android, contentDescription = "Робот", tint = TextPrimary, modifier = Modifier.size(40.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Нажмите для подсказки", fontSize = 12.sp, color = TextSecondary)

                if (robotMessage != null) {
                    Card(
                        modifier = Modifier.padding(top = 8.dp).width(300.dp),
                        colors = CardDefaults.cardColors(containerColor = CardColor),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            robotMessage.text,
                            modifier = Modifier.padding(12.dp),
                            color = TextPrimary,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier.weight(1.2f).fillMaxWidth().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            val exitScale by androidx.compose.animation.core.animateFloatAsState(
                targetValue = if (isExiting) 0f else 1f,
                animationSpec = androidx.compose.animation.core.tween(durationMillis = 400)
            )
            val exitAlpha by androidx.compose.animation.core.animateFloatAsState(
                targetValue = if (isExiting) 0f else 1f,
                animationSpec = androidx.compose.animation.core.tween(durationMillis = 300)
            )

            Card(
                modifier = Modifier
                    .width(200.dp)
                    .scale(if (isDragging) 1.08f else exitScale)
                    .graphicsLayer { alpha = exitAlpha }
                    .graphicsLayer {
                        translationX = offsetX
                        translationY = offsetY
                        shadowElevation = if (isDragging) 20f else 4f
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { onDragStart() },
                            onDragEnd = { onDragEnd() },
                            onDragCancel = { onDragEnd() },
                            onDrag = { change, dragAmount -> onDrag(change, dragAmount) }
                        )
                    },
                colors = CardDefaults.cardColors(containerColor = CardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isDragging) 12.dp else 4.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val icon = when (file.type) {
                        "cache" -> Icons.Default.CleaningServices
                        "photo" -> Icons.Default.PhotoCamera
                        "video" -> Icons.Default.VideoCameraBack
                        "folder" -> Icons.Default.Folder
                        "app" -> Icons.Default.Apps
                        "hidden" -> Icons.Default.VisibilityOff
                        "process" -> Icons.Default.Memory
                        "setting" -> Icons.Default.Settings
                        "report" -> Icons.Default.Assessment
                        else -> Icons.Default.InsertDriveFile
                    }
                    Icon(icon, contentDescription = null, tint = AccentColor, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        file.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                    if (file.sizeMB > 0) Text("${file.sizeMB} МБ", fontSize = 12.sp, color = TextSecondary)
                    if (file.isDuplicate) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFFF9800)) {
                            Text("ДУБЛИКАТ", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.weight(0.9f).fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            activeZones.forEach { zone ->
                DropZone(
                    type = zone.type,
                    icon = zone.icon,
                    label = zone.label,
                    color = zone.color,
                    isHighlighted = draggedOverZone == zone.type,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun DropZone(
    type: DropZoneType,
    icon: ImageVector,
    label: String,
    color: Color,
    isHighlighted: Boolean,
    modifier: Modifier = Modifier
) {
    val currentScale = if (isHighlighted) 1.08f else 1f
    val borderColor = if (isHighlighted) color else color.copy(alpha = 0.3f)
    val bgColor = if (isHighlighted) color.copy(alpha = 0.25f) else CardColor
    val shape = RoundedCornerShape(20.dp)

    Card(
        modifier = modifier
            .scale(currentScale)
            .clip(shape)
            .border(3.dp, borderColor, shape),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isHighlighted) 8.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

@Composable
fun CleanerCertificateScreen(onRestart: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(BgColor), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CardColor),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.EmojiEvents, contentDescription = null, modifier = Modifier.size(72.dp), tint = Color(0xFFFFD700))
                Spacer(modifier = Modifier.height(12.dp))
                Text("Поздравляем!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                Text("Вы прошли все уровни!", fontSize = 16.sp, color = TextPrimary, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onRestart,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("В главное меню", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}