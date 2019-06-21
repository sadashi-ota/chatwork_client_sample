package com.sadashi.client.chatwork.domain.rooms

import com.sadashi.client.chatwork.core.domain.Entity

class Message(
    messageId: MessageId,
    val body: String,
    val account: Account,
    val sendTime: Int,
    val updateTime: Int
) : Entity<MessageId>(messageId)