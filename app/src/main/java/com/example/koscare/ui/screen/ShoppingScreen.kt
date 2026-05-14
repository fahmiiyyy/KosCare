package com.example.koscare.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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

    var itemName by remember {
        mutableStateOf("")
    }

    var quantity by remember {
        mutableIntStateOf(1)
    }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->

            selectedImageUri = uri
        }

    val items by shoppingViewModel.items.collectAsState()

    LaunchedEffect(Unit) {

        shoppingViewModel.getItems()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {

        Text(
            text = "Shopping List",

            style = MaterialTheme.typography.headlineMedium,

            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = itemName,
            onValueChange = {
                itemName = it
            },

            modifier = Modifier.fillMaxWidth(),

            label = {
                Text("Nama Barang")
            },

            shape = RoundedCornerShape(20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = quantity.toString(),
            onValueChange = {

                quantity = it.toIntOrNull() ?: 1
            },

            modifier = Modifier.fillMaxWidth(),

            label = {
                Text("Quantity")
            },

            shape = RoundedCornerShape(20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {

                launcher.launch("image/*")
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Pilih Gambar")
        }

        selectedImageUri?.let { uri ->

            Spacer(modifier = Modifier.height(12.dp))

            Image(
                painter = rememberAsyncImagePainter(uri),

                contentDescription = null,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),

                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                if (itemName.isNotEmpty()) {

                    selectedImageUri?.let { uri ->

                        shoppingViewModel.uploadImageAndAddItem(
                            context = context,
                            imageUri = uri,
                            itemName = itemName,
                            quantity = quantity
                        )

                    } ?: run {

                        shoppingViewModel.addItem(
                            itemName = itemName,
                            quantity = quantity,
                            imageUrl = null
                        )
                    }

                    itemName = ""
                    quantity = 1
                    selectedImageUri = null
                }
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Tambah Barang")
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(items) { item ->

                Card(
                    shape = RoundedCornerShape(20.dp),

                    colors = CardDefaults.cardColors(
                        containerColor =
                            if (item.is_bought)
                                Emerald.copy(alpha = 0.2f)

                            else
                                MaterialTheme.colorScheme.surface
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {

                        item.image_url?.let { imageUrl ->

                            Image(
                                painter =
                                    rememberAsyncImagePainter(imageUrl),

                                contentDescription = null,

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp),

                                contentScale = ContentScale.Crop
                            )

                            Spacer(
                                modifier = Modifier.height(12.dp)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),

                            horizontalArrangement =
                                Arrangement.SpaceBetween
                        ) {

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = item.item_name,

                                    style =
                                        MaterialTheme.typography.titleMedium,

                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(
                                    modifier = Modifier.height(4.dp)
                                )

                                Text(
                                    text = "Jumlah: ${item.quantity}"
                                )
                            }

                            Row {

                                IconButton(
                                    onClick = {

                                        shoppingViewModel
                                            .updateBoughtStatus(item)
                                    }
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Done,

                                        contentDescription = null,

                                        tint =
                                            if (item.is_bought)
                                                Emerald

                                            else
                                                MaterialTheme
                                                    .colorScheme
                                                    .onSurface
                                    )
                                }

                                IconButton(
                                    onClick = {

                                        item.id?.let {

                                            shoppingViewModel
                                                .deleteItem(it)
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
}