package com.example.track_location.LocationMap.MapOtherUser

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.track_location.LocationMap.MapBaseFrgVM
import com.example.track_location.data.MapRemote.bean.DirectionResponses
import com.general.data.bean.User
import com.general.firebase.FireBaseManager
import com.general.firebase.FirebaseChildCallBack
import com.google.android.gms.maps.model.Polyline
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MapOtherUserFrgVM(application: Application) : MapBaseFrgVM(application) {
    var otherUser: User? = null
    var polyline: Polyline? = null
    var remoteUserSingleListener: ValueEventListener? = null
    var remoteUserChangeListener: ChildEventListener? = null
    val otherUserLiveSingle: LiveData<User?>
        get() {
            val userSingleMutableLiveData = MutableLiveData<User?>()
            remoteUserSingleListener = fireBaseManager.addRemoteSingleValueListener(
                fireBaseReferences.usersReference, object : FirebaseChildCallBack() {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (singleSnapshot in dataSnapshot.children) {
                            val user = singleSnapshot.getValue(
                                User::class.java
                            )
                            if (user != null) {
                                if (user.user_id == otherUser?.user_id) {
                                    otherUser = user
                                    userSingleMutableLiveData.postValue(user)
                                    break
                                }
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            return userSingleMutableLiveData
        }
    val otherUserLiveChanges: LiveData<User?>
        get() {
            val userChangeMutableLiveData = MutableLiveData<User?>()
            remoteUserChangeListener = fireBaseManager.addRemoteChildListener(
                fireBaseReferences.usersReference,
                object : FirebaseChildCallBack() {
                    override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                        val user = dataSnapshot.getValue(
                            User::class.java
                        )
                        if (user != null) {
                            if (user.user_id == otherUser?.user_id) {
                                user.remoteChildEvent = FireBaseManager.RemoteChildChangedEvent
                                otherUser = user
                                userChangeMutableLiveData.postValue(user)
                            }
                        }
                    }
                })
            return userChangeMutableLiveData
        }

    fun getDirection(
        appCompatActivity: AppCompatActivity,
        origin: String,
        destination: String
    ): LiveData<DirectionResponses> {
        return mapDataManager.getDirection(
            appCompatActivity,
            origin, destination
        )
    }

    override fun removeFirebaseListeners() {
        fireBaseManager.removeRemoteSingleValueListener(
            fireBaseReferences.usersReference,
            remoteUserSingleListener
        )
        fireBaseManager.removeRemoteChildListener(
            fireBaseReferences.usersReference,
            remoteUserChangeListener
        )
    }
}