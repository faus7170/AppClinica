package com.example.appclinica.ui.ejercicio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.ejercicio.controlador.AdapterExercise
import com.example.appclinica.ui.ejercicio.model.TestDatos
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EjercicioActivity : AppCompatActivity() {

    lateinit var adapter: AdapterExercise
    lateinit var mRecyclerView: RecyclerView
    lateinit var userList: MutableList<TestDatos>
    //lateinit var database: FirebaseFirestore
    val database = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio)

        mRecyclerView = findViewById(R.id.recyclerViewEjercicio)
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        //database = Firebase.firestore
        userList = mutableListOf()

        database.collection("ejercicios").get().addOnSuccessListener { document ->
            userList.clear()
            for (getdatos in document) {
                val nombre = getdatos.getString("nombre")
                val descripcion = getdatos.getString("descripcion")
                val id = getdatos.id
                val testDatos = TestDatos(descripcion!!,nombre!!,id)

                userList.add(testDatos)
            }

            adapter = AdapterExercise(userList,{
                val extras = Bundle()
                extras.putString("id", it.id)
                extras.putString("nombre", it.nombre)

                val intent = Intent(this, PasosActivity::class.java)
                intent.putExtras(extras)
                startActivity(intent)

            },true)
            mRecyclerView.adapter = adapter

        }.addOnFailureListener { exception ->

        }
    }
}