package com.team.cat_hackathon.presentation.fragmentHome

import HomeAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mo_chatting.chatapp.appClasses.isInternetAvailable
import com.team.cat_hackathon.R
import com.team.cat_hackathon.data.api.RequestState
import com.team.cat_hackathon.data.models.Team
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.databinding.FragmentHomeBinding
import com.team.cat_hackathon.presentation.MainActivity
import com.team.cat_hackathon.utils.showSnackbar
import com.team.cat_hackathon.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
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
        lifecycleScope.launch {
            setObservers()
            if (isInternetAvailable(requireContext())) {
                if (viewModel.homeDataRequestState.value == null)
                    viewModel.requestHomeData()
            }else{
                showNoInterNetAnim()
            }
        }
    }

    private fun showNoInterNetAnim() {
        binding.lottieNoConnection.isVisible = true
        binding.lottieNoConnection.playAnimation()
        binding.searchLayout.isVisible = false
        binding.collapsingBar.isVisible = false

    }

    private fun setObservers() {
        viewModel.homeDataRequestState.observe(viewLifecycleOwner) { requestState ->
            requestState?.let {
                when (requestState) {
                    is RequestState.Error -> {
                        binding.progressBar.isVisible = false
                    }
                    is RequestState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is RequestState.Sucess -> {
                        binding.progressBar.isVisible = false

                        myAdapter.teamsAdapter.teams?.clear()
                        myAdapter.teamsAdapter.teams?.addAll(
                            requestState.data?.teams ?: emptyList()
                        )
                        myAdapter.teamsAdapter.notifyDataSetChanged()

                        myAdapter.usersAdapter.members?.clear()
                        myAdapter.usersAdapter.members?.addAll(
                            requestState.data?.users ?: emptyList()
                        )
                        myAdapter.usersAdapter.notifyDataSetChanged()
                    }
                    }
                }
            }
        }

    private fun setViewPager() {
        viewPager = binding.viewPager
        val users = ArrayList<User>()
        val teams = ArrayList<Team>()

        viewModel.homeDataRequestState.value?.data?.users.let {
            users.addAll(it ?: emptyList())
        }
        viewModel.homeDataRequestState.value?.data?.teams.let {
            teams.addAll(it ?: emptyList())
        }
        myAdapter = HomeAdapter(
            users = users,
            teams = teams,
            onTeamClicked
        )

        viewPager.adapter = myAdapter
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