package com.example.diplom2.screen.dop_content.lessons_medium

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.diplom2.screen.activateLessonMedium
import com.example.diplom2.screen.getActiveLessonMedium
import com.example.diplom2.screen.getLastStepMedium
import com.example.diplom2.screen.saveLastStepMedium
import com.example.diplom2.screen.updateLessonProgressMedium

private const val TAG = "LessonDataTransfer"

// ==================== ФУНКЦИИ СОХРАНЕНИЯ ====================

private fun getLessonProgressPrefs(context: Context, userId: Long) =
    context.getSharedPreferences("lesson_progress_$userId", Context.MODE_PRIVATE)

private fun saveSelectedMethod(context: Context, userId: Long, method: String?) {
    try {
        getLessonProgressPrefs(context, userId).edit().putString("selected_method", method).apply()
    } catch (e: Exception) {
        Log.e(TAG, "Ошибка сохранения selectedMethod: ${e.message}")
    }
}

private fun getSelectedMethod(context: Context, userId: Long): String? {
    return try {
        getLessonProgressPrefs(context, userId).getString("selected_method", null)
    } catch (e: Exception) {
        Log.e(TAG, "Ошибка чтения selectedMethod: ${e.message}")
        null
    }
}

private fun saveGoogleSubStep(context: Context, userId: Long, subStep: Int) {
    try {
        getLessonProgressPrefs(context, userId).edit().putInt("google_substep", subStep).apply()
    } catch (e: Exception) {
        Log.e(TAG, "Ошибка сохранения google_substep: ${e.message}")
    }
}

private fun getGoogleSubStep(context: Context, userId: Long): Int {
    return try {
        getLessonProgressPrefs(context, userId).getInt("google_substep", 0)
    } catch (e: Exception) {
        Log.e(TAG, "Ошибка чтения google_substep: ${e.message}")
        0
    }
}

private fun saveUsbSubStep(context: Context, userId: Long, subStep: Int) {
    try {
        getLessonProgressPrefs(context, userId).edit().putInt("usb_substep", subStep).apply()
    } catch (e: Exception) {
        Log.e(TAG, "Ошибка сохранения usb_substep: ${e.message}")
    }
}

private fun getUsbSubStep(context: Context, userId: Long): Int {
    return try {
        getLessonProgressPrefs(context, userId).getInt("usb_substep", 0)
    } catch (e: Exception) {
        Log.e(TAG, "Ошибка чтения usb_substep: ${e.message}")
        0
    }
}

private fun saveLessonScore(context: Context, userId: Long, lessonKey: String, score: Int) {
    try {
        getLessonProgressPrefs(context, userId).edit().putInt("${lessonKey}_score", score).apply()
    } catch (e: Exception) {
        Log.e(TAG, "Ошибка сохранения score: ${e.message}")
    }
}

private fun getLessonScore(context: Context, userId: Long, lessonKey: String): Int {
    return try {
        getLessonProgressPrefs(context, userId).getInt("${lessonKey}_score", 0)
    } catch (e: Exception) {
        Log.e(TAG, "Ошибка чтения score: ${e.message}")
        0
    }
}

// ==================== ОСНОВНОЙ ЭКРАН ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDataTransferScreen(navController: NavController, userId: Long) {
    val context = LocalContext.current
    val lessonKey = "game_datatransfer"
    val totalSteps = 6

    val activeLesson = getActiveLessonMedium(context, userId)
    val savedStep = if (activeLesson == lessonKey) getLastStepMedium(context, userId, lessonKey) else 0

    var step by remember { mutableIntStateOf(savedStep) }
    var selectedMethod by remember { mutableStateOf(getSelectedMethod(context, userId)) }
    var googleSubStep by remember { mutableIntStateOf(getGoogleSubStep(context, userId)) }
    var usbSubStep by remember { mutableIntStateOf(getUsbSubStep(context, userId)) }
    var score by remember { mutableIntStateOf(getLessonScore(context, userId, lessonKey)) }
    var isSaving by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            activateLessonMedium(context, userId, lessonKey)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка активации урока: ${e.message}")
        }
    }

    LaunchedEffect(step) {
        isSaving = true
        try {
            val progress = if (step >= totalSteps - 1) 1f else step.toFloat() / (totalSteps - 1)
            updateLessonProgressMedium(context, userId, progress)
            saveLastStepMedium(context, userId, lessonKey, step)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка сохранения шага/прогресса: ${e.message}")
        } finally {
            isSaving = false
        }
    }

    LaunchedEffect(selectedMethod) {
        try { saveSelectedMethod(context, userId, selectedMethod) }
        catch (e: Exception) { Log.e(TAG, "Ошибка сохранения метода: ${e.message}") }
    }

    LaunchedEffect(googleSubStep) {
        try { saveGoogleSubStep(context, userId, googleSubStep) }
        catch (e: Exception) { Log.e(TAG, "Ошибка сохранения googleSubStep: ${e.message}") }
    }

    LaunchedEffect(usbSubStep) {
        try { saveUsbSubStep(context, userId, usbSubStep) }
        catch (e: Exception) { Log.e(TAG, "Ошибка сохранения usbSubStep: ${e.message}") }
    }

    LaunchedEffect(score) {
        try { saveLessonScore(context, userId, lessonKey, score) }
        catch (e: Exception) { Log.e(TAG, "Ошибка сохранения score: ${e.message}") }
    }

    fun onBackPressed() {
        when (step) {
            0 -> navController.popBackStack()
            1 -> step = 0
            2 -> {
                when (selectedMethod) {
                    "google" -> {
                        if (googleSubStep > 0) googleSubStep--
                        else step = 1
                    }
                    "usb" -> {
                        if (usbSubStep > 0) usbSubStep--
                        else step = 1
                    }
                }
            }
            5 -> step = 1
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Перенос данных", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2C5F6E))
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (step) {
                0 -> Step0Preparation(
                    userId = userId,
                    score = score,
                    onScoreUpdate = { newScore -> score = newScore },
                    onNext = { newScore ->
                        score = newScore
                        step = 1
                    }
                )
                1 -> Step1MethodChoice(
                    onMethodSelected = { method ->
                        selectedMethod = method
                        step = 2
                    }
                )
                2 -> {
                    when (selectedMethod) {
                        "google" -> GoogleMethodScreen(
                            userId = userId,
                            subStep = googleSubStep,
                            onSubStepChange = { newSubStep -> googleSubStep = newSubStep },
                            score = score,
                            onScoreUpdate = { newScore -> score = newScore },
                            onComplete = { step = 5 }
                        )
                        "usb" -> UsbMethodScreen(
                            userId = userId,
                            subStep = usbSubStep,
                            onSubStepChange = { newSubStep -> usbSubStep = newSubStep },
                            score = score,
                            onScoreUpdate = { newScore -> score = newScore },
                            onComplete = { step = 5 }
                        )
                    }
                }
                5 -> FinalStepScreen(
                    score = score,
                    onFinish = { navController.popBackStack() }
                )
            }
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp).align(Alignment.Center),
                    color = Color(0xFF2C5F6E)
                )
            }
        }
    }
}