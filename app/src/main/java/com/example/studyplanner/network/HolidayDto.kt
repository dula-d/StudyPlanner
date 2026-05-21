package com.example.studyplanner.network

data class HolidayDto(
    val title: String,
    val date: String,
    val notes: String? = null,
    val bunting: Boolean? = null
)