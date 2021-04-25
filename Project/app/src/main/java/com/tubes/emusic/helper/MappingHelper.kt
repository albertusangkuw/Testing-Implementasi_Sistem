package com.tubes.emusic.helper

import android.database.Cursor
import com.tubes.emusic.api.Listsong
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


}