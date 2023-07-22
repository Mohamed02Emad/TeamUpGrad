package com.team.cat_hackathon.presentation.fragmentTeams

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.GridLayoutManager
import com.team.cat_hackathon.R
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.databinding.FragmentHomeBinding
import com.team.cat_hackathon.databinding.FragmentTeamsBinding
import com.team.cat_hackathon.presentation.adapters.TeamAdapter

class TeamsFragment : Fragment() {

    private lateinit var binding: FragmentTeamsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTeamsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

    }
    companion object {
        @JvmStatic
        fun newInstance() =
            TeamsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private fun initRecyclerView(){
        val recyclerView =binding.recyclerViewInTeam
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        //recyclerView.adapter= TeamAdapter()
    }
    private fun haveNoTeamUiVisibility(value : Boolean){
        binding.groupNotInTeam.isGone = !value
    }
    private fun haveTeamUiVisibility(value : Boolean){
        binding.groupInTeam.isGone = !value
    }
    private fun onClickBack(){
        binding.backButton.setOnClickListener{

        }
    }
    private fun onClickJoin(){
        binding.joinTextInTeam.setOnClickListener{

        }
    }
}