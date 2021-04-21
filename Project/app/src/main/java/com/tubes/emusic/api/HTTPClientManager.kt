package com.tubes.emusic.api

import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpClient
import org.json.JSONObject

class HTTPClientManager {
    companion object {
        val host: String = "http://18.140.59.14:8081/"
        val client =  AsyncHttpClient()
        val gson = Gson()
        public fun getStatusRequest(result: String) : Boolean{
            try {
                val responseObject = JSONObject(result)
                val status = responseObject.getString("status"   )
                if(status == "200"){
                    return true
                }
            }catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

    }
}
