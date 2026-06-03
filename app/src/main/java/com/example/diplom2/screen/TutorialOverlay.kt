package com.example.diplom2.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

data class TutorialStep(
    val targetKey: String,
    val title: String,
    val text: String,
    val icon: ImageVector
)

@Composable
fun TutorialOverlay(
    steps: List<TutorialStep>,
    currentStep: Int,
    elementBounds: Map<String, Rect>,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    scrollState: androidx.compose.foundation.ScrollState? = null,
    accentColor: Color = Color(0xFF9C27B0)   // цвет подсветки
) {
    if (currentStep >= steps.size) return
    val step = steps[currentStep]
    val targetBounds = elementBounds[step.targetKey]
    val density = LocalDensity.current
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val screenWidthPx = displayMetrics.widthPixels.toFloat()
    val screenHeightPx = displayMetrics.heightPixels.toFloat()

    // Автопрокрутка к элементу
    LaunchedEffect(currentStep, targetBounds) {
        if (targetBounds != null && scrollState != null) {
            val targetY = targetBounds.top - 100 // отступ сверху, чтобы не впритык
            scrollState.animateScrollTo(targetY.roundToInt())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(100f)
            .background(Color.Black.copy(alpha = 0.75f))
            .clickable(enabled = false) {}
    ) {
        if (targetBounds != null) {
            val paddingPx = with(density) { 8.dp.toPx() }
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (targetBounds.left - paddingPx).roundToInt(),
                            (targetBounds.top - paddingPx).roundToInt()
                        )
                    }
                    .width(with(density) { (targetBounds.width + paddingPx * 2).toDp() })
                    .height(with(density) { (targetBounds.height + paddingPx * 2).toDp() })
                    .border(
                        width = 3.dp,
                        brush = Brush.horizontalGradient(listOf(accentColor, accentColor.copy(alpha = 0.6f))),
                        shape = RoundedCornerShape(12.dp)
                    )
            )
        }

        CoachMarkCard(
            step = step,
            stepIndex = currentStep,
            totalSteps = steps.size,
            targetBounds = targetBounds,
            screenWidthPx = screenWidthPx,
            screenHeightPx = screenHeightPx,
            onNext = onNext,
            onSkip = onSkip,
            accentColor = accentColor
        )
    }
}

@Composable
fun CoachMarkCard(
    step: TutorialStep,
    stepIndex: Int,
    totalSteps: Int,
    targetBounds: Rect?,
    screenWidthPx: Float,
    screenHeightPx: Float,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    accentColor: Color
) {
    val density = LocalDensity.current
    val cardWidthPx = with(density) { 280.dp.toPx() }
    val cardHeightPx = with(density) { 120.dp.toPx() }
    val arrowSizePx = with(density) { 12.dp.toPx() }
    val gapPx = with(density) { 12.dp.toPx() }
    val showAbove: Boolean
    val xOffsetPx: Float
    val yOffsetPx: Float

    if (targetBounds != null) {
        val targetCenterX = (targetBounds.left + targetBounds.right) / 2
        val targetCenterY = (targetBounds.top + targetBounds.bottom) / 2
        xOffsetPx = (targetCenterX - cardWidthPx / 2).coerceIn(16f, screenWidthPx - cardWidthPx - 16f)

        val spaceAbove = targetBounds.top
        val spaceBelow = screenHeightPx - targetBounds.bottom

        if (spaceBelow >= cardHeightPx + arrowSizePx + gapPx + 16f) {
            showAbove = false
            yOffsetPx = targetBounds.bottom + gapPx + arrowSizePx
        } else if (spaceAbove >= cardHeightPx + arrowSizePx + gapPx + 16f) {
            showAbove = true
            yOffsetPx = targetBounds.top - gapPx - arrowSizePx - cardHeightPx
        } else {
            showAbove = targetCenterY > screenHeightPx / 2
            yOffsetPx = if (showAbove) {
                targetBounds.top - gapPx - arrowSizePx - cardHeightPx
            } else {
                targetBounds.bottom + gapPx + arrowSizePx
            }
        }
    } else {
        showAbove = false
        xOffsetPx = (screenWidthPx - cardWidthPx) / 2
        yOffsetPx = (screenHeightPx - cardHeightPx) / 2
    }

    val arrowXOffsetPx = if (targetBounds != null) {
        val targetCenterX = (targetBounds.left + targetBounds.right) / 2
        (targetCenterX - xOffsetPx).coerceIn(24f, cardWidthPx - 24f)
    } else {
        cardWidthPx / 2
    }

    Box(
        modifier = Modifier.offset { IntOffset(xOffsetPx.roundToInt(), yOffsetPx.roundToInt()) }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (!showAbove) {
                Box(
                    modifier = Modifier
                        .offset { IntOffset(arrowXOffsetPx.roundToInt() - arrowSizePx.roundToInt(), 0) }
                        .size(0.dp)
                        .drawArrowDown(arrowSizePx, accentColor)
                )
                Spacer(modifier = Modifier.height(arrowSizePx.dp))
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2438)),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(12.dp),
                modifier = Modifier.width(with(density) { cardWidthPx.toDp() })
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(step.icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(step.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.weight(1f))
                        Surface(shape = RoundedCornerShape(10.dp), color = accentColor) {
                            Text(
                                "${stepIndex + 1}/$totalSteps",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(step.text, fontSize = 14.sp, color = Color(0xFFE0E0E0), lineHeight = 20.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = onSkip, contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp), modifier = Modifier.height(32.dp)) {
                            Text("Пропустить", color = Color(0xFF90A4AE), fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Button(
                            onClick = onNext,
                            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(if (stepIndex + 1 < totalSteps) "Далее →" else "Начать!", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            if (showAbove) {
                Spacer(modifier = Modifier.height(arrowSizePx.dp))
                Box(
                    modifier = Modifier
                        .offset { IntOffset(arrowXOffsetPx.roundToInt() - arrowSizePx.roundToInt(), 0) }
                        .size(0.dp)
                        .drawArrowUp(arrowSizePx, accentColor)
                )
            }
        }
    }
}

fun Modifier.drawArrowDown(sizePx: Float, color: Color): Modifier = this.then(
    Modifier.drawBehind {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(sizePx, 0f)
            lineTo(sizePx / 2, sizePx)
            close()
        }
        drawPath(path, color)
    }
)

fun Modifier.drawArrowUp(sizePx: Float, color: Color): Modifier = this.then(
    Modifier.drawBehind {
        val path = Path().apply {
            moveTo(sizePx / 2, 0f)
            lineTo(sizePx, sizePx)
            lineTo(0f, sizePx)
            close()
        }
        drawPath(path, color)
    }
)