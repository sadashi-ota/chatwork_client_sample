package com.sadashi.client.chatwork.ui.login

import android.net.Uri
import android.util.Base64
import android.util.Log
import com.sadashi.client.chatwork.BuildConfig
import com.sadashi.client.chatwork.utility.RandomStringBuilder
import java.security.MessageDigest

class LoginPresenter : LoginContract.Presentation {

    companion object {
        private const val CODE_LENGTH = 64
        private const val LOGIN_URL = "${BuildConfig.LOGIN_URL}?response_type=code&client_id=${BuildConfig.CLIENT_ID}" +
                "&redirect_uri=${BuildConfig.AUTH_CALLBACK}&scope=users.all:read" +
                "&code_challenge_method=S256&code_challenge="
    }

    private lateinit var view: LoginContract.View
    private lateinit var loginTransition: LoginTransition
    private lateinit var codeVerifier: String

    override fun setUp(view: LoginContract.View, loginTransition: LoginTransition) {
        this.view = view
        this.loginTransition = loginTransition
        this.codeVerifier = RandomStringBuilder.build(CODE_LENGTH)
    }

    override fun onStartLogin() {
        val messageDigest = MessageDigest.getInstance("SHA-256").apply {
            update(codeVerifier.toByteArray())
        }
        val url = LOGIN_URL +
                Base64.encodeToString(messageDigest.digest(), Base64.NO_PADDING + Base64.URL_SAFE)

        loginTransition.moveLoginHtmlPage(url)
    }

    override fun onLoaded(uri: Uri): Boolean {
        val code = uri.getQueryParameter("code") ?: return false

        return true
    }

}