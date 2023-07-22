package com.team.cat_hackathon.data.repositories

import RetrofitInstance
import android.util.Log
import com.mo_chatting.chatapp.data.dataStore.DataStoreImpl
import com.team.cat_hackathon.data.models.LoginResponse
import com.team.cat_hackathon.data.models.User
import retrofit2.Response

class AuthRepository(val dataStoreImpl: DataStoreImpl) {
    suspend fun loginUser(username: String?, password: String?, deviceName: String?):Response<LoginResponse>{
        Log.d("mohamed", "loginUser: sending")
        val response = RetrofitInstance.api.loginUser(username, password, deviceName)
        Log.d("mohamed", "loginUser: received ")
        return response
    }

    suspend fun cacheUSer(user: User, accessToken: String) {
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