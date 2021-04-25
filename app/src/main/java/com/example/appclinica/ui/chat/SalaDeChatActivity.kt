package com.example.appclinica.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.chat.controlador.MessageAdapter
import com.example.appclinica.ui.chat.modelo.GetDatosMensaje
import com.example.appclinica.ui.chat.modelo.GetDatosUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class SalaDeChatActivity : AppCompatActivity() {

    lateinit var messageAdapter: MessageAdapter
    lateinit var btnenviar: ImageButton
    lateinit var txt_mensaje: TextView
    lateinit var txtName: TextView
    lateinit var recyclerView: RecyclerView
    lateinit var iduser:String
    lateinit var auth: FirebaseAuth
    lateinit var uid :String
    lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sala_de_chat)

        val valor = intent.extras!!.getString("id")

        val idpe = Firebase.auth.currentUser

        uid = idpe.uid

        iduser = valor.toString()


        txt_mensaje = findViewById(R.id.txtSendMensaje)
        txtName = findViewById(R.id.viewNameSalaChat)
        btnenviar = findViewById(R.id.btnSendMensaje)
        recyclerView = findViewById(R.id.recyclerSalaChat)

        recyclerView.setHasFixedSize(true)


        val linerLinearLayoutManager = LinearLayoutManager(this)
        linerLinearLayoutManager.stackFromEnd = true

        recyclerView.layoutManager = linerLinearLayoutManager

        btnenviar.setOnClickListener {
            sendMessege(uid,iduser,txt_mensaje.text.toString())
            txt_mensaje.text = ""
        }

        val database = Firebase.database
        val myRef = database.getReference("user").child(valor.toString())

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<GetDatosUser>()

                txtName.text = post!!.nombre

                readMessege(uid,iduser)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

    fun sendMessege(sender:String,reciver:String,msm:String){

        val database = Firebase.database
        val myRefprueba = database.getReference("chats")

        val hashMap: HashMap<String,String> = hashMapOf()
        hashMap.put("sender",sender)
        hashMap.put("reciver",reciver)
        hashMap.put("msm",msm)

        myRefprueba.push().setValue(hashMap)

        contactSender(database)
        contactrReciver(database)

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

        var mutableList: MutableList<GetDatosMensaje>

        mutableList = mutableListOf()

        val database = Firebase.database
        val myRef = database.getReference("chats")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mutableList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue<GetDatosMensaje>()

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
}