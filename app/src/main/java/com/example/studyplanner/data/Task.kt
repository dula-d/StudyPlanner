package com.example.studyplanner.data

data class Task(
    val id: Int,
    val title: String,
    val module: String,
    val dueDate: String,
    val priority: String,
    val notes: String,
    val status: String = "Pending",
    val isCompleted: Boolean = false
)