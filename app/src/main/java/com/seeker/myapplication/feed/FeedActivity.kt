package com.seeker.myapplication.feed

import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.core.ImageTranscoderType
import com.facebook.imagepipeline.core.MemoryChunkType
import com.seeker.myapplication.R
import com.seeker.myapplication.TweetApp
import com.seeker.myapplication.model.TweetModel
import com.seeker.myapplication.model.UserModel
import com.seeker.myapplication.offline.TweetFeedOfflineViewModel
import com.seeker.myapplication.utils.PreferenceHelper
import com.seeker.myapplication.utils.PreferenceHelper.screenName
import com.seeker.myapplication.utils.PreferenceHelper.userId
import com.seeker.myapplication.utils.PreferenceHelper.userName
import com.seeker.myapplication.utils.isNetworkAvailable
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.activity_splash_screenn.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONException


class FeedActivity : AppCompatActivity() {

    var mFeedList: ArrayList<TweetModel>? = null
    var mAdapter: FeedAdapter? = null
    private lateinit var feedViewModel: FeedViewModel
    private lateinit var tweetOfflineViewModel: TweetFeedOfflineViewModel
    private var user: UserModel? = null
    private var isHardRefresh: Boolean? = false
    private var isLoading: Boolean? = true

    var m_IsNetworkAvailable: Boolean = false
    var offlineecords = 0


    /*Connection obsrver, where we will get network conection updates*/
    private val connectionCheck_Observer =
        Observer<Boolean> {
            if(!it){
                //internet Not available
                m_IsNetworkAvailable = false
                no_internet_indicator?.visibility=View.VISIBLE
            }else{
                //internet available
                /*When internet connection restored, Load new feed */
                m_IsNetworkAvailable = true
                no_internet_indicator?.visibility=View.GONE
                loadData()
            }
        }


    /*feed observer, Feed data will be received here and then stored in SQLite*/
    private val feedResponseLD_Observer =
        Observer<com.seeker.myapplication.base.Result<ArrayList<TweetModel>>> {
            when (it) {
                is com.seeker.myapplication.base.Result.Success -> {

                    contentLoadingProgress.visibility = View.GONE

                    if (isHardRefresh!!) {
                        tweetOfflineViewModel.deleteAllTweets()
                        mFeedList?.clear()
                        mAdapter?.notifyDataSetChanged()
                        swipe_container.isRefreshing = false
                        isHardRefresh = false
                    }

                    if (it.data.isNullOrEmpty()) {
                        //Do Nothing for now
                    } else {
                        var oldListValue = mFeedList?.size
                        //update list and adapter
                        mFeedList?.addAll(it.data)
                        mAdapter?.notifyItemRangeInserted(oldListValue!!, mFeedList?.size!!)
                        tweetOfflineViewModel.insertTweets(it.data)

                        Handler().postDelayed(Runnable {
                            tweetOfflineViewModel.getTweets()
                        }, 3000)
                    }
                    isLoading = false
                }
                is com.seeker.myapplication.base.Result.Error -> {
                    swipe_container.isRefreshing = false
                    Toast.makeText(this@FeedActivity, "" + it.exception, Toast.LENGTH_SHORT).show()
                    isLoading = false
                }
            }
        }

    /**/
    private val offlineFeedResponseLD_Observer =
        Observer<ArrayList<TweetModel>> {

            contentLoadingProgress.visibility = View.GONE

            if (isHardRefresh!!) {
                mFeedList?.clear()
                mAdapter?.notifyDataSetChanged()
                swipe_container.isRefreshing = false
                isHardRefresh = false
            }

            if (it.isNullOrEmpty()) {
                //Do Nothing for now
            } else {
                var oldListValue = mFeedList?.size
                //update list and adapter
                mFeedList?.addAll(it)
                mAdapter?.notifyItemRangeInserted(oldListValue!!, mFeedList?.size!!)
            }
            isLoading = false

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        getUserData()
        initView()
        initViewModel()
        loadData()
    }

    private fun getUserData() {
        /*If we need to show some detail on UI(Toolbar)*/
        //user = intent.getSerializableExtra("UserData") as UserModel?

        val prefs = PreferenceHelper.customPreference(this, PreferenceHelper.USER_PREFERENCE)
        user = UserModel(prefs.userId, prefs.userName!!, prefs.screenName!!)

        tool_username.text=user?.name
        tool_userId.text="@"+user?.screenName
    }


    private fun initView() {
        mFeedList = ArrayList()
        mAdapter = FeedAdapter(mFeedList!!, this)
        Fresco.initialize(
            applicationContext,
            ImagePipelineConfig.newBuilder(applicationContext)
                .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                .experiment().setNativeCodeDisabled(true)
                .build()
        )


        feed_rv?.apply {
            layoutManager = LinearLayoutManager(this@FeedActivity)
            adapter = mAdapter
        }
        swipe_container.setOnRefreshListener {
            loadData()
        }

        feed_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @Override
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                var lastCompletelyVisibleItemPosition = 0
                lastCompletelyVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition()

                try {
                    if (lastCompletelyVisibleItemPosition == mAdapter?.itemCount!! - 1) {
                        if (!isLoading!!) {
                            isLoading = true

                            if (m_IsNetworkAvailable) {
                                contentLoadingProgress.visibility = View.VISIBLE
                                feedViewModel.getFeed(mFeedList!!.last().id - 1)
                            } else {
                                tweetOfflineViewModel?.allTweets_Count.let {
                                    offlineecords = it.value!!
                                }

                                if (offlineecords > mFeedList?.size!!) {
                                    contentLoadingProgress.visibility = View.VISIBLE
                                    tweetOfflineViewModel.getTweets()
                                }
                            }


                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            @Override
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

        })
    }

    private fun loadData() {
        if (m_IsNetworkAvailable) {
            isHardRefresh = true
            feedViewModel.getFeed(null)
        } else {
            isHardRefresh = true
            tweetOfflineViewModel.getTweets()
            tweetOfflineViewModel?.getCount()
        }
    }

    private fun initViewModel() {
        feedViewModel =
            ViewModelProviders.of(this, TweetApp.applnstance?.let {
                ViewModelFeedFactory(
                    it
                )
            }).get(FeedViewModel::class.java)
        feedViewModel.feedResponseLD.observe(this, feedResponseLD_Observer)
        feedViewModel.checkConnection().observeForever(connectionCheck_Observer)


        tweetOfflineViewModel =
            ViewModelProviders.of(this, TweetApp.applnstance?.let {
                ViewModelOfflineFeedFactory(
                    it
                )
            }).get(TweetFeedOfflineViewModel::class.java)
        tweetOfflineViewModel.allTweets.observe(this, offlineFeedResponseLD_Observer)


    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }


    class ViewModelOfflineFeedFactory(var app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TweetFeedOfflineViewModel(app) as T
        }
    }

    class ViewModelFeedFactory(var app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FeedViewModel(app) as T
        }
    }
}
