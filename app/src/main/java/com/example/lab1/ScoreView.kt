import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.MutableLiveData
import com.example.lab1.ScoreViewModel
import com.example.lab1.db.ScoreWithPlayer

class ScoreView : ComponentActivity() {

    private val scoreViewModel: ScoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Załaduj dane
        scoreViewModel.loadScoresWithPlayers()

        setContent {
            ScoreScreen(
                scoresWithPlayers = scoreViewModel.scoresWithPlayers
            )
        }
    }
}

@Composable
fun ScoreScreen(scoresWithPlayers: LiveData<List<ScoreWithPlayer>>) {
    // Pobranie wartości z LiveData
    val scores = scoresWithPlayers.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Wyświetlanie wyników
        scores.value?.forEach { scoreWithPlayer ->
            Text(
                text = "${scoreWithPlayer.name}: ${scoreWithPlayer.score}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Przycisk przejścia do profilu
        Button(onClick = { /* Użyj jakiejś akcji */ }) {
            Text("Go to Profile")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScoreScreenPreview() {
    // Tworzenie przykładowych danych w LiveData
    val sampleScores = MutableLiveData<List<ScoreWithPlayer>>()
    sampleScores.value = listOf(
        ScoreWithPlayer(id = 1, name = "John Doe", score = 100),
        ScoreWithPlayer(id = 2, name = "Jane Doe", score = 150)
    )

    // Używamy ScoreScreen, przekazując LiveData
    ScoreScreen(scoresWithPlayers = sampleScores)
}

