package bd

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import table.UserPurchase
import dao.GameDao
import dao.UserDao
import dao.UserPurchaseDao
import table.Game
import table.User

@Database(entities = [User::class, Game::class, UserPurchase::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun gameDao(): GameDao
    abstract fun userPurchaseDao(): UserPurchaseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addCallback(PrepopulateCallback()) // ← callback добавляется в билдер
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}