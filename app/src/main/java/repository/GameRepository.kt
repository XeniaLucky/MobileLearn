package repository

import android.annotation.SuppressLint
import dao.GameDao
import dao.UserPurchaseDao
import table.Game
import table.UserPurchase
import java.time.LocalDate

class GameRepository(
    private val gameDao: GameDao,
    private val purchaseDao: UserPurchaseDao
) {
    suspend fun getAvailableGames(userId: Long, levelId: Int): List<Game> {
        val allGames = gameDao.getGamesForLevel(levelId)
        val familyActive = purchaseDao.getActiveFamilySubscription(userId) != null
        return allGames.map { game ->
            if (game.price == 0) game
            else {
                val purchased = purchaseDao.getPurchaseForGame(userId, game.id) != null
                if (purchased || familyActive) game.copy(price = 0) // доступно бесплатно
                else game
            }
        }
    }

    @SuppressLint("NewApi")
    suspend fun purchaseGame(userId: Long, gameId: Int) {
        val purchase = UserPurchase(
            userId = userId,
            purchaseType = "game",
            gameId = gameId,
            purchaseDate = LocalDate.now().toString(),
            expiryDate = null
        )
        purchaseDao.insertPurchase(purchase)
    }
    suspend fun getGameById(gameId: Int): Game? = gameDao.getGameById(gameId)
    suspend fun isGamePurchased(userId: Long, gameId: Int): Boolean {
        return purchaseDao.getPurchaseForGame(userId, gameId) != null
    }
}

