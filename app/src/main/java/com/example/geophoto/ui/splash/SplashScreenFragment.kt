package com.example.geophoto.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import com.example.geophoto.R
import com.example.geophoto.base.BaseFragment
import com.example.geophoto.base.LOADING_TIME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.geophoto.R.id.action_loadingScreenFr_to_authenticationFr as toAuthentication
import com.example.geophoto.R.id.action_loadingScreenFr_to_galleryFr as toGallery

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenFragment : BaseFragment(R.layout.fragment_splash_screen) {

    private val viewModel: SplashScreenViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(lifecycle.coroutineScope.coroutineContext).launch(Dispatchers.Main) {
            delay(LOADING_TIME)
            if (viewModel.getPrefs() == true) navigate(toAuthentication) else navigate(toGallery)
        }

    }
}