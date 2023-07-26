package com.team.cat_hackathon.presentation.fragmentEditProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.team.cat_hackathon.R
import com.team.cat_hackathon.databinding.FragmentEditProfileBinding
import com.team.cat_hackathon.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
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
            setChanges()
        }
    }

    private fun setOnClicks() {
        binding.apply {
            backArrow.setOnClickListener {
                findNavController().navigateUp()
            }

            btnSave.setOnClickListener {
                val user = viewModel.getUser()
                user?.let {
                    // Use viewModelScope to handle the coroutine
                    viewModel.viewModelScope.launch {
                        viewModel.updateUser(it)
                        btnSave.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setChanges(){
        binding.apply {

            var name = etName.text.toString()
            var track = etTrack.text.toString()
            var github = etGithubLink.text.toString()
            var linkedin = etLinkedInLink.text.toString()
            var facebook = etFacebookLink.text.toString()
            var img = viewModel.getUser()?.imageUrl ?: ""

            etName.doAfterTextChanged {text ->
                name= text.toString()
                if(viewModel.isInputEqualToCachedUser(
                    img,name,track,github,linkedin,facebook
                )){
                    btnSave.visibility = View.GONE
                }else{

                    btnSave.visibility = View.VISIBLE
                }
            }
            etTrack.doAfterTextChanged {text ->
                track = text.toString()
                if(viewModel.isInputEqualToCachedUser(
                        img, name,track,github,linkedin,facebook
                )){
                    btnSave.visibility = View.GONE
                }else{
                    btnSave.visibility = View.VISIBLE
                }
            }
            etGithubLink.doAfterTextChanged {text ->
                github=text.toString()
                if(viewModel.isInputEqualToCachedUser(
                        img,name,track,github,linkedin,facebook
                    )){
                    btnSave.visibility = View.GONE
                }else{
                    btnSave.visibility = View.VISIBLE
                }
            }
            etLinkedInLink.doAfterTextChanged {text ->
                linkedin=text.toString()
                if(viewModel.isInputEqualToCachedUser(
                        img,name,track,github,linkedin,facebook
                    )){
                    btnSave.visibility = View.GONE
                }else{
                    btnSave.visibility = View.VISIBLE
                }
            }
            etFacebookLink.doAfterTextChanged {text ->
                facebook=text.toString()
                if(viewModel.isInputEqualToCachedUser(
                        img,name,track,github,linkedin,facebook
                    )){
                    btnSave.visibility = View.GONE
                }else{
                    btnSave.visibility = View.VISIBLE
                }
            }
            ivUserImage.setOnClickListener{
                openGallery()
                img=viewModel.getImg()
                if(viewModel.isInputEqualToCachedUser(
                        img,name,track,github,linkedin,facebook
                    )){
                    btnSave.visibility = View.GONE
                }else{
                    btnSave.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun openGallery() {

    }
    private suspend fun getData(){
        binding.apply {
            val cachedUser = viewModel.getCachedUser()
            etName.setText(cachedUser?.name ?: "")
            etTrack.setText(cachedUser?.track ?: "")
            etGithubLink.setText(cachedUser?.githubUrl ?: "")
            etLinkedInLink.setText(cachedUser?.linkedinUrl ?: "")
            etFacebookLink.setText(cachedUser?.facebookUrl ?: "")
            cachedUser?.imageUrl?.let{url->
                Glide.with(ivUserImage)
                    .load(cachedUser.imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerInside()
                    .error(R.drawable.ic_profile)
                    .into(ivUserImage)
            }
            btnSave.visibility=View.GONE
        }
    }

}