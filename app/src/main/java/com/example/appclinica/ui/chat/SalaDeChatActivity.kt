package com.example.appclinica.ui.chat

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.chat.controlador.MessageAdapter
import com.example.appclinica.ui.chat.modelo.DatosMensaje
import com.example.appclinica.ui.chat.modelo.MessageReciver
import com.example.appclinica.ui.chat.modelo.MessageSender
import com.example.appclinica.ui.psicologo.ViewPsiocologo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class SalaDeChatActivity : ViewPsiocologo() {

    lateinit var messageAdapter: MessageAdapter
    lateinit var btnenviar: ImageButton
    lateinit var txt_mensaje: TextView
    lateinit var recyclerView: RecyclerView
    lateinit var iduser:String
    lateinit var auth: FirebaseAuth
    lateinit var uid :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sala_de_chat)

        val valor = intent.extras!!.getString("id")

        val idpe = Firebase.auth.currentUser

        uid = idpe.uid

        iduser = valor.toString()


        getValores()

        recyclerView.setHasFixedSize(true)

        val linerLinearLayoutManager = LinearLayoutManager(this)
        linerLinearLayoutManager.stackFromEnd = true

        recyclerView.layoutManager = linerLinearLayoutManager

        btnenviar.setOnClickListener {
            sendMessege(uid,iduser,txt_mensaje.text.toString(), ServerValue.TIMESTAMP)
            txt_mensaje.text = ""
        }

        val name = SalaDeChatActivity::class.simpleName
        activityPerfile(iduser,name.toString())

        readMessege(uid,iduser)

    }

    private fun getValores() {
        txt_mensaje = findViewById(R.id.txtSendMensaje)
        txtNombre = findViewById(R.id.viewNameSalaChat)
        btnenviar = findViewById(R.id.btnSendMensaje)
        recyclerView = findViewById(R.id.recyclerSalaChat)
        imgProfile = findViewById(R.id.imgCircleSalaChat)
    }

    fun sendMessege(sender: String, reciver: String, msm: String, timestamp: MutableMap<String, String>){

        val database = Firebase.database
        val myRefprueba = database.getReference("chats")

        /*val hashMap: HashMap<String,String> = hashMapOf()
        hashMap.put("sender",sender)
        hashMap.put("reciver",reciver)
        hashMap.put("msm",msm)*/

        myRefprueba.push().setValue(MessageSender(sender,reciver,msm,timestamp))

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

        //var mutableList: MutableList<DatosMensaje>
        var mutableList: MutableList<MessageReciver>

        mutableList = mutableListOf()

        val database = Firebase.database
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
}