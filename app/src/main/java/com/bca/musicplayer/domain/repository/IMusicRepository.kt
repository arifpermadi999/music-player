package com.bca.musicplayer.domain.repository

import com.bca.musicplayer.data.collector.ResultBound
import com.bca.musicplayer.domain.models.Music
import kotlinx.coroutines.flow.Flow

interface IMusicRepository {
    fun getListMusic(search: String): Flow<ResultBound<List<Music>>>
}