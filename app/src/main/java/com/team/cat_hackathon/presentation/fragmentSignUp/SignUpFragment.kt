package com.team.cat_hackathon.presentation.fragmentSignUp

import android.os.Bundle
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
import com.team.cat_hackathon.databinding.FragmentSignUpBinding
import com.team.cat_hackathon.utils.BUTTON_MIN_ANIMATION_DURATION
import com.team.cat_hackathon.utils.showSnackbar
import com.team.cat_hackathon.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
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
        setOnClicks()
        setObservers()
    }

    private fun setObservers() {
        viewModel.registerRequestState.observe(viewLifecycleOwner){state ->
            stopButtonAnimation()
            state?.let {
                when (state) {
                    is RequestState.Error -> {
                        showSnackbar(state.message ?: "error",requireContext(),binding.root)
                    }
                    is RequestState.Loading -> {  }
                    is RequestState.Sucess -> {
                        state.data.let {response ->
                            lifecycleScope.launch {
                                viewModel.cacheUserData(response!!.user, response.access_token)
                                viewModel.setIsLoggedIn(true)
                                binding.signupButton.visibility=View.GONE
                                navigateToHome()
                            }
                        }
                    }
                }
            }
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

            editTextPasswordSignup.doAfterTextChanged { text ->

                val password = if (!text.isNullOrEmpty()) text.toString() else ""
                viewModel.setPassword(password)
            }

            signupButton.apply {
                setOnClickListener {
                    startAnimation {
                        binding.progressBar.visibility = View.VISIBLE
                        trySignUp()
                    }
                }
            }

        }
    }

    private fun trySignUp() {
        val context = requireContext()
        if (isInternetAvailable(context)) {
            lifecycleScope.launch {
                viewModel.registerUser(
                    viewModel.name.value!!,
                    viewModel.email.value!!,
                    viewModel.password.value!!
                )
            }
        } else {
            stopButtonAnimation()
            showToast("no network connection", context)
        }
    }

    private fun stopButtonAnimation() {
        lifecycleScope.launch {
            delay(BUTTON_MIN_ANIMATION_DURATION)
            binding.signupButton.revertAnimation()
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun navigateToLogin() {
        findNavController().navigateUp()
    }

    private fun navigateToHome(){
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragment2ToHomeFragment())
    }

}