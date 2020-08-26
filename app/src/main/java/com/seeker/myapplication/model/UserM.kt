package com.seeker.myapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "User")
data class UserModel (
    @PrimaryKey
    @ColumnInfo(name = "Id")
    val id: Long,

    @ColumnInfo(name = "Name")
    val name: String,

    @ColumnInfo(name = "ScreenName")
    val screenName: String
) : Serializable{

}
