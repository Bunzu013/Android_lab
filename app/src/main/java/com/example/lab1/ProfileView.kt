package com.example.lab1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

class ProfileView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DisplayProfileData()
        }
    }

    @Composable
    fun DisplayProfileData() {
        // Retrieve data passed from MainActivity
        val name = intent.getStringExtra("NAME") ?: "Unknown"
        val email = intent.getStringExtra("EMAIL") ?: "Unknown"
        val color = intent.getStringExtra("COLOR") ?: "Unknown"
        val profileUriString = intent.getStringExtra("PROFILE_URI") ?: ""
        val profileUri = Uri.parse(profileUriString)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Image(
                painter = if (profileUri != null) {
                    rememberImagePainter(profileUri)
                } else {
                    painterResource(id = R.drawable.ic_question)
                },
                contentDescription = "Profile photo",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )


            Text("Name: $name", style = MaterialTheme.typography.bodyLarge)
            Text("Email: $email", style = MaterialTheme.typography.bodyLarge)
            Text("Number Color: $color", style = MaterialTheme.typography.bodyLarge)
        }
    }

}
