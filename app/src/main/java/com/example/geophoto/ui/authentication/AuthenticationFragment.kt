package com.example.geophoto.ui.authentication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.data.constants.REMEMBER_USER
import com.example.geophoto.R
import com.example.geophoto.base.BaseFragment
import com.example.geophoto.databinding.FragmentAuthenticationBinding
import com.example.geophoto.delegates.viewBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import com.example.geophoto.R.id.action_authenticationFr_to_forgotPasswordFr as toForgotPassword
import com.example.geophoto.R.id.action_authenticationFr_to_galleryFr as toGallery
import com.example.geophoto.R.id.action_authenticationFr_to_registrationFr as toRegistration

@AndroidEntryPoint
class AuthenticationFragment : BaseFragment(R.layout.fragment_authentication),
    View.OnClickListener {

    private val binding: FragmentAuthenticationBinding by viewBinding()
    private val viewModel: AuthenticationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backPressedCloseApp(requireActivity())
        initListeners()
    }

    private fun initListeners() {
        binding.toRegisterBtn.setOnClickListener(this)
        binding.authMainBtn.setOnClickListener(this)
        binding.toForgotPasswordBtn.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.toRegisterBtn -> {
                navigate(toRegistration)
            }
            R.id.authMainBtn -> {
                authUser()
            }
            R.id.toForgotPasswordBtn -> {
                navigate(toForgotPassword)
            }
        }
    }

    private fun authUser() {
        val emailText = binding.authEmailInput.text.toString().trim()
        val passwordText = binding.authPasswordInput.text.toString().trim()
        when {
            emailText.isEmpty() -> toast(R.string.empty_email_field_message)
            passwordText.isEmpty() -> toast(R.string.empty_password_field_message)
            else -> {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            toast(R.string.success_login_message)
                            navigate(toGallery)

                        } else toast(R.string.error_login_message)
                    }
                viewModel.setSharedPref(REMEMBER_USER, binding.checkBoxRememberMeAuth.isChecked)
            }
        }
    }
}