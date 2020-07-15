package com.marmarovas.animediary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marmarovas.animediary.utils.ActionLiveData

class ActionBarViewModel : ViewModel() {

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

//    private val _saveClicked = MutableLiveData<Boolean>()
//    val saveClicked: LiveData<Boolean>
//        get() = _saveClicked

    //--------------------------------------------------------------//
    // ACTIONS
    //--------------------------------------------------------------//

    //set default menu for My Collection screen
    val showMyCollectionPageActionBarMenu = ActionLiveData<Boolean>()

    //Show default menu for Review Page screen
    val showReviewPageActionBarMenu = ActionLiveData<Boolean>()

    //Show default menu for Add Review Page screen
    val showAddReviewPageActionBarMenu = ActionLiveData<Boolean>()

    //Notify certain fragments to go back to previous screen
    val goToMyAnimeCollectionPage = ActionLiveData<Boolean>()

    //Save review
    private val _saveClickedAction = ActionLiveData<Boolean>()
    val saveClickedAction : LiveData<Boolean>
        get() = _saveClickedAction

    //Notify edit has been clicked
    private val _editClickedAction = ActionLiveData<Boolean>()
    val editClickedAction : LiveData<Boolean>
        get() = _editClickedAction

    //Collapse search action bar
    val collapseSearchAction = ActionLiveData<Boolean>()

    //Display save button
    val showSaveButton = ActionLiveData<Boolean>()

    //Notify save button has been clicked
//    val onSaveClicked = ActionLiveData<Boolean>()

    //--------------------------------------------------------------//
    // END OF ACTIONS
    //--------------------------------------------------------------//

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

    fun onSaveClicked(){
        _saveClickedAction.sendAction(true)
    }

    fun onEditClicked(){
        _editClickedAction.sendAction(true)
    }

    fun setShowActionBar(showActionBar: Boolean) {
        _showActionBar.value = showActionBar
    }


    //----------END SETTERS----------------------//



}