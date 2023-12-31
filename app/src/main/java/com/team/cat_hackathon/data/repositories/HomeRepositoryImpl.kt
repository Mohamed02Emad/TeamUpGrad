package com.team.cat_hackathon.data.repositories

import RetrofitInstance
import android.content.Context
import android.util.Log
import com.mo_chatting.chatapp.data.dataStore.DataStoreImpl
import com.team.cat_hackathon.data.models.AllDataResponse
import com.team.cat_hackathon.data.models.MessageResponse
import com.team.cat_hackathon.data.models.UpdateUserResponse
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.data.source.MyDao
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

class HomeRepositoryImpl (val dao : MyDao, val context : Context , val dataStoreImpl: DataStoreImpl) {


    suspend fun getCachedUser(): User {
        return dataStoreImpl.getUser()
    }

    suspend fun updateUser(user: User? = null, imagePart: MultipartBody.Part? = null) :Response<UpdateUserResponse>?{
        val token = "Bearer ${dataStoreImpl.getToken().trimEnd().trimStart()}"

        try {
            val response = if (imagePart != null) {

                val mediaType = "multipart/form-data".toMediaType()

                RetrofitInstance.api.updateUser(
                    token = token,
                    imageUrl = imagePart,
                    name = user?.name?.toRequestBody(mediaType),
                    track = user?.track?.toRequestBody(mediaType),
                    bio = user?.bio?.toRequestBody(mediaType),
                    linkedinUrl = user?.linkedinUrl?.toRequestBody(mediaType),
                    facebookUrl = user?.facebookUrl?.toRequestBody(mediaType),
                    githubUrl = user?.githubUrl?.toRequestBody(mediaType)
                )
            } else if (user != null) {
                RetrofitInstance.api.updateUser(
                    token = token,
                    name = user.name,
                    track = user.track,
                    bio = user.bio,
                    linkedinUrl = user.linkedinUrl,
                    facebookUrl = user.facebookUrl,
                    githubUrl = user.githubUrl
                )
            } else {
                RetrofitInstance.api.updateUser(
                    token = token
                )
            }
          return response
        } catch (e: Exception) {
           return null
        }
    }

    suspend fun getHomeData(): Response<AllDataResponse>? {
        val token = "Bearer ${dataStoreImpl.getToken().trimEnd().trimStart()}"
        return try {
            RetrofitInstance.api.getAllData(token)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createTeam(name: String, bio: String): Response<MessageResponse> {
        val token = "Bearer ${dataStoreImpl.getToken().trimEnd().trimStart()}"
        return RetrofitInstance.api.createTeam(token, name, bio)
    }

    suspend fun updateCachedUser(newUser: User) {
      dataStoreImpl.insertUser(newUser)
    }


}