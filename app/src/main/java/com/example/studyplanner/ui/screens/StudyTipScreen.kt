package com.example.studyplanner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun StudyTipScreen(
    studyTip: String,
    apiStatusMessage: String,
    onRefreshStudyTip: () -> Unit,
    onBackToDashboard: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 18.dp,
                bottom = 120.dp
            ),
        verticalArrangement = Arrangement.Top
    ) {
        StudyTipTopBar(
            onBackToDashboard = onBackToDashboard,
            onRefreshStudyTip = onRefreshStudyTip
        )

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "Study Tip",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Daily study motivation loaded from an online quote API.",
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(18.dp))

        ApiStatusCard(
            apiStatusMessage = apiStatusMessage
        )

        Spacer(modifier = Modifier.height(22.dp))

        MotivationCard(
            studyTip = studyTip
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRefreshStudyTip,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh study tip",
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Refresh Online Study Tip",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedButton(
            onClick = onBackToDashboard,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = colorScheme.primary
            )
        ) {
            Text(
                text = "Back to Dashboard",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun StudyTipTopBar(
    onBackToDashboard: () -> Unit,
    onRefreshStudyTip: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = colorScheme.surface,
            shadowElevation = 4.dp
        ) {
            IconButton(
                onClick = onBackToDashboard,
                modifier = Modifier.size(54.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back to dashboard",
                    tint = colorScheme.primary,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Surface(
            shape = CircleShape,
            color = colorScheme.surface,
            shadowElevation = 4.dp
        ) {
            IconButton(
                onClick = onRefreshStudyTip,
                modifier = Modifier.size(54.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh study tip",
                    tint = colorScheme.primary,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
fun ApiStatusCard(
    apiStatusMessage: String
) {
    val colorScheme = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "API status",
                tint = colorScheme.onPrimaryContainer,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = apiStatusMessage,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun MotivationCard(
    studyTip: String
) {
    val colorScheme = MaterialTheme.colorScheme

    val displayTip = if (studyTip.isBlank()) {
        "Stay focused by completing one important study task at a time."
    } else {
        studyTip
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Study motivation",
                    tint = colorScheme.primary,
                    modifier = Modifier
                        .padding(14.dp)
                        .size(30.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Online Study Motivation",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = colorScheme.surfaceVariant
            ) {
                Text(
                    text = "“$displayTip”",
                    modifier = Modifier.padding(20.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Medium,
                    color = colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}