package com.team.cat_hackathon.presentation.fragmentSignUp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mo_chatting.chatapp.appClasses.isInternetAvailable
import com.team.cat_hackathon.data.api.RequestState
import com.team.cat_hackathon.databinding.FragmentSignUpBinding
import com.team.cat_hackathon.utils.showSnackbar
import com.team.cat_hackathon.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var context: Context
    private val viewModel : SignUpViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context=requireContext()
        setOnClicks()
        setObservers()
    }

    private fun setObservers() {
        if(isInternetAvailable(context)){
            viewModel.registerRequestState.observe(viewLifecycleOwner){state ->
                state?.let {
                    when (state) {
                        is RequestState.Error -> {
                            showSnackbar(state.message ?: "error",requireContext(),binding.root)
                        }
                        is RequestState.Loading -> {
                            //todo : show snack bar
                        }
                        is RequestState.Sucess -> {
                            state.data.let {response ->
                                lifecycleScope.launch {
                                    viewModel.cacheUserData(response!!.user, response.access_token)
                                    viewModel.setIsLoggedIn(true)
                                    navigateToHome()
                                }
                            }
                        }
                    }
                }
            }
        }else{
            showToast("no network connection",context)
        }
    }

    private fun setOnClicks() {
        binding.apply {
            textViewLogin.setOnClickListener {
                navigateToLogin()
            }

            editTextUsernameSignup.doAfterTextChanged {text->
                val username = if (!text.isNullOrEmpty()) text.toString() else ""
                viewModel.setName(username)
            }

            editTextEmailSignup.doAfterTextChanged {text->
                val email = if (!text.isNullOrEmpty()) text.toString() else ""
                viewModel.setEmail(email)
            }

            editTextPasswordSignup.doAfterTextChanged { text->

                val password = if (!text.isNullOrEmpty()) text.toString() else ""
                viewModel.setPassword(password)
            }

            if(isInternetAvailable(context)){
                signupButton.setOnClickListener {
                    lifecycleScope.launch {
                        viewModel.registerUser(
                            viewModel.name.value!!,
                            viewModel.email.value!!,
                            viewModel.password.value!!
                        )
                    }
                }
            }else{
                showToast("no network connection",context)
            }

        }
    }

    private fun navigateToLogin() {
        findNavController().navigateUp()
    }

    private fun navigateToHome(){
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragment2ToHomeFragment())
    }

}