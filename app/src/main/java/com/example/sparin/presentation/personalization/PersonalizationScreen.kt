package com.example.sparin.presentation.personalization

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Personalization Screen (Placeholder)
 */
@Composable
fun PersonalizationScreen(
    onNavigateToHome: () -> Unit
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
                text = "Complete Your Profile",
                style = MaterialTheme.typography.headlineLarge
            )
            Text("Enter your details to get started")
            Button(onClick = onNavigateToHome) {
                Text("Complete Setup")
            }
        }
    }
}
