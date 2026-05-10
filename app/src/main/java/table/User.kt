package table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String,
    val passwordHash: String,
    val name: String,
    val levelId: Int,   // 1-beginner, 2-intermediate, 3-expert
    val isPremium: Boolean = false,
    val familySubscriptionActive: Boolean = false,
    val familySubscriptionExpiry: String? = null
)