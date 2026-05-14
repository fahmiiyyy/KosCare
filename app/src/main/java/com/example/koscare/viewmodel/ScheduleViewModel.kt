package com.example.koscare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koscare.data.model.Schedule
import com.example.koscare.data.remote.SupabaseClientProvider
import com.example.koscare.data.repository.ScheduleRepository
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScheduleViewModel : ViewModel() {

    private val repository = ScheduleRepository()

    private val client = SupabaseClientProvider.client

    private val _schedules =
        MutableStateFlow<List<Schedule>>(emptyList())

    val schedules = _schedules.asStateFlow()

    fun getSchedules() {

        viewModelScope.launch {

            _schedules.value =
                repository.getSchedules()
        }
    }

    fun addSchedule(
        title: String,
        description: String,
        scheduleDate: String,
        scheduleTime: String
    ) {

        viewModelScope.launch {

            val userId =
                client.auth.currentUserOrNull()?.id

            if (userId != null) {

                repository.addSchedule(

                    Schedule(
                        title = title,
                        description = description,
                        schedule_date = scheduleDate,
                        schedule_time = scheduleTime,
                        user_id = userId
                    )
                )

                getSchedules()
            }
        }
    }

    fun deleteSchedule(
        scheduleId: String
    ) {

        viewModelScope.launch {

            repository.deleteSchedule(scheduleId)

            getSchedules()
        }
    }

    fun updateStatus(
        schedule: Schedule
    ) {

        viewModelScope.launch {

            repository.updateScheduleStatus(
                scheduleId = schedule.id ?: "",
                status = !schedule.status
            )

            getSchedules()
        }
    }
}