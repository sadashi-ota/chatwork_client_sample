package com.sadashi.client.chatwork.ui.rooms

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sadashi.client.chatwork.R

class RoomsActivity : AppCompatActivity() {

    companion object {
        fun callingIntent(context: Context): Intent = Intent(context, RoomsActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms)
    }
}