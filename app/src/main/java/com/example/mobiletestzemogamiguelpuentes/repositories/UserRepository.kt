package com.example.mobiletestzemogamiguelpuentes.repositories

import android.app.Application
import com.example.mobiletestzemogamiguelpuentes.application.AppPosts
import com.example.mobiletestzemogamiguelpuentes.model.User
import com.example.mobiletestzemogamiguelpuentes.services.requests.RequestUserDetail
import com.example.mobiletestzemogamiguelpuentes.services.responses.ResponseUserDetail
import com.example.mobiletestzemogamiguelpuentes.tools.Constants
import com.example.mobiletestzemogamiguelpuentes.tools.Utils
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val application: Application) {

    fun requestUserDetail(
        userId: Int,
        requestUserDetail: RequestUserDetail,
        listener: OnListenerResponseUserDetail
    ) {
        Utils().printLog("Request: " + Gson().toJson(requestUserDetail))
        val call: Call<User> =
            AppPosts.apiServices.requestUserDetail(
                userId
            )
        call.enqueue(object : Callback<User> {
            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                val responseObj: User = response.body()!!
                Utils().printLog(responseObj.toString())
                Utils().printLog("Response: " + Gson().toJson(responseObj))

                if (response.code() == Constants.SUCCESS_STATUS) {
                    listener.onResponseUserDetail(ResponseUserDetail(responseObj))
                } else {
                    listener.onFailedUserDetail()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Utils().printLog(call.toString())
                Utils().printLog(t.message.toString())
                listener.onFailedUserDetail()
            }
        })
    }

    /**
     * Listener interface for responding user detail information service status.
     */
    interface OnListenerResponseUserDetail {
        fun onResponseUserDetail(responseUserDetail: ResponseUserDetail)
        fun onFailedUserDetail()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        private val LOCK = Any()
        fun getInstance(context: Application) = instance ?: synchronized(LOCK) {
            instance ?: UserRepository(context).also { instance = it }
        }
    }
}