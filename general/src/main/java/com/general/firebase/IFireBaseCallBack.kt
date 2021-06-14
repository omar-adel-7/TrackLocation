package com.general.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

interface IFireBaseCallBack {
    fun onDataChange(dataSnapshot: DataSnapshot)
    fun onChildAdded(dataSnapshot: DataSnapshot, s: String?)
    fun onChildChanged(dataSnapshot: DataSnapshot, s: String?)
    fun onChildRemoved(dataSnapshot: DataSnapshot)
    fun onChildMoved(dataSnapshot: DataSnapshot, s: String?)
    fun onCancelled(databaseError: DatabaseError)
}