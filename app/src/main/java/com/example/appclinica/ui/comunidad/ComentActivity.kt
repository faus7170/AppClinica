package com.example.appclinica.ui.comunidad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appclinica.R
import com.example.appclinica.ui.comunidad.controlador.TestAdapterComunidad
import com.example.appclinica.ui.comunidad.model.SetPregunt
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

class ComentActivity : AppCompatActivity(), TestAdapterComunidad.onClickLister {

    lateinit var adapter: TestAdapterComunidad
    lateinit var btnenviar: ImageButton
    lateinit var txtPregunta: TextView
    lateinit var viewName: TextView
    lateinit var imagenCircleImageView: CircleImageView
    lateinit var recyclerView: RecyclerView
    lateinit var txtComent:EditText
    val database = Firebase.database
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coment)
        getValores()

        val idpublicacion = intent.extras!!.getString("id")
        val pregunta = intent.extras!!.getString("pregunta")
        val nombre = intent.extras!!.getString("nombre")
        val foto = intent.extras!!.getString("foto")

        txtPregunta.text = pregunta
        viewName.text = nombre

        Glide.with(this).load(foto).into(imagenCircleImageView)

        val user = Firebase.auth.currentUser


        btnenviar.setOnClickListener {
            sendComentario(idpublicacion.toString(),txtComent.text.toString(),user.uid)
            txtComent.setText("")
        }

        readComentarios(idpublicacion.toString())

    }

    fun sendComentario(sender: String, question: String,uid:String){

        db.collection("usuarios").document(uid).get().addOnSuccessListener { getdatos ->
            val nombre = getdatos.getString("nombre")
            val foto = getdatos.getString("foto")

            val myRefprueba = database.getReference("publicacion").child(sender).child("comentarios")

            val hashMap: HashMap<String, String> = hashMapOf()
            hashMap.put("sender", sender)
            hashMap.put("pregunta", question)
            hashMap.put("nombre", nombre!!)
            hashMap.put("foto", foto!!)

            myRefprueba.push().setValue(hashMap)


        }.addOnFailureListener { exception ->

        }


    }

    fun readComentarios(idpregunta: String){

        var mutableList: MutableList<SetPregunt> = mutableListOf()

        val myRef = database.getReference("publicacion").child(idpregunta).child("comentarios")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mutableList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue<SetPregunt>()

                    mutableList.add(post!!)

                }

                adapter = TestAdapterComunidad(mutableList,false,this@ComentActivity)
                recyclerView.adapter = adapter

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

    private fun getValores() {
        txtPregunta = findViewById(R.id.viewPreguntaComent)
        viewName = findViewById(R.id.viewNameComent)
        btnenviar = findViewById(R.id.btnSendComent)
        recyclerView = findViewById(R.id.recyclerComent)
        txtComent= findViewById(R.id.txtSendComent)
        imagenCircleImageView = findViewById(R.id.imgCircleComent)

        recyclerView.setHasFixedSize(true)

        val linerLinearLayoutManager = LinearLayoutManager(this)
        linerLinearLayoutManager.stackFromEnd = true

        recyclerView.layoutManager = linerLinearLayoutManager
    }

    override fun onComentar(id: String, nombre: String, pregunta: String, foto: String) {
        TODO("Not yet implemented")
    }

    override fun onBorrar(nombre: String) {
        TODO("Not yet implemented")
    }
}