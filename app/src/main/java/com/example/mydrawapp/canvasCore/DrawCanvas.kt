package com.example.mydrawapp.canvasCore

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.example.mydrawapp.dataClasses.PathData

@Composable
fun DrawCanvas(
    pathData: MutableState<PathData>,
    pathList: SnapshotStateList<PathData>
) {
    var tempPath = remember { Path() }

    Canvas(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .fillMaxHeight(0.75f)
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = {
                        tempPath = Path()
                        pathList.add(pathData.value.copy(path = tempPath))
                    },
                    onDragEnd = {
                        pathList.add(pathData.value.copy(path = tempPath))
                    }
                ) { change, dragAmount ->
                    tempPath.moveTo(
                        change.position.x - dragAmount.x,
                        change.position.y - dragAmount.y
                    )
                    tempPath.lineTo(
                        change.position.x,
                        change.position.y
                    )
                    if (pathList.isNotEmpty()) {
                        pathList.removeAt(pathList.size - 1)
                    }
                    pathList.add(pathData.value.copy(path = tempPath))
                }
            }
            .pointerInput(true) {
                detectTapGestures(
                    onTap = { offset ->
                        tempPath = Path()
                        tempPath.moveTo(offset.x, offset.y)
                        tempPath.lineTo(offset.x, offset.y)
                        pathList.add(pathData.value.copy(path = tempPath))
                    }
                )
            }
    ) {
        pathList.forEach { pathData ->
            drawPath(
                pathData.path,
                color = pathData.color,
                style = Stroke(
                    pathData.lineWidth,
                    cap = pathData.cap
                )
            )
        }
    }
}