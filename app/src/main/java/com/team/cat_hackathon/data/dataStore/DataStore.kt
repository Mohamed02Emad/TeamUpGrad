package com.mo_chatting.chatapp.data.dataStore

import com.team.cat_hackathon.data.models.User

interface DataStore {
    suspend fun getIsOnBoardingFinished():Boolean

   suspend fun setIsOnBoardingFinished(isOnBoardingFinished:Boolean)

   suspend fun getIsLoggedIn():Boolean

   suspend fun setIsLoggedIn(isLogged:Boolean)

   suspend fun insertUser(user: User)

   suspend fun getUser():User

   suspend fun insertToken(token: String)

   suspend fun getToken(): String
}
