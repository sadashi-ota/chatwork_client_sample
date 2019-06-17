package com.sadashi.client.chatwork.ui.room.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sadashi.client.chatwork.R
import com.sadashi.client.chatwork.domain.rooms.RoomId

class RoomDetailActivity : AppCompatActivity(), RoomDetailTransition {
    companion object {
        private const val INTENT_KEY = "com.sadashi.client.chatwork.INTENT_PARAM_ROOM"

        fun callingIntent(context: Context, id: RoomId): Intent {
            return Intent(context, RoomDetailActivity::class.java).also {
                it.putExtra(INTENT_KEY, id.value)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        val roomId = RoomId(intent.getIntExtra(INTENT_KEY, RoomId.INVALID_ID))
        roomId.isValid() || run {
            finish()
            return
        }

        supportFragmentManager.beginTransaction().also {
            it.replace(R.id.container, RoomDetailFragment.newInstance(roomId))
        }.commitNow()
    }

    override fun navigationBack() {
        finish()
    }
}