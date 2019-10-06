package com.sadashi.client.chatwork.infra.domain.room

import com.sadashi.client.chatwork.domain.auth.AccessToken
import com.sadashi.client.chatwork.domain.auth.AuthorizeRepository
import com.sadashi.client.chatwork.domain.auth.AuthorizedToken
import com.sadashi.client.chatwork.domain.auth.RefreshToken
import com.sadashi.client.chatwork.domain.rooms.Account
import com.sadashi.client.chatwork.domain.rooms.Message
import com.sadashi.client.chatwork.domain.rooms.Room
import com.sadashi.client.chatwork.domain.rooms.RoomId
import com.sadashi.client.chatwork.infra.api.RoomApiClient
import com.sadashi.client.chatwork.infra.api.json.AccountDetailResponseJson
import com.sadashi.client.chatwork.infra.api.json.MessageResponseJson
import com.sadashi.client.chatwork.infra.api.json.RoomResponseJson
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.Date

internal class RoomRepositoryImplTest : Spek({

    lateinit var authorizeRepository: AuthorizeRepository
    lateinit var apiClient: RoomApiClient
    val scheduler: Scheduler = Schedulers.trampoline()

    lateinit var roomRepository: RoomRepositoryImpl

    val authorizedToken = AuthorizedToken(
        accessToken = AccessToken("Dummy access token"),
        refreshToken = RefreshToken("Dummy refresh token"),
        expiredTime = Date(),
        tokenType = "Dummy token type",
        scope = "Dummy scope"
    )
    val validRoomId = RoomId(1)
    val invalidRoomId = RoomId(-1)

    lateinit var roomResponseJson: RoomResponseJson
    lateinit var room: Room
    lateinit var messageResponseJson: MessageResponseJson
    lateinit var message: Message
    lateinit var accountDetailResponseJson: AccountDetailResponseJson
    lateinit var account: Account

    beforeEachTest {
        authorizeRepository = mockk()
        apiClient = mockk()

        roomRepository = RoomRepositoryImpl(authorizeRepository, apiClient, scheduler)
    }

    describe("#getRooms") {
        beforeEach { mockkObject(RoomConverter) }
        afterEach { unmockkObject(RoomConverter) }

        context("When getToken() is succeed") {
            beforeEach {
                every { authorizeRepository.getToken() } returns Single.just(authorizedToken)
            }
            context("When api is succeed") {
                beforeEach {
                    room = mockk()
                    roomResponseJson = mockk()
                    every {
                        apiClient.getRooms(eq(authorizedToken.accessTokenString))
                    } returns Single.just(listOf(roomResponseJson))

                    every {
                        RoomConverter.convertToDomainModelFromList(eq(listOf(roomResponseJson)))
                    } returns listOf(room)
                }
                it("Succeed to get room list") {
                    roomRepository.getRooms().test().await()
                        .assertValue(listOf(room))
                        .assertNoErrors()
                        .assertComplete()
                }
            }
            context("When api is failed") {
                beforeEach {
                    every {
                        apiClient.getRooms(any())
                    } returns Single.error(Throwable("Dummy error"))
                }
                it("Failed to get room list") {
                    roomRepository.getRooms().test().await()
                        .assertNoValues()
                        .assertError(Throwable::class.java)
                        .assertNotComplete()
                }
            }
        }
        context("When getToken() is failed") {
            beforeEach {
                every {
                    authorizeRepository.getToken()
                } returns Single.error(Throwable("Dummy error"))
            }
            it("Failed to get room list") {
                roomRepository.getRooms().test().await()
                    .assertNoValues()
                    .assertError(Throwable::class.java)
                    .assertNotComplete()
            }
        }
    }

    describe("#getRoom") {
        beforeEach { mockkObject(RoomConverter) }
        afterEach { unmockkObject(RoomConverter) }

        context("When getToken() is succeed") {
            beforeEach {
                every { authorizeRepository.getToken() } returns Single.just(authorizedToken)
            }
            context("When api is succeed") {
                beforeEach {
                    message = mockk()
                    roomResponseJson = mockk()
                    every {
                        apiClient.getRoom(eq(authorizedToken.accessTokenString), validRoomId.value)
                    } returns Single.just(roomResponseJson)

                    every {
                        RoomConverter.convertToDomainModel(eq(roomResponseJson))
                    } returns room
                }
                it("Succeed to get room") {
                    roomRepository.getRoom(validRoomId).test().await()
                        .assertValue(room)
                        .assertNoErrors()
                        .assertComplete()
                }
            }
            context("When api is failed") {
                beforeEach {
                    every {
                        apiClient.getRoom(any(), invalidRoomId.value)
                    } returns Single.error(Throwable("Dummy error"))
                }
                it("Failed to get room") {
                    roomRepository.getRoom(invalidRoomId).test().await()
                        .assertNoValues()
                        .assertError(Throwable::class.java)
                        .assertNotComplete()
                }
            }
        }
        context("When getToken() is failed") {
            beforeEach {
                every {
                    authorizeRepository.getToken()
                } returns Single.error(Throwable("Dummy error"))
            }
            it("Failed to get room") {
                roomRepository.getRoom(validRoomId).test().await()
                    .assertNoValues()
                    .assertError(Throwable::class.java)
                    .assertNotComplete()
            }
        }
    }

    describe("#getMessages") {
        beforeEach { mockkObject(MessageConverter) }
        afterEach { unmockkObject(MessageConverter) }

        context("When getToken() is succeed") {
            beforeEach {
                every { authorizeRepository.getToken() } returns Single.just(authorizedToken)
            }
            context("When api is succeed") {
                beforeEach {
                    message = mockk()
                    messageResponseJson = mockk()

                    every {
                        MessageConverter.convertToDomainModelFromList(eq(listOf(messageResponseJson)))
                    } returns listOf(message)
                }
                context("When force load") {
                    beforeEach {
                        every {
                            apiClient.getMessages(
                                eq(authorizedToken.accessTokenString),
                                validRoomId.value,
                                1
                            )
                        } returns Single.just(listOf(messageResponseJson))
                    }
                    it("Succeed to get message list") {
                        roomRepository.getMessages(validRoomId, true).test().await()
                            .assertValue(listOf(message))
                            .assertNoErrors()
                            .assertComplete()
                    }
                }
                context("When unforced load") {
                    beforeEach {
                        every {
                            apiClient.getMessages(
                                eq(authorizedToken.accessTokenString),
                                validRoomId.value,
                                0
                            )
                        } returns Single.just(listOf(messageResponseJson))
                    }
                    it("Succeed to get message list") {
                        roomRepository.getMessages(validRoomId, false).test().await()
                            .assertValue(listOf(message))
                            .assertNoErrors()
                            .assertComplete()
                    }
                }
            }
            context("When api is failed") {
                beforeEach {
                    every {
                        apiClient.getMessages(any(), invalidRoomId.value, 1)
                    } returns Single.error(Throwable("Dummy error"))
                }
                it("Failed to get message list") {
                    roomRepository.getMessages(invalidRoomId, true).test().await()
                        .assertNoValues()
                        .assertError(Throwable::class.java)
                        .assertNotComplete()
                }
            }
        }
        context("When getToken() is failed") {
            beforeEach {
                every {
                    authorizeRepository.getToken()
                } returns Single.error(Throwable("Dummy error"))
            }
            it("Failed to get message list") {
                roomRepository.getMessages(validRoomId, true).test().await()
                    .assertNoValues()
                    .assertError(Throwable::class.java)
                    .assertNotComplete()
            }
        }
    }

    describe("#getMembers") {
        beforeEach { mockkObject(AccountConverter) }
        afterEach { unmockkObject(AccountConverter) }

        context("When getToken() is succeed") {
            beforeEach {
                every { authorizeRepository.getToken() } returns Single.just(authorizedToken)
            }
            context("When api is succeed") {
                beforeEach {
                    account = mockk()
                    accountDetailResponseJson = mockk()

                    every {
                        apiClient.getMembers(
                            eq(authorizedToken.accessTokenString),
                            validRoomId.value
                        )
                    } returns Single.just(listOf(accountDetailResponseJson))
                    every {
                        AccountConverter.convertToDomainModelFromList(
                            eq(listOf(accountDetailResponseJson))
                        )
                    } returns listOf(account)
                }
                it("Succeed to get account list") {
                    roomRepository.getMembers(validRoomId).test().await()
                        .assertValue(listOf(account))
                        .assertNoErrors()
                        .assertComplete()
                }
            }
            context("When api is failed") {
                beforeEach {
                    every {
                        apiClient.getMembers(any(), invalidRoomId.value)
                    } returns Single.error(Throwable("Dummy error"))
                }
                it("Failed to get account list") {
                    roomRepository.getMembers(invalidRoomId).test().await()
                        .assertNoValues()
                        .assertError(Throwable::class.java)
                        .assertNotComplete()
                }
            }
        }
        context("When getToken() is failed") {
            beforeEach {
                every {
                    authorizeRepository.getToken()
                } returns Single.error(Throwable("Dummy error"))
            }
            it("Failed to get account list") {
                roomRepository.getMembers(validRoomId).test().await()
                    .assertNoValues()
                    .assertError(Throwable::class.java)
                    .assertNotComplete()
            }
        }
    }
})