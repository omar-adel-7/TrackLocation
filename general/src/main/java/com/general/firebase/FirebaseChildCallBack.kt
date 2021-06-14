package com.general.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

open class FirebaseChildCallBack : IFireBaseCallBack
//  ,
//    AFirebaseChildCallBack()
{
    override fun onDataChange(dataSnapshot: DataSnapshot) {}
    override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {}
    override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
    override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
    override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
    override fun onCancelled(databaseError: DatabaseError) {}
}