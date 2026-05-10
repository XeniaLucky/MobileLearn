package table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_purchases")
data class UserPurchase(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val userId: Long,
        val purchaseType: String,      // "game" или "family_subscription"
        val gameId: Int?,              // для покупки игры
        val purchaseDate: String,
        val expiryDate: String?        // для подписки
)
