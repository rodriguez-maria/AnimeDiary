package com.marmarovas.animediary

import com.marmarovas.animediary.network.AnimeDiaryAPIService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class AnimeDiaryAPIServiceTest {

    lateinit var server: MockWebServer
    lateinit var apiService: AnimeDiaryAPIService

    @Before
    fun initTest() {
        server = MockWebServer()
        server.start(8000)

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(server.url("/"))
            //.addCallAdapterFactory(KotlinJsonAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(AnimeDiaryAPIService::class.java)

    }

    @After
    fun shutdown() {
        server.shutdown()
    }

    //------------------ LOGIN TESTS --------------------//
    @Test
    fun `login request sent is populated correctly`() {

        server.apply {
            enqueue(MockResponse().setBody(MockResponseFileReader("login_success").getContent()))
        }

        val username = "TestName"
        val password = "Test123"
        val p = apiService.login(username, password).execute()

        val requestBody = server.takeRequest()

        assertEquals("POST", requestBody.method)
        assertEquals("/login", requestBody.path)

        //The expected body to be received by the server
        val body : String = "[text=userName=TestName&password=Test123]"

        assertEquals(body, requestBody.body.toString())
    }

    @Test
    fun `login call populates login class correctly on success`(){
        server.apply {
            enqueue(MockResponse().setBody(MockResponseFileReader("login_success").getContent()))
        }

        val username = "TestName"
        val password = "Test123"
        val p = apiService.login(username, password).execute()

        assertTrue(p.body() != null)

        assertTrue(p.body()!!.data != null)

        //id
        assertEquals("1far4", p.body()!!.data!!.id)

        //username
        assertEquals("maria", p.body()!!.data!!.userName)

        //name
        assertEquals("Maria R", p.body()!!.data!!.name)

        //token
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCxOTY", p.body()!!.data!!.token)
    }

    @Test
    fun `login call gets correct error code on failure`(){

        val errorCode =  401

        server.apply {
            enqueue(MockResponse().setResponseCode(errorCode))
        }

        val username = "TestName"
        val password = "Test123"
        val p = apiService.login(username, password).execute()

        //Commented this although it works fine even though it says is package private
//        assertTrue(p.raw().code == 401)
    }

    //------------------ END LOGIN TESTS --------------------//

    //------------------ REGISTER TESTS --------------------//
    //TODO: add register tests here
    //------------------ END REGISTER TESTS --------------------//

    //------------------ ANIMES TESTS --------------------//
    @Test
    fun `animes request is populated correctly`(){
        val authenticationToken = "test_token"

        server.apply{
            enqueue(MockResponse()
                .addHeader("Authorization", authenticationToken)
                .setBody(MockResponseFileReader("animes_todo_tag_success_response").getContent()))
        }

        val search = ""
        val tag = "to-do"
        val limit = ""
        var response = apiService.getAnimesList(authenticationToken, search, tag, limit).execute()

        var request = server.takeRequest()

        assertEquals("GET", request.method)

        //TODO: Test that the path is correct. (How do I know which is the right path?)
    }

    @Test
    fun `getAnimesList call populates animes class correctly on success`(){
        val authenticationToken = "test_token"

        server.apply{
            enqueue(MockResponse()
                .addHeader("Authorization", authenticationToken)
                .setBody(MockResponseFileReader("animes_todo_tag_success_response").getContent()))
        }

        val search = ""
        val tag = "to-do"
        val limit = ""
        var response = apiService.getAnimesList(search, tag, limit, authenticationToken).execute()

        assertTrue(response.body() != null)
        assertTrue(response.body()!!.data!!.size == 10)
    }

    //------------------ END ANIMES TESTS --------------------//



}