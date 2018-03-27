package com.github.ttaf.gpsphotogallery.util

import android.database.Cursor

fun <T> Cursor.map(action: (Cursor) -> T): List<T> {
    val result = ArrayList<T>()

    if(this.moveToFirst()){
        while (!this.isAfterLast) {
            result += action(this)
            this.moveToNext()
        }
    }

    return result
}

fun Cursor.getString(columnName: String): String = this.getString(this.getColumnIndex(columnName))

fun Cursor.getLong(columnName: String): Long = this.getLong(this.getColumnIndex(columnName))

fun Cursor.getDouble(columnName: String): Double = this.getDouble(this.getColumnIndex(columnName))
