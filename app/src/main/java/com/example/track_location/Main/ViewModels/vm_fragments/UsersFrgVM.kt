package com.example.track_location.Main.ViewModels.vm_fragments

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.track_location.Main.ViewModels.BaseViewModel
import com.general.data.bean.User
import com.general.firebase.FireBaseManager
import com.general.firebase.FirebaseChildCallBack
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*

class UsersFrgVM(application: Application) : BaseViewModel(application) {
    private val usersList: MutableList<User?> = ArrayList()
    fun getUsersList(): MutableList<User?> {
        return usersList
    }

    var remoteUsersListListener: ValueEventListener? = null
    var remoteUsersChangeListener: ChildEventListener? = null
    val usersLiveList: LiveData<List<User?>>
        get() {
            val usersListMutableLiveData = MutableLiveData<List<User?>>()
            remoteUsersListListener = fireBaseManager.addRemoteSingleValueListener(
                fireBaseReferences.usersReference,
                object : FirebaseChildCallBack() {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        usersList.clear()
                        for (singleSnapshot in dataSnapshot.children) {
                            val user = singleSnapshot.getValue(
                                User::class.java
                            )
                            usersList.add(user)
                        }
                        usersListMutableLiveData.postValue(usersList)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            return usersListMutableLiveData
        }
    val usersLiveChanges: LiveData<User?>
        get() {
            val usersChangeMutableLiveData = MutableLiveData<User?>()
            remoteUsersChangeListener = fireBaseManager.addRemoteChildListener(
                fireBaseReferences.usersReference,
                object : FirebaseChildCallBack() {
                    override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                        val user = dataSnapshot.getValue(
                            User::class.java
                        )
                        if (user != null) {
                            user.remoteChildEvent = FireBaseManager.RemoteChildAddedEvent
                            usersChangeMutableLiveData.postValue(user)
                        }
                    }

                    override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                        val user = dataSnapshot.getValue(
                            User::class.java
                        )
                        if (user != null) {
                            user.remoteChildEvent = FireBaseManager.RemoteChildChangedEvent
                            usersChangeMutableLiveData.postValue(user)
                        }
                    }
                })
            return usersChangeMutableLiveData
        }

    fun removeFirebaseListeners() {
        fireBaseManager.removeRemoteSingleValueListener(
            fireBaseReferences.usersReference,
            remoteUsersListListener
        )
        fireBaseManager.removeRemoteChildListener(
            fireBaseReferences.usersReference,
            remoteUsersChangeListener
        )
    }
}