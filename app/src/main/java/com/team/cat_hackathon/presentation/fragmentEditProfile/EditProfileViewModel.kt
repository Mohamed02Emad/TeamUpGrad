package com.team.cat_hackathon.presentation.fragmentEditProfile

import android.util.Log
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


    suspend fun updateUser(user: User) {
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

    fun isInputEqualToCachedUser(img:String,name: String, track: String,github:String,linkedin:String,facebook:String): Boolean {
        val cachedUser = cachedUserLiveData.value!!
        return cachedUser.name.trimEnd().trimStart() == name.trimEnd().trimStart() &&
                cachedUser.track?.trim() == track.trim() &&
                cachedUser.imageUrl?.trim() == img.trim() &&
                cachedUser.githubUrl?.trim() == github.trim() &&
                cachedUser.linkedinUrl?.trim() == linkedin.trim() &&
                cachedUser.facebookUrl?.trim() == facebook.trim()
    }

    fun getImg():String{
        return ""
    }
}