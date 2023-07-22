package com.team.cat_hackathon.presentation.fragmentHome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.data.api.RequestState
import com.team.cat_hackathon.data.models.Team
import com.team.cat_hackathon.data.models.TeamsResponse
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.data.models.UsersResponse
import com.team.cat_hackathon.data.repositories.HomeRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repository: HomeRepositoryImpl) : ViewModel() {

    private val _teamRequestState: MutableLiveData<RequestState<TeamsResponse>> = MutableLiveData()
    val teamRequestState: LiveData<RequestState<TeamsResponse>> = _teamRequestState

    var teamResponse: TeamsResponse? = null

    private val _individualRequestState: MutableLiveData<RequestState<UsersResponse>> = MutableLiveData()
    val individualRequestState: LiveData<RequestState<UsersResponse>> = _individualRequestState

    var individualResponse: UsersResponse? = null

    fun getTeamsByQuery(searchQuery : String?) = viewModelScope.launch {
        _teamRequestState.postValue(RequestState.Loading())
        val response = repository.getTeams(searchQuery)
        _teamRequestState.postValue(handleDataFromTeamsRequest(response))
    }

    private fun handleDataFromTeamsRequest(response: Response<TeamsResponse>): RequestState<TeamsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                    teamResponse = result
                return RequestState.Sucess(teamResponse)
            }
        }
        return RequestState.Error(response.message())
    }
    private fun handleDataFromIndividualsRequest(response: Response<UsersResponse>): RequestState<UsersResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                    individualResponse = result
                return RequestState.Sucess(individualResponse)
            }
        }
        return RequestState.Error(response.message())
    }

   fun getFakeUsers(number: Int):ArrayList<User>{
       return repository.getFakeUsers(number)
   }

    fun getFakeTeams(number: Int):ArrayList<Team>{
        return repository.getFakeTeams(number)
    }
}