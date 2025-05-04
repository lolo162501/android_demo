package com.example.myapplication

import com.example.myapplication.user.UserRepository
import com.example.myapplication.user.UserService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class UserRepositoryIntegrationTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var repository: UserRepository

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(UserService::class.java)
        repository = UserRepository(api)
    }

    @After
    fun shutdown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `測試撈取資訊`() = runBlocking {
        val body = """[{"id":1,"name":"Carol","email":"carol@example.com"}]"""
        mockWebServer.enqueue(MockResponse().setBody(body).setResponseCode(200))
        val result = repository.fetchFirstUserName()
        println("Result => $result")
        assertEquals("First user: Carol", result)
    }
}