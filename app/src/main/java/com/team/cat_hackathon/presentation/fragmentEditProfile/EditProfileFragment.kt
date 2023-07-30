package com.team.cat_hackathon.presentation.fragmentEditProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.team.cat_hackathon.R
import com.team.cat_hackathon.data.api.ApiVars.BASE_URL_WITHOUT_API
import com.team.cat_hackathon.data.api.RequestState
import com.team.cat_hackathon.databinding.FragmentEditProfileBinding
import com.team.cat_hackathon.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            getData()
            setOnClicks()
            setObservers()
            setChanges()
        }
    }

    private fun setObservers() {
        viewModel.updateUserResponse.observe(viewLifecycleOwner) { requestState ->
            requestState?.let {
                when (requestState) {
                    is RequestState.Error -> {
                        showToast(requestState.data?.message ?:"failed to update", requireContext())
                        lifecycleScope.launch {
                            binding.btnSave.revertAnimation()
                            delay(200)
                            binding.btnSave.isVisible = false
                        }
                        binding.progressBar.isVisible = false
                    }

                    is RequestState.Loading -> {
                    }

                    is RequestState.Sucess -> {
                        showToast(requestState.data?.message ?:"profile was updated" , requireContext())
                        lifecycleScope.launch {
                            binding.btnSave.revertAnimation()
                            delay(200)
                            binding.btnSave.isVisible = false
                            val newUser = requestState.data?.user
                            viewModel.updateCachedUser(newUser!!)
                        }
                        binding.progressBar.isVisible = false

                    }
                }
            }

        }
    }

    private fun setOnClicks() {
        binding.apply {
            backArrow.setOnClickListener {
                findNavController().navigateUp()
            }

            btnSave.apply {
                setOnClickListener {
                    startAnimation {
                        binding.progressBar.isVisible = true
                        val user = viewModel.getUser()
                        user?.let { user ->
                            user.githubUrl = binding.etGithubLink.text.toString()
                            user.facebookUrl = binding.etFacebookLink.text.toString()
                            user.linkedinUrl = binding.etLinkedInLink.text.toString()
                            user.name = binding.etName.text.toString()
                            user.bio = binding.etBio.text.toString()
                            user.track = binding.etTrack.text.toString()
                            viewModel.viewModelScope.launch {
                                viewModel.updateUser(user)
                                btnSave.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setChanges(){
        binding.apply {

            var name = etName.text.toString()
            var track = etTrack.text.toString()
//            var email = etEmail.text.toString()
            var email =""
            var bio = etBio.text.toString()
            var github = etGithubLink.text.toString()
            var linkedin = etLinkedInLink.text.toString()
            var facebook = etFacebookLink.text.toString()

            etName.doAfterTextChanged { text ->
                name = text.toString()
                if (viewModel.isInputEqualToCachedUser(
                         name, track, email, bio, github, linkedin, facebook
                    )
                ) {
                    btnSave.visibility = View.GONE
                } else {

                    btnSave.visibility = View.VISIBLE
                }
            }
            etTrack.doAfterTextChanged { text ->
                track = text.toString()
                if (viewModel.isInputEqualToCachedUser(
                       name, track, email, bio, github, linkedin, facebook
                    )
                ) {
                    btnSave.visibility = View.GONE
                } else {
                    btnSave.visibility = View.VISIBLE
                }
            }
//            etEmail.doAfterTextChanged { text ->
//                email = text.toString()
//                if (viewModel.isInputEqualToCachedUser(
//                      name, track, email, bio, github, linkedin, facebook
//                    )
//                ) {
//                    btnSave.visibility = View.GONE
//                } else {
//                    btnSave.visibility = View.VISIBLE
//                }
//            }
            etBio.doAfterTextChanged { text ->
                bio = text.toString()
                if (viewModel.isInputEqualToCachedUser(
                       name, track, email, bio, github, linkedin, facebook
                    )
                ) {
                    btnSave.visibility = View.GONE
                } else {
                    btnSave.visibility = View.VISIBLE
                }
            }
            etGithubLink.doAfterTextChanged { text ->
                github = text.toString()
                if (viewModel.isInputEqualToCachedUser(
                      name, track, email, bio, github, linkedin, facebook
                    )
                ) {
                    btnSave.visibility = View.GONE
                } else {
                    btnSave.visibility = View.VISIBLE
                }
            }
            etLinkedInLink.doAfterTextChanged { text ->
                linkedin = text.toString()
                if (viewModel.isInputEqualToCachedUser(
                         name, track, email, bio, github, linkedin, facebook
                    )
                ) {
                    btnSave.visibility = View.GONE
                }else{
                    btnSave.visibility = View.VISIBLE
                }
            }
            etFacebookLink.doAfterTextChanged {text ->
                facebook=text.toString()
                if(viewModel.isInputEqualToCachedUser(
                         name, track, email, bio, github, linkedin, facebook
                    )){
                    btnSave.visibility = View.GONE
                }else{
                    btnSave.visibility = View.VISIBLE
                }
            }

            ivUserImage.setOnClickListener{
                openGallery()
            }
        }
    }


    private suspend fun getData(){
        binding.apply {
            val cachedUser = viewModel.getCachedUser()
            etName.setText(cachedUser?.name ?: "")
            etTrack.setText(cachedUser?.track ?: "")
//            etEmail.setText(cachedUser?.email ?: "")
            etBio.setText(cachedUser?.bio ?: "")
            etGithubLink.setText(cachedUser?.githubUrl ?: "")
            etLinkedInLink.setText(cachedUser?.linkedinUrl ?: "")
            etFacebookLink.setText(cachedUser?.facebookUrl ?: "")

                Glide.with(ivUserImage)
                    .load(BASE_URL_WITHOUT_API+cachedUser.imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerInside()
                    .error(R.drawable.ic_profile)
                    .into(ivUserImage)

            btnSave.visibility = View.GONE
        }
    }

    private fun openGallery() {
        singlePhotoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    val singlePhotoPicker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.setImage(uri)
                        Glide.with(binding.ivUserImage)
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerInside()
                            .error(R.drawable.ic_profile)
                            .into(binding.ivUserImage)
                    viewModel.imageChanged.value = true
                    binding.btnSave.visibility = View.VISIBLE
                }
            }
        }

}