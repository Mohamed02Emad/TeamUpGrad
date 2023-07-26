package com.team.cat_hackathon.presentation.fragmentNotification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.team.cat_hackathon.data.api.RequestState
import com.team.cat_hackathon.data.models.Member
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
            viewModel.getRequestList()
        }
        setOnClicks()
        setObservers()
    }

    private fun setObservers() {
        viewModel.responseState.observe(viewLifecycleOwner) { state ->
            state?.let {
                when (state) {
                    is RequestState.Error, is RequestState.Loading -> {}
                    is RequestState.Sucess -> {
                        setRecyclerView(state.data!!.members)
                    }
                }
            }
        }
        viewModel.acceptState.observe(viewLifecycleOwner) { state ->
            state?.let {
                when (state) {
                    is RequestState.Error ->{
                        showToast(state.message ?: "error", requireContext())
                    }
                    is RequestState.Loading -> {}
                    is RequestState.Sucess -> {
                        showToast(state.data!!.message, requireContext())
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
                    }
                }
            }
        }
    }

    private fun setRecyclerView(members: List<Member>) {
        myAdapter = NotificationsAdapter(
            members,
            acceptUser = viewModel.acceptUser,
            rejectUser = viewModel.rejectUser
        )
        binding.rvNotifications.adapter = myAdapter
    }


    private fun setOnClicks() {
    }

}