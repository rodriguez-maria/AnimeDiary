package com.marmarovas.animediary.utils

    import android.widget.ImageView
    import androidx.core.net.toUri
    import com.bumptech.glide.Glide

    object AnimeDiaryUtils {

        fun bindImage(imgView: ImageView, imgUrl: String?) {
            imgUrl?.let {
                //Convert url string (from XML) to a Uri object
                val imgUri = imgUrl.toUri().buildUpon().scheme("http").build()
                Glide.with(imgView.context)
                    .load(imgUri)
                    .fitCenter()
                    .into(imgView)
            }
        }
    }