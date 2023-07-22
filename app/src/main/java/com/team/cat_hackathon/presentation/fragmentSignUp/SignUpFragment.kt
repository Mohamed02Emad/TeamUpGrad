package com.team.cat_hackathon.presentation.fragmentSignUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide.init
import com.team.cat_hackathon.R
import com.team.cat_hackathon.databinding.FragmentLoginBinding
import com.team.cat_hackathon.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClicks()
    }

    private fun setOnClicks() {
        binding.apply {
            textViewLogin.setOnClickListener {
                navigateToLogin()
            }
        }
    }

    private fun navigateToLogin() {
        findNavController().navigateUp()
    }

}