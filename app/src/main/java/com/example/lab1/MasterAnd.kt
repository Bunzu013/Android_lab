package com.example.lab1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.scale

data class GameRowState(
    val selectedColors: List<Color> = listOf(),
    val feedbackColors: List<Color> = listOf()
)

class MasterAnd : ComponentActivity() {

    var colorList = listOf(
        Color.Red,
        Color.Blue,
        Color.Green,
        Color.Yellow,
        Color.Black,
        Color.Magenta,
        Color.Cyan,
        Color.Gray,
        Color(128, 0, 128),
        Color(255, 165, 0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MasterAndScreen(
                name = intent?.getStringExtra("NAME") ?: "No name provided",
                email = intent?.getStringExtra("EMAIL") ?: "No email provided",
                numberColor = intent?.getIntExtra("COLOR", 5) ?: 5,
                profileUriString = intent?.getStringExtra("PROFILE_URI")
            )
        }
    }

    @Composable
    fun MasterAndScreen(
        name: String = "Preview Name",
        email: String = "preview@example.com",
        numberColor: Int = 5,
        profileUriString: String? = null
    ) {
        val profileImageUri = profileUriString?.let { Uri.parse(it) }

        val infiniteTransition = rememberInfiniteTransition()
        val titleScale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        // Losowanie dostępnych kolorów na podstawie numberColor
        //val availableColors = selectRandomColors(colorList, numberColor)

        var gameRows by remember { mutableStateOf(mutableListOf<GameRowState>()) }
        var selectedColors by remember { mutableStateOf(List(4) { Color.Transparent }) }
        var availableColors by remember {  mutableStateOf(selectRandomColors(colorList, numberColor))  }
        var correctColors by remember { mutableStateOf(selectRandomColors(availableColors, 4)) }
        var score by remember { mutableStateOf(0) }
        var gameFinished by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MasterAnd Game",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .scale(titleScale)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = "Your score: $score",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            gameRows.forEachIndexed { index, rowState ->
                AnimatedVisibility(
                    visible = true, // Wszystkie wiersze są widoczne
                    enter = expandVertically(expandFrom = Alignment.Top)
                ) {
                    GameRow(
                        selectedColors = rowState.selectedColors,
                        feedbackColors = rowState.feedbackColors,
                        clickable = false,
                        onSelectColorClick = {},
                        onCheckClick = {}
                    )
                }
            }

            if (!gameFinished) {
                GameRow(
                    selectedColors = selectedColors,
                    feedbackColors = listOf(),
                    clickable = true,
                    onSelectColorClick = { index ->
                        selectedColors = selectedColors.toMutableList().also {
                            it[index] = selectNextAvailableColor(
                                availableColors,
                                selectedColors,
                                selectedColors[index],
                                numberColor
                            )
                        }
                    },
                    onCheckClick = {
                        if (!selectedColors.contains(Color.Transparent)) {
                            val feedback = checkColors(selectedColors, correctColors)

                            gameRows = gameRows.toMutableList().also {
                                it.add(GameRowState(selectedColors.toList(), feedback))
                            }

                            if (feedback.all { it == Color.Green }) {
                                gameFinished = true
                            } else {
                                selectedColors = List(4) { Color.Transparent }
                            }
                            score++
                        }
                    }
                )
            }

            if (gameFinished) {
                Button(onClick = {
                    gameRows = mutableListOf()
                    selectedColors = List(4) { Color.Transparent }
                    correctColors = selectRandomColors(availableColors, numberColor)
                    score = 0
                    gameFinished = false
                }) {
                    Text("Start over")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    val intent = Intent(this@MasterAnd, ScoreView::class.java).apply {
                        putExtra("NAME", name)
                        putExtra("EMAIL", email)
                        putExtra("COLOR", numberColor)
                        putExtra("PROFILE_URI", profileImageUri?.toString())
                        putExtra("SCORE", score)
                    }
                    startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Score")
            }

            Button(
                onClick = {
                    val intent = Intent(this@MasterAnd, ProfileScreen::class.java).apply {
                        putExtra("NAME", name)
                        putExtra("EMAIL", email)
                        putExtra("COLOR", numberColor)
                        putExtra("PROFILE_URI", profileImageUri?.toString())
                    }
                    startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Profile")
            }
        }
    }
    fun checkColors(
        selectedColors: List<Color>,
        correctColors: List<Color>
    ): List<Color> {
        val feedback = mutableListOf<Color>()
        val checked = mutableListOf<Boolean>()

        // Ensure both lists are of the same length
        val size = selectedColors.size.coerceAtMost(correctColors.size)

        // Check for exact matches (green feedback)
        for (i in 0 until size) {
            if (selectedColors[i] == correctColors[i]) {
                feedback.add(Color.Green)
                checked.add(true)
            } else {
                feedback.add(Color.Transparent)
                checked.add(false)
            }
        }

        // Check for color matches in wrong positions (yellow feedback)
        for (i in 0 until size) {
            if (feedback[i] == Color.Transparent) {
                for (j in 0 until size) {
                    if (!checked[j] && selectedColors[i] == correctColors[j]) {
                        feedback[i] = Color.Yellow
                        checked[j] = true
                        break
                    }
                }
            }
        }

        // If the list sizes are unequal, handle the remaining elements (optional, based on game logic)
        while (feedback.size < selectedColors.size) {
            feedback.add(Color.Transparent)
        }

        return feedback
    }

    fun selectRandomColors(availableColors: List<Color>, numberColor: Int): List<Color> {
        return availableColors.shuffled().take(numberColor)
    }

    fun selectNextAvailableColor(
        colors: List<Color>,
        selectedColors: List<Color>,
        currentColor: Color,
        numberColor: Int
    ): Color {
        val availableColors = colors.take(numberColor)
        val currentIndex = availableColors.indexOf(currentColor)

        for (i in (currentIndex + 1) until availableColors.size) {
            if (!selectedColors.contains(availableColors[i])) {
                return availableColors[i]
            }
        }

        return availableColors.firstOrNull { !selectedColors.contains(it) } ?: currentColor
    }

    @Composable
    fun CircularButton(
        onClick: () -> Unit,
        color: Color,
        enabled: Boolean
    ) {
        val animColor = remember { Animatable(color) }

        LaunchedEffect(color) {
            animColor.animateTo(
                targetValue = color,
                animationSpec = tween(500, easing = LinearEasing)
            )
        }

        Button(
            onClick = onClick,
            modifier = Modifier
                .size(50.dp)
                .background(animColor.value, shape = CircleShape)
                .border(2.dp, Color.Black, CircleShape),
            colors = ButtonDefaults.buttonColors(containerColor = animColor.value),
            shape = CircleShape,
            enabled = enabled
        ) {}
    }

    @Composable
    fun FeedbackCircle(color: Color, delayMillis: Int) {
        val animColor = remember { Animatable(Color.Transparent) }

        LaunchedEffect(color) {
            kotlinx.coroutines.delay(delayMillis.toLong())
            animColor.animateTo(
                targetValue = color,
                animationSpec = tween(durationMillis = 500) // Czas trwania animacji
            )
        }

        Box(
            modifier = Modifier
                .size(15.dp)
                .background(animColor.value, shape = CircleShape)
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

            AnimatedVisibility(
                visible = !selectedColors.contains(Color.Transparent),
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                IconButton(
                    onClick = onCheckClick,
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Check",
                        tint = Color.Black
                    )
                }
            }

            feedbackColors.forEachIndexed { index, feedbackColor ->
                FeedbackCircle(color = feedbackColor, delayMillis = index * 300) // Opóźnienie dla kolejnych kółek
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MasterAndPreview() {
        MasterAndScreen()
    }
}
