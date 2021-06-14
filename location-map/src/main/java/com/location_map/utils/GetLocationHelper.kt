package com.location_map.utils

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.general.utils.BaseUtil.checkInternet
import com.general.utils.BaseUtil.logMessage
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.location_map.model.CustomAddress
import java.io.IOException
import java.util.*

class GetLocationHelper(private val context: Context, private val pendingIntent: PendingIntent
 , private val getLocationHelperCallback : GetLocationHelperCallback ) {
    var fusedLocationClient: FusedLocationProviderClient? = null
    var locationRequest: LocationRequest? = null

    fun accessLocationFunc() {
        if (checkGpsState(context)) {
            accessLocation()
        }
    }

    private fun accessLocation() {
        val version = Build.VERSION.SDK_INT
        if (version >= 23) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                getLocationHelperCallback.onReqeustLocationPermission()
            } else {
                initMyLocationFunction()
            }
        } else {
            initMyLocationFunction()
        }
    }

    fun initMyLocationFunction() {
        getLocationHelperCallback.onBeforeInitMyLocationFunction()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        startLocationClient()
    }

    private fun startLocationClient() {
        try {
            if (locationRequest == null) {
                locationRequest = LocationRequest.create()
                prepareLocationRequest()
                requestLocationUpdates()
            } else {
                requestLocationUpdates()
            }
        } catch (e: Exception) {
            logMessage("exception startLocationClient ", e.toString())
        }
    }

    private fun prepareLocationRequest() {
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest?.fastestInterval = 1000
        locationRequest?.interval = (10 * 1000).toLong()
        locationRequest?.smallestDisplacement = 30f
    }

    private fun requestLocationUpdates() {
        fusedLocationClient?.requestLocationUpdates(locationRequest, pendingIntent)
    }

    fun stopLocationUpdates() {
        fusedLocationClient?.removeLocationUpdates(pendingIntent)
    }

    private fun checkGpsState(context: Context): Boolean {
        var availableGps = true
        if(checkInternet(context))
        {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                availableGps = false
                getLocationHelperCallback.onGpsDisabled()
            }
        } else {
            availableGps = false
            getLocationHelperCallback.onNoInternet()
        }
        return availableGps
    }


    companion object {
        fun getAddress(context: Context?, latLng: LatLng): CustomAddress {
            val customAddress = CustomAddress()
            val gcd = Geocoder(context, Locale.getDefault())
            var addresses: List<Address>? = null
            try {
                addresses = gcd.getFromLocation(
                    latLng.latitude,
                    latLng.longitude, 1
                )
                customAddress.cityName = addresses[0].getAddressLine(0)
                customAddress.stateName = addresses[0].getAddressLine(1)
                customAddress.countryName = addresses[0].getAddressLine(2)
                customAddress.address =
                    addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                customAddress.city = addresses[0].locality
                customAddress.state = addresses[0].adminArea
                customAddress.country = addresses[0].countryName
                customAddress.postalCode = addresses[0].postalCode
                customAddress.knownName =
                    addresses[0].featureName // Only if available else return NULL
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (ofp: IndexOutOfBoundsException) {
                ofp.printStackTrace()
            }
            return customAddress
        }

    }

    interface GetLocationHelperCallback
    {
        fun   onReqeustLocationPermission()
        fun   onBeforeInitMyLocationFunction()
        fun   onGpsDisabled()
        fun   onNoInternet()
    }
}