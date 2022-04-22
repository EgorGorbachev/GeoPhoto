package com.example.geophoto.ui.authentication

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.domain.usecase.PrefsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val useCase: PrefsUseCase
):ViewModel() {
    fun setSharedPref(prefName: String, value:Any) = useCase.setSharedPref(prefName, value)
}