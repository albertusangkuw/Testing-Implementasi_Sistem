package com.tubes.emusic.api

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.delay

data class ResponseMusic (
        @SerializedName("status") var status : Int,
        @SerializedName("message") var message : String,
        @SerializedName("data") var data : List<MusicData>
)

data class MusicData (
        @SerializedName("idsong") var idsong : Int,
        @SerializedName("idalbum") var idalbum : Int,
        @SerializedName("title") var title : String,
        @SerializedName("urlsongs") var urlsongs : String,
        @SerializedName("genre") var genre : String
)
class MusicApi {
    companion object {
        //Shared Variable
        private var status: Boolean = false
        private var resultMusic: ResponseMusic? = null

        public suspend fun getMusicById(id: Int): ResponseMusic? {
            var url = HTTPClientManager.host + "music/" + id

            HTTPClientManager.client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {

                    val result = String(responseBody)

                    Log.d("API", "Hasil ${result}")
                    if (HTTPClientManager.getStatusRequest(result)) {
                        var data = HTTPClientManager.gson.fromJson(result.trimIndent(), ResponseMusic::class.java)
                        resultMusic = data
                        Log.d("API", "Success Get Music by id")
                        status = true
                    } else {
                        Log.d("API", "Failed Get Music by id")
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

            delay(1000L)
            return resultMusic
        }

        public suspend fun searchMusicByName(nameMusic: String): ResponseMusic? {
            var url = HTTPClientManager.host + "music?title=" + nameMusic

            HTTPClientManager.client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {

                    val result = String(responseBody)

                    Log.d("API", "Hasil ${result}")
                    if (HTTPClientManager.getStatusRequest(result)) {
                        var data = HTTPClientManager.gson.fromJson(result.trimIndent(), ResponseMusic::class.java)
                        resultMusic = data
                        Log.d("API", "Success Search Music")
                        status = true
                    } else {
                        Log.d("API", "Failed Search Music ")
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

            delay(1000L)
            return resultMusic
        }

        public suspend fun addLikedMusic(id: Int, idUser: String): Boolean {
            var url = HTTPClientManager.host + "music/" + id + "/likes/" + idUser
            HTTPClientManager.client.post(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {
                    val result = String(responseBody)
                    Log.d("API", "Hasil ${result}")
                    if (HTTPClientManager.getStatusRequest(result)) {
                        Log.d("API", "Success Add 1 Followers Music")
                        status = true
                    } else {
                        Log.d("API", "Failed Add 1 Followers Music")
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

            delay(1000L)
            return status
        }

        public suspend fun deleteLikedMusic(id: Int, idUser: String): Boolean {
            var url = HTTPClientManager.host + "music/" + id + "/likes/" + idUser + "/remove"
            HTTPClientManager.client.delete(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {
                    val result = String(responseBody)
                    Log.d("API", "Hasil ${result}")
                    if (HTTPClientManager.getStatusRequest(result)) {
                        Log.d("API", "Success Delete 1 Followers Music")
                        status = true
                    } else {
                        Log.d("API", "Failed Delete 1 Followers Music")
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

            delay(1000L)
            return status
        }
    }

}