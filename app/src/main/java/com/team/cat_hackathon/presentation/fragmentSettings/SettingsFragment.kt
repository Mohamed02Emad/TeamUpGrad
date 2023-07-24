package com.team.cat_hackathon.presentation.fragmentSettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.team.cat_hackathon.data.api.RequestState
import com.team.cat_hackathon.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClicks()
        setObservers()
    }

    private fun setObservers() {
        viewModel.logoutRequestState.observe(viewLifecycleOwner){state ->
            state?.let {
                when (state){
                    is RequestState.Error -> {
                    }
                    is RequestState.Loading -> {
                    }
                    is RequestState.Sucess -> {
                        if (state.data!!.code == 1){
                            lifecycleScope.launch{
                                viewModel.cleanDataStore()
                                navigateToLoginScreen()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setOnClicks() {
        binding.apply {
            cardEditProfile.setOnClickListener {
                navigateToEditProfile()
            }
            cardLogOut.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.logOut()
                }
            }
        }
    }

    fun navigateToEditProfile(){
        findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToEditProfileFragment())
    }
    fun navigateToLoginScreen(){
        findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToLoginFragment())
    }

}