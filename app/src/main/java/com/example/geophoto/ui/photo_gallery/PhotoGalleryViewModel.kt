package com.example.geophoto.ui.photo_gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.domain.model.PhotoLocation
import com.example.domain.usecase.PhotoDataBaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoGalleryViewModel @Inject constructor(
    private val useCase: PhotoDataBaseUseCase
) : ViewModel() {

    val allData: LiveData<List<PhotoLocation>> = useCase.allDataPhoto

    fun deleteFromDatabase(photoLocation: PhotoLocation) {
        CoroutineScope(Dispatchers.Main).launch {
            useCase.delete(photoLocation)
        }
    }
}