package com.example.koscare.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {

            // HEADER
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "📅",
                    fontSize = 28.sp
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "KosCare",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Emerald
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // FORM CARD
            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(28.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFEAF4F0)
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    Text(
                        text = "📝 Add Schedule",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                        },

                        modifier = Modifier.fillMaxWidth(),

                        placeholder = {
                            Text("Activity title...")
                        },

                        shape = RoundedCornerShape(18.dp),

                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            description = it
                        },

                        modifier = Modifier.fillMaxWidth(),

                        placeholder = {
                            Text("Notes...")
                        },

                        shape = RoundedCornerShape(18.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row {

                        OutlinedTextField(
                            value = scheduleDate,
                            onValueChange = {
                                scheduleDate = it
                            },

                            modifier = Modifier.weight(1f),

                            placeholder = {
                                Text("2026-05-14")
                            },

                            shape = RoundedCornerShape(18.dp),

                            singleLine = true
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        OutlinedTextField(
                            value = scheduleTime,
                            onValueChange = {
                                scheduleTime = it
                            },

                            modifier = Modifier.weight(1f),

                            placeholder = {
                                Text("20:00")
                            },

                            shape = RoundedCornerShape(18.dp),

                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

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

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),

                        shape = RoundedCornerShape(18.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Emerald
                        )
                    ) {

                        Text(
                            text = "Add Schedule",
                            fontSize = 17.sp
                        )
                    }
                }
            }
        }

        // SCHEDULE LIST
        items(schedules) { schedule ->

            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(24.dp),

                colors = CardDefaults.cardColors(
                    containerColor =
                        if (schedule.status)
                            Emerald.copy(alpha = 0.15f)

                        else
                            MaterialTheme.colorScheme.surface
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),

                    horizontalArrangement =
                        Arrangement.SpaceBetween,

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = schedule.title,

                            style =
                                MaterialTheme.typography.titleLarge,

                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = schedule.description,

                            color = Color.Gray
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        Text(
                            text =
                                "${schedule.schedule_date} • ${schedule.schedule_time}",

                            color = Emerald,

                            fontWeight = FontWeight.SemiBold
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
                                        Color.Gray
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

                                contentDescription = null,

                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }

        item {

            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}