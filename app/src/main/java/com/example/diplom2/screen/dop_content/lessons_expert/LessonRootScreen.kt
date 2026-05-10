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
fun LessonRootScreen(navController: NavController, userId: Long) {
    var bootloaderUnlocked by remember { mutableStateOf(false) }
    var twrpInstalled by remember { mutableStateOf(false) }
    var magiskInstalled by remember { mutableStateOf(false) }
    var rootVerified by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
        val lessonKey = "lesson_root"
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
    // ШАГ 0: ВСТУПЛЕНИЕ (скролл уже есть - LazyColumn)
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
                        Text("🔓 ROOT-ДОСТУП – ПОЛНОЕ РУКОВОДСТВО ДЛЯ ЭКСПЕРТА", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("📌 **ЧТО ТАКОЕ ROOT?**", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Root (рутирование) – это получение полного доступа ко всем файлам и настройкам Android. " +
                                    "По умолчанию вы работаете как обычный пользователь, а root даёт права администратора (суперпользователя).\n\n" +
                                    "⚡ **ЧТО ДАЁТ ROOT?**\n" +
                                    "• Удаление любых встроенных приложений (даже системных)\n" +
                                    "• Полный бэкап всей системы (Nandroid backup)\n" +
                                    "• Установка модулей (Magisk) для расширения функций\n" +
                                    "• Блокировка рекламы системно через hosts-файл\n" +
                                    "• Разгон/андервольт процессора (аккуратнее с этим)\n" +
                                    "• Настройка фаервола и контроль трафика\n" +
                                    "• Изменение системных звуков, анимации, шрифтов\n" +
                                    "• Полный контроль над разрешениями приложений (Xposed, AppOps)\n" +
                                    "• Запуск скриптов при загрузке системы\n\n" +
                                    "⚠️ **РИСКИ И ПОСЛЕДСТВИЯ:**\n" +
                                    "• Аннулирование гарантии (навсегда, даже после удаления root)\n" +
                                    "• Некоторые приложения не работают (банки, игры с защитой, Google Pay)\n" +
                                    "• Можно получить «кирпич» (неудачный опыт, но редко)\n" +
                                    "• Системные обновления по воздуху (OTA) перестают работать\n" +
                                    "• Повышенная уязвимость при неосторожном обращении\n\n" +
                                    "🤔 **СТОИТ ЛИ РУТИРОВАТЬСЯ?**\n" +
                                    "• Если вы хотите максимум контроля – ДА\n" +
                                    "• Если дорожите гарантией и банками – НЕТ\n" +
                                    "• Если не боитесь экспериментировать – ДА\n" +
                                    "• Если нужны только кастомные прошивки – root необязателен, но часто идёт вместе\n\n" +
                                    "🎯 **ЧТО ВАМ ПОНАДОБИТСЯ?**\n" +
                                    "• Компьютер с ADB (мы установили в прошлом уроке)\n" +
                                    "• Качественный USB-кабель\n" +
                                    "• Заряженный телефон (не менее 70%)\n" +
                                    "• Нервные клетки и запас времени (1-2 часа)\n" +
                                    "• Доступ в интернет для скачивания файлов\n\n" +
                                    "📋 **ЭТОТ УРОК НАУЧИТ:**\n" +
                                    "1. Разблокировать загрузчик (bootloader)\n" +
                                    "2. Установить TWRP (кастомное восстановление)\n" +
                                    "3. Прошить Magisk (современный способ root)\n" +
                                    "4. Проверить наличие root и настроить Magisk Hide\n\n" +
                                    "⚠️ **КРИТИЧЕСКИ ВАЖНО:**\n" +
                                    "• Разблокировка загрузчика УДАЛЯЕТ ВСЕ ДАННЫЕ! Сделайте бэкап!\n" +
                                    "• Каждый производитель имеет свой способ разблокировки\n" +
                                    "• На некоторых телефонах разблокировка невозможна (например, американские Samsung)\n" +
                                    "• Если что-то пойдёт не так – не паникуйте, ищите решение на XDA-Developers",
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
                ) { Text("НАЧАТЬ ПРАКТИКУ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black) }
                Spacer(modifier = Modifier.height(32.dp)) // ДОБАВЛЕНО: отступ снизу
            }
        }
        return
    }

    // ШАГ 1: Разблокировка загрузчика (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("🔓 ШАГ 1: РАЗБЛОКИРОВКА ЗАГРУЗЧИКА (BOOTLOADER)", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "⚠️ **ВНИМАНИЕ! Разблокировка загрузчика удаляет ВСЕ ДАННЫЕ с телефона!**\n" +
                                    "Сделайте резервную копию фото, контактов, важных файлов!\n\n" +
                                    "🔹 **ДЛЯ РАЗНЫХ ПРОИЗВОДИТЕЛЕЙ:**\n\n" +
                                    "**Xiaomi (Mi, Redmi, Poco):**\n" +
                                    "• Подайте заявку на разблокировку через Mi Unlock Tool\n" +
                                    "• Обычно нужно ждать 72 часа-2 недели\n" +
                                    "• Включите в настройках: Mi Unlock status → Привязать аккаунт\n\n" +
                                    "**OnePlus:**\n" +
                                    "• Включите OEM-разблокировку в настройках разработчика\n" +
                                    "• Выполните fastboot oem unlock\n\n" +
                                    "**Google Pixel:**\n" +
                                    "• fastboot flashing unlock\n\n" +
                                    "**Samsung (Exynos):**\n" +
                                    "• Включите OEM-разблокировку в настройках\n" +
                                    "• В режиме загрузчика нажмите долго кнопку вверх\n\n" +
                                    "**Samsung (Snapdragon, США):**\n" +
                                    "❌ Разблокировка невозможна!\n\n" +
                                    "**Huawei/Honor:**\n" +
                                    "❌ С 2018 года разблокировка официально недоступна\n\n" +
                                    "**Realme, Oppo, Vivo:**\n" +
                                    "• Нужна заявка, часто есть ограничения\n\n" +
                                    "**Общая инструкция (если производитель позволяет):**\n" +
                                    "1. Включите опции разработчика (7 нажатий по номеру сборки)\n" +
                                    "2. Включите OEM-разблокировку\n" +
                                    "3. Включите отладку по USB\n" +
                                    "4. Перезагрузитесь в загрузчик: adb reboot bootloader\n" +
                                    "5. Выполните: fastboot oem unlock (или fastboot flashing unlock)\n" +
                                    "6. Подтвердите действие и дождитесь перезагрузки\n\n" +
                                    "🔍 **КАК ПРОВЕРИТЬ, РАЗБЛОКИРОВАН ЛИ ЗАГРУЗЧИК?**\n" +
                                    "• При включении телефона появляется предупреждение о разблокировке\n" +
                                    "• В режиме загрузчика (fastboot) есть статус UNLOCKED",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "💡 **СОВЕТ ПРОФИ:** После разблокировки загрузчика заново включите отладку по USB – она сбрасывается.\n" +
                                    "Также перед первым запуском системы настройте телефон заново.",
                            fontSize = 14.sp,
                            color = Color(0xFFD4AF37).copy(alpha = 0.7f)
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
                    Text("Я разблокировал загрузчик (или ознакомился с инструкцией)", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = bootloaderUnlocked,
                        onCheckedChange = { bootloaderUnlocked = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (bootloaderUnlocked) {
                item {
                    Text("✅ Отлично! Теперь можно переходить к установке TWRP.", color = Color.Green, fontSize = 14.sp)
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
                    Text("❌ Подтвердите, что вы ознакомились с инструкцией по разблокировке", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp)) // ДОБАВЛЕНО: отступ снизу
            }
        }
        return
    }

    // ШАГ 2: Установка TWRP (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("💿 ШАГ 2: УСТАНОВКА TWRP (КАСТОМНОЕ ВОССТАНОВЛЕНИЕ)", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "TWRP (Team Win Recovery Project) – это улучшенное восстановление, которое позволяет:\n" +
                                    "• Устанавливать кастомные прошивки\n" +
                                    "• Делать полные бэкапы системы (Nandroid)\n" +
                                    "• Устанавливать Magisk и другие zip-файлы\n" +
                                    "• Очищать различные разделы (wipe)\n\n" +
                                    "🔹 **УСТАНОВКА TWRP:**\n\n" +
                                    "1️⃣ **Скачайте TWRP для вашей модели телефона:**\n" +
                                    "   • Официальный сайт: https://twrp.me\n" +
                                    "   • Или ищите на форуме XDA-Developers\n\n" +
                                    "2️⃣ **Перезагрузитесь в загрузчик (bootloader):**\n" +
                                    "   adb reboot bootloader\n\n" +
                                    "3️⃣ **Установите TWRP (замените twrp.img на ваше имя файла):**\n" +
                                    "   fastboot flash recovery twrp.img\n\n" +
                                    "4️⃣ **Перезагрузитесь в TWRP сразу, не загружая систему:**\n" +
                                    "   • Нажмите и удерживайте нужную комбинацию кнопок (у всех по-разному)\n" +
                                    "   • Или выполните: fastboot boot twrp.img\n\n" +
                                    "5️⃣ **Разрешите TWRP изменять системный раздел:**\n" +
                                    "   • При первом запуске TWRP спросит:\n" +
                                    "   • «Оставить системный раздел доступным для записи?»\n" +
                                    "   • Проведите ползунок, чтобы разрешить\n\n" +
                                    "🔹 **ЧТО ДЕЛАТЬ, ЕСЛИ TWRP НЕ УСТАНАВЛИВАЕТСЯ?**\n" +
                                    "• Проверьте, что вы скачали TWRP именно для вашей модели\n" +
                                    "• Используйте другую версию TWRP\n" +
                                    "• Для телефонов на Mediatek нужен SP Flash Tool\n" +
                                    "• Для Xiaomi – часто TWRP ставят через MiFlash",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "💡 **ВАЖНО:** После установки TWRP при первом входе НЕ забудьте сделать бэкап stock-прошивки! " +
                                    "Это поможет вернуть телефон в исходное состояние, если что-то пойдёт не так.",
                            fontSize = 14.sp,
                            color = Color(0xFFD4AF37).copy(alpha = 0.7f)
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
                    Text("Я установил TWRP", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = twrpInstalled,
                        onCheckedChange = { twrpInstalled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (twrpInstalled) {
                item {
                    Text("✅ Отлично! TWRP установлен. Теперь прошьём Magisk.", color = Color.Green, fontSize = 14.sp)
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
                    Text("❌ Подтвердите, что вы установили TWRP", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 3: Установка Magisk (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("🛠️ ШАГ 3: УСТАНОВКА MAGISK (СОВРЕМЕННЫЙ ROOT)", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Magisk – это современный способ получения root-доступа. Главное преимущество – системный root (systemless), " +
                                    "то есть изменения не затрагивают системный раздел. Это позволяет:\n" +
                                    "• Скрывать root от приложений (Magisk Hide)\n" +
                                    "• Устанавливать модули прямо из приложения\n" +
                                    "• Получать OTA-обновления (если не менять system)\n\n" +
                                    "🔹 **УСТАНОВКА MAGISK ЧЕРЕЗ TWRP:**\n\n" +
                                    "1️⃣ **Скачайте Magisk APK с официального GitHub:**\n" +
                                    "   https://github.com/topjohnwu/Magisk/releases\n\n" +
                                    "2️⃣ **Переименуйте .apk в .zip** (TWRP работает с zip)\n\n" +
                                    "3️⃣ **Перезагрузитесь в TWRP:**\n" +
                                    "   adb reboot recovery\n\n" +
                                    "4️⃣ **В TWRP выберите «Install» → найдите Magisk.zip**\n\n" +
                                    "5️⃣ **Проведите ползунок для подтверждения установки**\n\n" +
                                    "6️⃣ **После установки нажмите «Reboot → System»**\n\n" +
                                    "🔹 **УСТАНОВКА MAGISK БЕЗ TWRP (патч boot-образа):**\n" +
                                    "• Установите приложение Magisk на телефон\n" +
                                    "• Нажмите «Install» → «Select and Patch a File»\n" +
                                    "• Выберите файл boot.img вашей прошивки\n" +
                                    "• Патченный образ появится в папке Download\n" +
                                    "• Прошейте его через fastboot: fastboot flash boot magisk_patched.img\n\n" +
                                    "🔹 **ПЕРВЫЙ ЗАПУСК MAGISK:**\n" +
                                    "• Откройте приложение Magisk\n" +
                                    "• Разрешите дополнительные настройки\n" +
                                    "• Magisk может запросить дополнительные компоненты – соглашайтесь\n\n" +
                                    "🔹 **ЧТО ДЕЛАТЬ, ЕСЛИ MAGISK НЕ ВИДИТ ROOT?**\n" +
                                    "• Перезагрузите телефон\n" +
                                    "• Установите Magisk через TWRP заново\n" +
                                    "• На некоторых телефонах нужно прошить патченный boot.img\n" +
                                    "• Проверьте, не включён ли откат (rollback) на прошивке",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "💡 **СОВЕТ:** После установки Magisk рекомендую сразу настроить Magisk Hide, чтобы банковские приложения не ругались на root.",
                            fontSize = 14.sp,
                            color = Color(0xFFD4AF37).copy(alpha = 0.7f)
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
                    Text("Я установил Magisk", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = magiskInstalled,
                        onCheckedChange = { magiskInstalled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (magiskInstalled) {
                item {
                    Text("✅ Magisk установлен! Теперь проверим root.", color = Color.Green, fontSize = 14.sp)
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
                    Text("❌ Подтвердите, что вы установили Magisk", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 4: Проверка root и настройка (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("✅ ШАГ 4: ПРОВЕРКА ROOT И НАСТРОЙКА", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "🔹 **КАК ПРОВЕРИТЬ, ЧТО ROOT РАБОТАЕТ?**\n\n" +
                                    "1️⃣ **С помощью приложения Root Checker:**\n" +
                                    "   • Скачайте Root Checker из Play Маркет\n" +
                                    "   • Откройте и нажмите «Verify Root»\n" +
                                    "   • Если root есть – появится зелёная галочка\n\n" +
                                    "2️⃣ **Через ADB:**\n" +
                                    "   adb shell\n" +
                                    "   su\n" +
                                    "   • Если знак доллара ($) сменился на решётку (#) – root получен!\n\n" +
                                    "3️⃣ **Проверка прав на запись в system:**\n" +
                                    "   adb shell\n" +
                                    "   su\n" +
                                    "   mount -o remount,rw /system\n" +
                                    "   • Если ошибки нет – всё отлично\n\n" +
                                    "🔹 **НАСТРОЙКА MAGISK HIDE (скрытие root):**\n" +
                                    "• Откройте Magisk → Настройки → Magisk Hide → Включить\n" +
                                    "• Выберите приложения, которые нужно скрыть от root (банки, игры, Google Pay)\n" +
                                    "• После изменения настроек перезагрузите телефон\n\n" +
                                    "🔹 **ПОЛЕЗНЫЕ МОДУЛИ MAGISK:**\n" +
                                    "• **Systemless Hosts** – для блокировки рекламы (AdAway)\n" +
                                    "• **ViPER4Android FX** – улучшение звука\n" +
                                    "• **MagiskHide Props Config** – подмена fingerprint для работы Google Pay\n" +
                                    "• **Universal SafetyNet Fix** – обход проверки SafetyNet\n" +
                                    "• **Busybox** – полезные команды для терминала\n\n" +
                                    "⚠️ **ЧТО ДЕЛАТЬ, ЕСЛИ ROOT НЕ РАБОТАЕТ?**\n" +
                                    "• Переустановите Magisk через TWRP\n" +
                                    "• Скачайте последнюю версию Magisk\n" +
                                    "• Попробуйте другой метод установки\n" +
                                    "• Ищите решение на XDA для вашей модели\n\n" +
                                    "🔹 **КАК УДАЛИТЬ ROOT?**\n" +
                                    "• Откройте Magisk → «Полная деинсталляция» → «Полное удаление»\n" +
                                    "• Или прошейте стоковый boot-образ через fastboot\n" +
                                    "• Или переустановите стоковую прошивку через TWRP",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "💡 **ВАЖНО:** Если вы хотите использовать Google Pay, установите модуль Universal SafetyNet Fix " +
                                    "и настройте Magisk Hide принудительно. Иначе платежи через NFC будут недоступны.",
                            fontSize = 14.sp,
                            color = Color(0xFFD4AF37).copy(alpha = 0.7f)
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
                    Text("Я проверил root и настроил Magisk Hide", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = rootVerified,
                        onCheckedChange = { rootVerified = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (rootVerified) {
                item {
                    Text("✅ Поздравляю! Вы успешно получили root-доступ!", color = Color.Green, fontSize = 14.sp)
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
                    Text("❌ Подтвердите, что вы проверили root", color = Color.Red, fontSize = 14.sp)
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
                Text("🎉 ВЫ СТАЛИ ЭКСПЕРТОМ ПО ROOT-ДОСТУПУ!", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Вы заработали $score очков.", fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "📚 **ЧТО ДАЛЬШЕ?**\n\n" +
                            "• Изучайте модули Magisk – они раскрывают настоящую силу root\n" +
                            "• Установите AFWall+ для фаервола\n" +
                            "• Попробуйте Titanium Backup для бэкапов\n" +
                            "• Переходите к следующим урокам: Кастомные прошивки, Скрипты, Безопасность\n\n" +
                            "💡 **ПОСЛЕДНИЙ СОВЕТ:**\n" +
                            "С root приходит большая сила, но и большая ответственность. " +
                            "Не устанавливайте всё подряд, проверяйте модули и приложения перед установкой. " +
                            "Берегите свои данные!",
                    fontSize = 15.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "lesson_root", true)
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