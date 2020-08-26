package com.seeker.myapplication.utils

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.widget.TextView
import androidx.core.content.ContextCompat


fun setTextViewDrawableColor(textView: TextView, color: Int) {
    for (drawable in textView.compoundDrawables) {
        if (drawable != null) {
            drawable.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(
                    textView.context,
                    color
                ), PorterDuff.Mode.SRC_IN
            )
        }
    }
}