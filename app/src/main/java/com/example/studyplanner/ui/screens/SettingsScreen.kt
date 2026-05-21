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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    darkModeEnabled: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    notificationsEnabled: Boolean,
    onNotificationsChange: (Boolean) -> Unit,
    studyRemindersEnabled: Boolean,
    onStudyRemindersChange: (Boolean) -> Unit,
    onBackToDashboard: () -> Unit
) {
    var publicHolidayUpdatesEnabled by remember { mutableStateOf(true) }
    var studyMotivationEnabled by remember { mutableStateOf(true) }
    var showCompletedTasks by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 32.dp,
                bottom = 110.dp
            ),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Manage display options, reminders and connected services.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(22.dp))

        SettingsSectionCard(
            icon = "🎨",
            title = "Appearance"
        ) {
            SettingsSwitchRow(
                title = "Dark Mode",
                subtitle = "Use a darker display style for the application.",
                checked = darkModeEnabled,
                onCheckedChange = onDarkModeChange
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        SettingsSectionCard(
            icon = "🔔",
            title = "Notifications and Reminders"
        ) {
            SettingsSwitchRow(
                title = "Notifications",
                subtitle = "Allow notifications for important study updates.",
                checked = notificationsEnabled,
                onCheckedChange = onNotificationsChange
            )

            SettingsDivider()

            SettingsSwitchRow(
                title = "Study Reminders",
                subtitle = "Show reminders for pending coursework tasks.",
                checked = studyRemindersEnabled,
                onCheckedChange = onStudyRemindersChange
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        SettingsSectionCard(
            icon = "🌐",
            title = "Connected Services"
        ) {
            SettingsSwitchRow(
                title = "Public Holiday Updates",
                subtitle = "Load UK public holiday data using an online API.",
                checked = publicHolidayUpdatesEnabled,
                onCheckedChange = { publicHolidayUpdatesEnabled = it }
            )

            SettingsDivider()

            SettingsSwitchRow(
                title = "Study Motivation",
                subtitle = "Load study motivation tips using an online API.",
                checked = studyMotivationEnabled,
                onCheckedChange = { studyMotivationEnabled = it }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        SettingsSectionCard(
            icon = "📋",
            title = "Task Display"
        ) {
            SettingsSwitchRow(
                title = "Show Completed Tasks",
                subtitle = "Display completed tasks in the task list screen.",
                checked = showCompletedTasks,
                onCheckedChange = { showCompletedTasks = it }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        SettingsSectionCard(
            icon = "🔒",
            title = "Data and Privacy"
        ) {
            SettingsInfoLine(
                title = "Local Data",
                description = "Study tasks are stored locally on the device using the app database."
            )

            SettingsDivider()

            SettingsInfoLine(
                title = "Internet Access",
                description = "Connected services only use public APIs for holidays and study motivation."
            )

            SettingsDivider()

            SettingsInfoLine(
                title = "Personal Data",
                description = "This prototype does not require account registration or personal login details."
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onBackToDashboard,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
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
fun SettingsSectionCard(
    icon: String,
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text(
                text = "$icon $title",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            content()
        }
    }
}

@Composable
fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun SettingsInfoLine(
    title: String,
    description: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun SettingsDivider() {
    Divider(
        modifier = Modifier.padding(vertical = 14.dp),
        color = MaterialTheme.colorScheme.outlineVariant
    )
}