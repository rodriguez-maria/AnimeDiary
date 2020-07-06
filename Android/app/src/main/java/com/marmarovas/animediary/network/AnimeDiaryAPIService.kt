package com.marmarovas.animediary.network

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.annotation.NonNull
import com.marmarovas.animediary.R
import com.marmarovas.animediary.network.animes.Anime
import com.marmarovas.animediary.network.animes.Animes
import com.marmarovas.animediary.network.login.Login
import com.marmarovas.animediary.network.registeruser.User
import com.marmarovas.animediary.utils.CacheDataAccess
import com.marmarovas.animediary.utils.TokenDataAccess
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL =
    "http://192.168.0.23:8080/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface AnimeDiaryAPICallback<T> {
    fun onSuccess(response: T);
    fun onFailure(response: T);
}

interface AnimeDiaryAPIService {
    @FormUrlEncoded
    @POST("login")
    fun login(@Field("userName") username: String, @Field("password") password: String):
            Call<Login>

    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("userName") username: String, @Field("password") password: String, @Field(
            "name"
        ) name: String
    ):
            Call<User>

    @GET("animes")
    fun getAnimesList(
        @Header("Authorization") token: String,
        @Query("search") search: String,
        @Query("my_list") myList: Boolean,
        @Query("tags") tags: String,
        @Query("limit") limit: Int
    ):
            Call<Animes>
}

//Defines a AnimeDiaryAPI object to initialize the Retrofit service as well as methods to process calls to server
object AnimeDiaryAPI {
    val retrofitService: AnimeDiaryAPIService by lazy {
        retrofit.create(AnimeDiaryAPIService::class.java)
    }

    //
    /**
     * Save token and return error code or message on failure
     */
    fun loginUser(
        context: Context,
        username: String,
        password: String,
        responseListener: AnimeDiaryAPICallback<String>
    ) {
        retrofitService.login(username, password).enqueue(
            object : Callback<Login> {

                override fun onFailure(call: Call<Login>, t: Throwable) {
                    // there is more than just a failing request (like: no internet connection)
                    Log.d("Login User Failure", t.toString())
                    responseListener.onFailure(t.toString())
                }

                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    if (response.isSuccessful) {
                        Log.d("Login User Success", response.toString())
//                      //Save token
                        TokenDataAccess.setToken(response.body()!!.data!!.token, context)
                        responseListener.onSuccess("true")
                    } else {
                        responseListener.onFailure(response.raw().code().toString())
                    }
                }
            })
    }

    /**
     * Return an Animes object
     *
     * search : Used for searching animes by. Searches all animes if empty value is passed.
     * myList : true -> get the animes of the logged user. Anything else will return the generic list of animes
     * tags : Searches animes by tags. Comma separated for multiple (will return results that matches any of the tags).
     * limit : The max number of animes to return
     * context : the current context of the fragment
     */
    fun getAnimesList(
        search: String,
        myList : Boolean,
        tags: String,
        limit: Int,
        context: Context,
        animeCallback: AnimeDiaryAPICallback<Animes?>
    ) {

        val token = TokenDataAccess.getToken(context)

        retrofitService.getAnimesList("Bearer $token" ?: "", search, myList, tags, limit).enqueue(
            object : Callback<Animes> {

                override fun onFailure(call: Call<Animes>, t: Throwable) {
                    Log.d("Animes server failure", t.toString())
                    animeCallback.onFailure(null)
                }

                override fun onResponse(call: Call<Animes>, response: Response<Animes>) {
                    if (response.isSuccessful) {

                        Log.d("Animes success", response.body().toString())
                        animeCallback.onSuccess(response.body())

                    } else {

                        Log.v("Animes failure", response.errorBody().toString())

                        //Remove current token from shared preferences
                        TokenDataAccess.setTokenToNull(context)

                        var animesObj = Animes(
                            false,
                            null,
                            null,
                            response.code().toString()
                        )
                        animeCallback.onFailure(animesObj)
                    }
                }
            }
        )
    }

}

