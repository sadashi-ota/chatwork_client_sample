package com.sadashi.client.chatwork.ui.rooms

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.sadashi.client.chatwork.R
import com.sadashi.client.chatwork.di.RoomsModuleInjection
import com.sadashi.client.chatwork.domain.rooms.Room
import com.sadashi.client.chatwork.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_login.progressBar
import kotlinx.android.synthetic.main.activity_rooms.rootLayout

class RoomsActivity : AppCompatActivity(), RoomsContract.View, RoomsTransition {
    companion object {
        fun callingIntent(context: Context): Intent = Intent(context, RoomsActivity::class.java)
    }

    private lateinit var presenter: RoomsContract.Presentation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms)

        presenter = RoomsModuleInjection(this).getPresenter()
        presenter.setUp()
    }

    override fun showRoomsList(rooms: List<Room>) {

    }

    override fun clearRoomsList() {

    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun dismissProgress() {
        progressBar.visibility = View.GONE
    }

    override fun showErrorDialog(throwable: Throwable) {
        Snackbar.make(rootLayout, "Error!!!!!!", Snackbar.LENGTH_LONG).show()
    }


    override fun moveLoginPage() {
        startActivity(LoginActivity.callingIntent(this))
    }
}