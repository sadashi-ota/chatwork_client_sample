package com.sadashi.client.chatwork.infra.domain.room

import com.sadashi.client.chatwork.domain.rooms.Account
import com.sadashi.client.chatwork.domain.rooms.AccountId
import com.sadashi.client.chatwork.infra.api.json.AccountDetailJson

object AccountConverter {
    fun convertToDomainModelFromList(accountList: List<AccountDetailJson>): List<Account> {
        return accountList.map { convertToDomainModel(it) }
    }

    private fun convertToDomainModel(json: AccountDetailJson): Account {
        return Account(
            accountId = AccountId(json.accountId),
            avatarImageUrl = json.avatarImageUrl,
            name = json.name
        )
    }
}