package com.sun_asterisk.myfood.ui.register

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.R.layout
import com.sun_asterisk.myfood.base.BaseFragment
import com.sun_asterisk.myfood.data.remote.request.RegisterRequest
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.annotation.Role
import com.sun_asterisk.myfood.utils.extension.delayTask
import com.sun_asterisk.myfood.utils.extension.goBackFragment
import com.sun_asterisk.myfood.utils.extension.isEmailValid
import com.sun_asterisk.myfood.utils.extension.isMultiClick
import com.sun_asterisk.myfood.utils.extension.isPhoneNumber
import com.sun_asterisk.myfood.utils.extension.notNull
import com.sun_asterisk.myfood.utils.extension.showAlertDialogBasic
import com.sun_asterisk.myfood.utils.extension.showDatePickerHoloLightDialog
import com.sun_asterisk.myfood.utils.extension.showToast
import com.sun_asterisk.myfood.utils.extension.toDateWithFormat
import com.sun_asterisk.myfood.utils.listener.OnDataCalendarListener
import kotlinx.android.synthetic.main.fragment_register.buttonRegister
import kotlinx.android.synthetic.main.fragment_register.editTextBirthDay
import kotlinx.android.synthetic.main.fragment_register.editTextEmail
import kotlinx.android.synthetic.main.fragment_register.editTextName
import kotlinx.android.synthetic.main.fragment_register.editTextPassword
import kotlinx.android.synthetic.main.fragment_register.editTextPasswordConfirm
import kotlinx.android.synthetic.main.fragment_register.editTextPhoneNumber
import kotlinx.android.synthetic.main.fragment_register.radioGroupRole
import kotlinx.android.synthetic.main.fragment_register.textViewLogin
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : BaseFragment(), OnClickListener, OnDataCalendarListener {

    private val viewModel: RegisterViewModel by viewModel()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallBack: LocationCallback
    private var locationUpdateState = false
    private lateinit var locationRequest: LocationRequest
    private var lastLocation: Location? = null
    private var role = Role.BUYER
    private var birthday = ""

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(layout.fragment_register, container, false)
    }

    override fun setUpView() {
        context?.let { fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(it) }
        locationCallBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                lastLocation = locationResult.lastLocation
            }
        }
        locationRequest = LocationRequest()
        locationRequest.interval = INTERVAL
        locationRequest.fastestInterval = FASTEST_INTERVAL
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        editTextBirthDay.setOnClickListener(this)
        textViewLogin.setOnClickListener(this)
        buttonRegister.setOnClickListener(this)
        radioGroupRole.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButtonRoleBuyer -> {
                    role = Role.BUYER
                    lastLocation = null
                }
                R.id.radioButtonRoleFarmer -> {
                    role = Role.FARMER
                }
            }
        }
        setupPosition()
    }

    override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
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

    override fun bindView() {
    }

    override fun registerLiveData() {
        viewModel.onRegisterEvent.observe(this, Observer {
            it.notNull {
                context?.showAlertDialogBasic(
                    getString(com.sun_asterisk.myfood.R.string.text_register_success),
                    false
                ) {
                    delayTask({ goBackFragment() }, Constant.TIME_DELAY_SHORT)
                }
            }
        })

        viewModel.onMessageErrorEvent.observe(this, Observer {
            it.notNull { msg -> context?.showToast(msg) }
        })
    }

    private fun validateForm(): Boolean {
        if (editTextEmail.text.toString().trim().isEmpty()) {
            editTextEmail.error = getString(com.sun_asterisk.myfood.R.string.text_not_empty)
            editTextEmail.requestFocus()
            return false
        }
        if (!editTextEmail.text.toString().trim().isEmailValid()) {
            editTextEmail.error = getString(com.sun_asterisk.myfood.R.string.text_email_wrong_format)
            editTextEmail.requestFocus()
            return false
        }
        if (editTextName.text.toString().trim().isEmpty()) {
            editTextName.error = getString(com.sun_asterisk.myfood.R.string.text_not_empty)
            editTextName.requestFocus()
            return false
        }
        if (editTextPhoneNumber.text.toString().trim().isEmpty()) {
            editTextPhoneNumber.error = getString(com.sun_asterisk.myfood.R.string.text_not_empty)
            editTextPhoneNumber.requestFocus()
            return false
        }
        if (!editTextPhoneNumber.text.isPhoneNumber()) {
            editTextPhoneNumber.error = getString(com.sun_asterisk.myfood.R.string.text_wrong_phone_number_type)
            editTextPhoneNumber.requestFocus()
            return false
        }
        if (editTextBirthDay.text.toString().trim().isEmpty()) {
            editTextBirthDay.error = getString(com.sun_asterisk.myfood.R.string.text_not_empty)
            editTextBirthDay.requestFocus()
            return false
        }
        if (editTextPassword.text.toString().trim().isEmpty()) {
            editTextPassword.error = getString(com.sun_asterisk.myfood.R.string.text_not_empty)
            editTextPassword.requestFocus()
            return false
        }
        if (editTextPassword.text.toString().trim().length < Constant.PASSWORD_NUMBER_OF_CHARACTER) {
            editTextPassword.error = getString(com.sun_asterisk.myfood.R.string.text_password_rule)
            editTextPassword.requestFocus()
            return false
        }
        if (editTextPasswordConfirm.text.toString().trim().isEmpty()) {
            editTextPasswordConfirm.error = getString(com.sun_asterisk.myfood.R.string.text_not_empty)
            editTextPasswordConfirm.requestFocus()
            return false
        }
        if (editTextPasswordConfirm.text.toString() != editTextPassword.text.toString()) {
            editTextPasswordConfirm.setText("")
            editTextPasswordConfirm.error = getString(com.sun_asterisk.myfood.R.string.text_wrong_password)
            editTextPasswordConfirm.requestFocus()
            return false
        }
        if (role == Role.FARMER && lastLocation == null) {
            context?.showAlertDialogBasic(getString(R.string.text_request_location)) {
                requestLocationPermissions()
            }
            return false
        }
        return true
    }

    private fun requestLocationPermissions() {

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

    private fun setupPosition() {
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

        activity?.let {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener(it) { location ->
                if (location != null) {
                    lastLocation = location
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
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null)
    }

    override fun onClick(v: View?) {
        if (isMultiClick()) return
        when (v?.id) {
            R.id.textViewLogin -> goBackFragment()
            R.id.editTextBirthDay -> {
                context?.showDatePickerHoloLightDialog(listener = this)
            }
            R.id.buttonRegister -> {
                if (validateForm()) {
                    viewModel.register(
                        RegisterRequest(
                            editTextName.text.toString().trim(),
                            editTextEmail.text.toString().trim(),
                            editTextPassword.text.toString().trim(),
                            editTextPhoneNumber.text.toString().trim(),
                            birthday,
                            role,
                            if (role == Role.FARMER) arrayListOf(
                                lastLocation!!.latitude.toFloat(),
                                lastLocation!!.longitude.toFloat()
                            ) else null
                        )
                    )
                }
            }
        }
    }

    override fun onDataSet(dateStr: String) {
        birthday = dateStr
        editTextBirthDay.setText(
            dateStr.toDateWithFormat(
                Constant.DATETIME_FORMAT_YYYY_MM_DD,
                getString(com.sun_asterisk.myfood.R.string.text_time_format)
            )
        )
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val INTERVAL = 10000L
        private const val FASTEST_INTERVAL = 5000L
        private const val REQUEST_CHECK_SETTINGS = 2
    }
}
