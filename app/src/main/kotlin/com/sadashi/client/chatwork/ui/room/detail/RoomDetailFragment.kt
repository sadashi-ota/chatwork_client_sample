package com.sadashi.client.chatwork.ui.room.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sadashi.client.chatwork.R
import com.sadashi.client.chatwork.di.RoomDetailModuleInjection
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

    lateinit var presenter: RoomDetailContract.Presentation

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val transition = (context as? RoomDetailTransition)
            ?: throw ClassCastException("must cast RoomsTransition")

        presenter = RoomDetailModuleInjection(context).getPresenter()
        presenter.setUp(this, transition)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_room_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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