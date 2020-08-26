package com.seeker.myapplication.base

import com.seeker.myapplication.model.TweetModel

sealed class Result<out T: Any> {
    data class Success<out T:Any> (val data:T): Result<T>()
    data class Error(val exception: String) : Result<Nothing>()
}

