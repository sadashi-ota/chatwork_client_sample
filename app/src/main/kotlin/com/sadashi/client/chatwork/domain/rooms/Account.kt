package com.sadashi.client.chatwork.domain.rooms

import com.sadashi.client.chatwork.core.domain.Entity

class Account(
    accountId: AccountId,
    val avatarImageUrl: String,
    val name: String
) : Entity<AccountId>(accountId)