package com.example.geophoto.ui.photo

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.domain.model.PhotoLocation
import com.example.domain.usecase.PhotoDataBaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val useCase: PhotoDataBaseUseCase
) : ViewModel() {

    var currentAddress: String? = null
    var currentPhoto: Uri? = null

    suspend fun insertDatabase(photoLocation: PhotoLocation) {
        useCase.add(photoLocation)
    }
}