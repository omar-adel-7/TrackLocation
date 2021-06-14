package com.example.track_location.firebase

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.track_location.MyApp
import com.example.track_location.R
import com.example.track_location.Splash.Splash
import com.example.track_location.data.DataManager
import com.example.track_location.utils.AppUtil
import com.example.track_location.utils.MyUserData
import com.example.track_location.utils.NotificationUtils
import com.general.utils.BaseUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*
import javax.inject.Inject

class CustomFirebaseMessagingService : FirebaseMessagingService() {
    var mNotificationUtils: NotificationUtils? = null
    var notification_id = 1

    @Inject
    lateinit var dataManager: DataManager
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            mNotificationUtils = NotificationUtils(this)
            if (remoteMessage.notification != null) {
                sendNotification(remoteMessage.notification)
            }
        } catch (e: Exception) {
            BaseUtil.logMessage("ExceptioonMessageReceived ", e.toString())
        }
    }

    private fun sendNotification(notificationRemote: RemoteMessage.Notification?) {

        val intent = Intent(this, Splash::class.java)
        intent.action = System.currentTimeMillis().toString()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val mBuilder = NotificationCompat.Builder(
            this, NotificationUtils.MY_CHANNEL_ID
        )
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        mBuilder.setContentText(notificationRemote?.body)
        mBuilder.setWhen(Calendar.getInstance().timeInMillis)
        mBuilder.setSmallIcon(notificationIcon)
        mBuilder.setSound(null)
        val notification = mBuilder.build()
        notification_id = System.currentTimeMillis().toInt() % 10000
        mNotificationUtils?.manager?.notify(notification_id, notification)
        AppUtil.playNotificationSound(this)
    }

    private val notificationIcon: Int
        private get() = R.drawable.ic_stat_name

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        (application as MyApp).myComponent.inject(this)
        MyUserData.saveUserTokenId(token)
        sendTokenToWebServer()
    }

    fun sendTokenToWebServer() {
        if (MyUserData.isUserLoggedIn) {
            dataManager.updateUserTokenId()
        }
    }

}