package com.marmarovas.animediary.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.marmarovas.animediary.R

class TokenDataAccess {
    companion object {
        private var token : String? = null

        fun getToken(context : Context?) : String? {
            if(token != null){
                return token
            }

            token = context?.getSharedPreferences(context.getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE)?.getString(context.getString(
                R.string.token), null)
            return token;
        }

        fun setToken(token : String, context : Context){
            this.token = token
            val preferencesEditor: SharedPreferences.Editor = context.getSharedPreferences(context.getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE).edit()
            preferencesEditor.putString(context.getString(R.string.token), token)
            preferencesEditor.apply()
        }

        fun setTokenToNull(context : Context){
            token = null
            val preferencesEditor: SharedPreferences.Editor = context.getSharedPreferences(context.getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE).edit()
            preferencesEditor.putString(context.getString(R.string.token), token)
            preferencesEditor.apply()
        }
    }

}