package com.example.appclinica.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*

class SalaChatActivity : AppCompatActivity() {

    lateinit var txt_name: TextView
    lateinit var txt_mensaje: TextView
    lateinit var btn_enviar: Button
    lateinit var recyclerView: RecyclerView
    lateinit var adapterChat: AdapterChat
    lateinit var catch: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sala_chat)

        //val user = Firebase.auth.currentUser

        //dato = user.uid



        val database = Firebase.database
        val myRef = database.getReference("psicologo").child("id_psicologo").child("chat_usr").child("id_usr")
        //val myRefusr = database.getReference("usuarios").child("id_usr").child("chat_psico").child("id_psico")
        //val myRef = database.getReference("hora")

        getValores()

        getAndSetRealtime(myRef)

        btn_enviar.setOnClickListener {

            myRef.push().setValue(MensajeEnviar(txt_mensaje.text.toString(), "emisor", ServerValue.TIMESTAMP))
            txt_mensaje.text = ""

        }


    }

    private fun getAndSetRealtime(myRef: DatabaseReference) {

        adapterChat = AdapterChat("s")
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapterChat

        adapterChat.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                setScroll()
                super.onItemRangeInserted(positionStart, itemCount)
            }

        })

        val childEventListener = object : ChildEventListener {

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                //val datosChat: DatosChat = dataSnapshot.getValue(DatosChat::class.java)

                val comment = dataSnapshot.getValue<MensajeRecivir>()
                adapterChat.addMensaje(comment!!)

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        myRef.addChildEventListener(childEventListener)
    }

    private fun getValores() {
        txt_name = findViewById(R.id.txtNombrePsicologo)
        txt_mensaje = findViewById(R.id.txtContenidoMensaje)
        btn_enviar = findViewById(R.id.btnEnviarMensaje)
        recyclerView = findViewById(R.id.idRecyclerChat)
    }

    fun setScroll(){
        recyclerView.scrollToPosition(adapterChat.itemCount - 1)
    }



}