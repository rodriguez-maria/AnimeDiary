package com.marmarovas.animediary.screens.animeslistpage

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun bindImage(imgView : ImageView, imgUrl : String?){
    imgUrl?.let{
        //Convert url string (from XML) to a Uri object
        val imgUri = imgUrl.toUri().buildUpon().scheme("http").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .into(imgView)
    }
}