package com.example.mobiletestzemogamiguelpuentes.repositories

import android.app.Application
import com.example.mobiletestzemogamiguelpuentes.application.AppPosts
import com.example.mobiletestzemogamiguelpuentes.model.Post
import com.example.mobiletestzemogamiguelpuentes.model.db.DataPost
import com.example.mobiletestzemogamiguelpuentes.services.requests.RequestAllPosts
import com.example.mobiletestzemogamiguelpuentes.services.requests.RequestPostDetail
import com.example.mobiletestzemogamiguelpuentes.services.responses.ResponseAllPosts
import com.example.mobiletestzemogamiguelpuentes.services.responses.ResponsePostDetail
import com.example.mobiletestzemogamiguelpuentes.tools.Constants
import com.example.mobiletestzemogamiguelpuentes.tools.Utils
import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostsRepository(private val application: Application) {

    fun fetchStoreAllPosts(
        requestAllPosts: RequestAllPosts,
        listener: OnListenerResponsePosts
    ) {
        Utils().printLog("Request: " + Gson().toJson(requestAllPosts))
        val call: Call<List<Post>> =
            AppPosts.apiServices.requestAllPosts(
            )
        call.enqueue(object : Callback<List<Post>> {
            override fun onResponse(
                call: Call<List<Post>>,
                response: Response<List<Post>>
            ) {
                val responseObj: List<Post> = response.body()!!
                Utils().printLog(responseObj.toString())
                Utils().printLog("Response: " + Gson().toJson(responseObj))

                if (response.code() == Constants.SUCCESS_STATUS && Utils().isNetworkAvailbale(
                        application
                    )
                ) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val list = ArrayList<DataPost>()
                        responseObj.forEach {
                            val dataPost = DataPost()
                            dataPost.postId = it.id
                            dataPost.userId = it.userId
                            dataPost.title = it.title
                            dataPost.body = it.body
                            dataPost.favorite = false
                            list.add(dataPost)
                        }
                        AppPosts(application).dataPostDao().deleteAll()
                        val insert = AppPosts(application).dataPostDao().insertAll(list)
                        Utils().printLog("Inserting in Posts DB $insert")
                        listener.onSuccessFetchStorePosts()
                    }
                } else {
                    listener.onFailedFetchStorePosts()
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Utils().printLog(call.toString())
                Utils().printLog(t.message.toString())
                listener.onFailedFetchStorePosts()
            }
        })
    }

    fun getCachePosts(
        listener: OnListenerResponseCache
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = AppPosts(application).dataPostDao().getAll()
            listener.onCacheAllPosts(ResponseAllPosts(list))
        }
    }

    fun getFavoriteCachePosts(
        listener: OnListenerResponseFavoriteCache
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = AppPosts(application).dataPostDao().getAll().filter { it.favorite }
            listener.onCacheFavoritePosts(ResponseAllPosts(list))
        }
    }

    fun requestPostDetail(
        postId: Int,
        requestPostDetail: RequestPostDetail,
        listener: OnListenerResponsePostDetail
    ) {
        Utils().printLog("Request: " + Gson().toJson(requestPostDetail))
        val call: Call<Post> =
            AppPosts.apiServices.requestPostDetail(
                postId
            )
        call.enqueue(object : Callback<Post> {
            override fun onResponse(
                call: Call<Post>,
                response: Response<Post>
            ) {

                val responseObj: Post = response.body()!!
                Utils().printLog(responseObj.toString())
                Utils().printLog("Response: " + Gson().toJson(responseObj))

                if (response.code() == Constants.SUCCESS_STATUS) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val detail = AppPosts(application).dataPostDao().findById(postId)
                        listener.onSuccessPostDetail(ResponsePostDetail(detail))
                    }
                } else {
                    listener.onFailedPostDetail()
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                Utils().printLog(call.toString())
                Utils().printLog(t.message.toString())
                listener.onFailedPostDetail()
            }
        })
    }

    fun switchPostFavorite(
        postId: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val post = AppPosts(application).dataPostDao().findById(postId)
            post.favorite = !post.favorite
            AppPosts(application).dataPostDao().update(post)
        }
    }

    fun deletePost(
        postId: Int, listener: OnListenerResponseDeletePost
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val post = AppPosts(application).dataPostDao().findById(postId)
            if (post != null) {
                AppPosts(application).dataPostDao().delete(post)
                listener.onResponseDeletePost()
            }
        }
    }

    fun deleteAllPosts(
        listener: OnListenerResponseDeleteAll
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            AppPosts(application).dataPostDao().deleteAll()
            listener.onResponseDeleteAll(ResponseAllPosts(ArrayList()))
        }
    }

    fun deleteFavoritePosts(
        listener: OnListenerResponseDeleteAllFavorites
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val filtered = AppPosts(application).dataPostDao().getAll().filter { it.favorite }
            filtered.forEach {
                AppPosts(application).dataPostDao().delete(it)
            }
            listener.onResponseDeleteAllFavorites(ResponseAllPosts(ArrayList()))
        }
    }

    /**
     * Listener interface for responding all posts service status.
     */
    interface OnListenerResponseDeletePost {
        fun onResponseDeletePost()
    }

    /**
     * Listener interface for responding all posts service status.
     */
    interface OnListenerResponseDeleteAllFavorites {
        fun onResponseDeleteAllFavorites(responseAllPosts: ResponseAllPosts)
    }

    /**
     * Listener interface for responding all posts service status.
     */
    interface OnListenerResponseDeleteAll {
        fun onResponseDeleteAll(responseAllPosts: ResponseAllPosts)
    }

    /**
     * Listener interface for responding all posts service status.
     */
    interface OnListenerResponseCache {
        fun onCacheAllPosts(responseAllPosts: ResponseAllPosts)
    }

    /**
     * Listener interface for responding all posts service status.
     */
    interface OnListenerResponseFavoriteCache {
        fun onCacheFavoritePosts(responseAllPosts: ResponseAllPosts)
    }

    /**
     * Listener interface for responding all posts service status.
     */
    interface OnListenerResponsePosts {
        fun onSuccessFetchStorePosts()
        fun onFailedFetchStorePosts()
    }


    /**
     * Listener interface for responding post detail service status.
     */
    interface OnListenerResponsePostDetail {
        fun onSuccessPostDetail(responsePostDetail: ResponsePostDetail)
        fun onFailedPostDetail()
    }

    companion object {
        @Volatile
        private var instance: PostsRepository? = null
        private val LOCK = Any()
        fun getInstance(context: Application) = instance ?: synchronized(LOCK) {
            instance ?: PostsRepository(context).also { instance = it }
        }
    }
}