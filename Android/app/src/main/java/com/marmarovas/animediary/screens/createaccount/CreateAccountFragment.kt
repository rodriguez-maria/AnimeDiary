package com.marmarovas.animediary.screens.createaccount

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment

import com.marmarovas.animediary.R
import com.marmarovas.animediary.ActionBarViewModel
import com.marmarovas.animediary.databinding.CreateAccountFragmentBinding

class CreateAccountFragment : Fragment() {

    companion object {
        fun newInstance() = CreateAccountFragment()
    }

    private  val sharedViewModel by activityViewModels<ActionBarViewModel>()

    private lateinit var viewModel: CreateAccountViewModel
    private lateinit var binding : CreateAccountFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.create_account_fragment,
            container,
            false
        )

        viewModel = ViewModelProviders.of(this).get(CreateAccountViewModel::class.java)

        //Add observer to the login success variable
        viewModel.createAccountSuccess.observe(viewLifecycleOwner, Observer { isSuccessful ->
            goToNextFragment(isSuccessful)
        })

        binding.createButton.setOnClickListener { onCreate() }

        //don't show action bar on this fragment
        sharedViewModel.setShowActionBar(false)

        return binding.root
    }

    private fun onCreate(){
        createUser()
    }

    private fun createUser(){
        val name = binding.nameEdittext.text.toString()
        val username = binding.usernameEdittext.text.toString()
        val password = binding.passwordEdittext.text.toString()
        //call view model here
        viewModel.createAccount(name, username,password)
    }

    private fun goToNextFragment(isSuccessful : Boolean){
        if(isSuccessful){
            Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
//            NavHostFragment.findNavController(this).navigate(R.id.action_createAccountFragment_to_animesListPage)
            NavHostFragment.findNavController(this).navigate(R.id.action_createAccountFragment_to_myCollectionListFragment)
        } else {
            Toast.makeText(activity, "Unable to create an account.Try later.", Toast.LENGTH_SHORT)
                .show()
        }
    }

}
