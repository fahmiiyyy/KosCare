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

    private val _schedules = MutableStateFlow<List<Schedule>>(emptyList())
    val schedules = _schedules.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun getSchedules() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _schedules.value = repository.getSchedules()
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memuat jadwal. Periksa koneksi internet kamu."
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun refreshSchedules() {
        viewModelScope.launch {
            try {
                _schedules.value = repository.getSchedules()
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memperbarui jadwal. Coba lagi."
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
            _errorMessage.value = null
            try {
                val userId = client.auth.currentUserOrNull()?.id
                if (userId == null) {
                    _errorMessage.value = "Sesi kamu sudah berakhir. Silakan login ulang."
                    return@launch
                }

                val newSchedule = Schedule(
                    title = title.trim(),
                    description = description.trim(),
                    schedule_date = scheduleDate,
                    schedule_time = scheduleTime,
                    user_id = userId,
                    status = false
                )

                _schedules.value = _schedules.value + newSchedule

                repository.addSchedule(newSchedule)
                refreshSchedules()

            } catch (e: Exception) {
                _errorMessage.value = "Gagal menambah jadwal. Coba lagi ya."
                refreshSchedules()
            }
        }
    }

    fun deleteSchedule(scheduleId: String) {
        viewModelScope.launch {
            try {
                _schedules.value = _schedules.value.filter { it.id != scheduleId }
                repository.deleteSchedule(scheduleId)
            } catch (e: Exception) {
                _errorMessage.value = "Gagal menghapus jadwal. Coba lagi."
                refreshSchedules()
            }
        }
    }

    fun updateStatus(schedule: Schedule) {
        viewModelScope.launch {
            try {
                _schedules.value = _schedules.value.map {
                    if (it.id == schedule.id) it.copy(status = !it.status) else it
                }
                repository.updateScheduleStatus(
                    scheduleId = schedule.id ?: return@launch,
                    status = !schedule.status
                )
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memperbarui status. Coba lagi."
                refreshSchedules()
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}