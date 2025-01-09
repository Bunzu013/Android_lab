package com.example.lab1.db

class ScoreRepository(private val scoreDao: ScoreDao) {
    fun getAllScores() = scoreDao.getAllScores()
    suspend fun insertScore(score: Score) = scoreDao.insertScore(score)
}
