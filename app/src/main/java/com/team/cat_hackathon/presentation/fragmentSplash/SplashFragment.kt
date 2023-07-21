package com.team.cat_hackathon.presentation.fragmentSplash

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.team.cat_hackathon.R
import com.team.cat_hackathon.databinding.FragmentSplashBinding
import com.team.cat_hackathon.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {

    var systemVersion : Int? = null
    private lateinit var binding : FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (systemVersion!! >  31) {
            null
        }else {
            binding = FragmentSplashBinding.inflate(layoutInflater)
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity). handleFullScreen()
        Handler(Looper.getMainLooper()).postDelayed({
            handleDirections()
        }, 1000)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        systemVersion = Build.VERSION.SDK_INT
        if (systemVersion!! > 31) {
            handleDirections()
        }
    }

    private fun handleDirections() {
        (activity as MainActivity).undoFullScreen()
        lifecycleScope.launch {
            val isOnBoardingFinnished = (activity as MainActivity).isOnBoardingFinished()
            val isLoggedIn = (activity as MainActivity).isUserLoggedIn()
            if (isLoggedIn){
                openHomeFragment()
            } else if(isOnBoardingFinnished) {
                openLoginFragment()
            }else{
                openOnBoardingFragment()
            }
        }
    }

    private fun openOnBoardingFragment() {
        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToOnBoardingFragment())
    }

    private fun openHomeFragment() {
        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment2())
    }

    private fun openLoginFragment() {
        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
    }
}