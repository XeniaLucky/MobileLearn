package dao

import androidx.room.*
import table.UserPurchase

@Dao
interface UserPurchaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchase(purchase: UserPurchase)


    @Query("SELECT * FROM user_purchases WHERE userId = :userId AND gameId = :gameId")
    suspend fun getPurchaseForGame(userId: Long, gameId: Int): UserPurchase?

    @Query("SELECT * FROM user_purchases WHERE userId = :userId AND purchaseType = 'family_subscription' AND expiryDate >= date('now')")
    suspend fun getActiveFamilySubscription(userId: Long): UserPurchase?

}

