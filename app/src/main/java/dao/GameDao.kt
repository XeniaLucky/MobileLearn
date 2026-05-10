package dao

import androidx.room.*
import table.Game

@Dao
interface GameDao {
    @Query("SELECT * FROM games WHERE levelRequiredId IS NULL OR levelRequiredId = :levelId")
    suspend fun getGamesForLevel(levelId: Int): List<Game>

    @Query("SELECT * FROM games WHERE id = :gameId")
    suspend fun getGameById(gameId: Int): Game?

}