package com.bca.musicplayer.ui.music

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bca.musicplayer.domain.usecase.MusicPlayerUsecase

class MusicViewModel(private val musicPlayerUsecase: MusicPlayerUsecase) : ViewModel() {
    fun getListMusic(search: String) = musicPlayerUsecase.getListMusic(search).asLiveData()
}