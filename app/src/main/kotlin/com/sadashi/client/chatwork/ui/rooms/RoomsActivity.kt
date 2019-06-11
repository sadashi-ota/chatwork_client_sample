package com.sadashi.client.chatwork.ui.rooms

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sadashi.client.chatwork.R
import com.sadashi.client.chatwork.ui.login.LoginActivity

class RoomsActivity : AppCompatActivity(), RoomsTransition {
    companion object {
        fun callingIntent(context: Context): Intent = Intent(context, RoomsActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        supportFragmentManager.beginTransaction().also {
            it.replace(R.id.container, RoomsFragment.newInstance())
        }.commitNow()
    }

    override fun moveLoginPage() {
        val intent = LoginActivity.callingIntent(this)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

        startActivity(intent)
        finish()
    }
}