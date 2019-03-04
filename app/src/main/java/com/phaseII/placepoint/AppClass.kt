package com.phaseII.placepoint

import com.onesignal.OneSignal
import android.R.attr.action
import android.app.*
import android.content.ContentResolver
import android.net.Uri
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
import com.onesignal.OSNotification
import com.onesignal.OSNotificationAction
import org.json.JSONObject
import com.onesignal.OSNotificationOpenResult
import android.support.design.internal.NavigationMenu
import android.content.Intent
import android.media.RingtoneManager
import com.phaseII.placepoint.DashBoard.DashBoardActivity
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.app.NotificationManager
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context.NOTIFICATION_SERVICE
import android.media.AudioManager
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib


class AppClass : Application() ,AppsFlyerConversionListener{
    override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAttributionFailure(p0: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onInstallConversionDataLoaded(conversionData: MutableMap<String, String>?) {
        for ( attrName:String in conversionData!!.keys) {
     Log.d(AppsFlyerLib.LOG_TAG, "attribute: " + attrName + " = " + conversionData.get(attrName));
   }
    }

    override fun onInstallConversionFailure(p0: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
//    override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
//
//
//    }
//
//    override fun onAttributionFailure(p0: String?) {
//
//    }
//
//    override fun onInstallConversionDataLoaded(conversionData: MutableMap<String, String>?) {
//for ( attrName:String in conversionData!!.keys) {
//     Log.d(AppsFlyerLib.LOG_TAG, "attribute: " + attrName + " = " + conversionData.get(attrName));
//   }
//
//    }
//
//    override fun onInstallConversionFailure(p0: String?) {
//    }

    var classtatus: Boolean = false
    override fun onCreate() {
        super.onCreate()
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationOpenedHandler(ExampleNotificationOpenedHandler())
                .setNotificationReceivedHandler(ExampleNotificationOpenedHandler())
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init()
        val appsFlyerId = AppsFlyerLib.getInstance().getAppsFlyerUID(this)
        AppsFlyerLib.getInstance().init("LocXuyz85AtUXHXPmF7ttT", this, applicationContext)
        AppsFlyerLib.getInstance().startTracking(this)
    }

    internal inner class ExampleNotificationOpenedHandler : OneSignal.NotificationOpenedHandler,OneSignal.NotificationReceivedHandler {
        override fun notificationReceived(notification: OSNotification?) {
//            val alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/raw/bikehorn")
//            val inboxStyle = NotificationCompat.InboxStyle()
            //inboxStyle.setBigContentTitle(notificationString)

//            val pendingIntent = PendingIntent.getActivity(applicationContext, 0,
//                    Intent(applicationContext, NavigationMenu::class.java),
//                    0)
//            RingtoneManager.getDefaultUri(R.raw.bikehorn)
//            val notification = NotificationCompat.Builder(applicationContext)
////                    .setSmallIcon(R.drawable.logo_black)
////                    .setContentTitle(notificationString)
////                    .setContentText(notificationString)
////                    .setContentIntent(pendingIntent)
//                    .setAutoCancel(true)
//                    .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/bb"))
//                    .build()

//            val NOTIFICATION_CHANNEL_ID = "my_channel_id_01"
//            var manager:NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                val notificationChannel = NotificationChannel(
//                        NOTIFICATION_CHANNEL_ID, "My app no sound", NotificationManager.IMPORTANCE_LOW
//                )
//
//                //Configure the notification channel, NO SOUND
//                notificationChannel.description = "no sound"
//                notificationChannel.setSound(null, null)
//                notificationChannel.enableLights(false)
//                notificationChannel.enableVibration(false)
//                manager.createNotificationChannel(notificationChannel)
//
//            }

//            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//
//            val CHANNEL_ID = "channel_id"
//
//            // You must create the channel to show the notification on Android 8.0 and higher versions
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                // Set importance to IMPORTANCE_LOW to mute notification sound on Android 8.0 and above
//                val channel = NotificationChannel(CHANNEL_ID, "name", NotificationManager.IMPORTANCE_LOW)
//                notificationManager.createNotificationChannel(channel)
//                channel.setSound(null, null)
//
//            }
//
//            //val mBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
var audio:AudioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

            when (audio.getRingerMode()) {
                AudioManager.RINGER_MODE_NORMAL -> {
                    val mBuilder = NotificationCompat.Builder(applicationContext)
                            .setSmallIcon(R.mipmap.ic_launcher)

                            // .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/raw/bikehorn"))
                            .setSound(Uri.parse("android.resource://" + applicationContext.getPackageName() + "/" + R.raw.bikehorn))
                            .setAutoCancel(true)
                            .setOnlyAlertOnce(false)

                    val mNotifyMgr = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

                    mNotifyMgr.notify(0, mBuilder.build())
                    val mp= MediaPlayer.create(applicationContext, R.raw.bikehorn)
                    mp.start()
                }
                AudioManager.RINGER_MODE_SILENT -> {
                    val mBuilder = NotificationCompat.Builder(applicationContext)
                            .setSmallIcon(R.mipmap.ic_launcher)

                            // .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/raw/bikehorn"))
                            //.setSound(Uri.parse("android.resource://" + applicationContext.getPackageName() + "/" + R.raw.bikehorn))
                            .setAutoCancel(true)
                            .setOnlyAlertOnce(false)

                    val mNotifyMgr = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

                    mNotifyMgr.notify(0, mBuilder.build())
                }
                AudioManager.RINGER_MODE_VIBRATE -> {
                    val mBuilder = NotificationCompat.Builder(applicationContext)
                            .setSmallIcon(R.mipmap.ic_launcher)

                            // .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/raw/bikehorn"))
                            //.setSound(Uri.parse("android.resource://" + applicationContext.getPackageName() + "/" + R.raw.bikehorn))
                            .setAutoCancel(true)
                            .setOnlyAlertOnce(false)

                    val mNotifyMgr = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

                    mNotifyMgr.notify(0, mBuilder.build())
                }
            }



//            notificationManager.notify(
//                    1001, // notification id
//                    mBuilder.build())

//            val builder =  NotificationCompat.Builder(applicationContext)
//
//                    .setAutoCancel(true)
//                    .setDefaults(Notification.FLAG_ONLY_ALERT_ONCE);


//            val notification = Notification()
//            notification.defaults = 0
//            notification.defaults = notification.defaults or Notification.DEFAULT_VIBRATE
//            manager.notify(73195, builder.build())
//           // notification.sound = Uri.parse("android.resource://" + applicationContext.getPackageName() + "/" + R.raw.bikehorn)//Here is FILE_NAME is the name of file that you want to play
//
//            OneSignal.enableSound(false);

            // Vibrate if vibrate is enabled

//           Log.i("noti",notification.toString(),null)
//            val builder = NotificationCompat.Builder(applicationContext)
//                    .setContentTitle("Alertizen")
////                    .setContentText(notification.g)
//                    //                  .setSound(soundUri)
//                    .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/raw/bikehorn"))
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setAutoCancel(true)
//

           }

        // This fires when a notification is opened by tapping on it.
        override fun notificationOpened(result: OSNotificationOpenResult) {
            val actionType = result.action.type
            val data = result.notification.payload.additionalData
            val customKey: String?

            if (data != null) {
                customKey = data.optString("customkey", null)
                if (customKey != null)
                    Log.i("OneSignalExample", "customkey set with value: $customKey")
            }

            if (actionType == OSNotificationAction.ActionType.ActionTaken)
                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID)

            // The following can be used to open an Activity of your choice.
            // Replace - getApplicationContext() - with any Android Context.
             val intent =  Intent(applicationContext, DashBoardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
             startActivity(intent)

            // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
            //   if you are calling startActivity above.
            /*
        <application ...>
          <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
        </application>
     */
        }
    }

}

