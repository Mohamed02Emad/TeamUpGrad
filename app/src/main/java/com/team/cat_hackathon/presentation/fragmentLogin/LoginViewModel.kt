package com.team.cat_hackathon.presentation.fragmentLogin

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androiddevs.mvvmnewsapp.data.api.RequestState
import com.team.cat_hackathon.data.models.LoginResponse
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.data.repositories.AuthRepository
import com.team.cat_hackathon.utils.CAN_LOGIN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val repository: AuthRepository) : ViewModel() {

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

    private val _loginRequestState: MutableLiveData<RequestState<LoginResponse>?> = MutableLiveData(null)
    val loginRequestState: LiveData<RequestState<LoginResponse>?> = _loginRequestState
    suspend fun loginUser(username: String?, password: String?) {
        val deviceName = getDeviceName()
        _loginRequestState.postValue(RequestState.Loading())

        val response = repository.loginUser(username, password, deviceName)

        _loginRequestState.postValue(handleUserResponse(response))
    }

    private fun handleUserResponse(response: Response<LoginResponse>): RequestState<LoginResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return RequestState.Sucess(result)
            }
        }
        return RequestState.Error(response.message())
    }

    private fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            model.capitalize()
        } else {
            "$manufacturer ${model.capitalize()}"
        }
    }

    fun validateData(email: String?, password: String?): String {
        if (email!!.isNullOrEmpty()) return "please enter your email"
        if (password!!.isNullOrEmpty()) return "please enter your password"
        if (password!!.length < 6) return "Short password"
        return CAN_LOGIN
    }

    suspend fun cacheUserData(user: User, accessToken: String) {
      repository.cacheUSer(user, accessToken)
    }

    suspend fun setIsLoggedIn(isLoggedIn: Boolean){
        repository.setUserIsLogged(true)
    }

    suspend fun getIsLogged(): String {
         return repository.getIsLogged().toString()
    }


}