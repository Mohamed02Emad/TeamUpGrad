package com.team.cat_hackathon.presentation.fragmentHome

import HomeAdapter
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mo_chatting.chatapp.appClasses.isInternetAvailable
import com.team.cat_hackathon.R
import com.team.cat_hackathon.data.api.RequestState
import com.team.cat_hackathon.data.models.AllDataResponse
import com.team.cat_hackathon.data.models.Team
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.databinding.FragmentHomeBinding
import com.team.cat_hackathon.presentation.MainActivity
import com.team.cat_hackathon.utils.openFacebookIntent
import com.team.cat_hackathon.utils.openGithubIntent
import com.team.cat_hackathon.utils.openLinkedInIntent
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
    private var bottomSheetDialog: BottomSheetDialog? = null

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
        setViews()
        attachTabLayoutToViewPager()
        setOnClicks()
        lifecycleScope.launch {
            setObservers()
            sendDataRequest()
        }
    }

    private suspend fun sendDataRequest() {
        if (isInternetAvailable(requireContext())) {
           // if (viewModel.homeDataRequestState.value == null)
                viewModel.requestHomeData()
        } else {
            showNoInterNetAnim()
        }
    }

    private fun setViews() {
        if (!isInternetAvailable(requireContext())){
            binding.btnCreateTeam.isVisible = false
        }
    }

    private fun setOnClicks() {
        binding.btnCreateTeam.setOnClickListener {
            openBottomSheet()
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
                        setAdaptersData(requestState)
                        setSearchFeature()
                    }
                }
            }
        }

        viewModel.createTeamRequestState.observe(viewLifecycleOwner) { requestState ->
            requestState?.let {
                when (requestState) {
                    is RequestState.Error -> {

                        showToast(requestState.data?.message ?: "team was not created ${requestState.message}", requireContext())
                        val btnDone = bottomSheetDialog?.findViewById<CircularProgressButton>(R.id.button_done)
                        btnDone?.let{
                            it.apply {
                               revertAnimation()
                                val progressBar = bottomSheetDialog?.findViewById<ProgressBar>(R.id.bottom_sheet_progress_bar)
                                progressBar?.isVisible = false
                            }
                        }
                    }
                    is RequestState.Loading -> {}
                    is RequestState.Sucess -> {
                        lifecycleScope.launch {
                            viewModel.updateCachedUser()
                        }
                        showToast("team was created", requireContext())
                        binding.btnCreateTeam.isGone = true
                        val btnDone = bottomSheetDialog?.findViewById<CircularProgressButton>(R.id.button_done)
                        btnDone?.let{
                            it.apply {
                                val progressBar =
                                    bottomSheetDialog?.findViewById<ProgressBar>(R.id.bottom_sheet_progress_bar)
                                progressBar?.isVisible = false
                                revertAnimation()
                            }
                        }
                        bottomSheetDialog?.dismissWithAnimation = true
                        bottomSheetDialog?.dismiss()
                        lifecycleScope.launch {
                            viewModel.requestHomeData()
                            viewModel.resetCreateTeamState()
                        }
                    }
                }
            }
        }

    }

    private fun setSearchFeature() {
        searchInAdapterLists(binding.searchET.text)
        binding.searchET.doAfterTextChanged { text ->
           searchInAdapterLists(text)
        }

    }

    private fun searchInAdapterLists(text: Editable?) {

        if (viewModel.isUserSearch.value!!) {


            if (text.toString() == viewModel.userSearch) return
            viewModel.userSearch = text.toString()
            if (text.isNullOrEmpty()) {
                myAdapter.usersAdapter.members?.clear()
                myAdapter.usersAdapter.members?.addAll(
                    viewModel.homeDataRequestState.value!!.data?.users ?: emptyList()
                )
            } else {
                myAdapter.usersAdapter.members?.clear()
                myAdapter.usersAdapter.members?.addAll(
                    viewModel.homeDataRequestState.value!!.data?.users
                        ?.filter {
                            it.name.lowercase().contains(text.toString().lowercase()) ||
                                    it.track?.lowercase()
                                        ?.contains(text.toString().lowercase()) ?: false
                        } ?: emptyList()
                )
            }
            myAdapter.usersAdapter.notifyDataSetChanged()


        } else {
            if (text.toString() == viewModel.teamSearch) return
            viewModel.teamSearch = text.toString()
            if (text.isNullOrEmpty()) {
                myAdapter.teamsAdapter.teams?.clear()
                myAdapter.teamsAdapter.teams?.addAll(
                    viewModel.homeDataRequestState.value!!.data?.teams ?: emptyList()
                )
            } else {
                myAdapter.teamsAdapter.teams?.clear()
                myAdapter.teamsAdapter.teams?.addAll(
                    viewModel.homeDataRequestState.value!!.data?.teams
                        ?.filter {
                            it.name.lowercase().contains(text.toString().lowercase())
                        }
                        ?: emptyList()
                )
            }
            myAdapter.teamsAdapter.notifyDataSetChanged()

        }
    }

    private fun setAdaptersData(requestState: RequestState<AllDataResponse>) {
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
            onTeamClicked,
            userClicekd,
            linkedInClicked,
            faceBookClicked,
            githubClicked
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
                        lifecycleScope.launch {
                            val isShowCreateTeamButton = !viewModel.isUserInTeam() && isInternetAvailable(requireContext())
                            if (isShowCreateTeamButton) {
                                binding.btnCreateTeam.isGone = false
                            }
                            viewModel.setSearchToUser(false)
                            setSearchFeature()
                        }
                    } else {
                        myAdapter.usersRecyclerView.scrollToPosition(0)
                        lifecycleScope.launch {
                            binding.btnCreateTeam.isGone = true
                            viewModel.setSearchToUser(true)
                            setSearchFeature()
                        }
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

    val userClicekd : (User , Int) -> Unit = {user ,_->
       findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment(user))
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


    private fun openBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.bottom_sheet_create_team)
        setBottomSheetOnClicks(dialog)
        dialog.show()
        bottomSheetDialog = dialog

    }

    private fun setBottomSheetOnClicks(dialog: BottomSheetDialog) {
        val btnDone = dialog.findViewById<CircularProgressButton>(R.id.button_done)
        val progressBar = dialog.findViewById<ProgressBar>(R.id.bottom_sheet_progress_bar)
        btnDone?.apply {
            setOnClickListener {
                startAnimation {
                    if (isInternetAvailable(requireContext())){
                    progressBar?.isVisible = true
                    val teamName = dialog.findViewById<EditText>(R.id.et_team_name)
                    val teamBio = dialog.findViewById<EditText>(R.id.et_team_bio)

                    if (teamBio?.text.toString().isNotBlank() && teamName?.text.toString()
                            .isNotBlank()
                    ) {
                        viewModel.createTeam(teamName?.text,teamBio?.text)
                    }else{
                        revertAnimation()
                        progressBar?.isVisible = false
                        showToast("enter all data",requireContext())
                    }
                    }else{
                        showSnackbar("No Internet connection" , requireContext(),binding.root)
                    }
                }
            }
        }
    }


}