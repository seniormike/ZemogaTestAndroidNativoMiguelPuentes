package com.example.mobiletestzemogamiguelpuentes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mobiletestzemogamiguelpuentes.model.User
import com.example.mobiletestzemogamiguelpuentes.repositories.UserRepository
import com.example.mobiletestzemogamiguelpuentes.services.requests.RequestUserDetail
import com.example.mobiletestzemogamiguelpuentes.services.responses.ResponseUserDetail
import com.example.mobiletestzemogamiguelpuentes.tools.Utils

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository.getInstance(application)

    /**
     * LiveData properties of artists list.
     */
    private var _dialogMsg = MutableLiveData<String>()
    var dialogMsg: LiveData<String> = _dialogMsg

    /**
     * LiveData properties of user.
     */
    private var _user = MutableLiveData<User>()
    var user: LiveData<User> = _user

    /**
     * Method that calls top artists service.
     */
    fun requestUserDetail(
        userId: Int,
        request: RequestUserDetail,
    ) {
        userRepository.requestUserDetail(
            userId,
            request,
            object : UserRepository.OnListenerResponseUserDetail {

                override fun onResponseUserDetail(responseUserDetail: ResponseUserDetail) {
                    _user.value = responseUserDetail.user
                }

                override fun onFailedUserDetail() {
                    Utils().printLog("On failure")
                    _dialogMsg.postValue("Oops! Revisa tu conexión e intenta de nuevo.")
                }
            })
    }
}