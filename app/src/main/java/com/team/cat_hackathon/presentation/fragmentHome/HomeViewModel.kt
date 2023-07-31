package com.team.cat_hackathon.presentation.fragmentHome

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.cat_hackathon.data.api.RequestState
import com.team.cat_hackathon.data.models.AllDataResponse
import com.team.cat_hackathon.data.models.MessageResponse
import com.team.cat_hackathon.data.models.UpdateUserResponse
import com.team.cat_hackathon.data.repositories.HomeRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repository: HomeRepositoryImpl) : ViewModel() {

    private val _homeDataRequestState: MutableLiveData<RequestState<AllDataResponse>?> = MutableLiveData(null)
    val homeDataRequestState: LiveData<RequestState<AllDataResponse>?> = _homeDataRequestState

    private val _createTeamRequestState: MutableLiveData<RequestState<MessageResponse>?> = MutableLiveData(null)
    val createTeamRequestState: LiveData<RequestState<MessageResponse>?> = _createTeamRequestState

    private val _isUserSearch: MutableLiveData<Boolean> = MutableLiveData(false)
    val isUserSearch: LiveData<Boolean> = _isUserSearch

    var userSearch = ""
    var teamSearch = ""

    private val _isBottomSheetOpened = MutableLiveData<Boolean>(false)
    val isBottomSheetOpened: LiveData<Boolean> = _isBottomSheetOpened

   suspend fun requestHomeData() = withContext(Dispatchers.IO){
        _homeDataRequestState.postValue(RequestState.Loading())
        val response = repository.getHomeData()
       _homeDataRequestState.postValue(handleDataFromHomeRequest(response))
    }

    private fun handleDataFromHomeRequest(response: Response<AllDataResponse>?): RequestState<AllDataResponse> {
        if (response?.isSuccessful == true) {
            response.body()?.let { result ->
                return RequestState.Sucess(result)
            }
        }
        return RequestState.Error(response?.message() ?: "error")
    }

    fun setSearchToUser(b: Boolean) {
         _isUserSearch.value = b
    }


     fun createTeam(name: Editable?, bio: Editable?) {
         viewModelScope.launch {
             _createTeamRequestState.postValue(RequestState.Loading())
          val response = repository.createTeam(name.toString() , bio.toString())
             _createTeamRequestState.postValue(handleCreateTeamResponse(response))
         }
    }

    private fun handleCreateTeamResponse(response: Response<MessageResponse>): RequestState<MessageResponse>? {
        if (response?.isSuccessful == true) {
            response.body()?.let { result ->
                return RequestState.Sucess(result)
            }
        }
        return RequestState.Error(response?.message() ?: "error")
    }

    suspend fun updateCachedUser() {
        val response = repository.updateUser()
        cacheUserFromResponse(response)
    }

    private suspend fun cacheUserFromResponse(response: Response<UpdateUserResponse>?) {
        if (response?.isSuccessful == true) {
            response.body()?.let { result ->
                 repository.updateCachedUser(response.body()!!.user!!)
            }
        }
    }

    suspend fun isUserInTeam(): Boolean {
      return (repository.getCachedUser().team_id ?: -1) > 0
    }

    fun resetCreateTeamState() {
        _createTeamRequestState.postValue(null)
    }

}