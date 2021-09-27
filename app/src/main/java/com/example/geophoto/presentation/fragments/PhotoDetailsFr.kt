package com.example.geophoto.presentation.fragments

import android.R.layout
import android.app.AlertDialog
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.geophoto.R
import com.example.geophoto.models.PhotoLatLng
import com.example.geophoto.models.PhotoLocation
import com.example.geophoto.presentation.base.BaseFragment
import com.example.geophoto.viewModels.PhotoDetailsViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_photo_details.*
import kotlinx.android.synthetic.main.fragment_photo_details.view.*
import kotlinx.android.synthetic.main.info_menu.*
import kotlinx.android.synthetic.main.info_menu.view.*


@AndroidEntryPoint
class PhotoDetailsFr : BaseFragment(R.layout.fragment_photo_details) {
	
	private val args by navArgs<PhotoDetailsFrArgs>()
	private val viewModel by viewModels<PhotoDetailsViewModel>()
	
	private lateinit var bottomSheetDialog: BottomSheetDialog
	private lateinit var bottomSheetView: View
	
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		viewModel.photoDetailsPhoto = args.photoLocation.photo
		viewModel.photoDetailsAddress = args.photoLocation.address
		viewModel.photoDetailsAddress = args.photoLocation.address
		viewModel.photoDetailsComment= args.photoLocation.comment
		viewModel.photoDetailsLatLng =
			LatLng(args.photoLocation.latitude, args.photoLocation.longitude)
		Log.e("lollll", viewModel.photoDetailsLatLng.toString())
		
		photoDetailsImg.setImageURI(viewModel.photoDetailsPhoto)
		
		bottomSheetDialog = BottomSheetDialog(requireActivity())
		
		bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.info_menu, bottomSheet)
		
		backToPhotoGalleryFrBtn.setOnClickListener {
			Navigation.findNavController(requireView()).navigate(R.id.action_photoDetailsFr_to_photoGalleryFr)
		}
		
		showDetailsInfoBtn.setOnClickListener {
			bottomSheetDialog.setContentView(bottomSheetView)
			bottomSheetDialog.show()
			
			bottomSheetView.photoDetailsAddress.text = viewModel.photoDetailsAddress
			bottomSheetView.photoDetailsComment.text = viewModel.photoDetailsComment
			
			bottomSheetView.editCommentBtn.setOnClickListener {
				addCommentAlertDialog()
			}
			
			bottomSheetView.showPhotoLocationOnMap.setOnClickListener {
				val action = PhotoDetailsFrDirections.actionPhotoDetailsFrToMapFr(
					PhotoLatLng(args.photoLocation.latitude, args.photoLocation.longitude)
				)
				findNavController().navigate(action)
			}
			
		}
	}
	
	private fun addCommentAlertDialog() {
		val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
		alertDialog.setTitle("Your comment")
		val input = EditText(requireContext())
		val lp = LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT,
			LinearLayout.LayoutParams.MATCH_PARENT
		)
		input.layoutParams = lp
		alertDialog.setView(input)
		
		alertDialog.setPositiveButton(
			"YES"
		) { dialog, which ->
			insertDatabase(
				viewModel.photoDetailsPhoto!!,
				viewModel.photoDetailsAddress!!,
				viewModel.photoDetailsLatLng?.latitude!!,
				viewModel.photoDetailsLatLng?.longitude!!,
				input.text.toString()
			)
			bottomSheetView.photoDetailsComment.text = input.text.toString()
		}
		alertDialog.show()
		
	}
	
	private fun insertDatabase(
		photo: Uri,
		address: String,
		latitude: Double,
		longitude: Double,
		comment:String
	) {
		val photoLocation = PhotoLocation( photo, address, latitude, longitude, comment)
		viewModel.insertDatabase(photoLocation)
	}
	
	override fun onStop() {
		super.onStop()
		bottomSheetDialog.dismiss()
	}
	
	override fun onDestroy() {
		super.onDestroy()
		bottomSheetDialog.dismiss()
	}
	
	
	
}
