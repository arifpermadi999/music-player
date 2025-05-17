package com.bca.musicplayer.data.repository

import com.bca.musicplayer.data.collector.ResultBound
import com.bca.musicplayer.data.mapper.MusicMapper
import com.bca.musicplayer.data.source.remote.response.MusicResponse
import com.bca.musicplayer.data.source.remote.source.MusicRemoteDataSource
import com.bca.musicplayer.domain.models.Music
import com.bca.musicplayer.domain.repository.IMusicRepository
import com.dicoding.core.data.source.remote.network.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MusicRepository(private val musicRemoteDataSource: MusicRemoteDataSource) : IMusicRepository {
    override fun getListMusic(search: String): Flow<ResultBound<List<Music>>> {
        return musicRemoteDataSource.getAllMusic(search)
            .map { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        val domainData = MusicMapper.mapListMusicToDomain(response.data)
                        ResultBound.Success(domainData)
                    }
                    is ApiResponse.Empty -> {
                        ResultBound.Success(emptyList())  // bisa juga Error/Empty tergantung kebijakan kamu
                    }
                    is ApiResponse.Error -> {
                        ResultBound.Error(response.errorMessage)
                    }
                    is ApiResponse.Loading -> {
                        ResultBound.Loading()
                    }
                }
            }
    }
}