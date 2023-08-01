package com.team.cat_hackathon.presentation.fragmentOnBoarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.team.cat_hackathon.databinding.FragmentOnBoardingBinding
import com.team.cat_hackathon.presentation.adapters.OnBoardingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardingBinding
    private val viewModel: OnBoardingViewModel by viewModels()
    private lateinit var viewPager: ViewPager
    private lateinit var myAdapter: OnBoardingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnBoardingAdapter()
        setOnClicks()
    }

    private fun setOnClicks() {
        binding.apply {
            tvSkip.setOnClickListener {
                viewModel.setIsOnBoardingFinished(true)
                navigateToAuth()
            }

            btnNext.setOnClickListener {
                btnNextClicked()
            }
        }
    }

    private fun btnNextClicked() {
        val nextItem = viewPager.currentItem + 1
        if (nextItem < myAdapter.count) {
            viewPager.currentItem = nextItem
        } else {
            viewModel.setIsOnBoardingFinished(true)
            navigateToAuth()
        }
    }


    private fun setOnBoardingAdapter() {
        viewPager = binding.viewPager
        myAdapter = OnBoardingAdapter(viewModel.onBoardings)
        viewPager.adapter = myAdapter
        binding.dotsIndicator.attachTo(viewPager)
    }

    private fun navigateToAuth() {
        findNavController().navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToLoginFragment())
    }

}