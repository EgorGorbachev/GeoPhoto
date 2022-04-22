package com.example.geophoto.ui.photo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.data.constants.FASTEST_INTERVAL
import com.example.data.constants.UPDATE_INTERVAL
import com.example.domain.model.PhotoLocation
import com.example.geophoto.R
import com.example.geophoto.base.BaseFragment
import com.example.geophoto.databinding.FragmentPhotoBinding
import com.example.geophoto.delegates.viewBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import com.example.geophoto.R.id.action_galleryFr_to_photoGalleryFr as toPhotoGallery


const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 102

@AndroidEntryPoint
class PhotoFragment : BaseFragment(R.layout.fragment_photo), OnMapReadyCallback,
    View.OnClickListener {

    private val mapViewModel: PhotoViewModel by viewModels()
    private val binding: FragmentPhotoBinding by viewBinding()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap
    private lateinit var currentAddress: Address
    private var lastKnownLocation: Location? = null
    private lateinit var mLocationCallback: LocationCallback
    private var mLocationRequest: LocationRequest? = null
    private lateinit var mCurrentLocation: LatLng

    private lateinit var uri: Uri

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        initListeners()
        getGPSStatus()
        backPressedCloseApp(requireActivity())
        getLocationPermission()
        initClient()
    }

    private fun initClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun initUi() {
        if (mapViewModel.currentPhoto != null && mapViewModel.currentAddress != null) {
            binding.photoHint.isVisible = false
            binding.photoPhotoImageView.setImageURI(mapViewModel.currentPhoto)
            binding.photoLocationInfoText.text = mapViewModel.currentAddress
        }
    }

    private fun initListeners() {
        binding.photoTakePhotoButton.setOnClickListener(this)
        binding.photoToGalleryButton.setOnClickListener(this)
        binding.photoAddCommentButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.photoTakePhotoButton -> takePhoto()
            R.id.photoToGalleryButton -> navigate(toPhotoGallery)
            R.id.photoAddCommentButton -> photoReadyCheck()
        }
    }

    private fun takePhoto() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            toast(R.string.permission_denied_message)
        } else {
            getLocationPermission()
            cameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private fun photoReadyCheck() {
        if (binding.photoPhotoImageView.drawable == null) toast(R.string.photo_first_message)
        else addCommentAlertDialog()
    }


    private fun getGPSStatus() {
        var locationManager: LocationManager? = null
        var gpsEnabled = false
        var networkEnabled = false
        if (locationManager == null) {
            locationManager =
                requireContext().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        }
        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }
        try {
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }
        if (!gpsEnabled && !networkEnabled) {
            val dialog = AlertDialog.Builder(requireContext())
            dialog.setMessage(getString(R.string.photo_GPS_not_enabled_message))
            dialog.setPositiveButton(
                getString(R.string.photo_to_enable_positive_button)
            ) { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            val alert = dialog.create()
            alert?.setCanceledOnTouchOutside(false)
            alert.show()
        }

    }

    private val cameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    externalStoragePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
                !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                    toast(R.string.permission_denied_message)
                }
                else -> {
                    toast(R.string.camera_permission_request_message)
                }
            }
        }

    private val externalStoragePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    openYourActivity()
                }
                !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                    toast(R.string.permission_denied_message)
                }
                else -> {
                    toast(R.string.camera_permission_request_message)
                }
            }
        }


    @SuppressLint("SimpleDateFormat")
    private fun openYourActivity() {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val currentDate = sdf.format(Date())
        val outputDir = requireActivity().getExternalFilesDir(null)
        File.createTempFile(currentDate, "jpg", outputDir);
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val path = File(requireActivity().getExternalFilesDir(null), "$currentDate.jpg")
        uri = FileProvider.getUriForFile(
            requireContext(),
            "com.example.android.supportv4.my_files",
            path
        )
        intent.putExtra((MediaStore.EXTRA_OUTPUT), uri)
        launchSomeActivity.launch(intent)

    }

    private var launchSomeActivity = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            binding.photoPhotoImageView.setImageURI(uri)
            binding.photoHint.isVisible = false
            mapViewModel.currentPhoto = uri
            CoroutineScope(lifecycleScope.coroutineContext).launch {
                mapViewModel.insertDatabase(
                    PhotoLocation(
                        uri,
                        currentAddress.getAddressLine(0),
                        mCurrentLocation.latitude,
                        mCurrentLocation.longitude,
                        getString(R.string.base_comment)
                    )
                )
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
    }

    private fun addCommentAlertDialog() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        alertDialog.setTitle(getString(R.string.photo_dialog_message))
        val input = EditText(requireContext())
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        alertDialog.setView(input)

        alertDialog.setPositiveButton(
            getString(R.string.yes)
        ) { _, _ ->
            CoroutineScope(lifecycleScope.coroutineContext).launch {
                mapViewModel.insertDatabase(
                    PhotoLocation(
                        mapViewModel.currentPhoto!!,
                        currentAddress.getAddressLine(0),
                        mCurrentLocation.latitude,
                        mCurrentLocation.longitude,
                        input.text.toString()
                    )
                )
            }
        }
        alertDialog.show()
    }


    private fun geoLocate() {
        val geocoder = Geocoder(requireContext())
        var list = listOf<Address>()
        try {
            list = geocoder.getFromLocation(
                lastKnownLocation!!.latitude,
                lastKnownLocation!!.longitude,
                1
            )
            mCurrentLocation = LatLng(list[0].latitude, list[0].longitude)

        } catch (e: IOException) {
            Log.e("geocoder", "${e.message}")
        }

        if (list.isNotEmpty()) {
            val address: Address = list[0]
            currentAddress = address
            mapViewModel.currentAddress = address.getAddressLine(0)
            binding.photoLocationInfoText.text = currentAddress.getAddressLine(0)
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            getDeviceLocation()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    private fun getDeviceLocation() {
        try {
            val locationResult = fusedLocationClient.lastLocation
            fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->

                if (location != null) {
                    mCurrentLocation = LatLng(location.latitude, location.longitude)
                }
            }
            locationResult.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    lastKnownLocation = task.result
                    if (task.result == null) {
                        mLocationCallback = object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult?) {
                                locationResult ?: return
                            }
                        }
                        mLocationRequest = LocationRequest.create()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                            .setInterval(UPDATE_INTERVAL)
                            .setFastestInterval(FASTEST_INTERVAL)
                        fusedLocationClient.requestLocationUpdates(
                            mLocationRequest!!,
                            mLocationCallback, Looper.getMainLooper()
                        )
                    }
                    if (lastKnownLocation != null) {
                        geoLocate()
                    }
                } else {
                    Log.e("Exc", "Exception: %s", task.exception)
                }

            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
}