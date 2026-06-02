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
fun LessonFilesystemScreen(navController: NavController, userId: Long) {
    var explorerInstalled by remember { mutableStateOf(false) }
    var pathsLearned by remember { mutableStateOf(false) }
    var permissionsLearned by remember { mutableStateOf(false) }
    var adbCommandsLearned by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
        val lessonKey = "lesson_filesystem"
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
                        Text("📁 ФАЙЛОВАЯ СИСТЕМА ANDROID – ПОЛНОЕ РУКОВОДСТВО ДЛЯ ЭКСПЕРТА", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("📌 **ЧТО ТАКОЕ ФАЙЛОВАЯ СИСТЕМА ANDROID?**", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Android основан на Linux, поэтому его файловая система похожа на Linux. " +
                                    "Но управлять ею можно и с Windows через ADB (Android Debug Bridge). " +
                                    "Понимание структуры каталогов поможет вам:\n\n" +
                                    "⚡ **ЗАЧЕМ ЭТО НУЖНО?**\n" +
                                    "• Находить и изменять системные файлы\n" +
                                    "• Удалять предустановленные приложения (Debloat)\n" +
                                    "• Восстанавливать удалённые файлы\n" +
                                    "• Изменять разрешения и права доступа\n" +
                                    "• Понимать, где что хранится для бэкапов\n" +
                                    "• Чинить проблемы, связанные с заполнением памяти\n" +
                                    "• Изучать устройство Android изнутри\n\n" +
                                    "🎯 **ЭТОТ УРОК НАУЧИТ:**\n" +
                                    "1️⃣ Структуре важнейших каталогов Android\n" +
                                    "2️⃣ Работе с файловой системой через ADB (Windows/командная строка)\n" +
                                    "3️⃣ Работе с root-файлами через Root Explorer на телефоне\n" +
                                    "4️⃣ Копированию, перемещению, удалению файлов через ADB\n" +
                                    "5️⃣ Поиску файлов и их редактированию\n" +
                                    "6️⃣ Работе с разрешениями через ADB\n\n" +
                                    "⚠️ **ВАЖНО:**\n" +
                                    "• Редактирование системных файлов может привести к неработоспособности телефона\n" +
                                    "• Всегда делайте бэкап перед изменениями\n" +
                                    "• Некоторые операции требуют root\n" +
                                    "• Не удаляйте файлы, если не знаете, за что они отвечают\n\n" +
                                    "📌 **ДЛЯ РАБОТЫ С WINDOWS ВАМ ПОНАДОБИТСЯ:**\n" +
                                    "• ADB (Android Debug Bridge) – установили в первом уроке\n" +
                                    "• Root Explorer (или MiXplorer) на телефоне – для просмотра файлов\n" +
                                    "• Блокнот или любой текстовый редактор на компьютере\n" +
                                    "• Командная строка Windows (cmd) или PowerShell\n" +
                                    "• Включённая отладка по USB на телефоне",
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
                ) { Text("ИССЛЕДОВАТЬ ФАЙЛОВУЮ СИСТЕМУ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black) }
                Spacer(modifier = Modifier.height(32.dp)) // ДОБАВЛЕНО: отступ снизу
            }
        }
        return
    }

    // ШАГ 1: Главные каталоги Android (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("🗂️ ШАГ 1: ГЛАВНЫЕ КАТАЛОГИ ANDROID", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "📌 **КОРНЕВАЯ ФАЙЛОВАЯ СИСТЕМА (/) – самое важное:**\n\n" +
                                    "🔹 **/system** – системные файлы и приложения.\n" +
                                    "   • /system/app – предустановленные приложения\n" +
                                    "   • /system/priv-app – системные приложения с высокими привилегиями\n" +
                                    "   • /system/bin – бинарные файлы (команды)\n" +
                                    "   • /system/etc – конфигурационные файлы\n" +
                                    "   • /system/framework – каркас Android (.jar)\n" +
                                    "   • /system/media – звуки, рингтоны, анимация загрузки\n" +
                                    "   • /system/build.prop – свойства системы (можно редактировать для тюнинга)\n\n" +
                                    "🔹 **/data** – пользовательские данные (самое ценное).\n" +
                                    "   • /data/app – установленные приложения .apk\n" +
                                    "   • /data/data – данные приложений (настройки, базы данных, кэш)\n" +
                                    "   • /data/media/0 – ваша внутренняя память (папки DCIM, Music, Download)\n" +
                                    "   • /data/system – системные настройки, блокировки, аккаунты\n" +
                                    "   • /data/dalvik-cache – оптимизированные приложения (можно чистить для освобождения места)\n\n" +
                                    "🔹 **/cache** – кэш системы (можно безопасно чистить)\n" +
                                    "🔹 **/dev** – виртуальные устройства (блочные, драйверы)\n" +
                                    "🔹 **/proc** – виртуальная файловая система процессов (информация о системе)\n" +
                                    "   • /proc/cpuinfo – информация о процессоре\n" +
                                    "   • /proc/meminfo – информация о памяти\n" +
                                    "   • /proc/version – версия ядра\n\n" +
                                    "🔹 **/sdcard** – ссылка на /data/media/0 (внутренняя память)\n" +
                                    "🔹 **/storage** – все точки монтирования хранилищ (SD-карта, USB)\n" +
                                    "🔹 **/mnt** – смонтированные устройства\n" +
                                    "🔹 **/vendor** – файлы от производителя (особенно важно для кастомных прошивок)\n" +
                                    "🔹 **/product** – специфичные для модели телефона файлы (Android 10+)\n" +
                                    "🔹 **/apex** – модули Android (Android 10+)\n\n" +
                                    "📌 **ЧТО МОЖНО ДЕЛАТЬ (с root):**\n" +
                                    "• Править build.prop для оптимизации\n" +
                                    "• Удалять или замораживать системные приложения\n" +
                                    "• Менять звуки и анимацию загрузки\n" +
                                    "• Редактировать hosts для блокировки рекламы\n\n" +
                                    "📌 **ЧТО НЕЛЬЗЯ ДЕЛАТЬ (опасно):**\n" +
                                    "• Удалять папки /system/bin, /system/framework\n" +
                                    "• Править разрешения ядра без опыта\n" +
                                    "• Удалять /data/system без причины (сбросятся настройки)\n\n" +
                                    "💡 **СОВЕТ:** Установите приложение **Root Explorer** или **MiXplorer** " +
                                    "на телефон – они показывают все эти папки и права доступа.\n" +
                                    "Через ADB вы можете просматривать файлы командой: adb shell ls -la /system",
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
                    Text("Я изучил структуру каталогов Android", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = pathsLearned,
                        onCheckedChange = { pathsLearned = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (pathsLearned) {
                item {
                    Text("✅ Отлично! Продолжаем.", color = Color(0xFF2E8058), fontSize = 14.sp)
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
                    Text("❌ Подтвердите изучение структуры", color = Color( 0xFF9B0C3F), fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 2: Работа с файлами через ADB (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("💻 ШАГ 2: РАБОТА С ФАЙЛАМИ ЧЕРЕЗ ADB (WINDOWS)", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "ADB позволяет управлять файловой системой телефона прямо с компьютера.\n\n" +
                                    "🔹 **НАСТРОЙКА ADB НА WINDOWS (если не сделали в первом уроке):**\n\n" +
                                    "1️⃣ Скачайте Platform Tools с официального сайта:\n" +
                                    "   https://developer.android.com/studio/releases/platform-tools\n" +
                                    "2️⃣ Распакуйте в C:\\adb\n" +
                                    "3️⃣ Откройте командную строку или PowerShell от имени администратора\n" +
                                    "4️⃣ Перейдите в папку: cd C:\\adb\n" +
                                    "5️⃣ Проверьте: .\\adb version\n" +
                                    "6️⃣ Добавьте в PATH для удобства (по желанию):\n" +
                                    "   • Win+R → sysdm.cpl → Дополнительно → Переменные среды\n" +
                                    "   • Path → Добавить → C:\\adb\n\n" +
                                    "🔹 **БАЗОВЫЕ КОМАНДЫ ADB ДЛЯ ФАЙЛОВОЙ СИСТЕМЫ (выполняются в командной строке):**\n\n" +
                                    "• Просмотр файлов и папок:\n" +
                                    "   adb shell ls -la /sdcard\n" +
                                    "   adb shell ls -la /system\n\n" +
                                    "• Копирование файла с телефона на компьютер:\n" +
                                    "   adb pull /sdcard/DCIM/photo.jpg C:\\Users\\Ваше_Имя\\Desktop\\\n" +
                                    "   adb pull /sdcard/Download/file.txt . (в текущую папку)\n\n" +
                                    "• Копирование файла с компьютера на телефон:\n" +
                                    "   adb push C:\\Users\\Ваше_Имя\\Desktop\\file.txt /sdcard/Download/\n" +
                                    "   adb push file.txt /sdcard/\n\n" +
                                    "• Создание папки:\n" +
                                    "   adb shell mkdir /sdcard/NewFolder\n\n" +
                                    "• Удаление файла:\n" +
                                    "   adb shell rm /sdcard/file.txt\n\n" +
                                    "• Удаление папки с содержимым:\n" +
                                    "   adb shell rm -rf /sdcard/NewFolder\n\n" +
                                    "• Переименование/перемещение файла:\n" +
                                    "   adb shell mv /sdcard/old.txt /sdcard/new.txt\n" +
                                    "   adb shell mv /sdcard/file.txt /sdcard/Download/\n\n" +
                                    "• Копирование внутри телефона:\n" +
                                    "   adb shell cp /sdcard/file.txt /sdcard/backup.txt\n\n" +
                                    "🔹 **РАБОТА С РАЗРЕШЕНИЯМИ (CHMOD):**\n\n" +
                                    "• Права доступа в Android (как в Linux):\n" +
                                    "   r = 4 (чтение), w = 2 (запись), x = 1 (выполнение)\n" +
                                    "• Изменить права (через ADB shell):\n" +
                                    "   adb shell chmod 644 /sdcard/file.txt   (владелец:rw-, группа:r--, остальные:r--)\n" +
                                    "   adb shell chmod 755 /sdcard/script.sh   (rwxr-xr-x)\n" +
                                    "   adb shell chmod +x /sdcard/script.sh   (добавить выполнение)\n\n" +
                                    "🔹 **ПОЛУЧЕНИЕ ИНФОРМАЦИИ О ФАЙЛАХ:**\n\n" +
                                    "• Размер файла:\n" +
                                    "   adb shell ls -la /sdcard/file.txt\n" +
                                    "   adb shell du -h /sdcard/file.txt\n" +
                                    "• Свободное место на диске:\n" +
                                    "   adb shell df -h\n" +
                                    "   adb shell df /data\n\n" +
                                    "🔹 **ПРАКТИЧЕСКОЕ ЗАДАНИЕ (выполните в командной строке):**\n\n" +
                                    "1️⃣ Создайте на телефоне папку Test:\n" +
                                    "   adb shell mkdir /sdcard/Test\n\n" +
                                    "2️⃣ Скопируйте любой файл с компьютера в эту папку:\n" +
                                    "   adb push C:\\file.txt /sdcard/Test/\n\n" +
                                    "3️⃣ Скопируйте файл обратно на компьютер:\n" +
                                    "   adb pull /sdcard/Test/file.txt C:\\Users\\Ваше_Имя\\Desktop\\\n\n" +
                                    "4️⃣ Удалите папку:\n" +
                                    "   adb shell rm -rf /sdcard/Test\n\n" +
                                    "💡 **СОВЕТ:** Всегда используйте пути без пробелов или заключайте их в кавычки. " +
                                    "Для длинных команд удобно использовать PowerShell, где можно копировать и вставлять.",
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
                    Text("Я освоил команды ADB для работы с файлами", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = adbCommandsLearned,
                        onCheckedChange = { adbCommandsLearned = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (adbCommandsLearned) {
                item {
                    Text("✅ Отлично! ADB-команды освоены.", color = Color(0xFF2E8058), fontSize = 14.sp)
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
                    Text("❌ Подтвердите изучение команд ADB", color = Color( 0xFF9B0C3F), fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 3: Работа с файлами на телефоне (Root Explorer) (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("📱 ШАГ 3: РАБОТА С ФАЙЛАМИ НА ТЕЛЕФОНЕ (ROOT EXPLORER)", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Приложения-менеджеры файлов удобнее для быстрых операций прямо на телефоне.\n\n" +
                                    "🔹 **ЛУЧШИЕ ФАЙЛОВЫЕ МЕНЕДЖЕРЫ ДЛЯ ROOT:**\n\n" +
                                    "• **Root Explorer** – самый мощный, платный, есть триал\n" +
                                    "• **MiXplorer** – бесплатный, много функций, лучшая альтернатива\n" +
                                    "• **Solid Explorer** – красивый интерфейс, есть бесплатная версия с ограничениями\n" +
                                    "• **FX Explorer** – простой, с root-доступом\n" +
                                    "• **Material Files** – open-source, без рекламы, только базовые функции\n\n" +
                                    "🔹 **УСТАНОВКА ROOT EXPLORER (или MiXplorer):**\n\n" +
                                    "1️⃣ Скачайте Root Explorer из Play Маркет или MiXplorer с официального сайта\n" +
                                    "2️⃣ Откройте приложение и дайте root-доступ\n" +
                                    "3️⃣ В меню выберите «Root» или перейдите в корень файловой системы (/)\n" +
                                    "4️⃣ Теперь вы видите все системные папки (system, data, vendor, proc)\n\n" +
                                    "🔹 **ЧТО МОЖНО ДЕЛАТЬ В ROOT EXPLORER:**\n\n" +
                                    "• Копировать, перемещать, удалять любые файлы (включая системные)\n" +
                                    "• Изменять права доступа к файлам (chmod)\n" +
                                    "• Изменять владельца файлов (chown)\n" +
                                    "• Редактировать текстовые файлы (встроенный редактор)\n" +
                                    "• Создавать символические ссылки\n" +
                                    "• Распаковывать ZIP-архивы\n" +
                                    "• Смотреть информацию о свободном месте\n\n" +
                                    "🔹 **ПРАКТИЧЕСКОЕ ЗАДАНИЕ (в Root Explorer):**\n\n" +
                                    "1️⃣ Перейдите в папку /sdcard и создайте новую папку «TestRoot»\n" +
                                    "2️⃣ Скопируйте в неё любой файл из папки Download\n" +
                                    "3️⃣ Измените права доступа на 644 (rw-r--r--)\n" +
                                    "4️⃣ Переименуйте файл\n" +
                                    "5️⃣ Скопируйте файл обратно в Download\n" +
                                    "6️⃣ Удалите папку TestRoot\n\n" +
                                    "🔹 **РЕДАКТИРОВАНИЕ СИСТЕМНЫХ ФАЙЛОВ (осторожно!):**\n\n" +
                                    "• Перейдите в /system\n" +
                                    "• Найдите файл build.prop\n" +
                                    "• Сделайте бэкап (скопируйте его в /sdcard)\n" +
                                    "• Откройте build.prop через встроенный редактор\n" +
                                    "• Добавьте в конец строку: # Test modification\n" +
                                    "• Сохраните и закройте\n" +
                                    "• Если телефон работает нормально – изменения безопасны\n\n" +
                                    "🔹 **ЧТО НЕЛЬЗЯ ДЕЛАТЬ В ROOT EXPLORER (опасно!):**\n\n" +
                                    "• Не удаляйте папку /system/bin\n" +
                                    "• Не меняйте права на корневые папки (/, /system) на 777\n" +
                                    "• Не удаляйте файлы, если не знаете, за что они отвечают\n" +
                                    "• Не редактируйте build.prop, если не понимаете, что делаете\n\n" +
                                    "💡 **СОВЕТ:** Перед редактированием любых системных файлов " +
                                    "сделайте бэкап. Если что-то пойдёт не так – загрузитесь в TWRP " +
                                    "и восстановите файл из бэкапа.",
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
                    Text("Я установил Root Explorer и выполнил задания", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = explorerInstalled,
                        onCheckedChange = { explorerInstalled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (explorerInstalled) {
                item {
                    Text("✅ Отлично! Вы освоили работу с файловой системой.", color = Color(0xFF2E8058), fontSize = 14.sp)
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
                    Text("❌ Подтвердите выполнение заданий", color = Color( 0xFF9B0C3F), fontSize = 14.sp)
                }
            }
            item {
                Text("Очки: $score", fontSize = 14.sp, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        return
    }

    // ШАГ 4: Права доступа (chmod) и безопасность (ИСПРАВЛЕН - добавлен скролл)
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
                        Text("📋 ШАГ 4: ПРАВА ДОСТУПА (CHMOD) И БЕЗОПАСНОСТЬ", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Понимание прав доступа критически важно для безопасности системы.\n\n" +
                                    "🔹 **ТРИ ГРУППЫ ПОЛЬЗОВАТЕЛЕЙ:**\n" +
                                    "• **Владелец (Owner)** – создатель файла (обычно root или приложение)\n" +
                                    "• **Группа (Group)** – группа пользователей (например, shell, sdcard_rw)\n" +
                                    "• **Остальные (Others)** – все остальные приложения и пользователи\n\n" +
                                    "🔹 **ТРИ ТИПА ПРАВ:**\n" +
                                    "• **r (read)** – чтение (4)\n" +
                                    "• **w (write)** – запись (2)\n" +
                                    "• **x (execute)** – выполнение (1)\n\n" +
                                    "🔹 **КАК ЗАПИСЫВАЮТСЯ ПРАВА (восьмеричная система):**\n\n" +
                                    "• rwx = 7 (4+2+1) – полный доступ\n" +
                                    "• rw- = 6 (4+2+0) – чтение и запись\n" +
                                    "• r-x = 5 (4+0+1) – чтение и выполнение\n" +
                                    "• r-- = 4 (4+0+0) – только чтение\n" +
                                    "• -wx = 3 (0+2+1) – запись и выполнение\n" +
                                    "• -w- = 2 (0+2+0) – только запись\n" +
                                    "• --x = 1 (0+0+1) – только выполнение\n\n" +
                                    "🔹 **ПРИМЕРЫ ПРАВ ДЛЯ РАЗНЫХ ФАЙЛОВ:**\n\n" +
                                    "• 644 (rw-r--r--) – обычные файлы (например, build.prop)\n" +
                                    "• 755 (rwxr-xr-x) – исполняемые файлы и папки (например, /system/bin)\n" +
                                    "• 600 (rw-------) – приватные файлы (только владелец)\n" +
                                    "• 777 (rwxrwxrwx) – полный доступ для всех (опасно!)\n\n" +
                                    "🔹 **ИЗМЕНЕНИЕ ПРАВ ЧЕРЕЗ ADB (Windows):**\n\n" +
                                    "• adb shell chmod 644 /sdcard/file.txt\n" +
                                    "• adb shell chmod 755 /sdcard/script.sh\n" +
                                    "• adb shell chmod -R 755 /sdcard/folder (рекурсивно для папки)\n" +
                                    "• adb shell chmod +x /sdcard/script.sh (добавить выполнение)\n\n" +
                                    "🔹 **ПРОСМОТР ТЕКУЩИХ ПРАВ:**\n\n" +
                                    "• adb shell ls -la /system/build.prop\n" +
                                    "• Вывод: -rw-r--r-- 1 root root 8456 2024-01-15 10:30 build.prop\n" +
                                    "   Первая часть: - (файл), rw- (владелец), r-- (группа), r-- (остальные)\n\n" +
                                    "🔹 **БЕЗОПАСНОСТЬ ПРИ РАБОТЕ С ФАЙЛАМИ:**\n\n" +
                                    "• Никогда не ставьте 777 на системные файлы и папки\n" +
                                    "• Не меняйте владельца системных файлов с root на что-то другое\n" +
                                    "• Перед изменением прав на системных файлах делайте бэкап\n" +
                                    "• Для приложений используйте минимально возможные права\n" +
                                    "• Регулярно проверяйте, какие приложения имеют доступ к вашим файлам\n\n" +
                                    "🔹 **ЧАСТЫЕ ОШИБКИ:**\n\n" +
                                    "• Слишком широкие права (777) на конфиденциальных данных\n" +
                                    "• Забыли сделать файл исполняемым (chmod +x) для скриптов\n" +
                                    "• Неправильная рекурсия (chmod -R на большой папке может занять время)\n\n" +
                                    "💡 **СОВЕТ:** Золотое правило – давайте файлу ровно столько прав, сколько ему нужно. " +
                                    "Для обычных файлов достаточно 644, для папок – 755. 777 используйте только в крайнем случае.",
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
                    Text("Я изучил права доступа (chmod) и безопасность", fontSize = 14.sp, color = Color.White)
                    Switch(
                        checked = permissionsLearned,
                        onCheckedChange = { permissionsLearned = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
            if (permissionsLearned) {
                item {
                    Text("✅ Поздравляю! Вы освоили файловую систему Android!", color = Color(0xFF2E8058), fontSize = 14.sp)
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
                    Text("❌ Подтвердите, что вы изучили права доступа", color = Color( 0xFF9B0C3F), fontSize = 14.sp)
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
                Text("🎉 ВЫ СТАЛИ ЭКСПЕРТОМ ПО ФАЙЛОВОЙ СИСТЕМЕ ANDROID!", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Вы заработали $score очков.", fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "📚 **ЧТО ДАЛЬШЕ?**\n\n" +
                            "• Изучайте команды терминала Linux – они работают через ADB shell\n" +
                            "• Настройте автоматические бэкапы через ADB\n" +
                            "• Освойте работу с образами (img) через ADB\n" +
                            "• Переходите к следующим урокам: Сеть и серверы, Эмуляция, Оптимизация\n\n" +
                            "🔧 **ПОЛЕЗНЫЕ ССЫЛКИ ДЛЯ WINDOWS:**\n" +
                            "• ADB Cheatsheet – шпаргалка по ADB командам\n" +
                            "• XDA Developers – форум с готовыми туториалами\n" +
                            "• Android Developers – официальная документация по ADB\n" +
                            "• GitHub – скрипты для автоматизации бэкапов через ADB\n\n" +
                            "💡 **ПОСЛЕДНИЙ СОВЕТ:**\n" +
                            "Изучение файловой системы – это основа для всех продвинутых операций. " +
                            "Чем лучше вы знаете, где что лежит и как управлять правами, " +
                            "тем быстрее сможете диагностировать проблемы и настраивать систему под себя. " +
                            "Всегда делайте бэкап перед серьёзными изменениями!",
                    fontSize = 15.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "lesson_filesystem", true)
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