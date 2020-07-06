package com.marmarovas.animediary.screens.animeslistpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

import com.marmarovas.animediary.R
import com.marmarovas.animediary.SharedViewModel

/**
 * A simple [Fragment] subclass.
 */
class MyCollectionListFragment : AnimesListPageFragment() {

    private val sharedViewModel by activityViewModels<SharedViewModel>()

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
        sharedViewModel.setActionBarTitle(getString(R.string.app_name))
//        sharedViewModel.setActionBarSubtitle("My Collection")

        //show action bar on this fragment
        sharedViewModel.setShowActionBar(true)

        return view
    }

    private fun addObservers(){
        //Observe anime data
        viewModel.animes.observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()){
                displayMessageOnFragment(getString(R.string.empty_dairy_text))
            }
        })

        //Observe search query
        sharedViewModel.searchQuery.observe(viewLifecycleOwner, Observer{
            if(it != null && !it.isEmpty()){
                displayAnimeList(it,true,"",50)
            }
        })

        //Observe search bar expansion
        sharedViewModel.searchButtonExpanded.observe(viewLifecycleOwner, Observer{
            if(!it){
                displayAnimeList("",true,"",50)
            }
        })
    }

}
