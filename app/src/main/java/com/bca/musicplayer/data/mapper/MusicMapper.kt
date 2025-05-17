package com.bca.musicplayer.data.mapper

import com.bca.musicplayer.data.source.remote.response.MusicResponse
import com.bca.musicplayer.domain.models.Music

object MusicMapper {
    fun mapListMusicToDomain(input: List<MusicResponse>): List<Music> = input.map {
        Music(
            musicName = it.trackName,
            artistName = it.artistName,
            previewUrl = it.previewUrl,
            imageUrl = it.artworkUrl100
        );
    }
}