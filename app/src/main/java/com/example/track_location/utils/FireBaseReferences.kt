package com.example.track_location.utils

import com.general.utils.MyConstants
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class FireBaseReferences @Inject constructor() {
    val usersReference: DatabaseReference
    init {
        usersReference = FirebaseDatabase.getInstance().getReference(MyConstants.UsersReference)
    }
}