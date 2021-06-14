package com.example.track_location.utils

import com.general.data.bean.User
import com.general.data.shareddata.Prefs
import com.general.utils.MyConstants

object MyUserData {
    val isUserLoggedIn: Boolean
        get() = if (userId == MyConstants.UserIdNotLogin) {
            false
        } else {
            true
        }
    val userId: String
        get() = Prefs.getString(
            MyConstants.UserId,
            MyConstants.UserIdNotLogin
        )
    val userLongitude: Double
        get() = Prefs.getDouble(MyConstants.UserLongitude, 0.0)
    val userLatitude: Double
        get() = Prefs.getDouble(MyConstants.UserLatitude, 0.0)

    fun saveUserId(userId: String?) {
        Prefs.putStringNow(MyConstants.UserId, userId)
    }

    fun saveUserTokenId(userTokenId: String?) {
        Prefs.putStringNow(MyConstants.UserTokenId, userTokenId)
    }

    fun saveUserName(userName: String?) {
        Prefs.putStringNow(MyConstants.UserName, userName)
    }

    fun saveMyLocation(longitude: Double, latitude: Double, locationUpdatedDate: Long) {
        Prefs.putDoubleNow(MyConstants.UserLongitude, longitude)
        Prefs.putDoubleNow(MyConstants.UserLatitude, latitude)
        Prefs.putLongNow(MyConstants.UserLocationUpdatedDate, locationUpdatedDate)
    }

    val userTokenId: String
        get() = Prefs.getString(MyConstants.UserTokenId)
    val isMeHasLocation: Boolean
        get() =
            Prefs.getDouble(MyConstants.UserLongitude, 0.0) != 0.0 && Prefs.getDouble(
                MyConstants.UserLatitude,
                0.0
            ) != 0.0

    val myUserDetails: User
        get() = User(
            Prefs.getString(
                MyConstants.UserId,
                MyConstants.UserIdNotLogin
            ),
            Prefs.getString(MyConstants.UserEmail),
            Prefs.getString(MyConstants.UserName),
            Prefs.getString(MyConstants.UserTokenId),
            Prefs.getDouble(MyConstants.UserLongitude, 0.0),
            Prefs.getDouble(MyConstants.UserLatitude, 0.0),
            Prefs.getLong(MyConstants.UserLocationUpdatedDate, 0L),
            Prefs.getLong(MyConstants.UserCreatedDate, 0L)

        )

    fun saveMyUserDetails(user: User) {
        Prefs.putStringNow(MyConstants.UserId, user.user_id)
        Prefs.putStringNow(MyConstants.UserEmail, user.email)
        Prefs.putStringNow(MyConstants.UserName, user.name)
        Prefs.putStringNow(MyConstants.UserTokenId, user.token)
        Prefs.putDoubleNow(MyConstants.UserLongitude, user.longitude)
        Prefs.putDoubleNow(MyConstants.UserLatitude, user.latitude)
        Prefs.putLongNow(MyConstants.UserLocationUpdatedDate, user.locationUpdatedDate)
        Prefs.putLongNow(MyConstants.UserCreatedDate, user.createdDate)
    }

    fun logout() {
        Prefs.remove(MyConstants.UserId)
        Prefs.remove(MyConstants.UserEmail)
        Prefs.remove(MyConstants.UserName)
        Prefs.remove(MyConstants.UserTokenId)
        Prefs.remove(MyConstants.UserLongitude)
        Prefs.remove(MyConstants.UserLatitude)
        Prefs.remove(MyConstants.UserLocationUpdatedDate)
        Prefs.remove(MyConstants.UserCreatedDate)
    }
}