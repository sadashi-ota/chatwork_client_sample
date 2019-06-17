package com.sadashi.client.chatwork.ui.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.sadashi.client.chatwork.R
import com.sadashi.client.chatwork.di.LoginModuleInjection
import com.sadashi.client.chatwork.ui.room.list.RoomsActivity
import kotlinx.android.synthetic.main.activity_login.btnLogin
import kotlinx.android.synthetic.main.activity_login.progressBar
import kotlinx.android.synthetic.main.activity_login.rootView

class LoginActivity : AppCompatActivity(), LoginContract.View, LoginTransition {
    companion object {
        fun callingIntent(context: Context): Intent = Intent(context, LoginActivity::class.java)
    }

    private lateinit var presenter: LoginContract.Presentation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter = LoginModuleInjection(this).getPresenter()
        presenter.setUp(this, this)

        supportActionBar?.title = "Login"

        btnLogin.setOnClickListener {
            presenter.login()
        }

        intent?.data?.let { uri ->
            presenter.onLoaded(uri)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.data?.let { uri ->
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

    override fun moveRooms() {
        startActivity(RoomsActivity.callingIntent(this))
        finish()
    }
}
