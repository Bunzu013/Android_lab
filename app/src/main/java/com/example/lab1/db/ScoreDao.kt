package com.example.lab1.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ScoreDao {
    @Query("""
        SELECT scores.id, players.name, scores.score 
        FROM scores 
        INNER JOIN players ON scores.player_id = players.id 
        ORDER BY scores.score ASC
    """)
    fun getAllScores(): LiveData<List<ScoreWithPlayer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(score: Score)
}

data class ScoreWithPlayer(
    val id: Int,
    val name: String,
    val score: Int
)
