package com.example.koscare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koscare.data.model.Schedule
import com.example.koscare.data.repository.AuthRepository
import com.example.koscare.data.repository.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScheduleViewModel : ViewModel() {

    private val repository =
        ScheduleRepository()

    private val authRepository =
        AuthRepository()

    private val _schedules =
        MutableStateFlow<List<Schedule>>(emptyList())

    val schedules =
        _schedules.asStateFlow()

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading =
        _isLoading.asStateFlow()

    private val _errorMessage =
        MutableStateFlow<String?>(null)

    val errorMessage =
        _errorMessage.asStateFlow()

    fun getSchedules() {

        viewModelScope.launch {

            _isLoading.value = true

            _errorMessage.value = null

            try {

                val userId =
                    authRepository
                        .getCurrentUserId()

                if (userId == null) {

                    _errorMessage.value =
                        "User belum login"

                    return@launch
                }

                _schedules.value =
                    repository.getSchedules(userId)

            } catch (e: Exception) {

                e.printStackTrace()

                _errorMessage.value =
                    "Gagal memuat jadwal"

            } finally {

                _isLoading.value = false
            }
        }
    }

    private fun refreshSchedules() {

        viewModelScope.launch {

            try {

                val userId =
                    authRepository
                        .getCurrentUserId()

                if (userId != null) {

                    _schedules.value =
                        repository.getSchedules(userId)
                }

            } catch (e: Exception) {

                e.printStackTrace()

                _errorMessage.value =
                    "Gagal refresh jadwal"
            }
        }
    }

    fun addSchedule(
        title: String,
        description: String,
        scheduleDate: String,
        scheduleTime: String
    ) {

        viewModelScope.launch {

            try {

                val userId =
                    authRepository
                        .getCurrentUserId()

                if (userId == null) {

                    _errorMessage.value =
                        "User belum login"

                    return@launch
                }

                val schedule =
                    Schedule(

                        user_id = userId,

                        title = title.trim(),

                        description = description.trim(),

                        schedule_date = scheduleDate,

                        schedule_time = scheduleTime,

                        status = false
                    )

                repository.addSchedule(schedule)

                refreshSchedules()

            } catch (e: Exception) {

                e.printStackTrace()

                _errorMessage.value =
                    "Gagal menambah jadwal"
            }
        }
    }

    fun deleteSchedule(
        scheduleId: String
    ) {

        viewModelScope.launch {

            try {

                repository.deleteSchedule(
                    scheduleId
                )

                refreshSchedules()

            } catch (e: Exception) {

                e.printStackTrace()

                _errorMessage.value =
                    "Gagal menghapus jadwal"
            }
        }
    }

    fun updateStatus(
        schedule: Schedule
    ) {

        viewModelScope.launch {

            try {

                repository.updateScheduleStatus(

                    scheduleId =
                        schedule.id ?: return@launch,

                    status =
                        !schedule.status
                )

                refreshSchedules()

            } catch (e: Exception) {

                e.printStackTrace()

                _errorMessage.value =
                    "Gagal update status"
            }
        }
    }

    fun clearError() {

        _errorMessage.value = null
    }
}