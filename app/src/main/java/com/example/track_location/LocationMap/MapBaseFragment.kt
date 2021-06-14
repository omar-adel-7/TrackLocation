package com.example.track_location.LocationMap

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.track_location.R
import com.example.track_location.databinding.FrgMapBinding
import com.example.track_location.utils.MyUserData
import com.general.data.bean.User
import com.general.utils.BaseUtil
import com.general.utils.CustomAlertDialog
import com.general.utils.GeneralUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.location_map.fragments.BaseMapControlFragment
import com.location_map.utils.GetLocationHelper
import java.util.*

open class MapBaseFragment : BaseMapControlFragment() {
    lateinit var frgVM: MapBaseFrgVM
    var customAlertDialog: CustomAlertDialog? = null
    var _binding: FrgMapBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FrgMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val locationManager =
            getContainerActivity().getSystemService(Context.LOCATION_SERVICE)
                    as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startMapWork()
        }
    }

    override fun prepareMapFragFunc() {
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
    }

    override fun onCustomMapClick(latLng: LatLng?) {
        if (latLng != null) {
            updateMyLocationMarker(latLng)
        }
    }

    override fun findLocation() {
        mGoogleMap?.setInfoWindowAdapter(CustomInfoWindowAdapter(layoutInflater))
        mGoogleMap?.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {}
            override fun onMarkerDrag(marker: Marker) {}
            override fun onMarkerDragEnd(marker: Marker) {
                mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLng(marker.position))
                updateMyLocationMarker(marker.position)
            }
        })
        super.findLocation()
        if (MyUserData.isMeHasLocation) {
            updateMyLocationMarker(LatLng(MyUserData.userLatitude, MyUserData.userLongitude))
        }
        getLocationHelper = GetLocationHelper(getContainerActivity(), pendingIntent , this )
        getLocationHelper?.accessLocationFunc()
    }

    fun addUserMarker(user: User?) {
        if (user != null) {
            val markerOptions = MarkerOptions()
            val latLng = LatLng(user.latitude, user.longitude)
            markerOptions.position(latLng)
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            val customAddress = GetLocationHelper.getAddress(getContainerActivity(), latLng)
            val dateText = GeneralUtil.convertDateFromLongToStr(user.locationUpdatedDate)
            markerOptions.title(
                user.name + " " + getString(R.string.at)
                        + " " + customAddress.customAddress + getString(R.string.and) + " " + getString(
                    R.string.locationLastUpdate
                )
                        + " " + dateText
            )
            val marker = mGoogleMap?.addMarker(markerOptions)
            marker?.tag = user
            frgVM.markerArray.add(marker)
        }
    }

    override fun showNoInternet() {
        Snackbar.make(
            getContainerActivity().findViewById(android.R.id.content),
            getString(R.string.noInternetConnection), Snackbar.LENGTH_LONG
        ).show()
    }

    override fun gotBaseLocationChanged(newLocation: Location) {
        if (isAdded) {
            updateMyLocationMarker(LatLng(newLocation.latitude, newLocation.longitude))
        }
    }

    override fun gotLocationChanged(newLocation: Location) {
        if (isAdded) {
            BaseUtil.logMessage(
                "gotLocationChanged Longitude:",
                "" + newLocation.longitude.toString()
            )
            BaseUtil.logMessage("gotLocationChanged Latitude:", newLocation.latitude.toString())
        }
    }

    fun updateMyLocationMarker(latLng: LatLng) {
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        val customAddress = GetLocationHelper.getAddress(getContainerActivity(), latLng)
        markerOptions.title(
            getString(R.string.you) + " " + getString(R.string.at)
                    + " " + customAddress.customAddress
        )
        myLocationMarker?.remove()
        myLocationMarker = mGoogleMap?.addMarker(markerOptions)
        myLocationMarker?.isDraggable = true
        MyUserData.saveMyLocation(
            latLng.longitude, latLng.latitude, Date().time
        )
        myLocationMarker?.tag = MyUserData.myUserDetails
        if (MyUserData.isUserLoggedIn) {
            frgVM.updateUserLocation(getContainerActivity(), latLng.longitude, latLng.latitude)
                .observe(viewLifecycleOwner, { responseBody ->
                    if (responseBody == null) {
                        customAlertDialog?.alertDialog(getString(R.string.err_in_update_user))
                    }
                })
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val customAddress = GetLocationHelper.getAddress(getContainerActivity(), marker.position)
        val dateText =
            GeneralUtil.convertDateFromLongToStr((marker.tag as User).locationUpdatedDate)
        Toast.makeText(
            getContainerActivity(), " " + getString(R.string.marker_clicked) + " "
                    + customAddress.customAddress + " "
                    + (marker.tag as User).name + " " + getString(R.string.locationLastUpdate)
                    + " " + dateText, Toast.LENGTH_LONG
        ).show()
        return false
    }

    private val pendingIntent: PendingIntent
        private get() {
            val intent =
                Intent(getContainerActivity(), LocationUpdatesBroadcastReceiver::class.java)
            intent.action = LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES
            return PendingIntent.getBroadcast(
                getContainerActivity(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

    override fun onStop() {
        super.onStop()
        frgVM.removeFirebaseListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}