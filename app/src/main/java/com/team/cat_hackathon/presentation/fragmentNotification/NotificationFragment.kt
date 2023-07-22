package com.team.cat_hackathon.presentation.fragmentNotification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.team.cat_hackathon.R
import com.team.cat_hackathon.databinding.FragmentNotificationBinding
import com.team.cat_hackathon.presentation.adapters.NotificationsAdapter
import dagger.hilt.android.AndroidEntryPoint

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
        setOnClicks()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        myAdapter = NotificationsAdapter(viewModel.getFakeUsers(4))
        binding.rvNotifications.adapter=myAdapter
    }

    private fun setOnClicks() {
    }

}