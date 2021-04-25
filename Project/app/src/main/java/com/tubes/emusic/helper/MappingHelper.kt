package com.tubes.emusic.helper

import android.database.Cursor
import com.tubes.emusic.MainActivity
import com.tubes.emusic.api.AlbumData
import com.tubes.emusic.api.Listsong
import com.tubes.emusic.api.PlaylistData
import com.tubes.emusic.api.Userfollowing
import com.tubes.emusic.db.DBManager
import com.tubes.emusic.db.DatabaseContract

object MappingHelper {
    fun mapListsongToArrayList(cursor: Cursor?): ArrayList<Listsong> {
        val list = ArrayList<Listsong>()
        cursor?.apply {
            while (moveToNext()) {
                list.add(
                    Listsong(
                        idsong = getInt(getColumnIndexOrThrow(DatabaseContract.SongDB.ID)) ,
                        idalbum =  getInt(getColumnIndexOrThrow(DatabaseContract.SongDB.IDALBUM)),
                        title = getString(getColumnIndexOrThrow(DatabaseContract.SongDB.TITLE)),
                        urlsongs = getString(getColumnIndexOrThrow(DatabaseContract.SongDB.URLSONGS))   ,
                        genre = getString(getColumnIndexOrThrow(DatabaseContract.SongDB.GENRE)),
                    )
                )
            }
        }
        return  list
    }

    fun mapListAlbumToArrayList(cursor: Cursor?): ArrayList<AlbumData> {
        val list = ArrayList<AlbumData>()
        cursor?.apply {
            while (moveToNext()) {
                list.add(
                        AlbumData(
                                idalbum = getInt(getColumnIndexOrThrow(DatabaseContract.AlbumDB.ID)) ,
                                genre =  getString(getColumnIndexOrThrow(DatabaseContract.AlbumDB.GENRE)),
                                namealbum =  getString(getColumnIndexOrThrow(DatabaseContract.AlbumDB.NAMEALBUM)),
                                iduser =  getString(getColumnIndexOrThrow(DatabaseContract.AlbumDB.IDUSER))   ,
                                urlimagecover =  getString(getColumnIndexOrThrow(DatabaseContract.AlbumDB.URLIMAGECOVER))   ,
                                daterelease =  getString(getColumnIndexOrThrow(DatabaseContract.AlbumDB.DATERELEASE)) ,
                                listsong =   mapListsongToArrayList(MainActivity.db?.queryCustomById(
                                                getString(getColumnIndexOrThrow(DatabaseContract.AlbumDB.ID)),
                                                DatabaseContract.SongDB.IDALBUM,
                                                DatabaseContract.SongDB.TABLE_NAME
                                )),
                                userfollowing = mapListAlbumFollowingToArrayList(MainActivity.db?.queryCustomById(
                                        getString(getColumnIndexOrThrow(DatabaseContract.AlbumDB.ID)),
                                        DatabaseContract.AlbumFollowingDB.IDALBUM,
                                        DatabaseContract.AlbumFollowingDB.TABLE_NAME
                                ))

                        )
                )
            }
        }
        return  list
    }


    fun mapListPlaylistSongToArrayList(cursor: Cursor?): ArrayList<PlaylistData> {
        val list = ArrayList<PlaylistData>()
        cursor?.apply {
            while (moveToNext()) {
                val dataIdSong = mapPlaylistSongToArrayList(MainActivity.db?.queryCustomById(
                        getString(getColumnIndexOrThrow(DatabaseContract.PlaylistDB.ID)),
                        DatabaseContract.PlaylistSongDB.IDPLAYLIST,
                        DatabaseContract.PlaylistSongDB.TABLE_NAME
                ))
                val listsong = ArrayList<Listsong>()
                for (i in dataIdSong){
                    listsong.add( (mapListsongToArrayList(MainActivity.db?.queryById(i.idsong.toString(),DatabaseContract.SongDB.TABLE_NAME)) ).get(0))
                }

                list.add(
                        PlaylistData(
                                idplaylist = getInt(getColumnIndexOrThrow(DatabaseContract.PlaylistDB.ID)) ,
                                iduser =  getString(getColumnIndexOrThrow(DatabaseContract.PlaylistDB.IDUSER)),
                                nameplaylist =  getString(getColumnIndexOrThrow(DatabaseContract.PlaylistDB.NAMEPLAYLIST)),
                                datecreated =   getString(getColumnIndexOrThrow(DatabaseContract.PlaylistDB.DATECREATED))   ,
                                urlimagecover =  getString(getColumnIndexOrThrow(DatabaseContract.PlaylistDB.URLIMAGECOVER))   ,
                                listsong = listsong,
                                userfollowing = mapListPlaylistFollowingToArrayList(MainActivity.db?.queryCustomById(
                                        getString(getColumnIndexOrThrow(DatabaseContract.PlaylistDB.ID)),
                                        DatabaseContract.PlaylistFollowingDB.IDPLAYLIST,
                                        DatabaseContract.PlaylistFollowingDB.TABLE_NAME
                                ) )
                        )
                )
            }
        }
        return  list
    }

    fun mapListPlaylistFollowingToArrayList(cursor: Cursor?): ArrayList<Userfollowing> {
        val list = ArrayList<Userfollowing>()
        cursor?.apply {
            while (moveToNext()) {
                list.add(
                        Userfollowing(
                                username = null ,
                                iduser =  getString(getColumnIndexOrThrow(DatabaseContract.PlaylistFollowingDB.IDUSER)),
                                email = null,
                                country =  null,
                                urlphotoprofile = null ,
                                datejoin = null,
                                categories = null
                        )
                )
            }
        }
        return  list
    }

    fun mapListAlbumFollowingToArrayList(cursor: Cursor?): ArrayList<Userfollowing> {
        val list = ArrayList<Userfollowing>()
        cursor?.apply {
            while (moveToNext()) {
                list.add(
                        Userfollowing(
                                username = null ,
                                iduser =  getString(getColumnIndexOrThrow(DatabaseContract.AlbumFollowingDB.IDUSER)),
                                email = null,
                                country =  null,
                                urlphotoprofile = null ,
                                datejoin = null,
                                categories = null
                        )
                )
            }
        }
        return  list
    }

    fun mapPlaylistSongToArrayList(cursor: Cursor?): ArrayList<Listsong> {
        val list = ArrayList<Listsong>()
        cursor?.apply {
            while (moveToNext()) {
                list.add(
                        Listsong(
                                idsong = getInt(getColumnIndexOrThrow(DatabaseContract.PlaylistSongDB.IDSONG)) ,
                                idalbum =  getInt(getColumnIndexOrThrow(DatabaseContract.PlaylistSongDB.IDPLAYLIST)),
                                "",
                                "" ,
                                "",
                        )
                )
            }
        }
        return  list
    }






}