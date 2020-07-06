package com.marmarovas.animediary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    //Action bar title
    private val _actionBarTitle = MutableLiveData<String>()
    val actionBarTitle: LiveData<String>
        get() = _actionBarTitle

    //Action bar subtitle
    private val _actionBarSubtitle = MutableLiveData<String>()
    val actionBarSubtitle: LiveData<String>
        get() = _actionBarSubtitle

    //Action bar add button clicked
    private val _showAddAnimeReviewList = MutableLiveData<Boolean>()
    val showAddAnimeReviewList: LiveData<Boolean>
        get() = _showAddAnimeReviewList

    //Action bar search
    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String>
        get() = _searchQuery

    private val _searchButtonExpanded = MutableLiveData<Boolean>()
    val searchButtonExpanded : LiveData<Boolean>
        get() = _searchButtonExpanded

    //Action bar show or hide
    private val _showActionBar = MutableLiveData<Boolean>()
    val showActionBar: LiveData<Boolean>
        get() = _showActionBar

    init {
        _actionBarTitle.value = "Anime Diary"
        _actionBarSubtitle.value = ""
        _showAddAnimeReviewList.value = false
    }

    //---------SETTERS---------------------------//

    fun setActionBarTitle(title: String) {
        _actionBarTitle.value = title
    }

    fun setActionBarSubtitle(subtitle: String) {
        _actionBarSubtitle.value = subtitle
    }

    fun onAddReviewAction(isAddButtonClicked: Boolean) {
        _showAddAnimeReviewList.value = isAddButtonClicked
    }

    fun setSearchQuery(query: String?) {
        _searchQuery.value = query
    }

    fun setSearchButtonExpanded(isExpanded : Boolean){
        _searchButtonExpanded.value = isExpanded
    }

    fun setShowActionBar(showActionBar: Boolean) {
        _showActionBar.value = showActionBar
    }

    //----------END SETTERS----------------------//


}