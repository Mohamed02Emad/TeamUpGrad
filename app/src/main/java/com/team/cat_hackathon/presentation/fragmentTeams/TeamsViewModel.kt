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

    private val _deleteState: MutableLiveData<RequestState<MessageResponse>> =
        MutableLiveData()
    val deleteState: LiveData<RequestState<MessageResponse>> = _deleteState

    var isSelectMode: MutableLiveData<Boolean> = MutableLiveData(false)

    var users: MutableLiveData<ArrayList<User>> = MutableLiveData(ArrayList())

    var selectedList: MutableLiveData<ArrayList<Pair<Int, Int?>>> =
        MutableLiveData(ArrayList())

    fun triggerSelectMode() {
        isSelectMode.value = !isSelectMode.value!!
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


    private fun handleResponse(response: Response<MessageResponse>?): RequestState<MessageResponse>? {
        if (response?.isSuccessful == true) {
            response.body()?.let { result ->
                return RequestState.Sucess(result)
            }
        }
        return RequestState.Error(response?.message() ?: "error")
    }

    suspend fun getCurrentUser(): User = repository.getCachedUser()

    fun addOrRemoveFromSelectedList(user: User, position: Int) {
        viewModelScope.launch {
            if (user.id == getCachedUser().id) return@launch
            val userExist = selectedList.value!!.contains(user.id to position)
            val index = users.value!!.indexOf(user)
            val list = selectedList.value!!
            if (userExist) {
                list.remove(user.id to index)
                user.isCheck = false
                users.value!![index] = user
                selectedList.value = list
            } else {
                user.isCheck = true
                users.value!![index] = user
                list.add(user.id to index)
                selectedList.value = list
            }
        }
    }

    private suspend fun deleteMember(userId: Int, teamId: Int) {
        _deleteState.postValue(RequestState.Loading())
        val response = repository.deleteMember(userId, teamId)
        _deleteState.postValue(handleResponse(response))
    }

    suspend fun deleteSelection() {
            val teamId = getCachedUser().team_id
            selectedList.value!!.sortedByDescending { it.second!! }
            for (i in selectedList.value!!) {
                deleteMember(i.first, teamId!!)
                users.value!!.removeAt(i.second!!)
            }
            selectedList.value!!.clear()
    }

    fun resetDeletState() {
        _deleteState.value = null
    }

}