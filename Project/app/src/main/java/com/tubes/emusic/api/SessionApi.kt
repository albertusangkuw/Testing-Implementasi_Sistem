package com.tubes.emusic.api

import android.util.Log
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.delay

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

            delay(1000L)
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

            delay(1000L)
            return cookieStatus
        }

    }
}