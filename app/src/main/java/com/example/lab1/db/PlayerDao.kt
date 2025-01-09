package com.example.lab1.db

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction

@Dao
interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: Player)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(score: Score)

    @Query("SELECT * FROM players")
    suspend fun getAllPlayers(): List<Player>

    @Transaction
    @Query("SELECT * FROM players")
    suspend fun getPlayersWithScores(): List<PlayerWithScores> // Łączenie danych gracza z wynikami
}

data class PlayerWithScores(
    @Embedded val player: Player,
    @Relation(
        parentColumn = "id",
        entityColumn = "playerId"
    )
    val scores: List<Score> // Wszystkie wyniki gracza
)
