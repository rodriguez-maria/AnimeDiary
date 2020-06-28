package com.marmarovas.animediary.screens.createaccount

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.marmarovas.animediary.network.AnimeDiaryAPI
import com.marmarovas.animediary.network.registeruser.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateAccountViewModel(application : Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    val createAccountSuccess = MutableLiveData<Boolean>()

    fun createAccount(name : String, username : String, password : String){
        AnimeDiaryAPI.retrofitService.registerUser(username, password, name).enqueue(
            object: Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.isSuccessful){
                        Log.d("Create Account Response", response.body().toString())
                        //save toke
//                        saveToken()
                        createAccountSuccess.value = true
                    } else {
                        Log.d("Create Account Response", response.errorBody().toString() + "error" + response.code())
                        createAccountSuccess.value = false
                    }
                }
            }
        )
    }

    private fun saveToken(token : String){
        val preferencesEditor: SharedPreferences.Editor = context.getSharedPreferences("com.marmarovas.animediary", Context.MODE_PRIVATE).edit()
        preferencesEditor.putString("TOKEN", token)
        preferencesEditor.apply()
    }
}
