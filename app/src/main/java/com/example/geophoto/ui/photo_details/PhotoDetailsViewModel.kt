package com.example.geophoto.ui.photo_details

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.domain.model.PhotoLocation
import com.example.domain.usecase.PhotoDataBaseUseCase
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
    private val useCase: PhotoDataBaseUseCase
) : ViewModel() {

    var photoDetailsAddress: String? = null
    var photoDetailsPhoto: Uri? = null
    var photoDetailsLatLng: LatLng? = null
    var photoDetailsComment: String? = null

    fun insertDatabase(photoLocation: PhotoLocation) {
        CoroutineScope(Dispatchers.Main).launch {
            useCase.add(photoLocation)
        }
    }
}