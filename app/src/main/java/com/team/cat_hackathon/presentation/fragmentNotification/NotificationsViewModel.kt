package com.team.cat_hackathon.presentation.fragmentNotification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.cat_hackathon.data.api.RequestState
import com.team.cat_hackathon.data.models.JoinRequestsResponse
import com.team.cat_hackathon.data.models.MessageResponse
import com.team.cat_hackathon.data.repositories.TeamsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(val repository: TeamsRepository) : ViewModel() {

    private val _responseState: MutableLiveData<RequestState<JoinRequestsResponse>?> =
        MutableLiveData(null)
    val responseState: LiveData<RequestState<JoinRequestsResponse>?> = _responseState

    private val _acceptState: MutableLiveData<RequestState<MessageResponse>?> =
        MutableLiveData(null)
    val acceptState: LiveData<RequestState<MessageResponse>?> = _acceptState

    private val _rejectState: MutableLiveData<RequestState<MessageResponse>?> =
        MutableLiveData(null)
    val rejectState: LiveData<RequestState<MessageResponse>?> = _rejectState

    suspend fun getRequestList() {
        _responseState.postValue(RequestState.Loading())
        val response = repository.getRequesteToJoinList()
        _responseState.postValue(handleListResponse(response))
    }

    private fun handleListResponse(response: Response<JoinRequestsResponse>?): RequestState<JoinRequestsResponse>? {
        if (response?.isSuccessful == true) {
            response.body()?.let { result ->
                return RequestState.Sucess(result)
            }
        }
        return RequestState.Error(response?.message() ?: "error")
    }

    private fun handleActionResponse(response: Response<MessageResponse>?): RequestState<MessageResponse>? {
        if (response?.isSuccessful == true) {
            response.body()?.let { result ->
                return RequestState.Sucess(result)
            }
        }
        return RequestState.Error(response?.message() ?: "error")
    }

    fun setAcceptState(nothing: Nothing?) {
       _acceptState.postValue(null)
    }

    fun setRejectState(nothing: Nothing?) {
       _rejectState.postValue(null)
    }


    val acceptUser: (Int) -> Unit = { userId ->
        viewModelScope.launch {
            _acceptState.postValue(RequestState.Loading())
            val response = repository.acceptUser(userId)
            _acceptState.postValue(handleActionResponse(response))
        }
    }
    val rejectUser: (Int) -> Unit = { userId ->
        viewModelScope.launch {
            _rejectState.postValue(RequestState.Loading())
            val response = repository.rejectUser(userId)
            _rejectState.postValue(handleActionResponse(response))

        }
    }


}