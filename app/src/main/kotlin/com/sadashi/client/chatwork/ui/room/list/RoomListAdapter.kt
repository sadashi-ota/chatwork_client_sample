package com.sadashi.client.chatwork.ui.room.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sadashi.client.chatwork.R
import com.sadashi.client.chatwork.domain.rooms.Room
import com.sadashi.client.chatwork.extensions.imageUrl
import com.sadashi.client.chatwork.extensions.inflate
import kotlinx.android.synthetic.main.item_room.view.icon
import kotlinx.android.synthetic.main.item_room.view.name

class RoomListAdapter(
    private val onItemClicked: (Room) -> Unit
) : ListAdapter<Room, RoomViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val holder = RoomViewHolder(parent)
        holder.itemView.setOnClickListener {
            val room = getItem(holder.adapterPosition) ?: return@setOnClickListener
            onItemClicked(room)
        }
        return holder
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = getItem(position) ?: return
        holder.bind(room)
    }
}

class RoomViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    parent.inflate(R.layout.item_room)
) {
    fun bind(room: Room) {
        itemView.icon.imageUrl(room.iconPath)
        itemView.name.text = room.name
    }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Room>() {

    override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
        return oldItem == newItem
    }

}
