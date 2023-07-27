package com.team.cat_hackathon.presentation.fragmentTeams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.mo_chatting.chatapp.appClasses.isInternetAvailable
import com.team.cat_hackathon.R
import com.team.cat_hackathon.data.api.RequestState
import com.team.cat_hackathon.data.models.Team
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.databinding.FragmentTeamsBinding
import com.team.cat_hackathon.presentation.MainActivity
import com.team.cat_hackathon.presentation.adapters.MembersAdapter
import com.team.cat_hackathon.presentation.fragmentHome.HomeFragmentDirections
import com.team.cat_hackathon.presentation.fragmentProfile.ProfileFragmentArgs
import com.team.cat_hackathon.utils.NO_TEAM
import com.team.cat_hackathon.utils.openFacebookIntent
import com.team.cat_hackathon.utils.openGithubIntent
import com.team.cat_hackathon.utils.openLinkedInIntent
import com.team.cat_hackathon.utils.showSnackbar
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
        if (!isInternetAvailable(requireContext())) {
            binding.lottieNoConnection.isVisible = true
            binding.lottieNoConnection.playAnimation()
        } else {
            CoroutineScope(Dispatchers.Main).launch {

                binding.progressBar.isVisible = true

                val cachedUser = viewModel.getCurrentUser()
                val currentUserTeamId = cachedUser.team_id

                val team =
                    if (navArgs.team != null) navArgs.team
                    else if (currentUserTeamId == -1) null
                    else viewModel.getCurrentUserTeam(currentUserTeamId!!)

                if (currentUserTeamId == NO_TEAM && team == null) {
                    setViewsVisibility(null)
                } else {
                    setViewsVisibility(team)
                    setObservers()
                }

                setOnClicks()
            }
        }
    }

    private fun setObservers() {
        try {
            viewModel.joinRequestState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is RequestState.Error -> {
                        binding.darkBackground.isGone = true
                        binding.progressBar.isVisible = false
                        showSnackbar(state.message ?: "Error", requireContext(), binding.root)
                    }
                    is RequestState.Loading -> {
                        binding.darkBackground.isVisible = true
                        binding.progressBar.isVisible = true
                    }
                    is RequestState.Sucess -> {
                        binding.darkBackground.isGone = true
                        binding.progressBar.isVisible = false
                        state.data?.let { response ->
                            showSnackbar(
                                state.message ?: "requested",
                                requireContext(),
                                binding.root
                            )
                            binding.joinTextInTeam.isGone = true
                        }
                    }

                }
            }
        } catch (e: Exception) {
        }
    }

    private fun setOnClicks() {
        binding.apply {
            buttonNotInTeam.setOnClickListener{
                navigateToHome()
            }
        }
    }

    private fun navigateToHome() {
        (activity as MainActivity).navigateToHome()
    }

    private suspend fun setViewsVisibility(team: Team?) {
        binding.progressBar.isVisible = false
        if (team != null) {
            initCollapsing(team.id)
            showDataOnScreen(true)
            setViews(team)
            initRecyclerView(viewModel.getUsers(team.id))
        } else {
            showDataOnScreen(false)
        }
    }

    private fun topBarViewsVisibility(teamId: Int, currentUserTeamId: Int?) {
        if (currentUserTeamId == teamId) {
            val btnBack = binding.toolbar.findViewById<CardView>(R.id.btn_back)
            btnBack.isGone = true
            binding.toolbar.findViewById<TextView>(R.id.joinText_inTeam).isGone = true
        }
    }

    private suspend fun setViews(team: Team) {
        val currentUserTeamId = viewModel.getCurrentUser().team_id
        topBarViewsVisibility(team.id, currentUserTeamId)
        val teamNameTv = binding.toolbar.findViewById<TextView>(R.id.teamName_inTeam)
        binding.teamBioInTeam.text = team.description
        teamNameTv.text = team.name

    }

    private fun initRecyclerView(usersList: List<User>) {
        val recyclerView = binding.recyclerViewInTeam
        val layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

        val arr = ArrayList<User>()
        arr.addAll(usersList)
        myAdapter = MembersAdapter(
            arr,
            userClicekd,
            linkedInClicked,
            faceBookClicked,
            githubClicked
            )
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = myAdapter
    }

    private fun showDataOnScreen(boolean: Boolean) {
        haveTeamUiVisibility(boolean)
        haveNoTeamUiVisibility(!boolean)
    }

    private fun haveNoTeamUiVisibility(value: Boolean) {
        binding.groupNotInTeam.isGone = !value
    }

    private fun haveTeamUiVisibility(value: Boolean) {
        binding.groupInTeam.isGone = !value
    }

    private fun initCollapsing(teamId: Int) {
        this.activity?.let {
            (requireActivity() as MainActivity).setSupportActionBar(binding.myToolbar)
            (requireActivity() as MainActivity).getSupportActionBar()
                ?.setDisplayShowTitleEnabled(false)
            setBarViewsVisibility()
            setBarClicks(teamId)
        }
    }

    private fun setBarViewsVisibility() {
        val btnJoin = binding.toolbar.findViewById<TextView>(R.id.joinText_inTeam)
        //todo : join button logic
        //if user is in team remove join us
        //if user is in team requests change text to requseted
    }

    private fun setBarClicks(teamId: Int) {
        val btnBack = binding.toolbar.findViewById<CardView>(R.id.btn_back)
        val btnJoin = binding.toolbar.findViewById<TextView>(R.id.joinText_inTeam)

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        btnJoin.setOnClickListener {
            lifecycleScope.launch {
                viewModel.sendJoinRequest(teamId)
            }
        }

    }

    val userClicekd : (User) -> Unit = {user ->
        try {
            findNavController().navigate(TeamsFragmentDirections.actionTeamsFragment2ToProfileFragment(user))
        }catch (e:Exception) {
            val args = ProfileFragmentArgs(user).toBundle()
            findNavController().navigate(R.id.action_teamsFragment_to_profileFragment , args)
        }
    }
    val linkedInClicked : (String) -> Unit = {url->
        openLinkedInIntent(url , requireContext())
    }
    val faceBookClicked : (String) -> Unit = {url->
        openFacebookIntent(url , requireContext())
    }
    val githubClicked : (String) -> Unit = {url->
        openGithubIntent(url , requireContext())
    }

}