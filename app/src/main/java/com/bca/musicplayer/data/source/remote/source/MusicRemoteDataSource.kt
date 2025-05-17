package com.bca.musicplayer.data.source.remote.source

import com.bca.musicplayer.data.source.remote.response.MusicResponse
import com.dicoding.core.data.source.remote.network.ApiResponse
import com.dicoding.core.data.source.remote.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class MusicRemoteDataSource(private val apiService: ApiService) {
    fun getAllMusic(search:String): Flow<ApiResponse<List<MusicResponse>>> {
        //get data from remote api
        return flow {
            try {
                val response = apiService.searchMusic(search,"music")
                if (response.resultCount > 0){
                    val dataResponse = response.results
                    emit(ApiResponse.Success(dataResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e : Exception){
                emit(ApiResponse.Error(e.toString()))
                Timber.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}