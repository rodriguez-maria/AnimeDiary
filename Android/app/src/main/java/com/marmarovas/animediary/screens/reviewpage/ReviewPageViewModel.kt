package com.marmarovas.animediary.screens.reviewpage

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marmarovas.animediary.network.AnimeDiaryAPI
import com.marmarovas.animediary.network.AnimeDiaryAPICallback
import com.marmarovas.animediary.network.animes.AnimeData
import com.marmarovas.animediary.network.animes.Animes

class ReviewPageViewModel : ViewModel() {

    private var _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private var _success = MutableLiveData<String>()
    val success: LiveData<String>
        get() = _success

    fun confirmChanges(
        itemBeforeChanges: AnimeData,
        itemAfterChanges: AnimeData,
        context: Context?
    ) {
        if (itemBeforeChanges != itemAfterChanges) {
            saveChanges(itemAfterChanges, context)
        }
    }

    fun saveChanges(item: AnimeData, context: Context?) {
        AnimeDiaryAPI.addOrUpdateReview(
            item.anime.id,
            item.rating,
            item.notes,
            item.tags,
            context,
            object : AnimeDiaryAPICallback<Animes?> {
                override fun onSuccess(response: Animes?) {
                    //nothing to do in this case
                    _success.value = "Changes successfully saved."
                }

                override fun onFailure(response: Animes?) {
                    if (response == null) {
                        _error.value = "Could not save changes. Unable to contact server."
                    } else {
                        when (response.error) {
                            "400" -> {
                                _error.value = "Invalid input."
                                true
                            }
                            "404" -> {
                                _error.value = "Anime not found"
                                true
                            }
                            "500" -> {
                                _error.value =
                                    "Something went wrong. Check your internet connection."
                                true
                            }
                            else -> {
                                false
                            }
                        }
                    }
                }

            }
        )
    }
}

