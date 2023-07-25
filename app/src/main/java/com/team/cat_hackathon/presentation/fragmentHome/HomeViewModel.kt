package com.team.cat_hackathon.presentation.fragmentHome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.cat_hackathon.data.api.RequestState
import com.team.cat_hackathon.data.models.Team
import com.team.cat_hackathon.data.models.AllDataResponse
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.data.repositories.HomeRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repository: HomeRepositoryImpl) : ViewModel() {

    private val _homeDataRequestState: MutableLiveData<RequestState<AllDataResponse>> = MutableLiveData()
    val homeDataRequestState: LiveData<RequestState<AllDataResponse>> = _homeDataRequestState

    var homeDataResponse: AllDataResponse? = AllDataResponse(emptyList<User>(), emptyList<Team>())

   suspend fun requestHomeData() = withContext(Dispatchers.IO){
        _homeDataRequestState.postValue(RequestState.Loading())
        val response = repository.getHomeData()
       _homeDataRequestState.postValue(handleDataFromHomeRequest(response))
    }
    
    private fun handleDataFromHomeRequest(response: Response<AllDataResponse>?): RequestState<AllDataResponse> {
        if (response?.isSuccessful == true) {
            response.body()?.let { result ->
                    homeDataResponse = result
                return RequestState.Sucess(homeDataResponse)
            }
        }
        return RequestState.Error(response?.message() ?: "error")
    }

    //todo: delete later
   fun getFakeUsers(number: Int):ArrayList<User>{
       return repository.getFakeUsers(number)
   }

    fun getFakeTeams(number: Int):ArrayList<Team>{
        return repository.getFakeTeams(number)
    }
}