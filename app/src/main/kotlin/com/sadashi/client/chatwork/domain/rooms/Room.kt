package com.sadashi.client.chatwork.domain.rooms

import com.sadashi.client.chatwork.core.domain.Entity

class Room(
    id: RoomId,
    val name: String,
    val unreadNum: Int,
    val myTaskNum: Int,
    val fileNum: Int,
    val iconPath: String,
    val lastUpdateTime: Int
) : Entity<RoomId>(id)