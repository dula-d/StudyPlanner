package com.example.studyplanner.network

import retrofit2.http.GET

interface HolidayApiService {

    @GET("bank-holidays.json")
    suspend fun getBankHolidays(): Map<String, HolidayDivisionDto>
}

data class HolidayDivisionDto(
    val division: String,
    val events: List<HolidayDto>
)