package com.example.track_location.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi

class NotificationUtils(base: Context?) : ContextWrapper(base) {
    lateinit var  mManager: NotificationManager
    fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // create my general channel
            createSingleChannel(MY_CHANNEL_ID, MY_CHANNEL_NAME)
            // create my location channel
            createSingleChannel(MY_LOCATION_CHANNEL_ID, MY_LOCATION_CHANNEL_NAME)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createSingleChannel(channelId: String, channelName: String) {
        val notificationChannel = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_DEFAULT
        )
        // Sets whether notifications posted to this channel should display notification lights
        notificationChannel.enableLights(true)
        // Sets whether notification posted to this channel should vibrate.
        notificationChannel.enableVibration(false)
        // Sets the notification light color for notifications posted to this channel
        notificationChannel.lightColor = Color.BLUE
        // Sets whether notifications posted to this channel appear on the lockscreen or not
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        notificationChannel.setSound(null, null)
        manager.createNotificationChannel(notificationChannel)
    }

    val manager: NotificationManager
        get() {
            mManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            return mManager
        }

    companion object {
        const val MY_CHANNEL_ID = "com.example.track_location.my_general_channel"
        const val MY_CHANNEL_NAME = "My General Channel"
        const val MY_LOCATION_CHANNEL_ID = "com.example.track_location.my_location_channel"
        const val MY_LOCATION_CHANNEL_NAME = "My Location Channel"
    }

    init {
        createChannels()
    }
}