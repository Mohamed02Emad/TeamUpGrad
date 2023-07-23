package com.team.cat_hackathon.presentation.fragmentHome

import HomeAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.androiddevs.mvvmnewsapp.data.api.RequestState
import com.google.android.material.tabs.TabLayoutMediator
import com.team.cat_hackathon.R
import com.team.cat_hackathon.data.models.Team
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.databinding.FragmentHomeBinding
import com.team.cat_hackathon.utils.showSnackbar
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.requestHomeData()
        }
        setViewPager()
        setObservers()
        attachTabLayoutToViewPager()
    }

    private fun setObservers() {
        viewModel.homeDataRequestState.observe(viewLifecycleOwner) { requestState ->
            requestState?.let {
                when (requestState) {
                    is RequestState.Error -> {
                        showSnackbar(
                            requestState.message ?: "Error",
                            requireContext(),
                            binding.root
                        )
                    }

                    is RequestState.Loading -> {

                    }

                    is RequestState.Sucess -> {
                        //todo : test this later
                        showSnackbar(
                            requestState.data!!.users[0].name,
                            requireContext(),
                            binding.root
                        )
                        viewModel.homeDataResponse?.teams?.let{
                            myAdapter.teamsAdapter.teams?.clear()
                            myAdapter.teamsAdapter.teams?.addAll(it)
                            myAdapter.teamsAdapter.notifyDataSetChanged()
                        }
                        viewModel.homeDataResponse?.users?.let{
                            myAdapter.usersAdapter.members?.clear()
                            myAdapter.usersAdapter.members?.addAll(it)
                            myAdapter.usersAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    private fun setViewPager() {
        viewPager = binding.viewPager
        val users = ArrayList<User>()
        val teams = ArrayList<Team>()
        viewModel.homeDataResponse?.users?.let {
            users.addAll(it)
        }
        viewModel.homeDataResponse?.teams?.let {
            teams.addAll(it)
        }
        myAdapter = HomeAdapter(
                users = users,
                teams = teams,
            onTeamClicked
        )

        viewPager.adapter = myAdapter
    }

    val onTeamClicked: (Team) -> Unit = { team ->
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToTeamsFragment(team))
    }

    private fun attachTabLayoutToViewPager() {
        TabLayoutMediator(
            binding.tebLayout,
            viewPager
        ) { tab, position ->
            if (position == 0)
                tab.setText(R.string.Teams)
            else
                tab.setText(R.string.Individuals)
        }.attach()
    }

}