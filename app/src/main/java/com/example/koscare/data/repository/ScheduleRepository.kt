package com.example.koscare.data.repository

import com.example.koscare.data.model.Schedule
import com.example.koscare.data.remote.RetrofitClient
import com.example.koscare.data.remote.ScheduleApiService

class ScheduleRepository {

    private val apiService =
        RetrofitClient
            .retrofit
            .create(
                ScheduleApiService::class.java
            )

    suspend fun getSchedules(
        userId: String
    ): List<Schedule> {

        val response =
            apiService.getSchedules(
                userId = "eq.$userId"
            )

        return response.body() ?: emptyList()
    }

    suspend fun addSchedule(
        schedule: Schedule
    ) {

        apiService.addSchedule(schedule)
    }

    suspend fun deleteSchedule(
        scheduleId: String
    ) {

        apiService.deleteSchedule(
            id = "eq.$scheduleId"
        )
    }

    suspend fun updateScheduleStatus(
        scheduleId: String,
        status: Boolean
    ) {

        apiService.updateScheduleStatus(

            id = "eq.$scheduleId",

            body = mapOf(
                "status" to status
            )
        )
    }
}