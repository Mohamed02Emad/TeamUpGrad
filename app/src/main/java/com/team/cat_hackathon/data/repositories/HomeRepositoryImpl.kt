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

    fun getFakeTeams(numUser: Int):ArrayList<Team> {
        val teams = ArrayList<Team>()
        for (i in 1..numUser) {
             val bio = generateRandomString(90)
             val name = "name "+generateRandomString(6)
            teams.add(Team(0,name, bio ,"numberOfMEmbers", "listOfUsers"))
        }
        return teams
    }
    fun getFakeUsers(numUsers: Int): ArrayList<User> {
        val userList = ArrayList<User>()
        for (i in 1..numUsers) {
            val id = i
            val name = generateRandomString(8)
            val email = "${generateRandomString(6)}@example.com"
            val emailVerifiedAt = if (Random.nextBoolean()) generateRandomString(10) else null
            val track = if (Random.nextBoolean()) generateRandomString(5) else null
            val bio = if (Random.nextBoolean()) generateRandomString(20) else null
           // val imageUrl = if (Random.nextBoolean()) "https://example.com/image$i.jpg" else null
            val imageUrl = "https://placebear.com/g/200/200"
            val githubUrl = if (Random.nextBoolean()) "https://github.com/user$i" else null
            val facebookUrl = if (Random.nextBoolean()) "https://facebook.com/user$i" else null
            val linkedinUrl = if (Random.nextBoolean()) "https://linkedin.com/user$i" else null
            val isLeader = Random.nextInt(0, 2)
            val isInTeam = Random.nextInt(0, 2)
            val createdAt = if (Random.nextBoolean()) "2023-07-22 12:34:56" else null
            val updatedAt = if (Random.nextBoolean()) "2023-07-22 12:34:56" else null

            val user = User(
                id, name, email, emailVerifiedAt, track, bio, imageUrl, githubUrl,
                facebookUrl, linkedinUrl, isLeader, isInTeam, createdAt, updatedAt
            )

            userList.add(user)
        }

        return userList
    }

    private fun generateRandomString(length: Int): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }




}