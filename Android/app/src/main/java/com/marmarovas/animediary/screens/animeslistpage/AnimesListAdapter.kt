package com.marmarovas.animediary.screens.animeslistpage

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marmarovas.animediary.R
import com.marmarovas.animediary.network.animes.AnimeData

class AnimesListAdapter : RecyclerView.Adapter<AnimeItemViewHolder>() {

    var data = listOf<AnimeData>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: AnimeItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.anime.title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val  view = layoutInflater.inflate(R.layout.anime_item_view, parent,false) as TextView
        return AnimeItemViewHolder(view)
    }
}