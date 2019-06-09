package com.sadashi.client.chatwork.domain.auth

import com.sadashi.client.chatwork.core.domain.ValueObject

data class CodeVerifier(val value: String) : ValueObject
