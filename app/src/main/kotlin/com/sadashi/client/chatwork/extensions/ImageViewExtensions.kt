package com.sadashi.client.chatwork.extensions

import android.widget.ImageView
import com.squareup.picasso.Picasso

fun ImageView.imageUrl(url: String?) {
    if (url.isNullOrEmpty()) {
        setImageDrawable(null)
        return
    }
    Picasso.get().load(url).into(this)
}
