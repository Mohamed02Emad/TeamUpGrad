package com.team.cat_hackathon.data.repositories

import RetrofitInstance
import com.mo_chatting.chatapp.data.dataStore.DataStoreImpl
import com.team.cat_hackathon.data.models.MessageResponse
import com.team.cat_hackathon.data.models.TeamWithUsersResponse
import com.team.cat_hackathon.data.models.User
import retrofit2.Response
import kotlin.random.Random

class TeamsRepository(val dataStoreImpl: DataStoreImpl) {
    suspend fun getTeamById(
        teamId: Int
    ): Response<TeamWithUsersResponse>? {
        val token = "Bearer ${dataStoreImpl.getToken().trimEnd().trimStart()}"
        return try {
            RetrofitInstance.api.getTeamDetails(token, teamId)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateCacheUser(user: User) {
        dataStoreImpl.insertUser(user)
    }

    suspend fun getCachedUser() = dataStoreImpl.getUser()

    suspend fun getUsers(teamId: Int): List<User> = try {
        val token = "Bearer ${dataStoreImpl.getToken().trimEnd().trimStart()}"
        val response = RetrofitInstance.api.getTeamDetails(token, teamId)
        if (response.isSuccessful) {
            response.body()!!.team.members
        } else {
            emptyList<User>()
        }
    } catch (e: Exception) {
        emptyList<User>()
    }


    private fun generateRandomString(length: Int): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    suspend fun sendJoinRequest(teamId: Int): Response<MessageResponse>? {
        val token = "Bearer ${dataStoreImpl.getToken().trimEnd().trimStart()}"
        return try {
            RetrofitInstance.api.requestToJoinTeam(token, teamId)
        } catch (e: Exception) {
            null
        }
    }

}