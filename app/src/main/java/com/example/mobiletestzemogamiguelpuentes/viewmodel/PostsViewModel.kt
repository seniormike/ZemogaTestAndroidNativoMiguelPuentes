package com.example.mobiletestzemogamiguelpuentes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mobiletestzemogamiguelpuentes.model.db.DataPost
import com.example.mobiletestzemogamiguelpuentes.repositories.PostsRepository
import com.example.mobiletestzemogamiguelpuentes.services.requests.RequestAllPosts
import com.example.mobiletestzemogamiguelpuentes.services.requests.RequestPostDetail
import com.example.mobiletestzemogamiguelpuentes.services.responses.ResponseAllPosts
import com.example.mobiletestzemogamiguelpuentes.services.responses.ResponsePostDetail
import com.example.mobiletestzemogamiguelpuentes.tools.Utils

class PostsViewModel(application: Application) : AndroidViewModel(application) {

    private val postsRepository = PostsRepository.getInstance(application)

    /**
     * LiveData properties of posts list.
     */
    private var _postsList = MutableLiveData<List<DataPost>>()
    var postsList: LiveData<List<DataPost>> = _postsList

    /**
     * LiveData properties of posts list.
     */
    private var _favoritePostsList = MutableLiveData<List<DataPost>>()
    var favoritePostsList: LiveData<List<DataPost>> = _favoritePostsList

    /**
     * LiveData properties of detail post.
     */
    private var _post = MutableLiveData<DataPost>()
    var post: LiveData<DataPost> = _post

    /**
     * LiveData properties of artists list.
     */
    private var _dialogMsg = MutableLiveData<String>()
    var dialogMsg: LiveData<String> = _dialogMsg

    /**
     * Method that calls top artists service.
     */
    fun requestFetchStorePosts(
        request: RequestAllPosts,
    ) {
        if (Utils().isNetworkAvailbale(getApplication())) {
            postsRepository.fetchStoreAllPosts(request,
                object : PostsRepository.OnListenerResponsePosts {
                    override fun onSuccessFetchStorePosts() {
                        getCachePosts()
                    }

                    override fun onFailedFetchStorePosts() {
                        Utils().printLog("On failure")
                        _dialogMsg.postValue("Oops! Check your connection and try again.")
                    }
                })
        } else {
            _dialogMsg.postValue("Oops! Check your connection and try again.")
        }
    }


    /**
     * Method that calls top artists service.
     */
    fun getCachePosts(
    ) {
        postsRepository.getCachePosts(
            object : PostsRepository.OnListenerResponseCache {
                override fun onCacheAllPosts(responseAllPosts: ResponseAllPosts) {
                    _postsList.postValue(responseAllPosts.posts)
                    _dialogMsg.postValue("")
                }
            })
    }

    /**
     * Method that calls top artists service.
     */
    fun getFavoriteCachePosts(
    ) {
        postsRepository.getFavoriteCachePosts(
            object : PostsRepository.OnListenerResponseFavoriteCache {
                override fun onCacheFavoritePosts(responseAllPosts: ResponseAllPosts) {
                    _favoritePostsList.postValue(responseAllPosts.posts)
                    _dialogMsg.postValue("")
                }
            })
    }

    /**
     * Method that calls top artists service.
     */
    fun requestPostDetail(
        postId: Int,
        request: RequestPostDetail,
    ) {
        postsRepository.requestPostDetail(
            postId,
            request,
            object : PostsRepository.OnListenerResponsePostDetail {
                override fun onSuccessPostDetail(responsePostDetail: ResponsePostDetail) {
                    _post.postValue(responsePostDetail.post)
                }

                override fun onFailedPostDetail() {
                    Utils().printLog("On failure")
                }
            })
    }

    /**
     * Method that sets favorite value to specific post.
     */
    fun switchPostFavorite(
        postId: Int
    ) {
        postsRepository.switchPostFavorite(
            postId
        )
        requestPostDetail(postId, RequestPostDetail())
    }

    /**
     * Method that sets favorite value to specific post.
     */
    fun deleteAllPosts(
    ) {
        postsRepository.deleteAllPosts(
            object : PostsRepository.OnListenerResponseDeleteAll {
                override fun onResponseDeleteAll(responseAllPosts: ResponseAllPosts) {
                    _postsList.postValue(responseAllPosts.posts)
                    _dialogMsg.postValue("")
                }
            }
        )
    }

    /**
     * Method that sets favorite value to specific post.
     */
    fun deletePost(
        postId: Int
    ) {
        postsRepository.deletePost(
            postId, object : PostsRepository.OnListenerResponseDeletePost {
                override fun onResponseDeletePost() {
                    getCachePosts()
                    getFavoriteCachePosts()
                }
            }
        )
    }

    /**
     * Method that sets favorite value to specific post.
     */
    fun deleteAllFavoritePosts(
    ) {
        postsRepository.deleteFavoritePosts(
            object : PostsRepository.OnListenerResponseDeleteAllFavorites {
                override fun onResponseDeleteAllFavorites(responseAllPosts: ResponseAllPosts) {
                    _favoritePostsList.postValue(responseAllPosts.posts)
                }
            }
        )
    }

}