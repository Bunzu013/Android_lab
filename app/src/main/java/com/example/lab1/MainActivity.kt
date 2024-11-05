package com.example.lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileScreenInitial() // Wywołanie funkcji do wyświetlenia ekranu
        }
    }

    @Composable
    fun ProfileScreenInitial() {
        val name = rememberSaveable { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MasterAnd",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 48.dp)
            )
            Box {
                Image(
                    painter = painterResource(
                        id = R.drawable.ic_launcher_background // Upewnij się, że obrazek istnieje w katalogu res/drawable
                    ),
                    contentDescription = "Profile photo",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text("Name") },
                singleLine = true,
                isError = false,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                supportingText = { Text("Name can't be empty") }
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ProfileScreenInitialPreview() {
        ProfileScreenInitial() // Możesz również dodać podgląd tutaj
    }
}
