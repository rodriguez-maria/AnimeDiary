package com.marmarovas.animediary.screens.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.marmarovas.animediary.R
import com.marmarovas.animediary.databinding.LoginFragmentBinding


class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    private lateinit var binding:LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_fragment,
            container,
            false
        )

        binding.loginButton.setOnClickListener{ onLogin() }
        binding.createAccountButton.setOnClickListener { onCreateAccount() }

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        //Add observer to the login success variable
        viewModel.loginSuccessful.observe(viewLifecycleOwner, Observer { isSuccessful ->
            logInUser(isSuccessful)
        })

        return  binding.root
    }

     private fun onLogin(){
         verifyUser()
    }

    //Request verification from server through view model
    private fun verifyUser(){
        val username = binding.userNameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        viewModel.loginUser(username, password)
    }

    //Show the user the animes list page if login is successful, otherwise display an error message
    private fun logInUser(isSuccessful : Boolean){
        if(isSuccessful){

            Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_animesListPage)

        } else {
            var message = "Unknown error"

            if(viewModel.error == ("401")){
                //Unauthorized
                message = "Invalid password or user name"
            }

            Toast.makeText(activity, message, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun onCreateAccount() {
        NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_createAccountFragment)
    }

}
