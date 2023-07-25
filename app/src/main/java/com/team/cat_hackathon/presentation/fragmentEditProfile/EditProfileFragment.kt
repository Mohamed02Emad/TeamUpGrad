package com.team.cat_hackathon.presentation.fragmentEditProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.team.cat_hackathon.R
import com.team.cat_hackathon.databinding.FragmentEditProfileBinding
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
        setOnClicks()
        getData()
        setChanges()
    }

    private fun setOnClicks() {
        binding.apply {
            backArrow.setOnClickListener {
                findNavController().navigateUp()
            }

            btnSave.setOnClickListener {
                viewModel.setName(etName.text.toString())
                viewModel.setTrack(etTrack.text.toString())
                viewModel.setGithubUrl(etGithubLink.text.toString())
                viewModel.setLinkedinUrl(etLinkedInLink.text.toString())
                viewModel.setFacebookUrl(etFacebookLink.text.toString())

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
                    viewModel.setName(name)
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
                    viewModel.setTrack(track)
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
                    viewModel.setGithubUrl(github)
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
                    viewModel.setLinkedinUrl(linkedin)
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
                    viewModel.setFacebookUrl(facebook)
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
                    viewModel.setImgUrl(img)
                }
            }
        }
    }

    private fun openGallery() {

    }
    private fun getData(){
        binding.apply {
            val cachedUser = viewModel.getUser()
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
                    .error(R.drawable.ellipse)
                    .into(ivUserImage)
            }
        }
    }

}