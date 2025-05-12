package com.example.mypharmacy.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Loading indicator overlay that covers the entire screen
 * @param isLoading Whether the loading indicator should be shown
 * @param message Optional message to display below the loading indicator
 */
@Composable
fun LoadingOverlay(
    isLoading: Boolean,
    message: String? = null,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .then(modifier),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator(message = message)
        }
    }
}

/**
 * A circular loading indicator with an optional message
 * @param message Optional message to display below the loading indicator
 */
@Composable
fun LoadingIndicator(
    message: String? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(150.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Utilisation simple de CircularProgressIndicator sans animations complexes
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp
            )

            message?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * A simple circular progress indicator for inline use
 */
@Composable
fun InlineLoading(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = modifier.size(24.dp),
        strokeWidth = 2.dp,
        color = MaterialTheme.colorScheme.primary
    )
}

/**
 * A full page loading indicator for initial data loading
 */
@Composable
fun FullPageLoading(
    message: String = "Chargement en cours...",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Version simplifiée sans animation avancée
            CircularProgressIndicator(
                modifier = Modifier.size(56.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}