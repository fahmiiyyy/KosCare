package com.example.koscare.data.repository

import com.example.koscare.data.model.Schedule
import com.example.koscare.data.remote.SupabaseClientProvider
import io.github.jan.supabase.postgrest.from

class ScheduleRepository {

    private val client = SupabaseClientProvider.client

    suspend fun getSchedules(): List<Schedule> {
        return client
            .from("schedules")
            .select()
            .decodeList<Schedule>()
    }

    suspend fun addSchedule(schedule: Schedule) {
        client
            .from("schedules")
            .insert(schedule)
    }

    suspend fun deleteSchedule(scheduleId: String) {
        if (scheduleId.isBlank()) return  // guard

        client
            .from("schedules")
            .delete {
                filter {
                    eq("id", scheduleId)
                }
            }
    }

    suspend fun updateScheduleStatus(
        scheduleId: String,
        status: Boolean
    ) {
        if (scheduleId.isBlank()) return  // guard — ini yang bikin bug centang

        client
            .from("schedules")
            .update(
                {
                    set("status", status)
                }
            ) {
                filter {
                    eq("id", scheduleId)
                }
            }
    }
}