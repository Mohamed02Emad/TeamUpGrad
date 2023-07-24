package com.team.cat_hackathon.presentation.fragmentEditProfile

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


    suspend fun updateUser(user: User){
        repository.updateUser(user)
    }
    suspend fun getCachedUser(){
        val cachedUser = repository.getCachedUser()
        _cachedUserLiveData.postValue(cachedUser)
    }
    fun getUser(): User? {
        return cachedUserLiveData.value
    }
    fun setName(name: String) {
        val currentUser = _cachedUserLiveData.value ?: User()
        currentUser.name = name
        _cachedUserLiveData.postValue(currentUser)
    }

    fun setTrack(track: String) {
        val currentUser = _cachedUserLiveData.value ?: User()
        currentUser.track = track
        _cachedUserLiveData.postValue(currentUser)
    }

    fun setGithubUrl(github: String) {
        val currentUser = _cachedUserLiveData.value ?: User()
        currentUser.githubUrl = github
        _cachedUserLiveData.postValue(currentUser)
    }
    fun setLinkedinUrl(linkedin: String) {
        val currentUser = _cachedUserLiveData.value ?: User()
        currentUser.linkedinUrl = linkedin
        _cachedUserLiveData.postValue(currentUser)
    }
    fun setFacebookUrl(facebook: String) {
        val currentUser = _cachedUserLiveData.value ?: User()
        currentUser.linkedinUrl = facebook
        _cachedUserLiveData.postValue(currentUser)
    }

    fun isInputEqualToCachedUser(name: String, track: String,github:String,linkedin:String,facebook:String): Boolean {
        val cachedUser = cachedUserLiveData.value
        return cachedUser?.name == name && cachedUser.track == track
                && cachedUser.githubUrl == github && cachedUser.linkedinUrl == linkedin && cachedUser.facebookUrl == facebook
    }
}