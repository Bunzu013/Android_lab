package com.example.lab1

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning

@Composable
fun LoginView(
    onNextClick: (String, String, Int, Uri?) -> Unit
) {
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val profileImageUri = remember { mutableStateOf<Uri?>(null) }
    val numberColor = remember { mutableStateOf(5) }

    val nameError = remember { mutableStateOf(false) }
    val emailError = remember { mutableStateOf(false) }
    val numberColorError = remember { mutableStateOf(false) }

    // Validation function
    val validateFields = {
        nameError.value = name.value.isEmpty()
        emailError.value = !android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
        numberColorError.value = numberColor.value !in 5..10
        !(nameError.value || emailError.value || numberColorError.value)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Image Picker
        ProfileImageWithPicker(
            profileImageUri = profileImageUri.value,
            onImageSelected = { profileImageUri.value = it }
        )

        // Name Field with Validation
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

        // Email Field with Validation
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

        // Number of Colors Field with Validation
        TextField(
            value = numberColor.value.toString(),
            onValueChange = {
                val parsedValue = it.toIntOrNull()
                if (parsedValue != null) {
                    numberColor.value = parsedValue
                    numberColorError.value = parsedValue !in 5..10
                } else {
                    numberColorError.value = true
                }
            },
            label = { Text("Number color (5-10)") },
            isError = numberColorError.value,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        if (numberColorError.value) {
            Text(
                "Must be a number between 5 and 10",
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                if (validateFields()) {
                    onNextClick(name.value, email.value, numberColor.value, profileImageUri.value)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = validateFields()
        ) {
            Text("Next")
        }
        if (!validateFields()) {
            Text(
                "Data cannot be empty or invalid",
                color = MaterialTheme.colorScheme.error
            )
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
                imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
      TextField(
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
