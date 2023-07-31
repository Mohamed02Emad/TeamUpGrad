package com.team.cat_hackathon.presentation.fragmentLogin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mo_chatting.chatapp.appClasses.isInternetAvailable
import com.team.cat_hackathon.data.api.RequestState
import com.team.cat_hackathon.databinding.FragmentLoginBinding
import com.team.cat_hackathon.utils.BUTTON_MIN_ANIMATION_DURATION
import com.team.cat_hackathon.utils.CAN_LOGIN
import com.team.cat_hackathon.utils.showSnackbar
import com.team.cat_hackathon.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {


    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClicks()
        setObservers()
    }

    private fun setObservers() {
        viewModel.loginRequestState.observe(viewLifecycleOwner){state->
            state?.let {
               stopButtonAnimation()
                when (state){
                    is RequestState.Error -> {
                        showSnackbar( state.message?:"error" , requireContext() , binding.root)
                    }
                    is RequestState.Loading -> {}
                    is RequestState.Sucess -> {
                        lifecycleScope.launch {
                            state.data?.let { response ->
                                viewModel.cacheUserData(response.user!!, response.access_token!!)
                                viewModel.setIsLoggedIn(true)
                                navigateToHomeScreen()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun stopButtonAnimation() {
        lifecycleScope.launch {
            delay(BUTTON_MIN_ANIMATION_DURATION)
            binding.buttonLogin.revertAnimation()
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun setOnClicks() {
        binding.apply {

            editTextEmailLogin.doAfterTextChanged { text ->
                val email = text ?: ""
                viewModel.setEmail(email.toString())
            }

            editTextPasswordLogin.doAfterTextChanged { text ->
                val password = text ?: ""
                viewModel.setPassword(password.toString())
            }

            buttonLogin.apply {
                setOnClickListener {
                    startAnimation {
                        binding.progressBar.visibility = View.VISIBLE
                        lifecycleScope.launch {
                            tryLogin()
                        }
                    }
                }
            }

            textViewSignup.setOnClickListener {
                navigateToSignUp()
            }

        }
    }

    private fun tryLogin() {
        val context = requireContext()
        if(isInternetAvailable(context)){
            val canSendRequestState = viewModel.validateData(
                viewModel.email.value,
                viewModel.password.value
            )
            if (canSendRequestState == CAN_LOGIN) {
                lifecycleScope.launch {
                    viewModel.loginUser(viewModel.email.value, viewModel.password.value)
                }
            } else {
                showSnackbar(canSendRequestState, requireContext(), binding.root)
                stopButtonAnimation()
            }
        }else{
            showToast("no network connection",context)
            stopButtonAnimation()
        }
    }

    private fun navigateToSignUp() {
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment2())
    }

    private fun navigateToHomeScreen() {
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
    }

}