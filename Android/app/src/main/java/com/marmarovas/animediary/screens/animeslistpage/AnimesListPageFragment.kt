package com.marmarovas.animediary.screens.animeslistpage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.marmarovas.animediary.AnimeItemViewModel

import com.marmarovas.animediary.R
import com.marmarovas.animediary.databinding.AnimesListPageFragmentBinding
import com.marmarovas.animediary.network.animes.AnimeData

open class AnimesListPageFragment : Fragment() {

    val viewModel by viewModels<AnimesListPageViewModel>()
    val animeItemViewModel by activityViewModels<AnimeItemViewModel>()

    private val adapter = AnimesListAdapter(){
        it?.let{
            navigateToReviewPage(it)
        }
    }

    private lateinit var binding: AnimesListPageFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil
            .inflate(inflater, R.layout.animes_list_page_fragment, container, false)

        binding.animesListRecyclerview.adapter = adapter

        //Observe data
        addObservers()

        return binding.root
    }

    override fun onDestroy() {
        Log.d("AnimesListPage Fragment", "on destroy called")
        super.onDestroy()
    }

    private fun addObservers() {
        //Observe whether or not we could not retrieve the list of animes from the server
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "error message observer", Toast.LENGTH_SHORT).show()
            goToLoginPageOrStay(it)
        })

        //Observe the anime data
        viewModel.animes.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "animes observer", Toast.LENGTH_SHORT).show()
            it?.let {
                setAdapterOrDisplayEmptyListMessage(it)
            }
        })
    }

    private fun goToLoginPageOrStay(message: String) {
        showShortMessage(message)
        if (viewModel.isUnauthorized) {
            findNavController().navigate(R.id.action_go_back_to_title_page)
        }
    }

    private fun showShortMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setAdapterOrDisplayEmptyListMessage(list: List<AnimeData>) {
        binding.emptyListMessageTextview.visibility = View.GONE
        binding.animesListRecyclerview.visibility = View.VISIBLE
        adapter.submitList(list)
    }

    open fun navigateToReviewPage(item : AnimeData){
        animeItemViewModel.setSelectedAnimeItem(item)
//        findNavController().navigate(R.id.navigate_to_review_page)
//        viewModel.onNavigatedToReviewPage()
    }
//
    fun displayAnimeList(search: String, myList: Boolean, tags: String, limit: Int) {
        val ctx = context
        if (ctx == null) {
            showShortMessage("Unable to retrieve data")
        } else {
            viewModel.getAnimes(search, myList, tags, limit, ctx)
        }
    }

    fun setLayoutTitle(title: String) {
        binding.layoutTitleTextView.text = title
    }

    fun displayMessageOnFragment(message: String) {
        //TODO: show animation around the add button in the app bar

        binding.emptyListMessageTextview.text = message
        binding.emptyListMessageTextview.visibility = View.VISIBLE

        binding.animesListRecyclerview.visibility = View.GONE
    }
}
