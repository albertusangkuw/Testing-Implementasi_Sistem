package com.tubes.emusic.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.tubes.emusic.db.DatabaseContract.SongDB.Companion.TABLE_NAME

 class DatabaseContract {
     class AlbumDB : BaseColumns {
         companion object {
             const val TABLE_NAME = "album"
             const val IDALBUM = "idalbum"
             const val DATERELEASE = "daterelease"
             const val NAMEALBUM = "namealbum"
             const val URLIMAGECOVER = "urlimagecover"
             const val GENRE = "genre"
             const val IDUSER = "iduser"
         }
     }

     class AlbumFollowingDB : BaseColumns {
         companion object {
             const val TABLE_NAME = "album_following"
             const val ID = "id"
             const val IDALBUM = "idalbum"
             const val IDUSER = "iduser"
         }
     }

     class ArtistDB : BaseColumns {
         companion object {
             const val TABLE_NAME = "artist"
             const val IDUSER = "iduser"
             const val BIO = "bio"
         }
     }

     class PlaylistDB : BaseColumns {
         companion object {
             const val TABLE_NAME = "playlist"
             const val IDPLAYLIST = "idplaylist"
             const val DATECREATED = "datecreated"
             const val IDUSER = "iduser"
             const val NAMEPLAYLIST = "nameplaylist"
             const val URLIMAGECOVER = "urlimagecover"
         }
     }

     class PlaylistFollowingDB : BaseColumns {
         companion object {
             const val TABLE_NAME = "playlist_following"
             const val ID = "id"
             const val IDPLAYLIST = "idplaylist"
             const val IDUSER = "iduser"
         }
     }

     class PlaylistSongDB : BaseColumns {
         companion object {
             const val TABLE_NAME = "playlist_song"
             const val ID = "id"
             const val IDSONG = "idsong"
             const val IDPLAYLIST = "idplaylist"
         }
     }

     class RegularUserDB : BaseColumns {
         companion object {
             const val TABLE_NAME = "regular_user"
             const val DATEOFBIRTH = "dateofbirth"
             const val GENDER = "gender"
             const val IDUSER = "iduser"
         }
     }

     class SongDB : BaseColumns {
         companion object {
             const val TABLE_NAME = "song"
             const val ID = "idsong"
             const val IDALBUM = "idalbum"
             const val TITLE = "title"
             const val URLSONGS = "urlsongs"
             const val GENRE = "genre"
         }
     }

     class SongLikeDB : BaseColumns {
         companion object {
             const val TABLE_NAME = "song_like"
             const val ID = "id"
             const val IDUSER = "iduser"
             const val IDSONG = "idsong"
         }
     }

     class UserDB : BaseColumns {
         companion object {
             const val TABLE_NAME = "user"
             const val IDUSER = "iduser"
             const val USERNAME = "username"
             const val EMAIL = "email"
             const val PASSWORD = "password"
             const val COUNTRY = "country"
             const val URLPHOTOPROFILE = "urlphotoprofile"
             const val DATEJOIN = "datejoin"
             const val CATEGORIES = "categories"
         }
     }

     class UserFollowingDB : BaseColumns {
         companion object {
             const val TABLE_NAME = "user_following"
             const val ID = "id"
             const val USERID = "userid"
             const val FOLLOWINGUSERID = "followinguserid"
         }
     }

     class UserHistoryDB : BaseColumns {
         companion object {
             const val TABLE_NAME = "user_history"
             const val ID = "id"
             const val IDUSER = "id_user"
             const val IDLIST = "id_list"
             const val TYPE = "type"
             const val DATE = "date"
         }
     }

     class DatabaseHelper(context: Context) :  SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
         companion object {
             private const val DATABASE_NAME = "dbemusic"
             private const val DATABASE_VERSION = 1
             private val SQL_CREATE_TABLE_ALBUM =  "CREATE TABLE $TABLE_NAME" +
                     " (${DatabaseContract.AlbumDB.IDALBUM} INTEGER  PRIMARY KEY ," +
                     " ${DatabaseContract.AlbumDB.DATERELEASE} DATE ," +
                     " ${DatabaseContract.AlbumDB.NAMEALBUM} STRING ," +
                     " ${DatabaseContract.AlbumDB.URLIMAGECOVER} TEXT ," +
                     " ${DatabaseContract.AlbumDB.GENRE} STRING ," +
                     " ${DatabaseContract.AlbumDB.IDUSER} CHARACTER )"
             private val SQL_CREATE_TABLE_ALBUM_FOLLOWING =  "CREATE TABLE $TABLE_NAME" +
                     " (${DatabaseContract.AlbumFollowingDB.ID} INTEGER  PRIMARY KEY ," +
                     " ${DatabaseContract.AlbumFollowingDB.IDALBUM} INTEGER ," +
                     " ${DatabaseContract.AlbumFollowingDB.IDUSER} CHARACTER )"
             private val SQL_CREATE_TABLE_ARTIST =  "CREATE TABLE $TABLE_NAME" +
                     " (${DatabaseContract.ArtistDB.IDUSER} CHARACTER  PRIMARY KEY ," +
                     " ${DatabaseContract.ArtistDB.BIO} TEXT )"
             private val SQL_CREATE_TABLE_PLAYLIST =  "CREATE TABLE $TABLE_NAME" +
                     " (${DatabaseContract.PlaylistDB.IDPLAYLIST} INTEGER  PRIMARY KEY ," +
                     " ${DatabaseContract.PlaylistDB.DATECREATED} DATETIME ," +
                     " ${DatabaseContract.PlaylistDB.IDUSER} CHARACTER ," +
                     " ${DatabaseContract.PlaylistDB.NAMEPLAYLIST} STRING ," +
                     " ${DatabaseContract.PlaylistDB.URLIMAGECOVER} TEXT )"
             private val SQL_CREATE_TABLE_PLAYLIST_FOLLOWING =  "CREATE TABLE $TABLE_NAME" +
                     " (${DatabaseContract.PlaylistFollowingDB.ID} INTEGER  PRIMARY KEY ," +
                     " ${DatabaseContract.PlaylistFollowingDB.IDPLAYLIST} INTEGER ," +
                     " ${DatabaseContract.PlaylistFollowingDB.IDUSER} CHARACTER )"
             private val SQL_CREATE_TABLE_PLAYLIST_SONG =  "CREATE TABLE $TABLE_NAME" +
                     " (${DatabaseContract.PlaylistSongDB.ID} INTEGER  PRIMARY KEY ," +
                     " ${DatabaseContract.PlaylistSongDB.IDSONG} INTEGER ," +
                     " ${DatabaseContract.PlaylistSongDB.IDPLAYLIST} INTEGER )"
             private val SQL_CREATE_TABLE_REGULAR_USER =  "CREATE TABLE $TABLE_NAME" +
                     " ${DatabaseContract.RegularUserDB.DATEOFBIRTH} DATE ," +
                     " ${DatabaseContract.RegularUserDB.GENDER} CHARACTER ," +
                     " (${DatabaseContract.RegularUserDB.IDUSER} CHARACTER  PRIMARY KEY )"
             private val SQL_CREATE_TABLE_SONG =  "CREATE TABLE $TABLE_NAME" +
                     " (${DatabaseContract.SongDB.ID} INTEGER  PRIMARY KEY ," +
                     " ${DatabaseContract.SongDB.IDALBUM} INTEGER ," +
                     " ${DatabaseContract.SongDB.TITLE} TEXT ," +
                     " ${DatabaseContract.SongDB.URLSONGS} TEXT ," +
                     " ${DatabaseContract.SongDB.GENRE} TEXT )"
             private val SQL_CREATE_TABLE_SONG_LIKE =  "CREATE TABLE $TABLE_NAME" +
                     " (${DatabaseContract.SongLikeDB.ID} INTEGER  PRIMARY KEY ," +
                     " ${DatabaseContract.SongLikeDB.IDUSER} CHARACTER ," +
                     " ${DatabaseContract.SongLikeDB.IDSONG} INTEGER )"
             private val SQL_CREATE_TABLE_USER =  "CREATE TABLE $TABLE_NAME" +
                     " (${DatabaseContract.UserDB.IDUSER} CHARACTER PRIMARY KEY ," +
                     " ${DatabaseContract.UserDB.USERNAME} STRING ," +
                     " ${DatabaseContract.UserDB.EMAIL} STRING ," +
                     " ${DatabaseContract.UserDB.PASSWORD} CHARACTER ," +
                     " ${DatabaseContract.UserDB.COUNTRY} STRING ," +
                     " ${DatabaseContract.UserDB.URLPHOTOPROFILE} TEXT ," +
                     " ${DatabaseContract.UserDB.DATEJOIN} DATE ," +
                     " ${DatabaseContract.UserDB.CATEGORIES} INTEGER )"
             private val SQL_CREATE_TABLE_USER_FOLLOWING =  "CREATE TABLE $TABLE_NAME" +
                     " (${DatabaseContract.UserFollowingDB.ID} INTEGER PRIMARY KEY ," +
                     " ${DatabaseContract.UserFollowingDB.USERID} CHARACTER ," +
                     " ${DatabaseContract.UserFollowingDB.FOLLOWINGUSERID} CHARACTER )"
             private val SQL_CREATE_TABLE_USER_HISTORY =  "CREATE TABLE $TABLE_NAME" +
                     " (${DatabaseContract.UserHistoryDB.ID} INTEGER PRIMARY KEY ," +
                     " ${DatabaseContract.UserHistoryDB.IDUSER} CHARACTER ," +
                     " ${DatabaseContract.UserHistoryDB.IDLIST} INTEGER ," +
                     " ${DatabaseContract.UserHistoryDB.TYPE} INTEGER ," +                                                         " ${DatabaseContract.UserHistoryDB.IDLIST} INTEGER ," +
                     " ${DatabaseContract.UserHistoryDB.DATE} TIMESTAMP )"
         }
         override fun onCreate(db: SQLiteDatabase) {
             db.execSQL(SQL_CREATE_TABLE_ALBUM)
             db.execSQL(SQL_CREATE_TABLE_ALBUM_FOLLOWING)
             db.execSQL(SQL_CREATE_TABLE_ARTIST)
             db.execSQL(SQL_CREATE_TABLE_PLAYLIST)
             db.execSQL(SQL_CREATE_TABLE_PLAYLIST_FOLLOWING)
             db.execSQL(SQL_CREATE_TABLE_PLAYLIST_SONG)
             db.execSQL(SQL_CREATE_TABLE_REGULAR_USER)
             db.execSQL(SQL_CREATE_TABLE_SONG)
             db.execSQL(SQL_CREATE_TABLE_SONG_LIKE)
             db.execSQL(SQL_CREATE_TABLE_USER)
             db.execSQL(SQL_CREATE_TABLE_USER_FOLLOWING)
             db.execSQL(SQL_CREATE_TABLE_USER_HISTORY)
         }
         override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
         }
     }
}