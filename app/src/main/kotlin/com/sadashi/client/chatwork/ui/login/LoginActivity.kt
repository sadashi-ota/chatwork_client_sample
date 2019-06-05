package com.sadashi.client.chatwork.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.sadashi.client.chatwork.R
import kotlinx.android.synthetic.main.activity_login.btnLogin
import kotlinx.android.synthetic.main.activity_login.progressBar
import kotlinx.android.synthetic.main.activity_login.rootView

class LoginActivity : AppCompatActivity(), LoginContract.View, LoginTransition {
    private val presenter: LoginContract.Presentation = LoginPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter.setUp(this, this)

        btnLogin.setOnClickListener {
            presenter.onStartLogin()
        }

        intent?.data?.let { uri ->
            Snackbar.make(rootView, "Succeed to Login. $uri", Snackbar.LENGTH_SHORT)
            presenter.onLoaded(uri)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.data?.let { uri ->
            Snackbar.make(rootView, "Succeed to Login. $uri", Snackbar.LENGTH_SHORT)
            presenter.onLoaded(uri)
        }
    }

    override fun moveLoginHtmlPage(url: String) {
        val intent = Intent().also {
            it.data = Uri.parse(url)
            it.action = Intent.ACTION_VIEW
        }
        startActivity(intent)
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun dismissProgress() {
        progressBar.visibility = View.GONE
    }

    override fun showErrorDialog(throwable: Throwable) {
        Snackbar.make(rootView, "Error!!!!!!", Snackbar.LENGTH_LONG).show()
    }

    override fun navigationBack() {
        finish()
    }
}
