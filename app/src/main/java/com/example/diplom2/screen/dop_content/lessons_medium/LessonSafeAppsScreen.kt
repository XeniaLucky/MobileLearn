package com.example.diplom2.screen.dop_content.lessons_medium

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.diplom2.screen.saveLessonProgress
import com.example.diplom2.screen.setActiveLessonMedium
import com.example.diplom2.screen.updateLessonProgressMedium
import kotlinx.coroutines.launch
import kotlinx.coroutines.launch
import com.example.diplom2.screen.saveLessonProgress
import com.example.diplom2.screen.saveLastStepMedium
import com.example.diplom2.screen.getLastStepMedium
import com.example.diplom2.screen.activateLessonMedium
import com.example.diplom2.screen.getActiveLessonMedium
import com.example.diplom2.screen.updateLessonProgressMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonSafeAppsScreen(navController: NavController, userId: Long) {
    var currentQuestion by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var showResult by remember { mutableStateOf(false) }
    var installApproved by remember { mutableStateOf(false) }
    var permissionsChecked by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lessonKey = "game_safeapps"
    val totalSteps = 4

    // Восстанавливаем шаг, если урок уже был активен
    var step by remember {
        val activeLesson = getActiveLessonMedium(context, userId)
        if (activeLesson == lessonKey) {
            mutableIntStateOf(getLastStepMedium(context, userId, lessonKey))
        } else {
            mutableIntStateOf(0)
        }
    }

    var brightness by remember { mutableFloatStateOf(0.8f) }
    var sync by remember { mutableStateOf(true) }
    var powerSaver by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Активируем урок (без сброса, если он уже активен)
    LaunchedEffect(Unit) {
        activateLessonMedium(context, userId, lessonKey)
    }

    // Обновляем прогресс и сохраняем шаг при каждом переходе
    LaunchedEffect(step) {
        val progress = if (step >= totalSteps - 1) 1f else step.toFloat() / (totalSteps - 1)
        updateLessonProgressMedium(context, userId, progress)
        saveLastStepMedium(context, userId, lessonKey, step)
    }

    val questions = listOf(
        Question("Какой магазин приложений является самым безопасным?", listOf("Любой сайт", "Google Play Маркет", "Магазин от незнакомца", "Пиратский сайт"), 1),
        Question("Что нужно проверить перед установкой приложения?", listOf("Только цену", "Отзывы, количество скачиваний, разрешения", "Цвет иконки", "Рейтинг разработчика"), 1),
        Question("Сколько должно быть скачиваний у безопасного приложения?", listOf("10-100", "Тысячи или миллионы", "0-10", "1 скачивание"), 1),
        Question("Что такое разрешения приложения?", listOf("Цена приложения", "Доступ к функциям телефона", "Название приложения", "Размер приложения"), 1),
        Question("Какое разрешение должно насторожить в приложении-фонарике?", listOf("Управление яркостью", "Доступ к контактам", "Включение вспышки", "Работа в фоне"), 1),
        Question("Что такое Google Play Protect?", listOf("Антивирус от Google", "Платный сервис", "Игра", "Лаунчер"), 0),
        Question("Что делать, если приложение просит подозрительные разрешения?", listOf("Согласиться", "Отказаться и удалить", "Игнорировать", "Поделиться с друзьями"), 1),
        Question("Какие отзывы должны быть у безопасного приложения?", listOf("Только положительные", "Много, в основном хорошие, есть конструктивная критика", "Нет отзывов", "Одни звёзды без текста"), 1),
        Question("Что такое «взломанное» приложение?", listOf("Бесплатная версия", "Приложение с изменённым кодом, часто с вирусами", "Демо-версия", "Бета-версия"), 1),
        Question("Почему опасны приложения из неизвестных источников?", listOf("Они дорогие", "Могут содержать вирусы и украсть данные", "Они не работают", "Они занимают много места"), 1),
        Question("Как часто нужно обновлять приложения?", listOf("Никогда", "Раз в год", "Регулярно, когда выходят обновления", "Только при покупке нового телефона"), 2),
        Question("Что делать с неиспользуемыми приложениями?", listOf("Оставить на память", "Удалить", "Переименовать", "Скрыть"), 1),
        Question("Можно ли устанавливать приложения по ссылке из SMS от незнакомца?", listOf("Да", "Нет, это опасно", "Только если ссылка красивая", "Если обещают приз"), 1),
        Question("Что такое фишинг?", listOf("Рыбалка", "Вид спорта", "Мошенничество для кражи данных", "Название приложения"), 2),
        Question("Какой пароль лучше использовать для аккаунтов?", listOf("123456", "qwerty", "Сложный, из букв, цифр и символов", "Дата рождения"), 2)
    )

    if (step == 0) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("🔒 Безопасность при установке приложений – ПОЛНОЕ РУКОВОДСТВО", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "📌 **1. Откуда можно устанавливать приложения?**\n\n" +
                                    "✅ **БЕЗОПАСНЫЕ источники:**\n" +
                                    "• Google Play Маркет – официальный магазин, приложения проходят автоматическую проверку на вирусы.\n" +
                                    "• Galaxy Store (Samsung), AppGallery (Huawei) – аналоги.\n" +
                                    "• Официальные сайты известных компаний (например, Telegram с официального сайта).\n\n" +
                                    "❌ **ОПАСНЫЕ источники:**\n" +
                                    "• Неизвестные сайты, предлагающие «взломанные» или «бесплатные» версии платных приложений.\n" +
                                    "• Ссылки в SMS, WhatsApp, Telegram от незнакомцев.\n" +
                                    "• Подозрительные магазины с похожими названиями (например, «Play Market» с другим логотипом).\n" +
                                    "• Торренты и файлообменники.\n\n" +
                                    "📌 **2. Как проверить приложение перед установкой?**\n\n" +
                                    "1️⃣ **Количество скачиваний:**\n" +
                                    "   • 10+ миллионов – скорее всего, надёжное приложение.\n" +
                                    "   • Меньше 1000 – стоит насторожиться (особенно если просит много разрешений).\n\n" +
                                    "2️⃣ **Отзывы пользователей:**\n" +
                                    "   • Читайте не только положительные, но и отрицательные – часто там пишут о реальных проблемах.\n" +
                                    "   • Если много жалоб на вирусы, рекламу или кражу данных – НЕ УСТАНАВЛИВАЙТЕ.\n\n" +
                                    "3️⃣ **Разработчик:**\n" +
                                    "   • Известная компания (Google, Microsoft, Adobe) – доверяйте.\n" +
                                    "   • Неизвестный «Ivanov Dev» – проверьте другие его приложения.\n\n" +
                                    "4️⃣ **Дата последнего обновления:**\n" +
                                    "   • Обновлялось в этом году – хорошо.\n" +
                                    "   • Не обновлялось 2-3 года – возможно, заброшено и небезопасно.\n\n" +
                                    "📌 **3. Разрешения приложений – что это и как проверять**\n\n" +
                                    "При установке приложение запрашивает доступ к функциям телефона. Это НОРМАЛЬНО:\n" +
                                    "• Мессенджеры: контакты, микрофон, камера, файлы.\n" +
                                    "• Карты: геолокация.\n" +
                                    "• Фоторедакторы: доступ к фото.\n" +
                                    "• Банки: SMS (для подтверждения операций).\n\n" +
                                    "⚠️ **ЭТО ДОЛЖНО НАСТОРОЖИТЬ (КРАСНЫЕ ФЛАГИ):**\n" +
                                    "• Приложение-фонарик просит доступ к контактам.\n" +
                                    "• Игра просит доступ к SMS или камере.\n" +
                                    "• Калькулятор просит доступ к геолокации.\n" +
                                    "• Приложение для погоды просит доступ к списку звонков.\n\n" +
                                    "📌 **4. Google Play Protect – ваш защитник**\n\n" +
                                    "Это встроенный антивирус от Google. Он автоматически сканирует приложения на наличие вредоносного кода.\n" +
                                    "Как включить: Настройки → Безопасность → Play Protect → Включить.\n" +
                                    "Раз в неделю заходите туда и запускайте сканирование вручную.\n\n" +
                                    "📌 **5. Что делать, если уже установили опасное приложение?**\n\n" +
                                    "1. НЕМЕДЛЕННО удалите его (зажмите иконку → Удалить).\n" +
                                    "2. Запустите антивирус (Kaspersky, Malwarebytes).\n" +
                                    "3. Поменяйте пароли от важных аккаунтов (почта, банк, соцсети).\n" +
                                    "4. Проверьте разрешения всех приложений и отзовите подозрительные.\n" +
                                    "5. Если телефон стал тормозить – сделайте сброс до заводских настроек.\n\n" +
                                    "📌 **6. Дополнительные советы**\n\n" +
                                    "• Включите двухфакторную аутентификацию в важных сервисах.\n" +
                                    "• Не используйте один пароль везде.\n" +
                                    "• Регулярно удаляйте приложения, которыми не пользуетесь.\n" +
                                    "• Обновляйте приложения – в обновлениях закрывают уязвимости.\n" +
                                    "• Отключайте «Установку из неизвестных источников» после использования.\n\n" +
                                    "🎯 **Теперь проверьте знания – ответьте на 15 вопросов!**",
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        )
                    }
                }
            }
            item {
                Button(
                    onClick = { step = 1 },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Начать тест (15 вопросов)", color = Color.White) }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        return
    }

    if (step == 1) {
        val question = questions[currentQuestion]
        val isLastQuestion = currentQuestion == questions.size - 1

        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Вопрос ${currentQuestion + 1} из ${questions.size}", fontSize = 14.sp, color = Color(0xFF2C5F6E))
            LinearProgressIndicator(progress = (currentQuestion + 1).toFloat() / questions.size, modifier = Modifier.fillMaxWidth(), color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(24.dp))

            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                Text(question.text, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))

            question.options.forEachIndexed { index, option ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable { if (!showResult) selectedAnswer = index },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = when {
                        showResult && index == question.correctAnswer -> Color(0xFFC8E6C9)
                        showResult && selectedAnswer == index && index != question.correctAnswer -> Color(0xFFFFCDD2)
                        selectedAnswer == index -> Color(0xFF2C5F6E).copy(alpha = 0.2f)
                        else -> Color.White
                    })
                ) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = selectedAnswer == index, onClick = { if (!showResult) selectedAnswer = index }, colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF2C5F6E)))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(option, fontSize = 16.sp)
                        if (showResult && index == question.correctAnswer) {
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.Green, modifier = Modifier.size(20.dp))
                        } else if (showResult && selectedAnswer == index && index != question.correctAnswer) {
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.Cancel, contentDescription = null, tint = Color.Red, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (!showResult && selectedAnswer != null) {
                Button(onClick = {
                    showResult = true
                    if (selectedAnswer == question.correctAnswer) score += 20 else score = (score - 5).coerceAtLeast(0)
                }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))) { Text("Проверить", color = Color.White) }
            } else if (showResult && !isLastQuestion) {
                Button(onClick = { currentQuestion++; selectedAnswer = null; showResult = false }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))) { Text("Следующий вопрос →", color = Color.White) }
            } else if (showResult && isLastQuestion) {
                Button(onClick = { step = 2 }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))) { Text("Закончить тест", color = Color.White) }
            }
            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF2C5F6E), modifier = Modifier.padding(top = 16.dp))
        }
        return
    }

    if (step == 2) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📱 Симуляция: установка подозрительного приложения", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Приложение «Супер Калькулятор» запрашивает доступ к контактам, SMS, камере и геолокации.", fontSize = 14.sp)
                    Text("Калькулятору такие разрешения НЕ НУЖНЫ!", fontSize = 14.sp, color = Color.Red)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Проверить разрешения (обнаружены лишние)", fontSize = 14.sp)
                Switch(checked = permissionsChecked, onCheckedChange = { permissionsChecked = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E)))
            }
            if (permissionsChecked) Text("✅ Правильно! Вы заметили подозрительные разрешения.", color = Color.Green, fontSize = 12.sp)

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Отказаться от установки и удалить приложение", fontSize = 14.sp)
                Switch(checked = installApproved, onCheckedChange = { installApproved = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E)))
            }
            if (installApproved) Text("✅ Отлично! Небезопасное приложение не установлено.", color = Color.Green, fontSize = 12.sp)

            if (permissionsChecked && installApproved) {
                Button(onClick = { step = 3 }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))) { Text("Готово", color = Color.White) }
            } else {
                Text("❌ Сначала проверьте разрешения и откажитесь от установки", color = Color.Red, fontSize = 12.sp, textAlign = TextAlign.Center)
            }
            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF2C5F6E))
        }
        return
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🎉 ПОЗДРАВЛЯЕМ! Вы прошли полный курс безопасности приложений!", fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Вы заработали $score очков.", color = Color(0xFF2C5F6E), fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("💡 Золотые правила безопасности:\n• Устанавливайте только из Google Play\n• Проверяйте отзывы и разрешения\n• Включите Play Protect\n• Удаляйте подозрительные приложения", fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    scope.launch {
                        saveLessonProgress(context, userId, "game_safeapps", true)
                        navController.popBackStack()
                    }
                }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))) { Text("Завершить", color = Color.White) }
            }
        }
    }
}

data class Question(val text: String, val options: List<String>, val correctAnswer: Int)