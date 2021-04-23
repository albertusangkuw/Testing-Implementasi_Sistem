package com.tubes.emusic.api

/*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

 */
import android.util.Log
import com.google.gson.annotations.SerializedName
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.tubes.emusic.entity.User
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*


//Generator Class JSON https://json2kt.com/
data class ResponseUser(

        @SerializedName("status") var status: Int,
        @SerializedName("message") var message: String,
        @SerializedName("artist") var artist: List<Artist>,
        @SerializedName("regularuser") var regularuser: List<Regularuser>

)

data class Artist(

        @SerializedName("iduser") var iduser: String,
        @SerializedName("username") var username: String,
        @SerializedName("email") var email: String,
        @SerializedName("country") var country: String,
        @SerializedName("urlphotoprofile") var urlphotoprofile: String,
        @SerializedName("datejoin") var datejoin: String,
        @SerializedName("categories") var categories: Int,
        @SerializedName("bio") var bio: String

)


data class Regularuser(

        @SerializedName("iduser") var iduser: String,
        @SerializedName("username") var username: String,
        @SerializedName("email") var email: String,
        @SerializedName("country") var country: String,
        @SerializedName("urlphotoprofile") var urlphotoprofile: String,
        @SerializedName("datejoin") var datejoin: String,
        @SerializedName("categories") var categories: Int,
        @SerializedName("gender") var gender: String,
        @SerializedName("dateofbirth") var dateofbirth: String

)

data class ResponseDetailUser (

        @SerializedName("status") var status : Int,
        @SerializedName("message") var message : String,
        @SerializedName("id") var id : String,
        @SerializedName("datafollowers") var datafollowers : List<String>,
        @SerializedName("datafollowing") var datafollowing : List<String>,
        @SerializedName("dataplaylist") var dataplaylist : List<String>,
        @SerializedName("dataalbum") var dataalbum : List<String>

)
class UserApi {
    companion object{
        //Shared Variable
        private var user : User? = null
        private var status: Boolean = false
        private var cookieStatus: Boolean = false
        private var resultUser: ResponseUser? = null
        private var resultDetailUser: ResponseDetailUser? = null

        public suspend fun getSingleUser(email: String) : User? {
            var url =  HTTPClientManager.host + "users/${email}"

            HTTPClientManager.client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {

                    val result = String(responseBody)

                    Log.d("API", "hasil ${result}")
                    if (HTTPClientManager.getStatusRequest(result)) {

                        var mUser = HTTPClientManager.gson.fromJson(result.trimIndent(), ResponseUser::class.java)

                        for (i in mUser.regularuser) {
                            user = User(i.iduser,
                                    i.username,
                                    i.email,
                                    i.country,
                                    i.urlphotoprofile,
                                    i.datejoin,
                                    2)
                            break
                        }
                        Log.d("API", "Success Get Single User with ID " + user!!.iduser)
                        status = true
                    } else {
                        Log.d("API", "Failed Get Single User")
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
            return user
        }


        public suspend fun searchUser(username: String): ResponseUser?{
            var url =  HTTPClientManager.host + "users?username="+ username
            HTTPClientManager.client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {
                    val result = String(responseBody)
                    Log.d("API", "Hasil ${result}")
                    if(HTTPClientManager.getStatusRequest(result)){
                        var mUser = HTTPClientManager.gson.fromJson(result.trimIndent(), ResponseUser::class.java)
                        resultUser = mUser
                        Log.d( "API","Success Search user")
                        cookieStatus = true
                    }else{
                        Log.d( "API","Failed search user")
                        cookieStatus = false
                    }
                }
                override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                    // Jika koneksi gagal
                    cookieStatus = false
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
            return resultUser
        }

        public suspend fun insertUser(newUser: User,password : String) : Boolean{
            var url =  HTTPClientManager.host + "registrasi"
            val params = RequestParams()
            params.put("username", newUser.email)
            params.put("email", newUser.email)
            params.put("password", password)
            params.put("country", "Indonesia")
            params.put("dateJoin",  SimpleDateFormat("yyyy-MM-dd").format(Date()).toString())
            params.put("categories", "2")
            params.put("urlphotoprofile", "")

            HTTPClientManager.client.post(url, params, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {
                    val result = String(responseBody)
                    Log.d("API", "Hasil ${result}")
                    if (HTTPClientManager.getStatusRequest(result)) {
                        Log.d("API", "Success Insert User")
                        cookieStatus = true
                    } else {
                        Log.d("API", "Failed Insert User")
                        cookieStatus = false
                    }
                }

                override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                    // Jika koneksi gagal
                    cookieStatus = false
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
            return cookieStatus
        }


        public suspend fun updateUser(user: User,password : String) : Boolean{
            var url =  HTTPClientManager.host + "users/"+ user.iduser + "/update"
            val params = RequestParams()
            params.put("username", user.username)
            params.put("email", user.email)
            params.put("password", password)
            params.put("urlphotoprofile", "")

            HTTPClientManager.client.put(url, params, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {
                    val result = String(responseBody)
                    Log.d("API", "Hasil ${result}")
                    if (HTTPClientManager.getStatusRequest(result)) {
                        Log.d("API", "Success Update User")
                        cookieStatus = true
                    } else {
                        Log.d("API", "Failed Update User")
                        cookieStatus = false
                    }
                }

                override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                    // Jika koneksi gagal
                    cookieStatus = false
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
            return cookieStatus
        }

        public suspend fun addFollowingUser(iduser: String, idfollowing:String): Boolean{
            var url =  HTTPClientManager.host + "users/"+ iduser + "/following/"+ idfollowing
            HTTPClientManager.client.post(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {
                    val result = String(responseBody)
                    Log.d("API", "Hasil ${result}")
                    if(HTTPClientManager.getStatusRequest(result)){
                        Log.d( "API","Success Following user")
                        cookieStatus = true
                    }else{
                        Log.d( "API","Failed Following user")
                        cookieStatus = false
                    }
                }
                override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                    // Jika koneksi gagal
                    cookieStatus = false
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
            return cookieStatus
        }

        public suspend fun deleteFollowingUser(iduser: String, idfollowing:String): Boolean{
            var url =  HTTPClientManager.host + "users/"+ iduser + "/following/"+ idfollowing + "/remove"
            HTTPClientManager.client.delete(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {
                    val result = String(responseBody)
                    Log.d("API", "Hasil ${result}")
                    if(HTTPClientManager.getStatusRequest(result)){
                        Log.d( "API","Success Delete Following user")
                        cookieStatus = true
                    }else{
                        Log.d( "API","Failed Delete Following user")
                        cookieStatus = false
                    }
                }
                override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                    // Jika koneksi gagal
                    cookieStatus = false
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
            return cookieStatus
        }

        public suspend fun getDetailSingleUser(iduser: String) : ResponseDetailUser? {
            var url =  HTTPClientManager.host + "users/${iduser}/detail"

            HTTPClientManager.client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {

                    val result = String(responseBody)

                    Log.d("API", "Hasil ${result}")
                    if (HTTPClientManager.getStatusRequest(result)) {
                        var mUser = HTTPClientManager.gson.fromJson(result.trimIndent(), ResponseDetailUser::class.java)
                        resultDetailUser = mUser
                        Log.d("API", "Success Get Detail Single User with ID ")
                        status = true
                    } else {
                        Log.d("API", "Failed Get  Detail Single User")
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
            return resultDetailUser
        }



    }
}