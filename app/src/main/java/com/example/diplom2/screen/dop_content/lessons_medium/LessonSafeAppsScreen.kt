package com.example.diplom2.screen.dop_content.lessons_medium

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.diplom2.screen.*
import kotlinx.coroutines.launch

private const val TAG = "LessonSafeApps"
private const val PREFS_NAME = "safe_apps_progress"

// Расширенный класс вопроса с пояснениями и подсказкой
data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswer: Int,
    val explanation: String,
    val optionsExplanations: List<String>,
    val hint: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonSafeAppsScreen(navController: NavController, userId: Long) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lessonKey = "game_safeapps"
    val totalSteps = 4

    // Функции сохранения/восстановления состояния
    fun saveInt(key: String, value: Int) {
        try {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putInt("${userId}_$key", value).apply()
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка сохранения $key: ${e.message}")
        }
    }

    fun getInt(key: String, default: Int): Int {
        return try {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getInt("${userId}_$key", default)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка чтения $key: ${e.message}")
            default
        }
    }

    fun saveBoolean(key: String, value: Boolean) {
        try {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putBoolean("${userId}_$key", value).apply()
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка сохранения $key: ${e.message}")
        }
    }

    fun getBoolean(key: String, default: Boolean): Boolean {
        return try {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean("${userId}_$key", default)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка чтения $key: ${e.message}")
            default
        }
    }

    // Восстановление прогресса урока
    val activeLesson = getActiveLessonMedium(context, userId)
    val savedStep = if (activeLesson == lessonKey) getLastStepMedium(context, userId, lessonKey) else 0
    var step by remember { mutableIntStateOf(savedStep) }

    // Восстановление внутренних состояний
    var score by remember { mutableIntStateOf(getInt("score", 0)) }
    var currentQuestion by remember { mutableIntStateOf(getInt("currentQuestion", 0)) }
    var selectedAnswer by remember { mutableStateOf<Int?>(getInt("selectedAnswer", -1).takeIf { it != -1 }) }
    var showResult by remember { mutableStateOf(getBoolean("showResult", false)) }
    var showHint by remember { mutableStateOf(getBoolean("showHint", false)) }
    var permissionsChecked by remember { mutableStateOf(getBoolean("permissionsChecked", false)) }
    var installApproved by remember { mutableStateOf(getBoolean("installApproved", false)) }

    // Активация урока
    LaunchedEffect(Unit) {
        activateLessonMedium(context, userId, lessonKey)
    }

    // Сохранение прогресса шага
    LaunchedEffect(step) {
        val progress = if (step >= totalSteps - 1) 1f else step.toFloat() / (totalSteps - 1)
        updateLessonProgressMedium(context, userId, progress)
        saveLastStepMedium(context, userId, lessonKey, step)
    }

    // Сохранение всех состояний при их изменении
    LaunchedEffect(score) { saveInt("score", score) }
    LaunchedEffect(currentQuestion) { saveInt("currentQuestion", currentQuestion) }
    LaunchedEffect(selectedAnswer) { saveInt("selectedAnswer", selectedAnswer ?: -1) }
    LaunchedEffect(showResult) { saveBoolean("showResult", showResult) }
    LaunchedEffect(showHint) { saveBoolean("showHint", showHint) }
    LaunchedEffect(permissionsChecked) { saveBoolean("permissionsChecked", permissionsChecked) }
    LaunchedEffect(installApproved) { saveBoolean("installApproved", installApproved) }

    // Список вопросов (без изменений)
    val questions = listOf(
        Question(
            "Какой магазин приложений является самым безопасным?",
            listOf("Любой сайт", "Google Play Маркет", "Магазин от незнакомца", "Пиратский сайт"),
            1,
            "Google Play Маркет — официальный магазин Google. Все приложения проходят автоматическую проверку на вирусы и вредоносный код. Это значительно снижает риск заражения.",
            listOf(
                "❌ Любой сайт: на сомнительных сайтах приложения могут быть заражены вирусами.",
                "✅ Google Play Маркет: официальный магазин с проверкой приложений.",
                "❌ Магазин от незнакомца: неизвестные источники — частый способ распространения вредоносного ПО.",
                "❌ Пиратский сайт: пиратские версии часто содержат трояны и шпионские программы."
            ),
            "💡 Подсказка: Приложения из официального магазина проходят проверку безопасности."
        ),
        Question(
            "Что нужно проверить перед установкой приложения?",
            listOf("Только цену", "Отзывы, количество скачиваний, разрешения", "Цвет иконки", "Рейтинг разработчика"),
            1,
            "Перед установкой обязательно изучайте: отзывы пользователей (особенно отрицательные), количество скачиваний (миллионы – хороший знак) и разрешения, которые запрашивает приложение.",
            listOf(
                "❌ Только цену: цена не гарантирует безопасность.",
                "✅ Отзывы, количество скачиваний, разрешения: комплексная проверка.",
                "❌ Цвет иконки: злоумышленники могут скопировать иконку известного приложения.",
                "❌ Рейтинг разработчика: важнее реальные отзывы и количество установок."
            ),
            "💡 Подсказка: Посмотрите, сколько людей уже установили приложение, и что они пишут в отзывах."
        ),
        Question(
            "Сколько должно быть скачиваний у безопасного приложения?",
            listOf("10-100", "Тысячи или миллионы", "0-10", "1 скачивание"),
            1,
            "Популярные приложения обычно имеют миллионы скачиваний. Если у приложения меньше 1000 установок, но оно просит много разрешений — это повод насторожиться.",
            listOf(
                "❌ 10-100: очень мало для популярного приложения, возможен риск.",
                "✅ Тысячи или миллионы: признак доверия пользователей.",
                "❌ 0-10: скорее всего, тестовое или вредоносное приложение.",
                "❌ 1 скачивание: почти наверняка опасно."
            ),
            "💡 Подсказка: Чем больше скачиваний, тем выше вероятность, что приложение безопасно."
        ),
        Question(
            "Что такое разрешения приложения?",
            listOf("Цена приложения", "Доступ к функциям телефона", "Название приложения", "Размер приложения"),
            1,
            "Разрешения — это права, которые приложение запрашивает для доступа к функциям вашего телефона: камере, микрофону, контактам, SMS, геолокации и т.д.",
            listOf(
                "❌ Цена приложения: не связана с разрешениями.",
                "✅ Доступ к функциям телефона: именно так.",
                "❌ Название приложения: не определяет его права.",
                "❌ Размер приложения: не имеет отношения к разрешениям."
            ),
            "💡 Подсказка: Разрешения показывают, к каким данным сможет получить доступ приложение."
        ),
        Question(
            "Какое разрешение должно насторожить в приложении-фонарике?",
            listOf("Управление яркостью", "Доступ к контактам", "Включение вспышки", "Работа в фоне"),
            1,
            "Фонарику нужна только вспышка (камера) и, возможно, управление яркостью экрана. Доступ к контактам, SMS, геолокации — явный признак вредоносного ПО.",
            listOf(
                "❌ Управление яркостью: допустимо для фонарика.",
                "✅ Доступ к контактам: категорически не нужно фонарику, очень опасно.",
                "❌ Включение вспышки: основная функция фонарика.",
                "❌ Работа в фоне: может быть допустимо, если фонарик работает в фоне."
            ),
            "💡 Подсказка: Представьте — зачем фонарику ваши контакты? Это ненормально."
        ),
        Question(
            "Что такое Google Play Protect?",
            listOf("Антивирус от Google", "Платный сервис", "Игра", "Лаунчер"),
            0,
            "Google Play Protect — это встроенный антивирус, который автоматически сканирует все приложения на устройстве и в Google Play. Он работает бесплатно и включён по умолчанию.",
            listOf(
                "✅ Антивирус от Google: верно.",
                "❌ Платный сервис: бесплатный.",
                "❌ Игра: нет.",
                "❌ Лаунчер: нет."
            ),
            "💡 Подсказка: Это защитник вашего телефона от вредоносных приложений."
        ),
        Question(
            "Что делать, если приложение просит подозрительные разрешения?",
            listOf("Согласиться", "Отказаться и удалить", "Игнорировать", "Поделиться с друзьями"),
            1,
            "Если приложение запрашивает явно ненужные ему разрешения (например, игра — доступ к контактам), лучше отказаться от установки и удалить его. Это защитит ваши данные.",
            listOf(
                "❌ Согласиться: опасно, данные могут быть украдены.",
                "✅ Отказаться и удалить: правильное решение.",
                "❌ Игнорировать: игнорирование не отменяет запрос, установка не продолжится.",
                "❌ Поделиться с друзьями: не решит проблему."
            ),
            "💡 Подсказка: Не рискуйте — удаляйте подозрительное приложение."
        ),
        Question(
            "Какие отзывы должны быть у безопасного приложения?",
            listOf("Только положительные", "Много, в основном хорошие, есть конструктивная критика", "Нет отзывов", "Одни звёзды без текста"),
            1,
            "У нормального приложения есть и положительные, и отрицательные отзывы. Если отзывы только хорошие — это может быть накрутка. Отсутствие отзывов при миллионах скачиваний — подозрительно.",
            listOf(
                "❌ Только положительные: возможно накрутка, скрытие проблем.",
                "✅ Много, в основном хорошие, есть конструктивная критика: здоровый баланс.",
                "❌ Нет отзывов: странно для популярного приложения.",
                "❌ Одни звёзды без текста: малоинформативно, возможно фейк."
            ),
            "💡 Подсказка: Читайте не только оценки, но и тексты отзывов."
        ),
        Question(
            "Что такое «взломанное» приложение?",
            listOf("Бесплатная версия", "Приложение с изменённым кодом, часто с вирусами", "Демо-версия", "Бета-версия"),
            1,
            "Взломанные приложения — это изменённые версии, где обходят лицензию. Хакеры часто встраивают в них вредоносный код: трояны, рекламные модули, шпионы.",
            listOf(
                "❌ Бесплатная версия: официальные бесплатные приложения безопасны.",
                "✅ Приложение с изменённым кодом, часто с вирусами: верно.",
                "❌ Демо-версия: демо обычно безопасны.",
                "❌ Бета-версия: бета-тестирование проводится через официальные каналы."
            ),
            "💡 Подсказка: Не скачивайте взломанные приложения — они почти всегда опасны."
        ),
        Question(
            "Почему опасны приложения из неизвестных источников?",
            listOf("Они дорогие", "Могут содержать вирусы и украсть данные", "Они не работают", "Они занимают много места"),
            1,
            "Приложения из неизвестных источников не проходят проверку безопасности. Они могут содержать вирусы, трояны, шпионское ПО, красть логины, пароли, банковские данные.",
            listOf(
                "❌ Они дорогие: цена не связана с безопасностью.",
                "✅ Могут содержать вирусы и украсть данные: главная опасность.",
                "❌ Они не работают: могут работать, но с вредоносными функциями.",
                "❌ Они занимают много места: размер не определяет опасность."
            ),
            "💡 Подсказка: Всегда проверяйте источник — Google Play или официальный сайт."
        ),
        Question(
            "Как часто нужно обновлять приложения?",
            listOf("Никогда", "Раз в год", "Регулярно, когда выходят обновления", "Только при покупке нового телефона"),
            2,
            "Регулярные обновления приложений закрывают уязвимости, добавляют новые функции и повышают безопасность. Желательно настраивать автоматическое обновление.",
            listOf(
                "❌ Никогда: опасно, останутся дыры в безопасности.",
                "❌ Раз в год: слишком редко, уязвимости не исправляются.",
                "✅ Регулярно, когда выходят обновления: правильно.",
                "❌ Только при покупке нового телефона: бессмысленно."
            ),
            "💡 Подсказка: Включите автоматическое обновление в Google Play."
        ),
        Question(
            "Что делать с неиспользуемыми приложениями?",
            listOf("Оставить на память", "Удалить", "Переименовать", "Скрыть"),
            1,
            "Неиспользуемые приложения занимают место и могут быть необновляемыми, что создаёт риски безопасности. Лучше их удалить.",
            listOf(
                "❌ Оставить на память: небезопасно, занимает место.",
                "✅ Удалить: правильное решение.",
                "❌ Переименовать: не решает проблему.",
                "❌ Скрыть: приложение остаётся, риски сохраняются."
            ),
            "💡 Подсказка: Регулярно проводите ревизию приложений и удаляйте ненужные."
        ),
        Question(
            "Можно ли устанавливать приложения по ссылке из SMS от незнакомца?",
            listOf("Да", "Нет, это опасно", "Только если ссылка красивая", "Если обещают приз"),
            1,
            "Никогда не переходите по ссылкам из SMS от незнакомцев. Это может быть фишинг или ссылка на вредоносное приложение.",
            listOf(
                "❌ Да: очень опасно.",
                "✅ Нет, это опасно: верно.",
                "❌ Только если ссылка красивая: внешний вид не гарантирует безопасность.",
                "❌ Если обещают приз: классическая уловка мошенников."
            ),
            "💡 Подсказка: Никогда не переходите по подозрительным ссылкам, даже если обещают выигрыш."
        ),
        Question(
            "Что такое фишинг?",
            listOf("Рыбалка", "Вид спорта", "Мошенничество для кражи данных", "Название приложения"),
            2,
            "Фишинг — это вид интернет-мошенничества, когда злоумышленники пытаются получить ваши личные данные (пароли, номера карт) через поддельные сайты или сообщения.",
            listOf(
                "❌ Рыбалка: дословный перевод, но не о том.",
                "❌ Вид спорта: нет.",
                "✅ Мошенничество для кражи данных: верно.",
                "❌ Название приложения: нет."
            ),
            "💡 Подсказка: Фишинг — это когда вас обманом заставляют ввести пароль на поддельном сайте."
        ),
        Question(
            "Какой пароль лучше использовать для аккаунтов?",
            listOf("123456", "qwerty", "Сложный, из букв, цифр и символов", "Дата рождения"),
            2,
            "Надёжный пароль должен содержать не менее 8 символов, включая заглавные и строчные буквы, цифры и специальные символы. Избегайте личной информации.",
            listOf(
                "❌ 123456: самый ненадёжный пароль.",
                "❌ qwerty: тоже очень слабый.",
                "✅ Сложный, из букв, цифр и символов: правильно.",
                "❌ Дата рождения: легко подобрать."
            ),
            "💡 Подсказка: Используйте менеджеры паролей и генерируйте случайные пароли."
        )
    )

    // Функция возврата назад
    fun onBackPressed() {
        when (step) {
            0 -> navController.popBackStack()
            1 -> step = 0
            2 -> step = 1
            3 -> step = 2
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Безопасность приложений", color = Color.White) },
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
                0 -> TheoryScreen(step = step, onStartTest = { step = 1 })
                1 -> TestScreen(
                    questions = questions,
                    currentQuestion = currentQuestion,
                    selectedAnswer = selectedAnswer,
                    showResult = showResult,
                    showHint = showHint,
                    score = score,
                    onScoreUpdate = { newScore -> score = newScore },
                    onNextQuestion = { currentQuestion++ },
                    onResetQuestionState = {
                        selectedAnswer = null
                        showResult = false
                        showHint = false
                    },
                    onSetSelectedAnswer = { selectedAnswer = it },
                    onSetShowResult = { showResult = it },
                    onSetShowHint = { showHint = it },
                    onFinishTest = { step = 2 }
                )
                2 -> SimulationScreen(
                    permissionsChecked = permissionsChecked,
                    installApproved = installApproved,
                    onPermissionsChecked = { permissionsChecked = it },
                    onInstallApproved = { installApproved = it },
                    onComplete = { step = 3 },
                    score = score
                )
                3 -> FinalScreen(
                    score = score,
                    onFinish = {
                        scope.launch {
                            saveLessonProgress(context, userId, lessonKey, true)
                            navController.popBackStack()
                        }
                    }
                )
            }
        }
    }
}

// ШАГ 0: Теория
@Composable
private fun TheoryScreen(step: Int, onStartTest: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
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
                onClick = onStartTest,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Начать тест (15 вопросов)", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ШАГ 1: Тестирование (с LazyColumn для прокрутки)
@Composable
private fun TestScreen(
    questions: List<Question>,
    currentQuestion: Int,
    selectedAnswer: Int?,
    showResult: Boolean,
    showHint: Boolean,
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    onNextQuestion: () -> Unit,
    onResetQuestionState: () -> Unit,
    onSetSelectedAnswer: (Int?) -> Unit,
    onSetShowResult: (Boolean) -> Unit,
    onSetShowHint: (Boolean) -> Unit,
    onFinishTest: () -> Unit
) {
    val question = questions[currentQuestion]
    val isLastQuestion = currentQuestion == questions.size - 1

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Прогресс и вопрос
        item {
            Column {
                Text("Вопрос ${currentQuestion + 1} из ${questions.size}", fontSize = 14.sp, color = Color(0xFF2C5F6E))
                LinearProgressIndicator(
                    progress = (currentQuestion + 1).toFloat() / questions.size,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF2C5F6E)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Карточка вопроса
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(question.text, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { onSetShowHint(!showHint) },
                        modifier = Modifier.align(Alignment.End),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2C5F6E))
                    ) {
                        Icon(Icons.Default.Help, contentDescription = "Подсказка", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Подсказка", fontSize = 12.sp)
                    }

                    AnimatedVisibility(visible = showHint, enter = fadeIn(), exit = fadeOut()) {
                        Column {
                            Spacer(modifier = Modifier.height(12.dp))
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(question.hint, modifier = Modifier.padding(12.dp), fontSize = 14.sp, color = Color(0xFF8A6D3B))
                            }
                        }
                    }
                }
            }
        }

        // Варианты ответов
        items(question.options.size) { index ->
            val option = question.options[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !showResult) { if (!showResult) onSetSelectedAnswer(index) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        showResult && index == question.correctAnswer -> Color(0xFFC8E6C9)
                        showResult && selectedAnswer == index && index != question.correctAnswer -> Color(0xFFFFCDD2)
                        selectedAnswer == index -> Color(0xFF2C5F6E).copy(alpha = 0.2f)
                        else -> Color.White
                    }
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedAnswer == index,
                            onClick = { if (!showResult) onSetSelectedAnswer(index) },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF2C5F6E))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(option, fontSize = 16.sp, fontWeight = if (selectedAnswer == index) FontWeight.Bold else FontWeight.Normal)
                        Spacer(modifier = Modifier.weight(1f))
                        if (showResult && index == question.correctAnswer) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Правильно", tint = Color(0xFF2E8058), modifier = Modifier.size(24.dp))
                        } else if (showResult && selectedAnswer == index && index != question.correctAnswer) {
                            Icon(Icons.Default.Cancel, contentDescription = "Неправильно", tint = Color( 0xFF9B0C3F), modifier = Modifier.size(24.dp))
                        }
                    }

                    AnimatedVisibility(visible = showResult) {
                        Column {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(question.optionsExplanations[index], fontSize = 13.sp, color = Color.Gray, lineHeight = 18.sp)
                        }
                    }
                }
            }
        }

        // Общее пояснение
        if (showResult) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("📖 Пояснение:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF2E7D32))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(question.explanation, fontSize = 14.sp, lineHeight = 20.sp)
                    }
                }
            }
        }

        // Кнопки управления
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                if (!showResult && selectedAnswer != null) {
                    Button(
                        onClick = {
                            onSetShowResult(true)
                            if (selectedAnswer == question.correctAnswer) onScoreUpdate(score + 20)
                            else onScoreUpdate((score - 5).coerceAtLeast(0))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Проверить ответ", color = Color.White)
                    }
                } else if (showResult && !isLastQuestion) {
                    Button(
                        onClick = {
                            onNextQuestion()
                            onResetQuestionState()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Следующий вопрос →", color = Color.White)
                    }
                } else if (showResult && isLastQuestion) {
                    Button(
                        onClick = onFinishTest,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Завершить тест", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("🏆 Очки: $score", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// ШАГ 2: Симуляция
@Composable
private fun SimulationScreen(
    permissionsChecked: Boolean,
    installApproved: Boolean,
    onPermissionsChecked: (Boolean) -> Unit,
    onInstallApproved: (Boolean) -> Unit,
    onComplete: () -> Unit,
    score: Int
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("📱 Симуляция: установка подозрительного приложения", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                Spacer(modifier = Modifier.height(12.dp))
                Text("Приложение «Супер Калькулятор» запрашивает доступ к контактам, SMS, камере и геолокации.", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("⚠️ Калькулятору такие разрешения НЕ НУЖНЫ! Это явный признак вредоносного ПО.", fontSize = 14.sp, color = Color( 0xFF9B0C3F))
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Проверить разрешения", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("(обнаружены лишние разрешения)", fontSize = 12.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = permissionsChecked,
                        onCheckedChange = onPermissionsChecked,
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E))
                    )
                }
                if (permissionsChecked) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("✅ Правильно! Вы заметили подозрительные разрешения.", color = Color(0xFF2E8058), fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Отказаться от установки и удалить приложение", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("(не устанавливать подозрительное ПО)", fontSize = 12.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = installApproved,
                        onCheckedChange = onInstallApproved,
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2C5F6E))
                    )
                }
                if (installApproved) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("✅ Отлично! Небезопасное приложение не установлено.", color = Color(0xFF2E8058), fontSize = 14.sp)
                }
            }
        }

        if (permissionsChecked && installApproved) {
            Button(
                onClick = onComplete,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Готово", color = Color.White)
            }
        } else {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFCDD2)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "❌ Сначала проверьте разрешения и откажитесь от установки",
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp,
                    color = Color( 0xFF9B0C3F),
                    textAlign = TextAlign.Center
                )
            }
        }
        Text("🏆 Очки: $score", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ШАГ 3: Финальный экран
@Composable
private fun FinalScreen(score: Int, onFinish: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🎉 ПОЗДРАВЛЯЕМ!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Вы успешно прошли полный курс безопасности приложений!", fontSize = 18.sp, textAlign = TextAlign.Center, lineHeight = 24.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Вы заработали $score очков.", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                Spacer(modifier = Modifier.height(20.dp))
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)), shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("💡 Золотые правила безопасности:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "• Устанавливайте приложения только из Google Play\n" +
                                    "• Внимательно проверяйте отзывы и количество скачиваний\n" +
                                    "• Анализируйте разрешения – они должны соответствовать функциям\n" +
                                    "• Включите Google Play Protect\n" +
                                    "• Удаляйте неиспользуемые и подозрительные приложения",
                            fontSize = 14.sp,
                            lineHeight = 22.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onFinish,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Завершить", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}