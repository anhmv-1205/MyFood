package com.sun_asterisk.myfood.ui.map

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.BaseFragment
import java.io.IOException

class MapsFragment : BaseFragment(), OnMapReadyCallback, OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false
    private lateinit var marker: Marker

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun setUpView() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        context?.let { fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(it) }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                lastLocation = result.lastLocation
                placeMarkerOnMap(LatLng(lastLocation.latitude, lastLocation.longitude))
            }
        }
        createLocationRequest()
    }

    override fun bindView() {
    }

    override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
        map.setOnMarkerClickListener(this)
        setupMap()
    }

    private fun setupMap() {
        context?.let { it ->
            if (ActivityCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                activity?.let { it2 ->
                    ActivityCompat.requestPermissions(
                        it2,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
                return
            }
        }

        map.isMyLocationEnabled = true

        activity?.let {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener(it) { location ->
                if (location != null) {
                    lastLocation = location
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    placeMarkerOnMap(currentLatLng)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM))
                }
            }
        }
    }

    private fun placeMarkerOnMap(location: LatLng) {
        if (this::marker.isInitialized) marker.remove()

        marker = map.addMarker(MarkerOptions().position(location).title(getAddress(location)))
        /** if you want a custom icon as the pin. Let create a mipmap
         *   markerOptions.icon(BitmapDescriptorFactory.fromBitmap(
        BitmapFactory.decodeResource(resources, R.mipmap.ic_user_location)))
         */
    }

    private fun getAddress(latLng: LatLng): String {
        context?.let {
            val geoCoder = Geocoder(it)
            val addresses: List<Address>?
            val address: Address?
            var addressText = ""

            try {
                addresses = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, MAX_RESULTS)

                if (null != addresses && !addresses.run { isEmpty() }) {
                    address = addresses[0]
                    try {
                        addressText += address.getAddressLine(0).toString()
                    } catch (ignored: Exception) {
                    }
                }
            } catch (e: IOException) {
                Log.e(TAG, e.localizedMessage)
            }

            return addressText
        }
        return ""
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()

        locationRequest.interval = INTERVAL

        locationRequest.fastestInterval = FASTEST_INTERVAL
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        activity?.let {
            val client = LocationServices.getSettingsClient(it)
            val task = client.checkLocationSettings(builder.build())

            task.addOnSuccessListener {
                locationUpdateState = true
                startLocationUpdates()
            }
            task.addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        activity?.let { exception.startResolutionForResult(it, REQUEST_CHECK_SETTINGS) }
                    } catch (sendEx: IntentSender.SendIntentException) {
                    }
                }
            }
        }
    }

    private fun startLocationUpdates() {
        context?.let {
            if (ActivityCompat.checkSelfPermission(it, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                activity?.let { it2 ->
                    ActivityCompat.requestPermissions(
                        it2,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
                return
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return false
    }

    companion object {
        private val TAG = MapsFragment::class.java.simpleName
        private const val EXTRA_ID_CATEGORY = "EXTRA_ID_CATEGORY"

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
        private const val PLACE_PICKER_REQUEST = 3
        private const val DEFAULT_ZOOM = 12f
        private const val MAX_RESULTS = 1
        private const val INTERVAL = 10000L
        private const val FASTEST_INTERVAL = 5000L

        fun newInstance(idCategory: String) = MapsFragment().apply {
            val args = Bundle()
            args.putString(EXTRA_ID_CATEGORY, idCategory)
            arguments = args
        }
    }
}
