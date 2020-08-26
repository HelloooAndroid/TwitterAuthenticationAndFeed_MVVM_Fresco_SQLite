package com.seeker.myapplication.feed

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.seeker.myapplication.base.Result
import com.seeker.myapplication.model.TweetModel
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import retrofit2.Call

class FeedRepo() /*: BaseRepository(app)*/ {

    var feedResponseLD = MutableLiveData<Result<ArrayList<TweetModel>>>()


    fun getFeed(lastId:Long?) {
        try {
            val twitterApiClient = TwitterCore.getInstance().apiClient

            /*get Users Home timeline*/
            val call2: Call<MutableList<Tweet>>? = twitterApiClient.statusesService.homeTimeline(
                10,
                null,
                lastId,
                false,
                true,
                false,
                true
            )

            call2?.enqueue(object : Callback<MutableList<Tweet>>() {
                override fun success(result: com.twitter.sdk.android.core.Result<MutableList<Tweet>>?) {
                    /*save data to list*/
                    Log.d("Success: ", "" + result?.data?.size)

                    var mFeedList: ArrayList<TweetModel>? = ArrayList()

                    /*large data handled in Background thread*/
                    for (i in result?.data?.orEmpty()!!) {
                        var mediaUrl = ""
                        if (!i.entities.media.isNullOrEmpty() && i.entities.media.size>0) {
                            mediaUrl = i.entities.media.get(0).mediaUrlHttps
                        }
                        mFeedList?.add(
                            TweetModel(
                                i.id,
                                i.text,
                                i.retweetCount,
                                i.retweeted,
                                i.favoriteCount,
                                i.favorited,
                                i.user.name,
                                i.user.screenName,
                                i.user.profileImageUrlHttps,
                                i.user.verified,
                                mediaUrl
                            )
                        )
                    }

                    feedResponseLD.postValue(Result.Success(mFeedList!!))


                    /*save data to Sqlite Background thread*/


                }

                override fun failure(exception: TwitterException?) {
                    feedResponseLD.postValue(Result.Error(exception?.message!!))
                    Log.d("Failed :", "" + exception?.message)

                }

            })
        } catch (e: Exception) {
        }
    }
}

