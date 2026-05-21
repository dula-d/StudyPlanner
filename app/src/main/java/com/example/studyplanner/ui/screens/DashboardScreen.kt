package com.example.studyplanner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen(
    totalTasks: Int,
    completedTasks: Int,
    pendingTasks: Int,
    overdueTasks: Int,
    isOnline: Boolean,
    studyTip: String,
    onMenuClick: () -> Unit,
    onTaskListClick: () -> Unit,
    onAddTaskClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onStudyTipClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    var showSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val colorScheme = MaterialTheme.colorScheme

    val blue = Color(0xFF2F6FED)
    val green = Color(0xFF18A66A)
    val orange = Color(0xFFE1842B)
    val red = Color(0xFFD94D4D)
    val purple = Color(0xFF7357D6)
    val teal = Color(0xFF118C8C)
    val slate = Color(0xFF64748B)

    val quickActions = listOf(
        DashboardAction(Icons.Default.List, "View Tasks", "See saved tasks", blue, onTaskListClick),
        DashboardAction(Icons.Default.Add, "Add Task", "Create deadline", green, onAddTaskClick),
        DashboardAction(Icons.Default.DateRange, "Calendar", "View holidays", purple, onCalendarClick),
        DashboardAction(Icons.Default.Star, "Study Tip", "Get motivation", orange, onStudyTipClick),
        DashboardAction(Icons.Default.Settings, "Settings", "Preferences", slate, onSettingsClick),
        DashboardAction(Icons.Default.Info, "About", "App info", teal, onAboutClick)
    )

    val filteredActions = quickActions.filter { action ->
        action.title.contains(searchQuery, ignoreCase = true) ||
                action.subtitle.contains(searchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 18.dp,
            bottom = 170.dp
        ),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            DashboardTopBar(
                showSearch = showSearch,
                onMenuClick = onMenuClick,
                onSearchClick = {
                    showSearch = !showSearch
                    if (!showSearch) {
                        searchQuery = ""
                    }
                },
                onSettingsClick = onSettingsClick
            )
        }

        if (showSearch) {
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Search app features") },
                    placeholder = { Text("Example: task, calendar, study tip") },
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }

        if (!isOnline) {
            item {
                InternetStatusCard(isOnline = isOnline)
            }
        }

        item {
            ProgressSummaryCard(
                totalTasks = totalTasks,
                completedTasks = completedTasks
            )
        }

        item {
            TodayStudyTipCard(
                studyTip = studyTip,
                onStudyTipClick = onStudyTipClick
            )
        }

        item {
            TaskOverviewCard(
                totalTasks = totalTasks,
                completedTasks = completedTasks,
                pendingTasks = pendingTasks,
                overdueTasks = overdueTasks,
                blue = blue,
                green = green,
                orange = orange,
                red = red
            )
        }

        item {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = colorScheme.onBackground,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        item {
            if (filteredActions.isEmpty()) {
                EmptySearchCard()
            } else {
                QuickActionsListCard(actions = filteredActions)
            }
        }
    }
}

@Composable
fun DashboardTopBar(
    showSearch: Boolean,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleIconButton(
            icon = Icons.Default.Menu,
            contentDescription = "Open menu",
            onClick = onMenuClick
        )

        Spacer(modifier = Modifier.weight(1f))

        CircleIconButton(
            icon = if (showSearch) Icons.Default.Close else Icons.Default.Search,
            contentDescription = if (showSearch) "Close search" else "Search",
            onClick = onSearchClick
        )

        Spacer(modifier = Modifier.width(12.dp))

        CircleIconButton(
            icon = Icons.Default.Settings,
            contentDescription = "Settings",
            onClick = onSettingsClick
        )
    }
}

@Composable
fun CircleIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(27.dp)
            )
        }
    }
}

@Composable
fun ProgressSummaryCard(
    totalTasks: Int,
    completedTasks: Int
) {
    val colorScheme = MaterialTheme.colorScheme

    val safeTotal = if (totalTasks <= 0) 0 else totalTasks
    val safeCompleted = completedTasks.coerceIn(0, safeTotal)
    val progressFraction = if (safeTotal == 0) 0f else safeCompleted.toFloat() / safeTotal.toFloat()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(22.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = colorScheme.surfaceVariant,
                    shadowElevation = 1.dp
                ) {
                    Box(
                        modifier = Modifier.size(64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$safeCompleted/$safeTotal",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.width(18.dp))

                Column {
                    Text(
                        text = "Today’s Progress",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "$safeCompleted of $safeTotal tasks completed.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(9.dp)
                    .background(
                        color = colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progressFraction)
                        .height(9.dp)
                        .background(
                            color = colorScheme.primary,
                            shape = RoundedCornerShape(20.dp)
                        )
                )
            }
        }
    }
}

@Composable
fun InternetStatusCard(
    isOnline: Boolean
) {
    val colorScheme = MaterialTheme.colorScheme

    val backgroundColor = if (isOnline) {
        colorScheme.secondaryContainer
    } else {
        colorScheme.errorContainer
    }

    val textColor = if (isOnline) {
        colorScheme.onSecondaryContainer
    } else {
        colorScheme.onErrorContainer
    }

    val statusText = if (isOnline) "Online" else "No internet connection"

    val descriptionText = if (isOnline) {
        "Public holidays and study tips can be loaded."
    } else {
        "Online features are currently unavailable."
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = statusText,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = descriptionText,
                style = MaterialTheme.typography.bodySmall,
                color = textColor.copy(alpha = 0.85f)
            )
        }
    }
}

@Composable
fun TodayStudyTipCard(
    studyTip: String,
    onStudyTipClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    val displayTip = if (studyTip.isBlank()) {
        "Stay focused by completing one important study task at a time."
    } else {
        studyTip
    }

    Card(
        onClick = onStudyTipClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            Column(
                modifier = Modifier.padding(
                    start = 22.dp,
                    end = 22.dp,
                    top = 22.dp,
                    bottom = 18.dp
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = colorScheme.primaryContainer
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Study tip",
                            tint = colorScheme.primary,
                            modifier = Modifier
                                .padding(10.dp)
                                .size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Study Tip",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "“$displayTip”",
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Medium,
                    color = colorScheme.onSurface,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorScheme.primary)
                    .clickable(onClick = onStudyTipClick)
                    .padding(horizontal = 22.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = colorScheme.onPrimary.copy(alpha = 0.16f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Open study tip",
                        tint = colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Open motivation",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = colorScheme.onPrimary
                    )

                    Text(
                        text = "Read a study tip for focus",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onPrimary.copy(alpha = 0.85f)
                    )
                }

                Text(
                    text = "›",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun TaskOverviewCard(
    totalTasks: Int,
    completedTasks: Int,
    pendingTasks: Int,
    overdueTasks: Int,
    blue: Color,
    green: Color,
    orange: Color,
    red: Color
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Task Overview",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Live status of study tasks and deadlines.",
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TaskMetricCell(
                    number = totalTasks.toString(),
                    label = "Total",
                    icon = Icons.Default.List,
                    color = blue,
                    modifier = Modifier.weight(1f)
                )

                TaskMetricCell(
                    number = completedTasks.toString(),
                    label = "Done",
                    icon = Icons.Default.Check,
                    color = green,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TaskMetricCell(
                    number = pendingTasks.toString(),
                    label = "Pending",
                    icon = Icons.Default.DateRange,
                    color = orange,
                    modifier = Modifier.weight(1f)
                )

                TaskMetricCell(
                    number = overdueTasks.toString(),
                    label = "Overdue",
                    icon = Icons.Default.Warning,
                    color = red,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun TaskMetricCell(
    number: String,
    label: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        color = color.copy(alpha = 0.12f)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = colorScheme.surface.copy(alpha = 0.85f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = color,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(21.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = number,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = colorScheme.onSurface,
                    maxLines = 1
                )

                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun QuickActionsListCard(
    actions: List<DashboardAction>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            actions.forEach { action ->
                QuickActionRow(
                    icon = action.icon,
                    title = action.title,
                    subtitle = action.subtitle,
                    color = action.color,
                    onClick = action.onClick
                )
            }
        }
    }
}

@Composable
fun QuickActionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = colorScheme.onSurface,
            modifier = Modifier.size(30.dp)
        )

        Spacer(modifier = Modifier.width(20.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(
            text = "›",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun EmptySearchCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = "No matching feature found.",
            modifier = Modifier.padding(20.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

data class DashboardAction(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val color: Color,
    val onClick: () -> Unit
)