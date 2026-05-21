package com.example.koscare.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.koscare.ui.theme.Background
import com.example.koscare.ui.theme.Emerald
import com.example.koscare.viewmodel.ProfileViewModel
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.window.DialogProperties

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel: ProfileViewModel
) {
    val context = LocalContext.current
    val profile by viewModel.profile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showImageDialog by remember { mutableStateOf(false) }
    var scale by remember { mutableStateOf(1f) }

    LaunchedEffect(Unit) {
        viewModel.getProfile()
    }

    LaunchedEffect(profile) {
        fullName = profile?.full_name ?: ""
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
        viewModel.clearMessages()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profil Saya",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Avatar
        Box(contentAlignment = Alignment.BottomEnd) {
            val imageModel = imageUri ?: profile?.profile_image_url

            if (imageModel != null) {
                AsyncImage(
                    model = imageModel,
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .clickable { showImageDialog = true },
                    contentScale = ContentScale.Crop
                )
            } else {
                // Placeholder saat belum ada foto
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Emerald
                    )
                }
            }

            FloatingActionButton(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.size(40.dp),
                containerColor = Emerald
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Ganti foto",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        if (imageUri != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Foto baru dipilih — klik Simpan untuk menyimpan.",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = {
                fullName = it
                viewModel.clearMessages()
            },
            label = { Text("Nama Lengkap") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Email tidak bisa diubah.",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(24.dp))

        successMessage?.let {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$it",
                    color = Color(0xFF166534),
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Pesan error
        errorMessage?.let {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$it",
                    color = Color(0xFF991B1B),
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        Button(
            onClick = {
                if (imageUri != null) {
                    viewModel.uploadImageAndSaveProfile(
                        context = context,
                        imageUri = imageUri!!,
                        fullName = fullName
                    )
                } else {
                    viewModel.updateProfile(
                        fullName = fullName,
                        imageUrl = profile?.profile_image_url
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Emerald),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(22.dp)
                )
            } else {
                Text(
                    text = "Simpan Profil",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedButton(
            onClick = { viewModel.logout { onLogout() } },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            enabled = !isLoading
        ) {
            Text("Logout", color = Color(0xFFEF4444))
        }
    }

    // Dialog preview foto fullscreen
    if (showImageDialog) {
        val imageModel = imageUri ?: profile?.profile_image_url
        if (imageModel != null) {
            Dialog(
                onDismissRequest = {
                    showImageDialog = false
                    scale = 1f
                },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .clickable {
                            showImageDialog = false
                            scale = 1f
                        },
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = imageModel,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .graphicsLayer(scaleX = scale, scaleY = scale)
                            .pointerInput(Unit) {
                                detectTransformGestures { _, _, zoom, _ ->
                                    scale = (scale * zoom).coerceIn(1f, 5f)
                                }
                            },
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
        }
    }
}