package com.bca.musicplayer.domain.models


data class Music (
    val musicName: String,
    val artistName: String,
    val previewUrl: String,
    val imageUrl: String,
    var isPlay: Boolean = false,
)