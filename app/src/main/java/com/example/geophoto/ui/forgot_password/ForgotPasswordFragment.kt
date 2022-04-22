package com.example.geophoto.ui.forgot_password

import android.os.Bundle
import android.view.View
import com.example.geophoto.R
import com.example.geophoto.base.BaseFragment
import com.example.geophoto.databinding.FragmentForgotPasswordBinding
import com.example.geophoto.delegates.viewBinding
import com.google.firebase.auth.FirebaseAuth
import com.example.geophoto.R.id.action_forgotPasswordFr_to_authenticationFr as toAuthentication

class ForgotPasswordFragment : BaseFragment(R.layout.fragment_forgot_password) {

    private val binding: FragmentForgotPasswordBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.forgotPassSubmitBtn.setOnClickListener {
            val email = binding.forgotPasswordEmailInput.text.toString()
            if (email.isEmpty()) {
                toast(R.string.forgot_password_empty_email_message)
            } else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnSuccessListener {
                    navigate(toAuthentication)
                    toast(R.string.forgot_password_success_sent_message)
                }
            }

        }
    }

}