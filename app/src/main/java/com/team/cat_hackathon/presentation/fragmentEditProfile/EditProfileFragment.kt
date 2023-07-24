package com.team.cat_hackathon.presentation.fragmentEditProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.team.cat_hackathon.databinding.FragmentEditProfileBinding

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
    }

    private fun setOnClicks() {
        binding.apply {
            backArrow.setOnClickListener {
                findNavController().navigateUp()
            }

            btnSave.setOnClickListener {

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
            etName.doAfterTextChanged {text ->
                name= text.toString()
                if(viewModel.isInputEqualToCachedUser(
                    name,track,github,linkedin,facebook
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
                        name,track,github,linkedin,facebook
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
                        name,track,github,linkedin,facebook
                    )){
                    btnSave.visibility = View.GONE
                }else{
                    btnSave.visibility = View.VISIBLE
                    viewModel.setGithubUrl(github)
                }
            }
            etLinkedInLink.doAfterTextChanged {text ->
                linkedin=text.toString()
                if(viewModel.getUser()?.linkedinUrl == text.toString()){
                    btnSave.visibility = View.GONE
                }else{
                    btnSave.visibility = View.VISIBLE
                    viewModel.setLinkedinUrl(linkedin)
                }
            }
            etFacebookLink.doAfterTextChanged {text ->
                facebook=text.toString()
                if(viewModel.getUser()?.facebookUrl == text.toString()){
                    btnSave.visibility = View.GONE
                }else{
                    btnSave.visibility = View.VISIBLE
                    viewModel.setFacebookUrl(facebook)
                }
            }
        }
    }

}