package com.bca.musicplayer.domain.usecase

import com.bca.musicplayer.data.collector.ResultBound
import com.bca.musicplayer.domain.models.Music
import com.bca.musicplayer.domain.repository.IMusicRepository
import kotlinx.coroutines.flow.Flow

class MusicPlayerIterator(private val musicRepository: IMusicRepository) : MusicPlayerUsecase {
    override fun getListMusic(search: String): Flow<ResultBound<List<Music>>> = musicRepository.getListMusic(search)
}