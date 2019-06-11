package com.sadashi.client.chatwork.ui.rooms

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sadashi.client.chatwork.R
import com.sadashi.client.chatwork.di.RoomsModuleInjection
import com.sadashi.client.chatwork.domain.rooms.Room
import kotlinx.android.synthetic.main.fragment_rooms.progressBar
import kotlinx.android.synthetic.main.fragment_rooms.roomListView
import kotlinx.android.synthetic.main.fragment_rooms.rootLayout
import kotlinx.android.synthetic.main.fragment_rooms.toolbar

class RoomsFragment : Fragment(), RoomsContract.View {

    companion object {
        fun newInstance() = RoomsFragment()
    }

    private lateinit var presenter: RoomsContract.Presentation
    private lateinit var roomListAdapter: RoomListAdapter

    private val menuClickListener = Toolbar.OnMenuItemClickListener { item ->
        item ?: return@OnMenuItemClickListener false

        when (item.itemId) {
            R.id.menu_logout -> {
                presenter.logout()
                true
            }
            else -> false
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val transition = (context as? RoomsTransition)
            ?: throw ClassCastException("must cast RoomsTransition")

        presenter = RoomsModuleInjection(context).getPresenter()
        presenter.setUp(this, transition)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_rooms, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeUi(view)
        presenter.onStart()
    }

    override fun onDetach() {
        super.onDetach()
        presenter.terminate()
    }

    override fun showRoomsList(rooms: List<Room>) {
        roomListAdapter.submitList(rooms)
    }

    override fun clearRoomsList() {
        roomListAdapter.submitList(null)
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

    private fun initializeUi(view: View) {
        toolbar.also {
            it.title = "Rooms"
            it.inflateMenu(R.menu.rooms)
            it.setOnMenuItemClickListener(menuClickListener)
        }

        roomListAdapter = RoomListAdapter {
            // TODO: Not implements
            Snackbar.make(rootLayout, "Click ${it.name}.", Snackbar.LENGTH_LONG).show()
        }

        roomListView.also {
            it.layoutManager = LinearLayoutManager(view.context)
            it.adapter = roomListAdapter
            it.setHasFixedSize(true)
        }
    }
}