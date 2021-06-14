package com.location_map.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.location_map.R
import com.location_map.utils.GetLocationHelper
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseMapControlFragment : BaseMapFragment(), OnMapReadyCallback,
    OnMarkerClickListener, GetLocationHelper.GetLocationHelperCallback {
    var myLocationMarker: Marker? = null
    var gotLocation = false
    protected var getLocationHelper: GetLocationHelper? = null
    var snackbar: Snackbar? = null

    override fun setWork() {
        super.setWork()
        startMapWork()
    }

    override fun prepareMapClicks() {
        mGoogleMap?.setOnMarkerClickListener(this)
        mGoogleMap?.setOnMapClickListener { latLng ->
            val customAddress = GetLocationHelper.getAddress(getContainerActivity(), latLng)
            Toast.makeText(getContainerActivity(), customAddress.customAddress, Toast.LENGTH_LONG)
                .show()
            onCustomMapClick(latLng)
        }
        mGoogleMap?.setOnInfoWindowClickListener { marker -> infoWindowOnClick(marker) }
        findLocation()
    }

    open fun onCustomMapClick(latLng: LatLng?) {}


    override fun onReqeustLocationPermission() {
        permReqLuncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    val permReqLuncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            getLocationHelper?.initMyLocationFunction()
        }
    }

    override fun onBeforeInitMyLocationFunction() {
        try {
            mGoogleMap?.isMyLocationEnabled = true
        } catch (e: Exception) {
            Log.d("Exception ", e.toString())
        }
        getContainerActivity().window.addFlags(128)
    }

    override fun onGpsDisabled() {
        snackbar = Snackbar.make(
            getContainerActivity().findViewById(android.R.id.content),
            getString(R.string.gpsIsDisabled),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(R.string.enable_gps)) {
                val callGPSSettingIntent = Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS
                )
                openActivityForResult(
                    callGPSSettingIntent
                )
            }
            .setActionTextColor( ContextCompat.getColor(
                getContainerActivity(),
                R.color.snack_color
            ))
        snackbar?.show()
    }

    open fun openActivityForResult(intent: Intent) {
        resultLauncher.launch(intent)
    }

    var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val locationManager =
            getContainerActivity().getSystemService(Context.LOCATION_SERVICE)
                    as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocationHelper?.accessLocationFunc()
        }
    }

    override fun onNoInternet() {
        showNoInternet()
    }

    abstract fun showNoInternet()

    fun onLocationChanged(newLocation: Location?) {
        if (newLocation != null) {
            if (!gotLocation) {
                gotLocation = true
                gotBaseLocationChanged(newLocation)
                val latLng = LatLng(newLocation.latitude, newLocation.longitude)
                moveCameraToLocation(latLng)
            }
            gotLocationChanged(newLocation)
        }
    }

    fun moveCameraToLocation(latLng: LatLng) {
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mGoogleMap?.animateCamera(CameraUpdateFactory.zoomTo(15f))

    }

    //place marker at current position
    open fun gotBaseLocationChanged(newLocation: Location) {
        val latLng = LatLng(newLocation.latitude, newLocation.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
        val customAddress = GetLocationHelper.getAddress(getContainerActivity(), latLng)
        markerOptions.title(customAddress.customAddress)
        myLocationMarker?.remove()
        myLocationMarker = mGoogleMap?.addMarker(markerOptions)
    }

    override fun onPause() {
        super.onPause()
        getLocationHelper?.stopLocationUpdates()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNotifyEvent(location: Location?) {
        onLocationChanged(location)
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

}