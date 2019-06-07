package com.sadashi.client.chatwork.ui.room.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sadashi.client.chatwork.R

class RoomListActivity : AppCompatActivity() {

    companion object {
        fun callingIntent(context: Context): Intent = Intent(context, RoomListActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_list)
    }
}