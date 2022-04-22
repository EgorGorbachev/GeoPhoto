package com.example.data.source.local.data_sources

import android.app.Application
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.data.constants.KEY_SHARED_PREFS_NAME
import com.example.data.util.get
import com.example.data.util.put
import com.example.data.util.remove
import javax.inject.Inject

class SharedPreferencesDataSource @Inject constructor(private val app: Application) {

    private val sharedPrefs by lazy {

        val masterKey = MasterKey.Builder(app)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            app,
            KEY_SHARED_PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun setSharedPref(prefName: String, value:Any) {
        sharedPrefs.put(prefName, value)
    }

    fun getSharedPref(prefName: String): String {
        return sharedPrefs.get(prefName, "")
    }

    fun deleteSharedPref(prefName: String) {
        sharedPrefs.remove(prefName)
    }

}