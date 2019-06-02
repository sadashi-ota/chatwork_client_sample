package com.sadashi.client.chatwork.ui.login

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import com.google.android.material.snackbar.Snackbar
import com.sadashi.client.chatwork.BuildConfig
import com.sadashi.client.chatwork.R
import com.sadashi.client.chatwork.utility.RandomStringBuilder
import kotlinx.android.synthetic.main.activity_login.btnLogin
import kotlinx.android.synthetic.main.activity_login.rootView
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    companion object {
        const val CODE_LENGTH = 64
        const val LOGIN_URL = "${BuildConfig.LOGIN_URL}?response_type=code&client_id=${BuildConfig.CLIENT_ID}&scope=users.all:read&code_challenge_method=S256&code_challenge="
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            moveLoginHtmlPage()
        }

        intent?.data?.let { uri ->
            Snackbar.make(rootView, "Succeed to Login. $uri", Snackbar.LENGTH_SHORT)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.data?.let { uri ->
            Snackbar.make(rootView, "Succeed to Login. $uri", Snackbar.LENGTH_SHORT)
        }
    }

    private fun moveLoginHtmlPage() {
        val messageDigest = MessageDigest.getInstance("SHA-256").apply {
            update(RandomStringBuilder.build(CODE_LENGTH).toByteArray())
        }
        val url = LOGIN_URL + Base64.encodeToString(messageDigest.digest(), Base64.NO_PADDING)
        val intent = Intent().also {
            it.data = Uri.parse(url)
            it.action = Intent.ACTION_VIEW
        }
        startActivity(intent)
    }
}
