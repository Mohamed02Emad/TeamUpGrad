package com.team.cat_hackathon.presentation.fragmentOnBoarding

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mo_chatting.chatapp.data.dataStore.DataStoreImpl
import com.team.cat_hackathon.R
import com.team.cat_hackathon.data.models.OnBoarding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var dataStoreImpl : DataStoreImpl
     val onBoardings : ArrayList<OnBoarding> = ArrayList()

    init {
        onBoardings.add(OnBoarding(R.drawable.ic_forming_team, "Connect with your fellow graduates and build a strong team" ))
        onBoardings.add(OnBoarding(R.drawable.ic_scrum_board, "Create or join teams for group projects for graduation project" ))
        onBoardings.add(OnBoarding(R.drawable.ic_conversation_pana, "Chat with your team members to discuss ideas"))
    }

    suspend fun setIsOnBoardingFinished(isOnBoardingFinished: Boolean) = withContext(Dispatchers.IO){
        dataStoreImpl.setIsOnBoardingFinished(isOnBoardingFinished)
    }
    fun isFirstPage(currentPage: Int): Boolean = currentPage == 0
    fun isLastPage(currentPage: Int): Boolean = currentPage == 2

}