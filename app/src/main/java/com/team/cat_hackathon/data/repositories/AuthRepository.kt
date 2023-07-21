package com.team.cat_hackathon.data.repositories

import RetrofitInstance
import android.util.Log
import com.mo_chatting.chatapp.data.dataStore.DataStoreImpl
import com.team.cat_hackathon.data.models.LoginResponse
import com.team.cat_hackathon.data.models.User
import retrofit2.Response
import kotlin.math.log

class AuthRepository(val dataStoreImpl: DataStoreImpl) {
    suspend fun loginUser(username: String?, password: String?, deviceName: String?):Response<LoginResponse>{
        return RetrofitInstance.api.loginUser(username, password, deviceName)
    }

    suspend fun cacheUSer(user: User, accessToken: String) {
        dataStoreImpl.insertUser(user)
        dataStoreImpl.insertToken(accessToken)
    }

}