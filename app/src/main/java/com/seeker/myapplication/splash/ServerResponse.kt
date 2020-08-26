package com.seeker.myapplication.splash

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ServerResponse(
    @SerializedName("errorCode")
    var errorCode:Int,
    @SerializedName("errValue")
    var errValue:String,
    @SerializedName("msgBuffer")
    var msgBuffer:String
)