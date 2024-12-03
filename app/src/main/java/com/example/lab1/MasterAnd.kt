package com.example.lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class GameRowState(
    val selectedColors: List<Color> = listOf(),
    val feedbackColors: List<Color> = listOf()
)

class MasterAnd : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MasterAndScreen()
        }
    }

    @Composable
    fun MasterAndScreen() {
        // Stan gry
        var gameRows by remember { mutableStateOf(mutableListOf<GameRowState>()) }
        var selectedColors by remember { mutableStateOf(listOf(Color.Transparent, Color.Transparent, Color.Transparent, Color.Transparent)) }
        var correctColors by remember { mutableStateOf(selectRandomColors(listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow))) }
        var score by remember { mutableStateOf(0) }
        var gameFinished by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Wyświetlenie wyniku
            Text(
                text = "Your score: $score",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            // Wyświetlanie poprzednich wierszy (rządów) z feedbackiem
            gameRows.forEach { rowState ->
                GameRow(
                    selectedColors = rowState.selectedColors,
                    feedbackColors = rowState.feedbackColors,
                    clickable = false,  // Poprzednie rzędy są zablokowane
                    onSelectColorClick = {},
                    onCheckClick = {}
                )
            }
            if(!gameFinished) {
    // Bieżący rząd
    GameRow(
        selectedColors = selectedColors,
        feedbackColors = listOf(),
        clickable = true,  // Aktywny tylko aktualny rząd
        onSelectColorClick = { index ->
            selectedColors = selectedColors.toMutableList().also {
                it[index] = selectNextAvailableColor(
                    listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow),
                    selectedColors,
                    index
                )
            }
        },
        onCheckClick = {
            if (!selectedColors.contains(Color.Transparent)) {
                val feedback = checkColors(selectedColors, correctColors)

                gameRows = gameRows.toMutableList().also {
                    it.add(
                        GameRowState(
                            selectedColors.toList(),
                            feedback
                        )
                    ) // Dodajemy próbę z feedbackiem
                }

                // Jeśli użytkownik odgadł układ
                if (feedback.all { it == Color.Green }) {
                    gameFinished = true
                } else {
                    // Resetowanie tylko bieżącego wiersza
                    selectedColors = listOf(
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent
                    )
                }
                score++
            }
        }
    )
}
            // Przycisk restartu
            Button(onClick = {
                gameRows = mutableListOf() // Resetujemy wszystkie wiersze
                selectedColors = listOf(Color.Transparent, Color.Transparent, Color.Transparent, Color.Transparent)
                correctColors = selectRandomColors(listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow))
                score = 0
                gameFinished = false
            }) {
                Text("Start over")
            }
        }
    }

    // Wybranie następnego dostępnego koloru
    fun selectNextAvailableColor(
        availableColors: List<Color>,
        selectedColors: List<Color>,
        index: Int
    ): Color {
        val available = availableColors.filter { it !in selectedColors }
        return available.firstOrNull() ?: Color.Transparent
    }

    // Losowanie 4 unikalnych kolorów
    fun selectRandomColors(availableColors: List<Color>): List<Color> {
        return availableColors.shuffled().take(4)
    }

    // Porównanie wybranych kolorów z poprawnymi
    fun checkColors(
        selectedColors: List<Color>,
        correctColors: List<Color>
    ): List<Color> {
        val feedback = mutableListOf<Color>()
        val checked = mutableListOf(false, false, false, false)

        // Idealne dopasowanie
        for (i in selectedColors.indices) {
            if (selectedColors[i] == correctColors[i]) {
                feedback.add(Color.Green)
                checked[i] = true
            } else {
                feedback.add(Color.Transparent) // Póki co brak feedbacku (nie trafiony)
            }
        }

        // Częściowe dopasowanie
        for (i in selectedColors.indices) {
            if (feedback[i] == Color.Transparent) {
                for (j in correctColors.indices) {
                    if (!checked[j] && selectedColors[i] == correctColors[j]) {
                        feedback[i] = Color.Yellow
                        checked[j] = true
                        break
                    }
                }
            }
        }

        return feedback
    }

    @Composable
    fun CircularButton(
        onClick: () -> Unit,
        color: Color,
        enabled: Boolean
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .size(50.dp)
                .background(color = color, shape = CircleShape)
                .border(2.dp, Color.Black, CircleShape),
            colors = ButtonDefaults.buttonColors(
                containerColor = color,
                contentColor = Color.Black
            ),
            shape = CircleShape,
            enabled = enabled
        ) {}
    }

    @Composable
    fun FeedbackCircle(color: Color) {
        Box(
            modifier = Modifier
                .size(15.dp)
                .background(color, shape = CircleShape)
                .border(2.dp, Color.Black, CircleShape)
        )
    }

    @Composable
    fun GameRow(
        selectedColors: List<Color>,
        feedbackColors: List<Color>,
        clickable: Boolean,
        onSelectColorClick: (Int) -> Unit,
        onCheckClick: () -> Unit
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            selectedColors.forEachIndexed { index, color ->
                CircularButton(
                    onClick = { onSelectColorClick(index) },
                    color = color,
                    enabled = clickable
                )
            }

            // Przycisk sprawdzania
            IconButton(
                onClick = onCheckClick,
                modifier = Modifier.size(50.dp),
                enabled = !selectedColors.contains(Color.Transparent) // Aktywny tylko jeśli wszystkie kolory są ustawione
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check",
                    tint = Color.Black
                )
            }

            // Wyświetlenie feedbacku (kolory)
            feedbackColors.forEach { feedbackColor ->
                FeedbackCircle(color = feedbackColor)
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun CircularButtonPreview() {
        MasterAndScreen()
    }
}
