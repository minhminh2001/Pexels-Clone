package com.example.image.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface OnEventControlListener {
    fun onEvent(eventAction: Int, control: View?, data: Any?)
}