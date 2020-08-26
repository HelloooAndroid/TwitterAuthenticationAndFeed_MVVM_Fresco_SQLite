package com.seeker.myapplication.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object PreferenceHelper {

    public val USER_PREFERENCE = "USER_PREFERENCE"
    val USER_ID = "USER_ID"
    val USER_NAME = "USER_NAME"
    val USER_SCREEN_NAME = "PASSWORD"

    fun defaultPreference(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun customPreference(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    var SharedPreferences.userId
        get() = getLong(USER_ID, 0)
        set(value) {
            editMe {
                it.putLong(USER_ID, value)
            }
        }

    var SharedPreferences.userName
        get() = getString(USER_NAME, "")
        set(value) {
            editMe {
                it.putString(USER_NAME, value)
            }
        }

    var SharedPreferences.screenName
        get() = getString(USER_SCREEN_NAME, "")
        set(value) {
            editMe {
                it.putString(USER_SCREEN_NAME, value)
            }
        }

    var SharedPreferences.clearValues
        get() = { }
        set(value) {
            editMe {
                it.clear()
            }
        }
}