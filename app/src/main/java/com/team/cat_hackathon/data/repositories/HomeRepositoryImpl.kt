package com.team.cat_hackathon.data.repositories

import RetrofitInstance
import android.content.Context
import android.util.Log
import com.mo_chatting.chatapp.data.dataStore.DataStoreImpl
import com.team.cat_hackathon.data.models.AllDataResponse
import com.team.cat_hackathon.data.models.UpdateUserResponse
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.data.source.MyDao
import com.team.cat_hackathon.utils.byteArrayToFile
import retrofit2.Response

class HomeRepositoryImpl (val dao : MyDao, val context : Context , val dataStoreImpl: DataStoreImpl) {


    suspend fun getCachedUser(): User {
        return dataStoreImpl.getUser()
    }

    suspend fun updateUser(user: User, uploadedImage: ByteArray?) {
        val token = "Bearer ${dataStoreImpl.getToken().trimEnd().trimStart()}"


        val imagePart = try {
//            val requestBody = uploadedImage!!.toRequestBody("image/*".toMediaTypeOrNull())
//            MultipartBody.Part.createFormData("image", "image.png", requestBody)
                byteArrayToFile(context, uploadedImage!!, "user${user.id}_profile_image")
        } catch (e: Exception) {
            Log.d("mohamed", "error \n${e.message} ")
            null
        }

        try {
            val response = RetrofitInstance.api.updateUser(
                token = token,
                imageUrl = imagePart,
                name = user.name,
                track = user.track,
                bio = user.bio,
                linkedinUrl = user.linkedinUrl,
                facebookUrl = user.facebookUrl,
                githubUrl = user.githubUrl
            )

            if (response.isSuccessful) {
                val messageResponse: UpdateUserResponse? = response.body()
                messageResponse?.let {
                    Log.d("mohamed", "uploadImage: success \n ${response.body()!!.message}")
                }
            } else {
                Log.d("mohamed", "uploadImage: success \n ${response.body()!!.message}")
            }
        } catch (e: Exception) {
            Log.d("mohamed", "uploadImage: success \n ${e.message}")

        }
    }
    suspend fun getHomeData(): Response<AllDataResponse>? {
        val token = "Bearer ${dataStoreImpl.getToken().trimEnd().trimStart()}"
      return try {
          RetrofitInstance.api.getAllData(token)
      }catch (e: Exception) {
          null
      }
    }


}