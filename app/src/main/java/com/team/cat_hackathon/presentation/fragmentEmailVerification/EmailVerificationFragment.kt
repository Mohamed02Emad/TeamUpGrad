package com.team.cat_hackathon.presentation.fragmentEmailVerification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.team.cat_hackathon.R
import com.team.cat_hackathon.data.api.RequestState
import com.team.cat_hackathon.databinding.FragmentEmailVerificationBinding
import com.team.cat_hackathon.presentation.fragmentSignUp.SignUpViewModel
import com.team.cat_hackathon.utils.showToast
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
        setViews()
        setOnClickListeners()
        setObservers()
    }

    private fun setViews() {
        val user = navArgs.user
        binding.tvSendToUser.text = "Code has been send to \n${user.email}"
        binding.pinview.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_text))
    }

    private fun setObservers() {
        viewModel.registerRequestState.observe(viewLifecycleOwner) { state ->
            state?.let {
                when (state) {
                    is RequestState.Error -> {
                        showToast(state.message ?: "error", requireContext())
                        binding.progressBar.isVisible = false
                        binding.btnVerify.revertAnimation()
                    }
                    is RequestState.Loading -> {}
                    is RequestState.Sucess -> {
                        binding.progressBar.isVisible = false
                        binding.btnVerify.revertAnimation()
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
            btnVerify.apply {
                setOnClickListener {
                    startAnimation {
                        binding.progressBar.isVisible = true
                        val code = pinview.value
                        val user = navArgs.user
                        lifecycleScope.launch {
                            viewModel.verifyUser(code, user)
                        }
                    }
                }
            }
        }
    }

    private fun goToHomeScreen() {
        findNavController().navigate(EmailVerificationFragmentDirections.actionEmailVerificationFragmentToHomeFragment())
    }

}