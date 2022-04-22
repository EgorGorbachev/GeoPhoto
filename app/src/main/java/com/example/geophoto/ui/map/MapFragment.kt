package com.example.geophoto.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.navArgs
import com.example.geophoto.R
import com.example.geophoto.base.BaseFragment
import com.example.geophoto.base.REQUEST_CODE
import com.example.geophoto.base.ZOOM
import com.example.geophoto.databinding.FragmentMapBinding
import com.example.geophoto.delegates.viewBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : BaseFragment(R.layout.fragment_map), OnMapReadyCallback, View.OnClickListener {

    private val binding: FragmentMapBinding by viewBinding()
    private val args: MapFragmentArgs by navArgs()
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var marker: Marker? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMapFragment()
        initListeners()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        backPressedWithArgsNavigate(MapFragmentDirections.actionMapFragmentToPhotoDetailsFr(args.photoLocation))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val photoLoc = LatLng(args.photoLatLng.lat, args.photoLatLng.lng)
        googleMap.addMarker(MarkerOptions().position(photoLoc).title(getString(R.string.map_photo_location)))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(photoLoc, ZOOM))

        googleMap.uiSettings.isMyLocationButtonEnabled = false
    }

    private fun initListeners() {
        binding.currentLocationButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.currentLocationButton -> getCurrentLoc()
        }
    }

    private fun initMapFragment() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun getCurrentLoc() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
            googleMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->

                if (location != null) {
                    val currentLocation = LatLng(location.latitude, location.longitude)

                    moveCamera(currentLocation)
                    addMarker(currentLocation)
                }
            }
            return
        } else {
            googleMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->

                if (location != null) {
                    val currentLocation = LatLng(location.latitude, location.longitude)
                    moveCamera(currentLocation)
                    addMarker(currentLocation)
                }
            }
        }
    }

    private fun moveCamera(latLng: LatLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM))
    }

    private fun addMarker(currentLocation: LatLng) {
        marker?.remove()
        marker = googleMap.addMarker(
            MarkerOptions().position(currentLocation).title(getString(R.string.map_your_current_location))
        )
    }


}