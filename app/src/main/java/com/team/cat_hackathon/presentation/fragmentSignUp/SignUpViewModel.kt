package com.team.cat_hackathon.presentation.fragmentSignUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.cat_hackathon.data.api.RequestState
import com.team.cat_hackathon.data.models.AuthResponse
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(val repository: AuthRepository) : ViewModel() {


    private val _registerRequestState: MutableLiveData<RequestState<AuthResponse>?> =
        MutableLiveData(null)
    val registerRequestState: LiveData<RequestState<AuthResponse>?> = _registerRequestState

    private val _name: MutableLiveData<String?> = MutableLiveData("")
    val name: LiveData<String?> = _name

    fun setName(name: String?) {
        _name.value = name
    }

    private val _email: MutableLiveData<String?> = MutableLiveData("")
    val email: LiveData<String?> = _email

    fun setEmail(email: String?) {
        _email.value = email
    }

    private val _password: MutableLiveData<String?> = MutableLiveData("")
    val password: LiveData<String?> = _password

    fun setPassword(password: String?) {
        _password.value = password
    }

    suspend fun registerUser(name: String, email: String, password: String) {
        _registerRequestState.postValue(RequestState.Loading())
        val response = repository.registerUser(name, email, password)
        _registerRequestState.postValue(handleUserResponse(response))
    }

    private fun handleUserResponse(response: Response<AuthResponse>?): RequestState<AuthResponse> {
        if (response?.isSuccessful == true) {
            response.body()?.let { result ->
                return RequestState.Sucess(result)
            }
        }
        if (response == null){
            return RequestState.Error( "error")
        }
        return RequestState.Error(response.message() ?: "error")
    }

    suspend fun cacheUserData(user: User, token: String) {
        repository.cacheUser(user, token)
    }

    suspend fun setIsLoggedIn(b: Boolean) {
        repository.setUserIsLogged(b)
    }
}