package com.example.lab1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class ScoreView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val score = intent.getIntExtra("SCORE", 0) // Get the score passed from the previous activity
        val name = intent.getStringExtra("NAME") ?: "No name provided"
        val email = intent.getStringExtra("EMAIL") ?: "No email provided"
        val numberColor = intent.getIntExtra("COLOR", 0)
        val profileUriString = intent.getStringExtra("PROFILE_URI")
        setContent {
            ScoreScreen(
                score = score,
                onPlayAgain = {
                    // Navigate back to MasterAnd to play again
                    val intent = Intent(this, MasterAnd::class.java).apply {
                        putExtra("NAME", name)
                        putExtra("EMAIL", email)
                        putExtra("COLOR", numberColor)
                        putExtra("PROFILE_URI", profileUriString)
                    }
                    startActivity(intent)
                    finish() // Close the score screen to avoid going back here
                },
                onGoToProfile = {
                    // Navigate to ProfileScreen
                    val intent = Intent(this, ProfileScreen::class.java).apply {
                        putExtra("NAME", name)
                        putExtra("EMAIL", email)
                        putExtra("COLOR", numberColor)
                        putExtra("PROFILE_URI", profileUriString)
                    }
                    startActivity(intent)
                    finish()
                }
            )
        }
    }
}
@Composable
fun ScoreScreen(
    score: Int,
    onPlayAgain: () -> Unit,
    onGoToProfile: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Score: $score",
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = onPlayAgain,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Play Again")
        }

        Button(
            onClick = onGoToProfile,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Profile")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ScoreScreenPreview() {
    ScoreScreen(
        score = 8,
        onPlayAgain = {},
        onGoToProfile = {}
    )
}

