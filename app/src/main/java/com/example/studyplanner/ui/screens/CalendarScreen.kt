package com.example.studyplanner.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.studyplanner.data.Task
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(
    tasks: List<Task>,
    holidays: List<String>,
    apiStatusMessage: String,
    onRefreshHolidays: () -> Unit,
    onBackToDashboard: () -> Unit
) {
    var currentMonth by remember {
        mutableStateOf(YearMonth.now())
    }

    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    val holidayDates = holidays.mapNotNull { holiday ->
        parseHolidayDate(holiday)
    }

    val selectedDateTasks = tasks.filter { task ->
        parseTaskDate(task.dueDate) == selectedDate
    }

    val selectedDateHoliday = holidays.firstOrNull { holiday ->
        parseHolidayDate(holiday) == selectedDate
    }

    val selectedDateIsWeekend =
        selectedDate.dayOfWeek == DayOfWeek.SATURDAY ||
                selectedDate.dayOfWeek == DayOfWeek.SUNDAY

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 34.dp,
            bottom = 100.dp
        ),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Text(
                text = "Calendar",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    CalendarMonthHeader(
                        currentMonth = currentMonth,
                        onPreviousMonth = {
                            currentMonth = currentMonth.minusMonths(1)
                        },
                        onNextMonth = {
                            currentMonth = currentMonth.plusMonths(1)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CalendarWeekHeader()

                    Spacer(modifier = Modifier.height(8.dp))

                    CalendarMonthGrid(
                        currentMonth = currentMonth,
                        selectedDate = selectedDate,
                        tasks = tasks,
                        holidayDates = holidayDates,
                        onDateSelected = { date ->
                            selectedDate = date
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CalendarLegend()
                }
            }
        }

        item {
            SelectedDateCard(
                selectedDate = selectedDate,
                selectedDateTasks = selectedDateTasks,
                selectedDateHoliday = selectedDateHoliday,
                selectedDateIsWeekend = selectedDateIsWeekend
            )
        }

        item {
            PublicHolidayCard(
                holidays = holidays,
                apiStatusMessage = apiStatusMessage,
                onRefreshHolidays = onRefreshHolidays
            )
        }

        item {
            OutlinedButton(
                onClick = onBackToDashboard,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp)
            ) {
                Text(
                    text = "Back to Dashboard",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CalendarMonthHeader(
    currentMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilledTonalButton(
            onClick = onPreviousMonth,
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("‹")
        }

        Text(
            text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        FilledTonalButton(
            onClick = onNextMonth,
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("›")
        }
    }
}

@Composable
fun CalendarWeekHeader() {
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach { day ->
            val isWeekend = day == "Sat" || day == "Sun"

            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = if (isWeekend) {
                    Color(0xFFD32F2F)
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                }
            )
        }
    }
}

@Composable
fun CalendarMonthGrid(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    tasks: List<Task>,
    holidayDates: List<LocalDate>,
    onDateSelected: (LocalDate) -> Unit
) {
    val firstDayOfMonth = currentMonth.atDay(1)
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOffset = firstDayOfMonth.dayOfWeek.value - 1

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (week in 0 until 6) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                for (dayIndex in 0 until 7) {
                    val cellIndex = week * 7 + dayIndex
                    val dayNumber = cellIndex - firstDayOffset + 1

                    if (dayNumber in 1..daysInMonth) {
                        val date = currentMonth.atDay(dayNumber)

                        val hasTask = tasks.any { task ->
                            parseTaskDate(task.dueDate) == date
                        }

                        val isWeekend =
                            date.dayOfWeek == DayOfWeek.SATURDAY ||
                                    date.dayOfWeek == DayOfWeek.SUNDAY

                        val isHoliday = holidayDates.contains(date)

                        CalendarDayCell(
                            date = date,
                            isSelected = date == selectedDate,
                            isToday = date == LocalDate.now(),
                            hasTask = hasTask,
                            isWeekend = isWeekend,
                            isHoliday = isHoliday,
                            onClick = {
                                onDateSelected(date)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarDayCell(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    hasTask: Boolean,
    isWeekend: Boolean,
    isHoliday: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isOffDay = isWeekend || isHoliday
    val cellShape = RoundedCornerShape(16.dp)

    val backgroundColor = when {
        isSelected -> Color(0xFF314D89)
        isHoliday -> Color(0xFFFFE2E2)
        isWeekend -> Color(0xFFFFF3F3)
        isToday -> Color(0xFFE4EBFF)
        else -> Color.Transparent
    }

    val textColor = when {
        isSelected -> Color.White
        isOffDay -> Color(0xFFD32F2F)
        else -> MaterialTheme.colorScheme.onSurface
    }

    val borderModifier = if (isHoliday && !isSelected) {
        Modifier.border(
            border = BorderStroke(1.dp, Color(0xFFD32F2F)),
            shape = cellShape
        )
    } else {
        Modifier
    }

    Surface(
        modifier = modifier
            .height(50.dp)
            .then(borderModifier)
            .clickable {
                onClick()
            },
        shape = cellShape,
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected || isToday || isOffDay) {
                    FontWeight.Bold
                } else {
                    FontWeight.Normal
                },
                color = textColor
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (hasTask) {
                    Surface(
                        modifier = Modifier.size(5.dp),
                        shape = CircleShape,
                        color = if (isSelected) Color.White else Color(0xFF314D89)
                    ) {}
                }

                if (isHoliday) {
                    Surface(
                        modifier = Modifier.size(5.dp),
                        shape = CircleShape,
                        color = if (isSelected) Color.White else Color(0xFFD32F2F)
                    ) {}
                }
            }
        }
    }
}

@Composable
fun CalendarLegend() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LegendItem(
            label = "Task",
            dotColor = Color(0xFF314D89),
            modifier = Modifier.weight(1f)
        )

        LegendItem(
            label = "Weekend",
            dotColor = Color(0xFFFFB3B3),
            textColor = Color(0xFFD32F2F),
            modifier = Modifier.weight(1f)
        )

        LegendItem(
            label = "Holiday",
            dotColor = Color(0xFFD32F2F),
            textColor = Color(0xFFD32F2F),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun LegendItem(
    label: String,
    dotColor: Color,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(8.dp),
            shape = CircleShape,
            color = dotColor
        ) {}

        Spacer(modifier = Modifier.size(6.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = textColor,
            maxLines = 1
        )
    }
}

@Composable
fun SelectedDateCard(
    selectedDate: LocalDate,
    selectedDateTasks: List<Task>,
    selectedDateHoliday: String?,
    selectedDateIsWeekend: Boolean
) {
    val isOffDay = selectedDateIsWeekend || selectedDateHoliday != null

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "Selected Date",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = selectedDate.format(
                    DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (isOffDay) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFFF0F0)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Text(
                            text = "Off day",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD32F2F)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        if (selectedDateIsWeekend) {
                            Text(
                                text = "Weekend",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFD32F2F)
                            )
                        }

                        if (selectedDateHoliday != null) {
                            Text(
                                text = "Public holiday: $selectedDateHoliday",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFD32F2F),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (selectedDateTasks.isEmpty()) {
                Text(
                    text = "No coursework tasks due on this date.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
            } else {
                selectedDateTasks.forEach { task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Text(
                                text = task.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Due date: ${task.dueDate}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PublicHolidayCard(
    holidays: List<String>,
    apiStatusMessage: String,
    onRefreshHolidays: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "Public Holiday API",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = apiStatusMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Loaded holidays: ${holidays.size}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onRefreshHolidays,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF314D89)
                )
            ) {
                Text(
                    text = "Refresh Public Holidays",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun parseTaskDate(dateText: String): LocalDate? {
    val dateFormats = listOf(
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        DateTimeFormatter.ofPattern("d/M/yyyy"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy"),
        DateTimeFormatter.ofPattern("d-M-yyyy")
    )

    for (format in dateFormats) {
        try {
            return LocalDate.parse(dateText.trim(), format)
        } catch (exception: Exception) {
            // Try next date format
        }
    }

    return null
}

fun parseHolidayDate(holidayText: String): LocalDate? {
    val possibleDateTexts = Regex(
        """\d{4}-\d{2}-\d{2}|\d{2}/\d{2}/\d{4}|\d{1,2}/\d{1,2}/\d{4}|\d{2}-\d{2}-\d{4}|\d{1,2}-\d{1,2}-\d{4}"""
    )
        .findAll(holidayText)
        .map { it.value }
        .toList()

    for (dateText in possibleDateTexts) {
        val parsedDate = parseTaskDate(dateText)
        if (parsedDate != null) {
            return parsedDate
        }
    }

    return null
}