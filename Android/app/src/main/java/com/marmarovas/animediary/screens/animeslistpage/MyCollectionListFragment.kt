package com.marmarovas.animediary.screens.animeslistpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        //Display the right menu for this screen
        actionBarViewModel.showMyCollectionPageActionBarMenu.sendAction(true)

        //show action bar on this fragment
        actionBarViewModel.setShowActionBar(true)

        return view
    }

    private fun addObservers(){
        //Observe anime data
        viewModel.animes.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "animes in my collection observer", Toast.LENGTH_SHORT).show()
            if(it.isEmpty()){
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

        //Observe search bar expansion
        actionBarViewModel.searchButtonExpanded.observe(viewLifecycleOwner, Observer{
            Toast.makeText(context, "search button expanded", Toast.LENGTH_SHORT).show()
            if(!it){
                displayAnimeList("",true,"",50)
            }
        })
    }

    override fun navigateToReviewPage(item : AnimeData){
        super.navigateToReviewPage(item)
        val action = AddAnimeListFragmentDirections.navigateToReviewPage(false)
        findNavController().navigate(action)
    }

}
