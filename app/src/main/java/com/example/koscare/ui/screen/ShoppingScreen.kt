package com.example.koscare.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.koscare.ui.theme.Background
import com.example.koscare.ui.theme.Emerald
import com.example.koscare.viewmodel.ShoppingViewModel

@Composable
fun ShoppingScreen(
    shoppingViewModel: ShoppingViewModel = viewModel()
) {
    val context = LocalContext.current
    var itemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var previewImage by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
        shoppingViewModel.clearError()
    }

    val items by shoppingViewModel.items.collectAsState()
    val isLoading by shoppingViewModel.isLoading.collectAsState()
    val errorMessage by shoppingViewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        shoppingViewModel.getItems()
    }

    Box {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Shopping List",
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
                            text = "🛒 Tambah Item",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = itemName,
                                onValueChange = {
                                    itemName = it
                                    shoppingViewModel.clearError()
                                },
                                placeholder = { Text("Nama item...") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            OutlinedButton(
                                onClick = { launcher.launch("image/*") },
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.size(56.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Image,
                                    contentDescription = "Pilih foto",
                                    tint = if (selectedImageUri != null) Emerald else Color.Gray
                                )
                            }
                        }

                        selectedImageUri?.let { uri ->
                            Spacer(modifier = Modifier.height(12.dp))
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Foto dipilih ✓",
                                color = Emerald,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = quantity,
                                onValueChange = {
                                    quantity = it
                                    shoppingViewModel.clearError()
                                },
                                label = { Text("Jumlah") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Button(
                                onClick = {
                                    if (itemName.isNotBlank()) {
                                        val qty = quantity.toIntOrNull()?.coerceAtLeast(1) ?: 1
                                        selectedImageUri?.let { uri ->
                                            shoppingViewModel.uploadImageAndAddItem(
                                                context = context,
                                                imageUri = uri,
                                                itemName = itemName,
                                                quantity = qty
                                            )
                                        } ?: run {
                                            shoppingViewModel.addItem(
                                                itemName = itemName,
                                                quantity = qty,
                                                imageUrl = null
                                            )
                                        }
                                        itemName = ""
                                        quantity = "1"
                                        selectedImageUri = null
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Emerald),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.height(56.dp),
                                enabled = !isLoading && itemName.isNotBlank()
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else {
                                    Text(text = "Tambah", fontSize = 16.sp)
                                }
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
                    }
                }
            }

            // Loading
            if (isLoading && items.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Emerald)
                    }
                }
            }

            // Empty state
            if (!isLoading && items.isEmpty()) {
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
                            Text(text = "🛒", fontSize = 40.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Daftar belanja kosong", fontWeight = FontWeight.SemiBold)
                            Text(
                                text = "Tambahkan item yang perlu dibeli!",
                                color = Color.Gray,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            items(items) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (item.is_bought)
                            Emerald.copy(alpha = 0.1f)
                        else
                            Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        item.image_url?.let { imageUrl ->
                            Image(
                                painter = rememberAsyncImagePainter(imageUrl),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { previewImage = imageUrl },
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.item_name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Jumlah: ${item.quantity}",
                                    color = Color.Gray,
                                    fontSize = 13.sp
                                )
                                if (item.is_bought) {
                                    Text(
                                        text = "✓ Sudah dibeli",
                                        color = Emerald,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                            Row {
                                IconButton(onClick = { shoppingViewModel.updateBoughtStatus(item) }) {
                                    Icon(
                                        imageVector = Icons.Default.Done,
                                        contentDescription = "Tandai dibeli",
                                        tint = if (item.is_bought) Emerald else Color.Gray
                                    )
                                }
                                IconButton(onClick = {
                                    item.id?.let { shoppingViewModel.deleteItem(it) }
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
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        // Dialog preview foto
        previewImage?.let { imageUrl ->
            Dialog(onDismissRequest = { previewImage = null }) {
                Card(shape = RoundedCornerShape(24.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}