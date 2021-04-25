package com.tubes.emusic.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.tubes.emusic.db.DatabaseContract.SongDB.Companion.TABLE_NAME

 class DatabaseContract {
    class SongDB : BaseColumns {
        companion object {
            const val TABLE_NAME = "song"
            const val ID = "_id"
            const val IDUSER = "iduser"
            const val IDALBUM = "idalbum"
            const val TITLE = "title"
            const val URLSONGS = "urlsongs"
            const val GENRE = "genre"
        }
    }

    class DatabaseHelper(context: Context) :  SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
        companion object {
            private const val DATABASE_NAME = "dbemusic"
            private const val DATABASE_VERSION = 1
            private val SQL_CREATE_TABLE_SONG =  "CREATE TABLE $TABLE_NAME" +
                                                " (${DatabaseContract.SongDB.ID} INTEGER  PRIMARY KEY ," +
                                                " ${DatabaseContract.SongDB.IDALBUM} INTEGER ," +
                                                " ${DatabaseContract.SongDB.IDUSER} TEXT ," +
                                                " ${DatabaseContract.SongDB.TITLE} TEXT ," +
                                                " ${DatabaseContract.SongDB.URLSONGS} TEXT ," +
                                                " ${DatabaseContract.SongDB.GENRE} TEXT )"

        }
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_TABLE_SONG)
        }
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }
    }
}