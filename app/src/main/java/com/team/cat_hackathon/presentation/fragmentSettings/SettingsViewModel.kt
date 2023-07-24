package com.team.cat_hackathon.presentation.fragmentSettings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.cat_hackathon.data.api.RequestState
import com.team.cat_hackathon.data.models.AuthResponse
import com.team.cat_hackathon.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(val repository: AuthRepository): ViewModel() {

    private val _logoutRequestState: MutableLiveData<RequestState<AuthResponse>> = MutableLiveData(null)
    val logoutRequestState: LiveData<RequestState<AuthResponse>> = _logoutRequestState

    suspend fun logOut(){
        _logoutRequestState.postValue(RequestState.Loading())
        val response = repository.logOutUser()
        _logoutRequestState.postValue(handleLogoutRequest(response))
    }

    private fun handleLogoutRequest(response: Response<AuthResponse>): RequestState<AuthResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return RequestState.Sucess(result)
            }
        }

        return RequestState.Error(response.message())
    }

    suspend fun cleanDataStore() {
        repository.clearDataStore()
    }
}