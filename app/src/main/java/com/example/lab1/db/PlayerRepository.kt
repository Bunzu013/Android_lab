package com.example.lab1.db

class PlayerRepository(private val playerDao: PlayerDao) {

    suspend fun getPlayersWithScores(): List<PlayerWithScores> {
        return playerDao.getPlayersWithScores()
    }
}
