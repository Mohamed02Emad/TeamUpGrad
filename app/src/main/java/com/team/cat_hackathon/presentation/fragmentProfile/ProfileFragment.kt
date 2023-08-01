package com.team.cat_hackathon.presentation.fragmentProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.labters.imagestackviewer.data.ImageData
import com.labters.imagestackviewer.data.ResourceType
import com.labters.imagestackviewer.presentation.StackImageViewer
import com.team.cat_hackathon.R
import com.team.cat_hackathon.data.api.ApiVars.BASE_URL_WITHOUT_API
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
                        openLinkedInIntent(linkedinUrl!!, requireContext())
                    }
                }
            }

            backArrow.setOnClickListener {
                findNavController().navigateUp()
            }

            ivUserImage.setOnClickListener {
                user.imageUrl?.let {url ->
                    if (url.isNotBlank()) {
                        showProfileImage(BASE_URL_WITHOUT_API + url)
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

            Glide.with(binding.ivUserImage)
                .load(BASE_URL_WITHOUT_API +user.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerInside()
                .error(R.drawable.ic_profile)
                .into(binding.ivUserImage)

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

            val hideSocialMediaText =
                user.facebookUrl.isNullOrBlank() &&
                        user.linkedinUrl.isNullOrBlank() &&
                        user.githubUrl.isNullOrBlank()

            if (hideSocialMediaText) {
                binding.titleTvSocialLicks.isGone = true
            }

        }
    }


    private fun showProfileImage(imageUrl: String) {
        StackImageViewer.openStackViewer(
            activity = requireActivity(),
            list = listOf(
                ImageData(ResourceType.UrlResource(imageUrl)),
            ), view = binding.ivUserImage
        )
    }


}