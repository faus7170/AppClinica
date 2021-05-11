package com.example.appclinica

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.appclinica.notification.FirebaseService
import com.example.appclinica.notification.NotificationData
import com.example.appclinica.notification.PushNotification
import com.example.appclinica.notification.RetrofitInstance
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit

const val TOPIC = "/topics/myTopic2"

class TestNotificationActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    lateinit var button: Button
    lateinit var etTitle: EditText
    lateinit var etContenido: EditText
    lateinit var etToken: EditText
    var retrofit: Retrofit? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_notification)

        /*<service
        android:name=".ui.notificador.MyFirebaseMessaging"
        android:exported="false">
        <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT" />
        </intent-filter>
        </service>*/

        /*<service android:name=".testNotificador.FirebaseService"
            android:permission="com.google.android.c2dm.permission.SEND">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT" />
                <action android:name="com.google.android.c2dm.intent.RECEIVE" />
            </intent-filter>

        </service> */

        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseService.token = it.token
            etToken.setText(it.token)
        }
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        button = findViewById(R.id.btnEnviarNotification)
        etTitle = findViewById(R.id.etTitleTest)
        etContenido = findViewById(R.id.etContenidoTest)
        etToken = findViewById(R.id.etTokenTest)

        //apiservice = getCliente("https://fcm.googleapis.com/").create(APIservice::class.java)


        button.setOnClickListener {
            val title = etTitle.text.toString()
            val message = etContenido.text.toString()
            val recipientToken = etToken.text.toString()
            if(title.isNotEmpty() && message.isNotEmpty() && recipientToken.isNotEmpty()) {
                PushNotification(NotificationData(title, message,"notificacion recivido"), recipientToken).also {
                    sendNotification(it)
                }
                //notidos("test",message,"test",etToken.toString())
            }
        }


    }




    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e(TAG, response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e(TAG, e.toString())
        }
    }
}