package com.seeker.myapplication.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.seeker.myapplication.base.Result
import com.seeker.myapplication.model.TweetModel
import com.seeker.myapplication.utils.ConnectionLiveData
import java.lang.ref.WeakReference

class FeedViewModel(private var app: Application) : AndroidViewModel(app) {
    var feedResponseLD = MutableLiveData<Result<ArrayList<TweetModel>>>()

    private val feedRepo = FeedRepo()

    init {
        feedResponseLD = feedRepo.feedResponseLD
    }

    internal fun getFeed(lastId: Long?) {
        feedRepo.getFeed(lastId)
    }

    private val connectionLD = ConnectionLiveData(WeakReference(app))
    fun checkConnection(): LiveData<Boolean> = connectionLD

}