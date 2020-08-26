package com.seeker.myapplication.feed

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seeker.myapplication.R
import com.seeker.myapplication.model.TweetModel
import com.seeker.myapplication.utils.setTextViewDrawableColor
import kotlinx.android.synthetic.main.row_tweet.view.*


class FeedAdapter(val feedList: ArrayList<TweetModel>, val _context: Activity) :
    RecyclerView.Adapter<FeedAdapter.ViewHolder>() {


    //returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_tweet, parent, false)
        return ViewHolder(v)
    }

    //binding the data on the list
    override fun onBindViewHolder(holder: FeedAdapter.ViewHolder, position: Int) {
        holder.bindItems(feedList[position], _context)
    }

    //return size of the list
    override fun getItemCount(): Int {
        return feedList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            tweet: TweetModel,
            _context: Activity
        ) {
            itemView.userName.text = tweet.user
            itemView.userImage.setImageURI(tweet.userImage);

            if (tweet.mediaUrl.equals("")) {
                itemView.media_iv.visibility = View.GONE
            } else {
                itemView.media_iv.visibility = View.VISIBLE
                itemView.media_iv.setImageURI(tweet.mediaUrl);
            }
            itemView.userScreenName.text = "@" + tweet.userScreenName

            itemView.description.text = tweet.text

            itemView.retweet.text = tweet.retweetCount.toString()
            if (tweet.retweeted) {
                itemView.retweet.setTextColor(_context.resources.getColor(R.color.colorPrimary))
                setTextViewDrawableColor(
                    itemView.retweet,
                    R.color.colorPrimary
                )
            } else {
                itemView.retweet.setTextColor(_context.resources.getColor(R.color.desableText))
                setTextViewDrawableColor(
                    itemView.retweet,
                    R.color.desableText
                )
            }

            itemView.like.text = tweet.favoriteCount.toString()
            if (tweet.favorited) {
                itemView.like.setTextColor(_context.resources.getColor(R.color.colorPrimary))
                setTextViewDrawableColor(
                    itemView.like,
                    R.color.colorPrimary
                )
            } else {
                itemView.like.setTextColor(_context.resources.getColor(R.color.desableText))
                setTextViewDrawableColor(
                    itemView.like,
                    R.color.desableText
                )
            }

        }
    }

}