package com.example.domain.repository

interface PrefsRepository {
    fun setSharedPref(prefName: String, value: Any)
    fun getSharedPref(prefName: String): Any
    fun deleteSharedPref(prefName: String)
}