package com.example.mobiletestzemogamiguelpuentes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mobiletestzemogamiguelpuentes.model.Comment
import com.example.mobiletestzemogamiguelpuentes.repositories.CommentsRepository
import com.example.mobiletestzemogamiguelpuentes.services.requests.RequestPostComments
import com.example.mobiletestzemogamiguelpuentes.services.responses.ResponsePostComments
import com.example.mobiletestzemogamiguelpuentes.tools.Utils

class CommentsViewModel(application: Application) : AndroidViewModel(application) {

    private val commentsRepository = CommentsRepository.getInstance(application)

    /**
     * LiveData properties of comments list.
     */
    private var _commentsList = MutableLiveData<List<Comment>>()
    var commentsList: LiveData<List<Comment>> = _commentsList

    /**
     * LiveData properties of artists list.
     */
    private var _dialogMsg = MutableLiveData<String>()
    var dialogMsg: LiveData<String> = _dialogMsg

    /**
     * Method that calls top artists service.
     */
    fun requestPostComments(
        postId: Int,
        request: RequestPostComments,
    ) {
        commentsRepository.requestPostComments(
            postId,
            request,
            object : CommentsRepository.OnListenerResponsePostComments {
                override fun onSuccessPostComments(responsePostComments: ResponsePostComments) {
                    _commentsList.value = responsePostComments.comments
                }

                override fun onFailedPostComments() {
                    Utils().printLog("On failure")
                    _dialogMsg.postValue("Oops! Revisa tu conexión e intenta de nuevo.")
                }
            })
    }
}