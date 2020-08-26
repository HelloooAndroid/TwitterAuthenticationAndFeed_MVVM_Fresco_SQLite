package com.seeker.myapplication.offline

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.seeker.myapplication.model.TweetModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TweetFeedOfflineViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TweetFeedOfflineRepo

    //val allTweets: LiveData<List<TweetModel>>
    var allTweets = MutableLiveData<ArrayList<TweetModel>>()
    public var allTweets_Count = MutableLiveData<Int>()


    init {
        val wordsDao = TweetDatabase.getDatabase(application,viewModelScope).tweetDao()
        repository = TweetFeedOfflineRepo(wordsDao)
        allTweets= repository.allTweets

    }

    //Launching a new coroutine to insert the data in a non-blocking way
    fun insertTweets(tweets: List<TweetModel>) = viewModelScope.launch(Dispatchers.IO) {
        for (tweet in tweets){
            repository.insert(tweet)
        }
    }

    //Launching a new coroutine to retrive the data in a non-blocking way
    fun getTweets() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAll()
    }

    fun getCount() = viewModelScope.launch(Dispatchers.IO) {
        allTweets_Count.postValue(repository.getCount())
    }

    //Launching a new coroutine to delete the data in a non-blocking way
    fun deleteAllTweets() = viewModelScope.launch(Dispatchers.IO) {
       repository.deleteAll()
    }
}
