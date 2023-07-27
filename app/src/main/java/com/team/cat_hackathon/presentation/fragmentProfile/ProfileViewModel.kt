package com.team.cat_hackathon.presentation.fragmentProfile

import androidx.lifecycle.ViewModel
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.data.repositories.HomeRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(val repository: HomeRepositoryImpl) : ViewModel() {


    suspend fun getCachedUser(): User = repository.getCachedUser()

}