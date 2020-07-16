package com.marmarovas.animediary.screens.animeslistpage

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.marmarovas.animediary.R
import com.marmarovas.animediary.ActionBarViewModel
import com.marmarovas.animediary.network.animes.AnimeData

/**
 * A simple [Fragment] subclass.
 */
class MyCollectionListFragment : AnimesListPageFragment() {

    private val actionBarViewModel by activityViewModels<ActionBarViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = super.onCreateView(inflater, container, savedInstanceState)

        //Set "My Collection" title for layout list
        setLayoutTitle(getString(R.string.my_collection_text))

        //Get user's collection of animes
        displayAnimeList("", true, "", 50)

        //Add observers
        addObservers()

        //Set action bar title and subtitle
        actionBarViewModel.setActionBarTitle(getString(R.string.app_name))
//        sharedViewModel.setActionBarSubtitle("My Collection")

        //show action bar on this fragment
        actionBarViewModel.setShowActionBar(true)

        setHasOptionsMenu(true)

        return view
    }

    private fun addObservers() {
        //Observe anime data
        viewModel.animes.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "animes in my collection observer", Toast.LENGTH_SHORT).show()
            if (it.isEmpty()) {
                displayMessageOnFragment(getString(R.string.empty_dairy_text))
            }
        })

//        //Observe search query
//        actionBarViewModel.searchQuery.observe(viewLifecycleOwner, Observer{
//            Toast.makeText(context, "search query observer", Toast.LENGTH_SHORT).show()
//            if(it != null && !it.isEmpty()){
//                displayAnimeList(it,true,"",50)
//                actionBarViewModel.setSearchQuery(null)
//            }
//        })
    }

    override fun navigateToReviewPage(item: AnimeData) {
        super.navigateToReviewPage(item)
        val action = AddAnimeListFragmentDirections.navigateToReviewPage(false)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        addReviewMenuItem = menu.findItem(R.id.action_add_review)
        searchMenuItem = menu.findItem(R.id.action_search)
        logOutMenuItem = menu.findItem(R.id.action_log_out)

        showActionBarMenuItems(true)

        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                activity?.invalidateOptionsMenu()
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                showActionBarMenuItems(false)
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_add_review -> {
            findNavController().navigate(R.id.action_myCollectionListFragment_to_addAnimeListFragment)
            true
        }
        else -> {
            false
        }
    }
}

