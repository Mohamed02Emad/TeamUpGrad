package com.team.cat_hackathon.presentation.fragmentEditProfile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.cat_hackathon.data.api.RequestState
import com.team.cat_hackathon.data.models.UpdateUserResponse
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.data.repositories.HomeRepositoryImpl
import com.team.cat_hackathon.utils.cacheImageToFile
import com.team.cat_hackathon.utils.createMultipartBodyPartFromFile
import com.team.cat_hackathon.utils.getImageFileFromRealPath
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(
    val repository: HomeRepositoryImpl, val appContext: Application,
) : ViewModel() {


    private val _cachedUserLiveData = MutableLiveData<User>()
    val cachedUserLiveData: LiveData<User> get() = _cachedUserLiveData

    private val _updateUserResponse: MutableLiveData<RequestState<UpdateUserResponse>?> =
        MutableLiveData(null)
    val updateUserResponse: LiveData<RequestState<UpdateUserResponse>?> = _updateUserResponse

    val imageChanged: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    private val _image = MutableLiveData<Uri?>(null)
    val image: LiveData<Uri?> = _image


    suspend fun updateUser(user: User) {
        val uri = image.value
        var response: Response<UpdateUserResponse>?
        response = if (uri == null) {
            repository.updateUser(user)
        } else {
            val path = cacheImageToFile(appContext, uri)
            val file = getImageFileFromRealPath(path)
            val part = createMultipartBodyPartFromFile(file)

            repository.updateUser(user, part)
        }
        _updateUserResponse.postValue(handleUserResponse(response))
    }

    private fun handleUserResponse(response: Response<UpdateUserResponse>?): RequestState<UpdateUserResponse>? {
        if (response?.isSuccessful == true) {
            response.body()?.let { result ->
                return RequestState.Sucess(result)
            }
        }
        return RequestState.Error(response?.message() ?: "error")
    }


    suspend fun getCachedUser(): User {
        val cachedUser = repository.getCachedUser()
        _cachedUserLiveData.postValue(cachedUser)
        return cachedUser
    }

    fun getUser(): User? {
        return cachedUserLiveData.value
    }

    fun isInputEqualToCachedUser(
        name: String,
        track: String,
        email: String,
        bio: String,
        github: String,
        linkedin: String,
        facebook: String
    ): Boolean {
        val cachedUser = cachedUserLiveData.value!!
        return cachedUser.name.trimEnd().trimStart() == name.trimEnd().trimStart() &&
                cachedUser.track?.trim() == track.trim() &&
//                cachedUser.email?.trim() == email.trim() &&
                cachedUser.bio?.trim() == bio.trim() &&
                imageChanged.value!! &&
                cachedUser.githubUrl?.trim() == github.trim() &&
                cachedUser.linkedinUrl?.trim() == linkedin.trim() &&
                cachedUser.facebookUrl?.trim() == facebook.trim()
    }

    fun setImage(uri: Uri) {
        _image.value = uri
    }

    suspend fun updateCachedUser(newUser: User) {
        repository.updateCachedUser(newUser)
    }
}