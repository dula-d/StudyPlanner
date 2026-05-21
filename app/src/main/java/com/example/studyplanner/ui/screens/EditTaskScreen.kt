package com.example.studyplanner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.studyplanner.data.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    task: Task?,
    onSaveChanges: (Task) -> Unit,
    onBackToTaskList: () -> Unit
) {
    if (task == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No task selected.",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onBackToTaskList,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "Back to Task List",
                    fontWeight = FontWeight.Bold
                )
            }
        }
        return
    }

    var title by remember(task.id) { mutableStateOf(task.title) }
    var module by remember(task.id) { mutableStateOf(task.module) }
    var dueDate by remember(task.id) { mutableStateOf(task.dueDate) }
    var priority by remember(task.id) { mutableStateOf(task.priority) }
    var notes by remember(task.id) { mutableStateOf(task.notes) }
    var errorMessage by remember(task.id) { mutableStateOf("") }

    var priorityExpanded by remember { mutableStateOf(false) }
    val priorityOptions = listOf("Low", "Medium", "High")

    var showConfirmDialog by remember { mutableStateOf(false) }
    var taskToSave by remember { mutableStateOf<Task?>(null) }

    if (showConfirmDialog && taskToSave != null) {
        AlertDialog(
            onDismissRequest = {
                showConfirmDialog = false
                taskToSave = null
            },
            title = {
                Text(text = "Save Changes?")
            },
            text = {
                Text(text = "Are you sure you want to update this task?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        taskToSave?.let { updatedTask ->
                            onSaveChanges(updatedTask)
                        }

                        showConfirmDialog = false
                        taskToSave = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        taskToSave = null
                    }
                ) {
                    Text(text = "Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 56.dp,
                bottom = 100.dp
            ),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Edit Task",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Update the selected study task details.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
        )

        Spacer(modifier = Modifier.height(22.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Task Details",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        errorMessage = ""
                    },
                    label = { Text("Task Title") },
                    placeholder = { Text("Example: Presentation Demo") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = module,
                    onValueChange = {
                        module = it
                        errorMessage = ""
                    },
                    label = { Text("Module") },
                    placeholder = { Text("Example: Application Development") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = dueDate,
                    onValueChange = {
                        dueDate = it
                        errorMessage = ""
                    },
                    label = { Text("Due Date") },
                    placeholder = { Text("Example: 22/05/2026") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                ExposedDropdownMenuBox(
                    expanded = priorityExpanded,
                    onExpandedChange = {
                        priorityExpanded = !priorityExpanded
                    }
                ) {
                    OutlinedTextField(
                        value = priority,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Priority") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = priorityExpanded
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(16.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = priorityExpanded,
                        onDismissRequest = {
                            priorityExpanded = false
                        }
                    ) {
                        priorityOptions.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = option)
                                },
                                onClick = {
                                    priority = option
                                    priorityExpanded = false
                                    errorMessage = ""
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = {
                        notes = it
                    },
                    label = { Text("Notes") },
                    placeholder = { Text("Add useful details for this task") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = RoundedCornerShape(16.dp)
                )

                if (errorMessage.isNotBlank()) {
                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.errorContainer
                    ) {
                        Text(
                            text = errorMessage,
                            modifier = Modifier.padding(14.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        when {
                            title.isBlank() -> {
                                errorMessage = "Task title is required."
                            }

                            module.isBlank() -> {
                                errorMessage = "Module name is required."
                            }

                            dueDate.isBlank() -> {
                                errorMessage = "Due date is required."
                            }

                            !isValidDateFormat(date = dueDate) -> {
                                errorMessage = "Please use a valid date format, for example 22/05/2026."
                            }

                            isPastDate(date = dueDate) -> {
                                errorMessage = "Due date cannot be in the past."
                            }

                            priority !in priorityOptions -> {
                                errorMessage = "Priority must be Low, Medium, or High."
                            }

                            else -> {
                                errorMessage = ""

                                taskToSave = task.copy(
                                    title = title.trim(),
                                    module = module.trim(),
                                    dueDate = dueDate.trim(),
                                    priority = priority,
                                    notes = notes.trim()
                                )

                                showConfirmDialog = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Save Changes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        OutlinedButton(
            onClick = onBackToTaskList,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Back to Task List",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}