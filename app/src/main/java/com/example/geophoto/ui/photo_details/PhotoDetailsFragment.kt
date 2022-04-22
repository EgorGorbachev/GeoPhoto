package com.example.geophoto.ui.photo_details

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.data.model.pojo.PhotoLatLng
import com.example.domain.model.PhotoLocation
import com.example.geophoto.R
import com.example.geophoto.base.BaseFragment
import com.example.geophoto.databinding.FragmentPhotoDetailsBinding
import com.example.geophoto.databinding.InfoMenuBinding
import com.example.geophoto.delegates.viewBinding
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import com.example.geophoto.R.id.action_photoDetailsFr_to_photoGalleryFr as toPhotoGallery

@AndroidEntryPoint
class PhotoDetailsFragment : BaseFragment(R.layout.fragment_photo_details), View.OnClickListener {

    private val args: PhotoDetailsFragmentArgs by navArgs()
    private val viewModel: PhotoDetailsViewModel by viewModels()
    private val binding: FragmentPhotoDetailsBinding by viewBinding()
    private val bindingBottomShit: InfoMenuBinding by viewBinding()
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheetView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPhotoDetails()
        initBottomSheet()
        initUi()
        initListeners()
    }

    private fun initUi() {
        Glide.with(requireContext()).load(viewModel.photoDetailsPhoto).into(binding.photoDetailsImg)
    }

    private fun initListeners() {
        binding.photoDetailsBackButton.setOnClickListener(this)
        binding.photoDetailsShowInfoButton.setOnClickListener(this)
        bindingBottomShit.photoDetailsEditCommentButton.setOnClickListener(this)
        bindingBottomShit.photoDetailsShowOnMapButton.setOnClickListener(this)
    }

    private fun initPhotoDetails() {
        viewModel.photoDetailsPhoto = args.photoLocation.photo
        viewModel.photoDetailsAddress = args.photoLocation.address
        viewModel.photoDetailsAddress = args.photoLocation.address
        viewModel.photoDetailsComment = args.photoLocation.comment
        viewModel.photoDetailsLatLng =
            LatLng(args.photoLocation.latitude, args.photoLocation.longitude)
    }

    private fun initBottomSheet() {
        bottomSheetDialog = BottomSheetDialog(requireActivity())
        bottomSheetView =
            LayoutInflater.from(requireContext()).inflate(R.layout.info_menu, bindingBottomShit.root)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.photoDetailsBackButton -> navigate(toPhotoGallery)
            R.id.photoDetailsShowInfoButton -> createBottomSheet()
            R.id.photoDetailsEditCommentButton -> showAlertDialog()
            R.id.photoDetailsShowOnMapButton -> navigate(
                PhotoDetailsFragmentDirections.actionPhotoDetailsFrToMapFragment(
                    PhotoLatLng(args.photoLocation.latitude, args.photoLocation.longitude),
                    args.photoLocation
                )
            )
        }
    }

    private fun createBottomSheet() {
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()

        bindingBottomShit.photoDetailsAddress.text = viewModel.photoDetailsAddress
        bindingBottomShit.photoDetailsComment.text = viewModel.photoDetailsComment
    }

    private fun showAlertDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle(getString(R.string.photo_details_your_comment))
        EditText(requireContext()).layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        alertDialog.setView(EditText(requireContext()))

        alertDialog.setPositiveButton(
            getString(R.string.yes)
        ) { _, _ ->
            viewModel.insertDatabase(
                PhotoLocation(
                    viewModel.photoDetailsPhoto!!,
                    viewModel.photoDetailsAddress!!,
                    viewModel.photoDetailsLatLng?.latitude!!,
                    viewModel.photoDetailsLatLng?.longitude!!,
                    EditText(requireContext()).text.toString()
                )
            )
            bindingBottomShit.photoDetailsComment.text = EditText(requireContext()).text.toString()
        }
        alertDialog.show()
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
