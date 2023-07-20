package com.androiddevs.mvvmnewsapp.data.api

sealed class RequestState<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Sucess<T>(data: T?) : RequestState<T>(data)
    class Error<T>(message: String?) : RequestState<T>( message = message)
    class Loading<T>() : RequestState<T>()
}