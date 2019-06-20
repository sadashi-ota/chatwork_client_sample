package com.sadashi.client.chatwork.ui.room.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sadashi.client.chatwork.R
import com.sadashi.client.chatwork.di.RoomDetailModuleInjection
import com.sadashi.client.chatwork.domain.rooms.Account
import com.sadashi.client.chatwork.domain.rooms.Message
import com.sadashi.client.chatwork.domain.rooms.Room
import com.sadashi.client.chatwork.domain.rooms.RoomId
import kotlinx.android.synthetic.main.fragment_room_detail.messageListView
import kotlinx.android.synthetic.main.fragment_room_detail.progressBar
import kotlinx.android.synthetic.main.fragment_room_detail.rootLayout
import kotlinx.android.synthetic.main.fragment_room_detail.toolbar

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
    private lateinit var messageListAdapter: MessageListAdapter

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

        messageListAdapter = MessageListAdapter {
            TODO("Not implements")
        }

        messageListView.also {
            it.layoutManager = LinearLayoutManager(view.context)
            it.adapter = messageListAdapter
            it.setHasFixedSize(true)
        }
    }

    override fun showRoomDetail(room: Room) {
        toolbar.title = room.name
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

    override fun showMessages(messages: List<Message>) {
        messageListAdapter.submitList(messages)
    }

    override fun showMembers(members: List<Account>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}