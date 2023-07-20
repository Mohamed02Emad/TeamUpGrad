package com.androiddevs.mvvmnewsapp.data.api

import com.team.cat_hackathon.data.api.API_KEY
import com.team.cat_hackathon.data.models.MyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MyAPI {

    //Todo: change methods to fit our api later

    @GET(getHeadLines)
    suspend fun getHeadLineNews(
        @Query("apiKey")
        apiKey: String = API_KEY,
        @Query("country")
        countryCode: String = "eg",
        @Query("page")
        pageNumber: Int = 1,
        @Query("pageSize")
        pageSize: Int = 20
    ): Response<MyResponse>

    @GET(getEverything)
    suspend fun searchForNews(
        @Query("apiKey")
        apiKey: String = API_KEY,
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("pageSize")
        pageSize: Int = 20
    ): Response<MyResponse>
}