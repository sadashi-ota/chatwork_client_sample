package com.sadashi.client.chatwork.ui.login

import android.net.Uri

interface LoginContract {
    interface Presentation {
        fun setUp(view: View, loginTransition: LoginTransition)
        fun onStartLogin()
        fun onLoaded(uri: Uri): Boolean
    }

    interface View {
        fun showProgress()
        fun dismissProgress()
        fun showErrorDialog(throwable: Throwable)
    }
}