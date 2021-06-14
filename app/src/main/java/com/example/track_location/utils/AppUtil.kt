package com.example.track_location.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.fragment.app.Fragment
import com.example.track_location.Main.MainActivity
import com.example.track_location.R
import com.example.track_location.Splash.Splash
import com.general.data.bean.User
import com.general.utils.GeneralUtil
import com.google.android.gms.maps.model.LatLng
import com.location_map.utils.GetLocationHelper

object AppUtil {
    fun logOut(context: Context) {
        MyUserData.logout()
        (context as MainActivity).finishAffinity()
        val myIntent = Intent(context, Splash::class.java)
        context.startActivity(myIntent)
    }

    fun restartApplication(context: Context) {
        (context as MainActivity).finishAffinity()
        val i = Intent(context, MainActivity::class.java)
        context.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    fun isCurrentFragment(context: Context?, fragmentName: String): Boolean {
        val currentFragment: Fragment? =
            (GeneralUtil.getRealActivity(context) as MainActivity).getCurrentFragment()
        if (currentFragment != null) {
            if (fragmentName == currentFragment.javaClass.name) {
                return true
            }
        }
        return false
    }

    fun isCurrentFragment(context: Context?, fragment: Fragment): Boolean {
        val currentFragment: Fragment? =
            (GeneralUtil.getRealActivity(context) as MainActivity).getCurrentFragment()
        if (currentFragment != null) {
            if (fragment.javaClass.name == currentFragment.javaClass.name) {
                return true
            }
        }
        return false
    }

    fun playNotificationSound(context: Context) {
        val notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        RingtoneManager.getRingtone(context, notificationSoundUri).play()
    }

    fun isMyUser(otherUser: User?): Boolean {
        if (MyUserData.isUserLoggedIn) {
            return MyUserData.userId == otherUser?.user_id
        }
        return false
    }

    fun sendLocationNotification(context: Context, newLocation: Location) {
        val mNotificationUtils = NotificationUtils(context)
        val latLng = LatLng(newLocation.latitude, newLocation.longitude)
        val customAddress = GetLocationHelper.getAddress(context, latLng)
        // Create an explicit content Intent that starts the main Activity.
        val notificationIntent = Intent(context, MainActivity::class.java)
        // Construct a task stack.
        val stackBuilder = TaskStackBuilder.create(
            context
        )
        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity::class.java)
        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent)
        // Get a PendingIntent containing the entire back stack.
        val notificationPendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        // Get a notification builder that's compatible with platform versions >= 4
        val builder = NotificationCompat.Builder(context, NotificationUtils.MY_LOCATION_CHANNEL_ID)
        // Define the notification settings.
        builder.setSmallIcon(R.mipmap.ic_launcher) // In a real app, you may want to use a library like Volley
            // to decode the Bitmap.
            .setSmallIcon(R.mipmap.ic_launcher)
            .setColor(Color.BLUE)
            .setContentTitle("Location update")
            .setContentText(customAddress.customAddress)
            .setContentIntent(notificationPendingIntent)
        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true)
        // Get an instance of the Notification manager
        // Issue the notification
        mNotificationUtils.manager.notify(0, builder.build())
    }
}