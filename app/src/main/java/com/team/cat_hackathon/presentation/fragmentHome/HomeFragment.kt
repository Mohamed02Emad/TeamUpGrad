package com.team.cat_hackathon.presentation.fragmentHome

import HomeAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.team.cat_hackathon.R
import com.team.cat_hackathon.data.models.Team
import com.team.cat_hackathon.databinding.FragmentHomeBinding
import com.team.cat_hackathon.presentation.MainActivity
import com.team.cat_hackathon.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var viewPager: ViewPager2
    private lateinit var myAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        (requireActivity() as MainActivity).setSupportActionBar(binding.myToolbar)
        (requireActivity() as MainActivity).getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewPager()
        attachTabLayoutToViewPager()
    }

    private fun setViewPager() {
        viewPager = binding.viewPager
//        myAdapter = HomeAdapter(
//                users = viewModel.individualResponse?.articles,
//                teams = viewModel.teamResponse?.articles
//        )

        // use fake data until we get data from backend
        myAdapter = HomeAdapter(
            users = viewModel.getFakeUsers(40),
            teams = viewModel.getFakeTeams(10),
            onTeamClicked
        )

        viewPager.adapter = myAdapter
        //viewPager.offscreenPageLimit = 1
        setOnPageChangeListener(viewPager)
    }

    private fun setOnPageChangeListener(viewPager: ViewPager2) {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                try {
                    if (position == 0) {
                        myAdapter.teamsRecyclerView.scrollToPosition(0)
                    } else {
                        myAdapter.usersRecyclerView.scrollToPosition(0)
                    }
                }catch (e:Exception){}
            }
        })
    }

    val onTeamClicked: (Team) -> Unit = { team ->
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToTeamsFragment(team))
    }

    private fun attachTabLayoutToViewPager() {
        val tabLayout = binding.toolbar.findViewById<TabLayout>(R.id.tebLayout)
        TabLayoutMediator(
           tabLayout,
            viewPager
        ) { tab, position ->
            if (position == 0)
                tab.setText(R.string.Teams)
            else
                tab.setText(R.string.Individuals)
        }.attach()

    }

}