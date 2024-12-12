package com.example.mydrawapp.viewModels

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.mydrawapp.dataClasses.PathData
import com.example.mydrawapp.canvasCore.DrawCanvas
import com.example.mydrawapp.canvasCore.saveCanvasToBitmap

@Composable
fun DrawScreen(
    pathData: MutableState<PathData>,
    pathList: SnapshotStateList<PathData>,
    navController: NavController
) {
    val context = LocalContext.current

    Column {
        DrawCanvas(pathData, pathList)
        BottomPanel(
            { color ->
                pathData.value = pathData.value.copy(
                    color = color
                )
            },
            { lineWidth ->
                pathData.value = pathData.value.copy(
                    lineWidth = lineWidth
                )
            },
            {
                pathList.removeIf { pathData ->
                    pathList[pathList.size - 1] == pathData
                }
            },
            {
                pathList.clear()
            },
            {
                navController.navigate("albumScreen")
            },
            {
                Log.d("Event", "true")
                saveCanvasToBitmap(pathList, context)
            },
        ) { cap ->
            pathData.value = pathData.value.copy(
                cap = cap
            )
        }
    }
}







