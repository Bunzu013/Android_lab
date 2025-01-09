package com.example.lab1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.lab1.db.ScoreWithPlayer

class ScoreViewModel : ViewModel() {

    private val _scoresWithPlayers = MutableLiveData<List<ScoreWithPlayer>>()
    val scoresWithPlayers: LiveData<List<ScoreWithPlayer>> = _scoresWithPlayers

    fun loadScoresWithPlayers() {
        // Przykład: wstawienie przykładowych danych
        _scoresWithPlayers.value = listOf(
            ScoreWithPlayer(id = 1, name = "John Doe", score = 100),
            ScoreWithPlayer(id = 2, name = "Jane Doe", score = 150),
            ScoreWithPlayer(id = 3, name = "Alice", score = 200)
        )
    }
}

