package com.example.mydrawapp.canvasCore

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.runtime.snapshots.SnapshotStateList
import android.graphics.Color
import android.widget.EditText
import android.widget.Toast
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.toArgb
import com.example.mydrawapp.dataBase.DatabaseHelper
import com.example.mydrawapp.dataBase.insertImageToDatabase
import com.example.mydrawapp.dataClasses.PathData

fun saveCanvasToBitmap(pathList: SnapshotStateList<PathData>, context: Context): Bitmap {
    val width = 1080
    val height = 1780
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    var newCap: Paint.Cap
    canvas.drawColor(Color.WHITE)


    pathList.forEach { pathData ->
        newCap = when (pathData.cap) {
            StrokeCap.Round -> {
                Paint.Cap.ROUND

            }

            StrokeCap.Square -> {
                Paint.Cap.SQUARE
            }

            else -> {
                Paint.Cap.BUTT
            }
        }
        canvas.drawPath(
            pathData.path.asAndroidPath(),
            Paint().apply {
                color = pathData.color.toArgb()
                style = Paint.Style.STROKE
                strokeWidth = pathData.lineWidth
                strokeCap = newCap
            }
        )
    }

    saveBitmapToFile(bitmap, context)

    return bitmap
}

fun saveBitmapToFile(bitmap: Bitmap, context: Context) {
    val editText = EditText(context).apply {
        hint = "Введите имя изображения"
    }

    val dialog = AlertDialog.Builder(context)
        .setTitle("Сохранить изображение")
        .setMessage("Введите имя файла для сохранения:")
        .setView(editText)
        .setPositiveButton("Сохранить") { _, _ ->
            val fileName = editText.text.toString().trim()

            if (fileName.isNotEmpty()) {
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.png")
                    put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                val uri = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )

                uri?.let {
                    context.contentResolver.openOutputStream(it).use { outputStream ->
                        if (outputStream != null) {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                            val dbHelper = DatabaseHelper(context)
                            val database = dbHelper.writableDatabase
                            val id = insertImageToDatabase(database, fileName, uri.toString())
                            Toast.makeText(context, "Файл сохранен", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(context, "Имя файла не может быть пустым", Toast.LENGTH_SHORT).show()
            }
        }
        .setNegativeButton("Отмена") { dialogInterface, _ -> dialogInterface.dismiss() }
        .create()

    dialog.show()
}







