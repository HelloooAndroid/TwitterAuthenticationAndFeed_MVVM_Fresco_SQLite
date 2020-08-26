package com.seeker.myapplication.splash

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.seeker.myapplication.base.Result
import com.seeker.myapplication.model.UserModel
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.User
import retrofit2.Call

class UserRepo()  {

    var userInfoResponseLD = MutableLiveData<Result<UserModel>>()


    fun getUserInfo() {
        try {
            val twitterApiClient = TwitterCore.getInstance().apiClient
            val call: Call<User> =
                twitterApiClient.accountService.verifyCredentials(true, false, true)
            call.enqueue(object : Callback<User?>() {

                override fun success(result: com.twitter.sdk.android.core.Result<User?>?) {
                    Log.d("Twitter_Auth: ", "" + result?.data?.screenName)


                    var user = UserModel(result?.data?.id!!,result?.data?.name!!,result?.data?.screenName!!)



                    userInfoResponseLD.postValue(Result.Success(user!!))

                }

                override fun failure(exception: TwitterException?) {
                    Log.d("Fail: ", "" + exception?.message)
                }
            })

        } catch (e: Exception) {
        }
    }
}

