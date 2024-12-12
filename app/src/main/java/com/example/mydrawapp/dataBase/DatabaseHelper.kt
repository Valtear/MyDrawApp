package com.example.mydrawapp.dataBase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.mydrawapp.dataClasses.ImageEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "images.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_IMAGES = "images"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PATH = "path"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_IMAGES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_PATH TEXT
            );
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_IMAGES")
        onCreate(db)
    }
}

fun insertImageToDatabase(database: SQLiteDatabase, name: String, path: String): Long {
    val values = ContentValues().apply {
        put("name", name)
        put("path", path)
    }
    return database.insert("images", null, values)
}

@SuppressLint("Range")
suspend fun getAllImages(database: SQLiteDatabase): List<ImageEntry> {
    return withContext(Dispatchers.IO) {
        val images = mutableListOf<ImageEntry>()
        val cursor = database.query(
            "images",
            arrayOf("id", "name", "path"),
            null,
            null,
            null,
            null,
            "id DESC"
        )
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val path = cursor.getString(cursor.getColumnIndex("path"))
            images.add(ImageEntry(id, name, path))
        }
        cursor.close()
        images
    }
}

fun getImagePathById(database: SQLiteDatabase, imageId: Long): String {
    var imagePath = ""

    val cursor = database.query(
        "images",
        arrayOf("path"),
        "id = ?",
        arrayOf(imageId.toString()),
        null, null, null
    )

    if (cursor.moveToFirst()) {
        val columnIndex = cursor.getColumnIndex("path")
        if (columnIndex != -1) {
            imagePath = cursor.getString(columnIndex)
        } else {
            Log.e("DatabaseError", "Column 'path' not found in the database.")
        }
    }

    cursor.close()

    return imagePath
}

fun deleteImage(database: SQLiteDatabase, imageId: Long) {
    val whereClause = "id = ?"
    val whereArgs = arrayOf(imageId.toString())

    database.delete("images", whereClause, whereArgs)
}

suspend fun loadImages(database: SQLiteDatabase, images: SnapshotStateList<ImageEntry>) {
    images.clear()
    val loadedImages = getAllImages(database)
    images.addAll(loadedImages)
}




