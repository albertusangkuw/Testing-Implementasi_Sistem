package com.tubes.emusic.helper

import android.util.Log
import com.tubes.emusic.MainActivity

object CheckObjectDB {
    fun searchDataSong(id: Int) : Boolean{
        var rs =  MappingHelper.mapListsongToArrayList(MainActivity.db?.queryById(id.toString()))
        for(i in rs){
            Log.d("DB", "Stats " + i.idsong + " vs " + id )
            if(i.idsong == id){
                return false
            }
        }
        return  true
    }
}