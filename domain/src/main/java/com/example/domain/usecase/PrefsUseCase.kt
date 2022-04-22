package com.example.domain.usecase

import com.example.domain.repository.PrefsRepository
import javax.inject.Inject

class PrefsUseCase @Inject constructor(
    private val repository: PrefsRepository
) {
    fun setSharedPref(prefName: String, value: Any) = repository.setSharedPref(prefName, value)
    fun getSharedPref(prefName: String) = repository.getSharedPref(prefName)
    fun deleteSharedPref(prefName: String) = repository.deleteSharedPref(prefName)
}