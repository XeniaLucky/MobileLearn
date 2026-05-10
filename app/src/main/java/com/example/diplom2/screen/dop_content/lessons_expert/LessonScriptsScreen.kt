package com.example.diplom2.screen.dop_content.lessons_expert

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.diplom2.screen.setActiveLessonExpert
import com.example.diplom2.screen.updateLessonProgressExpert
import kotlinx.coroutines.launch
import kotlinx.coroutines.launch
import com.example.diplom2.screen.saveLastStepExpert
import com.example.diplom2.screen.getLastStepExpert
import com.example.diplom2.screen.activateLessonExpert
import com.example.diplom2.screen.getActiveLessonExpert
import com.example.diplom2.screen.updateLessonProgressExpert


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScriptsScreen(navController: NavController, userId: Long) {
    var taskerInstalled by remember { mutableStateOf(false) }
    var profileCreated by remember { mutableStateOf(false) }
    var taskCreated by remember { mutableStateOf(false) }
    var scriptWritten by remember { mutableStateOf(false) }
    var automationTested by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
        val lessonKey = "lesson_scripts"
        val totalSteps = 7

        // Инициализируем стартовый шаг из сохранённого, если урок активен
        var step by remember {
            val activeLesson = getActiveLessonExpert(context, userId)
            if (activeLesson == lessonKey) {
                mutableIntStateOf(getLastStepExpert(context, userId, lessonKey))
            } else {
                mutableIntStateOf(0)
            }
        }

        var score by remember { mutableIntStateOf(0) }
        var adbInstalled by remember { mutableStateOf(false) }
        var usbDebugging by remember { mutableStateOf(false) }
        var deviceConnected by remember { mutableStateOf(false) }
        var commandExecuted by remember { mutableStateOf(false) }

        // Активируем урок (без сброса прогресса, если он уже активен)
        LaunchedEffect(Unit) {
            activateLessonExpert(context, userId, lessonKey)
        }

        // Обновляем прогресс и сохраняем шаг при каждом изменении step
        LaunchedEffect(step) {
            val progress = if (step >= totalSteps - 1) 1f else step / (totalSteps - 1).toFloat()
            updateLessonProgressExpert(context, userId, progress)
            saveLastStepExpert(context, userId, lessonKey, step)
        }
    // ШАГ 0: ВСТУПЛЕНИЕ (уже LazyColumn, добавляем отступ)
    if (step == 0) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF09020A))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("🤖 СКРИПТЫ И АВТОМАТИЗАЦИЯ – ПОЛНОЕ РУКОВОДСТВО ДЛЯ ЭКСПЕРТА", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("📌 **ЧТО ТАКОЕ АВТОМАТИЗАЦИЯ НА ANDROID?**", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Автоматизация – это когда телефон сам выполняет действия по заданным правилам. " +
                                    "Вы настраиваете один раз – и телефон работает за вас.\n\n" +
                                    "⚡ **ЧТО МОЖНО АВТОМАТИЗИРОВАТЬ?**\n" +
                                    "• При подключении к домашнему Wi-Fi → отключать мобильные данные\n" +
                                    "• При зарядке → включать режим «Не беспокоить»\n" +
                                    "• По расписанию → делать резервное копирование фото в облако\n" +
                                    "• При запуске игры → очищать память и отключать уведомления\n" +
                                    "• При низком заряде батареи (<15%) → отключать Bluetooth, GPS, снижать яркость\n" +
                                    "• При входе в определённое место (по GPS) → включать беззвучный режим\n" +
                                    "• В 23:00 → включать ночной режим и отключать звонки\n" +
                                    "• При получении SMS с кодом → автоматически копировать код в буфер обмена\n\n" +
                                    "🛠️ **ИНСТРУМЕНТЫ ДЛЯ АВТОМАТИЗАЦИИ:**\n" +
                                    "• **Tasker** – самый мощный инструмент (почти безграничные возможности)\n" +
                                    "• **MacroDroid** – проще для новичков, интуитивный интерфейс\n" +
                                    "• **Automate** – визуальное программирование (блок-схемы)\n" +
                                    "• **IFTTT** – облачная автоматизация (для интернет-сервисов)\n" +
                                    "• **Shell-скрипты** – для продвинутых, через Termux\n\n" +
                                    "🎯 **ЭТОТ УРОК НАУЧИТ:**\n" +
                                    "1️⃣ Основы работы с Tasker (самый мощный инструмент)\n" +
                                    "2️⃣ Создание профилей (условий) и задач (действий)\n" +
                                    "3️⃣ Написание простых shell-скриптов\n" +
                                    "4️⃣ Выполнение скриптов через Tasker\n" +
                                    "5️⃣ Реальные примеры автоматизации для повседневной жизни\n\n" +
                                    "📋 **ЧТО ВАМ ПОНАДОБИТСЯ:**\n" +
                                    "• Tasker (скачать с Play Маркет или с официального сайта – есть бесплатная триал-версия)\n" +
                                    "• Termux (для shell-скриптов) – бесплатно в Play Маркет\n" +
                                    "• Root (не обязателен, но расширяет возможности)\n" +
                                    "• Немного терпения и желание автоматизировать рутину\n\n" +
                                    "⚠️ **ВАЖНО:**\n" +
                                    "• Tasker имеет крутую кривую обучения, но оно того стоит\n" +
                                    "• Дайте все разрешения Tasker (доступ к уведомлениям, файлам, системе)\n" +
                                    "• Отключите оптимизацию батареи для Tasker, иначе профили могут не срабатывать\n" +
                                    "• Всегда тестируйте новые профили на «безопасных» условиях",
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
            item {
                Button(
                    onClick = { step = 1 },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37)),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("НАЧАТЬ ОСВОЕНИЕ АВТОМАТИЗАЦИИ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black) }
                Spacer(modifier = Modifier.height(32.dp)) // ДОБАВЛЕНО: отступ снизу
            }
        }
        return
    }

    // ШАГ 1: Установка и настройка Tasker (ИСПРАВЛЕН - добавлен скролл)
    if (step == 1) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF09020A))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("📱 ШАГ 1: УСТАНОВКА И НАСТРОЙКА TASKER", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Tasker – это приложение, которое превращает ваш телефон в умное устройство, " +
                                    "способное реагировать на тысячи событий и выполнять любые действия.\n\n" +
                                    "🔹 **ГДЕ СКАЧАТЬ TASKER?**\n\n" +
                                    "• **Google Play Маркет** – официальная версия (платная, около 300 руб)\n" +
                                    "• **Официальный сайт** – tasker.joaoapps.com (можно скачать триал-версию на 7 дней)\n" +
                                    "• **Альтернатива:** MacroDroid (бесплатный, но с ограничениями)\n\n" +
                                    "🔹 **ПЕРВЫЙ ЗАПУСК И НАСТРОЙКА:**\n\n" +
                                    "1️⃣ **Установите Tasker**\n" +
                                    "2️⃣ **Запустите приложение**\n" +
                                    "3️⃣ **Дайте все необходимые разрешения:**\n" +
                                    "   • Доступ к уведомлениям (Android 13+)\n" +
                                    "   • Доступ к файлам и медиа\n" +
                                    "   • Разрешение на установку из неизвестных источников (если установили не из Play Маркет)\n" +
                                    "4️⃣ **Отключите оптимизацию батареи для Tasker:**\n" +
                                    "   • Настройки → Приложения → Tasker → Батарея → Не оптимизировать\n" +
                                    "   • Иначе Tasker будет убит системой и профили не сработают\n" +
                                    "5️⃣ **Включите специальные возможности (если нужно):**\n" +
                                    "   • Настройки → Специальные возможности → Tasker → Включить\n" +
                                    "   • Это нужно для отслеживания событий на экране\n\n" +
                                    "🔹 **СТРУКТУРА TASKER:**\n\n" +
                                    "• **Профиль (Profile)** – условие, при котором что-то произойдёт (например, время, местоположение, запуск приложения)\n" +
                                    "• **Задача (Task)** – действие, которое выполнится при срабатывании профиля\n" +
                                    "• **Сцена (Scene)** – визуальный интерфейс (кнопки, окна), который можно показать\n\n" +
                                    "🔹 **ПРИМЕР ПРОСТОЙ ЗАДАЧИ (без профиля):**\n" +
                                    "• Нажмите на вкладку «Задачи» (Tasks)\n" +
                                    "• Нажмите «+»\n" +
                                    "• Введите название, например «Тест»\n" +
                                    "• Нажмите «+» → Действие → Alert → Flash\n" +
                                    "• В поле Text введите: Привет, мир!\n" +
                                    "• Нажмите «Галочку»\n" +
                                    "• Теперь нажмите на задачу → кнопку «Выполнить» (треугольник) – должно появиться сообщение\n\n" +
                                    "🔹 **ПРИМЕР ПРОСТОГО ПРОФИЛЯ:**\n" +
                                    "• Нажмите на вкладку «Профили» (Profiles)\n" +
                                    "• Нажмите «+» → Состояние (State) → Приложение (App)\n" +
                                    "• Выберите любое приложение (YouTube, Telegram)\n" +
                                    "• Нажмите «Галочку»\n" +
                                    "• Выберите задачу, которую создали ранее (или создайте новую)\n" +
                                    "• Теперь при запуске этого приложения будет появляться сообщение\n\n" +
                                    "💡 **СОВЕТ:** Начните с малого – создайте один простой профиль, поймите логику, " +
                                    "а потом переходите к сложным сценариям. В интернете тысячи готовых примеров.",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Я установил Tasker и дал все разрешения", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = taskerInstalled,
                        onCheckedChange = { taskerInstalled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (taskerInstalled) {
                item {
                    Text("✅ Отлично! Переходим к созданию профилей.", color = Color.Green, fontSize = 14.sp)
                }
                item {
                    Button(
                        onClick = { score += 20; step = 2 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                    ) {
                        Text("ДАЛЕЕ →", color = Color.Black)
                    }
                }
            } else {
                item {
                    Text("❌ Подтвердите установку Tasker", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 2: Создание профиля в Tasker (ИСПРАВЛЕН - добавлен скролл)
    if (step == 2) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF09020A))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("🎯 ШАГ 2: СОЗДАНИЕ ПРОФИЛЯ (УСЛОВИЯ) В TASKER", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Профиль – это условие, при котором Tasker начинает действовать. " +
                                    "Без профиля задачи можно выполнять только вручную.\n\n" +
                                    "🔹 **ТИПЫ ПРОФИЛЕЙ:**\n\n" +
                                    "• **Событие (Event)** – что-то краткосрочное (пришло уведомление, нажали кнопку)\n" +
                                    "• **Состояние (State)** – долгосрочное состояние (Wi-Fi включён, зарядка подключена, время 18:00)\n" +
                                    "• **Приложение (Application)** – запуск определённого приложения\n" +
                                    "• **Время (Time)** – по расписанию (каждый день в 08:00)\n" +
                                    "• **День/Ночь (Day/Night)** – разное время суток\n" +
                                    "• **Местоположение (Location)** – вход в определённую зону (работа, дом)\n" +
                                    "• **Состояние телефона (Phone)** – звонок, уровень заряда, экран\n\n" +
                                    "🔹 **ПРИМЕР РЕАЛЬНОГО ПРОФИЛЯ: «Домашний Wi-Fi»**\n\n" +
                                    "✅ **Цель:** Когда вы подключаетесь к домашнему Wi-Fi, отключать мобильные данные и включать нормальную громкость.\n\n" +
                                    "📌 **ИНСТРУКЦИЯ:**\n\n" +
                                    "1️⃣ **Откройте Tasker** → вкладка «Профили»\n" +
                                    "2️⃣ **Нажмите «+»** → «Состояние» → «Сеть» → «Wi-Fi Connected»\n" +
                                    "3️⃣ **Введите SSID вашей домашней сети** (можно оставить пустым, тогда сработает на любом Wi-Fi)\n" +
                                    "4️⃣ **Нажмите «Галочку»**\n" +
                                    "5️⃣ **Создайте новую задачу**, назовите «Домашние настройки»\n" +
                                    "6️⃣ **Нажмите «+»** → «Сеть» → «Мобильные данные» → выберите «Выкл»\n" +
                                    "7️⃣ **Нажмите «+»** → «Звук» → «Громкость звонка» → установите 10\n" +
                                    "8️⃣ **Нажмите «Галочку»**\n\n" +
                                    "✅ Теперь при подключении к домашнему Wi-Fi телефон сам:\n" +
                                    "• Отключит мобильный интернет\n" +
                                    "• Сделает звонки громкими\n\n" +
                                    "🔹 **ДОБАВЛЕНИЕ ВЫХОДА (Exit Task):**\n" +
                                    "• Когда вы отключаетесь от Wi-Fi, тоже нужно что-то сделать\n" +
                                    "• Зажмите созданную задачу → «Добавить задачу выхода»\n" +
                                    "• Создайте задачу «Уличные настройки»\n" +
                                    "• В ней включите мобильные данные и уменьшите громкость\n\n" +
                                    "🔹 **ДРУГИЕ ПОЛЕЗНЫЕ ПРОФИЛИ:**\n\n" +
                                    "• **Экономия батареи:** Уровень заряда < 15% → отключить Bluetooth, GPS, снизить яркость, включить энергосбережение\n" +
                                    "• **Ночной режим:** Время 23:00 – 07:00 → включить «Не беспокоить», отключить звук\n" +
                                    "• **Работа:** По геолокации (офис) → включить виброрежим, отключить Wi-Fi\n" +
                                    "• **Зарядка:** При подключении зарядки → включить экран, увеличить яркость, отключить таймер блокировки\n" +
                                    "• **Наушники:** При подключении наушников → открыть музыкальное приложение, уменьшить громкость\n\n" +
                                    "💡 **СОВЕТ:** Начните с одного профиля, протестируйте его, " +
                                    "убедитесь, что всё работает, а потом усложняйте.",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Я создал профиль (условие) в Tasker", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = profileCreated,
                        onCheckedChange = { profileCreated = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (profileCreated) {
                item {
                    Text("✅ Отлично! Профиль готов.", color = Color.Green, fontSize = 14.sp)
                }
                item {
                    Button(
                        onClick = { score += 25; step = 3 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                    ) {
                        Text("ДАЛЕЕ →", color = Color.Black)
                    }
                }
            } else {
                item {
                    Text("❌ Подтвердите, что вы создали профиль", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 3: Создание задачи в Tasker (ИСПРАВЛЕН - добавлен скролл)
    if (step == 3) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF09020A))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("🎬 ШАГ 3: СОЗДАНИЕ ЗАДАЧИ (ДЕЙСТВИЯ) В TASKER", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Задача – это набор действий, которые Tasker выполняет при срабатывании профиля. " +
                                    "Одна задача может включать десятки действий.\n\n" +
                                    "🔹 **КАТЕГОРИИ ДЕЙСТВИЙ (их сотни):**\n\n" +
                                    "• **Alert** – уведомления, вибрация, всплывающие сообщения\n" +
                                    "• **App** – запуск/закрытие приложений, управление ими\n" +
                                    "• **Audio** – громкость, звуковые профили, микрофон\n" +
                                    "• **Display** – яркость, ориентация экрана, блокировка, таймаут\n" +
                                    "• **File** – работа с файлами (копирование, удаление, перемещение)\n" +
                                    "• **Input** – эмуляция нажатий, жестов, текста (требуется root или специальные разрешения)\n" +
                                    "• **Media** – управление музыкой (плей/пауза, следующий трек)\n" +
                                    "• **Net** – Wi-Fi, Bluetooth, мобильные данные, самолётик\n" +
                                    "• **Phone** – звонки, SMS, контакты\n" +
                                    "• **Power** – управление питанием, отключение экрана\n" +
                                    "• **Run Shell** – выполнение shell-команд (для продвинутых)\n" +
                                    "• **Tasker** – управление самим Tasker (вкл/выкл профили)\n" +
                                    "• **Variables** – работа с переменными (сохранение данных)\n\n" +
                                    "🔹 **ПРИМЕР ЗАДАЧИ: «Умный дом»**\n\n" +
                                    "✅ **Цель:** При подключении к домашнему Wi-Fi выполнить:\n" +
                                    "• Отключить мобильные данные\n" +
                                    "• Включить Wi-Fi (он и так включён, но на всякий случай)\n" +
                                    "• Установить громкость звонка на 50%\n" +
                                    "• Отправить уведомление: «Добро пожаловать домой!»\n" +
                                    "• Сделать резервное копирование фото (если есть возможность)\n\n" +
                                    "📌 **ИНСТРУКЦИЯ ПО СОЗДАНИЮ ТАСКИ:**\n\n" +
                                    "1️⃣ **Откройте Tasker** → вкладка «Задачи»\n" +
                                    "2️⃣ **Нажмите «+»** → введите название «Домашний уют»\n" +
                                    "3️⃣ **Нажмите «+»** → «Сеть» → «Мобильные данные» → «Выкл»\n" +
                                    "4️⃣ **Нажмите «+»** → «Сеть» → «Wi-Fi» → «Вкл»\n" +
                                    "5️⃣ **Нажмите «+»** → «Звук» → «Громкость звонка» → 5\n" +
                                    "6️⃣ **Нажмите «+»** → «Alert» → «Flash» → текст «Добро пожаловать домой!»\n" +
                                    "7️⃣ **Нажмите «+»** → «Alert» → «Уведомление» → заголовок «Tasker», текст «Wi-Fi подключён»\n" +
                                    "8️⃣ **Нажмите «Галочку»**\n\n" +
                                    "🔹 **РАСШИРЕННЫЕ ДЕЙСТВИЯ:**\n\n" +
                                    "• **Переменные:** используйте их для хранения состояния. Например, если задаче нужно помнить, сколько раз она выполнялась.\n" +
                                    "• **If/Else:** условные действия. Например: если уровень заряда < 20%, то включить энергосбережение, иначе ничего не делать.\n" +
                                    "• **Циклы (Goto):** повторение действий (осторожно, бесконечные циклы могут зависнуть систему).\n" +
                                    "• **Run Shell:** выполнение команд терминала. Например: «input tap x y» – эмуляция нажатия в координатах.\n\n" +
                                    "🔹 **ПРИМЕР С УСЛОВИЕМ (If):**\n" +
                                    "• Добавьте действие → Tasker → Если (If)\n" +
                                    "• Условие: %BATT < 20 (уровень заряда меньше 20%)\n" +
                                    "• Добавьте действие при истине → Энергосбережение → Включить\n" +
                                    "• Добавьте действие → Tasker → Конец если (End If)\n\n" +
                                    "💡 **СОВЕТ:** Действия выполняются сверху вниз. Порядок важен. " +
                                    "Для отладки используйте «Flash» – он покажет текст на экране, " +
                                    "и вы поймёте, до какого шага доходит выполнение.",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Я создал задачу (действие) в Tasker", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = taskCreated,
                        onCheckedChange = { taskCreated = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (taskCreated) {
                item {
                    Text("✅ Отлично! Задача создана.", color = Color.Green, fontSize = 14.sp)
                }
                item {
                    Button(
                        onClick = { score += 25; step = 4 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                    ) {
                        Text("ДАЛЕЕ →", color = Color.Black)
                    }
                }
            } else {
                item {
                    Text("❌ Подтвердите, что вы создали задачу", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 4: Shell-скрипты через Termux (ИСПРАВЛЕН - добавлен скролл)
    if (step == 4) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF09020A))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("⌨️ ШАГ 4: SHELL-СКРИПТЫ ЧЕРЕЗ TERMUX", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Shell-скрипты – это текстовые файлы с командами, которые выполняются в терминале. " +
                                    "Они открывают безграничные возможности для автоматизации.\n\n" +
                                    "🔹 **ЧТО ТАКОЕ TERMUX?**\n" +
                                    "• Это эмулятор терминала для Android\n" +
                                    "• Позволяет выполнять команды Linux\n" +
                                    "• Можно устанавливать пакеты (python, git, nginx, openssh)\n\n" +
                                    "🔹 **УСТАНОВКА TERMUX:**\n" +
                                    "1️⃣ Скачайте Termux из Play Маркет или с F-Droid (последнюю версию)\n" +
                                    "2️⃣ Запустите Termux\n" +
                                    "3️⃣ Обновите пакеты: apt update && apt upgrade\n" +
                                    "4️⃣ Дайте разрешения (доступ к файлам, если нужно)\n\n" +
                                    "🔹 **ПРОСТОЙ SHELL-СКРИПТ (очистка кэша):**\n\n" +
                                    "1️⃣ Создайте файл: nano /sdcard/clearcache.sh\n" +
                                    "2️⃣ Напишите:\n" +
                                    "   #!/system/bin/sh\n" +
                                    "   rm -rf /data/data/*/cache/*\n" +
                                    "   echo «Кэш очищен!»\n" +
                                    "3️⃣ Сохраните: Ctrl+O → Enter → Ctrl+X\n" +
                                    "4️⃣ Дайте права на выполнение: chmod +x /sdcard/clearcache.sh\n" +
                                    "5️⃣ Запустите: sh /sdcard/clearcache.sh\n\n" +
                                    "🔹 **ВЫПОЛНЕНИЕ SHELL-СКРИПТОВ ЧЕРЕЗ TASKER:**\n\n" +
                                    "• В Tasker создайте задачу\n" +
                                    "• Добавьте действие → Code → Run Shell\n" +
                                    "• В поле «Command» напишите: sh /sdcard/clearcache.sh\n" +
                                    "• Поставьте галочку «Use Root», если скрипт требует root\n" +
                                    "• Привязать к профилю\n\n" +
                                    "🔹 **ПРИМЕРЫ ПОЛЕЗНЫХ SHELL-КОМАНД:**\n\n" +
                                    "📌 **Выключить телефон:**\n" +
                                    "   reboot -p\n\n" +
                                    "📌 **Перезагрузить телефон:**\n" +
                                    "   reboot\n\n" +
                                    "📌 **Перезагрузить в TWRP:**\n" +
                                    "   reboot recovery\n\n" +
                                     "📌 **Перезагрузить в загрузчик:**\n" +
                        "   reboot bootloader\n\n" +
                                "📌 **Удалить системное приложение (нужен root):**\n" +
                                "   pm uninstall -k --user 0 com.package.name\n\n" +
                                "📌 **Включить/выключить Wi-Fi:**\n" +
                                "   svc wifi enable\n" +
                                "   svc wifi disable\n\n" +
                                "📌 **Включить/выключить мобильные данные:**\n" +
                                "   svc data enable\n" +
                                "   svc data disable\n\n" +
                                "📌 **Сделать скриншот:**\n" +
                                "   /system/bin/screencap -p /sdcard/screenshot.png\n\n" +
                                "📌 **Запись логов:**\n" +
                                "   logcat -d > /sdcard/log.txt\n\n" +
                                "🔹 **ПРИМЕР СКРИПТА «УМНЫЙ ДОМ» (с root):**\n\n" +
                                "# Отключить мобильные данные\n" +
                                "svc data disable\n" +
                                "# Включить Wi-Fi\n" +
                                "svc wifi enable\n" +
                                "# Понизить яркость (echo значение в файл)\n" +
                                "echo 50 > /sys/class/leds/lcd-backlight/brightness\n" +
                                "# Отправить уведомление\n" +
                                "termux-notification --title «Скрипт» --content «Домашние настройки применены»\n\n" +
                                "💡 **СОВЕТ:** Начинайте с простых команд через Run Shell. " +
                                "Проверяйте каждую команду в терминале, прежде чем добавлять в Tasker. " +
                                "Для работы с системными файлами нужен root.",
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Я написал простой shell-скрипт", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = scriptWritten,
                        onCheckedChange = { scriptWritten = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (scriptWritten) {
                item {
                    Text("✅ Скрипт готов!", color = Color.Green, fontSize = 14.sp)
                }
                item {
                    Button(
                        onClick = { score += 30; step = 5 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                    ) {
                        Text("ДАЛЕЕ →", color = Color.Black)
                    }
                }
            } else {
                item {
                    Text("❌ Подтвердите, что вы написали скрипт", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 5: Тестирование и отладка (ИСПРАВЛЕН - добавлен скролл)
    if (step == 5) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF09020A))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("🧪 ШАГ 5: ТЕСТИРОВАНИЕ И ОТЛАДКА", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Прежде чем полагаться на автоматизацию, нужно убедиться, что она работает.\n\n" +
                                    "🔹 **ОТЛАДКА В TASKER:**\n\n" +
                                    "• Включите «Режим отладки»: Настройки Tasker → Отладка → Включить логи\n" +
                                    "• Используйте действие «Flash» для вывода сообщений\n" +
                                    "• Используйте действие «Писать в файл» для логирования\n" +
                                    "• Просматривайте логи: Меню → Отладка → Просмотр логов\n\n" +
                                    "🔹 **ПРОВЕРКА ПРОФИЛЯ:**\n" +
                                    "• На вкладке «Профили» нажмите на шестерёнку рядом с профилем\n" +
                                    "• Включите «Вход в журнал»\n" +
                                    "• Включите «Выход из журнала»\n" +
                                    "• Теперь в логах будет видно, когда сработал профиль\n\n" +
                                    "🔹 **ТЕСТОВЫЙ ПРОФИЛЬ (для проверки):**\n\n" +
                                    "✅ Создайте простой профиль с событием:\n" +
                                    "• Событие → Датчики → Встряхивание телефона (Shake)\n" +
                                    "• Задача → Alert → Flash → «Телефон встряхнули!»\n\n" +
                                    "🔥 Теперь потрясите телефон – должно появиться сообщение.\n\n" +
                                    "✅ **ДРУГИЕ ТЕСТОВЫЕ ПРОФИЛИ:**\n\n" +
                                    "• **Время:** По расписанию (каждую минуту) → Flash «Прошла минута»\n" +
                                    "• **Зарядка:** При подключении зарядки → Flash «Телефон заряжается»\n" +
                                    "• **Приложение:** При запуске YouTube → Flash «Открыл YouTube»\n\n" +
                                    "🔹 **ЧТО ДЕЛАТЬ, ЕСЛИ ПРОФИЛЬ НЕ РАБОТАЕТ?**\n\n" +
                                    "1️⃣ Проверьте разрешения (все ли даны)\n" +
                                    "2️⃣ Проверьте оптимизацию батареи (должна быть отключена)\n" +
                                    "3️⃣ Проверьте, активен ли профиль (галочка рядом с именем)\n" +
                                    "4️⃣ Проверьте условия – может, вы забыли что-то указать\n" +
                                    "5️⃣ Посмотрите логи – там будет ошибка, если она есть\n" +
                                    "6️⃣ Перезагрузите телефон и Tasker\n" +
                                    "7️⃣ Временно отключите все другие профили, чтобы исключить конфликты\n\n" +
                                    "🔹 **РАСШИРЕННАЯ ОТЛАДКА (с root):**\n\n" +
                                    "• Установите приложение Logcat Reader\n" +
                                    "• Фильтруйте логи по ключевому слову Tasker\n" +
                                    "• Вы увидите все действия и ошибки системы\n\n" +
                                    "💡 **СОВЕТ:** Не пытайтесь автоматизировать всё сразу. " +
                                    "Начните с одного простого профиля, доведите его до идеала, " +
                                    "а потом добавляйте новые. Так вы поймёте логику Tasker и избежите ошибок.",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Я протестировал автоматизацию", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = automationTested,
                        onCheckedChange = { automationTested = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (automationTested) {
                item {
                    Text("✅ Поздравляю! Вы освоили автоматизацию на Android!", color = Color.Green, fontSize = 14.sp)
                }
                item {
                    Button(
                        onClick = { score += 40; step = 6 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                    ) {
                        Text("ЗАВЕРШИТЬ УРОК", color = Color.Black)
                    }
                }
            } else {
                item {
                    Text("❌ Подтвердите, что вы протестировали автоматизацию", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ФИНАЛЬНЫЙ ЭКРАН (ИСПРАВЛЕН - добавлен скролл и отступ)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF09020A))
            .verticalScroll(rememberScrollState()) // ДОБАВЛЕН скролл
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
        ) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🎉 ВЫ ОСВОИЛИ АВТОМАТИЗАЦИЮ КАК ЭКСПЕРТ!", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Вы заработали $score очков.", fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "📚 **ЧТО ДАЛЬШЕ?**\n\n" +
                            "• Изучайте Tasker – он может почти всё, что вы можете представить\n" +
                            "• Скачивайте готовые профили с Reddit (r/tasker) и форумов\n" +
                            "• Создавайте свои сценарии и делитесь ими\n" +
                            "• Переходите к следующим урокам: Безопасность, Настройка сети\n\n" +
                            "💡 **ИДЕИ ДЛЯ АВТОМАТИЗАЦИИ:**\n\n" +
                            "• **Автоматический ответ:** При входящем звонке с незнакомого номера – отправить SMS с просьбой представиться\n" +
                            "• **Бэкап фото:** Каждую ночь в 03:00 – копировать новые фото в облако\n" +
                            "• **Умная блокировка:** Находясь дома, не требовать PIN-код\n" +
                            "• **Автоочистка:** Раз в день – удалять кэш всех приложений\n" +
                            "• **Умный Wi-Fi:** На улице – выключать Wi-Fi, включать мобильные данные; дома – наоборот\n" +
                            "• **Утренний будильник:** При остановке будильника – прочитать прогноз погоды и рассказать о событиях в календаре\n\n" +
                            "🔧 **ПОЛЕЗНЫЕ ССЫЛКИ:**\n" +
                            "• Tasker Wiki: tasker.joaoapps.com\n" +
                            "• Reddit: reddit.com/r/tasker\n" +
                            "• Готовые профили: tasker.wikidot.com\n" +
                            "• Termux: termux.com",
                    fontSize = 15.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "lesson_scripts", true)
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                ) { Text("ЗАВЕРШИТЬ", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black) }
            }
        }
        Spacer(modifier = Modifier.height(32.dp)) // ДОБАВЛЕНО: отступ снизу
    }
}