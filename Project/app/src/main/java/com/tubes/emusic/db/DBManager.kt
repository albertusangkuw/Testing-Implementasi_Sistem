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
        //public var DATABASE_TABLE: String = TABLE_NAME
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

    fun queryAll( table_name : String): Cursor {
        return database.query(
                table_name,
            null,
            null,
            null,
            null,
            null,
            "${BaseColumns._ID} ASC")
    }

    fun queryById(id: String, table_name : String): Cursor {
        return database.query(
                table_name,
            null,
            "${BaseColumns._ID} = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }

    fun queryCustomById(value: String,column: String, table_name : String): Cursor {
        return database.query(
                table_name,
                null,
                "${column} = ?", arrayOf(value),
                null,
                null,
                null,
                null)
    }


    fun insert(values: ContentValues?, table_name : String): Long {
        return database.insert(table_name, null, values)
    }

    fun update(id: String, values: ContentValues?, table_name : String ): Int {
        return database.update(table_name, values, "${BaseColumns._ID} = ?",
            arrayOf(id))
    }

    fun deleteById(id: String, table_name : String): Int {
        return database.delete(table_name, "${BaseColumns._ID} = '$id'", null)
    }
}