package com.example.geophoto.ui.photo_gallery

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.model.PhotoLocation
import com.example.geophoto.R
import com.example.geophoto.base.BaseFragment
import com.example.geophoto.databinding.FragmentPhotoGalleryBinding
import com.example.geophoto.delegates.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import com.example.geophoto.R.id.action_photoGalleryFr_to_photoGalleryFr as toPhotoGallery

@AndroidEntryPoint
class PhotoGalleryFragment : BaseFragment(R.layout.fragment_photo_gallery), PhotoGalleryAdapter.OnClick,
    View.OnClickListener {

    private val binding: FragmentPhotoGalleryBinding by viewBinding()
    private val viewModel: PhotoGalleryViewModel by viewModels()
    private var adapter = PhotoGalleryAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backPressedNavigate(toPhotoGallery)
        initListeners()
        initRecycler()
        showPhotos()
    }

    private fun initListeners() {
        binding.photoGalleryBackButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.photoGalleryBackButton -> navigate(toPhotoGallery)
        }
    }

    private fun initRecycler() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.photoGalleryRecycler)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        val layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.layoutManager = layoutManager
    }

    private fun showPhotos() {
        viewModel.allData.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    override fun onItemClick(photoLocation: PhotoLocation) {
        navigate(PhotoGalleryFragmentDirections.actionPhotoGalleryFrToPhotoDetailsFr(photoLocation))
    }

    override fun onItemLongClick(photoLocation: PhotoLocation) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage(getString(R.string.delete_photo_dialog))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deleteFromDatabase(photoLocation)
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
        val alert = dialogBuilder.create()
        alert.setTitle(getString(R.string.delete_photo_dialog_title))
        alert.show()
    }

    override fun setPhoto(photo: Uri, view: ImageView) {
        Glide.with(requireContext()).load(photo).into(view)
    }
}