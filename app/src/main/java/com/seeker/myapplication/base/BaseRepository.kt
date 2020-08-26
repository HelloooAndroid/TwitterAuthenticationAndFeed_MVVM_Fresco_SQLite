package com.seeker.myapplication.base

import android.app.Application
import retrofit2.Response


open class BaseRepository(private var app: Application) {
    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, errorMessage: String): Result<T>? {
        val result: Result<T> =  safeApiResult(call, errorMessage)
        return result
    }
    private suspend fun <T : Any> safeApiResult(call: suspend () -> Response<T>, errorMessage: String ): Result<T> {
        val response = call.invoke()
        if (response.isSuccessful)
            return Result.Success(response.body()!!)
        return Result.Error(errorMessage)
    }
}

