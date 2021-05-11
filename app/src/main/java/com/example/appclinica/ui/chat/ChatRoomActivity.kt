package com.example.appclinica.ui.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.HomeActivity
import com.example.appclinica.R
import com.example.appclinica.notification.NotificationData
import com.example.appclinica.notification.PushNotification
import com.example.appclinica.notification.RetrofitInstance
import com.example.appclinica.notification.Token
import com.example.appclinica.ui.chat.controlador.MessageAdapter
import com.example.appclinica.ui.chat.modelo.MessageReciver
import com.example.appclinica.ui.chat.modelo.MessageSender
import com.example.appclinica.ui.comunidad.ComunidadActivity
import com.example.appclinica.ui.psicologo.ViewPsiocologo
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatRoomActivity : ViewPsiocologo(), View.OnClickListener {

    lateinit var messageAdapter: MessageAdapter
    lateinit var btnenviar: ImageButton
    lateinit var btnVolver:ImageButton
    lateinit var txt_mensaje: EditText
    lateinit var recyclerView: RecyclerView
    lateinit var iduser:String
    lateinit var uid :String
    val database = Firebase.database
    //var notify = false
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sala_de_chat)

        iduser = intent.extras!!.getString("id").toString()

        getfindViewBy()

        //Obtener y mostrar datos del recpetor
        activityPerfile(iduser,"SalaDeChatActivity")


        uid = uidShared()

        readMessege(uid,iduser)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnSendMensaje ->{
                if (!txt_mensaje.text.toString().isEmpty()){
                    sendMessege(uid,iduser,txt_mensaje.text.toString(), ServerValue.TIMESTAMP)
                    txt_mensaje.setText("")
                }
            }R.id.btnVolverSalaChat ->{
                val intent = Intent(this, ChatActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    fun sendMessege(sender: String, reciver: String, msm: String, timestamp: MutableMap<String, String>){

        val myRefprueba = database.getReference("chats")

        myRefprueba.push().setValue(MessageSender(sender,reciver,msm,timestamp))

        contactSender(database)
        contactrReciver(database)

        val msg = msm

        senderNotification(reciver,nombreShared(),msg)
        /*if(notify){
            senderNotification(reciver, nombreShared(),msg)
        }
        notify = false*/



    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                //Log.d(TAG, "Response: ${Gson().toJson(response)}")
                Log.d("sendNotification","Correcto")
            } else {
                //Log.e(TAG, response.errorBody().toString())
                Log.d("sendNotification","Error")
            }
        } catch(e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    fun senderNotification(reciver: String, nombre:String,msm:String){

        /*PushNotification(NotificationData(nombre, msm,reciver),myTokenPhone).also {
            sendNotification(it)
        }*/

        val tokens = database.getReference("Tokens")

        val query:Query = tokens.orderByKey().equalTo(reciver)

        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue<Token>()

                    //Log.d("testKey","key: "+post!!.token)

                    PushNotification(NotificationData(nombre, msm,reciver),post!!.token).also {
                        sendNotification(it)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    private fun contactSender(database: FirebaseDatabase) {
        val myRefpruebalist = database.getReference("chatslist").child(uid).child(iduser)

        myRefpruebalist.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


            override fun onDataChange(snapshot: DataSnapshot) {

                if (!snapshot.exists()) {
                    myRefpruebalist.child("id").setValue(iduser)
                }

            }

        })


    }

    private fun contactrReciver(database: FirebaseDatabase) {
        val myRefpruebalist = database.getReference("chatslist").child(iduser).child(uid)

        myRefpruebalist.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


            override fun onDataChange(snapshot: DataSnapshot) {

                if (!snapshot.exists()) {
                    myRefpruebalist.child("id").setValue(uid)
                }

            }

        })
    }

    fun readMessege(myid:String,userid:String){

        //var mutableList: MutableList<DatosMensaje>
        val mutableList: MutableList<MessageReciver>

        mutableList = mutableListOf()

        //val database = Firebase.database
        val myRef = database.getReference("chats")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mutableList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue<MessageReciver>()

                    if (post!!.reciver.equals(myid) && post!!.sender.equals(userid)||
                        post!!.reciver.equals(userid) && post!!.sender.equals(myid)){

                        mutableList.add(post)
                    }

                    messageAdapter = MessageAdapter(mutableList)
                    recyclerView.adapter = messageAdapter

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

    fun uidShared(): String {
        val pref = applicationContext.getSharedPreferences("dateUser", MODE_PRIVATE)
        return pref.getString("uid", "default")!!
    }

    fun nombreShared(): String {
        val pref = applicationContext.getSharedPreferences("dateUser", MODE_PRIVATE)
        return pref.getString("nombre", "default")!!
    }

    private fun getfindViewBy() {
        txt_mensaje = findViewById(R.id.txtSendMensaje)
        txtNombre = findViewById(R.id.viewNameSalaChat)
        btnenviar = findViewById(R.id.btnSendMensaje)
        recyclerView = findViewById(R.id.recyclerSalaChat)
        imgProfile = findViewById(R.id.imgCircleSalaChat)
        btnVolver = findViewById(R.id.btnVolverSalaChat)
        btnVolver.setOnClickListener(this)
        btnenviar.setOnClickListener(this)

        recyclerView.setHasFixedSize(true)
        val linerLinearLayoutManager = LinearLayoutManager(this)
        linerLinearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linerLinearLayoutManager
    }


}

