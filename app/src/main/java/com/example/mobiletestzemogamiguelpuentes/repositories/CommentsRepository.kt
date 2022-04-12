package com.example.mobiletestzemogamiguelpuentes.repositories

import android.app.Application
import com.example.mobiletestzemogamiguelpuentes.application.AppPosts
import com.example.mobiletestzemogamiguelpuentes.model.Comment
import com.example.mobiletestzemogamiguelpuentes.services.requests.RequestPostComments
import com.example.mobiletestzemogamiguelpuentes.services.responses.ResponsePostComments
import com.example.mobiletestzemogamiguelpuentes.tools.Constants
import com.example.mobiletestzemogamiguelpuentes.tools.Utils
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentsRepository(private val application: Application) {

    fun requestPostComments(
        postId: Int,
        requestPostComments: RequestPostComments,
        listener: OnListenerResponsePostComments
    ) {
        Utils().printLog("Request: " + Gson().toJson(requestPostComments))
        val call: Call<List<Comment>> =
            AppPosts.apiServices.requestPostComments(
                postId
            )
        call.enqueue(object : Callback<List<Comment>> {
            override fun onResponse(
                call: Call<List<Comment>>,
                response: Response<List<Comment>>
            ) {
                val responseObj: List<Comment> = response.body()!!
                Utils().printLog(responseObj.toString())
                Utils().printLog("Response: " + Gson().toJson(responseObj))

                if (response.code() == Constants.SUCCESS_STATUS) {
                    listener.onSuccessPostComments(ResponsePostComments(responseObj))
                } else {
                    listener.onFailedPostComments()
                }
            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                Utils().printLog(call.toString())
                Utils().printLog(t.message.toString())
                listener.onFailedPostComments()
            }
        })
    }


    /**
     * Listener interface for responding post comments service status.
     */
    interface OnListenerResponsePostComments {
        fun onSuccessPostComments(responsePostComments: ResponsePostComments)
        fun onFailedPostComments()
    }

    companion object {
        @Volatile
        private var instance: CommentsRepository? = null
        private val LOCK = Any()
        fun getInstance(context: Application) = instance ?: synchronized(LOCK) {
            instance ?: CommentsRepository(context).also { instance = it }
        }
    }
}