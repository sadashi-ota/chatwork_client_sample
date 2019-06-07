package com.sadashi.client.chatwork.ui.rooms

import android.net.Uri

interface RoomsContract {
    interface Presentation {
        fun setUp(view: View, roomsTransition: RoomsTransition)
        fun onStartLogin()
    }

    interface View {
        fun showProgress()
        fun dismissProgress()
        fun showErrorDialog(throwable: Throwable)
    }
}