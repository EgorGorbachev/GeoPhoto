package com.example.geophoto.presentation.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.geophoto.R
import com.example.geophoto.models.PhotoLocation
import com.example.geophoto.presentation.base.BaseFragment
import com.example.geophoto.viewModels.PhotoViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_photo.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 102

@AndroidEntryPoint
class GalleryFr : BaseFragment(R.layout.fragment_photo), OnMapReadyCallback {
	
	private val mapViewModel by viewModels<PhotoViewModel>()
	
	private lateinit var fusedLocationClient: FusedLocationProviderClient
	
	private lateinit var googleMap: GoogleMap
	
	private lateinit var currentAddress: Address
	
	private var lastKnownLocation: Location? = null
	
	private var locationPermissionGranted = false
	
	private lateinit var mLocationCallback: LocationCallback
	private var mLocationRequest: LocationRequest? = null
	private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
	private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
	
	private lateinit var mCurrentLocation: LatLng
	
	private lateinit var baseComment: String
	
	private lateinit var uri: Uri
	
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
		
		baseComment = resources.getString(R.string.baseComment)
		
		takePhotoBtn.setOnClickListener {
			if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
				toast("Permission denied. You can give permission in settings")
			} else {
				getLocationPermission()
				cameraPermission.launch(Manifest.permission.CAMERA)
			}
		}
		
		if (mapViewModel.currentPhoto != null && mapViewModel.currentAddress != null) {
			hintPhoto.isVisible = false
			photo.setImageURI(mapViewModel.currentPhoto)
			location.text = mapViewModel.currentAddress
		}
		
		toGalleryBtn.setOnClickListener {
			Navigation.findNavController(requireView())
				.navigate(R.id.action_galleryFr_to_photoGalleryFr)
		}
		
		addCommentBtn.setOnClickListener {
			if (photo.drawable == null) {
				toast("Take a photo first!")
			} else {
				addCommentAlertDialog()
			}
			Log.e("lol", "")
		}
	}
	
	private val cameraPermission =
		registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
			when {
				granted -> {
					// user granted permission
					externalStoragePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
				}
				!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
					// user denied permission and set Don't ask again.
					toast("Permission denied. You can give permission in settings")
				}
				else -> {
					toast("For using camera app need permission")
				}
			}
		}
	
	private val externalStoragePermission =
		registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
			when {
				granted -> {
					// user granted permission
					openYourActivity()
				}
				!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
					// user denied permission and set Don't ask again.
					toast("Permission denied. You can give permission in settings")
				}
				else -> {
					toast("For using camera app need permission")
				}
			}
		}
	
	
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
	
	var launchSomeActivity = registerForActivityResult(StartActivityForResult()) { result ->
		if (result.resultCode == Activity.RESULT_OK) {
			toast("lol")
			photo.setImageURI(uri)
			hintPhoto.isVisible = false
			mapViewModel.currentPhoto = uri
			insertDatabase(
				uri,
				currentAddress.getAddressLine(0),
				mCurrentLocation.latitude,
				mCurrentLocation.longitude,
				getString(R.string.baseComment)
			)
		}
	}
	
	override fun onMapReady(p0: GoogleMap) {
		googleMap = p0
	}
	
	private fun addCommentAlertDialog() {
		val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
		alertDialog.setTitle("Your comment")
		val input = EditText(requireContext())
		val lp = LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT,
			LinearLayout.LayoutParams.MATCH_PARENT
		)
		input.layoutParams = lp
		alertDialog.setView(input)
		
		alertDialog.setPositiveButton(
			"YES"
		) { dialog, which ->
			insertDatabase(
				mapViewModel.currentPhoto!!,
				currentAddress.getAddressLine(0),
				mCurrentLocation.latitude,
				mCurrentLocation.longitude,
				input.text.toString()
			)
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
			location.text = currentAddress.getAddressLine(0)
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
	
	private fun insertDatabase(
		photo: Uri,
		address: String,
		latitude: Double,
		longitude: Double,
		comment: String
	) {
		val photoLocation = PhotoLocation(photo, address, latitude, longitude, comment)
		mapViewModel.insertDatabase(photoLocation)
	}
	
}

//	private val cameraShot =
//		registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
//			mapViewModel.currentPhoto = bitmap
//			if (bitmap != null) {
//				hintPhoto.isVisible = false
//				photo.setImageBitmap(bitmap)
//				getDeviceLocation()
//				GlobalScope.launch(Dispatchers.Main) {
//					delay(3000)
//					Log.e(
//						"kek",
//						mCurrentLocation.latitude.toString() + " ===  " + mCurrentLocation.longitude
//					)
//					insertDatabase(
//						bitmap,
//						currentAddress.getAddressLine(0),
//						mCurrentLocation.latitude,
//						mCurrentLocation.longitude,
//						baseComment
//					)
//				}
//			} else {
//				// something was wrong
//				toast("Something wrong")
//			}
//		}

//	private fun getPhotoPath() {
//		val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//		var photoFile: File? = null
//		try {
//			photoFile = createImageFile();
//		} catch (ex: IOException) {
//			// Error occurred while creating the File
//			Log.d("mylog", "Exception while creating file: $ex");
//		}
//		// Continue only if the File was successfully created
//		if (photoFile != null) {
//			Log.d("mylog", "Photofile not null");
//			val photoURI: Uri = FileProvider.getUriForFile(
//				requireContext(),
//				"com.vysh.fullsizeimage.fileprovider",
//				photoFile
//			);
//			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//		}
//	}
//
//	@SuppressLint("SimpleDateFormat")
//	@Throws(IOException::class)
//	private fun createImageFile(): File? {
//		// Create an image file name
//		val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//		val imageFileName = "JPEG_" + timeStamp + "_"
//		val storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//		val image = File.createTempFile(
//			imageFileName,  /* prefix */
//			".jpg",  /* suffix */
//			storageDir /* directory */
//		)
//		// Save a file: path for use with ACTION_VIEW intents
//		val mCurrentPhotoPath = image.absolutePath
//		Log.d("mylog", "Path: $mCurrentPhotoPath")
//		return image
//	}
//
//	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//			setPic()
//		} else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
//			val uri = data.data
//			try {
//				val bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri)
//				ivGallery.setImageBitmap(bitmap)
//			} catch (e: IOException) {
//				e.printStackTrace()
//			}
//		}
//	}