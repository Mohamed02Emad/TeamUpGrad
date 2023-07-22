package com.team.cat_hackathon.data.repositories

import RetrofitInstance
import com.mo_chatting.chatapp.data.dataStore.DataStoreImpl
import com.team.cat_hackathon.data.models.AuthResponse
import com.team.cat_hackathon.data.models.User
import retrofit2.Response

class AuthRepository(val dataStoreImpl: DataStoreImpl) {
    suspend fun loginUser(
        username: String?,
        password: String?,
        deviceName: String?
    ): Response<AuthResponse> {
        return RetrofitInstance.api.loginUser(username, password, deviceName)
    }

    suspend fun registerUser(
        username: String?,
        email: String?,
        password: String?
    ) : Response<AuthResponse>{
      return RetrofitInstance.api.registerUser(username , email, password)
    }

    suspend fun cacheUser(user: User, accessToken: String) {
        dataStoreImpl.insertUser(user)
        dataStoreImpl.insertToken(accessToken)
    }

    suspend fun setUserIsLogged(b: Boolean) {
        dataStoreImpl.setIsLoggedIn(b)
    }

    suspend fun getIsLogged(): Boolean {
        return dataStoreImpl.getIsLoggedIn()
    }

}