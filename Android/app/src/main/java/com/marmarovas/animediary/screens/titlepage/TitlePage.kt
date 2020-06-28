package com.marmarovas.animediary.screens.titlepage

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.marmarovas.animediary.R
import com.marmarovas.animediary.utils.OnFragmentChangedActionBarListener
import com.marmarovas.animediary.utils.TokenDataAccess

/**
 * A simple [Fragment] subclass.
 */
class TitlePage : Fragment() {

    private lateinit var actionBarListener : OnFragmentChangedActionBarListener

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

        actionBarListener.setActionBarVisibility(false)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_title_page, container, false)
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({showNextFragment()}, 1500)
    }

    //Show login fragment if no token is found, otherwise show the animes list page
    private fun showNextFragment(){
        Log.d("Title page", "show next fragment")
        val token = TokenDataAccess.getToken(context)

        if(token == null){
            findNavController().navigate(R.id.action_titlePage_to_loginFragment)
        } else {
            findNavController().navigate(R.id.action_titlePage_to_animesListPage)
        }
    }

}
