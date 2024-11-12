package com.example.lab1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import coil.compose.rememberImagePainter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileScreen()
        }
    }

    @Composable
    fun OutlinedTextFieldWithError(
        value: String,
        onValueChange: (String) -> Unit,
        label: String,
        isError: Boolean,
        errorMessage: String,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        validateInput: (String) -> Boolean
    ) {
        Column {
            OutlinedTextField(
                value = value,
                onValueChange = {
                    onValueChange(it)
                    validateInput(it) // Trigger validation
                },
                label = { Text(label) },
                singleLine = true,
                isError = isError,
                keyboardOptions = keyboardOptions,
                trailingIcon = {
                    if (isError) {
                        Icon(Icons.Filled.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            if (isError) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
        }
    }

    @Composable
    fun ProfileImageWithPicker(
        profileImageUri: Uri?,
        onImageSelected: (Uri?) -> Unit
    ) {
        val imagePicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { selectedUri ->
                onImageSelected(selectedUri)
            }
        )

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

            IconButton(
                onClick = {
                    imagePicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Change Profile Photo",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    @Composable
    fun ProfileScreen() {
        // States for user input
        val name = rememberSaveable { mutableStateOf("") }
        val email = rememberSaveable { mutableStateOf("") }
        val profileImageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
        val numberColor = rememberSaveable { mutableStateOf("") }

        // Validation states
        val nameError = rememberSaveable { mutableStateOf(false) }
        val emailError = rememberSaveable { mutableStateOf(false) }
        val numberColorError = rememberSaveable { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MasterAnd",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            ProfileImageWithPicker(
                profileImageUri = profileImageUri.value,
                onImageSelected = { profileImageUri.value = it }
            )

            OutlinedTextFieldWithError(
                value = name.value,
                onValueChange = { name.value = it },
                label = "Name",
                isError = nameError.value,
                errorMessage = "Name cannot be empty",
                validateInput = { input ->
                    nameError.value = input.isEmpty()
                    !nameError.value
                }
            )

            OutlinedTextFieldWithError(
                value = email.value,
                onValueChange = { email.value = it },
                label = "Email",
                isError = emailError.value,
                errorMessage = "Invalid email format",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                validateInput = { input ->
                    emailError.value = !android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()
                    !emailError.value
                }
            )

            OutlinedTextFieldWithError(
                value = numberColor.value,
                onValueChange = { numberColor.value = it },
                label = "Number color (5-10)",
                isError = numberColorError.value,
                errorMessage = "Must be a number between 5 and 10",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                validateInput = { input ->
                    val number = input.toIntOrNull()
                    numberColorError.value = !(number != null && number in 5..10)
                    !numberColorError.value
                }
            )

            Button(
                onClick = {
                    // You can add validation checks before proceeding
                    if (!nameError.value && !emailError.value && !numberColorError.value) {
                        // Proceed to next activity
                        val intent = Intent(this@MainActivity, ProfileView::class.java).apply {
                            putExtra("NAME", name.value)
                            putExtra("EMAIL", email.value)
                            putExtra("COLOR", numberColor.value)
                            putExtra("PROFILE_URI", profileImageUri.value?.toString()) // Use URI as String
                        }
                        startActivity(intent)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Next")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ProfileScreenPreview() {
        ProfileScreen()
    }
}
