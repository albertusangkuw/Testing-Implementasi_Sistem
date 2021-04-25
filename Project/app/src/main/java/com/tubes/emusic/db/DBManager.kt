package com.tubes.emusic.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.tubes.emusic.db.DatabaseContract.SongDB.Companion.TABLE_NAME

class DBManager(context: Context) {
    companion object {
        public var DATABASE_TABLE = TABLE_NAME
        private lateinit var dataBaseHelper: DatabaseContract.DatabaseHelper
        private lateinit var database: SQLiteDatabase
        private var INSTANCE:DBManager? = null
        fun getInstance(context: Context): DBManager = INSTANCE ?: synchronized(this) {
            INSTANCE ?: DBManager(context)
        }
    }

    init {
        dataBaseHelper = DatabaseContract.DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "${BaseColumns._ID} ASC")
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "${BaseColumns._ID} = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "${BaseColumns._ID} = ?",
            arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "${BaseColumns._ID} = '$id'", null)
    }
}