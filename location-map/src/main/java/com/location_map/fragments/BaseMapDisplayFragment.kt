package com.location_map.fragments

import android.widget.Toast
import com.location_map.utils.GetLocationHelper

class BaseMapDisplayFragment<M> : BaseMapFragment() {
    override fun prepareMapClicks() {
        mGoogleMap?.setOnMarkerClickListener(this)
        mGoogleMap?.setOnMapClickListener { latLng ->
            val customAddress = GetLocationHelper.getAddress(getContainerActivity(), latLng)
            Toast.makeText(getContainerActivity(), customAddress.customAddress, Toast.LENGTH_LONG)
                .show()
        }
        mGoogleMap?.setOnInfoWindowClickListener { marker -> infoWindowOnClick(marker) }
        findLocation()
    }
}