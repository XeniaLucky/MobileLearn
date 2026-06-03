package com.example.diplom2.screen.dop_content.gameMedium.GameCleaner

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CleanerViewModel(context: Context) : ViewModel() {
    private val prefs = context.getSharedPreferences("cleaner_game", Context.MODE_PRIVATE)

    private val _currentTaskIndex = MutableStateFlow(prefs.getInt("current_task", 1))
    val currentTaskIndex: StateFlow<Int> = _currentTaskIndex.asStateFlow()

    private val _score = MutableStateFlow(prefs.getInt("score", 0))
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _completedTasks = MutableStateFlow(prefs.getInt("completed_tasks", 0))
    val completedTasks: StateFlow<Int> = _completedTasks.asStateFlow()

    private val _robotMessage = MutableStateFlow<RobotMessage?>(null)
    val robotMessage: StateFlow<RobotMessage?> = _robotMessage.asStateFlow()

    private val _currentFile = MutableStateFlow<StorageItem?>(null)
    val currentFile: StateFlow<StorageItem?> = _currentFile.asStateFlow()

    private val _isGameFinished = MutableStateFlow(false)
    val isGameFinished: StateFlow<Boolean> = _isGameFinished.asStateFlow()

    private val _levelFiles = MutableStateFlow<List<StorageItem>>(emptyList())
    val levelFiles: StateFlow<List<StorageItem>> = _levelFiles.asStateFlow()

    private val _activeZones = MutableStateFlow<List<ActiveZone>>(emptyList())
    val activeZones: StateFlow<List<ActiveZone>> = _activeZones.asStateFlow()

    private val _isExiting = MutableStateFlow(false)
    val isExiting: StateFlow<Boolean> = _isExiting.asStateFlow()

    init {
        loadLevel()
    }

    private fun loadLevel() {
        val task = cleanerTasks.getOrNull(_currentTaskIndex.value - 1)
        if (task == null || _completedTasks.value >= cleanerTasks.size) {
            _isGameFinished.value = true
            return
        }

        val items = generateStorageItems(_currentTaskIndex.value)
        if (items.isNotEmpty()) {
            _levelFiles.value = items
            _currentFile.value = items.first()
            _activeZones.value = getZonesForTask(task.goalType)
            _robotMessage.value = RobotMessage(
                "📋 ${task.description}\n💡 ${task.tutorial ?: ""}",
                RobotMood.NEUTRAL
            )
        } else {
            completeLevel()
        }
    }

    fun processDrop(file: StorageItem, zone: DropZoneType): Boolean {  // ← Добавили ": Boolean"
        val task = cleanerTasks[_currentTaskIndex.value - 1]
        var isCorrect = false
        var message = ""

        when (task.goalType) {
            TaskGoalType.FREE_SPACE -> {
                if (zone == DropZoneType.TRASH && file.type == "cache") {
                    isCorrect = true
                    message = "Отлично! Временные файлы удалены. 👍"
                } else if (zone == DropZoneType.KEEP && file.type != "cache") {
                    isCorrect = true
                    message = "Верно! Этот файл не является мусором. 👍"
                } else {
                    message = "❌ ${file.explanation ?: "Это действие здесь не подходит."}"
                }
            }

            TaskGoalType.DELETE_DUPLICATES -> {
                if (zone == DropZoneType.TRASH && file.isDuplicate) {
                    isCorrect = true
                    message = "Верно! Лишняя копия удалена. 👍"
                } else if (zone == DropZoneType.KEEP && !file.isDuplicate) {
                    isCorrect = true
                    message = "Правильно! Оригинальное фото нужно сохранить. 👍"
                } else {
                    message = "❌ ${file.explanation ?: "Будьте внимательны с копиями."}"
                }
            }

            TaskGoalType.SINGLE_ACTION -> {
                if (zone == DropZoneType.SD_CARD && file.name.contains("DCIM")) {
                    isCorrect = true
                    message = "Отлично! Тяжёлая папка перенесена на SD-карту. 👍"
                } else if (zone == DropZoneType.KEEP && !file.name.contains("DCIM")) {
                    isCorrect = true
                    message = "Верно! Этот файл трогать не нужно. 👍"
                } else {
                    message = "❌ ${file.explanation ?: "Неверное действие для этого файла."}"
                }
            }

            TaskGoalType.DELETE_DOWNLOADS -> {
                if (zone == DropZoneType.TRASH && (file.type == "apk" || file.name.contains("старый"))) {
                    isCorrect = true
                    message = "Верно! Старый файл удалён. 👍"
                } else if (zone == DropZoneType.KEEP && file.type != "apk" && !file.name.contains("старый")) {
                    isCorrect = true
                    message = "Верно! Этот новый файл лучше оставить. 👍"
                } else {
                    message = "❌ ${file.explanation ?: "Неверное действие для этого файла."}"
                }
            }

            TaskGoalType.CLEAR_APP_CACHE -> {
                if (zone == DropZoneType.PROCESS && file.type == "app") {
                    isCorrect = true
                    message = "Кэш приложения успешно очищен! 👍"
                } else if (zone == DropZoneType.KEEP && file.type != "app") {
                    isCorrect = true
                    message = "Верно! Это не приложение, его кэш чистить не нужно. 👍"
                } else {
                    message = "❌ ${file.explanation ?: "Здесь нужно очищать только кэш приложений."}"
                }
            }

            TaskGoalType.COMPRESS_MEDIA -> {
                if (zone == DropZoneType.PROCESS && (file.type == "video" || file.type == "photo")) {
                    isCorrect = true
                    message = "Файл успешно сжат! Место освобождено. 👍"
                } else if (zone == DropZoneType.KEEP && file.type != "video" && file.type != "photo") {
                    isCorrect = true
                    message = "Верно! Этот файл сжимать не нужно. 👍"
                } else {
                    message = "❌ ${file.explanation ?: "Сжимать имеет смысл только фото и видео."}"
                }
            }

            TaskGoalType.DELETE_HIDDEN -> {
                if (zone == DropZoneType.TRASH && file.type == "hidden") {
                    isCorrect = true
                    message = "Скрытая папка с мусором удалена! 👍"
                } else if (zone == DropZoneType.KEEP && file.type != "hidden") {
                    isCorrect = true
                    message = "Верно! Это обычная папка, её трогать не нужно. 👍"
                } else {
                    message = "❌ Это не скрытая системная папка, её не нужно удалять."
                }
            }

            TaskGoalType.RESTORE -> {
                if (zone == DropZoneType.RESTORE && file.name.contains("недавно_удалённое")) {
                    isCorrect = true
                    message = "Фотография успешно восстановлена! 👍"
                } else {
                    message = "❌ ${file.explanation ?: "Это фото нужно восстановить, а не удалять!"}"
                }
            }

            TaskGoalType.CONFIGURE -> {
                if (zone == DropZoneType.PROCESS && file.name.contains("Планировщик")) {
                    isCorrect = true
                    message = "Автоочистка успешно настроена! 👍"
                } else {
                    message = "❌ ${file.explanation ?: "Эту настройку нужно выполнить, а не удалять."}"
                }
            }

            TaskGoalType.REPORT -> {
                if (zone == DropZoneType.PROCESS && file.type == "report") {
                    isCorrect = true
                    message = "Отчёт о памяти успешно создан! 👍"
                } else {
                    message = "❌ ${file.explanation ?: "Отчёт нужно создать, это полезная функция."}"
                }
            }

            TaskGoalType.DELETE_EMPTY_FOLDER -> {
                if (zone == DropZoneType.TRASH && file.type == "folder" && file.sizeMB == 0) {
                    isCorrect = true
                    message = "Пустая папка удалена! 👍"
                } else if (zone == DropZoneType.KEEP && (file.type != "folder" || file.sizeMB > 0)) {
                    isCorrect = true
                    message = "Верно! В этой папке есть файлы, оставляем её. 👍"
                } else {
                    message = "❌ Эта папка не пустая, её не нужно удалять."
                }
            }

            TaskGoalType.OPTIMIZE_BATTERY -> {
                if (zone == DropZoneType.TRASH && file.type == "process") {
                    isCorrect = true
                    message = "Фоновый процесс остановлен! Батарея скажет спасибо. 👍"
                } else if (zone == DropZoneType.KEEP && file.type != "process") {
                    isCorrect = true
                    message = "Верно! Это не фоновый процесс, оставляем. 👍"
                } else {
                    message = "❌ ${file.explanation ?: "Это не фоновый процесс."}"
                }
            }
        }

        if (isCorrect) {
            _score.value += 10
            _robotMessage.value = RobotMessage(message, RobotMood.HAPPY)
            _isExiting.value = true

            viewModelScope.launch {
                delay(400)
                val remainingFiles = _levelFiles.value.filter { it.id != file.id }
                _levelFiles.value = remainingFiles
                _isExiting.value = false

                if (remainingFiles.isEmpty()) {
                    completeLevel()
                } else {
                    _currentFile.value = remainingFiles.first()
                }
            }
            return true  // ← ДОБАВЛЕНО: возвращаем true при успехе
        } else {
            _score.value = (_score.value - 5).coerceAtLeast(0)
            _robotMessage.value = RobotMessage(message, RobotMood.SAD)
            return false  // ← ДОБАВЛЕНО: возвращаем false при ошибке
        }
    }

    private fun completeLevel() {
        _completedTasks.value += 1
        _robotMessage.value = RobotMessage("🎉 Уровень пройден!", RobotMood.EXCITED)

        if (_currentTaskIndex.value < cleanerTasks.size) {
            viewModelScope.launch {
                delay(1200)
                _currentTaskIndex.value += 1
                saveProgress()
                loadLevel()
            }
        } else {
            viewModelScope.launch {
                delay(1200)
                _isGameFinished.value = true
                saveProgress()
            }
        }
    }

    fun showHint(file: StorageItem) {
        _robotMessage.value = RobotMessage(
            file.explanation ?: "Попробуйте перетащить файл в подходящую зону.",
            RobotMood.THINKING
        )
    }

    fun clearRobotMessage() {
        _robotMessage.value = null
    }

    fun resetGame() {
        _currentTaskIndex.value = 1
        _score.value = 0
        _completedTasks.value = 0
        _robotMessage.value = RobotMessage("Начинаем заново! 🤗", RobotMood.HAPPY)
        _isGameFinished.value = false
        _isExiting.value = false
        saveProgress()
        loadLevel()
    }

    fun purchasePremium() {
        _robotMessage.value = RobotMessage("Премиум разблокирован! 🎉", RobotMood.EXCITED)
    }

    private fun saveProgress() {
        prefs.edit()
            .putInt("current_task", _currentTaskIndex.value)
            .putInt("score", _score.value)
            .putInt("completed_tasks", _completedTasks.value)
            .apply()
    }
}

class CleanerViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CleanerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CleanerViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}