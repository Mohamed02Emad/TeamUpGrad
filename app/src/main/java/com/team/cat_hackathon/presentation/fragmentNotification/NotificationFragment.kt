package com.team.cat_hackathon.presentation.fragmentNotification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mo_chatting.chatapp.appClasses.isInternetAvailable
import com.team.cat_hackathon.R
import com.team.cat_hackathon.data.api.RequestState
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.databinding.FragmentNotificationBinding
import com.team.cat_hackathon.presentation.adapters.NotificationsAdapter
import com.team.cat_hackathon.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private lateinit var binding : FragmentNotificationBinding
    private lateinit var myAdapter : NotificationsAdapter
    private val viewModel: NotificationsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launch {
            if (isInternetAvailable(requireContext())) {
                if(viewModel.getCachedUser().isLeader != 1) {
                    showNoNotificationsAnimation()
                    return@launch
                }
                viewModel.getRequestList()
                setOnClicks()
                setObservers()
            }else{
                binding.lottieNoConnection.isVisible = true
                binding.lottieNoConnection.setAnimation(R.raw.anim_no_connection)
                binding.lottieNoConnection.playAnimation()
                binding.tvTitle.isVisible = false
            }
        }
    }

    private fun setObservers() {
        viewModel.responseState.observe(viewLifecycleOwner) { state ->
            state?.let {
                when (state) {
                    is RequestState.Error -> {
                        binding.progressBar.isVisible = false
                    }
                    is RequestState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is RequestState.Sucess -> {
                        binding.progressBar.isVisible = false
                        setRecyclerView(state.data!!.members)
                    }
                }
            }
        }
        viewModel.acceptState.observe(viewLifecycleOwner) { state ->
            state?.let {
                when (state) {
                    is RequestState.Error ->{
                        showToast(state.data?.message ?: "error", requireContext())
                    }
                    is RequestState.Loading -> {}
                    is RequestState.Sucess -> {
                        showToast(state.data!!.message, requireContext())
                        viewModel.setAcceptState(null)
                        removeUserFromList(viewModel.currentActionUser.value)
                        viewModel.setCurrentActionUser(null)
                    }
                }
            }
        }
        viewModel.rejectState.observe(viewLifecycleOwner) { state ->
            state?.let {
                when (state) {
                    is RequestState.Error, is RequestState.Loading -> {}
                    is RequestState.Sucess -> {
                        showToast(state.data!!.message, requireContext())
                        viewModel.setRejectState(null)
                        removeUserFromList(viewModel.currentActionUser.value)
                        viewModel.setCurrentActionUser(null)
                    }
                }
            }
        }
    }

    private fun removeUserFromList(userId: Int?) {
        userId?.let { userId ->
            myAdapter.removeUser(userId)
        }
    }

    private fun setRecyclerView(members: List<User>) {
        val mutableMembersList = mutableListOf<User>()
        mutableMembersList.addAll(members)
        myAdapter = NotificationsAdapter(
            mutableMembersList,
            acceptUser = viewModel.acceptUser,
            rejectUser = viewModel.rejectUser,
            userCliceked

        )
        binding.rvNotifications.adapter = myAdapter
        if (mutableMembersList.isEmpty()){
            showNoNotificationsAnimation()
        }
    }


    private fun setOnClicks() {
    }

    fun showNoNotificationsAnimation(){
        binding.lottieNoConnection.setAnimation(R.raw.anim_no_notification)
        binding.lottieNoConnection.isVisible = true
        binding.lottieNoConnection.playAnimation()
    }

    val userCliceked: (User) -> Unit = { user ->
        findNavController().navigate(NotificationFragmentDirections.actionNotificationFragmentToProfileFragment(user))
    }
}