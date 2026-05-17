package com.example.koscare.ui.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.koscare.ui.theme.Background
import com.example.koscare.ui.theme.Emerald
import com.example.koscare.viewmodel.ScheduleViewModel
import java.util.Calendar
import java.util.Locale

@Composable
fun ScheduleScreen(
    scheduleViewModel: ScheduleViewModel = viewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var scheduleDate by remember { mutableStateOf("") }
    var scheduleTime by remember { mutableStateOf("") }

    val schedules by scheduleViewModel.schedules.collectAsState()
    val isLoading by scheduleViewModel.isLoading.collectAsState()
    val errorMessage by scheduleViewModel.errorMessage.collectAsState()

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            scheduleDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            scheduleTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    LaunchedEffect(Unit) {
        scheduleViewModel.getSchedules()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Schedule Tracker",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Emerald
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF4F0))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Tambah Jadwal",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                            scheduleViewModel.clearError()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Judul kegiatan...") },
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            description = it
                            scheduleViewModel.clearError()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Catatan tambahan...") },
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row {
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = scheduleDate,
                                onValueChange = {},
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Pilih Tanggal") },
                                shape = RoundedCornerShape(14.dp),
                                singleLine = true,
                                readOnly = true
                            )
                            Box(modifier = Modifier
                                .matchParentSize()
                                .clickable { datePickerDialog.show() })
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = scheduleTime,
                                onValueChange = {},
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Pilih Waktu") },
                                shape = RoundedCornerShape(14.dp),
                                singleLine = true,
                                readOnly = true
                            )
                            Box(modifier = Modifier
                                .matchParentSize()
                                .clickable { timePickerDialog.show() })
                        }
                    }

                    errorMessage?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "⚠️ $it",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            when {
                                title.isBlank() -> scheduleViewModel.run {
                                    // trigger error via ViewModel message
                                }
                                description.isBlank() || scheduleDate.isBlank() || scheduleTime.isBlank() -> {}
                                else -> {
                                    scheduleViewModel.addSchedule(
                                        title = title,
                                        description = description,
                                        scheduleDate = scheduleDate,
                                        scheduleTime = scheduleTime
                                    )
                                    title = ""
                                    description = ""
                                    scheduleDate = ""
                                    scheduleTime = ""
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald),
                        enabled = !isLoading &&
                                title.isNotBlank() &&
                                description.isNotBlank() &&
                                scheduleDate.isNotBlank() &&
                                scheduleTime.isNotBlank()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(22.dp)
                            )
                        } else {
                            Text(
                                text = "Tambah Jadwal",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Loading state
        if (isLoading && schedules.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Emerald)
                }
            }
        }

        // Empty state
        if (!isLoading && schedules.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "📅", fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Belum ada jadwal", fontWeight = FontWeight.SemiBold)
                        Text(
                            text = "Tambahkan jadwal pertamamu!",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        items(schedules) { schedule ->
                Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (schedule.status)
                            Emerald
                        else
                            Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = schedule.title,
                            color = if (schedule.status) Color.White else Color.Black,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = schedule.description,
                            color = if (schedule.status)
                                Color.White.copy(alpha = 0.85f)
                            else
                                Color.Gray,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${schedule.schedule_date}  •  ${schedule.schedule_time}",
                            color = if (schedule.status)
                                Color.White
                            else
                                Emerald,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                    Row {
                        IconButton(onClick = { scheduleViewModel.updateStatus(schedule) }) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "Selesai",
                                tint = if (schedule.status)
                                    Color.White
                                else
                                    Color(0xFFD1D5DB)
                            )
                        }
                        IconButton(onClick = {
                            schedule.id?.let { scheduleViewModel.deleteSchedule(it) }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Hapus",
                                tint = Color(0xFFEF4444)
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}