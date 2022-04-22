package com.example.geophoto.ui.splash

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.example.data.constants.REMEMBER_USER
import com.example.domain.usecase.PrefsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val useCase: PrefsUseCase
):ViewModel() {
    fun getPrefs() = useCase.getSharedPref(REMEMBER_USER)
}