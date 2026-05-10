package bd

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

class PrepopulateCallback : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // Вставляем тестового пользователя с plain-паролем (BCrypt пока отключён)
        db.execSQL("INSERT OR IGNORE INTO users (id, email, passwordHash, name, levelId, isPremium) VALUES (1, 'test@test.com', '123', 'Тестовый', 1, 0)")
        // Вставляем игры
        db.execSQL("INSERT OR IGNORE INTO games (id, gameKey, name, description, price, levelRequiredId, isPremium, iconResName) VALUES " +
                "(1, 'tap_run', 'Тап-ран', 'Тренировка скорости', 0, NULL, 0, 'touch_app'), " +
                "(2, 'swipe_quiz', 'Свайп-квиз', 'Учись свайпам', 0, NULL, 0, 'swipe'), " +
                "(3, 'settings_puzzle', 'Собери настройку', 'Настрой телефон', 0, NULL, 0, 'settings'), " +
                "(4, 'pro_photographer', 'Продвинутый фотограф', 'SIMULATOR РУЧНЫХ НАСТРОЕК КАМЕРЫ', 230, 2, 1, 'camera'), " +
                "(5, 'cyber_detective', 'Кибердетектив', 'Защита от мошенников', 230, 2, 1, 'security'), " +
                "(6, 'gestures', 'Жесты', 'Сложные уровни на скорость', 230, 2, 1, 'gesture')")
    }
}