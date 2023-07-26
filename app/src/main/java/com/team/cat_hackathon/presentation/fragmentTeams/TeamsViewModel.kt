package com.team.cat_hackathon.presentation.fragmentTeams

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.cat_hackathon.data.api.RequestState
import com.team.cat_hackathon.data.models.MessageResponse
import com.team.cat_hackathon.data.models.Team
import com.team.cat_hackathon.data.models.TeamWithUsersResponse
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.data.repositories.TeamsRepository
import com.team.cat_hackathon.utils.mapFullTeamToTeam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel @Inject constructor(val repository: TeamsRepository) : ViewModel() {


    private val _joinRequestState: MutableLiveData<RequestState<MessageResponse>> =
        MutableLiveData()
    val joinRequestState: LiveData<RequestState<MessageResponse>> = _joinRequestState
    private suspend fun getTeamById(teamId: Int) = withContext(Dispatchers.IO) {
        repository.getTeamById(teamId)
    }

    suspend fun updateCachedUser(user: User) {
        repository.updateCacheUser(user)
    }

    private suspend fun getCachedUser() = withContext(Dispatchers.IO) {
        repository.getCachedUser()
    }

    suspend fun getCurrentUserTeam(): Team? {
        val teamId = getCachedUser().id
        val response = getTeamById(teamId)
        return getTeamFromResponse(response)
    }

    private fun getTeamFromResponse(response: Response<TeamWithUsersResponse>?): Team? {
        if (response?.isSuccessful == true) {
            response.body()?.let { result ->
                return mapFullTeamToTeam(result.team)
            }
        }
        return null
    }

    suspend fun getUsers(teamId: Int): List<User> = repository.getTeamUsers(teamId)
    suspend fun sendJoinRequest(teamId: Int) = viewModelScope.launch {
        _joinRequestState.postValue(RequestState.Loading())
        val response = repository.sendJoinRequest(teamId)
        _joinRequestState.postValue(handleResponse(response))
    }


    private fun handleResponse(response: Response<MessageResponse>?): RequestState<MessageResponse>? {
        if (response?.isSuccessful == true) {
            response.body()?.let { result ->
                return RequestState.Sucess(result)
            }
        }
        return RequestState.Error(response?.message() ?: "error")
    }

    suspend fun getCurrentUser(): User = repository.getCachedUser()



}