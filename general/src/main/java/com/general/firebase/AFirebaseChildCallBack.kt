package com.general.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

abstract class AFirebaseChildCallBack {
    abstract fun onDataChange(dataSnapshot: DataSnapshot)
    abstract fun onChildAdded(dataSnapshot: DataSnapshot, s: String?)
    abstract fun onChildChanged(dataSnapshot: DataSnapshot, s: String?)
    abstract fun onChildRemoved(dataSnapshot: DataSnapshot)
    abstract fun onChildMoved(dataSnapshot: DataSnapshot, s: String?)
    abstract fun onCancelled(databaseError: DatabaseError)
}