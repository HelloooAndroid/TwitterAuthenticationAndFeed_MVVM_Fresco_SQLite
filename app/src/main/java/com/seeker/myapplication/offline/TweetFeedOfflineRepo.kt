package com.seeker.myapplication.offline

import androidx.lifecycle.MutableLiveData
import com.seeker.myapplication.model.TweetModel
import java.util.*
import kotlin.collections.ArrayList


class TweetFeedOfflineRepo(private val tweetDao: TweetDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    //val allTweets: LiveData<List<TweetModel>> = tweetDao.getTweetsById()

    var allTweets = MutableLiveData<ArrayList<TweetModel>>()



    suspend fun insert(tweetModel: TweetModel) {
        tweetDao.insert(tweetModel)
    }

    suspend fun getAll() {
        var list = tweetDao.getTweetsById()
        allTweets.postValue(ArrayList(list))
    }
    suspend fun getCount(): Int {
        return tweetDao.getCount()
    }
    suspend fun deleteAll() {
        tweetDao.deleteAll()
    }
}