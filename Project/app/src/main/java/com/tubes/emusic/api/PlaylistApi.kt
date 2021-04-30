package com.tubes.emusic.api

import android.content.ContentValues
import android.util.Log
import com.google.gson.annotations.SerializedName
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.tubes.emusic.MainActivity
import com.tubes.emusic.db.DatabaseContract
import com.tubes.emusic.entity.Playlist
import com.tubes.emusic.helper.CheckObjectDB.searchDataPlaylist
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class ResponsePlaylist (

        @SerializedName("status") var status : Int,
        @SerializedName("message") var message : String,
        @SerializedName("data") var data : List<PlaylistData>

)

data class Userfollowing (

        @SerializedName("iduser") var iduser : String,
        @SerializedName("username") var username : String?,
        @SerializedName("email") var email : String?,
        @SerializedName("country") var country : String?,
        @SerializedName("urlphotoprofile") var urlphotoprofile : String?,
        @SerializedName("datejoin") var datejoin : String?,
        @SerializedName("categories") var categories : Int?

)
data class PlaylistData (

        @SerializedName("idplaylist") var idplaylist : Int,
        @SerializedName("iduser") var iduser : String,
        @SerializedName("nameplaylist") var nameplaylist : String,
        @SerializedName("urlimagecover") var urlimagecover : String,
        @SerializedName("datecreated") var datecreated : String,
        @SerializedName("listsong") var listsong : List<MusicData>?,
        @SerializedName("userfollowing") var userfollowing : List<Userfollowing>?

)
class PlaylistApi {
    companion object{
        //Shared Variable
        private var status: Boolean = false
        private var resultPlaylist: ResponsePlaylist? = null

        public suspend fun getPlaylistById(id: Int) : ResponsePlaylist? {
            var url =  HTTPClientManager.host + "playlist/" + id

            HTTPClientManager.client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {

                    val result = String(responseBody)

                    Log.d("API", "Hasil ${result}")
                    if (HTTPClientManager.getStatusRequest(result)) {

                        var data = HTTPClientManager.gson.fromJson(result.trimIndent(), ResponsePlaylist::class.java)
                        resultPlaylist = data
                        Log.d("API", "Success Get Playlist by id" )
                        status = true
                        insertUpdatePlaylistDB(data.data)
                    } else {
                        Log.d("API", "Failed Get Playlist by id")
                        status = false
                    }
                }

                override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                    // Jika koneksi gagal

                    val errorMessage = when (statusCode) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode : ${error.message}"
                    }
                    Log.d("Failure", errorMessage)

                }

                // ----New Overridden method
                override fun getUseSynchronousMode(): Boolean {
                    return false
                }
            })

            delay(500L)
            return resultPlaylist
        }

        public suspend fun insertPlaylist(playlist: Playlist) : Boolean{
            var url =  HTTPClientManager.host + "playlist"
            val params = RequestParams()
            params.put("iduser", playlist.iduser)
            params.put("namePlaylist", playlist.namePlaylist)
            params.put("urlImageCover", playlist.urlImageCover)
            params.put("dateCreated",  SimpleDateFormat("yyyy-MM-dd").format(Date()).toString())

            HTTPClientManager.client.post(url, params, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {
                    val result = String(responseBody)
                    Log.d("API", "Hasil ${result}")
                    if (HTTPClientManager.getStatusRequest(result)) {
                        Log.d("API", "Success Insert Playlist")
                        status = true
                    } else {
                        Log.d("API", "Failed Insert Playlist")
                        status = false
                    }
                }

                override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                    // Jika koneksi gagal
                    status = false
                    val errorMessage = when (statusCode) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode : ${error.message}"
                    }
                    Log.d("Failure", errorMessage)
                }

                // ----New Overridden method
                override fun getUseSynchronousMode(): Boolean {
                    return false
                }

            })
            delay(500L)
            return status
        }

        public suspend fun deletePlaylist(id: Int): Boolean{
            var url =  HTTPClientManager.host + "playlist/"+ id + "/remove"
            HTTPClientManager.client.delete(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {
                    val result = String(responseBody)
                    Log.d("API", "Hasil ${result}")
                    if(HTTPClientManager.getStatusRequest(result)){
                        Log.d( "API","Success Delete Playlist")
                        status = true
                    }else{
                        Log.d( "API","Failed Delete Playlist")
                        status = false
                    }
                }
                override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                    // Jika koneksi gagal
                    status = false
                    val errorMessage = when (statusCode) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode : ${error.message}"
                    }
                    Log.d("Failure", errorMessage)
                }

                // ----New Overridden method
                override fun getUseSynchronousMode(): Boolean {
                    return false
                }

            })

            delay(500L)
            return status
        }

        public suspend fun addSongPlaylist(id: Int,idMusic :Int): Boolean{
            var url =  HTTPClientManager.host + "playlist/"+ id + "/music/" + idMusic
            HTTPClientManager.client.post(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {
                    val result = String(responseBody)
                    Log.d("API", "Hasil ${result}")
                    if(HTTPClientManager.getStatusRequest(result)){
                        Log.d( "API","Success Add 1 Music Playlist")
                        status = true
                    }else{
                        Log.d( "API","Failed Add 1 Music Playlist")
                        status = false
                    }
                }
                override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                    // Jika koneksi gagal
                    status= false
                    val errorMessage = when (statusCode) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode : ${error.message}"
                    }
                    Log.d("Failure", errorMessage)
                }

                // ----New Overridden method
                override fun getUseSynchronousMode(): Boolean {
                    return false
                }

            })

            delay(500L)
            return status
        }
        public suspend fun deleteSongPlaylist(id: Int,idMusic :Int): Boolean{
            var url =  HTTPClientManager.host + "playlist/"+ id + "/music/" + idMusic + "/remove"
            HTTPClientManager.client.delete(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {
                    val result = String(responseBody)
                    Log.d("API", "Hasil ${result}")
                    if(HTTPClientManager.getStatusRequest(result)){
                        Log.d( "API","Success Delete 1 Music Playlist")
                        status = true
                    }else{
                        Log.d( "API","Failed Delete 1 Music Playlist")
                        status = false
                    }
                }
                override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                    // Jika koneksi gagal
                    status= false
                    val errorMessage = when (statusCode) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode : ${error.message}"
                    }
                    Log.d("Failure", errorMessage)
                }

                // ----New Overridden method
                override fun getUseSynchronousMode(): Boolean {
                    return false
                }

            })

            delay(500L)
            return status
        }

        public suspend fun addFollowingPlaylist(id: Int, idUser :String): Boolean{
            var url =  HTTPClientManager.host + "playlist/"+ id + "/following/" + idUser
            HTTPClientManager.client.post(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {
                    val result = String(responseBody)
                    Log.d("API", "Hasil ${result}")
                    if(HTTPClientManager.getStatusRequest(result)){
                        Log.d( "API","Success Add 1 Followers Music Playlist")
                        status = true
                    }else{
                        Log.d( "API","Failed Add 1 Followers Music Playlist")
                        status = false
                    }
                }
                override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                    // Jika koneksi gagal
                    status= false
                    val errorMessage = when (statusCode) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode : ${error.message}"
                    }
                    Log.d("Failure", errorMessage)
                }

                // ----New Overridden method
                override fun getUseSynchronousMode(): Boolean {
                    return false
                }

            })

            delay(500L)
            return status
        }
        public suspend fun deleteFollowingPlaylist(id: Int, idUser :String): Boolean{
            var url =  HTTPClientManager.host + "playlist/"+ id + "/following/" + idUser + "/remove"
            HTTPClientManager.client.delete(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {
                    val result = String(responseBody)
                    Log.d("API", "Hasil ${result}")
                    if(HTTPClientManager.getStatusRequest(result)){
                        Log.d( "API","Success Delete 1 Followers Playlist")
                        status = true
                    }else{
                        Log.d( "API","Failed Delete 1 Followers Playlist")
                        status = false
                    }
                }
                override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                    // Jika koneksi gagal
                    status= false
                    val errorMessage = when (statusCode) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode : ${error.message}"
                    }
                    Log.d("Failure", errorMessage)
                }

                // ----New Overridden method
                override fun getUseSynchronousMode(): Boolean {
                    return false
                }

            })

            delay(500L)
            return status
        }

        fun insertUpdatePlaylistDB(data: List<PlaylistData>){
            for(i in data){
                val valuesPlaylist = ContentValues()
                valuesPlaylist.put(DatabaseContract.PlaylistDB.DATECREATED, i.datecreated)
                valuesPlaylist.put(DatabaseContract.PlaylistDB.ID, i.idplaylist)
                valuesPlaylist.put(DatabaseContract.PlaylistDB.IDUSER, i.iduser)
                valuesPlaylist.put(DatabaseContract.PlaylistDB.NAMEPLAYLIST, i.nameplaylist)
                valuesPlaylist.put(DatabaseContract.PlaylistDB.URLIMAGECOVER, i.urlimagecover)

                val valuesFollowing =  ArrayList<ContentValues>()
                if(i.userfollowing != null) {
                    for (j in i.userfollowing!!) {
                        val tempValues = ContentValues()
                        tempValues.put(DatabaseContract.PlaylistFollowingDB.IDUSER, j.iduser)
                        tempValues.put(DatabaseContract.PlaylistFollowingDB.IDPLAYLIST, i.idplaylist)
                        valuesFollowing.add(tempValues)
                    }
                }
                val valuesListSong = ArrayList<ContentValues>()
                if( i.listsong != null) {
                    for (j in i.listsong!!) {
                        val tempValues = ContentValues()
                        tempValues.put(DatabaseContract.PlaylistSongDB.IDSONG, j.idsong)
                        tempValues.put(DatabaseContract.PlaylistSongDB.IDPLAYLIST, i.idplaylist)
                        valuesListSong.add(tempValues)
                    }
                }

                Log.d("API", "Success Playlist Insert Update db " + valuesListSong  )
                if( searchDataPlaylist(i.idplaylist)){
                    MainActivity.db?.insert(valuesPlaylist, DatabaseContract.PlaylistDB.TABLE_NAME)
                    if(!valuesFollowing.isEmpty()) {
                        for(i in valuesFollowing) {
                            MainActivity.db?.insert(i, DatabaseContract.PlaylistFollowingDB.TABLE_NAME)
                        }
                    }
                    if(!valuesListSong.isEmpty()) {
                        for(i in valuesListSong) {
                            MainActivity.db?.insert(i, DatabaseContract.PlaylistSongDB.TABLE_NAME)
                        }
                    }
                }else{
                    MainActivity.db?.update(i.idplaylist.toString(), valuesPlaylist , DatabaseContract.PlaylistDB.TABLE_NAME)
                    if(!valuesFollowing.isEmpty()) {
                        MainActivity.db?.deleteCustomById(
                                i.idplaylist.toString(),
                                DatabaseContract.PlaylistFollowingDB.IDPLAYLIST,
                                DatabaseContract.PlaylistFollowingDB.TABLE_NAME
                        )
                        for(i in valuesFollowing) {
                            MainActivity.db?.insert(i, DatabaseContract.PlaylistFollowingDB.TABLE_NAME)
                        }
                    }
                    if(!valuesListSong.isEmpty()) {
                        MainActivity.db?.deleteCustomById(
                                i.idplaylist.toString(),
                                DatabaseContract.PlaylistSongDB.IDPLAYLIST,
                                DatabaseContract.PlaylistSongDB.TABLE_NAME
                        )
                        for(i in valuesListSong) {
                            MainActivity.db?.insert(i, DatabaseContract.PlaylistSongDB.TABLE_NAME)
                        }
                    }
                }
            }
        }

    }

}