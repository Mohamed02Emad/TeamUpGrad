package com.team.cat_hackathon.presentation.fragmentOnBoarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.team.cat_hackathon.R
import com.team.cat_hackathon.data.models.OnBoarding
import com.team.cat_hackathon.databinding.FragmentOnBoardingBinding
import com.team.cat_hackathon.presentation.adapters.OnBoardingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardingBinding
    private val viewModel : OnBoardingViewModel by viewModels()
    private lateinit var myAdapter: OnBoardingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentOnBoardingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnBoardingAdapter()
    }

    private fun setOnBoardingAdapter() {
        myAdapter = OnBoardingAdapter(viewModel.onBoardings)
        binding.viewPager.adapter = myAdapter
        binding.dotsIndicator.attachTo(binding.viewPager)
    }

}