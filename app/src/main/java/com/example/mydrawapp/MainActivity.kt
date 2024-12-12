package com.example.mydrawapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mydrawapp.dataBase.DatabaseHelper
import com.example.mydrawapp.dataBase.getImagePathById
import com.example.mydrawapp.dataClasses.ImageEntry
import com.example.mydrawapp.ui.theme.MyDrawAppTheme
import com.example.mydrawapp.dataClasses.PathData
import com.example.mydrawapp.viewModels.DrawScreen
import com.example.mydrawapp.viewModels.FullscreenImage
import com.example.mydrawapp.viewModels.ImageGallery
import com.example.mydrawapp.viewModels.MainScreen


class MainActivity : ComponentActivity() {
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        databaseHelper = DatabaseHelper(this)
        setContent {
            val navController = rememberNavController()

            val pathData = remember {
                mutableStateOf(PathData())
            }
            val pathList = remember {
                mutableStateListOf(PathData())
            }
            MyDrawAppTheme {
                NavHost(
                    navController = navController,
                    startDestination = "mainScreen"
                ) {
                    composable("mainScreen") {
                        MainScreen {
                            navController.navigate("drawScreen")
                        }
                    }
                    composable("drawScreen") {
                        DrawScreen(pathData, pathList, navController)
                    }
                    composable("albumScreen") {
                        ImageGallery(databaseHelper.readableDatabase, navController)
                    }
                    composable("fullscreen/{imageId}/{imageName}") { backStackEntry ->
                        val imageId = backStackEntry.arguments?.getString("imageId")?.toLong() ?: 0L
                        val imageName = backStackEntry.arguments?.getString("imageName") ?: ""
                        val imagePath = getImagePathById(databaseHelper.readableDatabase, imageId)

                        FullscreenImage(
                            image = ImageEntry(imageId, imageName, imagePath),
                            onClose = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }

}
