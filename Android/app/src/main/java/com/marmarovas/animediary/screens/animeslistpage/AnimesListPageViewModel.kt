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

    /**
     * Retrives a list of animes
     *
     * search : Used for searching animes by. Searches all animes if empty value is passed.
     * myList : true -> get the animes of the logged user. Anything else will return the generic list of animes
     * tags : Searches animes by tags. Comma separated for multiple (will return results that matches any of the tags).
     * limit : The max number of animes to return
     * context : the current context of the fragment
     */
    fun getAnimes(search : String, myList : Boolean, tags : String, limit : Int, context : Context){

        AnimeDiaryAPI.getAnimesList(search, myList, tags, limit, context, object : AnimeDiaryAPICallback<Animes?> {

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
}
