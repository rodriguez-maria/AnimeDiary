package com.marmarovas.animediary.network.animes

data class Animes (
    val success : Boolean,
    val data : List<AnimeData>?,
    val cursor : String?,
    val error : String?
)