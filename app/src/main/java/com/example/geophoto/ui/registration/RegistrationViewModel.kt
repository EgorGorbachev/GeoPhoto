package com.example.geophoto.ui.registration

import androidx.lifecycle.ViewModel
import com.example.data.constants.REMEMBER_USER
import com.example.domain.usecase.PrefsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val useCase: PrefsUseCase
) : ViewModel() {
    fun setSharedPref(value: Boolean) = useCase.setSharedPref(REMEMBER_USER, value)
}