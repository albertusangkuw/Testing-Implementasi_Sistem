package com.tubes.emusic.api

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.tubes.emusic.entity.User
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

class SessionApi {

    companion object{
        //Shared Variable
        private var status: Boolean = false
        private var cookieStatus: Boolean = false

        public suspend fun loginUser(email: String, password: String) : Boolean{
            var url =  HTTPClientManager.host + "login"
            url = url + "?email=" + email + "&password=" + password
            HTTPClientManager.client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {
                    val result = String(responseBody)
                    if(HTTPClientManager.getStatusRequest(result)){
                        Log.d( "API","Success Login")
                        status = true
                    }else{
                        Log.d( "API","Failed Login")
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

            delay(600L)
            return  status
        }

        public suspend fun logoutUser(): Boolean{
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

        public suspend fun checkCookie() : Boolean{
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

            delay(300L)
            return cookieStatus
        }

        public suspend fun forgotPasswrod(email: String) : Boolean{
            var url =  HTTPClientManager.host + "resetpassword"
            url = url + "?email=" + email
            HTTPClientManager.client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {
                    val result = String(responseBody)
                    if(HTTPClientManager.getStatusRequest(result)){
                        Log.d( "API","Success Reset -" + result)
                        status = true
                    }else{
                        Log.d( "API","Failed Reset")
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

            delay(600L)
            return  status
        }

        public suspend fun signwithGoogle(newUser: GoogleSignInAccount) : Boolean{
            //Try to login first if user already have account
            if(loginUser(newUser.email!!, newUser.id!!)){
                return true
            }
            delay(500L)

            var url =  HTTPClientManager.host + "registrasi"
            val params = RequestParams()
            params.put("username", newUser.givenName)
            params.put("email", newUser.email)
            params.put("password", newUser.id)
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
                        status = true
                    } else {
                        Log.d("API", "Failed Insert User")
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