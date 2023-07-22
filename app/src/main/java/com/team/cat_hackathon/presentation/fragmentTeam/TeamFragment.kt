package com.team.cat_hackathon.presentation.fragmentTeam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.team.cat_hackathon.R
import com.team.cat_hackathon.databinding.FragmentTeamsBinding

class TeamFragment : Fragment() {
    val navArgs  by navArgs<TeamFragmentArgs>()
    private lateinit var binding : FragmentTeamsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeamsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvGda.text = navArgs.team.name
    }
}