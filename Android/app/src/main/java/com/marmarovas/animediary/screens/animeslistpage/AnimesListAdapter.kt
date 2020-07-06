package com.marmarovas.animediary.screens.animeslistpage

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marmarovas.animediary.R
import com.marmarovas.animediary.databinding.ListItemAnimeBinding
import com.marmarovas.animediary.network.animes.AnimeData

class AnimesListAdapter : ListAdapter<AnimeData, AnimesListAdapter.ViewHolder>(AnimeDataDiffCallback()) {

//    var data = listOf<AnimeData>()
//        set(value){
//            field = value
//            notifyDataSetChanged()
//        }

//    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding : ListItemAnimeBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : AnimeData){
            binding.animeData = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent : ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val  binding = ListItemAnimeBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class AnimeDataDiffCallback : DiffUtil.ItemCallback<AnimeData>() {
        override fun areItemsTheSame(oldItem: AnimeData, newItem: AnimeData): Boolean {
            return oldItem.anime.id == newItem.anime.id
        }

        override fun areContentsTheSame(oldItem: AnimeData, newItem: AnimeData): Boolean {
            return oldItem == newItem
        }


    }
}