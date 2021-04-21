package com.tubes.emusic.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
/*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

 */
import com.loopj.android.http.AsyncHttpResponseHandler
import com.tubes.emusic.entity.User
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONObject


//Generator Class JSON https://json2kt.com/
data class ResponseUser (

    @SerializedName("status") var status : Int,
    @SerializedName("message") var message : String,
    @SerializedName("artist") var artist : List<Artist>,
    @SerializedName("regularuser") var regularuser : List<Regularuser>

)

data class Artist (

    @SerializedName("iduser") var iduser : String,
    @SerializedName("username") var username : String,
    @SerializedName("email") var email : String,
    @SerializedName("country") var country : String,
    @SerializedName("urlphotoprofile") var urlphotoprofile : String,
    @SerializedName("datejoin") var datejoin : String,
    @SerializedName("categories") var categories : Int,
    @SerializedName("bio") var bio : String

)


data class Regularuser (

    @SerializedName("iduser") var iduser : String,
    @SerializedName("username") var username : String,
    @SerializedName("email") var email : String,
    @SerializedName("country") var country : String,
    @SerializedName("urlphotoprofile") var urlphotoprofile : String,
    @SerializedName("datejoin") var datejoin : String,
    @SerializedName("categories") var categories : Int,
    @SerializedName("gender") var gender : String,
    @SerializedName("dateofbirth") var dateofbirth : String

)

class UserApi {
    companion object{
        //Shared Variable
        private var user : User? = null
        private var status: Boolean = false
        private var cookieStatus: Boolean = false

        public suspend fun getSingleUser(email: String) : User? {
            var url =  HTTPClientManager.host + "users?email=${email}"

            HTTPClientManager.client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {

                    val result = String(responseBody)

                    Log.d( "API","hasil ${result}" )
                    if(HTTPClientManager.getStatusRequest(result)){

                        var mUser = HTTPClientManager.gson.fromJson(result.trimIndent(), ResponseUser::class.java)

                        for( i in mUser.regularuser){
                            user = User(i.iduser,
                                i.username,
                                i.email,
                                i.country,
                                i.urlphotoprofile,
                                i.datejoin,
                                2)
                            break
                        }
                        Log.d( "API","Success Get Single User with ID " + user!!.iduser)
                        status = true
                    }else{
                        Log.d( "API","Failed Get Single User")
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


        /*
        public suspend fun searchUser(username: String): User{
            var url =  HTTPClientManager.host + "logout"
            HTTPClientManager.client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {
                    val result = String(responseBody)
                    if(HTTPClientManager.getStatusRequest(result)){
                        Log.d( "API","Succes Logout")
                        cookieStatus = true
                    }else{
                        Log.d( "API","Failed Logout")
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

        public suspend fun insertUser(newUser : User) : Boolean{
            var url =  HTTPClientManager.host + "checkcookie"
            HTTPClientManager.client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {
                    val result = String(responseBody)
                    if(HTTPClientManager.getStatusRequest(result)){
                        Log.d( "API","Cookie Valid")
                        cookieStatus = true
                    }else{
                        Log.d( "API","Cookie Not Valid")
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
*/
        //public suspend fun updateUser(newUser : User): Boolean

        //public suspend fun addFollowingUser(iduser: String, idfollowing:String)

        //public suspend fun deleteFollowingUser(iduser: String, idfollowing:String)

    }
}