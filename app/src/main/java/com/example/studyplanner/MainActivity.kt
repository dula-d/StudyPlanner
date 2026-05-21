package com.example.studyplanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studyplanner.data.Task
import com.example.studyplanner.network.rememberNetworkStatus
import com.example.studyplanner.ui.screens.AboutScreen
import com.example.studyplanner.ui.screens.AddTaskScreen
import com.example.studyplanner.ui.screens.CalendarScreen
import com.example.studyplanner.ui.screens.DashboardScreen
import com.example.studyplanner.ui.screens.EditTaskScreen
import com.example.studyplanner.ui.screens.SettingsScreen
import com.example.studyplanner.ui.screens.StudyTipScreen
import com.example.studyplanner.ui.screens.TaskListScreen
import com.example.studyplanner.ui.theme.StudyPlannerTheme
import com.example.studyplanner.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        NotificationHelper.createNotificationChannel(this)

        setContent {
            var darkModeEnabled by remember { mutableStateOf(false) }

            StudyPlannerTheme(darkTheme = darkModeEnabled) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    StudyPlannerApp(
                        darkModeEnabled = darkModeEnabled,
                        onDarkModeChange = { darkModeEnabled = it }
                    )
                }
            }
        }
    }
}

@Composable
fun StudyPlannerApp(
    darkModeEnabled: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {
    var currentScreen by remember { mutableStateOf("dashboard") }
    var selectedTask by remember { mutableStateOf<Task?>(null) }

    var notificationsEnabled by remember { mutableStateOf(false) }
    var studyRemindersEnabled by remember { mutableStateOf(false) }

    val taskViewModel: TaskViewModel = viewModel()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val isOnline = rememberNetworkStatus()

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            notificationsEnabled = true

            NotificationHelper.showNotification(
                context = context,
                title = "Study Planner",
                message = "Notifications are now enabled."
            )
        } else {
            notificationsEnabled = false

            Toast.makeText(
                context,
                "Notification permission denied.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun requestNotificationPermissionAndShowMessage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionStatus = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )

            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                notificationsEnabled = true

                NotificationHelper.showNotification(
                    context = context,
                    title = "Study Planner",
                    message = "Notifications are now enabled."
                )
            } else {
                notificationPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        } else {
            notificationsEnabled = true

            NotificationHelper.showNotification(
                context = context,
                title = "Study Planner",
                message = "Notifications are now enabled."
            )
        }
    }

    fun handleNotificationsChange(enabled: Boolean) {
        if (enabled) {
            requestNotificationPermissionAndShowMessage()
        } else {
            notificationsEnabled = false
            studyRemindersEnabled = false

            Toast.makeText(
                context,
                "Notifications disabled.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun handleStudyRemindersChange(enabled: Boolean) {
        if (enabled) {
            if (!notificationsEnabled) {
                Toast.makeText(
                    context,
                    "Please enable Notifications first.",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            studyRemindersEnabled = true

            val pendingTasks = taskViewModel.getPendingTasks()

            val reminderMessage = if (pendingTasks > 0) {
                "You have $pendingTasks pending coursework task(s). Check your task list."
            } else {
                "You have no pending coursework tasks."
            }

            NotificationHelper.showNotification(
                context = context,
                title = "Study Reminder",
                message = reminderMessage
            )
        } else {
            studyRemindersEnabled = false

            Toast.makeText(
                context,
                "Study reminders disabled.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerMenu(
                currentScreen = currentScreen,
                darkModeEnabled = darkModeEnabled,
                onDarkModeChange = onDarkModeChange,
                onCloseDrawer = {
                    scope.launch {
                        drawerState.close()
                    }
                },
                onNavigate = { screen ->
                    currentScreen = screen
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when (currentScreen) {
                "dashboard" -> {
                    DashboardScreen(
                        totalTasks = taskViewModel.getTotalTasks(),
                        completedTasks = taskViewModel.getCompletedTasks(),
                        pendingTasks = taskViewModel.getPendingTasks(),
                        overdueTasks = taskViewModel.getOverdueTasks(),
                        isOnline = isOnline,
                        studyTip = taskViewModel.studyTip.value,
                        onMenuClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                        onTaskListClick = {
                            currentScreen = "taskList"
                        },
                        onAddTaskClick = {
                            currentScreen = "addTask"
                        },
                        onCalendarClick = {
                            currentScreen = "calendar"
                        },
                        onStudyTipClick = {
                            currentScreen = "studyTip"
                        },
                        onSettingsClick = {
                            currentScreen = "settings"
                        },
                        onAboutClick = {
                            currentScreen = "about"
                        }
                    )
                }

                "taskList" -> {
                    TaskListScreen(
                        tasks = taskViewModel.tasks,
                        onBackToDashboard = {
                            currentScreen = "dashboard"
                        },
                        onAddTaskClick = {
                            currentScreen = "addTask"
                        },
                        onEditTaskClick = { task ->
                            selectedTask = task
                            currentScreen = "editTask"
                        },
                        onToggleCompleteClick = { taskId ->
                            taskViewModel.toggleComplete(taskId)
                        },
                        onDeleteTaskClick = { taskId ->
                            taskViewModel.deleteTask(taskId)
                        }
                    )
                }

                "addTask" -> {
                    AddTaskScreen(
                        onSaveTask = { title, module, dueDate, priority, notes ->
                            taskViewModel.addTask(
                                title = title,
                                module = module,
                                dueDate = dueDate,
                                priority = priority,
                                notes = notes
                            )
                            currentScreen = "taskList"
                        },
                        onBackToDashboard = {
                            currentScreen = "dashboard"
                        }
                    )
                }

                "editTask" -> {
                    EditTaskScreen(
                        task = selectedTask,
                        onSaveChanges = { updatedTask ->
                            taskViewModel.updateTask(updatedTask)
                            selectedTask = null
                            currentScreen = "taskList"
                        },
                        onBackToTaskList = {
                            selectedTask = null
                            currentScreen = "taskList"
                        }
                    )
                }

                "calendar" -> {
                    CalendarScreen(
                        tasks = taskViewModel.tasks,
                        holidays = taskViewModel.holidayList,
                        apiStatusMessage = taskViewModel.holidayApiStatus.value,
                        onRefreshHolidays = {
                            taskViewModel.loadHolidaysFromApi()
                        },
                        onBackToDashboard = {
                            currentScreen = "dashboard"
                        }
                    )
                }

                "studyTip" -> {
                    StudyTipScreen(
                        studyTip = taskViewModel.studyTip.value,
                        apiStatusMessage = taskViewModel.studyTipApiStatus.value,
                        onRefreshStudyTip = {
                            taskViewModel.loadStudyTipFromApi()
                        },
                        onBackToDashboard = {
                            currentScreen = "dashboard"
                        }
                    )
                }

                "settings" -> {
                    SettingsScreen(
                        darkModeEnabled = darkModeEnabled,
                        onDarkModeChange = onDarkModeChange,
                        notificationsEnabled = notificationsEnabled,
                        onNotificationsChange = { enabled ->
                            handleNotificationsChange(enabled)
                        },
                        studyRemindersEnabled = studyRemindersEnabled,
                        onStudyRemindersChange = { enabled ->
                            handleStudyRemindersChange(enabled)
                        },
                        onBackToDashboard = {
                            currentScreen = "dashboard"
                        }
                    )
                }

                "about" -> {
                    AboutScreen(
                        onBackToDashboard = {
                            currentScreen = "dashboard"
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AppDrawerMenu(
    currentScreen: String,
    darkModeEnabled: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onCloseDrawer: () -> Unit,
    onNavigate: (String) -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(320.dp),
        drawerContainerColor = MaterialTheme.colorScheme.background,
        drawerContentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp)
        ) {
            IconButton(
                onClick = onCloseDrawer
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close menu"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Study Planner",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Main Navigation Menu",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(28.dp))

            NavigationDrawerItem(
                label = { Text("Dashboard") },
                selected = currentScreen == "dashboard",
                onClick = {
                    onNavigate("dashboard")
                }
            )

            NavigationDrawerItem(
                label = { Text("Task List") },
                selected = currentScreen == "taskList",
                onClick = {
                    onNavigate("taskList")
                }
            )

            NavigationDrawerItem(
                label = { Text("Add Task") },
                selected = currentScreen == "addTask",
                onClick = {
                    onNavigate("addTask")
                }
            )

            NavigationDrawerItem(
                label = { Text("Calendar") },
                selected = currentScreen == "calendar",
                onClick = {
                    onNavigate("calendar")
                }
            )

            NavigationDrawerItem(
                label = { Text("Study Tip") },
                selected = currentScreen == "studyTip",
                onClick = {
                    onNavigate("studyTip")
                }
            )

            NavigationDrawerItem(
                label = { Text("Settings") },
                selected = currentScreen == "settings",
                onClick = {
                    onNavigate("settings")
                }
            )

            NavigationDrawerItem(
                label = { Text("About") },
                selected = currentScreen == "about",
                onClick = {
                    onNavigate("about")
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            NavigationDrawerItem(
                label = {
                    Text("Dark Mode")
                },
                selected = false,
                badge = {
                    Switch(
                        checked = darkModeEnabled,
                        onCheckedChange = onDarkModeChange
                    )
                },
                onClick = {
                    onDarkModeChange(!darkModeEnabled)
                }
            )
        }
    }
}