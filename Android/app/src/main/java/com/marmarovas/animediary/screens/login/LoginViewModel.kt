package com.marmarovas.animediary.screens.login

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.marmarovas.animediary.network.AnimeDiaryAPI
import com.marmarovas.animediary.network.AnimeDiaryAPICallback
import com.marmarovas.animediary.network.login.Login
import com.marmarovas.animediary.utils.TokenDataAccess
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel(application : Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    val loginSuccessful = MutableLiveData<Boolean>()

    var error : String = ""

    fun loginUser(username : String, password : String){

        AnimeDiaryAPI.loginUser(context, username, password, object : AnimeDiaryAPICallback<String>{

            override fun onSuccess(response: String) {
                Log.d("Login success VM", response)
                //success response on onSuccess method will always be true
                loginSuccessful.value = true
            }

            override fun onFailure(response : String) {
                Log.d("Login failure VM", response)
                error = response
                loginSuccessful.value = false
            }
        } )
    }
}
