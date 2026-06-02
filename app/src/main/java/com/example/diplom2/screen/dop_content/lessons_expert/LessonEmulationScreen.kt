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
fun LessonEmulationScreen(navController: NavController, userId: Long) {
    var vmosInstalled by remember { mutableStateOf(false) }
    var termuxProotSetup by remember { mutableStateOf(false) }
    var linuxInstalled by remember { mutableStateOf(false) }
    var windowsEmulation by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
        val lessonKey = "lesson_emulation"
        val totalSteps = 6

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
                        Text("📱 ЭМУЛЯЦИЯ И ВИРТУАЛИЗАЦИЯ НА ANDROID – ПОЛНОЕ РУКОВОДСТВО ДЛЯ ЭКСПЕРТА", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("📌 **ЧТО ТАКОЕ ЭМУЛЯЦИЯ НА ANDROID?**", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Эмуляция позволяет запускать другие операционные системы внутри Android. " +
                                    "Это открывает огромные возможности:\n\n" +
                                    "⚡ **ЧТО МОЖНО ЗАПУСТИТЬ?**\n" +
                                    "• **Windows (XP, 7, 10, 11)** – через BOCHS, Limbo, QEMU\n" +
                                    "• **Linux (Ubuntu, Debian, Arch)** – через Termux + proot, UserLAnd, Andronix\n" +
                                    "• **Другую версию Android** – через VMOS, VPhoneGaGa, Sandbox\n" +
                                    "• **DOS/Старые игры** – через Magic DOSBox\n" +
                                    "• **Игровые консоли** – PlayStation, GameBoy, PSP через эмуляторы\n" +
                                    "• **Запуск Windows-приложений** – через ExaGear (устарел) или Winlator\n\n" +
                                    "🎯 **ЭТОТ УРОК НАУЧИТ:**\n" +
                                    "1️⃣ Установке VMOS – виртуальной Android-среды\n" +
                                    "2️⃣ Запуску Linux через Termux + proot (без root)\n" +
                                    "3️⃣ Запуску Windows через Limbo (QEMU)\n" +
                                    "4️⃣ Настройке эмуляции игровых консолей (RetroArch)\n" +
                                    "5️⃣ Решению проблем с производительностью\n\n" +
                                    "⚠️ **ВАЖНО:**\n" +
                                    "• Эмуляция требует много ресурсов (ОЗУ, CPU)\n" +
                                    "• На телефонах с 2-3 ГБ ОЗУ Windows будет тормозить\n" +
                                    "• Виртуализация внутри виртуализации невозможна\n" +
                                    "• Некоторые эмуляторы требуют root\n" +
                                    "• Нагрев телефона при эмуляции Windows – нормально",
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
                ) { Text("НАЧАТЬ ОСВОЕНИЕ ЭМУЛЯЦИИ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black) }
                Spacer(modifier = Modifier.height(32.dp)) // ДОБАВЛЕНО: отступ снизу
            }
        }
        return
    }

    // ШАГ 1: VMOS (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("📲 ШАГ 1: VMOS – ЗАПУСК ANDROID ВНУТРИ ANDROID", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "VMOS – это приложение, которое создаёт виртуальный Android внутри вашей системы. " +
                                    "Идеально для тестирования приложений или запуска двух аккаунтов.\n\n" +
                                    "🔹 **ПРЕИМУЩЕСТВА VMOS:**\n" +
                                    "• Не требует root\n" +
                                    "• Работает на большинстве устройств\n" +
                                    "• Можно запускать несколько виртуальных Android\n" +
                                    "• Встроенный root внутри виртуальной системы\n" +
                                    "• Поддержка Google Play\n\n" +
                                    "🔹 **УСТАНОВКА VMOS:**\n\n" +
                                    "1️⃣ Скачайте VMOS с официального сайта (не из Play Маркет – там старая версия)\n" +
                                    "2️⃣ Установите приложение (разрешите установку из неизвестных источников)\n" +
                                    "3️⃣ Откройте VMOS и дайте все разрешения\n" +
                                    "4️⃣ Подождите, пока скачается образ Android\n" +
                                    "5️⃣ После установки вы попадёте в виртуальный Android\n\n" +
                                    "🔹 **ЧТО МОЖНО ДЕЛАТЬ В VMOS:**\n\n" +
                                    "• Устанавливать любые приложения (они не видны в основной системе)\n" +
                                    "• Получить root внутри виртуальной системы (без риска для основного телефона)\n" +
                                    "• Запускать приложения, которые не работают на вашей прошивке\n" +
                                    "• Клонировать приложения (несколько аккаунтов в одной игре)\n" +
                                    "• Тестировать вирусы/вредоносное ПО в безопасной среде\n" +
                                    "• Переносить файлы между основной и виртуальной системой\n\n" +
                                    "🔹 **НАСТРОЙКА VMOS (рекомендации):**\n\n" +
                                    "• В настройках выделите больше ОЗУ (2-3 ГБ) для плавности\n" +
                                    "• Включите поддержку Google Play, если не появилась автоматически\n" +
                                    "• Установите свои приложения через Play Маркет внутри VMOS\n" +
                                    "• Для копирования файлов используйте общую папку (Tools → File Share)\n\n" +
                                    "🔹 **АЛЬТЕРНАТИВЫ VMOS (с root):**\n\n" +
                                    "• **VPhoneGaGa** – аналог VMOS, но с большей производительностью\n" +
                                    "• **Sandbox** – легковесная изоляция\n" +
                                    "• **Shelter** – open-source, создаёт рабочий профиль\n\n" +
                                    "🔹 **ЧЕГО НЕЛЬЗЯ ДЕЛАТЬ В VMOS:**\n\n" +
                                    "• Ожидать высокой производительности в играх (тормозит)\n" +
                                    "• Рассчитывать на полную изоляцию от системы (может оставлять следы)\n" +
                                    "• Запускать очень старые версии Android (только 5.0+)\n\n" +
                                    "💡 **СОВЕТ:** VMOS отлично подходит для обхода проверки root в банковских приложениях – " +
                                    "вы можете запустить банк в виртуальной системе без root.",
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
                    Text("Я установил и настроил VMOS", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = vmosInstalled,
                        onCheckedChange = { vmosInstalled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (vmosInstalled) {
                item {
                    Text("✅ Виртуальный Android готов к работе!", color = Color(0xFF2E8058), fontSize = 14.sp)
                }
                item {
                    Button(
                        onClick = { score += 25; step = 2 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                    ) {
                        Text("ДАЛЕЕ →", color = Color.Black)
                    }
                }
            } else {
                item {
                    Text("❌ Подтвердите установку VMOS", color = Color( 0xFF9B0C3F), fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 2: Linux через Termux + proot (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("🐧 ШАГ 2: ЗАПУСК LINUX ЧЕРЕЗ TERMUX + PROOT", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Termux с proot позволяет запускать полноценный Linux (Ubuntu, Debian, Arch) без root. " +
                                    "Это один из самых популярных способов.\n\n" +
                                    "🔹 **ЧТО ТАКОЕ PROOT?**\n" +
                                    "• Утилита для запуска Linux в изолированной среде (без root)\n" +
                                    "• Эмулирует корневую файловую систему\n" +
                                    "• Работает практически на любом устройстве\n\n" +
                                    "🔹 **УСТАНОВКА PROOT И LINUX:**\n\n" +
                                    "1️⃣ Откройте Termux (установили в уроке Сеть и серверы)\n" +
                                    "2️⃣ Обновите пакеты:\n" +
                                    "   apt update && apt upgrade\n" +
                                    "3️⃣ Установите proot-distro:\n" +
                                    "   apt install proot-distro\n" +
                                    "4️⃣ Посмотрите доступные дистрибутивы:\n" +
                                    "   proot-distro list\n" +
                                    "5️⃣ Установите Ubuntu:\n" +
                                    "   proot-distro install ubuntu\n" +
                                    "6️⃣ Запустите Ubuntu:\n" +
                                    "   proot-distro login ubuntu\n\n" +
                                    "🔹 **ПЕРВЫЙ ЗАПУСК LINUX:**\n\n" +
                                    "• Вы попадёте в оболочку bash от имени root (внутри окружения)\n" +
                                    "• Обновите пакеты Ubuntu:\n" +
                                    "   apt update && apt upgrade\n" +
                                    "• Установите полезные программы:\n" +
                                    "   apt install nano git curl wget htop\n" +
                                    "• Выйти из окружения: exit\n\n" +
                                    "🔹 **УСТАНОВКА ДРУГИХ ДИСТРИБУТИВОВ:**\n\n" +
                                    "• Debian:\n" +
                                    "   proot-distro install debian\n" +
                                    "   proot-distro login debian\n" +
                                    "• Arch Linux:\n" +
                                    "   proot-distro install archlinux\n" +
                                    "   proot-distro login archlinux\n\n" +
                                    "🔹 **ЧТО МОЖНО ДЕЛАТЬ В LINUX НА ТЕЛЕФОНЕ:**\n\n" +
                                    "• Запускать Python, Node.js, Golang скрипты\n" +
                                    "• Компилировать программы (gcc, g++)\n" +
                                    "• Поднимать веб-серверы (nginx, apache)\n" +
                                    "• Работать с Git\n" +
                                    "• Использовать nmap, wireshark (для сканирования сети)\n" +
                                    "• Редактировать файлы через nano/vim\n\n" +
                                    "🔹 **ПОВЫШЕНИЕ ПРОИЗВОДИТЕЛЬНОСТИ:**\n\n" +
                                    "• Закройте фоновые приложения\n" +
                                    "• Используйте терминал вместо графического интерфейса\n" +
                                    "• Для тяжёлых задач используйте Andronix + VNC (графический Linux)\n\n" +
                                    "🔹 **УСТАНОВКА ГРАФИЧЕСКОГО LINUX (опционально, требует VNC):**\n\n" +
                                    "• Установите Andronix из Play Маркет\n" +
                                    "• Выберите дистрибутив и окружение (XFCE, LXQT)\n" +
                                    "• Скопируйте команды из приложения в Termux\n" +
                                    "• Установите VNC Viewer и подключитесь к localhost:5901\n" +
                                    "• Вы увидите полноценный рабочий стол Linux\n\n" +
                                    "💡 **СОВЕТ:** Для новичков лучше начинать с Ubuntu через proot-distro – " +
                                    "он самый стабильный. Для графического режима нужно много ОЗУ (минимум 3-4 ГБ).",
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
                    Text("Я установил Linux через Termux + proot", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = termuxProotSetup,
                        onCheckedChange = { termuxProotSetup = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (termuxProotSetup) {
                item {
                    Text("✅ Linux внутри Android готов к работе!", color = Color(0xFF2E8058), fontSize = 14.sp)
                }
                item {
                    Button(
                        onClick = { score += 30; step = 3 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                    ) {
                        Text("ДАЛЕЕ →", color = Color.Black)
                    }
                }
            } else {
                item {
                    Text("❌ Подтвердите установку Linux", color = Color( 0xFF9B0C3F), fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 3: Windows через Limbo (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("🪟 ШАГ 3: ЗАПУСК WINDOWS ЧЕРЕЗ LIMBO (QEMU)", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Limbo – это порт QEMU для Android, позволяющий запускать Windows, Linux и другие ОС.\n\n" +
                                    "⚠️ **ВАЖНО:** Для запуска Windows нужно мощное устройство (Snapdragon 845+, 4+ ГБ ОЗУ)\n\n" +
                                    "🔹 **УСТАНОВКА LIMBO:**\n\n" +
                                    "1️⃣ Скачайте Limbo с официального сайта или из Play Маркет (бесплатно)\n" +
                                    "2️⃣ Установите приложение\n" +
                                    "3️⃣ Скачайте образ Windows (например, Windows XP, 7, 10 Lite)\n" +
                                    "   • Windows XP – ~500 МБ, работает быстрее всего\n" +
                                    "   • Windows 7 – ~1 ГБ, требует больше ресурсов\n" +
                                    "   • Windows 10 – только Lite-версии, очень медленно\n\n" +
                                    "🔹 **НАСТРОЙКА ВИРТУАЛЬНОЙ МАШИНЫ:**\n\n" +
                                    "1️⃣ Откройте Limbo и нажмите «New VM»\n" +
                                    "2️⃣ Настройте параметры:\n" +
                                    "   • **Memory (RAM)** – 1024-2048 МБ (чем больше, тем лучше)\n" +
                                    "   • **CPU Cores** – 2-4 ядра\n" +
                                    "   • **CPU Model** – qemu64 или host (экспериментально)\n" +
                                    "   • **Hard Disk A** – выберите скачанный образ .img или .qcow2\n" +
                                    "   • **Network** – user (для доступа в интернет)\n" +
                                    "   • **VGA** – cirrus или virtio\n" +
                                    "3️⃣ Нажмите «Play» для запуска\n\n" +
                                    "🔹 **ЧТО МОЖНО ЗАПУСТИТЬ НА WINDOWS НА ТЕЛЕФОНЕ:**\n\n" +
                                    "• Старые игры (до 2005 года)\n" +
                                    "• Офисные приложения (Word, Excel)\n" +
                                    "• Программы, которые не имеют аналогов на Android\n" +
                                    "• Тестирование ПО в изолированной среде\n\n" +
                                    "🔹 **РЕКОМЕНДАЦИИ ДЛЯ Windows XP:**\n\n" +
                                    "• Установите Windows XP SP3 Lite (до 500 МБ)\n" +
                                    "• Отключите все визуальные эффекты\n" +
                                    "• Используйте классическую тему оформления\n" +
                                    "• Не запускайте тяжёлые приложения\n" +
                                    "• Подключите клавиатуру и мышь через Bluetooth/USB OTG\n\n" +
                                    "🔹 **ОПТИМИЗАЦИЯ ПРОИЗВОДИТЕЛЬНОСТИ:**\n\n" +
                                    "• Закройте все фоновые приложения\n" +
                                    "• Включите режим полёта (отключите сотовую связь)\n" +
                                    "• Используйте активное охлаждение (вентилятор)\n" +
                                    "• Снизьте разрешение экрана\n" +
                                    "• Отключите KVM (если включено)\n\n" +
                                    "🔹 **АЛЬТЕРНАТИВЫ LIMBO:**\n\n" +
                                    "• **BOCHS** – очень медленный, но запускает Windows 95/98\n" +
                                    "• **Winlator** – запуск Windows-приложений (без эмуляции ОС)\n" +
                                    "• **ExaGear** – устарел, но можно найти старые версии\n\n" +
                                    "💡 **СОВЕТ:** Для Windows 95/98 используйте BOCHS – он проще в настройке. " +
                                    "Для Windows XP и новее – только Limbo. " +
                                    "Не ждите чуда – даже топовые телефоны еле тянут Windows 10.",
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
                    Text("Я изучил запуск Windows через Limbo", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = windowsEmulation,
                        onCheckedChange = { windowsEmulation = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (windowsEmulation) {
                item {
                    Text("✅ Эмуляция Windows освоена!", color = Color(0xFF2E8058), fontSize = 14.sp)
                }
                item {
                    Button(
                        onClick = { score += 30; step = 4 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                    ) {
                        Text("ДАЛЕЕ →", color = Color.Black)
                    }
                }
            } else {
                item {
                    Text("❌ Подтвердите, что вы изучили эмуляцию Windows", color = Color( 0xFF9B0C3F), fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 4: Эмуляция игровых консолей (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("🎮 ШАГ 4: ЭМУЛЯЦИЯ ИГРОВЫХ КОНСОЛЕЙ", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Android – отличная платформа для эмуляции старых игровых консолей.\n\n" +
                                    "🔹 **ЛУЧШИЕ ЭМУЛЯТОРЫ ДЛЯ ANDROID:**\n\n" +
                                    "• **RetroArch** – универсальный эмулятор (все системы в одном)\n" +
                                    "• **PPSSPP** – PlayStation Portable\n" +
                                    "• **DuckStation** – PlayStation 1\n" +
                                    "• **AetherSX2** – PlayStation 2 (требует мощного телефона)\n" +
                                    "• **Dolphin Emulator** – GameCube и Wii\n" +
                                    "• **DraStic** – Nintendo DS\n" +
                                    "• **Citra** – Nintendo 3DS\n" +
                                    "• **My Boy!** – GameBoy Advanced\n" +
                                    "• **ePSXe** – PlayStation 1\n\n" +
                                    "🔹 **УСТАНОВКА RETROARCH (рекомендуется для новичков):**\n\n" +
                                    "1️⃣ Скачайте RetroArch из Play Маркет\n" +
                                    "2️⃣ Откройте приложение и дайте разрешения\n" +
                                    "3️⃣ Загрузите ядро (Core) нужной консоли: Main Menu → Load Core → Download Core\n" +
                                    "4️⃣ Выберите игру: Load Content → папка с ROM-файлом\n" +
                                    "5️⃣ Наслаждайтесь игрой\n\n" +
                                    "🔹 **ГДЕ БРАТЬ ИГРЫ (ROM)?**\n\n" +
                                    "• Создайте образы с ваших лицензионных дисков\n" +
                                    "• Используйте только для игр, которые у вас есть в оригинале\n" +
                                    "• Форматы: .iso, .bin, .cue, .nes, .gb, .gba, .nds, .psp\n\n" +
                                    "🔹 **НАСТРОЙКА УПРАВЛЕНИЯ:**\n\n" +
                                    "• RetroArch поддерживает геймпады (подключите по Bluetooth)\n" +
                                    "• Можно использовать сенсорные кнопки (настраиваются в опциях)\n" +
                                    "• Для некоторых эмуляторов внешний геймпад обязателен\n\n" +
                                    "🔹 **ПОВЫШЕНИЕ ПРОИЗВОДИТЕЛЬНОСТИ:**\n\n" +
                                    "• Включите форвард-рендеринг (skip frames)\n" +
                                    "• Понизьте разрешение (1x вместо 4x)\n" +
                                    "• Отключите сложные шейдеры\n" +
                                    "• Используйте аппаратное ускорение (Hardware Renderer)\n" +
                                    "• Для PS2 и GameCube нужно устройство с Snapdragon 865+\n\n" +
                                    "🔹 **ЧТО МОЖНО ЗАПУСТИТЬ НА СЛАБЫХ ТЕЛЕФОНАХ:**\n\n" +
                                    "• GameBoy (все версии)\n" +
                                    "• NES, SNES\n" +
                                    "• Sega Genesis\n" +
                                    "• PlayStation 1 (с настройками)\n" +
                                    "• PSP (не все игры)\n\n" +
                                    "🔹 **ЧТО НУЖНО ДЛЯ PS2 И GAMECUBE:**\n\n" +
                                    "• Snapdragon 845 и выше\n" +
                                    "• 6+ ГБ ОЗУ\n" +
                                    "• Активное охлаждение\n" +
                                    "• Настройки на минималках\n\n" +
                                    "💡 **СОВЕТ:** Начните с RetroArch и эмуляции GameBoy – это самый простой способ. " +
                                    "Постепенно переходите к более сложным консолям. " +
                                    "Используйте внешний геймпад для комфортной игры.",
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
                    Text("Я изучил эмуляцию игровых консолей", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = linuxInstalled,
                        onCheckedChange = { linuxInstalled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (linuxInstalled) {
                item {
                    Text("✅ Эмуляция игр освоена!", color = Color(0xFF2E8058), fontSize = 14.sp)
                }
                item {
                    Button(
                        onClick = { score += 30; step = 5 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                    ) {
                        Text("ЗАВЕРШИТЬ УРОК", color = Color.Black)
                    }
                }
            } else {
                item {
                    Text("❌ Подтвердите изучение", color = Color( 0xFF9B0C3F), fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ФИНАЛЬНЫЙ ЭКРАН (ИСПРАВЛЕН - добавлен скролл)
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
                Text("🎉 ВЫ ОСВОИЛИ ЭМУЛЯЦИЮ НА ANDROID КАК ЭКСПЕРТ!", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Вы заработали $score очков.", fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "📚 **ЧТО ДАЛЬШЕ?**\n\n" +
                            "• Скачайте готовые образы Linux с предустановленными программами\n" +
                            "• Настройте автоматический запуск VMOS с нужными приложениями\n" +
                            "• Поэкспериментируйте с различными эмуляторами консолей\n" +
                            "• Создайте свой ретро-кинозал с RetroArch\n" +
                            "• Переходите к практическому использованию: поднимите сервер, сайт, бота\n\n" +
                            "🔧 **ПОЛЕЗНЫЕ РЕСУРСЫ:**\n" +
                            "• RetroArch документация – docs.libretro.com\n" +
                            "• Proot-distro GitHub – github.com/termux/proot-distro\n" +
                            "• VMOS официальный сайт – vmos.com\n" +
                            "• Limbo эмулятор – github.com/limboemu/limbo\n\n" +
                            "💡 **ПОСЛЕДНИЙ СОВЕТ:**\n" +
                            "Эмуляция – это мощный инструмент, но не забывайте о производительности. " +
                            "На телефонах есть ограничения по нагреву и батарее. " +
                            "Для серьёзной работы лучше использовать компьютер. " +
                            "Но для ретро-игр и тестирования ПО – эмуляция идеальный вариант. " +
                            "Наслаждайтесь!",
                    fontSize = 15.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "lesson_emulation", true)
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