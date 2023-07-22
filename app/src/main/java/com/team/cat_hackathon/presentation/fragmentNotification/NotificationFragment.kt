package com.team.cat_hackathon.presentation.fragmentNotification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.team.cat_hackathon.R
import com.team.cat_hackathon.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {

    private lateinit var binding : FragmentNotificationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        return binding.root
    }

}