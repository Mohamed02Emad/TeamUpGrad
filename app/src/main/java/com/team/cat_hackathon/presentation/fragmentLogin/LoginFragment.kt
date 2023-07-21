package com.team.cat_hackathon.presentation.fragmentLogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.androiddevs.mvvmnewsapp.data.api.RequestState
import com.team.cat_hackathon.utils.CAN_LOGIN
import com.team.cat_hackathon.utils.showSnackbar
import com.team.cat_hackathon.databinding.FragmentLoginBinding
import com.team.cat_hackathon.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
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
                when (state){
                    is RequestState.Error -> {
                        showSnackbar(  "error" , requireContext() , binding.root)
                    }
                    is RequestState.Loading -> {

                    }
                    is RequestState.Sucess -> {
                        lifecycleScope.launch {
                            val user = state.data?.let { response ->
                                viewModel.cacheUserData(response.user ,response.access_token)
                                showToast(response.code.toString() , requireContext())
                                //navigateToHomeScreen()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun navigateToHomeScreen() {
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
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

            buttonLogin.setOnClickListener {

                val canSendRequestState =
                    viewModel.validateData(viewModel.email.value, viewModel.password.value)

                if (canSendRequestState == CAN_LOGIN) {

                    lifecycleScope.launch {
                        viewModel.loginUser(viewModel.email.value, viewModel.password.value)
                    }

                } else {
                    showSnackbar(canSendRequestState, requireContext(), binding.root)
                }
            }

        }
    }

}