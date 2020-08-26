package com.seeker.myapplication.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.seeker.myapplication.base.Result
import com.seeker.myapplication.model.UserModel

class UserViewModel(private var app: Application) : AndroidViewModel(app) {
    var userInfoResponseLD = MutableLiveData<Result<UserModel>>()

    private val userRepo = UserRepo(/*TweetFactory.tweetApi, app*/)

    init {
        userInfoResponseLD = userRepo.userInfoResponseLD
    }

    internal fun getUserInfo(){
        userRepo.getUserInfo()
    }
}