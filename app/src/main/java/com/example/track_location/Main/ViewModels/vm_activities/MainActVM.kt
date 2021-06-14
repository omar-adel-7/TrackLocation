package com.example.track_location.Main.ViewModels.vm_activities

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.track_location.Main.ViewModels.BaseViewModel
import com.example.track_location.utils.AppUtil
import com.example.track_location.utils.MyUserData
import com.general.data.bean.User
import com.general.firebase.FirebaseChildCallBack
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot


public class MainActVM(application: Application) : BaseViewModel(application) {


    fun updateUserTokenId() {
        dataManager
                .updateUserTokenId()
    }


    private val userMutableLiveData = MutableLiveData<User?>()
    var remoteUserListener: ChildEventListener? = null

    fun getMyUserDetails(): LiveData<User?> {
        remoteUserListener = fireBaseManager.addRemoteChildListener(
                fireBaseReferences.usersReference,
                object : FirebaseChildCallBack() {
                    override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                        val user = dataSnapshot.getValue(
                                User::class.java
                        )
                        if (user != null) {
                            if (AppUtil.isMyUser(user)) {
                                MyUserData.saveMyUserDetails(user)
                                userMutableLiveData.postValue(user)
                            }

                        }

                    }

                    override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                        val user = dataSnapshot.getValue(
                                User::class.java
                        )
                        if (user != null) {
                            if (AppUtil.isMyUser(user)) {
                                MyUserData.saveMyUserDetails(user)
                                userMutableLiveData.postValue(user)
                            }

                        }
                    }

                    override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    }
                })
        return userMutableLiveData
    }


    fun removeFirebaseListeners() {
        fireBaseManager.removeRemoteChildListener(
                fireBaseReferences.usersReference,
                remoteUserListener
        )
    }

}
