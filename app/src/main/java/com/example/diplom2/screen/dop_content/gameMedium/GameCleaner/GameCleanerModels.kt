package com.example.diplom2.screen.dop_content.gameMedium.GameCleaner

import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class SortType { SIZE, DATE, TYPE }

enum class TaskGoalType {
    FREE_SPACE, DELETE_DUPLICATES, SINGLE_ACTION,
    DELETE_DOWNLOADS,
    CLEAR_APP_CACHE, COMPRESS_MEDIA, DELETE_HIDDEN,
    RESTORE, CONFIGURE, REPORT, DELETE_EMPTY_FOLDER, OPTIMIZE_BATTERY
}

enum class FileSizeLevel { SMALL, MEDIUM, LARGE, HUGE }

data class StorageItem(
    val id: String,
    val name: String,
    val type: String,
    val sizeMB: Int,
    val date: String,
    val isCritical: Boolean = false,
    val recommendedAction: String? = null,
    val isDuplicate: Boolean = false,
    val explanation: String? = null,
    val dangerLevel: DangerLevel = DangerLevel.SAFE
)

enum class DangerLevel { SAFE, CAUTION, DANGEROUS }

data class CleanerTask(
    val id: Int,
    val title: String,
    val description: String,
    val goal: String,
    val targetFreeMB: Int = 0,
    val targetAction: String? = null,
    val isPremium: Boolean = false,
    val hint: String,
    val goalType: TaskGoalType,
    val taskType: String = "default",
    val tutorial: String? = null
)

enum class RobotMood { HAPPY, SAD, EXCITED, NEUTRAL, THINKING }

data class RobotMessage(val text: String, val mood: RobotMood)

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val unlocked: Boolean = false
)

enum class DropZoneType {
    TRASH, KEEP, SD_CARD, PROCESS, RESTORE, DELETE_FOREVER
}

data class ActiveZone(
    val type: DropZoneType,
    val label: String,
    val icon: ImageVector,
    val color: Color
)

fun getZonesForTask(goalType: TaskGoalType): List<ActiveZone> {
    return when (goalType) {
        TaskGoalType.FREE_SPACE, TaskGoalType.DELETE_HIDDEN,
        TaskGoalType.DELETE_EMPTY_FOLDER, TaskGoalType.OPTIMIZE_BATTERY,
        TaskGoalType.DELETE_DOWNLOADS -> listOf(  // ← ДОБАВЛЕНО
            ActiveZone(DropZoneType.TRASH, "Удалить", Icons.Default.Delete, Color(0xFFB00020)),
            ActiveZone(DropZoneType.KEEP, "Оставить", Icons.Default.CheckCircle, Color(0xFF4CAF50))
        )
        TaskGoalType.DELETE_DUPLICATES -> listOf(
            ActiveZone(DropZoneType.TRASH, "Удалить дубликат", Icons.Default.Delete, Color(0xFFB00020)),
            ActiveZone(DropZoneType.KEEP, "Оставить оригинал", Icons.Default.CheckCircle, Color(0xFF4CAF50))
        )
        TaskGoalType.SINGLE_ACTION -> listOf(
            ActiveZone(DropZoneType.SD_CARD, "На SD-карту", Icons.Default.SdStorage, Color(0xFF2196F3)),
            ActiveZone(DropZoneType.KEEP, "Оставить", Icons.Default.CheckCircle, Color(0xFF4CAF50))
        )
        TaskGoalType.CLEAR_APP_CACHE, TaskGoalType.COMPRESS_MEDIA -> listOf(
            ActiveZone(DropZoneType.PROCESS, "Обработать", Icons.Default.Build, Color(0xFF9C27B0)),
            ActiveZone(DropZoneType.KEEP, "Оставить", Icons.Default.CheckCircle, Color(0xFF4CAF50))
        )
        TaskGoalType.RESTORE -> listOf(
            ActiveZone(DropZoneType.RESTORE, "Восстановить", Icons.Default.Restore, Color(0xFF4CAF50)),
            ActiveZone(DropZoneType.DELETE_FOREVER, "Удалить навсегда", Icons.Default.DeleteForever, Color(0xFFB00020))
        )
        TaskGoalType.CONFIGURE, TaskGoalType.REPORT -> listOf(
            ActiveZone(DropZoneType.PROCESS, "Выполнить", Icons.Default.PlayArrow, Color(0xFF4CAF50)),
            ActiveZone(DropZoneType.KEEP, "Оставить", Icons.Default.CheckCircle, Color(0xFF9C27B0))
        )
    }
}
fun generateStorageItems(level: Int): List<StorageItem> = when (level) {
    1 -> listOf(
        StorageItem("cache1", "Кэш YouTube", "cache", 120, "2025-05-20",
            recommendedAction = "Удалить",
            explanation = "Кэш — это временные файлы. Их можно безопасно удалять.",
            dangerLevel = DangerLevel.SAFE),
        StorageItem("cache2", "Кэш TikTok", "cache", 85, "2025-05-19",
            recommendedAction = "Удалить",
            explanation = "Временные файлы TikTok. Безопасно удалять.",
            dangerLevel = DangerLevel.SAFE),
        StorageItem("cache3", "Кэш Chrome", "cache", 45, "2025-05-21",
            recommendedAction = "Удалить",
            explanation = "Временные файлы браузера. Можно удалять.",
            dangerLevel = DangerLevel.SAFE)
    )
    2 -> listOf(
        StorageItem("photo1", "IMG_001.jpg", "photo", 2, "2025-05-15",
            isDuplicate = true,
            explanation = "Это дубликат — копия фотографии. Можно удалять.",
            dangerLevel = DangerLevel.SAFE),
        StorageItem("photo2", "IMG_001_копия.jpg", "photo", 2, "2025-05-15",
            isDuplicate = true,
            explanation = "Дубликат фотографии. Безопасно удалять.",
            dangerLevel = DangerLevel.SAFE),
        StorageItem("photo3", "IMG_002.jpg", "photo", 3, "2025-05-14",
            isDuplicate = false,
            explanation = "Обычная фотография. Не дубликат — оставьте её!",
            dangerLevel = DangerLevel.CAUTION)
    )
    3 -> listOf(
        StorageItem("dcim", "DCIM (фото с камеры)", "folder", 800, "2025-05-20",
            recommendedAction = "Переместить на SD",
            explanation = "Папка с фото. Занимает много места. Перенесите на SD-карту.",
            dangerLevel = DangerLevel.CAUTION),
        StorageItem("screenshots", "Скриншоты", "folder", 50, "2025-05-19",
            recommendedAction = "Оставить",
            explanation = "Папка со скриншотами. Можно оставить.",
            dangerLevel = DangerLevel.CAUTION)
    )
    4 -> listOf(
        StorageItem("download1", "installer.exe", "apk", 15, "2025-05-22",
            recommendedAction = "Удалить",
            explanation = "Установочный файл. После установки не нужен.",
            dangerLevel = DangerLevel.SAFE),
        StorageItem("download2", "старый_файл.zip", "document", 20, "2025-05-01",
            recommendedAction = "Удалить",
            explanation = "Старый архив. Можно удалять.",
            dangerLevel = DangerLevel.CAUTION),
        StorageItem("download3", "новый_файл.pdf", "document", 3, "2025-05-23",
            recommendedAction = "Оставить",
            explanation = "Новый документ. Лучше оставить.",
            dangerLevel = DangerLevel.CAUTION)
    )
    5 -> listOf(
        StorageItem("app1", "YouTube", "app", 250, "",
            recommendedAction = "Очистить кэш",
            explanation = "YouTube накопил временные файлы. Очистка безопасна.",
            dangerLevel = DangerLevel.SAFE),
        StorageItem("app2", "TikTok", "app", 180, "",
            recommendedAction = "Очистить кэш",
            explanation = "Временные файлы TikTok. Можно очищать.",
            dangerLevel = DangerLevel.SAFE),
        StorageItem("app3", "Chrome", "app", 120, "",
            recommendedAction = "Очистить кэш",
            explanation = "Кэш браузера. Безопасно очищать.",
            dangerLevel = DangerLevel.SAFE)
    )
    6 -> listOf(
        StorageItem("video1", "video_2025.mp4", "video", 150, "2025-05-15",
            recommendedAction = "Сжать",
            explanation = "Большое видео. Сжатие уменьшит размер.",
            dangerLevel = DangerLevel.CAUTION),
        StorageItem("video2", "old_video.mp4", "video", 80, "2025-04-10",
            recommendedAction = "Сжать",
            explanation = "Старое видео. Можно сжать.",
            dangerLevel = DangerLevel.CAUTION),
        StorageItem("image1", "фото_1.jpg", "photo", 5, "2025-05-10",
            recommendedAction = "Сжать",
            explanation = "Фотография. Сжатие уменьшит размер.",
            dangerLevel = DangerLevel.CAUTION)
    )
    7 -> listOf(
        StorageItem(".nomedia", ".nomedia (скрытая)", "hidden", 0, "",
            recommendedAction = "Удалить",
            explanation = "Скрытая папка. Можно удалять.",
            dangerLevel = DangerLevel.SAFE),
        StorageItem(".thumbnails", ".thumbnails (скрытая)", "hidden", 50, "",
            recommendedAction = "Удалить",
            explanation = "Скрытая папка с миниатюрами. Можно удалять.",
            dangerLevel = DangerLevel.SAFE),
        StorageItem("normal", "Обычная папка", "folder", 10, "",
            explanation = "Обычная папка. Не трогайте.",
            dangerLevel = DangerLevel.CAUTION)
    )
    8 -> listOf(
        StorageItem("deleted_photo", "недавно_удалённое.jpg", "photo", 2, "2025-05-24",
            recommendedAction = "Восстановить",
            explanation = "Фото в корзине. Восстановите его!",
            dangerLevel = DangerLevel.CAUTION)
    )
    9 -> listOf(
        StorageItem("schedule", "Планировщик очистки", "setting", 0, "",
            recommendedAction = "Настроить",
            explanation = "Настройка автоочистки. Оставьте и настройте.",
            dangerLevel = DangerLevel.SAFE)
    )
    10 -> listOf(
        StorageItem("report", "Отчёт о памяти", "report", 0, "",
            recommendedAction = "Создать отчёт",
            explanation = "Отчёт покажет, что занимает место. Создайте его!",
            dangerLevel = DangerLevel.SAFE)
    )
    11 -> listOf(
        StorageItem("empty1", "Пустая папка 1", "folder", 0, "",
            recommendedAction = "Удалить",
            explanation = "Пустая папка (0 МБ). Можно удалять.",
            dangerLevel = DangerLevel.SAFE),
        StorageItem("empty2", "Пустая папка 2", "folder", 0, "",
            recommendedAction = "Удалить",
            explanation = "Пустая папка (0 МБ). Можно удалять.",
            dangerLevel = DangerLevel.SAFE),
        StorageItem("normal", "Обычная папка", "folder", 10, "",
            explanation = "В папке есть файлы. Не удаляйте!",
            dangerLevel = DangerLevel.CAUTION)
    )
    12 -> listOf(
        StorageItem("process1", "Фоновый процесс 1", "process", 0, "",
            recommendedAction = "Очистить",
            explanation = "Фоновый процесс. Расходует батарею. Остановите!",
            dangerLevel = DangerLevel.SAFE),
        StorageItem("process2", "Фоновый процесс 2", "process", 0, "",
            recommendedAction = "Очистить",
            explanation = "Фоновый процесс. Можно остановить.",
            dangerLevel = DangerLevel.SAFE)
    )
    else -> listOf(
        StorageItem("report", "Отчёт о памяти", "report", 0, "",
            recommendedAction = "Создать отчёт",
            explanation = "Отчёт покажет, что занимает место.",
            dangerLevel = DangerLevel.SAFE)
    )
}

val cleanerTasks = listOf(
    CleanerTask(1, "🗑️ Очистка кэша", "Удалите временные файлы приложений", "Освободи 250 МБ", 250, "Удалить", false,
        "Кэш — это временные файлы. Их можно смело удалять.", TaskGoalType.FREE_SPACE, "cache",
        tutorial = "Перетащите файлы кэша в корзину"),
    CleanerTask(2, "🖼️ Дубликаты фото", "Найдите и удалите копии фотографий", "Удали дубликаты", 0, "Удалить дубликат", false,
        "Дубликаты — это копии. Они занимают место без пользы.", TaskGoalType.DELETE_DUPLICATES, "duplicate",
        tutorial = "Дубликаты — в корзину, обычные фото — оставьте"),
    CleanerTask(3, "💾 Перенос на SD-карту", "Перенесите папку с фото на карту памяти", "Перенеси DCIM", 0, "Переместить на SD", false,
        "SD-карта — дополнительная память. Туда переносят большие файлы.", TaskGoalType.SINGLE_ACTION, "move",
        tutorial = "Папку DCIM — на SD-карту"),
    // ✅ ИЗМЕНЕНО: TaskGoalType.SINGLE_ACTION → TaskGoalType.DELETE_DOWNLOADS
    CleanerTask(4, "📥 Очистка загрузок", "Удалите старые файлы", "Удали старые файлы", 0, "Удалить", false,
        "Старые загруженные файлы часто не нужны.", TaskGoalType.DELETE_DOWNLOADS, "cache",
        tutorial = "Старые файлы и .exe — в корзину"),
    CleanerTask(5, "⚙️ Тяжёлые приложения", "Очистите кэш приложений", "Очисти кэш 3 приложений", 0, "Очистить кэш", false,
        "Приложения накапливают временные файлы.", TaskGoalType.CLEAR_APP_CACHE, "cache",
        tutorial = "Все приложения — обработать"),
    CleanerTask(6, "🎬 Сжатие видео и фото", "Уменьшите размер медиафайлов", "Сожми 3 файла", 0, "Сжать", true,
        "Сжатие уменьшает размер без потери качества.", TaskGoalType.COMPRESS_MEDIA, "compress",
        tutorial = "Видео и фото — обработать"),
    CleanerTask(7, "👻 Скрытые папки", "Найдите и удалите скрытые папки", "Удали скрытые папки", 0, "Удалить", true,
        "Скрытые папки (с точкой) часто содержат мусор.", TaskGoalType.DELETE_HIDDEN, "hidden",
        tutorial = "Папки с точкой в начале — в корзину"),
    CleanerTask(8, "🔄 Восстановление фото", "Восстановите случайно удалённое фото", "Восстанови фото", 0, "Восстановить", true,
        "Удалённые фото можно вернуть из корзины.", TaskGoalType.RESTORE, "restore",
        tutorial = "Удалённое фото — восстановить"),
    CleanerTask(9, "⏰ Автоочистка", "Настройте автоматическую очистку", "Настрой автоочистку", 0, "Настроить", true,
        "Автоочистка будет сама удалять мусор.", TaskGoalType.CONFIGURE, "settings",
        tutorial = "Планировщик — выполнить"),
    CleanerTask(10, "📊 Полный аудит", "Проведите проверку памяти", "Создай отчёт", 0, "Создать отчёт", true,
        "Отчёт покажет, что занимает место.", TaskGoalType.REPORT, "report",
        tutorial = "Отчёт — выполнить"),
    CleanerTask(11, "📁 Пустые папки", "Найдите и удалите пустые папки", "Удали пустые папки", 0, "Удалить", true,
        "Пустые папки не нужны.", TaskGoalType.DELETE_EMPTY_FOLDER, "empty_folder",
        tutorial = "Папки с размером 0 МБ — в корзину"),
    CleanerTask(12, "🔋 Оптимизация батареи", "Остановите фоновые процессы", "Очисти процессы", 0, "Очистить", true,
        "Фоновые процессы расходуют батарею.", TaskGoalType.OPTIMIZE_BATTERY, "battery",
        tutorial = "Фоновые процессы — в корзину")
)