package com.team.cat_hackathon.data.repositories

import RetrofitInstance
import android.content.Context
import com.team.cat_hackathon.data.models.TeamsResponse
import com.team.cat_hackathon.data.models.UsersResponse
import com.team.cat_hackathon.data.source.MyDao
import retrofit2.Response

class BaseRepositoryImpl (val dao : MyDao , val context : Context) {

    suspend fun getTeamsByQuery(query: String?): Response<TeamsResponse> {
        return RetrofitInstance.api.getTeamsByQuery(
            query = query
        )
    }

}