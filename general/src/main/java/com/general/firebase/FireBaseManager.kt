package com.general.firebase

import com.google.firebase.database.*
import javax.inject.Inject

class FireBaseManager @Inject constructor() {
    fun addRemoteSingleValueListener(
        databaseReference: DatabaseReference,
        firebaseChildCallBack: FirebaseChildCallBack
    ): ValueEventListener {
        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                firebaseChildCallBack.onDataChange(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                firebaseChildCallBack.onCancelled(error)
            }
        }
        databaseReference.addListenerForSingleValueEvent(valueEventListener)
        return valueEventListener
    }

    fun addRemoteChildListener(
        databaseReference: DatabaseReference,
        firebaseChildCallBack: FirebaseChildCallBack
    ): ChildEventListener {
        val childEventListener: ChildEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                firebaseChildCallBack.onChildAdded(snapshot, previousChildName)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                firebaseChildCallBack.onChildChanged(snapshot, previousChildName)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                firebaseChildCallBack.onChildRemoved(snapshot)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                firebaseChildCallBack.onChildMoved(snapshot, previousChildName)
            }

            override fun onCancelled(error: DatabaseError) {
                firebaseChildCallBack.onCancelled(error)
            }
        }
        databaseReference.addChildEventListener(childEventListener)
        return childEventListener
    }

    fun removeRemoteSingleValueListener(
        databaseReference: DatabaseReference,
        valueEventListener: ValueEventListener?
    ) {
        if (valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener)
        }
    }

    fun removeRemoteChildListener(
        databaseReference: DatabaseReference,
        childEventListener: ChildEventListener?
    ) {
        if (childEventListener != null) {
            databaseReference.removeEventListener(childEventListener)
        }
    }

    companion object {
        const val RemoteChildAddedEvent = "RemoteChildAddedEvent"
        const val RemoteChildChangedEvent = "RemoteChildChangedEvent"
        const val RemoteChildRemovedEvent = "RemoteChildRemovedEvent"
    }
}