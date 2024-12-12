package com.example.mydrawapp.viewModels

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.mydrawapp.dataClasses.ImageEntry
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme

@Composable
fun ImageItem(image: ImageEntry, onClick: () -> Unit, onDelete: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .size(350.dp)
            .padding(4.dp)
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { showMenu = true }
            )
    ) {
        Image(
            painter = rememberAsyncImagePainter(image.path),
            contentDescription = image.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        if (showMenu) {
            DropdownMenu(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onBackground),
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(onClick = {
                    showDeleteConfirmationDialog = true
                    showMenu = false
                }) {
                    Text("Удалить")
                }
            }
        }
        if (showDeleteConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmationDialog = false },
                title = { Text("Удалить изображение") },
                text = { Text("Вы уверены, что хотите удалить это изображение?") },
                confirmButton = {
                    Button(
                        onClick = {
                            onDelete()
                            showDeleteConfirmationDialog = false
                        }
                    ) {
                        Text("Да")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDeleteConfirmationDialog = false
                        }
                    ) {
                        Text("Нет")
                    }
                },
                containerColor = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}


