package com.sadashi.client.chatwork.ui.login

import android.net.Uri

interface LoginContract {
    interface Presentation {
        fun setUp(view: View, loginTransition: LoginTransition)
        fun onLoaded(uri: Uri): Boolean
        fun login()
        fun logout()
    }

    interface View {
        fun showProgress()
        fun dismissProgress()
        fun showErrorDialog(throwable: Throwable)
    }
}