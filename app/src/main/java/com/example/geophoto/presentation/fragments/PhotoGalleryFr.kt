package com.example.geophoto.presentation.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.geophoto.R
import com.example.geophoto.models.PhotoLocation
import com.example.geophoto.presentation.adapters.PhotoAdapter
import com.example.geophoto.presentation.base.BaseFragment
import com.example.geophoto.viewModels.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_photo_gallery.*

@AndroidEntryPoint
class PhotoGalleryFr : BaseFragment(R.layout.fragment_photo_gallery), PhotoAdapter.OnItemClick, PhotoAdapter.OnItemLongClick {
	
	private val viewModel by viewModels<GalleryViewModel>()
	
	private var adapter = PhotoAdapter(this, this)
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				Navigation.findNavController(requireView())
					.navigate(R.id.action_photoGalleryFr_to_photoGalleryFr)
			}
		})
	}
	
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		
		val recyclerView: RecyclerView = requireView().findViewById(R.id.photo_recycler)
		recyclerView.setHasFixedSize(true)
		
		recyclerView.adapter = adapter
		val layoutManager = GridLayoutManager(requireContext(), 3)
		recyclerView.layoutManager = layoutManager
		
		showPhotos()
		
		backToPhotoFrBtn.setOnClickListener {
			Navigation.findNavController(requireView())
				.navigate(R.id.action_photoGalleryFr_to_photoGalleryFr)
		}
		
	}
	
	private fun showPhotos() {
		viewModel.allData.observe(viewLifecycleOwner, {
			adapter.submitList(it)
		})
	}
	
	override fun onItemClick(photoLocation: PhotoLocation) {
		val action = PhotoGalleryFrDirections.actionPhotoGalleryFrToPhotoDetailsFr(photoLocation)
		findNavController().navigate(action)
	}
	
	override fun onItemLongClick(photoLocation: PhotoLocation) {
		val dialogBuilder = AlertDialog.Builder(requireContext())
		dialogBuilder.setMessage("Do you want to delete this photo?")
			.setCancelable(false)
			.setPositiveButton("Yes", DialogInterface.OnClickListener {
					dialog, id -> viewModel.deleteFromDatabase(photoLocation)
			})
			.setNegativeButton("No", DialogInterface.OnClickListener {
					dialog, id -> dialog.cancel()
			})
		val alert = dialogBuilder.create()
		alert.setTitle("Delete photo")
		alert.show()
	}
}