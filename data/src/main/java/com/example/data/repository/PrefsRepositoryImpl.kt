package com.example.data.repository

import com.example.data.source.local.data_sources.SharedPreferencesDataSource
import com.example.domain.repository.PrefsRepository
import javax.inject.Inject

class PrefsRepositoryImpl @Inject constructor(
    private val sharedPref: SharedPreferencesDataSource
) : PrefsRepository {
    override fun setSharedPref(prefName: String, value: Any) =
        sharedPref.setSharedPref(prefName, value)

    override fun getSharedPref(prefName: String): String = sharedPref.getSharedPref(prefName)

    override fun deleteSharedPref(prefName: String) = sharedPref.deleteSharedPref(prefName)
}