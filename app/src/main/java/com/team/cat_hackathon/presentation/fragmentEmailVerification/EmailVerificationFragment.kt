package com.team.cat_hackathon.presentation.fragmentEmailVerification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.team.cat_hackathon.data.api.RequestState
import com.team.cat_hackathon.databinding.FragmentEmailVerificationBinding
import com.team.cat_hackathon.presentation.fragmentSignUp.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EmailVerificationFragment : Fragment() {

    private lateinit var binding: FragmentEmailVerificationBinding
    private val viewModel: SignUpViewModel by viewModels()
    private val navArgs by navArgs<EmailVerificationFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmailVerificationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        setObservers()
    }

    private fun setObservers() {
        viewModel.registerRequestState.observe(viewLifecycleOwner) { state ->
            state?.let {
                when (state) {
                    is RequestState.Error -> {}
                    is RequestState.Loading -> {
                        //todo : show loading bar
                    }

                    is RequestState.Sucess -> {
                        lifecycleScope.launch {
                            viewModel.cacheUserData(
                                state.data!!.user!!,
                                state.data!!.access_token!!
                            )
                            viewModel.setIsLoggedIn(true)
                            goToHomeScreen()
                        }
                    }
                }
            }
        }
    }

    private fun setOnClickListeners() {
        binding.apply {
            btnVerify.setOnClickListener {
                val code = etVerification.text.toString()
                val user = navArgs.user
                lifecycleScope.launch {
                    viewModel.verifyUser(code, user)
                }
            }
        }
    }

    private fun goToHomeScreen() {
        findNavController().navigate(EmailVerificationFragmentDirections.actionEmailVerificationFragmentToHomeFragment())
    }

}