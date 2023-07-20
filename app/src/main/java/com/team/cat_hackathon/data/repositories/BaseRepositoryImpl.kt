package com.team.cat_hackathon.data.repositories

import RetrofitInstance
import android.content.Context
import com.team.cat_hackathon.data.models.MyResponse
import com.team.cat_hackathon.data.source.MyDao
import retrofit2.Response

class BaseRepositoryImpl (val dao : MyDao , val context : Context) {

    suspend fun getUser(email: String?, password: String?): Response<MyResponse> {
        return RetrofitInstance.api.getUser(
            email = email,
            password = password
        )
    }


}