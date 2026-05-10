package table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class Game(
    @PrimaryKey
    val id: Int,
    val gameKey: String,
    val name: String,
    val description: String,
    val price: Int,                // 0 = бесплатная
    val levelRequiredId: Int?,     // null - для всех уровней
    val isPremium: Boolean,
    val iconResName: String?
)