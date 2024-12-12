package com.example.mydrawapp.viewModels

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mydrawapp.R

@Composable
fun BottomPanel(
    onColorClick: (Color) -> Unit,
    onLineWidthChange: (Float) -> Unit,
    onUndoClick: () -> Unit,
    onClearClick: () -> Unit,
    onAlbumClick: () -> Unit,
    onSaveClick: () -> Unit,
    onCapClick: (StrokeCap) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.onBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ColorList { color ->
            onColorClick(color)
        }
        CustomSlider({ lineWidth ->
            onLineWidthChange(lineWidth)
        }) { cap ->
            onCapClick(cap)
        }
        ButtonPanel(
            {
                onUndoClick()
            },
            {
                onClearClick()
            },
            {
                onAlbumClick()
            }
        ) {
            onSaveClick()
        }
    }
}

@Composable
fun ColorList(onColorClick: (Color) -> Unit) {
    val colors = listOf(
        Color(0xFFFFFFFF), // White
        Color(0xFFF0F8FF), // Alice Blue
        Color(0xFFADD8E6), // Light Blue
        Color(0xFF87CEEB), // Sky Blue
        Color(0xFF00BFFF), // Deep Sky Blue
        Color(0xFF4682B4), // Steel Blue
        Color(0xFF4169E1), // Royal Blue
        Color(0xFF0000FF), // Blue
        Color(0xFF0000CD), // Medium Blue
        Color(0xFF00008B), // Dark Blue
        Color(0xFF000080), // Navy
        Color(0xFF00FFFF), // Aqua
        Color(0xFF00CED1), // Dark Turquoise
        Color(0xFF20B2AA), // Light Sea Green
        Color(0xFF008080), // Teal
        Color(0xFF00FF00), // Lime
        Color(0xFF90EE90), // Light Green
        Color(0xFF32CD32), // Lime Green
        Color(0xFF228B22), // Forest Green
        Color(0xFF008000), // Green
        Color(0xFF006400), // Dark Green
        Color(0xFFADFF2F), // Green Yellow
        Color(0xFFFFD700), // Gold
        Color(0xFFFFA500), // Orange
        Color(0xFFFF8C00), // Dark Orange
        Color(0xFFFF4500), // Orange Red
        Color(0xFFFF6347), // Tomato
        Color(0xFFFF7F50), // Coral
        Color(0xFFFFC0CB), // Pink
        Color(0xFFFF69B4), // Hot Pink
        Color(0xFFFF1493), // Deep Pink
        Color(0xFFFF0000), // Red
        Color(0xFFDC143C), // Crimson
        Color(0xFFB22222), // Firebrick
        Color(0xFF8B0000), // Dark Red
        Color(0xFF800080), // Purple
        Color(0xFF9370DB), // Medium Purple
        Color(0xFF8A2BE2), // Blue Violet
        Color(0xFF4B0082), // Indigo
        Color(0xFFA52A2A), // Brown
        Color(0xFFDEB887), // Burlywood
        Color(0xFFD2691E), // Chocolate
        Color(0xFFCD853F), // Peru
        Color(0xFFF4A460), // Sandy Brown
        Color(0xFF808080), // Gray
        Color(0xFFA9A9A9), // Dark Gray
        Color(0xFF696969), // Dim Gray
        Color(0xFF000000), // Black
        Color(0xFFC0C0C0), // Silver
    )
    LazyRow(modifier = Modifier.padding(10.dp)) {
        items(colors) { color ->
            Box(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clickable {
                        onColorClick(color)
                    }
                    .size(40.dp)
                    .background(color, CircleShape)
            )
        }
    }
}

@Composable
fun CustomSlider(onChange: (Float) -> Unit, onCapClick: (StrokeCap) -> Unit) {
    var position by remember {
        mutableFloatStateOf(0.05f)
    }

    Row(
        modifier = Modifier
            .padding(start = 10.dp)
            .fillMaxHeight(0.4F),
    ) {
        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.surfaceTint),
            onClick = {
                onCapClick(StrokeCap.Round)
            }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_circle_24),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.surfaceTint),
            onClick = {
                onCapClick(StrokeCap.Square)
            }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_square_24),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.surfaceTint),
            onClick = {
                onCapClick(StrokeCap.Butt)
            }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_format_align_justify_24),
                contentDescription = null
            )
        }
        Column(
            modifier = Modifier
                .weight(0.50f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Slider(
                colors = SliderDefaults.colors(thumbColor = Color.Black),
                value = position,
                onValueChange = {
                    val tempPosition = if (it > 0) it else 0.01f
                    position = tempPosition
                    onChange(tempPosition * 100)
                }
            )
        }
    }
}

@Composable
fun ButtonPanel(
    onUndoClick: () -> Unit,
    onClearClick: () -> Unit,
    onAlbumClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6F),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(start = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.surfaceTint),
                onClick = {
                    onUndoClick()
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_undo_24),
                    contentDescription = null
                )
            }
            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.surfaceTint),
                onClick = {
                    onClearClick()
                }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = null
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.surfaceTint),
                onClick = {
                    onAlbumClick()
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_photo_album_24),
                    contentDescription = null
                )
            }
            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.surfaceTint),
                onClick = {
                    onSaveClick()
                }) {
                Icon(
                    Icons.Default.Done,
                    contentDescription = null
                )
            }
        }
    }
}