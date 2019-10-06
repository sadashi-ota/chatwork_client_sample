package com.sadashi.client.chatwork.infra.domain.room

import com.sadashi.client.chatwork.domain.rooms.Message
import com.sadashi.client.chatwork.domain.rooms.MessageId
import com.sadashi.client.chatwork.infra.api.json.MessageResponseJson

object MessageConverter {
    fun convertToDomainModelFromList(messages: List<MessageResponseJson>): List<Message> {
        return messages.map { convertToDomainModel(it) }
    }

    private fun convertToDomainModel(message: MessageResponseJson): Message {
        return Message(
            messageId = MessageId(message.messageId),
            body = message.body,
            account = AccountConverter.convertToDomainModel(message.account),
            sendTime = message.sendTime,
            updateTime = message.updateTime
        )
    }
}