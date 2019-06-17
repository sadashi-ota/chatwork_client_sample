package com.sadashi.client.chatwork.ui.room.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.sadashi.client.chatwork.domain.rooms.Room
import com.sadashi.client.chatwork.domain.rooms.RoomId

class RoomDetailFragment : Fragment(), RoomDetailContract.View {
    companion object {
        private const val KEY_PARAM_ROOM_ID = "room_id"

        fun newInstance(roomId: RoomId): RoomDetailFragment {
            return RoomDetailFragment().also {
                it.arguments = Bundle().also { bundle ->
                    bundle.putInt(KEY_PARAM_ROOM_ID, roomId.value)
                }
            }
        }
    }

    override fun showRoomDetail(room: Room) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showErrorDialog(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}