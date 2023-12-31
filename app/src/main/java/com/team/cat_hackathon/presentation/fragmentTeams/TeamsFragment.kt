package com.team.cat_hackathon.presentation.fragmentTeams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
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
import com.team.cat_hackathon.presentation.fragmentProfile.ProfileFragmentArgs
import com.team.cat_hackathon.utils.NO_TEAM
import com.team.cat_hackathon.utils.openFacebookIntent
import com.team.cat_hackathon.utils.openGithubIntent
import com.team.cat_hackathon.utils.openLinkedInIntent
import com.team.cat_hackathon.utils.showDialog
import com.team.cat_hackathon.utils.showSnackbar
import com.team.cat_hackathon.utils.showToast
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
            showNoInternetState()
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                binding.progressBar.isVisible = true

                val cachedUser = viewModel.getCurrentUser()
                val currentUserTeamId = cachedUser.team_id

                val team = getCuurentTeamOrNull(currentUserTeamId)

                setViewModelTeamData(team)

                if (currentUserTeamId == NO_TEAM && team == null) {
                    setViewsVisibility(null, cachedUser)
                } else {
                    setViewsVisibility(team, cachedUser)
                    setObservers(team, cachedUser)
                }
                setOnClicks()
                setOnTextChanges()
            }
        }
    }

    private fun setOnTextChanges() {
        binding.apply {
            teamBioInTeam.doAfterTextChanged { newText ->
                val bio = newText.toString()
                viewModel.currentTeamBio = bio
                if (viewModel.hasTeamDataChanged()) {
                    setActionButtonToSava()
                } else {
                    setActionButtonToDelete()
                }
            }

            teamNameInTeam.doAfterTextChanged { newText ->
                val name = newText.toString()
                viewModel.currentTeamName = name
                if (viewModel.hasTeamDataChanged()) {
                    setActionButtonToSava()
                } else {
                    setActionButtonToDelete()
                }
            }
        }
    }


    private fun showNoInternetState() {
        binding.lottieNoConnection.isVisible = true
        binding.lottieNoConnection.playAnimation()
    }


    private fun setObservers(team: Team?, cachedUser: User) {
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
                                state.data.message ?: "requested",
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
        viewModel.isEditMode.observe(viewLifecycleOwner) { isEditMode ->
            val btnJoin = binding.toolbar.findViewById<TextView>(R.id.joinText_inTeam)

            lifecycleScope.launch {
                val isThisTeamLeader =
                    cachedUser.isLeader == 1 && team != null && team.id == cachedUser.team_id
                if (!isThisTeamLeader) return@launch

                enableEditTexts(isEnabled = isEditMode)

                if (isEditMode) {
                    btnJoin.text = "Cancel"
                    binding.btnTeamAction.isVisible = true
                    showDeleteUserIconOnUsers(true)

                } else {
                    btnJoin.text = "Edit"
                    binding.btnTeamAction.isVisible = false
                    showDeleteUserIconOnUsers(false)
                }
            }
        }

        viewModel.deleteState.observe(viewLifecycleOwner){state->
            state?.let {
                when(state) {
                    is RequestState.Error -> {
                        showToast("${state.message}" , requireContext())
                    }
                    is RequestState.Loading -> {}
                    is RequestState.Sucess -> {
                        viewModel.deletedUserPosition?.let {
                            myAdapter.notifyItemRemoved(it)
                        }
                        viewModel.deletedUserPosition = null
                    }
                }
            }
        }

        viewModel.deleteTeamState.observe(viewLifecycleOwner){state ->
            state?.let {
                when (state) {
                    is RequestState.Error -> showSnackbar(state.data?.message ?: "error" , requireContext() , binding.root)
                    is RequestState.Loading -> {}
                    is RequestState.Sucess -> {
                        lifecycleScope.launch {
                            binding.btnProgressBar.isVisible = false
                            viewModel.updateCachedUserWithoutTeam()
                            (activity as MainActivity).navigateToHome()
                        }
                    }
                }
            }
        }

        viewModel.leaveTeamState.observe(viewLifecycleOwner){state ->
            state?.let {
                when (state) {
                    is RequestState.Error -> showSnackbar(state.data?.message ?: "error" , requireContext() , binding.root)
                    is RequestState.Loading -> {}
                    is RequestState.Sucess -> {
                        lifecycleScope.launch {
                            viewModel.updateCachedUserWithoutTeam()
                            (activity as MainActivity).navigateToHome()
                        }
                    }
                }
            }
        }

        viewModel.updateTeamState.observe(viewLifecycleOwner){state ->
            state?.let {
                binding.btnTeamAction.revertAnimation()
                binding.btnProgressBar.isVisible= false
                when (state) {
                    is RequestState.Error -> showSnackbar(state.data?.message ?: "error" , requireContext() , binding.root)
                    is RequestState.Loading -> {}
                    is RequestState.Sucess -> {
                        showToast("Team was Updated", requireContext())
                        setActionButtonToDelete()
                        viewModel.cachedTeamBio = viewModel.currentTeamName
                        viewModel.cachedTeamBio = viewModel.currentTeamBio
                    }
                }
            }
        }
    }


    private fun showDeleteUserIconOnUsers(isVisible: Boolean) {
        for (i in 0 until viewModel.users.value!!.size) {
            if( viewModel.users.value!![i].isLeader != 1) {
                viewModel.users.value!![i].isCheck = isVisible
                myAdapter.notifyItemChanged(i)
            }
        }
    }
    private fun setOnClicks() {
        binding.apply {
            buttonNotInTeam.setOnClickListener {
                navigateToHome()
            }
            btnTeamAction.apply {
                setOnClickListener {
                    startAnimation {
                        if (isInternetAvailable(requireContext())) {

                            binding.btnProgressBar.isVisible = true
                            val btnState = viewModel.actionButtonState.value
                            if (btnState == ActionButtonState.SAVE) {
                                lifecycleScope.launch {
                                    if (viewModel.hasTeamDataChanged()) {
                                        viewModel.updateTeam(
                                            viewModel.currentTeamName!!,
                                            viewModel.currentTeamBio!!
                                        )
                                    }
                                }
                            } else {
                                showDialog(
                                    requireContext(),
                                    "Delete Team",
                                    "are you sure you want to delete this team ?",
                                    positiveClicked = {
                                        lifecycleScope.launch {
                                            viewModel.deleteTeam()
                                        }
                                    },
                                    negativeClicked = {
                                        binding.btnProgressBar.isVisible = false
                                        binding.btnTeamAction.revertAnimation()
                                    }
                                )
                            }

                        } else {
                            showToast("No Internet Connection", requireContext())
                        }
                    }
                }
            }
        }
    }


    private fun navigateToHome() {
        (activity as MainActivity).navigateToHome()
    }


    private suspend fun setViewsVisibility(team: Team?, cachedUser: User) {
        binding.progressBar.isVisible = false
        if (team != null) {
            initCollapsing(team.id, cachedUser)
            showDataOnScreen(true)
            setViews(team)
            viewModel.getUsers(team.id)
            initRecyclerView()
        } else {
            showDataOnScreen(false)
        }
    }

    private fun topBarViewsVisibility(teamId: Int, currentUserTeamId: Int?) {
        if (currentUserTeamId == teamId) {
            val btnBack = binding.toolbar.findViewById<CardView>(R.id.btn_back)
            btnBack.isGone = true
        }
    }
    private suspend fun setViews(team: Team) {
        val currentUserTeamId = viewModel.getCurrentUser().team_id
        topBarViewsVisibility(team.id, currentUserTeamId)
        val teamNameTv = binding.toolbar.findViewById<EditText>(R.id.teamName_inTeam)
        binding.teamBioInTeam.setText(team.description)
        teamNameTv.setText(team.name)
    }

    private fun initRecyclerView() {
        val recyclerView = binding.recyclerViewInTeam
        val layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

        myAdapter = MembersAdapter(
            viewModel.users.value,
            userClicekd,
            linkedInClicked,
            faceBookClicked,
            githubClicked,
            userLongClick
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

    private fun initCollapsing(teamId: Int, cachedUser: User) {
        this.activity?.let {
            (requireActivity() as MainActivity).setSupportActionBar(binding.myToolbar)
            (requireActivity() as MainActivity).getSupportActionBar()
                ?.setDisplayShowTitleEnabled(false)
            setJoinButtonLogicAndVisibility(teamId, cachedUser)
            setBarClicks()
        }
    }

    private fun setJoinButtonLogicAndVisibility(teamId: Int, cachedUser: User) {
        val btnJoin = binding.toolbar.findViewById<TextView>(R.id.joinText_inTeam)

        if (cachedUser.isLeader == 1 && cachedUser.team_id == teamId) {
            btnJoin?.text = "Edit"
            btnJoin.isVisible = true
            btnJoin.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.triggerEditMode()
                }
            }
        } else if (cachedUser.team_id == teamId) {
            btnJoin.isVisible = true
            btnJoin?.text = "Leave"
            btnJoin.setOnClickListener {
                showDialog(
                    requireContext(),
                    "Leave Team ",
                    "are you sure you want to leave ?",
                 positiveClicked = {
                    lifecycleScope.launch {
                        viewModel.leaveTeam()
                    }
                }
                )
            }
        } else if (cachedUser.team_id!! > 0) {
            btnJoin.isVisible = false
        } else {
            btnJoin.setOnClickListener {
                btnJoin.isVisible = true
                lifecycleScope.launch {
                    viewModel.sendJoinRequest(teamId)
                }
            }
        }
    }

    private fun setBarClicks() {
        val btnBack = binding.toolbar.findViewById<CardView>(R.id.btn_back)
        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    val userClicekd: (User, Int) -> Unit = { user, position ->
        if (viewModel.isEditMode.value!!) {
            if (user.isLeader != 1) {
                showDialog(
                    requireContext(),
                    "Remove User",
                    "remove ${user.name} from team ?",
                 positiveClicked = {
                    lifecycleScope.launch {
                        viewModel.deleteUser(user, position)
                    }
                }
                )
            }
            true
        } else {
            try {
                val args = ProfileFragmentArgs(user).toBundle()
                findNavController().navigate(R.id.action_teamsFragment2_to_profileFragment, args)

            } catch (e: Exception) {
                val args = ProfileFragmentArgs(user).toBundle()
                findNavController().navigate(R.id.action_teamsFragment_to_profileFragment, args)
            }
            false
        }
    }


    val userLongClick: (User, Int) -> Unit = { user, position ->
        if (!viewModel.isEditMode.value!! && user.isLeader == 1) {
            viewModel.triggerEditMode()
        }
    }
    val linkedInClicked: (String) -> Unit = { url ->
        openLinkedInIntent(url, requireContext())
    }
    val faceBookClicked: (String) -> Unit = { url ->
        openFacebookIntent(url, requireContext())
    }
    val githubClicked: (String) -> Unit = { url ->
        openGithubIntent(url, requireContext())
    }


    private fun enableEditTexts(isEnabled: Boolean) {
        binding.teamNameInTeam.isEnabled = isEnabled
        binding.teamBioInTeam.isEnabled = isEnabled
    }

    private suspend fun setViewModelTeamData(team: Team?) {
        viewModel.cachedTeamName = team?.name
        viewModel.cachedTeamBio = team?.name
        viewModel.currentTeamBio = team?.name
        viewModel.currentTeamName = team?.name
    }

    private suspend fun getCuurentTeamOrNull(currentUserTeamId: Int?): Team? {
        viewModel.sycnUser()
       return if (navArgs.team != null) navArgs.team
        else if (currentUserTeamId == -1) null
        else viewModel.getCurrentUserTeam(currentUserTeamId!!)
    }


    private fun setActionButtonToSava() {
        val btn = binding.btnTeamAction
        btn.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_auth_background)
        btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_background))
        btn.setText("Save")
        viewModel.setActionButtonState(ActionButtonState.SAVE)

    }

    private fun setActionButtonToDelete() {
        val btn = binding.btnTeamAction
        btn.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_delete_background)
        btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_blue))
        btn.setText("Delete")
        viewModel.setActionButtonState(ActionButtonState.DELETE)

    }

}