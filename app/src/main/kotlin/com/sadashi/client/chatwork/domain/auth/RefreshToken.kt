package com.sadashi.client.chatwork.domain.auth

import com.sadashi.client.chatwork.core.domain.ValueObject

data class RefreshToken(val value: String) : ValueObject
