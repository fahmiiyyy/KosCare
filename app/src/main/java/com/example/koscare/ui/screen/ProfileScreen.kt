package com.example.koscare.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import coil.compose.rememberAsyncImagePainter
import com.example.koscare.ui.theme.Background
import com.example.koscare.ui.theme.Emerald
import com.example.koscare.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel: ProfileViewModel
) {

    val context = LocalContext.current

    val profile by viewModel.profile.collectAsState()

    var fullName by remember {
        mutableStateOf("")
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var showImageDialog by remember {
        mutableStateOf(false)
    }

    var scale by remember {
        mutableStateOf(1f)
    }

    LaunchedEffect(Unit) {

        viewModel.getProfile()
    }

    LaunchedEffect(profile) {

        if (profile != null) {

            fullName =
                profile?.full_name ?: ""
        }
    }

    val launcher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.GetContent()
        ) {

            imageUri = it
        }

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .background(Background)
                .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(

            text = "Profile",

            style =
                MaterialTheme.typography.headlineMedium,

            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(28.dp)
        )

        Box(

            contentAlignment =
                Alignment.BottomEnd
        ) {

            Image(

                painter =
                    rememberAsyncImagePainter(

                        model =
                            imageUri
                                ?: profile?.profile_image_url
                    ),

                contentDescription =
                    null,

                modifier =
                    Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .clickable {

                            showImageDialog = true
                        },

                contentScale =
                    ContentScale.Crop
            )

            FloatingActionButton(

                onClick = {

                    launcher.launch("image/*")
                },

                modifier =
                    Modifier.size(46.dp),

                containerColor =
                    Emerald
            ) {

                Icon(

                    imageVector =
                        Icons.Default.Edit,

                    contentDescription =
                        null,

                    tint =
                        Color.White
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(28.dp)
        )

        OutlinedTextField(

            value = fullName,

            onValueChange = {

                fullName = it
            },

            label = {

                Text("Nama Lengkap")
            },

            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(18.dp),

            singleLine = true
        )

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        Button(

            onClick = {

                if (imageUri != null) {

                    viewModel
                        .uploadImageAndSaveProfile(

                            context = context,

                            imageUri = imageUri!!,

                            fullName = fullName
                        )

                } else {

                    viewModel.updateProfile(

                        fullName = fullName,

                        imageUrl =
                            profile?.profile_image_url
                    )
                }
            },

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),

            shape =
                RoundedCornerShape(18.dp),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor = Emerald
                )
        ) {

            Text(

                text = "Simpan Profile",

                fontSize = 16.sp
            )
        }

        Spacer(
            modifier =
                Modifier.height(18.dp)
        )

        OutlinedButton(

            onClick = {

                viewModel.logout {

                    onLogout()
                }
            },

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),

            shape =
                RoundedCornerShape(18.dp)
        ) {

            Text("Logout")
        }
    }

    if (showImageDialog) {

        Dialog(

            onDismissRequest = {

                showImageDialog = false

                scale = 1f
            }
        ) {

            Box(

                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black),

                contentAlignment =
                    Alignment.Center
            ) {

                Image(

                    painter =
                        rememberAsyncImagePainter(

                            model =
                                imageUri
                                    ?: profile?.profile_image_url
                        ),

                    contentDescription =
                        null,

                    modifier =
                        Modifier

                            .fillMaxWidth()

                            .graphicsLayer(

                                scaleX = scale,

                                scaleY = scale
                            )

                            .pointerInput(Unit) {

                                detectTransformGestures {

                                        _, _, zoom, _ ->

                                    scale *= zoom

                                    scale =
                                        scale.coerceIn(
                                            1f,
                                            5f
                                        )
                                }
                            },

                    contentScale =
                        ContentScale.Fit
                )
            }
        }
    }
}