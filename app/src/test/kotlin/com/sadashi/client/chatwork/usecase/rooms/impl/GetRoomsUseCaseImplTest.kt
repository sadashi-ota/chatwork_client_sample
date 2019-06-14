package com.sadashi.client.chatwork.usecase.rooms.impl

import com.sadashi.client.chatwork.domain.auth.AccessToken
import com.sadashi.client.chatwork.domain.auth.AuthorizeService
import com.sadashi.client.chatwork.domain.auth.AuthorizedToken
import com.sadashi.client.chatwork.domain.auth.RefreshToken
import com.sadashi.client.chatwork.domain.rooms.Room
import com.sadashi.client.chatwork.domain.rooms.RoomId
import com.sadashi.client.chatwork.domain.rooms.RoomRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.Date

internal class GetRoomsUseCaseImplTest : Spek({

    lateinit var roomRepository: RoomRepository
    lateinit var authorizeService: AuthorizeService

    lateinit var getRoomsUseCase: GetRoomsUseCaseImpl

    beforeEachTest {
        roomRepository = mockk()
        authorizeService = mockk()
        getRoomsUseCase = GetRoomsUseCaseImpl(roomRepository, authorizeService)
    }

    describe("#execute") {
        context("When succeeds to get authorized token") {
            beforeEach {
                every { authorizeService.getToken() } returns Single.just(VALID_AUTHORIZED_TOKEN)
            }

            context("When succeeds to get rooms") {
                beforeEach {
                    every { roomRepository.getRooms(eq(VALID_ACCESS_TOKEN)) } returns Single.just(ROOMS)
                }

                it("calls onSuccess") {
                    getRoomsUseCase.execute().test().await()
                        .assertValue(ROOMS)
                        .assertNoErrors()
                        .assertComplete()

                    verify(exactly = 1) {
                        authorizeService.getToken()
                        roomRepository.getRooms(eq(VALID_ACCESS_TOKEN))
                    }
                    confirmVerified(authorizeService, roomRepository)
                }
            }
            context("When fails to get rooms") {
                beforeEach {
                    every {
                        roomRepository.getRooms(eq(VALID_ACCESS_TOKEN))
                    } returns Single.error(Throwable("Dummy error"))
                }

                it("calls onError") {
                    getRoomsUseCase.execute().test().await()
                        .assertError(Throwable::class.java)
                        .assertNotComplete()

                    verify(exactly = 1) {
                        authorizeService.getToken()
                        roomRepository.getRooms(eq(VALID_ACCESS_TOKEN))
                    }
                    confirmVerified(authorizeService, roomRepository)
                }
            }
        }
        context("When fails to get authorized token") {
            beforeEach {
                every { authorizeService.getToken() } returns Single.error(Throwable("Dummy error."))
            }

            it("calls onError") {
                getRoomsUseCase.execute().test().await()
                    .assertError(Throwable::class.java)
                    .assertNotComplete()

                verify(exactly = 1) {
                    authorizeService.getToken()
                }
                confirmVerified(authorizeService, roomRepository)
            }
        }
    }
}) {
    companion object {

        val VALID_ACCESS_TOKEN = AccessToken("dummy_access_token")
        val VALID_AUTHORIZED_TOKEN = AuthorizedToken(
            accessToken = VALID_ACCESS_TOKEN,
            refreshToken = RefreshToken("dummy_refresh_token"),
            expiredTime = Date(),
            tokenType = "dummy_token_type",
            scope = "dummy_scope"
        )

        val ROOMS = listOf(
            Room(
                id = RoomId(1),
                name = "dummy room",
                unreadNum = 1,
                myTaskNum = 1,
                fileNum = 1,
                iconPath = "",
                lastUpdateTime = System.currentTimeMillis().toInt()
            )
        )
    }
}