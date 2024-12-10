package com.example.lab1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

class ProfileScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileScreenContent()
        }
    }

    @Composable
    fun ProfileImageWithPicker(
        profileImageUri: Uri?,
        onImageSelected: (Uri?) -> Unit
    ) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = if (profileImageUri != null) {
                    rememberImagePainter(profileImageUri)
                } else {
                    painterResource(id = R.drawable.ic_question) // Default image
                },
                contentDescription = "Profile photo",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }

    @Composable
    fun ProfileScreenContent() {
        // Retrieve the submitted data passed through intent
        val name = intent.getStringExtra("NAME") ?: "No name provided"
        val email = intent.getStringExtra("EMAIL") ?: "No email provided"
        val numberColor = intent.getIntExtra("COLOR", 4)
        val profileUriString = intent.getStringExtra("PROFILE_URI")
        val profileImageUri = if (profileUriString != null) Uri.parse(profileUriString) else null

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Profile Information",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Profile image display
            ProfileImageWithPicker(
                profileImageUri = profileImageUri,
                onImageSelected = { /* No action here as we are just displaying the image */ }
            )

            // Display the name, email, and color number
            Text(
                text = "Name: $name",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Email: $email",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Number color: $numberColor",
                style = MaterialTheme.typography.bodyLarge
            )

            // "Next" button
            Button(
                onClick = {
                    // Proceed to next activity (MasterAnd)
                    val intent = Intent(this@ProfileScreen, MasterAnd::class.java).apply {
                        putExtra("NAME", name)
                        putExtra("EMAIL", email)
                        putExtra("COLOR", numberColor)
                        putExtra("PROFILE_URI", profileImageUri?.toString())
                    }
                    startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start game")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ProfileScreenPreview() {
        ProfileScreenContent()
    }
}
