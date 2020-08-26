package com.seeker.myapplication.offline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.seeker.myapplication.model.TweetModel


@Dao
interface TweetDao{

    @Query("SELECT * from TimelineTweets ORDER BY Id DESC")
    fun getTweetsById(): List<TweetModel>

    @Query("SELECT COUNT(id) FROM TimelineTweets")
    fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tweet: TweetModel)

    @Query("DELETE FROM TimelineTweets")
    suspend fun deleteAll()
}