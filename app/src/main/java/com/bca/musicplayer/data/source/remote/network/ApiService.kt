package com.dicoding.core.data.source.remote.network

import com.bca.musicplayer.data.source.remote.response.MusicBaseApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search")
    suspend fun searchMusic(@Query("term") search: String,@Query("media") media: String): MusicBaseApiResponse

}