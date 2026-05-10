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
fun LessonCustomRomsScreen(navController: NavController, userId: Long) {
    var backupDone by remember { mutableStateOf(false) }
    var customRomSelected by remember { mutableStateOf(false) }
    var gappsInstalled by remember { mutableStateOf(false) }
    var wipeDone by remember { mutableStateOf(false) }
    var romInstalled by remember { mutableStateOf(false) }
    var magiskInstalled by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lessonKey = "lesson_custom_roms"
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
                        Text("🔄 КАСТОМНЫЕ ПРОШИВКИ – ПОЛНОЕ РУКОВОДСТВО ДЛЯ ЭКСПЕРТА", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("📌 **ЧТО ТАКОЕ КАСТОМНАЯ ПРОШИВКА?**", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Кастомная прошивка (Custom ROM) – это альтернативная версия Android, созданная энтузиастами. " +
                                    "Она заменяет стоковую (официальную) прошивку производителя.\n\n" +
                                    "⚡ **ЗАЧЕМ ЭТО НУЖНО?**\n" +
                                    "• Получить свежую версию Android, если производитель перестал обновлять телефон\n" +
                                    "• Избавиться от предустановленного «мусора» (приложений, которые нельзя удалить)\n" +
                                    "• Ускорить телефон (стоковые прошивки часто перегружены)\n" +
                                    "• Настроить внешний вид под себя (кастомизация интерфейса)\n" +
                                    "• Получить уникальные функции, которых нет в официальной прошивке\n" +
                                    "• Установить чистый Android (например, Pixel Experience) для максимальной производительности\n\n" +
                                    "🔍 **ПОПУЛЯРНЫЕ КАСТОМНЫЕ ПРОШИВКИ:**\n" +
                                    "• **LineageOS** – самая популярная, наследница CyanogenMod. Стабильная, минималистичная\n" +
                                    "• **Pixel Experience** – превращает телефон в Google Pixel (интерфейс, камера, приложения)\n" +
                                    "• **crDroid** – много настроек, кастомизация\n" +
                                    "• **Evolution X** – функции от разных прошивок в одном месте\n" +
                                    "• **ArrowOS** – лёгкая и быстрая, минимализм\n" +
                                    "• **Paranoid Android** – известна своим дизайном и плавностью\n\n" +
                                    "⚠️ **РИСКИ И НЮАНСЫ:**\n" +
                                    "• Можно получить «кирпич» (неудачная установка, но обычно лечится)\n" +
                                    "• Аннулирование гарантии (безвозвратно)\n" +
                                    "• Некоторые функции могут не работать (камера, NFC, датчики)\n" +
                                    "• Банковские приложения и Google Pay часто не работают (лечится Magisk Hide)\n" +
                                    "• Теряется возможность получать OTA-обновления от производителя\n" +
                                    "• Нужно переустанавливать всё с нуля (данные, приложения)\n\n" +
                                    "🤔 **СТОИТ ЛИ СТАВИТЬ КАСТОМНУЮ ПРОШИВКУ?**\n" +
                                    "• Если телефон старый и его перестали обновлять – ДА\n" +
                                    "• Если хотите максимум кастомизации и скорости – ДА\n" +
                                    "• Если вы не боитесь экспериментировать – ДА\n" +
                                    "• Если дорожите гарантией и банками – НЕТ\n" +
                                    "• Если у вас редкий телефон с плохой поддержкой – ЛУЧШЕ НЕ НАДО\n\n" +
                                    "📋 **ЧТО ВАМ ПОНАДОБИТСЯ?**\n" +
                                    "• Разблокированный загрузчик (см. урок про Root)\n" +
                                    "• Установленный TWRP (или другой кастомный рекавери)\n" +
                                    "• Качественный USB-кабель\n" +
                                    "• Полностью заряженный телефон (не менее 70%)\n" +
                                    "• Запас времени (1-2 часа)\n" +
                                    "• Скачанная кастомная прошивка для вашей модели\n\n" +
                                    "🎯 **ЭТОТ УРОК НАУЧИТ:**\n" +
                                    "1. Выбирать правильную прошивку для вашего телефона\n" +
                                    "2. Делать полный бэкап перед установкой\n" +
                                    "3. Устанавливать кастомную прошивку через TWRP\n" +
                                    "4. Устанавливать GApps (Google-сервисы)\n" +
                                    "5. Восстанавливаться в случае ошибки\n\n" +
                                    "⚠️ **КРИТИЧЕСКИ ВАЖНО:**\n" +
                                    "• Установка кастомной прошивки УДАЛЯЕТ ВСЕ ДАННЫЕ! Сделайте бэкап!\n" +
                                    "• Прошивка должна быть ТОЧНО ДЛЯ ВАШЕЙ МОДЕЛИ телефона\n" +
                                    "• Не прошивайте телефоны неизвестных брендов – высок риск кирпича\n" +
                                    "• Если что-то пошло не так – не паникуйте, ищите решение на XDA",
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

    // ШАГ 1: Выбор прошивки и подготовка (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("📥 ШАГ 1: ВЫБОР ПРОШИВКИ И ПОДГОТОВКА", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "✅ **ГДЕ ИСКАТЬ ПРОШИВКИ?**\n\n" +
                                    "🌐 **XDA-Developers (xda-developers.com)** – главный сайт для энтузиастов\n" +
                                    "   • Зайдите в раздел «Forums»\n" +
                                    "   • Найдите ваш телефон (поиск по модели)\n" +
                                    "   • Зайдите в подраздел «Android Development» или «ROMs»\n" +
                                    "   • Ищите темы с названиями [ROM] или [Custom ROM]\n" +
                                    "   • Перед скачиванием прочитайте первые 2-3 страницы – там обычно пишут о багах\n\n" +
                                    "📱 **Telegram-каналы** – сообщества владельцев вашей модели\n" +
                                    "   • Поищите в телеграме: «название_телефона ROM», «название_телефона customs»\n" +
                                    "   • Часто там выкладывают свежие сборки раньше, чем на XDA\n\n" +
                                    "🌍 **Официальные сайты прошивок:**\n" +
                                    "   • LineageOS: lineageos.org → Devices → найдите свой телефон → Downloads\n" +
                                    "   • Pixel Experience: download.pixelexperience.org → выберите модель → скачать\n" +
                                    "   • crDroid: crdroid.net → download → поиск по модели\n" +
                                    "   • Evolution X: evolution-x.org → downloads\n\n" +
                                    "🔍 **НА ЧТО ОБРАЩАТЬ ВНИМАНИЕ ПРИ ВЫБОРЕ?**\n" +
                                    "• Версия Android (последняя стабильная, не бета – если не готовы тестировать)\n" +
                                    "• Дата последнего обновления (лучше свежее – до 2-3 месяцев)\n" +
                                    "• Отзывы пользователей (стабильно ли работает, какие баги)\n" +
                                    "• Есть ли GApps в комплекте (часто написано «Includes GApps» или «Vanilla» – без GApps)\n" +
                                    "• Поддерживается ли ваш вариант телефона (Global, China, EU, India)\n\n" +
                                    "📂 **ЧТО КОНКРЕТНО СКАЧАТЬ?**\n" +
                                    "• **ROM-файл** (.zip) – сама прошивка\n" +
                                    "• **GApps** (если прошивка без Google-сервисов) – скачивайте с opengapps.org\n" +
                                    "   • Выберите Platform: ARM, ARM64 или x86 (зависит от процессора)\n" +
                                    "   • Выберите Android: вашу версию (12, 13, 14)\n" +
                                    "   • Выберите Variant: pico (минимальный), nano (базовый), micro (рекомендуется) или stock (полный)\n" +
                                    "• **Magisk** (если нужен root) – github.com/topjohnwu/Magisk/releases\n" +
                                    "• **Стоковая прошивка** (на случай возврата) – ищите на официальном сайте производителя или на XDA\n\n" +
                                    "💾 **БЭКАП ДАННЫХ (ОБЯЗАТЕЛЬНО!)**\n" +
                                    "• Фото, видео, документы → на компьютер или в облако\n" +
                                    "• Контакты → синхронизируйте с Google (Настройки → Аккаунты → Google → синхронизация контактов)\n" +
                                    "• Сообщения, звонки → через специальные приложения (SMS Backup & Restore)\n" +
                                    "• Данные приложений → через Swift Backup (нужен root) или Google Drive\n\n" +
                                    "🔹 **БЭКАП В TWRP (рекомендуется!):**\n" +
                                    "• Загрузитесь в TWRP (выключите телефон, зажмите кнопку питания + громкость вверх)\n" +
                                    "• Нажмите «Backup»\n" +
                                    "• Выберите разделы для бэкапа: System, Data, Boot, Vendor (если есть)\n" +
                                    "• Нажмите Swipe to Backup\n" +
                                    "• После создания бэкапа скопируйте папку TWRP на компьютер или флешку\n" +
                                    "• Так вы всегда сможете вернуться в случае проблем!\n\n" +
                                    "💡 **СОВЕТ:** Если не уверены в выборе, поищите на YouTube «как поставить кастомную прошивку на [ваша_модель]». " +
                                    "Там часто показывают процесс и говорят о подводных камнях.",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "💡 **СОВЕТ:** Сначала проверьте наличие прошивки для вашей модели. " +
                                    "Если её нет – лучше не рисковать. " +
                                    "Также почитайте тему целиком: часто на 5-10 страницах всплывают скрытые проблемы.",
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
                    Text("Я скачал прошивку и сделал бэкап данных", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = backupDone,
                        onCheckedChange = { backupDone = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (backupDone) {
                item {
                    Text("✅ Отлично! Данные сохранены, можно приступать.", color = Color.Green, fontSize = 14.sp)
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
                    Text("❌ Подтвердите, что вы скачали прошивку и сделали бэкап", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 2: Wipe в TWRP (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("🧹 ШАГ 2: WIPE (ОЧИСТКА) В TWRP", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Перед установкой новой прошивки нужно очистить старые данные, иначе телефон не загрузится.\n\n" +
                                    "🔹 **ТИПЫ WIPE (ОЧИСТКИ):**\n\n" +
                                    "• **Factory Reset (Data + Cache)** – удаляет приложения, настройки, но оставляет фото и файлы на Internal Storage\n" +
                                    "• **System** – удаляет саму прошивку (нужно для установки новой)\n" +
                                    "• **Data** – удаляет все ваши данные (приложения, настройки, но не фото и видео на Internal Storage)\n" +
                                    "• **Cache / Dalvik Cache** – временные файлы (безопасно удалять)\n" +
                                    "• **Internal Storage** – ВСЕ файлы на телефоне (фото, видео, музыка, документы)\n\n" +
                                    "✅ **ЧТО НУЖНО СДЕЛАТЬ ПЕРЕД УСТАНОВКОЙ ПРОШИВКИ:**\n\n" +
                                    "1️⃣ Загрузитесь в TWRP:\n" +
                                    "   • Выключите телефон\n" +
                                    "   • Зажмите и удерживайте кнопку питания + громкость вверх (или комбинацию под вашу модель)\n" +
                                    "   • Ждите появления логотипа TWRP\n\n" +
                                    "2️⃣ **Нажмите «Wipe» → «Advanced Wipe»**\n\n" +
                                    "3️⃣ **Выберите разделы для очистки:**\n" +
                                    "   ✅ Dalvik / ART Cache\n" +
                                    "   ✅ System\n" +
                                    "   ✅ Data\n" +
                                    "   ✅ Cache\n" +
                                    "   ❌ Micro SD (не трогайте)\n" +
                                    "   ❌ USB OTG (не трогайте)\n" +
                                    "   ❌ Internal Storage (НЕ ВЫБИРАЙТЕ, если не скопировали файлы на компьютер!)\n\n" +
                                    "4️⃣ **Проведите ползунок Swipe to Wipe**\n\n" +
                                    "5️⃣ **Дождитесь завершения (появится надпись «Successful»)**\n\n" +
                                    "🔹 **ПОЛНАЯ ОЧИСТКА (CLEAN FLASH) – рекомендуется для первой установки:**\n" +
                                    "• Выберите все разделы, кроме MicroSD и USB OTG, включая Internal Storage\n" +
                                    "• Все данные удалятся (фото, видео, музыка – тоже!)\n" +
                                    "• Нужен предварительный бэкап всех файлов на компьютер\n\n" +
                                    "🔹 **ЧАСТИЧНАЯ ОЧИСТКА (DIRTY FLASH):**\n" +
                                    "• Только очистка кэша (Cache, Dalvik)\n" +
                                    "• Используется для обновления прошивки на новую версию\n" +
                                    "• Данные приложений сохраняются (но могут возникнуть конфликты)\n\n" +
                                    "⚠️ **Важно:** Не выбирайте Internal Storage, если не скопировали все файлы на компьютер! " +
                                    "Иначе фото и видео будут удалены навсегда.",
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
                    Text("Я выполнил Wipe (очистку) в TWRP", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = wipeDone,
                        onCheckedChange = { wipeDone = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (wipeDone) {
                item {
                    Text("✅ Готово! Теперь можно устанавливать прошивку.", color = Color.Green, fontSize = 14.sp)
                }
                item {
                    Button(
                        onClick = { score += 20; step = 3 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                    ) {
                        Text("ДАЛЕЕ →", color = Color.Black)
                    }
                }
            } else {
                item {
                    Text("❌ Подтвердите, что вы выполнили Wipe", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 3: Установка ROM (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("📲 ШАГ 3: УСТАНОВКА КАСТОМНОЙ ПРОШИВКИ", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "🔹 **ИНСТРУКЦИЯ ПО УСТАНОВКЕ:**\n\n" +
                                    "1️⃣ **Скопируйте файлы на телефон**\n" +
                                    "   • Подключите телефон к компьютеру через USB\n" +
                                    "   • Перенесите ROM (.zip) и GApps (.zip) в корень памяти телефона или на SD-карту\n" +
                                    "   • Отключите телефон от компьютера\n\n" +
                                    "2️⃣ **В TWRP нажмите «Install»**\n\n" +
                                    "3️⃣ **Найдите загруженный ROM-файл (.zip)**\n" +
                                    "   • Если файл на SD-карте – нажмите «Select Storage» → Micro SD\n" +
                                    "   • Прокрутите список и найдите ваш файл\n\n" +
                                    "4️⃣ **Выберите файл прошивки** (он подсветится)\n\n" +
                                    "5️⃣ **Проведите ползунок Swipe to Confirm Flash**\n\n" +
                                    "6️⃣ **Дождитесь окончания установки (обычно 2–5 минут)**\n" +
                                    "   • В логе будет написано «Successful»\n\n" +
                                    "7️⃣ **НЕ перезагружайтесь**, если нужно установить GApps или Magisk\n\n" +
                                    "📌 **ПРАВИЛЬНЫЙ ПОРЯДОК УСТАНОВКИ:**\n" +
                                    "1. Прошивка (ROM) – обязательно\n" +
                                    "2. GApps (если прошивка без Google-сервисов) – сразу после ROM, без перезагрузки\n" +
                                    "3. Magisk (если нужен root) – после GApps, без перезагрузки\n" +
                                    "4. Другие моды (например, кастомное ядро) – по желанию\n\n" +
                                    "⚠️ **ВНИМАНИЕ:**\n" +
                                    "• Не прерывайте процесс установки! Телефон может превратиться в кирпич.\n" +
                                    "• Если установка зависла – подождите 10 минут, затем зажмите кнопку питания на 10-15 секунд для перезагрузки.\n" +
                                    "• При первой загрузке после установки телефон может загружаться 5-15 минут – это нормально (создаются кэши).\n" +
                                    "• Если экран завис на логотипе долго (более 20 минут) – что-то пошло не так, попробуйте снова.\n\n" +
                                    "🔹 **ПОСЛЕ УСТАНОВКИ:**\n" +
                                    "• Нажмите «Reboot → System»\n" +
                                    "• Первая загрузка может быть долгой (до 10-15 минут)\n" +
                                    "• Пройдите начальную настройку Android\n" +
                                    "• Восстановите данные из бэкапа",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "💡 **СОВЕТ:** Если вы ставите прошивку впервые, установите только ROM, " +
                                    "загрузитесь, проверьте работу (звонки, Wi-Fi, камера), а потом уже устанавливайте GApps и Magisk. " +
                                    "Так легче будет понять, где возникла ошибка.",
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
                    Text("Я выбрал прошивку и установил ROM", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = customRomSelected,
                        onCheckedChange = { customRomSelected = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (customRomSelected) {
                item {
                    Text("✅ ROM установлен! Переходим к следующему шагу.", color = Color.Green, fontSize = 14.sp)
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
                    Text("❌ Подтвердите, что вы установили ROM", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 4: Установка GApps (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("📦 ШАГ 4: УСТАНОВКА GAPPS (GOOGLE-СЕРВИСЫ)", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Некоторые кастомные прошивки не включают сервисы Google (Play Маркет, Gmail, Карты и т.д.). " +
                                    "Их нужно устанавливать отдельно.\n\n" +
                                    "🔹 **КАКИЕ GAPPS СУЩЕСТВУЮТ?**\n\n" +
                                    "• **OpenGApps** (opengapps.org) – самые популярные. Много вариантов:\n" +
                                    "   - pico – минимальный (только Play Маркет и сервисы) – 70 МБ\n" +
                                    "   - nano – + Google Assistant, поиск – 120 МБ\n" +
                                    "   - micro – + Gmail, Календарь, Карты – 200 МБ\n" +
                                    "   - stock – почти все приложения Google, заменяет стоковые – 500+ МБ\n" +
                                    "   - super – всё, что есть у Google\n\n" +
                                    "• **MindTheGApps** – лёгкие, совместимы со многими прошивками\n" +
                                    "• **NikGApps** – много кастомизации (можно убрать ненужные приложения)\n" +
                                    "• **BitGApps** – минималистичные\n\n" +
                                    "🔹 **КАК ВЫБРАТЬ ПРАВИЛЬНУЮ ВЕРСИЮ GAPPS:**\n\n" +
                                    "1️⃣ **Определите архитектуру процессора:**\n" +
                                    "   • ARM – 32-битные процессоры (старые телефоны)\n" +
                                    "   • ARM64 – 64-битные (почти все современные телефоны после ~2015 года)\n" +
                                    "   • x86 – редко (Intel, Asus)\n" +
                                    "   • Узнать можно в приложении CPU-Z или в характеристиках телефона\n\n" +
                                    "2️⃣ **Определите версию Android:**\n" +
                                    "   • Android 12 → 12.0\n" +
                                    "   • Android 13 → 13.0\n" +
                                    "   • Android 14 → 14.0\n" +
                                    "   • Смотрите в описании к прошивке\n\n" +
                                    "3️⃣ **Выберите вариант:**\n" +
                                    "   • Для новичков: nano или micro\n" +
                                    "   • Для минимализма: pico\n" +
                                    "   • Для максимального удобства: stock\n\n" +
                                    "🔹 **УСТАНОВКА GAPPS (без перезагрузки после ROM):**\n\n" +
                                    "1️⃣ **В TWRP, после установки ROM (не перезагружаясь!):**\n" +
                                    "   • Нажмите «Install»\n" +
                                    "   • Выберите файл GApps (.zip)\n" +
                                    "   • Проведите ползунок Swipe to Confirm Flash\n\n" +
                                    "2️⃣ **Дождитесь окончания установки** (1-2 минуты)\n\n" +
                                    "3️⃣ **Нажмите «Reboot → System»**\n\n" +
                                    "⚠️ **ЧАСТЫЕ ОШИБКИ ПРИ УСТАНОВКЕ GAPPS:**\n" +
                                    "• **Недостаточно места в системе** → попробуйте вариант pico или nano (они меньше)\n" +
                                    "• **Неправильная архитектура** → проверьте свой процессор, скачайте версию для ARM или ARM64\n" +
                                    "• **Несовместимая версия Android** → скачайте GApps под вашу версию Android\n" +
                                    "• **Ошибка 20** – не та версия Android → проверьте, что прошивка и GApps одной версии\n" +
                                    "• **Ошибка 64** – проблемы с пространством → удалите ненужное из /system (требует опыта)\n\n" +
                                    "✅ **После первой загрузки:**\n" +
                                    "• Пройдите настройку Android (выберите язык, подключитесь к Wi-Fi)\n" +
                                    "• Войдите в Google аккаунт\n" +
                                    "• Откройте Play Маркет → дождитесь обновления (может занять 5-10 минут)\n" +
                                    "• Установите нужные приложения",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "💡 **СОВЕТ:** Если прошивка уже включает GApps, этот шаг не нужен. " +
                                    "Обычно указано в описании: «includes GApps» или «with Google Apps». " +
                                    "Если написано «Vanilla» – значит GApps нет, нужно устанавливать отдельно.",
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
                    Text("Я установил GApps (если нужно)", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = gappsInstalled,
                        onCheckedChange = { gappsInstalled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (gappsInstalled) {
                item {
                    Text("✅ GApps установлены!", color = Color.Green, fontSize = 14.sp)
                }
                item {
                    Button(
                        onClick = { score += 25; step = 5 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                    ) {
                        Text("ДАЛЕЕ →", color = Color.Black)
                    }
                }
            } else {
                item {
                    Text("❌ Подтвердите, что вы установили GApps (или они не нужны)", color = Color.Red, fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 5: Установка Magisk (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("🔐 ШАГ 5: УСТАНОВКА MAGISK (ROOT) – ПО ЖЕЛАНИЮ", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Если вам нужен root-доступ, можно установить Magisk сразу после прошивки (без перезагрузки).\n\n" +
                                    "🔹 **УСТАНОВКА MAGISK ЧЕРЕЗ TWRP:**\n\n" +
                                    "1️⃣ **Скачайте Magisk APK с GitHub**\n" +
                                    "   • github.com/topjohnwu/Magisk/releases\n" +
                                    "   • Скачайте файл Magisk-vXX.X.apk (лучше последнюю стабильную версию)\n" +
                                    "   • Переименуйте его в Magisk-vXX.X.zip (TWRP работает с zip)\n\n" +
                                    "2️⃣ **В TWRP, после установки GApps (не перезагружаясь):**\n" +
                                    "   • Нажмите «Install»\n" +
                                    "   • Выберите Magisk.zip\n" +
                                    "   • Проведите ползунок Swipe to Confirm Flash\n\n" +
                                    "3️⃣ **Дождитесь окончания установки**\n\n" +
                                    "4️⃣ **Нажмите «Reboot → System»**\n\n" +
                                    "5️⃣ **После загрузки откройте приложение Magisk**\n" +
                                    "   • Оно может попросить дополнительные настройки – соглашайтесь\n" +
                                    "   • Если Magisk не видит root – повторите установку через TWRP\n\n" +
                                    "🔹 **УСТАНОВКА MAGISK БЕЗ TWRP (патч boot-образа):**\n" +
                                    "• Установите приложение Magisk на телефон\n" +
                                    "• Нажмите «Install» → «Select and Patch a File»\n" +
                                    "• Выберите файл boot.img вашей прошивки (его нужно предварительно извлечь)\n" +
                                    "• Патченный образ появится в папке Download\n" +
                                    "• Прошейте его через fastboot: fastboot flash boot magisk_patched.img\n\n" +
                                    "🔹 **ЧТО ДЕЛАТЬ, ЕСЛИ MAGISK НЕ УСТАНАВЛИВАЕТСЯ?**\n" +
                                    "• Попробуйте другую версию Magisk\n" +
                                    "• Установите Magisk после первой загрузки системы (через патч boot)\n" +
                                    "• Ищите решение на XDA для вашей модели\n\n" +
                                    "✅ **ПРОВЕРКА ROOT ДОСТУПА:**\n" +
                                    "• Установите Root Checker из Play Маркет\n" +
                                    "• Или через ADB: adb shell → su (должен появиться значок #)",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "💡 **СОВЕТ:** Если вы не планируете использовать root, этот шаг можно пропустить. " +
                                    "Magisk пригодится для скрытия root от банковских приложений и для модулей.",
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
                    Text("Я установил Magisk (если нужен root)", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = magiskInstalled,
                        onCheckedChange = { magiskInstalled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Телефон успешно загрузился с новой прошивкой", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = romInstalled,
                        onCheckedChange = { romInstalled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (romInstalled) {
                item {
                    Text("✅ Поздравляю! Вы успешно установили кастомную прошивку!", color = Color.Green, fontSize = 14.sp)
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
                    Text("❌ Подтвердите, что телефон загрузился с новой прошивкой", color = Color.Red, fontSize = 14.sp)
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
                Text("🎉 ВЫ СТАЛИ ЭКСПЕРТОМ ПО УСТАНОВКЕ КАСТОМНЫХ ПРОШИВОК!", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Вы заработали $score очков.", fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "📚 **ЧТО ДАЛЬШЕ?**\n\n" +
                            "• Настройте новую прошивку под себя\n" +
                            "• Изучите возможности Magisk и его модули\n" +
                            "• Попробуйте другие кастомные прошивки\n" +
                            "• Следите за обновлениями на XDA\n" +
                            "• Переходите к следующим урокам: Оптимизация, Скрипты, Безопасность\n\n" +
                            "💡 **ЧТО ДЕЛАТЬ, ЕСЛИ ЧТО-ТО ПОШЛО НЕ ТАК?**\n" +
                            "• Вернитесь в TWRP и восстановите бэкап (Wipe → Restore)\n" +
                            "• Прошейте стоковую прошивку через TWRP или fastboot\n" +
                            "• Ищите решение на XDA-Developers\n" +
                            "• Не паникуйте – большинство проблем решаются\n\n" +
                            "🔧 **ПОЛЕЗНЫЕ ССЫЛКИ:**\n" +
                            "• LineageOS Wiki – wiki.lineageos.org\n" +
                            "• XDA Developers – xda-developers.com\n" +
                            "• Magisk – github.com/topjohnwu/Magisk\n" +
                            "• OpenGApps – opengapps.org",
                    fontSize = 15.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "lesson_custom_roms", true)
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