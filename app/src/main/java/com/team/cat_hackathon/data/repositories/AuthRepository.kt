package com.team.cat_hackathon.data.repositories

import RetrofitInstance
import android.util.Log
import com.mo_chatting.chatapp.data.dataStore.DataStoreImpl
import com.team.cat_hackathon.data.models.AuthResponse
import com.team.cat_hackathon.data.models.User
import retrofit2.Response

class AuthRepository(val dataStoreImpl: DataStoreImpl) {
    suspend fun loginUser(
        username: String?,
        password: String?,
        deviceName: String?
    ): Response<AuthResponse>? {
        return try {
            RetrofitInstance.api.loginUser(username, password, deviceName)
        }catch (e:Exception) {
            Log.d("mohamed", "loginUser: ${e.message}")
            null}
    }

    suspend fun registerUser(
        username: String?,
        email: String?,
        password: String?
    ): Response<AuthResponse>? {
        return try {
            RetrofitInstance.api.registerUser(username, email, password)
        }catch (e: Exception) {
            null
        }
    }

    suspend fun logOutUser() : Response<AuthResponse>? {
        val token = "Bearer ${dataStoreImpl.getToken().trimEnd().trimStart()}"
        return try {
            RetrofitInstance.api.logOut(token)
        }catch (e:Exception){
            null
        }
    }

    suspend fun cacheUser(user: User, accessToken: String? = null) {
        dataStoreImpl.insertUser(user)
        accessToken?.let {
            dataStoreImpl.insertToken(accessToken)
        }
    }

    suspend fun clearDataStore(){
        dataStoreImpl.logOut()
    }

    suspend fun setUserIsLogged(b: Boolean) {
        dataStoreImpl.setIsLoggedIn(b)
    }

    suspend fun getIsLogged(): Boolean {
        return dataStoreImpl.getIsLoggedIn()
    }

    suspend fun verifyUser(code: String, user: User) : Response<AuthResponse>{
        val code = code.toInt()
        return RetrofitInstance.api.verifyUser(user.email,code)
    }

}