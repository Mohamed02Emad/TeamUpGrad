package com.team.cat_hackathon.presentation.fragmentEditProfile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.data.repositories.HomeRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(val repository: HomeRepositoryImpl) : ViewModel() {


    private val _cachedUserLiveData = MutableLiveData<User>()
    val cachedUserLiveData: LiveData<User> get() = _cachedUserLiveData

    val imageChanged: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    private val _image = MutableLiveData<Uri?>(null)
    val image: LiveData<Uri?> = _image


    suspend fun updateUser(user: User) {
        val uploadedImage = uploadImage(image.value)
        uploadedImage?.let {
            user.imageUrl = it
        }
        repository.updateUser(user)
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
                cachedUser.email?.trim() == email.trim() &&
                cachedUser.bio?.trim() == bio.trim() &&
                imageChanged.value!!&&
                cachedUser.githubUrl?.trim() == github.trim() &&
                cachedUser.linkedinUrl?.trim() == linkedin.trim() &&
                cachedUser.facebookUrl?.trim() == facebook.trim()
    }

    fun getImg(): String {
        return _cachedUserLiveData.value?.imageUrl ?: ""
    }

    private fun uploadImage(image: Uri?): String? {
        return if (image == null)
            null
        else {
            //repository.uploadImage(image)
            null
        }
    }

    fun setImage(uri: Uri) {
        _image.value = uri
    }
}