package com.marmarovas.animediary.screens.animeslistpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.marmarovas.animediary.R
import com.marmarovas.animediary.ActionBarViewModel
import com.marmarovas.animediary.network.animes.AnimeData

/**
 * A simple [Fragment] subclass.
 */
class AddAnimeListFragment : AnimesListPageFragment() {

    private val actionBarViewModel by activityViewModels<ActionBarViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        //Set title for layout list
        setLayoutTitle(getString(R.string.select_anime_text))

        //Get list of animes to add to user's collection
        displayAnimeList("", false, "", 50)

        //Add observers
        addObservers()

        //set right action bar menu for this screen
        actionBarViewModel.showAddReviewPageActionBarMenu.sendAction(true)

        //show action bar on this fragment
        actionBarViewModel.setShowActionBar(true)

        return view
    }

    override fun onDestroy() {
        actionBarViewModel.collapseSearchAction.sendAction(true)
        super.onDestroy()
    }

    private fun addObservers(){
        //Observe when to return to previous screen
        actionBarViewModel.goToMyAnimeCollectionPage.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(R.id.action_addAnimeListFragment_to_myCollectionListFragment)
            }
        })
    }

    override fun navigateToReviewPage(item : AnimeData){
        super.navigateToReviewPage(item)
        val action = AddAnimeListFragmentDirections.navigateToReviewPage(true)
        findNavController().navigate(action)
    }

}
