package com.example.diplom2.screen.dop_content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
// ----------------------------- БЕСПЛАТНЫЕ ИГРЫ -----------------------------

@Composable
fun TapRunGame(navController: NavController, onBack: () -> Unit) {
    var score by remember { mutableIntStateOf(0) }
    var timeLeft by remember { mutableIntStateOf(30) }
    var isPlaying by remember { mutableStateOf(false) }
    var bestScore by remember { mutableIntStateOf(loadBestScore("taprun")) }

    LaunchedEffect(isPlaying) {
        if (isPlaying && timeLeft > 0) {
            delay(1000)
            timeLeft--
            if (timeLeft == 0) {
                isPlaying = false
                if (score > bestScore) saveBestScore("taprun", score)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Тап-ран", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Время: $timeLeft с", fontSize = 20.sp, color = Color.White)
        Text("Счёт: $score", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
        Text("Рекорд: $bestScore", fontSize = 16.sp, color = Color.White.copy(0.7f))
        Spacer(modifier = Modifier.height(32.dp))

        if (!isPlaying && timeLeft == 0) {
            Button(onClick = {
                score = 0
                timeLeft = 30
                isPlaying = true
            }) {
                Text("Играть снова")
            }
        } else if (!isPlaying && timeLeft == 30) {
            Button(onClick = { isPlaying = true }) {
                Text("Начать игру")
            }
        } else {
            Button(
                onClick = { if (isPlaying) score++ },
                modifier = Modifier.size(150.dp),
                shape = RoundedCornerShape(75.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("ТАП!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onBack) { Text("Назад", color = Color.White) }
    }
}

@Composable
fun SwipeQuizGame(navController: NavController, onBack: () -> Unit) {
    val questions = listOf(
        "Как переключить приложение?" to listOf("Свайп вверх", "Свайп влево", "Нажать домой"),
        "Как открыть панель уведомлений?" to listOf("Свайп сверху вниз", "Свайп снизу вверх", "Двойной тап"),
        "Как закрыть приложение?" to listOf("Свайп вверх из списка", "Нажать назад", "Выключить экран")
    )
    var currentIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var finished by remember { mutableStateOf(false) }
    var bestScore by remember { mutableIntStateOf(loadBestScore("swipequiz")) }

    fun checkAnswer(selected: String) {
        if (finished) return
        val correct = when (currentIndex) {
            0 -> "Свайп вверх"
            1 -> "Свайп сверху вниз"
            2 -> "Свайп вверх из списка"
            else -> ""
        }
        if (selected == correct) score++
        if (currentIndex + 1 < questions.size) currentIndex++
        else {
            finished = true
            if (score > bestScore) saveBestScore("swipequiz", score)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A2E)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Свайп-квиз", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
        if (!finished) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(questions[currentIndex].first, fontSize = 22.sp, color = Color.White, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(24.dp))
            questions[currentIndex].second.forEach { answer ->
                Button(
                    onClick = { checkAnswer(answer) },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(answer, fontSize = 16.sp)
                }
            }
            Text("Счёт: $score/${questions.size}", fontSize = 18.sp, color = Color.White, modifier = Modifier.padding(16.dp))
        } else {
            Text("Игра окончена!", fontSize = 24.sp, color = Color.White)
            Text("Ваш счёт: $score/${questions.size}", fontSize = 20.sp, color = Color(0xFFFFD700))
            Text("Рекорд: $bestScore/${questions.size}", fontSize = 16.sp, color = Color.White.copy(0.7f))
            Button(onClick = {
                currentIndex = 0
                score = 0
                finished = false
            }) {
                Text("Сыграть снова")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onBack) { Text("Назад", color = Color.White) }
    }
}

@Composable
fun SettingsPuzzleGame(navController: NavController, onBack: () -> Unit) {
    val tasks = listOf(
        "Настройки → Звук → Громкость" to listOf("Звук", "Дисплей", "Батарея"),
        "Настройки → Безопасность → Блокировка экрана" to listOf("Безопасность", "Сеть", "Приложения"),
        "Настройки → О телефоне → Номер модели" to listOf("О телефоне", "Система", "Доступность")
    )
    var currentIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var finished by remember { mutableStateOf(false) }
    var bestScore by remember { mutableIntStateOf(loadBestScore("settingspuzzle")) }

    fun checkAnswer(selected: String) {
        if (finished) return
        val correct = tasks[currentIndex].first.split(" → ")[1].split(" → ")[0]
        if (selected == correct) score++
        if (currentIndex + 1 < tasks.size) currentIndex++
        else {
            finished = true
            if (score > bestScore) saveBestScore("settingspuzzle", score)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A2E)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Собери настройку", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
        if (!finished) {
            Spacer(modifier = Modifier.height(24.dp))
            Text("Куда нужно зайти?", fontSize = 20.sp, color = Color.White)
            Text(tasks[currentIndex].first, fontSize = 18.sp, color = Color(0xFFFFD700), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(24.dp))
            tasks[currentIndex].second.forEach { answer ->
                Button(
                    onClick = { checkAnswer(answer) },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(answer, fontSize = 16.sp)
                }
            }
            Text("Счёт: $score/${tasks.size}", fontSize = 18.sp, color = Color.White, modifier = Modifier.padding(16.dp))
        } else {
            Text("Поздравляем!", fontSize = 24.sp, color = Color.White)
            Text("Правильных ответов: $score/${tasks.size}", fontSize = 20.sp, color = Color(0xFFFFD700))
            Text("Рекорд: $bestScore/${tasks.size}", fontSize = 16.sp, color = Color.White.copy(0.7f))
            Button(onClick = {
                currentIndex = 0
                score = 0
                finished = false
            }) {
                Text("Пройти заново")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onBack) { Text("Назад", color = Color.White) }
    }
}

// ----------------------------- ПРЕМИУМ ИГРЫ (заглушки) -----------------------------
// ---------- ПЛАТНЫЕ ИГРЫ ----------

// 1. Продвинутый фотограф - симулятор ручных настроек камеры
@Composable
fun ProPhotographerGame(navController: NavController, onBack: () -> Unit) {
    var iso by remember { mutableIntStateOf(100) }
    var shutter by remember { mutableStateOf("1/100") }
    var aperture by remember { mutableStateOf("f/4") }
    var score by remember { mutableIntStateOf(0) }
    var taskIndex by remember { mutableIntStateOf(0) }
    var message by remember { mutableStateOf("") }

    val tasks = listOf(
        "Установите ISO 400" to { if (iso == 400) score++ },
        "Установите выдержку 1/200" to { if (shutter == "1/200") score++ },
        "Установите диафрагму f/2.8" to { if (aperture == "f/2.8") score++ }
    )

    fun nextTask() {
        if (taskIndex < tasks.size - 1) taskIndex++
        else message = "Поздравляем! Вы освоили ручные настройки! Счёт: $score/${tasks.size}"
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A2E)).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Продвинутый фотограф", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Задание: ${tasks[taskIndex].first}", fontSize = 20.sp, color = Color(0xFFFFD700), modifier = Modifier.padding(16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("ISO: $iso", fontSize = 18.sp, color = Color.White)
        Slider(value = iso.toFloat(), onValueChange = { iso = it.toInt() }, valueRange = 100f..3200f, steps = 6)
        Text("Выдержка: $shutter", fontSize = 18.sp, color = Color.White)
        Row {
            listOf("1/30", "1/60", "1/125", "1/200", "1/500").forEach { s ->
                Button(onClick = { shutter = s }, modifier = Modifier.padding(4.dp)) { Text(s) }
            }
        }
        Text("Диафрагма: $aperture", fontSize = 18.sp, color = Color.White)
        Row {
            listOf("f/1.8", "f/2.8", "f/4", "f/5.6").forEach { a ->
                Button(onClick = { aperture = a }, modifier = Modifier.padding(4.dp)) { Text(a) }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            tasks[taskIndex].second.invoke()
            nextTask()
        }) { Text("Проверить") }
        if (message.isNotEmpty()) Text(message, fontSize = 16.sp, color = Color(0xFFFFD700), modifier = Modifier.padding(16.dp))
        TextButton(onClick = onBack) { Text("Назад", color = Color.White) }
    }
}

// 2. Кибердетектив - обучение защите от мошенников
@Composable
fun CyberDetectiveGame(navController: NavController, onBack: () -> Unit) {
    var score by remember { mutableIntStateOf(0) }
    var currentQuestion by remember { mutableIntStateOf(0) }
    var resultMessage by remember { mutableStateOf("") }

    val questions = listOf(
        Triple("Что делать, если пришло SMS с просьбой перевести деньги другу?", listOf("Позвонить другу и уточнить", "Перевести сразу", "Игнорировать"), 0),
        Triple("Как распознать фишинговое письмо?", listOf("Проверить адрес отправителя", "Нажать на ссылку", "Ответить письмом"), 0),
        Triple("Что такое двухфакторная аутентификация?", listOf("Дополнительный код для входа", "Два пароля", "Отпечаток пальца"), 0)
    )

    fun checkAnswer(selectedIndex: Int) {
        if (selectedIndex == questions[currentQuestion].third) {
            score++
            resultMessage = "Верно!"
        } else {
            resultMessage = "Неверно. Правильный ответ: ${questions[currentQuestion].first.split("?")[0]} - ${questions[currentQuestion].second[questions[currentQuestion].third]}"
        }
        if (currentQuestion + 1 < questions.size) currentQuestion++
        else resultMessage += " Игра окончена! Вы набрали $score/${questions.size} баллов."
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A2E)).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Кибердетектив", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(questions[currentQuestion].first, fontSize = 20.sp, color = Color.White, modifier = Modifier.padding(16.dp))
        questions[currentQuestion].second.forEachIndexed { idx, answer ->
            Button(onClick = { checkAnswer(idx) }, modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                Text(answer)
            }
        }
        Text(resultMessage, fontSize = 16.sp, color = Color(0xFFFFD700), modifier = Modifier.padding(16.dp))
        TextButton(onClick = onBack) { Text("Назад", color = Color.White) }
    }
}

// 3. Жесты - сложные уровни на скорость
@Composable
fun GesturesGame(navController: NavController, onBack: () -> Unit) {
    var level by remember { mutableIntStateOf(1) }
    var score by remember { mutableIntStateOf(0) }
    var expectedGesture by remember { mutableStateOf("Свайп вверх") }
    var message by remember { mutableStateOf("") }

    val gestures = listOf("Свайп вверх", "Свайп вниз", "Свайп влево", "Свайп вправо", "Двойное касание", "Зажать")

    fun checkGesture(gesture: String) {
        if (gesture == expectedGesture) {
            score++
            if (level < 5) {
                level++
                expectedGesture = gestures.random()
                message = "Уровень $level! Выполните: $expectedGesture"
            } else {
                message = "Поздравляем! Вы прошли все уровни! Счёт: $score"
            }
        } else {
            message = "Неверно! Нужно: $expectedGesture"
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A2E)).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Жесты", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Уровень $level", fontSize = 24.sp, color = Color(0xFFFFD700))
        Text("Выполните жест: $expectedGesture", fontSize = 20.sp, color = Color.White, modifier = Modifier.padding(16.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.height(300.dp)) {
            items(gestures) { gesture ->
                Button(onClick = { checkGesture(gesture) }, modifier = Modifier.padding(8.dp).fillMaxWidth()) {
                    Text(gesture)
                }
            }
        }
        Text(message, fontSize = 16.sp, color = Color(0xFFFFD700), modifier = Modifier.padding(16.dp))
        TextButton(onClick = onBack) { Text("Назад", color = Color.White) }
    }
}

// ----------------------------- ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ ДЛЯ РЕКОРДОВ -----------------------------
// В реальном приложении замените на SharedPreferences или DataStore
private fun loadBestScore(game: String): Int {
    // Заглушка
    return 0
}
private fun saveBestScore(game: String, score: Int) {
    // Заглушка
}