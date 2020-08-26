package com.seeker.myapplication.splash

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.seeker.myapplication.TweetApp
import com.seeker.myapplication.base.Result
import com.seeker.myapplication.feed.FeedActivity
import com.seeker.myapplication.model.UserModel
import com.seeker.myapplication.utils.PreferenceHelper.USER_PREFERENCE
import com.seeker.myapplication.utils.PreferenceHelper.customPreference
import com.seeker.myapplication.utils.PreferenceHelper.screenName
import com.seeker.myapplication.utils.PreferenceHelper.userId
import com.seeker.myapplication.utils.PreferenceHelper.userName
import com.seeker.myapplication.utils.isNetworkAvailable
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.TwitterCore
import kotlinx.android.synthetic.main.activity_splash_screenn.*
import com.seeker.myapplication.R


class SplashScreenActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private var twitterConfig: TwitterConfig? = null
    private var m_IsNetworkAvailable: Boolean? = false

    /*User info observer*/
    private val userInfoResponseLD_Observer = Observer<Result<UserModel>> {
        when (it) {
            is Result.Success -> {
                //progressbarHide()
                if (it.data == null) {
                    //Do nothing for now
                } else {
                    var fullName = it?.data?.name
                    username.text = "Welcome, " + fullName?.substring(0, fullName.indexOf(" "))
                    username.visibility = View.VISIBLE

                    /*Save to shared pred*/
                    val prefs = customPreference(this, USER_PREFERENCE)
                    prefs.userId = it?.data?.id
                    prefs.userName = it?.data?.name
                    prefs.screenName = it?.data?.screenName

                    openFeed(it.data)
                }
            }
            is Result.Error -> {
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        m_IsNetworkAvailable = isNetworkAvailable(this@SplashScreenActivity)
        if (m_IsNetworkAvailable as Boolean) {
            initTwitter()
        }

        setContentView(R.layout.activity_splash_screenn)


        initView()
        logoAnimation()
        initViewModel()
        checkLoginInfo()

    }

    private fun initTwitter() {
        twitterConfig = TwitterConfig.Builder(this)
            .logger(DefaultLogger(Log.DEBUG))
            .twitterAuthConfig(
                TwitterAuthConfig(
                    getString(R.string.twitter_consumer_key),
                    getString(R.string.twitter_consumer_secret)
                )
            )
            .debug(true)
            .build()
        Twitter.initialize(twitterConfig)
    }

    private fun initView() {
        twitter_btn.setOnClickListener {
            twitter_login_button.callOnClick()
        }

        twitter_login_button.setCallback(object : Callback<TwitterSession>() {
            override fun success(result: com.twitter.sdk.android.core.Result<TwitterSession>?) {
                userViewModel.getUserInfo()
            }

            override fun failure(exception: TwitterException?) {
                Toast.makeText(
                    this@SplashScreenActivity,
                    resources.getString(R.string.login_fail),
                    Toast.LENGTH_SHORT
                ).show()

            }

        })
    }

    /*Will implement If got free time*/
    @SuppressLint("NewApi")
    private fun logoAnimation() {
         var animation: AnimatedVectorDrawable;
        val d: Drawable = logo_outlined.getDrawable()
        if (d is AnimatedVectorDrawable) {
            animation = d
            animation.start()
            logo_outlined.visibility=View.VISIBLE
        }

    }

    private fun initViewModel() {
        userViewModel =
            ViewModelProviders.of(this, TweetApp.applnstance?.let {
                ViewModelSplashFactory(
                    it
                )
            }).get(UserViewModel::class.java)
        userViewModel.userInfoResponseLD.observe(this, userInfoResponseLD_Observer)


    }

    private fun checkLoginInfo() {
        if (m_IsNetworkAvailable as Boolean) {
            /*If Internet available, Use Twitter session*/
            no_internet_indicator_splash.visibility=View.GONE

            val session =
                TwitterCore.getInstance().sessionManager.activeSession

            if (session != null) {
                twitter_btn.visibility = View.GONE
                userViewModel.getUserInfo()
            } else {
                twitter_btn.visibility = View.VISIBLE
            }
        } else {
            no_internet_indicator_splash.visibility=View.VISIBLE
            /*Else Use Shared Preference to get login User*/
            val prefs = customPreference(this, USER_PREFERENCE)
            if(prefs != null && prefs.userId != 0L){
            val user = UserModel(prefs.userId, prefs.userName!!, prefs.screenName!!)
            username.text = "Welcome, " + user.name?.substring(0, user.name.indexOf(" "))
            username.visibility = View.VISIBLE
            openFeed(user)}
        }
    }

    /*Open feed after 2 sec, Till "Welcome, User_Name" wil be displayed*/
    private fun openFeed(user: UserModel) {
        Handler().postDelayed(Runnable {
            var intent = Intent(this, FeedActivity::class.java)
            startActivity(intent)
            finish()
        },2000)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        twitter_login_button.onActivityResult(requestCode, resultCode, data)
    }


    class ViewModelSplashFactory(var app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserViewModel(app) as T
        }
    }


}
