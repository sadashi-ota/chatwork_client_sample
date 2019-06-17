package com.sadashi.client.chatwork.infra.domain.room

import com.sadashi.client.chatwork.domain.rooms.Message
import com.sadashi.client.chatwork.domain.rooms.MessageId
import com.sadashi.client.chatwork.infra.api.json.MessageJson

object MessageConverter {
    fun convertToDomainModelFromList(messages: List<MessageJson>): List<Message> {
        return messages.map { convertToDomainModel(it) }
    }

    private fun convertToDomainModel(message: MessageJson): Message {
        return Message(
            messageId = MessageId(message.messageId),
            body = message.body,
            sendTime = message.sendTime,
            updateTime = message.updateTime
        )
    }
}