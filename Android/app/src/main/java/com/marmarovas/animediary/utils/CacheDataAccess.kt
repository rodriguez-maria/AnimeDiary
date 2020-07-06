package com.marmarovas.animediary.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marmarovas.animediary.R
import com.marmarovas.animediary.network.animes.AnimeData

class CacheDataAccess {

    companion object {
        private var animeData: List<AnimeData>? = null

        /**
         * Cache serialized anime data
         */
        fun cacheAnimeData(data: List<AnimeData>?, context: Context) {
            animeData = data
            var serializedData: String = Gson().toJson(data)

            val preferencesEditor: SharedPreferences.Editor = context.getSharedPreferences(
                context.getString(
                    R.string.shared_pref_file_name
                ), Context.MODE_PRIVATE
            ).edit()
            preferencesEditor.putString("ANIME_DATA_CACHE", serializedData)
            preferencesEditor.apply()
        }

        /**
         * Return the deserialize anime data
         */
        fun getCacheAnimeData(context: Context): List<AnimeData>? {
            if (animeData != null) {
                return animeData
            }

            val serializedData = context.getSharedPreferences(
                context.getString(R.string.shared_pref_file_name),
                Context.MODE_PRIVATE
            )?.getString("ANIME_DATA_CACHE", null)

            val type = object : TypeToken<AnimeData>(){}.type
            animeData = Gson().fromJson(serializedData, type)

            return animeData;
        }

    }
}