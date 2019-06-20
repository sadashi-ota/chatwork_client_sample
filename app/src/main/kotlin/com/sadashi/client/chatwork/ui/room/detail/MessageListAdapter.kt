package com.sadashi.client.chatwork.ui.room.detail

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sadashi.client.chatwork.R
import com.sadashi.client.chatwork.domain.rooms.Message
import com.sadashi.client.chatwork.extensions.imageUrl
import com.sadashi.client.chatwork.extensions.inflate
import kotlinx.android.synthetic.main.item_message.view.body
import kotlinx.android.synthetic.main.item_message.view.icon
import kotlinx.android.synthetic.main.item_message.view.name

class MessageListAdapter(
    private val onItemClicked: (Message) -> Unit
) : ListAdapter<Message, MessageViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val holder = MessageViewHolder(parent)
        holder.itemView.setOnClickListener {
            val message = getItem(holder.adapterPosition) ?: return@setOnClickListener
            onItemClicked(message)
        }
        return holder
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val member = getItem(position) ?: return
        holder.bind(member)
    }
}

class MessageViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    parent.inflate(R.layout.item_room)
) {
    fun bind(message: Message) {
        itemView.icon.imageUrl(message.account.avatarImageUrl)
        itemView.name.text = message.account.name
        itemView.body.text = message.body
    }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Message>() {

    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }

}
