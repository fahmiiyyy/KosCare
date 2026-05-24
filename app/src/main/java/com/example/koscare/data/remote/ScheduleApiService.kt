package com.example.koscare.data.remote

import com.example.koscare.data.model.Schedule
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface ScheduleApiService {

    @GET("rest/v1/schedules")
    suspend fun getSchedules(

        @Query("select")
        select: String = "*",

        @Query("user_id")
        userId: String

    ): Response<List<Schedule>>

    @POST("rest/v1/schedules")
    suspend fun addSchedule(

        @Body schedule: Schedule,

        @Header("Prefer")
        prefer: String = "return=representation"

    ): Response<List<Schedule>>

    @DELETE("rest/v1/schedules")
    suspend fun deleteSchedule(

        @Query("id")
        id: String

    ): Response<Unit>

    @PATCH("rest/v1/schedules")
    suspend fun updateScheduleStatus(

        @Query("id")
        id: String,

        @Body body: Map<String, Boolean>

    ): Response<Unit>
}