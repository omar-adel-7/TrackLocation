package com.example.track_location.LocationMap.MapUsers

import androidx.lifecycle.ViewModelProvider
import com.example.track_location.Base.ParentActivity
import com.example.track_location.LocationMap.MapBaseFragment
import com.example.track_location.Main.MainActivity
import com.example.track_location.R
import com.example.track_location.utils.AppUtil.isMyUser
import com.general.base_act_frg.myapp.BaseParentActivity
import com.general.data.bean.User
import com.general.firebase.FireBaseManager
import com.general.utils.AppFont
import com.general.utils.CustomAlertDialog
import com.google.android.gms.maps.model.Marker

class MapUsersFragment : MapBaseFragment() {
    override fun setWork() {
        super.setWork()
        customAlertDialog = CustomAlertDialog(getContainerActivity())
        frgVM = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(getContainerActivity().application)
        )
            .get(MapUsersFrgVM::class.java)
         AppFont.showAppFont(
            getContainerActivity(),
                binding.mapTopBarNote
            , false
        )
    }

    override fun onResume() {
        super.onResume()
        (getContainerActivity() as ParentActivity).showDefaultCustomTitle()
        (getContainerActivity() as BaseParentActivity).updateTitle(getString(R.string.menu_map_users))
    }

    override fun findLocation() {
        super.findLocation()
        frgVM.markerArray.clear()
        (frgVM as MapUsersFrgVM).usersLiveList.observe(viewLifecycleOwner, { users ->
            for (i in users.indices) {
                addUserMarker(users[i])
            }
        })
        (frgVM as MapUsersFrgVM).usersLiveChanges.observe(viewLifecycleOwner, { user ->
            if (user?.remoteChildEvent == FireBaseManager.RemoteChildAddedEvent) {
                var found = false
                for (i in frgVM.markerArray.indices) {
                    if ((frgVM.markerArray[i]
                            ?.tag as User?)
                            ?.user_id
                        == user.user_id
                    ) {
                        found = true
                        break
                    }
                }
                if (!found) {
                    addUserMarker(user)
                }
            } else if (user?.remoteChildEvent == FireBaseManager.RemoteChildChangedEvent) {
                for (i in frgVM.markerArray.indices.reversed()) {
                    if ((frgVM.markerArray[i]?.tag as User?)?.user_id
                        == user.user_id
                    ) {
                        frgVM.markerArray[i]?.remove()
                        frgVM.markerArray.removeAt(i)
                        addUserMarker(user)
                        break
                    }
                }
            }
        })
    }

    override fun infoWindowOnClick(marker: Marker) {
        //    CustomAddress customAddress = GetLocationHelper.getAddress(getContainerActivity(), marker.getPosition());
        //  String dateText = GeneralUtil.convertDateFromLongToStr(((User) marker.getTag()).getLocationUpdatedDate());
//        Toast.makeText(getContainerActivity(), " " + getString(R.string.info_window_clicked) + " "
//                + customAddress.getCustomAddress() + " "
//                + (((User) marker.getTag()).getName()) + " " + getString(R.string.locationLastUpdate)
//                + " " + dateText, Toast.LENGTH_LONG).show();
        val user = marker.tag as User?
        if (!isMyUser(user)) {
            if (user != null) {
                (getContainerActivity() as MainActivity).openOtherUserFragment(user)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}