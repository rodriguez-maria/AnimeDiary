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
import com.marmarovas.animediary.SharedViewModel

/**
 * A simple [Fragment] subclass.
 */
class AddAnimeListFragment : AnimesListPageFragment() {

    private val sharedViewModel by activityViewModels<SharedViewModel>()

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

        //show action bar on this fragment
        sharedViewModel.setShowActionBar(true)

        return view
    }

    private fun addObservers(){
        //Observe when search action button is collapsed
        sharedViewModel.searchButtonExpanded.observe(viewLifecycleOwner, Observer {
            if(!it){
                findNavController().navigate(R.id.action_addAnimeListFragment_to_myCollectionListFragment)
            }
        })
    }

}
