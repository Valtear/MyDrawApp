package com.example.mydrawapp.viewModels

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mydrawapp.dataClasses.ImageEntry
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.example.mydrawapp.dataBase.deleteImage
import com.example.mydrawapp.dataBase.loadImages
import kotlinx.coroutines.launch

@Composable
fun ImageGallery(database: SQLiteDatabase, navController: NavController) {

    val images = remember { mutableStateListOf<ImageEntry>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        loadImages(database, images)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onBackground)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            content = {
                items(images) { image ->
                    ImageItem(image = image,
                        onClick = {
                            navController.navigate("fullscreen/${image.id}/${image.name}")
                        },
                        onDelete = {
                            coroutineScope.launch {
                                try {
                                    deleteImage(database, image.id)
                                    loadImages(database, images)
                                } catch (e: Exception) {
                                    Log.e("ImageGallery", "Error deleting image: ${e.message}")
                                }
                            }
                        },
                        )
                }
            },
            modifier = Modifier.padding(4.dp)
        )
    }
}




