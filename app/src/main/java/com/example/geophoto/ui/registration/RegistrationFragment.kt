package com.example.geophoto.ui.registration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.geophoto.R
import com.example.geophoto.base.BaseFragment
import com.example.geophoto.databinding.FragmentRegistrationBinding
import com.example.geophoto.delegates.viewBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import com.example.geophoto.R.id.action_registrationFr_to_authenticationFr as toAuthentication
import com.example.geophoto.R.id.action_registrationFr_to_galleryFr as toGallery

@AndroidEntryPoint
class RegistrationFragment : BaseFragment(R.layout.fragment_registration), View.OnClickListener {

    private val binding: FragmentRegistrationBinding by viewBinding()
    private val viewModel: RegistrationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.registrationToLoginButton.setOnClickListener(this)
        binding.registrationButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.registrationToLoginButton -> navigate(toAuthentication)
            R.id.registrationButton -> checkUserInfo()
        }
    }

    private fun checkUserInfo() {
        hideKeyboard()
        val emailText = binding.registrationEmailInput.text
        val passText = binding.registrationPasswordInput.text
        val repeatPassText = binding.registrationRepeatPasswordInput.text
        when {
            emailText.isEmpty() -> toast(R.string.registration_empty_email_field_message)
            passText.isEmpty() -> toast(R.string.registration_empty_password_field_message)
            passText.length < 6 -> toast(R.string.registration_password_symbols_message)
            repeatPassText.isEmpty() -> toast(R.string.registration_empty_password_confirm_field_message)
            repeatPassText.toString() != passText.toString() -> toast(R.string.registration_passwords_do_not_match_message)
            else -> {
                registerUser(emailText.toString(), passText.toString())
                checkRememberMe()
            }
        }
    }

    private fun checkRememberMe() {
        if (binding.registrationCheckBox.isChecked) viewModel.setSharedPref(true)
        else viewModel.setSharedPref(false)
    }

    private fun registerUser(emailText: String, passText: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            emailText.trim(),
            passText.trim()
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navigate(toGallery)
                toast(R.string.registration_success_message)
            } else simpleToast(requireContext(), task.exception.toString())
        }
    }
}