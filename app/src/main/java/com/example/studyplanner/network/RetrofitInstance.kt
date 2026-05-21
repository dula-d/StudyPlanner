package com.example.studyplanner.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .addInterceptor(loggingInterceptor)
        .build()

    private val holidayRetrofit = Retrofit.Builder()
        .baseUrl("https://www.gov.uk/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val quoteRetrofit = Retrofit.Builder()
        .baseUrl("https://zenquotes.io/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val holidayApiService: HolidayApiService =
        holidayRetrofit.create(HolidayApiService::class.java)

    val quoteApiService: QuoteApiService =
        quoteRetrofit.create(QuoteApiService::class.java)
}