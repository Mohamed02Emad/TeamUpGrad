package com.team.cat_hackathon.presentation.fragmentNotification

import androidx.lifecycle.ViewModel
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.data.repositories.HomeRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(val repositoryImpl: HomeRepositoryImpl)  :ViewModel(){


    fun getFakeUsers(number:Int):List<User>{
        return repositoryImpl.getFakeUsers(number)
    }
}