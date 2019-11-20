package com.sun_asterisk.myfood.ui.map

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
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
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.BaseFragment
import com.sun_asterisk.myfood.data.model.User
import com.sun_asterisk.myfood.utils.extension.addDistanceUnits
import com.sun_asterisk.myfood.utils.extension.bitmapDescriptorFromVector
import com.sun_asterisk.myfood.utils.extension.showToast
import kotlinx.android.synthetic.main.layout_custom_farmer_dialog.textViewPost
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException
import kotlin.math.ceil

class MapsFragment : BaseFragment(), OnMapReadyCallback, OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false
    private lateinit var marker: Marker
    private lateinit var myLocation: LatLng
    private var markersFarmer = mutableListOf<Marker>()
    private var farmers = mutableListOf<User>()
    private lateinit var farmer: User
    private val viewModel: MapsViewModel by viewModel()

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
        registerLiveData()
        arguments?.getString(EXTRA_ID_CATEGORY)?.let {
            viewModel.getUserByCategoryId(it)
        }
    }

    private fun registerLiveData() {
        viewModel.onUsersEvent.observe(this, Observer {
            farmers = it
            it.forEach { item ->
                item.location?.let { loc ->
                    val location = LatLng(loc[0].toDouble(), loc[1].toDouble())
                    val marker = map.addMarker(
                        MarkerOptions().position(location).title(getAddress(location)).icon(
                            bitmapDescriptorFromVector(context!!, R.drawable.ic_farmer)
                        )
                    )
                    markersFarmer.add(marker)
                }
            }
        })

        viewModel.onGetNumbersOfFoodByUserId.observe(this, Observer {
            it?.let { showDialog(it) }
        })

        viewModel.onMessageError.observe(this, Observer {
            context?.showToast(it.message.toString())
        })
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

        this.myLocation = location
        marker = map.addMarker(
            MarkerOptions().position(location).title(getAddress(location))
        )

        map.addCircle(
            CircleOptions().center(location).radius(5000.0).strokeWidth(3f).strokeColor(Color.GRAY).fillColor(
                Color.argb(10, 97, 149, 237)
            )
        )
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

    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker == this.marker) return false
        farmer = farmers[markersFarmer.indexOf(marker)]
        viewModel.getNumbersFoodByUserId(farmer.id)
        return false
    }

    private fun showDialog(numbersOfFood: Int) {
        val result = floatArrayOf(1F)
        Location.distanceBetween(
            myLocation.latitude,
            myLocation.longitude,
            farmer.location!![0].toDouble(),
            farmer.location!![1].toDouble(),
            result
        )

        val dialogView = LayoutInflater.from(context!!).inflate(R.layout.layout_custom_farmer_dialog, null)

        val alertDialog = AlertDialog.Builder(context!!).create()

        val textViewName = dialogView.findViewById<TextView>(R.id.textViewName)
        val textViewRating = dialogView.findViewById<TextView>(R.id.textViewRating)
        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
        val textViewDistance = dialogView.findViewById<TextView>(R.id.textViewDistance)
        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancel)
        val buttonDetail = dialogView.findViewById<Button>(R.id.buttonDetail)
        val textViewPost = dialogView.findViewById<TextView>(R.id.textViewPost)

        textViewName.text = farmer.name
        textViewRating.text = "4.5"
        ratingBar.rating = 4.5F
        textViewDistance.text = ceil((result[0]/1000).toDouble()).toString().addDistanceUnits()
        textViewPost.text = numbersOfFood.toString()
        buttonCancel.setOnClickListener { alertDialog.dismiss() }
        buttonDetail.setOnClickListener { context?.showToast("Detail") }

        alertDialog.setView(dialogView)
        alertDialog.show()
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
