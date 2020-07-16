package com.marmarovas.animediary.screens.animeslistpage

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
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

        //Set action bar title and subtitle
        actionBarViewModel.setActionBarTitle(getString(R.string.app_name))

        //show action bar on this fragment
        actionBarViewModel.setShowActionBar(true)

        //Allow this fragment to access the menu items in the action bar
        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        addReviewMenuItem = menu.findItem(R.id.action_add_review)
        searchMenuItem = menu.findItem(R.id.action_search)
        logOutMenuItem = menu.findItem(R.id.action_log_out)

        showActionBarMenuItems(false)
        searchMenuItem.expandActionView()

        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                navigateToMyCollectionsFragment()
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }
        })
    }

    override fun navigateToReviewPage(item: AnimeData) {
        super.navigateToReviewPage(item)
        val action = AddAnimeListFragmentDirections.navigateToReviewPage(true)
        findNavController().navigate(action)
    }

    private fun navigateToMyCollectionsFragment() {
        findNavController().navigate(R.id.action_addAnimeListFragment_to_myCollectionListFragment)
    }

}
