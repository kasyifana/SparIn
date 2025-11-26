package com.example.sparin.presentation.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Onboarding Screen (Placeholder)
 */
@Composable
fun OnboardingScreen(
    onNavigateToSignIn: () -> Unit
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
                text = "Welcome to SparIN",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "Find your sports partner easily!",
                style = MaterialTheme.typography.bodyLarge
            )
            Button(onClick = onNavigateToSignIn) {
                Text("Get Started")
            }
        }
    }
}
