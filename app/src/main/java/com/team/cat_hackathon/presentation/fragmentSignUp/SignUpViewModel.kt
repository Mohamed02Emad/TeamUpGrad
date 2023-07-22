package com.team.cat_hackathon.presentation.fragmentSignUp

import androidx.lifecycle.ViewModel
import com.team.cat_hackathon.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(val repository: AuthRepository) : ViewModel() {

}