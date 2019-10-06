package com.sadashi.client.chatwork.infra.domain.room

import com.sadashi.client.chatwork.domain.rooms.Account
import com.sadashi.client.chatwork.domain.rooms.AccountId
import com.sadashi.client.chatwork.infra.api.json.AccountDetailResponseJson
import com.sadashi.client.chatwork.infra.api.json.AccountJson

object AccountConverter {
    fun convertToDomainModel(json: AccountJson): Account {
        return Account(
            accountId = AccountId(json.accountId),
            avatarImageUrl = json.avatarImageUrl,
            name = json.name
        )
    }

    fun convertToDomainModelFromList(accountList: List<AccountDetailResponseJson>): List<Account> {
        return accountList.map { convertToDomainModelForDetail(it) }
    }

    private fun convertToDomainModelForDetail(json: AccountDetailResponseJson): Account {
        return Account(
            accountId = AccountId(json.accountId),
            avatarImageUrl = json.avatarImageUrl,
            name = json.name
        )
    }
}