package com.team.cat_hackathon.presentation.fragmentSettings

import androidx.lifecycle.ViewModel
import com.team.cat_hackathon.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(val repository: AuthRepository): ViewModel() {

    // TODO:  remember this when you add data store
//    @Inject
//    lateinit var dataStore :

    suspend fun logOut(){
        val response = repository.logOutUser()
    }

}