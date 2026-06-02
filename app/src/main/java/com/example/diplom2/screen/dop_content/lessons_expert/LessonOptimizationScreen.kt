package com.example.diplom2.screen.dop_content.lessons_expert

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun LessonOptimizationScreen(navController: NavController, userId: Long) {
    var debloatDone by remember { mutableStateOf(false) }
    var governorChanged by remember { mutableStateOf(false) }
    var buildPropOptimized by remember { mutableStateOf(false) }
    var lmkConfigured by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
        val lessonKey = "lesson_optimization"
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
                        Text("⚡ ОПТИМИЗАЦИЯ ANDROID – ПОЛНОЕ РУКОВОДСТВО ДЛЯ ЭКСПЕРТА", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("📌 **ЧТО ТАКОЕ ОПТИМИЗАЦИЯ?**", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Оптимизация Android – это настройка системы для максимальной производительности, плавности и энергоэффективности. " +
                                    "Вы сами решаете, как работает ваш телефон, вместо того чтобы полагаться на настройки производителя.\n\n" +
                                    "⚡ **ЧТО МОЖНО УЛУЧШИТЬ?**\n" +
                                    "• Удалить встроенный мусор (Debloat) – приложения, которые нельзя удалить обычным способом\n" +
                                    "• Настроить процессор (CPU Governor) – баланс между скоростью и батареей\n" +
                                    "• Увеличить оперативную память – настройка LMK (Low Memory Killer)\n" +
                                    "• Ускорить интерфейс – отключение анимации, настройка build.prop\n" +
                                    "• Продлить время работы – настройка doze, отключение фоновых процессов\n\n" +
                                    "📋 **ЭТОТ УРОК НАУЧИТ:**\n" +
                                    "1️⃣ Деблоатинг – удаление системных приложений через ADB\n" +
                                    "2️⃣ Настройка CPU Governor для производительности или экономии\n" +
                                    "3️⃣ Оптимизация build.prop (скрытые настройки Android)\n" +
                                    "4️⃣ Настройка LMK для управления оперативной памятью\n\n" +
                                    "⚠️ **ВАЖНО:**\n" +
                                    "• Для деблоатинга не нужен root (можно через ADB)\n" +
                                    "• Для изменения CPU Governor нужен root\n" +
                                    "• build.prop нужно редактировать аккуратно – ошибка может привести к кирпичу\n" +
                                    "• Все изменения желательно делать при включённой отладке по USB\n\n" +
                                    "🎯 **ПОДГОТОВКА:**\n" +
                                    "• Компьютер с ADB (установили в первом уроке)\n" +
                                    "• Включённая отладка по USB\n" +
                                    "• Root (для некоторых операций)\n" +
                                    "• Программа Kernel Adiutor (для настройки CPU)",
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
                ) { Text("НАЧАТЬ ОПТИМИЗАЦИЮ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black) }
                Spacer(modifier = Modifier.height(32.dp)) // ДОБАВЛЕНО: отступ снизу
            }
        }
        return
    }

    // ШАГ 1: Деблоатинг (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("🗑️ ШАГ 1: ДЕБЛОАТИНГ (УДАЛЕНИЕ СИСТЕМНЫХ ПРИЛОЖЕНИЙ)", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Деблоатинг – это удаление предустановленных приложений, которые нельзя удалить обычным способом.\n\n" +
                                    "🔍 **КАКИЕ ПРИЛОЖЕНИЯ МОЖНО УДАЛИТЬ?**\n\n" +
                                    "✅ **Безопасно удалять:**\n" +
                                    "• Facebook, Instagram, TikTok (социальные сети)\n" +
                                    "• Игры, которые предустановил производитель\n" +
                                    "• Приложения оператора (МТС, Билайн, Теле2)\n" +
                                    "• Ненужные сервисы производителя (Samsung Members, Mi Community)\n\n" +
                                    "❌ **НЕЛЬЗЯ удалять:**\n" +
                                    "• com.android.phone – телефонная служба\n" +
                                    "• com.android.providers.settings – настройки\n" +
                                    "• com.google.android.gms – сервисы Google\n" +
                                    "• com.android.systemui – системный интерфейс\n" +
                                    "• Сервисы, отвечающие за камеру, звук, Wi-Fi\n\n" +
                                    "🔹 **СПОСОБ 1: ЧЕРЕЗ ADB (без root)**\n\n" +
                                    "1️⃣ **Список всех приложений:**\n" +
                                    "   adb shell pm list packages\n\n" +
                                    "2️⃣ **Найти нужное приложение:**\n" +
                                    "   adb shell pm list packages | grep facebook\n\n" +
                                    "3️⃣ **Удалить приложение для текущего пользователя (безопасно):**\n" +
                                    "   adb shell pm uninstall -k --user 0 com.facebook.katana\n\n" +
                                    "4️⃣ **Вернуть удалённое приложение (если нужно):**\n" +
                                    "   adb shell cmd package install-existing com.facebook.katana\n\n" +
                                    "🔹 **СПОСОБ 2: ЧЕРЕЗ ROOT (полное удаление)**\n\n" +
                                    "• Установите приложение System App Remover (Root)\n" +
                                    "• Выберите приложения для удаления\n" +
                                    "• Нажмите «Uninstall»\n\n" +
                                    "🔹 **СПОСОБ 3: ЧЕРЕЗ TWRP (для опытных)**\n\n" +
                                    "• Установите файловый менеджер в TWRP\n" +
                                    "• Удаляйте папки в /system/app, /system/priv-app\n\n" +
                                    "📌 **ПОЛЕЗНЫЕ КОМАНДЫ ADB:**\n" +
                                    "• adb shell pm list packages -3 – только установленные пользователем\n" +
                                    "• adb shell pm list packages -s – только системные\n" +
                                    "• adb shell pm list packages -d – только отключённые\n" +
                                    "• adb shell pm disable-user --user 0 com.package – отключить (без удаления)\n" +
                                    "• adb shell pm enable com.package – включить обратно",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "💡 **СОВЕТ:** Не удаляйте всё подряд! Сначала отключите приложение, проверьте работу системы неделю, " +
                                    "а потом удаляйте. Некоторые приложения могут быть связаны с важными функциями.",
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
                    Text("Я удалил ненужные системные приложения", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = debloatDone,
                        onCheckedChange = { debloatDone = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (debloatDone) {
                item {
                    Text("✅ Отлично! Телефон стал легче и быстрее.", color = Color(0xFF2E8058), fontSize = 14.sp)
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
                    Text("❌ Подтвердите, что вы удалили ненужные приложения", color = Color( 0xFF9B0C3F), fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 2: Настройка CPU Governor (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("⚙️ ШАГ 2: НАСТРОЙКА CPU GOVERNOR", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "CPU Governor управляет частотой процессора. От него зависит скорость работы и время автономной работы.\n\n" +
                                    "🔍 **ТИПЫ GOVERNOR (самые распространённые):**\n\n" +
                                    "📌 **Производительность (Performance):**\n" +
                                    "• CPU всегда на максимальной частоте\n" +
                                    "• Самый быстрый, но батарея садится за 2-3 часа\n" +
                                    "• Используйте только для игр или тяжёлых задач\n\n" +
                                    "📌 **Энергосбережение (Powersave):**\n" +
                                    "• CPU всегда на минимальной частоте\n" +
                                    "• Телефон тормозит, но батарея держится долго\n" +
                                    "• Для повседневного использования не подходит\n\n" +
                                    "📌 **Сбалансированный (Interactive, OnDemand):**\n" +
                                    "• Частота меняется по нагрузке\n" +
                                    "• Лучший баланс скорость/батарея\n" +
                                    "• Рекомендуется для большинства пользователей\n\n" +
                                    "📌 **Пользовательский (ElementalX, Blu_active, Chill):**\n" +
                                    "• Созданы энтузиастами\n" +
                                    "• Более тонкая настройка\n" +
                                    "• Нужно тестировать и настраивать\n\n" +
                                    "🔹 **КАК НАСТРОИТЬ CPU GOVERNOR:**\n\n" +
                                    "1️⃣ **Скачайте Kernel Adiutor (root нужен)**\n" +
                                    "2️⃣ **Откройте раздел «CPU»**\n" +
                                    "3️⃣ **Выберите Governor:**\n" +
                                    "   • Для скорости: interactive или performance\n" +
                                    "   • Для батареи: conservative или powersave\n" +
                                    "   • Для баланса: ondemand (по умолчанию)\n" +
                                    "4️⃣ **Нажмите «Применить»**\n" +
                                    "5️⃣ **Поставьте галочку «Применить при загрузке»**\n\n" +
                                    "🔹 **ПРОДВИНУТАЯ НАСТРОЙКА (для Kernel Adiutor):**\n" +
                                    "• CPU Maximum Frequency – максимальная частота (чем выше, тем быстрее, но батарея садится быстрее)\n" +
                                    "• CPU Minimum Frequency – минимальная частота (чем ниже, тем дольше работает, но отклик хуже)\n" +
                                    "• GPU Governor – управление графикой (аналогично CPU)\n\n" +
                                    "⚠️ **ПРЕДУПРЕЖДЕНИЕ:**\n" +
                                    "• Не выставляйте максимальную частоту выше стоковой – телефон будет греться и есть риск повреждения процессора\n" +
                                    "• Не выставляйте минимальную частоту слишком низко – экран может не включиться из сна\n" +
                                    "• На некоторых телефонов смена governor невозможна (заблокировано ядром)",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "💡 **СОВЕТ:** Лучший вариант для большинства – интерактивный губернатор " +
                                    "с небольшими правками. Поищите рекомендации для вашей модели телефона на XDA.",
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
                    Text("Я настроил CPU Governor (или ознакомился с инструкцией)", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = governorChanged,
                        onCheckedChange = { governorChanged = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (governorChanged) {
                item {
                    Text("✅ Отлично! Телефон работает с оптимальными настройками.", color = Color(0xFF2E8058), fontSize = 14.sp)
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
                    Text("❌ Подтвердите, что вы ознакомились с настройкой CPU", color = Color( 0xFF9B0C3F), fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 3: Оптимизация build.prop (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("🔧 ШАГ 3: ОПТИМИЗАЦИЯ BUILD.PROP", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "build.prop – это файл с системными настройками Android. Изменяя его, можно ускорить телефон, улучшить отклик, сохранить батарею.\n\n" +
                                    "🔹 **ГДЕ НАХОДИТСЯ BUILD.PROP?**\n" +
                                    "• /system/build.prop (требуется root)\n" +
                                    "• /vendor/build.prop (на некоторых устройствах)\n\n" +
                                    "🔹 **КАК РЕДАКТИРОВАТЬ?**\n" +
                                    "• Через любой текстовый редактор с root-доступом (например, Root Explorer)\n" +
                                    "• Через ADB: adb pull /system/build.prop → редактируете на компьютере → adb push\n\n" +
                                    "📌 **ПОЛЕЗНЫЕ НАСТРОЙКИ ДЛЯ build.prop:**\n\n" +
                                    "🔸 **Ускорение интерфейса:**\n" +
                                    "   debug.composition.type=gpu\n" +
                                    "   debug.sf.hw=1\n" +
                                    "   persist.sys.composition.type=gpu\n" +
                                    "   persist.sys.ui.hw=1\n" +
                                    "   video.accelerate.hw=1\n\n" +
                                    "🔸 **Плавность прокрутки:**\n" +
                                    "   windowsmgr.max_events_per_sec=240\n" +
                                    "   persist.sys.scrollingcache=4\n" +
                                    "   ro.max.fling_velocity=12000\n" +
                                    "   ro.min.fling_velocity=8000\n\n" +
                                    "🔸 **Экономия батареи:**\n" +
                                    "   pm.sleep_mode=1\n" +
                                    "   power_supply.wakeup=enable\n" +
                                    "   ro.ril.disable.power.collapse=1\n" +
                                    "   wifi.supplicant_scan_interval=180\n\n" +
                                    "🔸 **Улучшение памяти:**\n" +
                                    "   persist.sys.purgeable_assets=1\n" +
                                    "   ro.config.low_ram=false\n" +
                                    "   dalvik.vm.heapstartsize=8m\n" +
                                    "   dalvik.vm.heapsize=512m\n\n" +
                                    "🔸 **Улучшение камеры (для некоторых телефонов):**\n" +
                                    "   persist.camera.HAL3.enabled=1\n" +
                                    "   persist.vendor.camera.HAL3.enabled=1\n\n" +
                                    "⚠️ **ПРЕДУПРЕЖДЕНИЕ:**\n" +
                                    "• Всегда делайте бэкап build.prop перед редактированием!\n" +
                                    "• Неправильные настройки могут привести к кирпичу\n" +
                                    "• Добавляйте по одной строке, проверяя работу системы\n" +
                                    "• После изменений перезагрузите телефон",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "💡 **СОВЕТ:** Не копируйте всё подряд. Каждая модель телефона требует индивидуального подхода. " +
                                    "Лучше найти готовый tweaks для вашей модели на XDA.",
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
                    Text("Я оптимизировал build.prop", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = buildPropOptimized,
                        onCheckedChange = { buildPropOptimized = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (buildPropOptimized) {
                item {
                    Text("✅ Отлично! Система настроена на максимальную производительность.", color = Color(0xFF2E8058), fontSize = 14.sp)
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
                    Text("❌ Подтвердите, что вы произвели настройку build.prop", color = Color( 0xFF9B0C3F), fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 4: Настройка LMK (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("🎯 ШАГ 4: НАСТРОЙКА LMK (LOW MEMORY KILLER)", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "LMK – это механизм Android, который автоматически закрывает приложения при нехватке оперативной памяти.\n\n" +
                                    "🔍 **КАК ЭТО РАБОТАЕТ?**\n" +
                                    "• Приложения имеют приоритеты: foreground, visible, service, background, empty\n" +
                                    "• Когда памяти мало, LMK убивает приложения с самым низким приоритетом\n\n" +
                                    "🔹 **КАК НАСТРОИТЬ LMK ЧЕРЕЗ KERNEL ADIUTOR:**\n\n" +
                                    "1️⃣ **Откройте Kernel Adiutor**\n" +
                                    "2️⃣ **Перейдите в раздел «Low Memory Killer»**\n" +
                                    "3️⃣ **Настройте пороги для каждого уровня:**\n" +
                                    "   • Foreground – приложения на экране (лучше оставить по умолчанию)\n" +
                                    "   • Visible – видимые приложения\n" +
                                    "   • Service – сервисы\n" +
                                    "   • Background – фоновые\n" +
                                    "   • Empty – пустые приложения\n\n" +
                                    "4️⃣ **Чем ниже число, тем позже приложения будут убиваться**\n" +
                                    "5️⃣ **Чем выше число, тем агрессивнее очистка (больше свободной памяти)**\n\n" +
                                    "📌 **РЕКОМЕНДУЕМЫЕ НАСТРОЙКИ (для телефонов с 4-6 ГБ ОЗУ):**\n" +
                                    "• Foreground: 0 MB\n" +
                                    "• Visible: 48 MB\n" +
                                    "• Service: 96 MB\n" +
                                    "• Background: 144 MB\n" +
                                    "• Empty: 192 MB\n\n" +
                                    "📌 **АГРЕССИВНЫЙ РЕЖИМ (для телефонов с 2-3 ГБ ОЗУ):**\n" +
                                    "• Foreground: 16 MB\n" +
                                    "• Visible: 64 MB\n" +
                                    "• Service: 128 MB\n" +
                                    "• Background: 256 MB\n" +
                                    "• Empty: 512 MB\n\n" +
                                    "🔹 **Swapfile (виртуальная память):**\n" +
                                    "• Можно создать файл подкачки на SD-карте или в/data\n" +
                                    "• Увеличивает доступную память, но замедляет работу\n" +
                                    "• Рекомендуется только для старых телефонов\n\n" +
                                    "🔹 **ПОЛЕЗНЫЕ ПРИЛОЖЕНИЯ ДЛЯ НАСТРОЙКИ:**\n" +
                                    "• Android Tweaker\n" +
                                    "• L-Speed\n" +
                                    "• FDE.AI\n\n" +
                                    "⚠️ **ПРЕДУПРЕЖДЕНИЕ:**\n" +
                                    "• Слишком агрессивные настройки могут привести к убиванию важных приложений (мессенджеры, музыка, GPS)\n" +
                                    "• Рекомендую менять по одному параметру и тестировать неделю",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "💡 **СОВЕТ:** Не экономьте память, если всё работает и так. " +
                                    "LMK – это скорее способ исправить проблемы с нехваткой ОЗУ, а не способ «ускорить» телефон. " +
                                    "На современных телефонах с 6+ ГБ ОЗУ лучше оставить по умолчанию.",
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
                    Text("Я настроил LMK", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = lmkConfigured,
                        onCheckedChange = { lmkConfigured = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (lmkConfigured) {
                item {
                    Text("✅ Поздравляю! Вы настроили все параметры оптимизации!", color = Color(0xFF2E8058), fontSize = 14.sp)
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
                    Text("❌ Подтвердите, что вы настроили LMK", color = Color( 0xFF9B0C3F), fontSize = 14.sp)
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
                Text("🎉 ВЫ ОПТИМИЗИРОВАЛИ ANDROID КАК ЭКСПЕРТ!", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Вы заработали $score очков.", fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "📚 **ЧТО ДАЛЬШЕ?**\n\n" +
                            "• Наблюдайте за работой телефона после настроек\n" +
                            "• Регулярно обновляйте прошивку\n" +
                            "• Изучайте возможности Kernel Adiutor и L-Speed\n" +
                            "• Переходите к следующим урокам: Скрипты, Безопасность, Настройка сети\n\n" +
                            "💡 **ПОСЛЕДНИЙ СОВЕТ:**\n" +
                            "Оптимизация – это не одноразовая настройка. " +
                            "В процессе использования вы поймёте, что вам нужно ускорить, а что можно отключить. " +
                            "Экспериментируйте, но всегда делайте бэкап!",
                    fontSize = 15.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "lesson_optimization", true)
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