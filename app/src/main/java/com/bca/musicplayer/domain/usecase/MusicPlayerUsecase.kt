package com.bca.musicplayer.domain.usecase

import com.bca.musicplayer.data.collector.ResultBound
import com.bca.musicplayer.domain.models.Music
import kotlinx.coroutines.flow.Flow

interface MusicPlayerUsecase {
    fun getListMusic(search: String): Flow<ResultBound<List<Music>>>
}