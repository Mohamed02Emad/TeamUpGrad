package com.team.cat_hackathon.presentation.fragmentTeams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.team.cat_hackathon.databinding.FragmentTeamsBinding

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

    private fun onClickBack() {
        binding.btnBack.setOnClickListener {

        }
    }
    private fun onClickJoin(){
        binding.joinTextInTeam.setOnClickListener{

        }
    }
}