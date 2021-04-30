package com.example.appclinica.ui.comunidad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.chat.controlador.MessageAdapter
import com.example.appclinica.ui.chat.modelo.MessageReciver
import com.example.appclinica.ui.chat.modelo.MessageSender
import com.example.appclinica.ui.comunidad.controlador.AdapterComent
import com.example.appclinica.ui.comunidad.controlador.AdapterPreguntas
import com.example.appclinica.ui.comunidad.model.SetPregunt
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ComentActivity : AppCompatActivity() {

    lateinit var adapter: AdapterComent
    lateinit var btnenviar: ImageButton
    lateinit var txtPregunta: TextView
    lateinit var recyclerView: RecyclerView
    lateinit var txtComent:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coment)
        getValores()

        val valor = intent.extras!!.getString("id")
        val valordos = intent.extras!!.getString("pregunta")

        txtPregunta.text = valordos

        val idpe = Firebase.auth.currentUser


        recyclerView.setHasFixedSize(true)

        val linerLinearLayoutManager = LinearLayoutManager(this)
        linerLinearLayoutManager.stackFromEnd = true

        recyclerView.layoutManager = linerLinearLayoutManager

        btnenviar.setOnClickListener {
            sendMessege(valor.toString(),txtComent.text.toString(),idpe.uid)
            txtComent.setText("")
        }

        readMessege(valor.toString())

    }

    fun sendMessege(idpregunta: String, comentario: String,sender:String){

        val database = Firebase.database
        val myRefprueba = database.getReference("preguntas").child(idpregunta).child("comentarios")

        val hashMap: HashMap<String, String> = hashMapOf()
        hashMap.put("idusuario",sender)
        hashMap.put("pregunta", comentario)

        myRefprueba.push().setValue(hashMap)


    }

    fun readMessege(idpregunta: String){

        var mutableList: MutableList<SetPregunt>

        mutableList = mutableListOf()

        val database = Firebase.database
        val myRef = database.getReference("preguntas").child(idpregunta).child("comentarios")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mutableList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue<SetPregunt>()

                    mutableList.add(post!!)

                }

                adapter = AdapterComent(mutableList)
                recyclerView.adapter = adapter

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

    private fun getValores() {
        txtPregunta = findViewById(R.id.viewPreguntaComent)
        btnenviar = findViewById(R.id.btnSendComent)
        recyclerView = findViewById(R.id.recyclerComent)
        txtComent= findViewById(R.id.txtSendComent)
    }
}