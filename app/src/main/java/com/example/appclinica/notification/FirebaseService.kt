package com.example.appclinica.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.appclinica.HomeActivity
import com.example.appclinica.R
import com.example.appclinica.ui.chat.ChatRoomActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private const val CHANNEL_ID = "my_channel"

class FirebaseService() : FirebaseMessagingService() {

    companion object {
        var sharedPref: SharedPreferences? = null

        var token: String?
            get() {
                return sharedPref?.getString("token", "")
            }
            set(value) {
                sharedPref?.edit()?.putString("token", value)?.apply()
            }
    }


    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        token = newToken

        updateToken(token.toString())
    }


    fun updateToken(refresToken: String){
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference : DatabaseReference = FirebaseDatabase.getInstance().getReference("Tokens")
        val token = Token(refresToken)
        reference.child(firebaseUser.uid).setValue(token)

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val user: String = message.data.get("iduser")!!
        val idNotificacion = user.replace("[^\\d]".toRegex(), "").toInt()

        //Log.d("testMensajeReciver","numeros de iduser "+j)

        val intent = Intent(this, ChatRoomActivity::class.java)
        val bundle = Bundle()
        bundle.putString("id",user)
        intent.putExtras(bundle)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //val notificationID = Random.nextInt()
        val notificationID = idNotificacion

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(message.data["title"])
                .setContentText(message.data["message"])
                .setSmallIcon(R.drawable.ic_message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

        notificationManager.notify(notificationID, notification)

        /*val user = remoteMessage.data.get("user")
        val icon = remoteMessage.data.get("icono")
        val title = remoteMessage.data.get("title")
        val body = remoteMessage.data.get("body")

        var notificacion : RemoteMessage.Notification = remoteMessage.notification!!

        val j: Int = user!!.replace("[^\\d]", "").toInt()
        val intent = Intent(this,SalaDeChatActivity::class.java)
        val bundle = Bundle()
        bundle.putString("userid",user)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent= PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT)

        val deaultsond:Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val oreoNotification = NotificadorOreo(this)

        val builder : Notification.Builder = oreoNotification.getOreoNotification(title!!, body!!,pendingIntent,deaultsond, icon!!)

        var i = 0
        if (j>0){
            i=j
        }

        oreoNotification.getManager().notify(i,builder.build())*/
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "Mensajes"
        val channel = NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Mensajes"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }

}