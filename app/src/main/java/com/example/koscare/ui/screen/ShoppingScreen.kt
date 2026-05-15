package com.example.koscare.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        mutableStateOf("1")
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "💻",
                    fontSize = 28.sp
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Shopping KosCare",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Emerald
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // FORM CARD
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(28.dp)
                    ),

                shape = RoundedCornerShape(28.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFEAF4F0)
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    Text(
                        text = "🛒 Add to List",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // ITEM NAME + IMAGE
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        OutlinedTextField(
                            value = itemName,
                            onValueChange = {
                                itemName = it
                            },

                            placeholder = {
                                Text("Item name...")
                            },

                            modifier = Modifier.weight(1f),

                            shape = RoundedCornerShape(18.dp),

                            singleLine = true
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = {

                                launcher.launch("image/*")
                            },

                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF4F4F4)
                            ),

                            shape = RoundedCornerShape(18.dp),

                            contentPadding = PaddingValues(0.dp),

                            modifier = Modifier.size(62.dp)
                        ) {

                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                tint = Color(0xFF10B981)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // PREVIEW IMAGE
                    selectedImageUri?.let { uri ->

                        Image(
                            painter = rememberAsyncImagePainter(uri),

                            contentDescription = null,

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(20.dp)),

                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // QUANTITY + BUTTON
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        OutlinedTextField(
                            value = quantity,

                            onValueChange = {

                                quantity = it
                            },

                            label = {
                                Text("Quantity")
                            },

                            modifier = Modifier.weight(1f),

                            shape = RoundedCornerShape(18.dp),

                            singleLine = true
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = {

                                if (itemName.isNotEmpty()) {

                                    val qty =
                                        quantity.toIntOrNull() ?: 1

                                    selectedImageUri?.let { uri ->

                                        shoppingViewModel
                                            .uploadImageAndAddItem(
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

                            colors = ButtonDefaults.buttonColors(
                                containerColor = Emerald
                            ),

                            shape = RoundedCornerShape(18.dp),

                            modifier = Modifier.height(56.dp)
                        ) {

                            Text(
                                text = "Add",
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }

        // LIST ITEM
        items(items) { item ->

            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(24.dp),

                colors = CardDefaults.cardColors(
                    containerColor =
                        if (item.is_bought)
                            Emerald.copy(alpha = 0.15f)

                        else
                            MaterialTheme.colorScheme.surface
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    item.image_url?.let { imageUrl ->

                        Image(
                            painter =
                                rememberAsyncImagePainter(imageUrl),

                            contentDescription = null,

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(20.dp)),

                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceBetween,

                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = item.item_name,

                                style =
                                    MaterialTheme.typography.titleLarge,

                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = "Quantity: ${item.quantity}",

                                color = Color.Gray
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
                                            Color.Gray
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

                                    contentDescription = null,

                                    tint = Color.Red
                                )
                            }
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