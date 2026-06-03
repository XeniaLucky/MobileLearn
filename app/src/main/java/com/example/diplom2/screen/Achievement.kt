package com.example.diplom2.screen

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import bd.AppDatabase

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val condition: suspend (Context, Long) -> Boolean   // изменено на suspend
)

object AchievementManager {
    private const val PREFS_NAME = "achievements"
    private lateinit var prefs: android.content.SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isUnlocked(context: Context, userId: Long, achievementId: String): Boolean {
        return prefs.getBoolean("${userId}_$achievementId", false)
    }

    suspend fun unlockAchievement(context: Context, userId: Long, achievementId: String) {
        if (!isUnlocked(context, userId, achievementId)) {
            prefs.edit().putBoolean("${userId}_$achievementId", true).apply()
        }
    }

    suspend fun checkAndUnlock(context: Context, userId: Long, achievement: Achievement) {
        if (!isUnlocked(context, userId, achievement.id) && achievement.condition(context, userId)) {
            unlockAchievement(context, userId, achievement.id)
        }
    }

    fun getUnlockedCount(context: Context, userId: Long): Int {
        val allAchievements = getAllAchievements()
        return allAchievements.count { isUnlocked(context, userId, it.id) }
    }

    suspend fun getAllAchievementsWithStatus(context: Context, userId: Long): List<Pair<Achievement, Boolean>> {
        return getAllAchievements().map { it to isUnlocked(context, userId, it.id) }
    }

    fun getAllAchievements(): List<Achievement>  {
        return listOf(
            Achievement(
                id = "first_lesson",
                title = "Первые шаги",
                description = "Пройдите первый урок",
                icon = "🏆",
                condition = { ctx, userId ->
                    val lightPrefs = ctx.getSharedPreferences("progress_light_$userId", Context.MODE_PRIVATE)
                    val anyLight = listOf("game_power", "game_call", "game_touch", "game_contacts", "game_messages", "game_camera", "game_wifi", "game_apps")
                        .any { lightPrefs.getBoolean("${it}_completed", false) }
                    val mediumPrefs = ctx.getSharedPreferences("progress_medium_$userId", Context.MODE_PRIVATE)
                    val anyMedium = listOf("game_notifications", "game_memory", "game_battery", "game_safeapps", "game_datatransfer", "game_recovery")
                        .any { mediumPrefs.getBoolean("${it}_completed", false) }
                    val expertPrefs = ctx.getSharedPreferences("progress_expert_$userId", Context.MODE_PRIVATE)
                    val anyExpert = listOf("lesson_adb", "lesson_root", "lesson_custom_roms", "lesson_optimization", "lesson_scripts", "lesson_security")
                        .any { expertPrefs.getBoolean("${it}_completed", false) }
                    anyLight || anyMedium || anyExpert
                }
            ),
            Achievement(
                id = "lesson_master_light",
                title = "Знаток начального уровня",
                description = "Пройдите все 8 уроков для начинающих",
                icon = "📱",
                condition = { ctx, userId ->
                    val prefs = ctx.getSharedPreferences("progress_light_$userId", Context.MODE_PRIVATE)
                    listOf("game_power", "game_call", "game_touch", "game_contacts", "game_messages", "game_camera", "game_wifi", "game_apps")
                        .all { prefs.getBoolean("${it}_completed", false) }
                }
            ),
            Achievement(
                id = "settings_expert",
                title = "Мастер настроек",
                description = "Пройдите игру 'Тайны настроек'",
                icon = "⚙️",
                condition = { ctx, userId ->
                    val prefs = ctx.getSharedPreferences("game_medium_$userId", Context.MODE_PRIVATE)
                    prefs.getBoolean("settings_game_completed", false)
                }
            ),
            Achievement(
                id = "lesson_master_medium",
                title = "Продвинутый пользователь",
                description = "Пройдите все 6 уроков среднего уровня",
                icon = "📱",
                condition = { ctx, userId ->
                    val prefs = ctx.getSharedPreferences("progress_medium_$userId", Context.MODE_PRIVATE)
                    listOf("game_notifications", "game_memory", "game_battery", "game_safeapps", "game_datatransfer", "game_recovery")
                        .all { prefs.getBoolean("${it}_completed", false) }
                }
            ),
            Achievement(
                id = "lesson_master_expert",
                title = "Эксперт Android",
                description = "Пройдите все 6 уроков экспертного уровня",
                icon = "🤖",
                condition = { ctx, userId ->
                    val prefs = ctx.getSharedPreferences("progress_expert_$userId", Context.MODE_PRIVATE)
                    listOf("lesson_adb", "lesson_root", "lesson_custom_roms", "lesson_optimization", "lesson_scripts", "lesson_security")
                        .all { prefs.getBoolean("${it}_completed", false) }
                }
            ),
            Achievement(
                id = "gamer",
                title = "Игроман",
                description = "Сыграйте в 3 разные игры",
                icon = "🎮",
                condition = { ctx, userId ->
                    val prefs = ctx.getSharedPreferences("games_played_$userId", Context.MODE_PRIVATE)
                    prefs.getInt("count", 0) >= 3
                }
            ),
            Achievement(
                id = "transfer_pro",
                title = "Мастер передачи данных",
                description = "Пройдите игру 'Обменник PRO'",
                icon = "📡",
                condition = { ctx, userId ->
                    val prefs = ctx.getSharedPreferences("game_medium_$userId", Context.MODE_PRIVATE)
                    prefs.getBoolean("transfer_game_completed", false)
                }
            ),
            Achievement(
                id = "streak_7",
                title = "Неделя прогресса",
                description = "Занимайтесь 7 дней подряд",
                icon = "🔥",
                condition = { ctx, userId ->
                    val prefs = ctx.getSharedPreferences("progress_medium_$userId", Context.MODE_PRIVATE)
                    prefs.getInt("streak_days", 0) >= 7
                }
            ),
            Achievement(
                id = "streak_30",
                title = "Месяц марафона",
                description = "Занимайтесь 30 дней подряд",
                icon = "🏅",
                condition = { ctx, userId ->
                    val prefs = ctx.getSharedPreferences("progress_medium_$userId", Context.MODE_PRIVATE)
                    prefs.getInt("streak_days", 0) >= 30
                }
            ),
            Achievement(
                id = "family_subscriber",
                title = "Семейный человек",
                description = "Активируйте семейный тариф",
                icon = "👨‍👩‍👧‍👦",
                condition = { ctx, userId ->
                    val db = bd.AppDatabase.getInstance(ctx)
                    val user = db.userDao().getUserById(userId)
                    user?.familySubscriptionActive == true
                }
            ),
            Achievement(
                id = "premium_game_buyer",
                title = "Коллекционер",
                description = "Купите любую премиум-игру",
                icon = "💎",
                condition = { ctx, userId ->
                    val db = bd.AppDatabase.getInstance(ctx)
                    val purchases = db.userPurchaseDao().getUserPurchases(userId)
                    purchases.any { it.purchaseType == "game" && it.gameId != null }
                }
            )
        )
    }
}