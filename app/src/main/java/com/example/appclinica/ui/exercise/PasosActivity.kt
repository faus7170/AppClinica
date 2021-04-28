package com.example.appclinica.ui.exercise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.exercise.controlador.AdapterPasosExercise
import com.example.appclinica.ui.exercise.model.GetDatosPasosExercise
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PasosActivity : AppCompatActivity() {

    lateinit var adapter: AdapterPasosExercise
    lateinit var mRecyclerView: RecyclerView
    lateinit var userList: MutableList<GetDatosPasosExercise>
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pasos)

        val bundle = intent.extras
        val dato = bundle?.getString("id")
        val nombre = bundle?.getString("nombre")

        mRecyclerView = findViewById(R.id.recyclerViewPasos)
        textView = findViewById(R.id.textViewNamePaso)

        textView.text = nombre

        mRecyclerView.layoutManager = LinearLayoutManager(this)

        getDatosFirestore(dato)

    }

    private fun getDatosFirestore(dato: String?) {
        val db = Firebase.firestore
        userList = mutableListOf()

        db.collection("ejercicios").document(dato.toString()).collection("pasos").get().addOnSuccessListener { document ->
            for (getdatos in document) {

                val ident = getdatos.getString("identificador")
                val contenido = getdatos.getString("contenido")
                val id = getdatos.id

                val getDatos = GetDatosPasosExercise(ident!!, contenido!!)

                userList.add(getDatos)

            }
            adapter = AdapterPasosExercise(userList) {
                val intent = Intent(this, MultimediaActivity::class.java)
                intent.putExtra("url", it.contenido)
                startActivity(intent)
            }
            mRecyclerView.adapter = adapter
        }.addOnFailureListener { exception ->

        }
    }

}