package com.sadashi.client.chatwork.infra.domain.room

import com.sadashi.client.chatwork.domain.rooms.Room
import com.sadashi.client.chatwork.domain.rooms.RoomId
import com.sadashi.client.chatwork.infra.api.json.RoomResponseJson

object RoomConverter {
    fun convertToDomainModelFromList(list: List<RoomResponseJson>): List<Room> {
        return list.map { convertToDomainModel(it) }
    }

    fun convertToDomainModel(json: RoomResponseJson): Room {
        return Room(
            id = RoomId(json.roomId),
            name = json.name,
            unreadNum = json.unreadNum,
            myTaskNum = json.myTaskNum,
            fileNum = json.fileNum,
            iconPath = json.iconPath,
            lastUpdateTime = json.lastUpdateTime
        )
    }
}