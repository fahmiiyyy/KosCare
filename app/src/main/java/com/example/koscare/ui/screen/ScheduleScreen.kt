package com.example.koscare.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.koscare.ui.theme.Background
import com.example.koscare.ui.theme.Emerald
import com.example.koscare.viewmodel.ScheduleViewModel

@Composable
fun ScheduleScreen(
    scheduleViewModel: ScheduleViewModel = viewModel()
) {

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var scheduleDate by remember {
        mutableStateOf("")
    }

    var scheduleTime by remember {
        mutableStateOf("")
    }

    val schedules by scheduleViewModel.schedules.collectAsState()

    LaunchedEffect(Unit) {

        scheduleViewModel.getSchedules()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {

        Text(
            text = "Schedule Tracker",

            style = MaterialTheme.typography.headlineMedium,

            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
            },

            modifier = Modifier.fillMaxWidth(),

            label = {
                Text("Judul Kegiatan")
            },

            shape = RoundedCornerShape(20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
            },

            modifier = Modifier.fillMaxWidth(),

            label = {
                Text("Catatan")
            },

            shape = RoundedCornerShape(20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = scheduleDate,
            onValueChange = {
                scheduleDate = it
            },

            modifier = Modifier.fillMaxWidth(),

            label = {
                Text("Tanggal")
            },

            placeholder = {
                Text("2026-05-14")
            },

            shape = RoundedCornerShape(20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = scheduleTime,
            onValueChange = {
                scheduleTime = it
            },

            modifier = Modifier.fillMaxWidth(),

            label = {
                Text("Jam")
            },

            placeholder = {
                Text("20:00")
            },

            shape = RoundedCornerShape(20.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                if (
                    title.isNotEmpty() &&
                    description.isNotEmpty() &&
                    scheduleDate.isNotEmpty() &&
                    scheduleTime.isNotEmpty()
                ) {

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
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Tambah Jadwal")
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(schedules) { schedule ->

                Card(
                    shape = RoundedCornerShape(20.dp),

                    colors = CardDefaults.cardColors(
                        containerColor =
                            if (schedule.status)
                                Emerald.copy(alpha = 0.2f)

                            else
                                MaterialTheme.colorScheme.surface
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),

                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = schedule.title,

                                style =
                                    MaterialTheme.typography.titleMedium,

                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = schedule.description
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Text(
                                text =
                                    "${schedule.schedule_date} • ${schedule.schedule_time}",

                                color = Emerald,

                                style =
                                    MaterialTheme.typography.bodySmall
                            )
                        }

                        Row {

                            IconButton(
                                onClick = {

                                    scheduleViewModel
                                        .updateStatus(schedule)
                                }
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Done,

                                    contentDescription = null,

                                    tint =
                                        if (schedule.status)
                                            Emerald

                                        else
                                            MaterialTheme
                                                .colorScheme
                                                .onSurface
                                )
                            }

                            IconButton(
                                onClick = {

                                    schedule.id?.let {

                                        scheduleViewModel
                                            .deleteSchedule(it)
                                    }
                                }
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Delete,

                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}