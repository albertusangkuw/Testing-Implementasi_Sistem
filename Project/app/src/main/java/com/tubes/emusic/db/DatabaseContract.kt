package com.tubes.emusic.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.tubes.emusic.db.DatabaseContract.SongDB.Companion.TABLE_NAME

 class DatabaseContract {
     class AlbumDB : BaseColumns {
         companion object {
             const val TABLE_NAME  = "album"
             const val ID= "_id"
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
             const val ID= "_id"
             const val BIO = "bio"
         }
     }

     class PlaylistDB : BaseColumns {
         companion object {
             const val TABLE_NAME = "playlist"
             const val ID= "_id"
             const val DATECREATED = "datecreated"
             const val IDUSER = "iduser"
             const val NAMEPLAYLIST = "nameplaylist"
             const val URLIMAGECOVER = "urlimagecover"
         }
     }

     class PlaylistFollowingDB : BaseColumns {
         companion object {
             const val TABLE_NAME = "playlist_following"
             const val ID= "_id"
             const val IDPLAYLIST = "idplaylist"
             const val IDUSER = "iduser"
         }
     }

     class PlaylistSongDB : BaseColumns {
         companion object {
             const val TABLE_NAME = "playlist_song"
             const val ID= "_id"
             const val IDSONG = "idsong"
             const val IDPLAYLIST = "idplaylist"
         }
     }

     class RegularUserDB : BaseColumns {
         companion object {
             const val TABLE_NAME = "regular_user"
             const val DATEOFBIRTH = "dateofbirth"
             const val GENDER = "gender"
             const val ID= "_id"
         }
     }

     class SongDB : BaseColumns {
         companion object {
             const val TABLE_NAME = "song"
             const val ID= "_id"
             const val IDALBUM = "idalbum"
             const val TITLE = "title"
             const val URLSONGS = "urlsongs"
             const val GENRE = "genre"
         }
     }

     class SongLikeDB : BaseColumns {
         companion object {
             const val TABLE_NAME = "song_like"
             const val ID= "_id"
             const val IDUSER = "iduser"
             const val IDSONG = "idsong"
         }
     }

     class UserDB : BaseColumns {
         companion object {
             const val TABLE_NAME = "user"
             const val ID= "_id"
             const val USERNAME = "username"
             const val EMAIL = "email"
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
             private val SQL_CREATE_TABLE_ALBUM =  "CREATE TABLE " + DatabaseContract.AlbumDB.TABLE_NAME+
                     " (${DatabaseContract.AlbumDB.ID} INTEGER  PRIMARY KEY ," +
                     " ${DatabaseContract.AlbumDB.DATERELEASE} TEXT ," +
                     " ${DatabaseContract.AlbumDB.NAMEALBUM} TEXT ," +
                     " ${DatabaseContract.AlbumDB.URLIMAGECOVER} TEXT ," +
                     " ${DatabaseContract.AlbumDB.GENRE} TEXT ," +
                     " ${DatabaseContract.AlbumDB.IDUSER} TEXT )"
             private val SQL_CREATE_TABLE_ALBUM_FOLLOWING =  "CREATE TABLE " + DatabaseContract.AlbumFollowingDB.TABLE_NAME+
                     " (${DatabaseContract.AlbumFollowingDB.ID} INTEGER  PRIMARY KEY ," +
                     " ${DatabaseContract.AlbumFollowingDB.IDALBUM} INTEGER ," +
                     " ${DatabaseContract.AlbumFollowingDB.IDUSER} TEXT )"
             private val SQL_CREATE_TABLE_ARTIST =  "CREATE TABLE " + DatabaseContract.ArtistDB.TABLE_NAME+
                     " (${DatabaseContract.ArtistDB.ID} TEXT  PRIMARY KEY ," +
                     " ${DatabaseContract.ArtistDB.BIO} TEXT )"
             private val SQL_CREATE_TABLE_PLAYLIST =  "CREATE TABLE " + DatabaseContract.PlaylistDB.TABLE_NAME+
                     " (${DatabaseContract.PlaylistDB.ID} INTEGER  PRIMARY KEY ," +
                     " ${DatabaseContract.PlaylistDB.DATECREATED} TEXT ," +
                     " ${DatabaseContract.PlaylistDB.IDUSER} TEXT ," +
                     " ${DatabaseContract.PlaylistDB.NAMEPLAYLIST} TEXT ," +
                     " ${DatabaseContract.PlaylistDB.URLIMAGECOVER} TEXT )"
             private val SQL_CREATE_TABLE_PLAYLIST_FOLLOWING =  "CREATE TABLE " + DatabaseContract.PlaylistFollowingDB.TABLE_NAME+
                     " (${DatabaseContract.PlaylistFollowingDB.ID} INTEGER  PRIMARY KEY ," +
                     " ${DatabaseContract.PlaylistFollowingDB.IDPLAYLIST} INTEGER ," +
                     " ${DatabaseContract.PlaylistFollowingDB.IDUSER} TEXT )"
             private val SQL_CREATE_TABLE_PLAYLIST_SONG =  "CREATE TABLE " + DatabaseContract.PlaylistSongDB.TABLE_NAME+
                     " (${DatabaseContract.PlaylistSongDB.ID} INTEGER  PRIMARY KEY ," +
                     " ${DatabaseContract.PlaylistSongDB.IDSONG} INTEGER ," +
                     " ${DatabaseContract.PlaylistSongDB.IDPLAYLIST} INTEGER )"
             private val SQL_CREATE_TABLE_REGULAR_USER =  "CREATE TABLE "  + DatabaseContract.RegularUserDB.TABLE_NAME+
                     " (${DatabaseContract.RegularUserDB.DATEOFBIRTH} TEXT ," +
                     " ${DatabaseContract.RegularUserDB.GENDER} TEXT ," +
                     " ${DatabaseContract.RegularUserDB.ID} TEXT  PRIMARY KEY )"
             private val SQL_CREATE_TABLE_SONG =  "CREATE TABLE " + DatabaseContract.SongDB.TABLE_NAME+
                     " (${DatabaseContract.SongDB.ID} INTEGER  PRIMARY KEY ," +
                     " ${DatabaseContract.SongDB.IDALBUM} INTEGER ," +
                     " ${DatabaseContract.SongDB.TITLE} TEXT ," +
                     " ${DatabaseContract.SongDB.URLSONGS} TEXT ," +
                     " ${DatabaseContract.SongDB.GENRE} TEXT )"
             private val SQL_CREATE_TABLE_SONG_LIKE =  "CREATE TABLE "+ DatabaseContract.SongLikeDB.TABLE_NAME+
                     " (${DatabaseContract.SongLikeDB.ID} INTEGER  PRIMARY KEY ," +
                     " ${DatabaseContract.SongLikeDB.IDUSER} TEXT ," +
                     " ${DatabaseContract.SongLikeDB.IDSONG} INTEGER )"
             private val SQL_CREATE_TABLE_USER =  "CREATE TABLE " + DatabaseContract.UserDB.TABLE_NAME +
                     " (${DatabaseContract.UserDB.ID} TEXT PRIMARY KEY ," +
                     " ${DatabaseContract.UserDB.USERNAME} TEXT ," +
                     " ${DatabaseContract.UserDB.EMAIL} TEXT ," +
                     " ${DatabaseContract.UserDB.COUNTRY} TEXT ," +
                     " ${DatabaseContract.UserDB.URLPHOTOPROFILE} TEXT ," +
                     " ${DatabaseContract.UserDB.DATEJOIN} TEXT ," +
                     " ${DatabaseContract.UserDB.CATEGORIES} INTEGER )"
             private val SQL_CREATE_TABLE_USER_FOLLOWING =  "CREATE TABLE "  + DatabaseContract.UserFollowingDB.TABLE_NAME +
                     " (${DatabaseContract.UserFollowingDB.ID} INTEGER PRIMARY KEY ," +
                     " ${DatabaseContract.UserFollowingDB.USERID} TEXT ," +
                     " ${DatabaseContract.UserFollowingDB.FOLLOWINGUSERID} TEXT )"
             private val SQL_CREATE_TABLE_USER_HISTORY =  "CREATE TABLE "  + DatabaseContract.UserHistoryDB.TABLE_NAME +
                     " (${DatabaseContract.UserHistoryDB.ID} INTEGER PRIMARY KEY ," +
                     " ${DatabaseContract.UserHistoryDB.IDUSER} TEXT ," +
                     " ${DatabaseContract.UserHistoryDB.IDLIST} INTEGER ," +
                     " ${DatabaseContract.UserHistoryDB.TYPE} INTEGER ," +
                     " ${DatabaseContract.UserHistoryDB.DATE} TEXT )"
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