package com.example.geophoto.presentation.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.geophoto.R
import com.example.geophoto.presentation.base.BaseFragment
import com.example.geophoto.viewModels.PhotoDetailsViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_map.*


@AndroidEntryPoint
class MapFr : BaseFragment(R.layout.fragment_map), OnMapReadyCallback {
	
	private val args by navArgs<MapFrArgs>()
	
	private lateinit var googleMap: GoogleMap
	
	private lateinit var fusedLocationClient: FusedLocationProviderClient
	
	private val REQUEST_CODE: Int = 1
	
	private var marker: Marker? = null
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
		mapFragment?.getMapAsync(this)
		
		fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
		
		currentLocationBtn.setOnClickListener {
			getCurrentLoc()
		}
	}
	
	override fun onMapReady(p0: GoogleMap) {
		googleMap = p0
		
		val photoLoc = LatLng(args.photoLatLng.lat, args.photoLatLng.lng)
		googleMap.addMarker(MarkerOptions().position(photoLoc).title("Photo Location"))
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(photoLoc, 15f))
		
		googleMap.uiSettings.isMyLocationButtonEnabled = false

	}
	
	private fun getCurrentLoc() {
		if (ActivityCompat.checkSelfPermission(
				requireContext(),
				Manifest.permission.ACCESS_FINE_LOCATION
			) != PackageManager.PERMISSION_GRANTED
		) {
			ActivityCompat.requestPermissions(
				requireActivity(),
				arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
				REQUEST_CODE
			)
			googleMap.isMyLocationEnabled = true
			fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
				
				if (location != null) {
					val currentLocation = LatLng(location.latitude, location.longitude)
					
					moveCamera(currentLocation, 15f)
					addMarker(currentLocation, "Your current location")
				}
			}
			return
		} else {
			googleMap.isMyLocationEnabled = true
			fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
				
				if (location != null) {
					val currentLocation = LatLng(location.latitude, location.longitude)
					
					moveCamera(currentLocation, 15f)
					addMarker(currentLocation, "Your current location")
				}
			}
		}
	}
	
	private fun moveCamera(latLng: LatLng, zoom: Float) {
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
	}
	
	private fun addMarker(currentLocation: LatLng, title: String) {
		marker?.remove()
		marker = googleMap.addMarker(
			MarkerOptions().position(currentLocation).title(title)
		)
	}
}