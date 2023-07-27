package com.team.cat_hackathon.presentation.fragmentProfile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.databinding.FragmentProfileBinding
import com.team.cat_hackathon.utils.openFacebookIntent
import com.team.cat_hackathon.utils.openGithubIntent
import com.team.cat_hackathon.utils.openLinkedInIntent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding
    private val navArgs by navArgs<ProfileFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = navArgs.user
        setViews(user)
        setOnClicks(user)
    }

    private fun setOnClicks(user: User) {
        binding.apply {
            user.apply {
                facebookUrl?.let {
                    facebook.setOnClickListener {
                        openFacebookIntent(facebookUrl!! , requireContext())
                    }
                }
                githubUrl?.let {
                    gitHub.setOnClickListener {
                        openGithubIntent(githubUrl!! , requireContext())
                    }
                }
                linkedinUrl?.let {
                    linkedin.setOnClickListener {
                        openLinkedInIntent(linkedinUrl!!,requireContext())
                    }
                }
            }
        }
    }

    private fun setViews(user: User) {
        lifecycleScope.launch {

            binding.apply {
                tvName.text = user.name
                tvTrack.text = user.track
                tvEmail.text = user.email
                tvBio.text = user.bio
            }

            user.apply {
                facebookUrl?.let {
                    binding.facebook.isVisible = true
                }
                githubUrl?.let {
                    binding.gitHub.isVisible = true
                }
                linkedinUrl?.let {
                    binding.linkedin.isVisible = true
                }
            }

        }
    }



}