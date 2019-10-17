package com.phaseII.placepoint

import com.onesignal.OneSignal
import android.app.*
import android.net.Uri
import android.util.Log
import com.onesignal.OSNotification
import com.onesignal.OSNotificationAction
import com.onesignal.OSNotificationOpenResult
import android.content.Intent
import com.phaseII.placepoint.DashBoard.DashBoardActivity
import android.content.Context
import android.app.NotificationManager
import android.media.AudioManager
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import android.app.NotificationChannel
import android.media.AudioAttributes
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import com.google.gson.JsonObject
import org.json.JSONObject


class AppClass : Application(), AppsFlyerConversionListener {

    override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
    }

    override fun onAttributionFailure(p0: String?) {
    }

    override fun onInstallConversionDataLoaded(conversionData: MutableMap<String, String>?) {
        for (attrName: String in conversionData!!.keys) {
            //Log.d(AppsFlyerLib.LOG_TAG, "attribute: " + attrName + " = " + conversionData.get(attrName));
        }
    }

    override fun onInstallConversionFailure(p0: String?) {
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
        OneSignal.enableSound(true);
        val appsFlyerId = AppsFlyerLib.getInstance().getAppsFlyerUID(this)
        AppsFlyerLib.getInstance().init("LocXuyz85AtUXHXPmF7ttT", this, applicationContext)
        AppsFlyerLib.getInstance().startTracking(this)
    }

    internal inner class ExampleNotificationOpenedHandler : OneSignal.NotificationOpenedHandler, OneSignal.NotificationReceivedHandler {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun notificationReceived(notification: OSNotification?) {
            notification!!.androidNotificationId
//            val alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/raw/bikehorn")
//            val inboxStyle = NotificationCompat.InboxStyle()
            //inboxStyle.setBigContentTitle(notificationString)
            var type = "2"
            try {
                var obj: String = notification.payload.additionalData.toString()
                var jsonObject: JSONObject = JSONObject(obj)
                type = jsonObject.optString("type")
            } catch (e: Exception) {

            }
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
            var audio: AudioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if (type == "1" || type == "0") {
                val mNotifyMgr = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

//                    var attributes = AudioAttributes.Builder()
//                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                            .build();

                    // var mChannel = NotificationChannel("uu5io",
                    var mChannel = NotificationChannel("uu5io" + notification!!.androidNotificationId,
                            applicationContext.getString(R.string.app_name),
                            NotificationManager.IMPORTANCE_HIGH)
                    mChannel.setSound(null, null)
                    // mChannel.setSound(sound, attributes); // This is IMPORTANT


                    if (mNotifyMgr != null)
                        mNotifyMgr.createNotificationChannel(mChannel);
                }
                val mBuilder = NotificationCompat.Builder(applicationContext)
                        .setSmallIcon(R.mipmap.ic_launcher)

                        //.setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)
                        //.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/raw/bikehorn"))
                        //  .setSound(Uri.parse("android.resource://" + applicationContext.getPackageName() + "/" + R.raw.bikehorn))
                        //.setChannelId("uu5io")
                        .setChannelId("uu5io" + notification!!.androidNotificationId)
                        .setAutoCancel(true)
                        .setOnlyAlertOnce(false)



                mNotifyMgr.notify(1, mBuilder.build())

            } else {
                when (audio.getRingerMode()) {
                    AudioManager.RINGER_MODE_NORMAL -> {
//                    val mp= MediaPlayer.create(applicationContext, R.raw.bikehorn_old)
//                    mp.start()
                        val sound = Uri.parse("android.resource://" + applicationContext.getPackageName() + "/" + R.raw.bikehorn)
//                    var channel: NotificationChannel? =null
                        val mNotifyMgr = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                            var attributes = AudioAttributes.Builder()
                                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                                    .build();

                            //var mChannel = NotificationChannel("uu5io",
                            var mChannel = NotificationChannel("uu5io" + notification!!.androidNotificationId,
                                    applicationContext.getString(R.string.app_name),
                                    NotificationManager.IMPORTANCE_HIGH)

                            mChannel.setSound(sound, attributes); // This is IMPORTANT


                            if (mNotifyMgr != null)
                                mNotifyMgr.createNotificationChannel(mChannel);
                        }

                        val mBuilder = NotificationCompat.Builder(applicationContext)
                                .setSmallIcon(R.mipmap.ic_launcher)

                                //.setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)
                                //.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/raw/bikehorn"))
                                .setSound(Uri.parse("android.resource://" + applicationContext.getPackageName() + "/" + R.raw.bikehorn))
                                .setChannelId("uu5io" + notification!!.androidNotificationId)
                                .setAutoCancel(true)
                                .setOnlyAlertOnce(false)



                        mNotifyMgr.notify(1, mBuilder.build())


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
//            val actionType = result.action.type
//
//            if (actionType == OSNotificationAction.ActionType.ActionTaken)
//                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID)

            // The following can be used to open an Activity of your choice.
            // Replace - getApplicationContext() - with any Android Context.
            var type = "1"
            try {
                var obj: String = result.notification.payload.additionalData.toString()
                var jsonObject: JSONObject = JSONObject(obj)
                type = jsonObject.optString("type")
            } catch (e: java.lang.Exception) {

            }
            when (type) {
                "3" -> {
                    val intent = Intent(applicationContext, DashBoardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("noti1", true)
                    startActivity(intent)
                }
                "2" -> {
                    val intent = Intent(applicationContext, DashBoardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("noti", true)
                    startActivity(intent)
                }
                else -> {
                    val intent = Intent(applicationContext, DashBoardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("noti2", true)
                    startActivity(intent)
                }
            }

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

