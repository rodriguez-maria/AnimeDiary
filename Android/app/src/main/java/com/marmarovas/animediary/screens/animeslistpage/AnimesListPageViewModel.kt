package com.marmarovas.animediary.screens.animeslistpage

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marmarovas.animediary.network.AnimeDiaryAPI
import com.marmarovas.animediary.network.AnimeDiaryAPICallback
import com.marmarovas.animediary.network.animes.Animes
import com.marmarovas.animediary.network.animes.AnimeData

class AnimesListPageViewModel : ViewModel() {

    val animes = MutableLiveData<List<AnimeData>>()
    val errorMessage = MutableLiveData<String>()
    var isUnauthorized : Boolean = false

    fun getAnimes(search : String, tag : String, limit : String, context : Context){

        AnimeDiaryAPI.getAnimesList(search, tag, limit, context, object : AnimeDiaryAPICallback<Animes?> {

            override fun onFailure(response: Animes?) {
                if(response == null){
                    errorMessage.value = "Unable to process request at the moment."
                } else {
                    Log.d("Animes VM", response.error.toString())
                    isUnauthorized = response.error == "401"
                    errorMessage.value = "Session has expired. Please log in again."
                }
            }

            override fun onSuccess(response: Animes?) {
                animes.value = response!!.data
            }
        })
    }

//    fun getAnimesList(search : String, tag : String, limit : String, token : String){
//
//        AnimeDiaryAPI.retrofitService.getAnimesList(search, tag, limit, token).enqueue(
//            object : Callback<Animes>{
//                override fun onFailure(call: Call<Animes>, t: Throwable) {
//
//                }
//
//                override fun onResponse(call: Call<Animes>, response: Response<Animes>) {
//                    if(response.isSuccessful){
//                        Log.v("AnimesList VM", response.body().toString())
//                    } else {
//                        Log.v("AnimesList VM", response.errorBody().toString())
//                    }
//
//                }
//            }
//        )
//    }
}
