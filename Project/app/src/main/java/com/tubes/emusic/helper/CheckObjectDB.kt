package com.tubes.emusic.helper

import android.util.Log
import com.tubes.emusic.MainActivity
import com.tubes.emusic.db.DBManager
import com.tubes.emusic.db.DatabaseContract

object CheckObjectDB {
    fun searchDataSong(id: Int) : Boolean{
        var rs =  MappingHelper.mapListsongToArrayList(MainActivity.db?.queryById(id.toString(),DatabaseContract.SongDB.TABLE_NAME))
        for(i in rs){
            Log.d("DB", "Stats Song" + i.idsong + " vs " + id )
            if(i.idsong == id){
                return false
            }
        }
        return  true
    }

    fun searchDataAlbum(id: Int) : Boolean{
        var rs =  MappingHelper.mapListAlbumToArrayList(MainActivity.db?.queryById(id.toString(),DatabaseContract.AlbumDB.TABLE_NAME))
        for(i in rs){
            Log.d("DB", "Stats Album " + i.namealbum+ " vs " + id )
            if(i.idalbum == id){
                return false
            }
        }
        return  true
    }
}