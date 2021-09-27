package com.example.geophoto.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.geophoto.R
import com.example.geophoto.presentation.base.BaseFragment
import com.example.geophoto.viewModels.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoadingScreenFr : BaseFragment(R.layout.fragment_loading_screen) {
	
	private val mapViewModel by viewModels<PhotoViewModel>()
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		GlobalScope.launch(Dispatchers.Main) {
			delay(3000)
			Navigation.findNavController(requireView())
				.navigate(R.id.action_loadingScreenFr_to_galleryFr)
		}
	}
}