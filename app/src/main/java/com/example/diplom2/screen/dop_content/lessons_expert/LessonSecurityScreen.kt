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
fun LessonSecurityScreen(navController: NavController, userId: Long) {
    var firewallInstalled by remember { mutableStateOf(false) }
    var permissionsChecked by remember { mutableStateOf(false) }
    var magiskHideConfigured by remember { mutableStateOf(false) }
    var encryptionChecked by remember { mutableStateOf(false) }
    var secureByDefault by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
        val lessonKey = "lesson_security"
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
                        Text("🔐 БЕЗОПАСНОСТЬ ДЛЯ РУТОВАННЫХ УСТРОЙСТВ – ПОЛНОЕ РУКОВОДСТВО", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("📌 **ПОЧЕМУ БЕЗОПАСНОСТЬ ВАЖНА ПРИ ROOT?**", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Root даёт приложениям доступ к системе. Это удобно, но и опасно: " +
                                    "зловредное приложение с root может украсть все ваши данные.\n\n" +
                                    "⚠️ **ГЛАВНЫЕ УГРОЗЫ ПРИ ROOT:**\n\n" +
                                    "• **Кража данных** – паролей, банковских карт, личных фото\n" +
                                    "• **Установка шпионского ПО без вашего ведома**\n" +
                                    "• **Блокировка телефона (вымогатели)** – потребуют выкуп\n" +
                                    "• **Использование вашего телефона в ботнетах** – для DDoS-атак\n" +
                                    "• **Подмена системных файлов** – даже после удаления root могут остаться следы\n\n" +
                                    "✅ **ЧТО МОЖНО СДЕЛАТЬ ДЛЯ ЗАЩИТЫ:**\n\n" +
                                    "1️⃣ **Установить фаервол (AFWall+)** – запретить доступ в интернет подозрительным приложениям\n" +
                                    "2️⃣ **Настроить Magisk Hide** – скрыть root от банков и приложений\n" +
                                    "3️⃣ **Контролировать разрешения** – через AppOps или XPrivacyLua\n" +
                                    "4️⃣ **Включить шифрование устройства** – защитит файлы при потере телефона\n" +
                                    "5️⃣ **Использовать антивирус** – специально для рутованных устройств\n" +
                                    "6️⃣ **Отключить root, когда не нужен** – через Magisk\n" +
                                    "7️⃣ **Не давать root-доступ не проверенным приложениям** – через Magisk диалог\n" +
                                    "8️⃣ **Регулярно проверять список приложений с root-доступом**\n\n" +
                                    "🎯 **ЭТОТ УРОК НАУЧИТ:**\n" +
                                    "1️⃣ Установке и настройке фаервола AFWall+\n" +
                                    "2️⃣ Контролю разрешений приложений через AppOps\n" +
                                    "3️⃣ Настройке Magisk Hide для банковских приложений\n" +
                                    "4️⃣ Шифрованию устройства и защите файлов\n" +
                                    "5️⃣ Базовым правилам безопасности для рутованных устройств\n\n" +
                                    "⚠️ **ВАЖНО:**\n" +
                                    "• Root даёт приложениям полный доступ – будьте внимательны, что устанавливаете\n" +
                                    "• Не давайте root-доступ неизвестным приложениям\n" +
                                    "• Регулярно проверяйте, какие приложения имеют root-доступ\n" +
                                    "• Делайте резервные копии важных данных\n" +
                                    "• Всегда имейте под рукой стоковую прошивку для восстановления",
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
                ) { Text("НАЧАТЬ НАСТРОЙКУ БЕЗОПАСНОСТИ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black) }
                Spacer(modifier = Modifier.height(32.dp)) // ДОБАВЛЕНО: отступ снизу
            }
        }
        return
    }

    // ШАГ 1: Установка и настройка AFWall+ (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("🛡️ ШАГ 1: УСТАНОВКА И НАСТРОЙКА AFWALL+ (ФАЕРВОЛ)", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "AFWall+ (Android Firewall+) – это мощный фаервол, который использует iptables для контроля интернет-доступа приложений.\n\n" +
                                    "🔹 **ЗАЧЕМ НУЖЕН ФАЕРВОЛ?**\n" +
                                    "• Приложения не смогут отправлять ваши данные в интернет без разрешения\n" +
                                    "• Вы контролируете, кто и куда подключается\n" +
                                    "• Экономия трафика (блокировка рекламы и трекеров)\n" +
                                    "• Защита от утечки данных (вредоносное ПО не сможет отправить ваш пароль на сервер)\n\n" +
                                    "🔹 **ГДЕ СКАЧАТЬ AFWALL+?**\n" +
                                    "• Google Play Маркет (есть бесплатная версия)\n" +
                                    "• F-Droid (свободный софт, рекомендуется)\n" +
                                    "• Официальный сайт: github.com/ukanth/afwall\n\n" +
                                    "🔹 **УСТАНОВКА И НАСТРОЙКА:**\n\n" +
                                    "1️⃣ Установите AFWall+ и откройте приложение\n" +
                                    "2️⃣ При первом запуске запросит root-доступ – дайте его\n" +
                                    "3️⃣ Вы увидите список всех приложений с чекбоксами:\n" +
                                    "   • **Wi-Fi** – доступ через Wi-Fi\n" +
                                    "   • **Мобильные данные (2G/3G/4G/5G)** – доступ через мобильную сеть\n" +
                                    "   • **LAN** – доступ в локальную сеть (если нужно)\n" +
                                    "   • **VPN** – доступ через VPN\n\n" +
                                    "4️⃣ **Базовые правила:**\n" +
                                    "   • Браузеру (Chrome, Firefox) – разрешить Wi-Fi + Мобильные данные\n" +
                                    "   • Мессенджерам (Telegram, WhatsApp) – разрешить Wi-Fi + Мобильные данные\n" +
                                    "   • Играм без интернета – запретить всё (кроме Wi-Fi, если нужны обновления)\n" +
                                    "   • Банковским приложениям – разрешить только Wi-Fi (безопаснее)\n" +
                                    "   • Системным приложениям (Google Play, Сервисы) – разрешить всё\n\n" +
                                    "5️⃣ **ПРИМЕРЫ БЛОКИРОВКИ:**\n" +
                                    "   • Игра, которая шлёт рекламу – запретить ей интернет\n" +
                                    "   • Фонарик, который просит доступ в сеть – подозрительно! Заблокировать\n" +
                                    "   • Калькулятор – зачем ему интернет? Заблокировать!\n\n" +
                                    "6️⃣ **Профили в AFWall+:**\n" +
                                    "   • **Домашний профиль** – максимальная свобода (доверенные сети)\n" +
                                    "   • **Уличный профиль** – только мессенджеры и банк\n" +
                                    "   • **Роуминг** – минимум приложений\n\n" +
                                    "7️⃣ **Как применить настройки:**\n" +
                                    "   • Нажмите на кнопку «Применить правила» (огонь) → перезагрузите фаервол\n" +
                                    "   • Правила применяются мгновенно\n" +
                                    "   • Для проверки откройте заблокированное приложение – интернет работать не будет\n\n" +
                                    "🔹 **ДОПОЛНИТЕЛЬНЫЕ ФУНКЦИИ AFWALL+:**\n\n" +
                                    "• **Журнал подключений** – видно, какие приложения пытались подключиться\n" +
                                    "• **Блокировка рекламы** – встроенный хост-файл\n" +
                                    "• **IPv6** – можно отключить для экономии батареи\n" +
                                    "• **Импорт/экспорт правил** – перенос настройки между телефонами\n\n" +
                                    "💡 **СОВЕТ:** Настройте один раз, и вы забудете о фаерволе. " +
                                    "Периодически проверяйте список новых приложений, которым вы разрешили интернет.",
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
                    Text("Я установил и настроил AFWall+ (фаервол)", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = firewallInstalled,
                        onCheckedChange = { firewallInstalled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (firewallInstalled) {
                item {
                    Text("✅ Фаервол защищает ваш трафик!", color = Color.Green, fontSize = 14.sp)
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
                    Text("❌ Подтвердите установку фаервола", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 2: Контроль разрешений (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("🔏 ШАГ 2: КОНТРОЛЬ РАЗРЕШЕНИЙ ЧЕРЕЗ APP OPS / XPRIVACY LUA", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Даже если приложение имеет root, вы можете контролировать, к чему оно имеет доступ.\n\n" +
                                    "🔹 **ЧТО ТАКОЕ APP OPS?**\n" +
                                    "• Встроенный в Android механизм контроля разрешений\n" +
                                    "• Доступен в Настройки → Приложения → Права приложений\n" +
                                    "• Можно запретить доступ к контактам, камере, микрофону, геолокации\n\n" +
                                    "🔹 **XPRIVACY LUA (требуется Xposed Framework)**\n" +
                                    "• Более мощный инструмент для контроля разрешений\n" +
                                    "• Может подменять данные – приложению кажется, что доступ есть, а на самом деле нет\n" +
                                    "• Блокирует доступ к:\n" +
                                    "   • Контактам (подмена пустым списком)\n" +
                                    "   • SMS/MMS (блокировка отправки)\n" +
                                    "   • Геолокации (подмена на случайные координаты)\n" +
                                    "   • Камере и микрофону (полная блокировка)\n" +
                                    "   • Файлам (доступ к виртуальной пустой папке)\n\n" +
                                    "🔹 **КАК НАСТРОИТЬ XPRIVACY LUA:**\n\n" +
                                    "1️⃣ Установите Xposed Framework (через Magisk)\n" +
                                    "2️⃣ Установите XPrivacyLua из репозитория Xposed\n" +
                                    "3️⃣ Откройте XPrivacyLua → выберите приложение\n" +
                                    "4️⃣ Отметьте, что должно быть заблокировано:\n" +
                                    "   • **Internet** – полная блокировка сети\n" +
                                    "   • **Location** – подмена координат\n" +
                                    "   • **Contacts** – подмена пустым списком\n" +
                                    "   • **Phone** – блокировка звонков и SMS\n" +
                                    "   • **Storage** – подмена внешнего хранилища\n" +
                                    "   • **Camera/Microphone** – блокировка записи\n" +
                                    "5️⃣ Включите режим «Ограничить все запросы»\n" +
                                    "6️⃣ Перезагрузите телефон\n\n" +
                                    "🔹 **ПРИМЕРЫ ИСПОЛЬЗОВАНИЯ:**\n\n" +
                                    "• **Приложение-фонарик** требует доступ к контактам? Заблокируйте – ему там не место\n" +
                                    "• **Игра** хочет знать ваше местоположение? Дайте поддельные координаты\n" +
                                    "• **Калькулятор** просит доступ в интернет? Заблокируйте полностью (не получит данные)\n" +
                                    "• **Мессенджер** без вашего ведома отправляет SMS? Заблокируйте SMS-доступ\n\n" +
                                    "🔹 **APP OPS (без root, но с ADB):**\n\n" +
                                    "• Установите App Ops из Play Маркет\n" +
                                    "• Дайте разрешение через ADB: adb shell pm grant com.appops.package android.permission.PACKAGE_USAGE_STATS\n" +
                                    "• Теперь можно запрещать отдельные разрешения приложениям\n\n" +
                                    "⚠️ **ПРЕДУПРЕЖДЕНИЕ:**\n" +
                                    "• Некоторые приложения могут работать некорректно при запрете разрешений\n" +
                                    "• XPrivacyLua требует Xposed – а это потенциально нестабильно\n" +
                                    "• Не блокируйте системные приложения (Google Play, Сервисы) – телефон может работать нестабильно\n\n" +
                                    "💡 **СОВЕТ:** Используйте App Ops для начала – это безопаснее. " +
                                    "XPrivacyLua для опытных пользователей, которые знают, что блокируют.",
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
                    Text("Я настроил контроль разрешений (App Ops / XPrivacyLua)", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = permissionsChecked,
                        onCheckedChange = { permissionsChecked = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (permissionsChecked) {
                item {
                    Text("✅ Разрешения под контролем!", color = Color.Green, fontSize = 14.sp)
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
                    Text("❌ Подтвердите настройку контроля разрешений", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 3: Magisk Hide (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("🕵️ ШАГ 3: MAGISK HIDE – СКРЫТИЕ ROOT ОТ ПРИЛОЖЕНИЙ", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Банковские приложения, Google Pay, Pokémon Go, некоторые игры не работают, если обнаружат root.\n" +
                                    "Magisk Hide позволяет скрыть root именно от этих приложений.\n\n" +
                                    "🔹 **ЧТО ТАКОЕ MAGISK HIDE?**\n" +
                                    "• Встроенная функция Magisk\n" +
                                    "• Маскирует root-признаки от выбранных приложений\n" +
                                    "• Работает на уровне ядра – приложение «думает», что телефона не рутирован\n\n" +
                                    "🔹 **КАК НАСТРОИТЬ MAGISK HIDE?**\n\n" +
                                    "1️⃣ Откройте приложение Magisk\n" +
                                    "2️⃣ Нажмите на иконку щита (Magisk Hide) в правом верхнем углу\n" +
                                    "3️⃣ Поставьте галочки напротив приложений, от которых нужно скрыть root:\n" +
                                    "   • Банки (Сбер, Тинькофф, ВТБ, Альфа)\n" +
                                    "   • Google Pay (Services, Play Store, Google Play Services)\n" +
                                    "   • Игры с защитой (Pokémon Go, PUBG)\n" +
                                    "   • Корпоративные приложения (MDM)\n" +
                                    "4️⃣ Включите Magisk Hide в настройках (одна кнопка)\n" +
                                    "5️⃣ Перезагрузите телефон\n\n" +
                                    "🔹 **УСИЛЕННАЯ МАСКИРОВКА (Magisk Hide Props Config):**\n\n" +
                                    "• Скачайте модуль MagiskHide Props Config из репозитория Magisk\n" +
                                    "• Установите через Magisk\n" +
                                    "• Запустите терминал (Termux) и выполните: props\n" +
                                    "• Выберите пункт 1 (Edit device fingerprint)\n" +
                                    "• Выберите подходящий fingerprint вашей модели (можно стандартный)\n" +
                                    "• Это поможет пройти проверку SafetyNet\n\n" +
                                    "🔹 **ПРОВЕРКА SAFETYNET:**\n" +
                                    "• Скачайте приложение SafetyNet Test из Play Маркет\n" +
                                    "• Запустите тест – он должен показать «Success»\n" +
                                    "• Если не проходит – установите модуль Universal SafetyNet Fix\n\n" +
                                    "🔹 **ПРИМЕРЫ, КОГДА НУЖЕН MAGISK HIDE:**\n\n" +
                                    "• **Сбербанк Онлайн** – при обнаружении root не открывается\n" +
                                    "• **Google Pay** – не даёт добавить карты\n" +
                                    "• **Покемон Го** – не запускается с root\n" +
                                    "• **Нетфликс** – не показывает фильмы HD\n" +
                                    "• **Fortnite** – блокирует рутованные устройства\n\n" +
                                    "⚠️ **ЧТО ДЕЛАТЬ, ЕСЛИ MAGISK HIDE НЕ ПОМОГАЕТ?**\n\n" +
                                    "• Некоторые банки обходят Magisk Hide (например, используют собственные детекторы)\n" +
                                    "• Попробуйте модуль Shamiko (альтернатива Magisk Hide)\n" +
                                    "• Временно удалите Magisk (полная деинсталляция)\n" +
                                    "• Используйте Island или Shelter для изоляции приложений\n\n" +
                                    "💡 **СОВЕТ:** Регулярно обновляйте Magisk – разработчики банков постоянно улучшают детекцию, " +
                                    "но и авторы Magisk Hide тоже не дремлют.",
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
                    Text("Я настроил Magisk Hide", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = magiskHideConfigured,
                        onCheckedChange = { magiskHideConfigured = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (magiskHideConfigured) {
                item {
                    Text("✅ Root скрыт от выбранных приложений!", color = Color.Green, fontSize = 14.sp)
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
                    Text("❌ Подтвердите настройку Magisk Hide", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 4: Шифрование устройства (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("🔒 ШАГ 4: ШИФРОВАНИЕ УСТРОЙСТВА И ЗАЩИТА ФАЙЛОВ", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Если телефон потеряется или его украдут, данные на незашифрованном устройстве можно легко прочитать.\n" +
                                    "Шифрование защищает ваши файлы даже без экрана блокировки.\n\n" +
                                    "🔹 **ЧТО ТАКОЕ ШИФРОВАНИЕ?**\n" +
                                    "• Файлы на телефоне превращаются в бессмысленный набор символов\n" +
                                    "• Для доступа нужен ключ (PIN, пароль или отпечаток)\n" +
                                    "• Без ключа данные невозможно прочитать даже с компьютера\n\n" +
                                    "🔹 **КАК ПРОВЕРИТЬ, ВКЛЮЧЕНО ЛИ ШИФРОВАНИЕ?**\n\n" +
                                    "• Настройки → Безопасность → Шифрование\n" +
                                    "• Там будет надпись «Зашифровано» или «Требуется шифрование»\n" +
                                    "• Или через ADB: adb shell getprop ro.crypto.state → должно быть «encrypted»\n\n" +
                                    "🔹 **КАК ВКЛЮЧИТЬ ШИФРОВАНИЕ?**\n\n" +
                                    "1️⃣ Установите PIN, пароль или графический ключ на блокировку экрана\n" +
                                    "2️⃣ Настройки → Безопасность → Шифрование → «Зашифровать телефон»\n" +
                                    "3️⃣ Телефон попросит ввести PIN/пароль\n" +
                                    "4️⃣ Нажмите «Зашифровать»\n" +
                                    "5️⃣ Телефон перезагрузится и процесс шифрования займёт 20-40 минут\n" +
                                    "6️⃣ После завершения телефон будет зашифрован\n\n" +
                                    "⚠️ **ВАЖНО ПЕРЕД ШИФРОВАНИЕМ:**\n" +
                                    "• Полностью зарядите телефон (не менее 80%)\n" +
                                    "• Не прерывайте процесс – можно получить кирпич\n" +
                                    "• Сделайте бэкап данных (шифрование может пойти не так)\n" +
                                    "• Убедитесь, что у вас есть стоковая прошивка для восстановления\n\n" +
                                    "🔹 **ЧТО ДЕЛАТЬ, ЕСЛИ ТЕЛЕФОН УЖЕ ЗАШИФРОВАН?**\n" +
                                    "• Не получится отключить шифрование без сброса данных\n" +
                                    "• Единственный способ – сброс до заводских настроек\n" +
                                    "• После сброса телефон снова не зашифрован\n\n" +
                                    "🔹 **ДОПОЛНИТЕЛЬНАЯ ЗАЩИТА ФАЙЛОВ (для рутованных):**\n\n" +
                                    "• **EDS Lite** – создание зашифрованных контейнеров (как TrueCrypt)\n" +
                                    "• **Cryptonite** – монтирование encfs-папок (пароль на папку)\n" +
                                    "• **SSE** – шифрование отдельных файлов\n" +
                                    "• **OpenKeychain** – PGP-шифрование для продвинутых\n\n" +
                                    "🔹 **ЗАЩИТА ОТДЕЛЬНЫХ ФАЙЛОВ (без root):**\n\n" +
                                    "• **Solid Explorer** – может шифровать папки\n" +
                                    "• **Andrognito** – скрывает и шифрует файлы\n" +
                                    "• **KeepSafe** – для фото/видео\n\n" +
                                    "💡 **СОВЕТ:** Шифрование всего телефона снижает производительность на 5-10%, " +
                                    "но это стоит того ради безопасности. Если не хотите терять производительность, " +
                                    "храните важные файлы в зашифрованных контейнерах.",
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
                    Text("Я проверил/включил шифрование устройства", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = encryptionChecked,
                        onCheckedChange = { encryptionChecked = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (encryptionChecked) {
                item {
                    Text("✅ Устройство защищено шифрованием!", color = Color.Green, fontSize = 14.sp)
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
                    Text("❌ Подтвердите проверку шифрования", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 5: Базовые правила безопасности (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("✅ ШАГ 5: БАЗОВЫЕ ПРАВИЛА БЕЗОПАСНОСТИ ДЛЯ РУТОВАННЫХ УСТРОЙСТВ", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Соблюдайте эти правила, чтобы root приносил только пользу, а не угрозы.\n\n" +
                                    "🔹 **ЗОЛОТЫЕ ПРАВИЛА:**\n\n" +
                                    "1️⃣ **Устанавливайте приложения ТОЛЬКО из проверенных источников**\n" +
                                    "   • Google Play Маркет\n" +
                                    "   • GitHub (официальные репозитории)\n" +
                                    "   • F-Droid\n" +
                                    "   • XDA-Developers (доверенные разработчики)\n\n" +
                                    "2️⃣ **НЕ давайте root-доступ незнакомым приложениям**\n" +
                                    "   • Magisk покажет диалог – внимательно читайте, что просит приложение\n" +
                                    "   • Если приложение запрашивает root без причины – отказывайте\n" +
                                    "   • Например, калькулятору root не нужен\n\n" +
                                    "3️⃣ **Регулярно проверяйте список приложений с root-доступом**\n" +
                                    "   • Magisk → Разрешения root\n" +
                                    "   • Удалите подозрительные приложения\n\n" +
                                    "4️⃣ **Используйте антивирус (для рутованных устройств)**\n" +
                                    "   • Malwarebytes (есть версия для Android)\n" +
                                    "   • Kaspersky (некоторые функции требуют root)\n" +
                                    "   • Bitdefender\n\n" +
                                    "5️⃣ **Включайте двухфакторную аутентификацию (2FA) везде, где можно**\n" +
                                    "   • Google Authenticator\n" +
                                    "   • Microsoft Authenticator\n" +
                                    "   • Authy\n\n" +
                                    "6️⃣ **НЕ отключайте Play Protect** (даже с root)\n" +
                                    "   • Play Protect сканирует новые приложения\n" +
                                    "   • Не помешает, даже если есть антивирус\n\n" +
                                    "7️⃣ **Сделайте бэкап системы перед опасными действиями**\n" +
                                    "   • Через TWRP (Nandroid backup)\n" +
                                    "   • Перед установкой модулей Magisk\n" +
                                    "   • Перед редактированием системных файлов\n\n" +
                                    "8️⃣ **Используйте VPN на публичных Wi-Fi**\n" +
                                    "   • Даже на зашифрованном телефоне трафик можно перехватить\n" +
                                    "   • Лучшие бесплатные: ProtonVPN, Windscribe\n\n" +
                                    "9️⃣ **Включите уведомления о новом root-доступе**\n" +
                                    "   • Magisk → Настройки → Уведомления о суперпользователе → Включить\n" +
                                    "   • Вы будете знать, когда приложение получает root\n\n" +
                                    "🔟 **Регулярно обновляйте систему**\n" +
                                    "   • Кастомные прошивки (обновления безопасности)\n" +
                                    "   • Magisk (исправление уязвимостей)\n" +
                                    "   • Модули (актуальные версии)\n\n" +
                                    "🔹 **ЧТО ДЕЛАТЬ, ЕСЛИ ТЕЛЕФОН УКРАЛИ?**\n\n" +
                                    "• Немедленно смените пароли от всех аккаунтов (через другой телефон)\n" +
                                    "• Используйте Google Find Device (Найти устройство)\n" +
                                    "• Если есть, используйте приложения-трекеры (Cerberus, Prey)\n" +
                                    "• Заблокируйте SIM-карту через оператора\n\n" +
                                    "🔹 **ПРОВЕРЬТЕ ЭТИ НАСТРОЙКИ (обязательно):**\n\n" +
                                    "✅ Настройки → Безопасность → Администраторы устройства\n" +
                                    "   • Убедитесь, что нет подозрительных администраторов\n" +
                                    "✅ Настройки → Приложения → Права приложений\n" +
                                    "   • Проверьте, кто имеет доступ к SMS, камере, контактам\n" +
                                    "✅ Настройки → Аккаунты\n" +
                                    "   • Удалите незнакомые аккаунты\n\n" +
                                    "💡 **СОВЕТ:** Безопасность – это не разовая настройка, а постоянный процесс. " +
                                    "Раз в месяц проверяйте список приложений с root, обновляйте Magisk и следите за новостями.",
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
                    Text("Я изучил правила безопасности для рутованных устройств", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = secureByDefault,
                        onCheckedChange = { secureByDefault = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (secureByDefault) {
                item {
                    Text("✅ Поздравляю! Вы знаете, как защитить рутованный телефон!", color = Color.Green, fontSize = 14.sp)
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
                    Text("❌ Подтвердите, что вы изучили правила", color = Color.Red, fontSize = 14.sp)
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
                Text("🎉 ВЫ СТАЛИ ЭКСПЕРТОМ ПО БЕЗОПАСНОСТИ РУТОВАННЫХ УСТРОЙСТВ!", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Вы заработали $score очков.", fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "📚 **ЧТО ДАЛЬШЕ?**\n\n" +
                            "• Регулярно проверяйте список приложений с root-доступом\n" +
                            "• Обновляйте Magisk и модули\n" +
                            "• Следите за новостями безопасности на XDA\n" +
                            "• Используйте менеджеры паролей (Bitwarden, 1Password)\n" +
                            "• Включите двухфакторную аутентификацию везде, где можно\n" +
                            "• Изучайте модуль MagiskHide Props Config для обхода SafetyNet\n\n" +
                            "🔧 **ПОЛЕЗНЫЕ ПРИЛОЖЕНИЯ ДЛЯ БЕЗОПАСНОСТИ:**\n" +
                            "• AFWall+ - фаервол\n" +
                            "• Magisk - управление root\n" +
                            "• XPrivacyLua - контроль разрешений\n" +
                            "• Malwarebytes - антивирус\n" +
                            "• Bitwarden - менеджер паролей\n" +
                            "• Aegis Authenticator - 2FA\n" +
                            "• Shelter - изоляция приложений\n\n" +
                            "💡 **ПОСЛЕДНИЙ СОВЕТ:**\n" +
                            "С root приходит большая сила, но и большая ответственность. " +
                            "Доверяйте только проверенным приложениям и разработчикам. " +
                            "Берегите свои данные!",
                    fontSize = 15.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "lesson_security", true)
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