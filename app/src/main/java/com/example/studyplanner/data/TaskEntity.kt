package com.example.studyplanner.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val moduleName: String,
    val dueDate: String,
    val priority: String,
    val notes: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)