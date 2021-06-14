package com.location_map.fragments

import android.location.Location
import android.widget.Toast
import com.general.base_act_frg.myapp.BaseFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.location_map.R
import com.location_map.listeners.MapListeners
import com.location_map.utils.GetLocationHelper

abstract class BaseMapFragment : BaseFragment(), OnMapReadyCallback, OnMarkerClickListener,
    MapListeners {
      var mGoogleMap: GoogleMap? = null
    var mapFragment: SupportMapFragment? = null

    override fun initVmWork() {}
    override fun setWork() {

    }

    open fun startMapWork() {
        prepareMapFragFunc()
        mapFragment?.getMapAsync(this)
    }


    open  fun prepareMapFragFunc(){}

    override fun findLocation() {}

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        prepareMap()
    }

    fun prepareMap() {
        mGoogleMap?.clear()
        mGoogleMap?.uiSettings?.isZoomControlsEnabled = true
        mGoogleMap?.isBuildingsEnabled = true
        mGoogleMap?.isIndoorEnabled = true
        mGoogleMap?.isTrafficEnabled = true
        mGoogleMap?.uiSettings?.isRotateGesturesEnabled = false
        prepareMapClicks()
    }

    override fun prepareMapClicks() {}
    override fun gotLocationChanged(newLocation: Location) {}
    override fun onMarkerClick(marker: Marker): Boolean {
        val customAddress = GetLocationHelper.getAddress(getContainerActivity(), marker.position)
        Toast.makeText(
            getContainerActivity(), " " + getString(R.string.marker_clicked) + " "
                    + customAddress.customAddress, Toast.LENGTH_LONG
        ).show()
        return false
    }

    open fun infoWindowOnClick(marker: Marker) {
        val customAddress = GetLocationHelper.getAddress(getContainerActivity(), marker.position)
        Toast.makeText(
            getContainerActivity(),
            " " + getString(R.string.info_window_clicked) + " " + customAddress.customAddress,
            Toast.LENGTH_LONG
        ).show()
    }
}