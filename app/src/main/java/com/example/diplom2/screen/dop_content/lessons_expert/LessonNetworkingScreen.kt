package com.example.diplom2.screen.dop_content.lessons_expert

import android.provider.ContactsContract.CommonDataKinds.StructuredName.PREFIX
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
fun LessonNetworkingScreen(navController: NavController, userId: Long) {
    var termuxInstalled by remember { mutableStateOf(false) }
    var webServerStarted by remember { mutableStateOf(false) }
    var sshConfigured by remember { mutableStateOf(false) }
    var portForwardingLearned by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
        val lessonKey = "lesson_networking"
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
                        Text("🌐 СЕТЬ И СЕРВЕРЫ НА ANDROID – ПОЛНОЕ РУКОВОДСТВО ДЛЯ ЭКСПЕРТА", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("📌 **ЧТО МОЖНО СДЕЛАТЬ С СЕТЬЮ НА ANDROID?**", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Ваш телефон может не только потреблять интернет, но и выступать в роли сервера. " +
                                    "Это открывает огромные возможности для автоматизации и удалённого доступа.\n\n" +
                                    "⚡ **ЧТО МОЖНО НАСТРОИТЬ?**\n" +
                                    "• **Веб-сервер** (nginx, Apache) – хостить сайты прямо с телефона\n" +
                                    "• **SSH-сервер** – удалённое управление телефоном через терминал\n" +
                                    "• **FTP-сервер** – быстрая передача файлов по Wi-Fi\n" +
                                    "• **Медиасервер** (Plex, Jellyfin) – стриминг фильмов на телевизор\n" +
                                    "• **VPN-сервер** – доступ к домашней сети из любой точки\n" +
                                    "• **DLNA** – делиться медиа с телевизором и колонками\n" +
                                    "• **Проброс портов** – доступ к серверам на телефоне из интернета\n" +
                                    "• **Анализ сети** – сканер портов, трассировка, проверка скорости\n\n" +
                                    "🛠️ **ИНСТРУМЕНТЫ:**\n" +
                                    "• **Termux** – эмулятор терминала для установки серверов\n" +
                                    "• **KSWEB** – готовый веб-сервер (PHP, MySQL, nginx) для новичков\n" +
                                    "• **SSH Server** – простой SSH-сервер с графическим интерфейсом\n" +
                                    "• **Servers Ultimate** – более десятка серверов в одном приложении\n" +
                                    "• **FTP Server** – быстрый FTP-сервер\n\n" +
                                    "🎯 **ЭТОТ УРОК НАУЧИТ:**\n" +
                                    "1️⃣ Настройке Termux и установке пакетов\n" +
                                    "2️⃣ Запуску веб-сервера nginx на телефоне\n" +
                                    "3️⃣ Настройке SSH-сервера для удалённого доступа\n" +
                                    "4️⃣ Пробросу портов через ADB и на роутере\n" +
                                    "5️⃣ Использованию телефона как сервера в локальной сети\n\n" +
                                    "⚠️ **ВАЖНО:**\n" +
                                    "• Выход в интернет через провайдера может блокировать входящие подключения\n" +
                                    "• Для доступа к серверу из интернета нужен белый IP или DDNS\n" +
                                    "• Безопасность имеет первостепенное значение – ставьте сложные пароли\n" +
                                    "• На телефоне нет такой защиты, как на серверных ОС, будьте аккуратны",
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
                ) { Text("НАЧАТЬ НАСТРОЙКУ СЕТИ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black) }
                Spacer(modifier = Modifier.height(32.dp)) // ДОБАВЛЕНО: отступ снизу
            }
        }
        return
    }

    // ШАГ 1: Установка Termux и базовая настройка (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("📦 ШАГ 1: УСТАНОВКА TERMUX И БАЗОВАЯ НАСТРОЙКА", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Termux – это эмулятор терминала Linux для Android. " +
                                    "Он позволяет устанавливать и запускать серверы прямо на телефоне.\n\n" +
                                    "🔹 **УСТАНОВКА TERMUX:**\n\n" +
                                    "• **Из Google Play Маркет** – последняя версия (рекомендуется)\n" +
                                    "• **Из F-Droid** – альтернативный магазин\n" +
                                    "• **С официального сайта** – termux.com\n\n" +
                                    "🔹 **ПЕРВЫЙ ЗАПУСК И НАСТРОЙКА:**\n\n" +
                                    "1️⃣ Откройте Termux\n" +
                                    "2️⃣ Дайте разрешения (доступ к файлам – для работы с /sdcard)\n" +
                                    "3️⃣ Обновите пакеты:\n" +
                                    "   apt update && apt upgrade\n" +
                                    "4️⃣ Дайте ответ «Y» при запросе (если спросит)\n" +
                                    "5️⃣ Установите базовые пакеты:\n" +
                                    "   apt install nano git curl wget\n" +
                                    "6️⃣ Дайте разрешение на установку из неизвестных источников (если потребуется)\n\n" +
                                    "🔹 **РАСШИРЕННАЯ НАСТРОЙКА (для доступа к `/sdcard`):**\n\n" +
                                    "• Termux по умолчанию не видит внутреннюю память телефона. Исправляется так:\n" +
                                    "   termux-setup-storage\n" +
                                    "• После этого появится папка `~/storage` с ссылками на DCIM, Music, Download\n\n" +
                                    "🔹 **УСТАНОВКА ВЕБ-СЕРВЕРА (nginx):**\n\n" +
                                    "• Установите nginx:\n" +
                                    "   apt install nginx\n" +
                                    "• Запустите nginx:\n" +
                                    "   nginx\n" +
                                    "• Проверьте, работает ли:\n" +
                                    "   curl localhost:8080 (nginx на Termux использует порт 8080)\n" +
                                    "• Откройте в браузере на телефоне: http://localhost:8080\n" +
                                    "• Увидите приветствие nginx – всё работает!\n\n" +
                                    "🔹 **ГДЕ ЛЕЖАТ ФАЙЛЫ ВЕБ-СЕРВЕРА?**\n\n" +
                                    "• По умолчанию: `$PREFIX/share/nginx/html`\n" +
                                    "• Полный путь можно узнать через: `nginx -t`\n" +
                                    "• Вы можете положить туда свой index.html\n" +
                                    "• Файлы из `/sdcard` можно подключить через символическую ссылку\n\n" +
                                    "🔹 **ДОСТУП К ВЕБ-СЕРВЕРУ С ДРУГИХ УСТРОЙСТВ В ЛОКАЛЬНОЙ СЕТИ:**\n\n" +
                                    "• Узнайте IP вашего телефона в Wi-Fi:\n" +
                                    "   ip -4 addr show wlan0 | grep -oP '(?<=inet\\s)\\d+(\\.\\d+){3}'\n" +
                                    "  (в Termux благодаря пакету net-tools)\n" +
                                    "• Или в настройках телефона: Wi-Fi → ваша сеть → IP-адрес\n" +
                                    "• На другом устройстве в той же сети откройте: http://IP_телефона:8080\n\n" +
                                    "🔹 **ЧТО ДЕЛАТЬ, ЕСЛИ НЕ РАБОТАЕТ?**\n\n" +
                                    "• Проверьте, что устройства в одной сети\n" +
                                    "• На телефоне может быть включён файервол (AFWall+ – разрешите nginx)\n" +
                                    "• Перезапустите nginx: nginx -s reload\n\n" +
                                    "💡 **СОВЕТ:** Termux лучше устанавливать из F-Droid – там версии свежее. " +
                                    "В Google Play иногда лежат старые версии, несовместимые с новыми репозиториями.",
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
                    Text("Я установил Termux и запустил веб-сервер nginx", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = termuxInstalled,
                        onCheckedChange = { termuxInstalled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (termuxInstalled) {
                item {
                    Text("✅ Веб-сервер запущен! Можно переходить к SSH.", color = Color(0xFF2E8058), fontSize = 14.sp)
                }
                item {
                    Button(
                        onClick = { score += 30; step = 2 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                    ) {
                        Text("ДАЛЕЕ →", color = Color.Black)
                    }
                }
            } else {
                item {
                    Text("❌ Подтвердите установку Termux и запуск сервера", color = Color( 0xFF9B0C3F), fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 2: Настройка SSH-сервера (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("🔐 ШАГ 2: НАСТРОЙКА SSH-СЕРВЕРА ДЛЯ УДАЛЁННОГО УПРАВЛЕНИЯ", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "SSH (Secure Shell) позволяет управлять телефоном через терминал с другого компьютера. " +
                                    "Это очень удобно для автоматизации и отладки.\n\n" +
                                    "🔹 **УСТАНОВКА SSH-СЕРВЕРА (openssh):**\n\n" +
                                    "• В Termux выполните:\n" +
                                    "   apt install openssh\n" +
                                    "• Запустите SSH-сервер:\n" +
                                    "   sshd\n" +
                                    "• Сервер запустится на порту 8022 (стандартный 22 занят системой)\n\n" +
                                    "🔹 **УСТАНОВКА ПАРОЛЯ:**\n\n" +
                                    "• Установите пароль для текущего пользователя:\n" +
                                    "   passwd\n" +
                                    "• Введите пароль дважды (он не отображается при вводе)\n\n" +
                                    "🔹 **ПОДКЛЮЧЕНИЕ ПО SSH С КОМПЬЮТЕРА:**\n\n" +
                                    "• На компьютере (Windows, macOS, Linux) выполните:\n" +
                                    "   ssh -p 8022 имя_пользователя@IP_телефона\n" +
                                    "• Имя пользователя в Termux – `whoami` (обычно `u0_aXXX`)\n" +
                                    "• Узнать IP телефона: `ip -4 addr show wlan0`\n\n" +
                                    "🔹 **ПОДКЛЮЧЕНИЕ ПО SSH С ДРУГОГО ТЕЛЕФОНА:**\n\n" +
                                    "• Установите Termux на второй телефон\n" +
                                    "• Выполните: ssh -p 8022 пользователь@IP_телефона-сервера\n\n" +
                                    "🔹 **НАСТРОЙКА SSH ПО КЛЮЧУ (без пароля, безопаснее):**\n\n" +
                                    "• На компьютере сгенерируйте ключ:\n" +
                                    "   ssh-keygen -t rsa -b 4096\n" +
                                    "• Скопируйте публичный ключ на телефон:\n" +
                                    "   ssh-copy-id -p 8022 пользователь@IP_телефона\n" +
                                    "• Или вручную добавьте содержимое ~/.ssh/id_rsa.pub в ~/.ssh/authorized_keys на телефоне\n\n" +
                                    "🔹 **ОСТАНОВКА SSH-СЕРВЕРА:**\n\n" +
                                    "• Найти PID процесса: ps aux | grep sshd\n" +
                                    "• Убить процесс: kill PID\n" +
                                    "• Или просто выключить Wi-Fi – соединение оборвётся\n\n" +
                                    "🔹 **ЧТО МОЖНО ДЕЛАТЬ ПО SSH:**\n\n" +
                                    "• Выполнять команды, как будто вы в Termux\n" +
                                    "• Передавать файлы через SCP: scp -P 8022 file.txt пользователь@IP:/sdcard/\n" +
                                    "• Управлять серверами (nginx, MySQL)\n" +
                                    "• Смотреть логи (logcat, dmesg)\n" +
                                    "• Редактировать файлы через nano\n" +
                                    "• Запускать скрипты автоматизации\n\n" +
                                    "💡 **СОВЕТ:** Для защиты от несанкционированного доступа используйте только ключи, " +
                                    "отключите вход по паролю в настройках sshd. При работе в общественных сетях используйте VPN.",
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
                    Text("Я настроил SSH-сервер и подключился к нему", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = sshConfigured,
                        onCheckedChange = { sshConfigured = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (sshConfigured) {
                item {
                    Text("✅ SSH-сервер работает! Теперь можно управлять телефоном удалённо.", color = Color(0xFF2E8058), fontSize = 14.sp)
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
                    Text("❌ Подтвердите настройку SSH", color = Color( 0xFF9B0C3F), fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 3: Проброс портов и доступ из интернета (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("🔄 ШАГ 3: ПРОБРОС ПОРТОВ И ДОСТУП ИЗ ИНТЕРНЕТА", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Чтобы сервер на телефоне был доступен из интернета (а не только в локальной сети), " +
                                    "нужно настроить проброс портов.\n\n" +
                                    "🔹 **ТИПЫ IP-АДРЕСОВ:**\n\n" +
                                    "• **Локальный IP** – внутри вашей Wi-Fi сети (192.168.x.x, 10.x.x.x)\n" +
                                    "• **Внешний IP** – адрес вашего роутера в интернете (его могут скрывать провайдеры)\n" +
                                    "• **Белый (статический) IP** – редкий и часто платный\n" +
                                    "• **Серый IP** – большинство провайдеров, порты заблокированы\n\n" +
                                    "🔹 **СПОСОБ 1: ПРОБРОС ПОРТОВ НА РОУТЕРЕ**\n\n" +
                                    "1️⃣ Зайдите в настройки роутера (обычно 192.168.0.1 или 192.168.1.1)\n" +
                                    "2️⃣ Найдите раздел «Проброс портов» (Port Forwarding)\n" +
                                    "3️⃣ Добавьте правило:\n" +
                                    "   • Внешний порт: 8080 (или любой)\n" +
                                    "   • Внутренний IP: IP вашего телефона в сети (например, 192.168.1.100)\n" +
                                    "   • Внутренний порт: 8080 (порт вашего сервера)\n" +
                                    "   • Протокол: TCP\n" +
                                    "4️⃣ Сохраните и примените\n" +
                                    "5️⃣ Теперь сервер доступен по внешнему IP роутера (можно узнать через 2ip.ru)\n\n" +
                                    "🔹 **СПОСОБ 2: ADB REVERSE (ТОЛЬКО ДЛЯ ОТЛАДКИ)**\n\n" +
                                    "• Позволяет обращаться к серверу на телефоне через компьютер\n" +
                                    "• На компьютере: adb reverse tcp:8080 tcp:8080\n" +
                                    "• Теперь на компьютере можно открыть localhost:8080\n" +
                                    "• Не требует роутера, но работает только при подключении к компьютеру\n\n" +
                                    "🔹 **СПОСОБ 3: СЕРВИСЫ ВРОДЕ NGROK (ДЛЯ ТЕСТИРОВАНИЯ)**\n\n" +
                                    "• Установите ngrok в Termux:\n" +
                                    "   wget https://bin.equinox.io/c/4VmDzA7iaHb/ngrok-stable-linux-arm64.zip\n" +
                                    "   unzip ngrok-stable-linux-arm64.zip\n" +
                                    "   ./ngrok http 8080\n" +
                                    "• Получите временный адрес вида https://xxxx.ngrok.io\n" +
                                    "• Доступен из интернета без проброса портов\n" +
                                    "• Бесплатно, но адрес меняется при каждом запуске\n\n" +
                                    "🔹 **СПОСОБ 4: DDNS (ДИНАМИЧЕСКИЙ DNS)**\n\n" +
                                    "• Если у вас динамический внешний IP, установите DDNS-клиент\n" +
                                    "• Например, duckdns.org – бесплатно\n" +
                                    "• Установите duckdns в Termux:\n" +
                                    "   git clone https://github.com/duckduckgo/duckdns-client.git\n" +
                                    "• Настройте cron для обновления IP\n\n" +
                                    "🔹 **БЕЗОПАСНОСТЬ ПРИ ПРОБРОСЕ ПОРТОВ:**\n\n" +
                                    "• Не открывайте порты без необходимости\n" +
                                    "• Используйте нестандартные порты (не 22, 80, 443)\n" +
                                    "• Установите фаервол на телефоне (AFWall+)\n" +
                                    "• Используйте SSH вместо открытых HTTP\n" +
                                    "• Для веб-сервера добавьте базовую аутентификацию\n\n" +
                                    "💡 **СОВЕТ:** Для тестирования лучше всего подходит ngrok – не нужно возиться с роутером. " +
                                    "Для постоянного доступа – проброс портов или DDNS.",
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
                    Text("Я изучил проброс портов и способы доступа из интернета", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = portForwardingLearned,
                        onCheckedChange = { portForwardingLearned = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (portForwardingLearned) {
                item {
                    Text("✅ Отлично! Теперь вы знаете, как сделать сервер доступным извне.", color = Color(0xFF2E8058), fontSize = 14.sp)
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
                    Text("❌ Подтвердите изучение проброса портов", color = Color( 0xFF9B0C3F), fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 4: Анализ сети и полезные утилиты (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("🌍 ШАГ 4: АНАЛИЗ СЕТИ И ПОЛЕЗНЫЕ УТИЛИТЫ", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Умение анализировать сеть поможет диагностировать проблемы и повысить безопасность.\n\n" +
                                    "🔹 **ОСНОВНЫЕ УТИЛИТЫ (все доступны в Termux):**\n\n" +
                                    "• **ping** – проверка доступности узла\n" +
                                    "   ping google.com\n" +
                                    "   ping -c 4 8.8.8.8\n" +
                                    "• **traceroute** – маршрут до узла\n" +
                                    "   traceroute google.com\n" +
                                    "   (установка: apt install traceroute)\n" +
                                    "• **netstat** – открытые порты и соединения\n" +
                                    "   netstat -tulpn\n" +
                                    "   netstat -an | grep LISTEN\n" +
                                    "• **nmap** – сканер портов (не входит в Termux, но можно собрать)\n" +
                                    "   apt install nmap\n" +
                                    "   nmap -sS 192.168.1.1/24\n" +
                                    "• **curl/wget** – запросы к веб-серверам\n" +
                                    "   curl ifconfig.me (узнать внешний IP)\n" +
                                    "   curl -I google.com (только заголовки)\n" +
                                    "• **tcpdump** – анализ трафика (root)\n" +
                                    "   apt install tcpdump\n" +
                                    "   tcpdump -i wlan0\n" +
                                    "• **iptables** – управление фаерволом (root)\n" +
                                    "   iptables -L (список правил)\n" +
                                    "   iptables -A INPUT -p tcp --dport 8080 -j ACCEPT\n\n" +
                                    "🔹 **АНАЛИЗ WI-FI И БЛИЖАЙШИХ СЕТЕЙ:**\n\n" +
                                    "• Список доступных сетей (Android API, требует разрешений):\n" +
                                    "   dumpsys wifi | grep -A 10 \"WifiConfiguration\"\n" +
                                    "• Сила сигнала: dumpsys wifi | grep -i signal\n" +
                                    "• Утилита в Termux для сканирования (не root): wavemon (не всегда доступна)\n\n" +
                                    "🔹 **ПОЛУЧЕНИЕ ИНФОРМАЦИИ ОБ IP:**\n\n" +
                                    "• Узнать локальный IP:\n" +
                                    "   ifconfig wlan0 | grep inet\n" +
                                    "   ip -4 addr show wlan0\n" +
                                    "• Узнать внешний IP:\n" +
                                    "   curl ifconfig.me\n" +
                                    "   curl 2ip.ru\n" +
                                    "• Информация о домене:\n" +
                                    "   dig google.com\n" +
                                    "   nslookup google.com\n\n" +
                                    "🔹 **ДИАГНОСТИКА ПРОБЛЕМ:\n\n" +
                                    "• **Проверка, открыт ли порт на телефоне:**\n" +
                                    "   netstat -tulpn | grep 8080\n" +
                                    "• **Проверка, открыт ли порт на роутере (из интернета):**\n" +
                                    "   Используйте онлайн-сервисы, например, portchecker.co\n" +
                                    "• **Трассировка до проблемного сайта:**\n" +
                                    "   traceroute example.com\n" +
                                    "• **Проверка DNS-сервера:**\n" +
                                    "   nslookup google.com 8.8.8.8\n\n" +
                                    "🔹 **ПОЛЕЗНЫЕ ПРИЛОЖЕНИЯ ДЛЯ АНАЛИЗА СЕТИ:**\n\n" +
                                    "• **Fing** – сканер сети, показывает все устройства в локальной сети\n" +
                                    "• **PingTools** – набор утилит (ping, traceroute, сканер портов)\n" +
                                    "• **Network Analyzer** – информация о Wi-Fi, сигнале, IP\n" +
                                    "• **Termius** – SSH-клиент для подключения к удалённым серверам\n\n" +
                                    "💡 **СОВЕТ:** Для глубокого анализа сетевого трафика используйте tcpdump " +
                                    "и анализируйте дамп через Wireshark на компьютере.",
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
                    Text("Я изучил сетевые утилиты и анализ сети", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = webServerStarted,
                        onCheckedChange = { webServerStarted = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (webServerStarted) {
                item {
                    Text("✅ Поздравляю! Вы освоили сетевые возможности Android!", color = Color(0xFF2E8058), fontSize = 14.sp)
                }
                item {
                    Button(
                        onClick = { score += 40; step = 5 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                    ) {
                        Text("ЗАВЕРШИТЬ УРОК", color = Color.Black)
                    }
                }
            } else {
                item {
                    Text("❌ Подтвердите, что вы изучили утилиты", color = Color( 0xFF9B0C3F), fontSize = 14.sp)
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
                Text("🎉 ВЫ СТАЛИ ЭКСПЕРТОМ ПО СЕТЕВЫМ ТЕХНОЛОГИЯМ НА ANDROID!", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Вы заработали $score очков.", fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "📚 **ЧТО ДАЛЬШЕ?**\n\n" +
                            "• Настройте автоматические бэкапы файлов по SSH\n" +
                            "• Организуйте домашнюю медиатеку с Plex или Jellyfin\n" +
                            "• Запустите своего Telegram/WhatsApp бота на телефоне\n" +
                            "• Используйте Termux для автоматизации задач по расписанию (cron)\n" +
                            "• Переходите к следующим урокам: Эмуляция, Оптимизация (уже прошли), Безопасность (прошли)\n\n" +
                            "🔧 **ПОЛЕЗНЫЕ ПРОДОЛЖЕНИЯ:**\n" +
                            "• Настройка VPN-сервера (OpenVPN, WireGuard) на телефоне\n" +
                            "• Создание собственного облака с Nextcloud на телефоне\n" +
                            "• Запуск Docker (через UserLAnd или Termux с proot)\n" +
                            "• Мониторинг сети с помощью Zabbix или Prometheus\n\n" +
                            "💡 **ПОСЛЕДНИЙ СОВЕТ:**\n" +
                            "Телефон – это не только потребитель контента, но и мощный сервер. " +
                            "Используйте его для экспериментов: поднимите сайт, бота, хранилище файлов. " +
                            "Главное – не забывайте о безопасности и бэкапах.",
                    fontSize = 15.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "lesson_networking", true)
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