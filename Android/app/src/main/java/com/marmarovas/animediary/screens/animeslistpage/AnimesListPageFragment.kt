package com.marmarovas.animediary.screens.animeslistpage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.marmarovas.animediary.R
import com.marmarovas.animediary.databinding.AnimesListPageFragmentBinding
import com.marmarovas.animediary.network.animes.AnimeData
import com.marmarovas.animediary.utils.OnFragmentChangedActionBarListener

open class AnimesListPageFragment : Fragment() {

    private val adapter = AnimesListAdapter()
    val viewModel by viewModels<AnimesListPageViewModel>()

    private lateinit var binding: AnimesListPageFragmentBinding

    private lateinit var actionBarListener: OnFragmentChangedActionBarListener

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
            goToLoginPageOrStay(it)
        })

        //Observe the anime data
        viewModel.animes.observe(viewLifecycleOwner, Observer {
            it?.let {
                setAdapterOrDisplayEmptyListMessage(it)
            }
        })
    }

    fun displayAnimeList(search: String, myList: Boolean, tags: String, limit: Int) {
        val ctx = context
        if (ctx == null) {
            showShortMessage("Unable to retrieve data")
        } else {
            viewModel.getAnimes(search, myList, tags, limit, ctx)
        }
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
//        adapter.data = list
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
