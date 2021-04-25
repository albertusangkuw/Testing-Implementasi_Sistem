package com.tubes.emusic.api

import android.content.ContentValues
import android.util.Log
import com.google.gson.annotations.SerializedName
import com.loopj.android.http.AsyncHttpResponseHandler
import com.tubes.emusic.MainActivity
import com.tubes.emusic.db.DatabaseContract
import com.tubes.emusic.helper.CheckObjectDB.searchDataSong
import com.tubes.emusic.helper.MappingHelper
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.delay


data class ResponseAlbum (

        @SerializedName("status") var status : Int,
        @SerializedName("message") var message : String,
        @SerializedName("data") var data : List<AlbumData>

)

data class AlbumData (

        @SerializedName("idalbum") var idalbum : Int,
        @SerializedName("iduser") var iduser : String,
        @SerializedName("namealbum") var namealbum : String,
        @SerializedName("urlimagecover") var urlimagecover : String,
        @SerializedName("genre") var genre : String,
        @SerializedName("daterelease") var daterelease : String,
        @SerializedName("listsong") var listsong : List<Listsong>,
        @SerializedName("userfollowing") var userfollowing : List<Userfollowing>

)

class AlbumApi {
    companion object{
        //Shared Variable
        private var status: Boolean = false
        private var resultAlbum: ResponseAlbum? = null

        public suspend fun getAlbumById(id: Int) : ResponseAlbum? {
            var url =  HTTPClientManager.host + "album/" + id

            HTTPClientManager.client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {

                    val result = String(responseBody)

                    Log.d("API", "Hasil ${result}")
                    if (HTTPClientManager.getStatusRequest(result)) {
                        var data = HTTPClientManager.gson.fromJson(result.trimIndent(), ResponseAlbum::class.java)
                        resultAlbum = data
                        //Database Insert to Song
                        var listSong = data.data[0].listsong
                        for(i in listSong){
                            val values = ContentValues()
                            values.put(DatabaseContract.SongDB.ID, i.idsong)
                            values.put(DatabaseContract.SongDB.IDALBUM, i.idalbum)
                            values.put(DatabaseContract.SongDB.GENRE, i.genre)
                            values.put(DatabaseContract.SongDB.TITLE, i.title)
                            values.put(DatabaseContract.SongDB.URLSONGS, i.urlsongs)
                            if(searchDataSong(i.idsong)){
                                MainActivity.db?.insert(values)
                            }else{
                                MainActivity.db?.update(i.idsong.toString(), values)
                            }
                        }
                        Log.d("API", "Success Get Album by id" )
                        status = true
                    } else {
                        Log.d("API", "Failed Get Album by id")
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
            /*
            if(searchDataSong(id)){
                delay(500L)
            }else{
                Log.d("DB", "Skip " + id )
            }

             */
            delay(500L)
            return resultAlbum
        }
        public suspend fun searchAlbumByName(nameAlbum: String) : ResponseAlbum? {
            var url =  HTTPClientManager.host + "album?namealbum=" + nameAlbum

            HTTPClientManager.client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {

                    val result = String(responseBody)

                    Log.d("API", "Hasil ${result}")
                    if (HTTPClientManager.getStatusRequest(result)) {
                        var data = HTTPClientManager.gson.fromJson(result.trimIndent(), ResponseAlbum::class.java)
                        resultAlbum = data
                        Log.d("API", "Success Search Album " )
                        status = true
                    } else {
                        Log.d("API", "Failed Search Album ")
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
            return resultAlbum
        }

        public suspend fun addFollowingAlbum(id: Int, idUser :String): Boolean{
            var url =  HTTPClientManager.host + "album/"+ id + "/likes/" + idUser
            HTTPClientManager.client.post(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {
                    val result = String(responseBody)
                    Log.d("API", "Hasil ${result}")
                    if(HTTPClientManager.getStatusRequest(result)){
                        Log.d( "API","Success Add 1 Followers Album")
                        status = true
                    }else{
                        Log.d( "API","Failed Add 1 Followers Album")
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
        public suspend fun deleteFollowingAlbum(id: Int, idUser :String): Boolean{
            var url =  HTTPClientManager.host + "album/"+ id + "/likes/" + idUser + "/remove"
            HTTPClientManager.client.delete(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {
                    val result = String(responseBody)
                    Log.d("API", "Hasil ${result}")
                    if(HTTPClientManager.getStatusRequest(result)){
                        Log.d( "API","Success Delete 1 Followers Album")
                        status = true
                    }else{
                        Log.d( "API","Failed Delete 1 Followers Album")
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

    }
}