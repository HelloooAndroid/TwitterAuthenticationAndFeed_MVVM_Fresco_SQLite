package com.seeker.myapplication.model

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "TimelineTweets")
data class TweetModel(
    @PrimaryKey
    @ColumnInfo(name="Id")
    val id: Long,

    @ColumnInfo(name="Text")
    val text: String,

    @ColumnInfo(name="RetweetCount")
    val retweetCount: Int,

    @ColumnInfo(name="Retweeted")
    val retweeted: Boolean,

    @ColumnInfo(name="FavoriteCount")
    val favoriteCount: Int,

    @ColumnInfo(name="Favorited")
    val favorited: Boolean,

    @ColumnInfo(name="User")
    val user: String,

    @ColumnInfo(name="UserScreenName")
    val userScreenName: String,

    @ColumnInfo(name="UserImage")
    val userImage: String,

    @ColumnInfo(name="UserVerified")
    val userVerified: Boolean,

    @ColumnInfo(name="MediaUrl")
    val mediaUrl: String

) : Serializable{
}