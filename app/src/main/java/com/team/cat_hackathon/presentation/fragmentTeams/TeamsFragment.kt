package com.team.cat_hackathon.presentation.fragmentTeams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.team.cat_hackathon.R
import com.team.cat_hackathon.data.models.Team
import com.team.cat_hackathon.databinding.FragmentTeamsBinding
import com.team.cat_hackathon.presentation.MainActivity

class TeamsFragment : Fragment() {

    private lateinit var binding: FragmentTeamsBinding
    private val navArgs by navArgs<TeamsFragmentArgs>()

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
        setViewsVisibility(team)
        setOnClicks()
        initCollapsing()
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

    private fun setViewsVisibility(team: Team?) {
        if (team != null) {
            haveTeamUiVisibility(true)
            haveNoTeamUiVisibility(false)
        } else {
            //  val userTeam = viewModel.getUserTeam()
            // if (userTeam != null) {
            //  haveTeamUiVisibility(true)
            // haveNoTeamUiVisibility(false)
            // }else{
            haveTeamUiVisibility(false)
            haveNoTeamUiVisibility(true)
            // initRecyclerView()
            // }

            // TODO: remember to handle backbutton visibility alone
        }
    }

    private fun initRecyclerView() {
        val recyclerView = binding.recyclerViewInTeam
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        //recyclerView.adapter= TeamAdapter()
    }

    private fun haveNoTeamUiVisibility(value: Boolean) {
        binding.groupNotInTeam.isGone = !value
    }

    private fun haveTeamUiVisibility(value: Boolean) {
        binding.groupInTeam.isGone = !value
    }

    private fun initCollapsing(){
        (requireActivity() as MainActivity).setSupportActionBar(binding.myToolbar)
        (requireActivity() as MainActivity).getSupportActionBar()?.setDisplayShowTitleEnabled(false)

        binding.toolbar.findViewById<CardView>(R.id.btn_back)
    }

}