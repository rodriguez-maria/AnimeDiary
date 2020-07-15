package com.marmarovas.animediary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marmarovas.animediary.network.animes.AnimeData

class AnimeItemViewModel : ViewModel() {

    private val _selectedAnimeItem = MutableLiveData<AnimeData>()

    val selectedAnimeItem : LiveData<AnimeData>
        get() = _selectedAnimeItem

    fun setSelectedAnimeItem(item : AnimeData){
        _selectedAnimeItem.value = item
    }
}