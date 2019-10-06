package com.sadashi.client.chatwork.infra.api.json

import com.squareup.moshi.Json

data class AccountDetailResponseJson(
    @Json(name = "account_id") val accountId: Int,
    @Json(name = "avatar_image_url") val avatarImageUrl: String,
    @Json(name = "chatwork_id") val chatworkId: String,
    @Json(name = "department") val department: String,
    @Json(name = "name") val name: String,
    @Json(name = "organization_id") val organizationId: Int,
    @Json(name = "organization_name") val organizationName: String,
    @Json(name = "role") val role: String
)