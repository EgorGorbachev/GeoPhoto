package com.example.geophoto.receivers

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings


class GpsLocationReceiver : BroadcastReceiver() {

    var alert: AlertDialog? = null
    var gpsOn = true

    override fun onReceive(context: Context?, intent: Intent?) {
        if (LocationManager.PROVIDERS_CHANGED_ACTION == intent!!.action) {
            val locationManager =
                context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (isGpsEnabled || isNetworkEnabled) {
                gpsOn = true
                alert?.dismiss()
            } else {
                if (gpsOn) {
                    val dialog = AlertDialog.Builder(context)
                    dialog.setMessage("GPS not enabled")
                    dialog.setPositiveButton(
                        "to enable"
                    ) { _, _ -> //this will navigate user to the device location settings screen
                        val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        context.startActivity(myIntent)
                    }
                    alert = dialog.create()
                    alert?.setCanceledOnTouchOutside(false)
                    alert?.show()
                    gpsOn = false
                }

            }
        }
    }
}