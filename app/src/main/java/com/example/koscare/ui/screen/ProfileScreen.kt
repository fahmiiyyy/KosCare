package com.example.koscare.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.koscare.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
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
                .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = "Profile",
            style =
                MaterialTheme.typography.headlineMedium,

            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        Image(
            painter =
                rememberAsyncImagePainter(
                    model =
                        imageUri
                            ?: profile?.profile_image_url
                ),

            contentDescription = null,

            modifier =
                Modifier
                    .size(140.dp)
                    .clip(CircleShape),

            contentScale =
                ContentScale.Crop
        )

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        Button(
            onClick = {

                launcher.launch("image/*")
            }
        ) {

            Text("Pilih Foto")
        }

        Spacer(
            modifier =
                Modifier.height(16.dp)
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
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
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
                Modifier.fillMaxWidth()
        ) {

            Text("Simpan Profile")
        }

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        OutlinedButton(

            onClick = {

                viewModel.logout {

                    navController.navigate("login") {

                        popUpTo(0)
                    }
                }
            },

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text("Logout")
        }
    }
}