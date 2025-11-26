package com.example.sparin.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Sign In Screen (Placeholder)
 */
@Composable
fun SignInScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToPersonalization: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Sign In to SparIN",
                style = MaterialTheme.typography.headlineLarge
            )
            Button(onClick = onNavigateToHome) {
                Text("Sign In with Google (Existing User)")
            }
            Button(onClick = onNavigateToPersonalization) {
                Text("Sign In with Google (New User)")
            }
        }
    }
}
