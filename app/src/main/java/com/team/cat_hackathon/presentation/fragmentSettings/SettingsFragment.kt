package com.team.cat_hackathon.presentation.fragmentSettings

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mo_chatting.chatapp.appClasses.isInternetAvailable
import com.team.cat_hackathon.data.api.RequestState
import com.team.cat_hackathon.databinding.FragmentSettingsBinding
import com.team.cat_hackathon.presentation.MainActivity
import com.team.cat_hackathon.utils.showToast
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
                        binding.groupLoadig.isGone=true
                        (activity as MainActivity).showBottomNavigation()
                        showToast(state.message ?: "error" , requireContext())
                    }
                    is RequestState.Loading -> {
                        binding.groupLoadig.isGone=false
                        (activity as MainActivity).hideBottomNavigation()
                    }
                    is RequestState.Sucess -> {
                        binding.groupLoadig.isGone=true
                        //     if (state.data!!.code == 1){
                            lifecycleScope.launch{
                                viewModel.cleanDataStore()
                                navigateToLoginScreen()
                            }
                        }
                   // }
                }
            }
        }
    }

    private fun setOnClicks() {
        binding.apply {
            cardEditProfile.setOnClickListener {
                navigateToEditProfile()
            }
            val context = requireContext()

            cardLogOut.setOnClickListener {
                if (isInternetAvailable(context)){
                    lifecycleScope.launch {
                        viewModel.logOut()
                    }
                }else{
                    showToast("no network connection",context)
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