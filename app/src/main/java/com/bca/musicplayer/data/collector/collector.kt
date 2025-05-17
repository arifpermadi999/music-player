package com.bca.musicplayer.data.collector

sealed class ResultBound<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : ResultBound<T>(data)
    class Loading<T>(data: T? = null) : ResultBound<T>(data)
    class Error<T>(message: String, data: T? = null) : ResultBound<T>(data, message)
}