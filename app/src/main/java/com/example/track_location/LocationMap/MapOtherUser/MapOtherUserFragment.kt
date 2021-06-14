package com.example.track_location.LocationMap.MapOtherUser

import android.graphics.Color
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.track_location.Base.ParentActivity
import com.example.track_location.LocationMap.MapBaseFragment
import com.example.track_location.R
import com.example.track_location.data.MapRemote.bean.DirectionResponses
import com.example.track_location.utils.MyUserData.isMeHasLocation
import com.example.track_location.utils.MyUserData.userLatitude
import com.example.track_location.utils.MyUserData.userLongitude
import com.general.base_act_frg.myapp.BaseParentActivity
import com.general.data.bean.User
import com.general.firebase.FireBaseManager
import com.general.utils.AppFont
import com.general.utils.CustomAlertDialog
import com.general.utils.GeneralUtil
import com.general.utils.MyConstants
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.location_map.utils.GetLocationHelper

class MapOtherUserFragment : MapBaseFragment() {

    override fun initVmWork() {
        frgVM = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(getContainerActivity().application)
        )
            .get(MapOtherUserFrgVM::class.java)
    }

    override fun setWork() {
        super.setWork()
        customAlertDialog = CustomAlertDialog(getContainerActivity())
        (getContainerActivity() as ParentActivity).disableMenuFunction()
        (frgVM as MapOtherUserFrgVM).otherUser =
            arguments?.getSerializable(MyConstants.INPUT_KEY_FRAGMENT) as User
        AppFont.showAppFont(
            getContainerActivity(),
            binding.mapTopBarNote
            , false
        )
        (getContainerActivity() as BaseParentActivity).updateTitle((frgVM as MapOtherUserFrgVM).otherUser?.name)
    }

    override fun onResume() {
        super.onResume()
        (getContainerActivity() as ParentActivity).disableMenuFunction()
        (getContainerActivity() as BaseParentActivity).showBack()
        (getContainerActivity() as BaseParentActivity).updateTitle((frgVM as MapOtherUserFrgVM).otherUser?.name)
    }

    override fun findLocation() {
        super.findLocation()
        frgVM.markerArray.clear()
        (frgVM as MapOtherUserFrgVM).otherUserLiveSingle.observe(viewLifecycleOwner, { user ->
            addUserMarker(user)
            displayUser(user)
        })
        (frgVM as MapOtherUserFrgVM).otherUserLiveChanges.observe(viewLifecycleOwner, { user ->
            if (user?.remoteChildEvent == FireBaseManager.RemoteChildChangedEvent) {
                for (i in frgVM.markerArray.indices.reversed()) {
                    if ((frgVM.markerArray[i]?.tag as User?)?.user_id
                        == user.user_id
                    ) {
                        frgVM.markerArray[i]?.remove()
                        frgVM.markerArray.removeAt(i)
                        addUserMarker(user)
                        displayUser(user)
                        break
                    }
                }
            }
        })
    }

    private fun displayUser(user: User?) {
        if(user!=null)
        {
            (getContainerActivity() as BaseParentActivity).updateTitle((frgVM as MapOtherUserFrgVM).otherUser?.name)
            if (isMeHasLocation) {
                val origin = "$userLatitude,$userLongitude"
                val destination = user.latitude.toString() + "," + user.longitude
                (frgVM as MapOtherUserFrgVM).getDirection(getContainerActivity(), origin, destination)
                    .observe(
                        viewLifecycleOwner,
                        { directionResponses -> drawPolyline(directionResponses) })
            }
        }
    }

    private fun drawPolyline(response: DirectionResponses?) {
        if (response != null) {
            if (response.routes.isNotEmpty()) {
                (frgVM as MapOtherUserFrgVM).polyline?.remove()
                val shape = response.routes[0].overviewPolyline?.points
                (frgVM as MapOtherUserFrgVM).polyline = mGoogleMap?.addPolyline(
                    PolylineOptions().color(
                        Color.MAGENTA
                    ).width(12f)
                )
                (frgVM as MapOtherUserFrgVM).polyline?.points = PolyUtil.decode(shape)
            }
        }
    }

    override fun infoWindowOnClick(marker: Marker) {
        val customAddress = GetLocationHelper.getAddress(getContainerActivity(), marker.position)
        val dateText =
            GeneralUtil.convertDateFromLongToStr((marker.tag as User).locationUpdatedDate)
        Toast.makeText(
            getContainerActivity(), " " + getString(R.string.info_window_clicked) + " "
                    + customAddress.customAddress + " "
                    + (marker.tag as User).name + " " + getString(R.string.locationLastUpdate)
                    + " " + dateText, Toast.LENGTH_LONG
        ).show()
    }
}