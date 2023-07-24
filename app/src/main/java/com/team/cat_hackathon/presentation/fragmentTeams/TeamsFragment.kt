package com.team.cat_hackathon.presentation.fragmentTeams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.team.cat_hackathon.R
import com.team.cat_hackathon.data.models.Team
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.databinding.FragmentTeamsBinding
import com.team.cat_hackathon.presentation.MainActivity
import com.team.cat_hackathon.presentation.adapters.MembersAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeamsFragment : Fragment() {

    private lateinit var binding: FragmentTeamsBinding
    private val navArgs by navArgs<TeamsFragmentArgs>()
    private val viewModel: TeamsViewModel by viewModels()
    private lateinit var myAdapter: MembersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeamsBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val team = navArgs.team
        CoroutineScope(Dispatchers.Main).launch {
            setViewsVisibility(team)
            setOnClicks()
        }
    }

    private fun setOnClicks() {
        binding.apply {
            btnBack.setOnClickListener{
                findNavController().navigateUp()
            }

            joinTextInTeam.setOnClickListener{

            }

            buttonNotInTeam.setOnClickListener{
                navigateToHome()
            }


        }
    }

    private fun navigateToHome() {
        (activity as MainActivity).navigateToHome()
    }

    private suspend fun setViewsVisibility(team: Team?) {
        if (team != null) {
            haveTeamUiVisibility(true)
            haveNoTeamUiVisibility(false)
            //todo : get list from team object
            initRecyclerView(viewModel.getFakeUsers(7))
            initCollapsing()
        } else {
            val userTeam = viewModel.getCurrentUserTeam()
            if (userTeam != null) {
                haveTeamUiVisibility(true)
                haveNoTeamUiVisibility(false)
                initRecyclerView(viewModel.getFakeUsers(7))
                initCollapsing()
            } else {
                haveTeamUiVisibility(false)
                haveNoTeamUiVisibility(true)
            }

            // TODO: remember to handle backbutton visibility alone
        }
    }

    private fun initRecyclerView(usersList: ArrayList<User>?) {
        val recyclerView = binding.recyclerViewInTeam
        val layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        myAdapter = MembersAdapter(usersList)
        recyclerView.adapter = myAdapter
    }

    private fun haveNoTeamUiVisibility(value: Boolean) {
        binding.groupNotInTeam.isGone = !value
    }

    private fun haveTeamUiVisibility(value: Boolean) {
        binding.groupInTeam.isGone = !value
    }

    private fun initCollapsing(){
            (requireActivity() as MainActivity).setSupportActionBar(binding.myToolbar)
            (requireActivity() as MainActivity).getSupportActionBar()
                ?.setDisplayShowTitleEnabled(false)
            binding.toolbar.findViewById<CardView>(R.id.btn_back)
    }

}