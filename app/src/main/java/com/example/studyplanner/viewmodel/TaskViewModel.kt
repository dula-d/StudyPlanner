package com.example.studyplanner.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyplanner.data.AppDatabase
import com.example.studyplanner.data.Task
import com.example.studyplanner.data.TaskEntity
import com.example.studyplanner.data.TaskRepository
import com.example.studyplanner.network.RetrofitInstance
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val taskDao = AppDatabase.getDatabase(application).taskDao()
    private val repository = TaskRepository(taskDao)

    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val tasks = mutableStateListOf<Task>()

    val holidayList = mutableStateListOf<String>()

    var holidayApiStatus = mutableStateOf("Loading online public holidays...")
        private set

    var studyTip = mutableStateOf("Loading online study tip...")
        private set

    var studyTipApiStatus = mutableStateOf("Loading online study tip...")
        private set

    init {
        viewModelScope.launch {
            repository.allTasks.collect { taskEntities ->
                tasks.clear()
                tasks.addAll(
                    taskEntities.map { entity ->
                        entity.toTask()
                    }
                )
            }
        }

        loadHolidaysFromApi()
        loadStudyTipFromApi()
    }

    fun getTotalTasks(): Int {
        return tasks.size
    }

    fun getCompletedTasks(): Int {
        return tasks.count { it.isCompleted }
    }

    fun getPendingTasks(): Int {
        return tasks.count { !it.isCompleted }
    }

    fun getOverdueTasks(): Int {
        val today = LocalDate.now()

        return tasks.count { task ->
            try {
                val taskDate = LocalDate.parse(task.dueDate, dateFormatter)
                !task.isCompleted && taskDate.isBefore(today)
            } catch (e: Exception) {
                false
            }
        }
    }

    fun addTask(
        title: String,
        module: String,
        dueDate: String,
        priority: String,
        notes: String
    ) {
        viewModelScope.launch {
            repository.insertTask(
                TaskEntity(
                    title = title,
                    moduleName = module,
                    dueDate = dueDate,
                    priority = priority,
                    notes = notes,
                    isCompleted = false
                )
            )
        }
    }

    fun updateTask(updatedTask: Task) {
        viewModelScope.launch {
            repository.updateTask(updatedTask.toTaskEntity())
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            repository.deleteTaskById(taskId)
        }
    }

    fun toggleComplete(taskId: Int) {
        val task = tasks.find { it.id == taskId }

        if (task != null) {
            viewModelScope.launch {
                repository.updateTaskCompletion(
                    taskId = taskId,
                    isCompleted = !task.isCompleted
                )
            }
        }
    }

    fun loadHolidaysFromApi() {
        viewModelScope.launch {
            holidayApiStatus.value = "Loading online public holidays..."
            holidayList.clear()

            try {
                val response = RetrofitInstance.holidayApiService.getBankHolidays()

                val englandAndWalesEvents =
                    response["england-and-wales"]?.events ?: emptyList()

                val today = LocalDate.now()

                val upcomingHolidays = englandAndWalesEvents
                    .filter { holiday ->
                        try {
                            val holidayDate = LocalDate.parse(holiday.date)
                            !holidayDate.isBefore(today)
                        } catch (e: Exception) {
                            false
                        }
                    }
                    .take(10)
                    .map { holiday ->
                        "${holiday.title} - ${holiday.date}"
                    }

                if (upcomingHolidays.isNotEmpty()) {
                    holidayList.addAll(upcomingHolidays)
                    holidayApiStatus.value = "Source: Online UK public holidays API"
                } else {
                    holidayApiStatus.value = "Online API loaded, but no upcoming holiday data was found."
                }
            } catch (e: Exception) {
                holidayApiStatus.value = "Internet/API error: ${e.message}"
            }
        }
    }

    fun loadStudyTipFromApi() {
        viewModelScope.launch {
            studyTip.value = "Loading online study tip..."
            studyTipApiStatus.value = "Loading online study tip..."

            try {
                val response = RetrofitInstance.quoteApiService.getRandomQuote()

                if (response.isNotEmpty()) {
                    val quote = response.first()
                    studyTip.value = "\"${quote.q}\" — ${quote.a}"
                    studyTipApiStatus.value = "Source: Online quote API"
                } else {
                    studyTip.value = "No study tip received from the online API."
                    studyTipApiStatus.value = "Online API returned no quote data."
                }
            } catch (e: Exception) {
                studyTip.value = "Internet/API error: ${e.message}"
                studyTipApiStatus.value = "Online API connection failed: ${e.message}"
            }
        }
    }

    private fun TaskEntity.toTask(): Task {
        return Task(
            id = id,
            title = title,
            module = moduleName,
            dueDate = dueDate,
            priority = priority,
            notes = notes,
            status = if (isCompleted) "Completed" else "Pending",
            isCompleted = isCompleted
        )
    }

    private fun Task.toTaskEntity(): TaskEntity {
        return TaskEntity(
            id = id,
            title = title,
            moduleName = module,
            dueDate = dueDate,
            priority = priority,
            notes = notes,
            isCompleted = isCompleted
        )
    }
}