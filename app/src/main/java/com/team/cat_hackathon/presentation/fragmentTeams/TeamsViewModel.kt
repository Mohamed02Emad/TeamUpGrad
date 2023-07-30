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

    private val _deleteState: MutableLiveData<RequestState<MessageResponse>?> =
        MutableLiveData()

    val deleteState: LiveData<RequestState<MessageResponse>?> = _deleteState

    private val _leaveTeamState: MutableLiveData<RequestState<MessageResponse>?> =
        MutableLiveData()

    val leaveTeamState: LiveData<RequestState<MessageResponse>?> = _leaveTeamState

    private val _deleteTeamState: MutableLiveData<RequestState<MessageResponse>?> =
        MutableLiveData()

    val deleteTeamState: LiveData<RequestState<MessageResponse>?> = _deleteTeamState


    var isEditMode: MutableLiveData<Boolean> = MutableLiveData(false)

    var users: MutableLiveData<ArrayList<User>> = MutableLiveData(ArrayList())

    var deletedUserPosition : Int? = null


    fun triggerEditMode() {
        isEditMode.value = !isEditMode.value!!
    }


    private suspend fun getTeamById(teamId: Int): Response<TeamWithUsersResponse>? {
        return repository.getTeamById(teamId)
    }

    suspend fun updateCachedUser(user: User) {
        repository.updateCacheUser(user)
    }

    private suspend fun getCachedUser() = withContext(Dispatchers.IO) {
        repository.getCachedUser()
    }

    suspend fun getCurrentUserTeam(teamId: Int): Team? {
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

    suspend fun getUsers(teamId: Int): ArrayList<User> {
        return if (users.value!!.isEmpty()) {
            users.value!!.addAll(repository.getTeamUsers(teamId))
            users.value!!
        } else {
            val list = repository.getTeamUsers(teamId)
            if (list.size != users.value!!.size) {
                users.value!!.clear()
                users.value!!.addAll(list)
            }
            users.value!!
        }
    }

    suspend fun sendJoinRequest(teamId: Int) = viewModelScope.launch {
        _joinRequestState.postValue(RequestState.Loading())
        val response = repository.sendJoinRequest(teamId)
        _joinRequestState.postValue(handleResponse(response))
    }


    private fun handleResponse(response: Response<MessageResponse>?, user: User? = null): RequestState<MessageResponse>? {
        if (response?.isSuccessful == true) {
            response.body()?.let { result ->
                user?.let{
                    users.value!!.remove(user)
                }
                return RequestState.Sucess(result)
            }
        }
        return RequestState.Error(response?.message() ?: "error")
    }

    suspend fun getCurrentUser(): User = repository.getCachedUser()

    private suspend fun deleteMember(user: User, teamId: Int) {
        _deleteState.postValue(RequestState.Loading())
        val response = repository.deleteMember(user.id, teamId)
        _deleteState.postValue(handleResponse(response , user))
    }

    suspend fun deleteUser(user: User, position: Int) {
        val teamId = getCachedUser().team_id
        deletedUserPosition = position
        deleteMember(user, teamId!!)
    }

    suspend fun deleteTeam(){
        val teamId = getCachedUser().team_id
        if (teamId != null) {
            _deleteTeamState.postValue(RequestState.Loading())
            val response = repository.deleteTeam(teamId)
            _deleteTeamState.postValue(handleResponse(response))
        }
    }

    suspend fun updateCachedUserWithoutTeam() {
        repository.leaveCurrentTeamInCache()
    }

    suspend fun leaveTeam() {
        val teamId = getCachedUser().team_id
        _leaveTeamState.postValue(RequestState.Loading())
        val response = repository.leaveTeam(teamId!!)
        _leaveTeamState.postValue(handleResponse(response))
    }

}
