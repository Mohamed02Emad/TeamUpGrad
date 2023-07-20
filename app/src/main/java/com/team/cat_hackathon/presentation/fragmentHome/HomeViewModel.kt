package com.team.cat_hackathon.presentation.fragmentHome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.data.api.RequestState
import com.team.cat_hackathon.data.models.MyResponse
import com.team.cat_hackathon.data.repositories.BaseRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repository: BaseRepositoryImpl) : ViewModel() {

    private val _requestState: MutableLiveData<RequestState<MyResponse>> = MutableLiveData()
    val requestState: LiveData<RequestState<MyResponse>> = _requestState

    var usersResponse: MyResponse? = null

    fun getUser(user: String? , password : String?) = viewModelScope.launch {
        _requestState.postValue(RequestState.Loading())
        val response = repository.getUser(user, password)
        _requestState.postValue(handleDataFromMyResponse(response))
    }

    private fun handleDataFromMyResponse(response: Response<MyResponse>): RequestState<MyResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                if (usersResponse == null) {
                    usersResponse = result
                }
                return RequestState.Sucess(usersResponse ?: result)
            }
        }
        return RequestState.Error(response.message())
    }
}