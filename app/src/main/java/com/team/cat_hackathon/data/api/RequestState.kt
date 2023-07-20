package com.androiddevs.mvvmnewsapp.data.api

sealed class RequestState<T>(
    val data: T? = null,
    val message: String? = null
) {

    /*
    this is a sealed class like an enum of classes
    we have 3 states for our request
    first state is sucess -> here we create a class to carry our request result (data ....)
    then we have Error -> here we create a class that have a string of the error message
    and finally loading -> if our RequestState is Loading we will show a progress bar until we get error of success
     */

    class Sucess<T>(data: T?) : RequestState<T>(data)
    class Error<T>(message: String?) : RequestState<T>( message = message)
    class Loading<T>() : RequestState<T>()
}