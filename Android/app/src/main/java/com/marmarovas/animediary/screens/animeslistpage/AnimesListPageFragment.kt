package com.marmarovas.animediary.screens.animeslistpage


import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.marmarovas.animediary.R
import com.marmarovas.animediary.databinding.AnimesListPageFragmentBinding
import com.marmarovas.animediary.network.animes.AnimeData
import com.marmarovas.animediary.utils.OnFragmentChangedActionBarListener

class AnimesListPageFragment : Fragment() {

    private val adapter = AnimesListAdapter()

    private lateinit var viewModel: AnimesListPageViewModel

    private lateinit var binding : AnimesListPageFragmentBinding

    private lateinit var actionBarListener : OnFragmentChangedActionBarListener

    companion object {
        fun getInstance() : AnimesListPageFragment {
            return this.getInstance()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            actionBarListener = context as OnFragmentChangedActionBarListener
        } catch (e : ClassCastException){
            throw java.lang.ClassCastException(context.toString() + e.message)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil
            .inflate(inflater, R.layout.animes_list_page_fragment,container,false)

        viewModel = ViewModelProviders.of(this).get(AnimesListPageViewModel::class.java)

        binding.animesListRecyclerview.adapter = adapter

        val ctx = context
        if(ctx == null){
            showShortMessage("Unable to retrieve data")
        } else {
            viewModel.getAnimes("", getString(R.string.list_tag_my_list), "50", ctx)
        }

        //show action bar on this fragment
        actionBarListener.setActionBarVisibility(true)

        //Observe data
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer{
            goToLoginPageOrStay(it)
        })

        viewModel.animes.observe(viewLifecycleOwner, Observer {
            it?.let{
                setAdapterOrDisplayEmptyListMessage(it)
            }
        })

        return binding.root
    }

    private fun goToLoginPageOrStay (message : String){
        showShortMessage(message)
        if(viewModel.isUnauthorized){
            findNavController().navigate(R.id.action_go_back_to_title_page)
        }
    }

    private fun showShortMessage(message : String){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setAdapterOrDisplayEmptyListMessage(list : List<AnimeData>){
        if(list.isEmpty()){
            binding.emptyDiaryTextview.visibility = View.VISIBLE
            binding.animesListRecyclerview.visibility = View.GONE
            //TODO: show animation around the add button in the app bar
        } else {
            binding.emptyDiaryTextview.visibility = View.GONE
            binding.animesListRecyclerview.visibility = View.VISIBLE
            adapter.data = list
        }
    }

    fun addAnimeReview(){

    }
}
