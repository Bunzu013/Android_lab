package com.example.lab1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginView { name, email, numberColor, profileUri ->
                val intent = Intent(this@MainActivity, ProfileScreen::class.java).apply {
                    putExtra("NAME", name)
                    putExtra("EMAIL", email)
                    putExtra("COLOR", numberColor)
                    putExtra("PROFILE_URI", profileUri?.toString())
                }
                startActivity(intent)
            }
        }
    }
}
