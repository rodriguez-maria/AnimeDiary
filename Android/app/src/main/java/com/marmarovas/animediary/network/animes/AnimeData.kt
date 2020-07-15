package com.marmarovas.animediary.network.animes

data class AnimeData (
    val anime : Anime,
    val notes : String?,
    val rating : Float?,
    val tags : List<String>?
)