package com.team.cat_hackathon.data.repositories

import RetrofitInstance
import com.mo_chatting.chatapp.data.dataStore.DataStoreImpl
import com.team.cat_hackathon.data.models.JoinRequestsResponse
import com.team.cat_hackathon.data.models.MessageResponse
import com.team.cat_hackathon.data.models.TeamWithUsersResponse
import com.team.cat_hackathon.data.models.User
import retrofit2.Response

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

    suspend fun getTeamUsers(teamId: Int): List<User> = try {
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

    suspend fun sendJoinRequest(teamId: Int): Response<MessageResponse>? {
        val token = "Bearer ${dataStoreImpl.getToken().trimEnd().trimStart()}"
        return try {
            RetrofitInstance.api.requestToJoinTeam(token, teamId)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getRequesteToJoinList(): Response<JoinRequestsResponse>? = try {
        val token = "Bearer ${dataStoreImpl.getToken().trimEnd().trimStart()}"
        val userTeamId = getCachedUser().team_id!!
        RetrofitInstance.api.getJoinTeamRequests(token, userTeamId)
    } catch (e: Exception) {
        null
    }

    suspend fun acceptUser(userId: Int): Response<MessageResponse> {
        val token = "Bearer ${dataStoreImpl.getToken().trimEnd().trimStart()}"
        val userTeamId = getCachedUser().team_id!!
        return RetrofitInstance.api.acceptUser(token, userTeamId, userId)
    }

    suspend fun rejectUser(userId: Int): Response<MessageResponse> {
        val token = "Bearer ${dataStoreImpl.getToken().trimEnd().trimStart()}"
        val userTeamId = getCachedUser().team_id!!
        return RetrofitInstance.api.rejectUser(token, userTeamId, userId)
    }

    suspend fun deleteMember(userId : Int ,teamId: Int) : Response<MessageResponse>{
        val token = "Bearer ${dataStoreImpl.getToken().trimEnd().trimStart()}"

        return RetrofitInstance.api.deleteMember(token , teamId , userId)
    }

    suspend fun deleteTeam(teamId:Int) : Response<MessageResponse>{
        val token = "Bearer ${dataStoreImpl.getToken().trimEnd().trimStart()}"
        return RetrofitInstance.api.deleteTeam(token, teamId)
    }

    suspend fun leaveCurrentTeamInCache() {
        val currentUser = getCachedUser()
        currentUser.team_id = null
        currentUser.isLeader = 0
        updateCacheUser(currentUser)
    }

    suspend fun leaveTeam(teamId: Int): Response<MessageResponse> {
        val token = "Bearer ${dataStoreImpl.getToken().trimEnd().trimStart()}"
        return RetrofitInstance.api.leaveTeam(token, teamId)
    }
    suspend fun updateTeam(teamId: Int , name :String , description : String): Response<MessageResponse> {
        val token = "Bearer ${dataStoreImpl.getToken().trimEnd().trimStart()}"
        return RetrofitInstance.api.updateTeam(token, teamId , name , description)
    }

    suspend fun syncUser() {
        try {
            val token = "Bearer ${dataStoreImpl.getToken().trimEnd().trimStart()}"
            val response = RetrofitInstance.api.updateUser(
                token = token
            )
            if (response.isSuccessful){
                response.body()?.let {response->
                    updateCacheUser(response.user!!)
                }
            }
        }catch(_:java.lang.Exception){}
    }

}