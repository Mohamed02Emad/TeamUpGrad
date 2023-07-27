package com.team.cat_hackathon.data.repositories

import RetrofitInstance
import android.content.Context
import android.util.Log
import com.mo_chatting.chatapp.data.dataStore.DataStoreImpl
import com.team.cat_hackathon.data.models.Team
import com.team.cat_hackathon.data.models.AllDataResponse
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.data.source.MyDao
import retrofit2.Response
import kotlin.random.Random

class HomeRepositoryImpl (val dao : MyDao, val context : Context , val dataStoreImpl: DataStoreImpl) {


    suspend fun getCachedUser(): User {
        return dataStoreImpl.getUser()
    }

    suspend fun updateUser(user: User) {

    }
    suspend fun getHomeData(): Response<AllDataResponse>? {
        val token = "Bearer ${dataStoreImpl.getToken().trimEnd().trimStart()}"
      return try {
          RetrofitInstance.api.getAllData(token)
      }catch (e: Exception) {
          null
      }
    }

    fun uploadImage(data: ByteArray) {

    }


}