package com.example.appclinica.ui.exercise

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.appclinica.HomeActivity
import com.example.appclinica.R
import com.example.appclinica.ui.exercise.controlador.AdapterExercise
import com.example.appclinica.ui.exercise.model.Exercise
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 *@author David Aguinsaca
 *Activity principal de ejerccios recupera datos de la firebase, ademas contiene un buscador
 *
 **/
class ExerciseActivity : AppCompatActivity() {

    lateinit var adapter: AdapterExercise
    lateinit var mRecyclerView: RecyclerView
    lateinit var userList: MutableList<Exercise>
    lateinit var userListSearches: MutableList<Exercise>
    lateinit var search:SearchView
    lateinit var imageButton: ImageButton
    //lateinit var database: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio)

        conexionFirestore()

        search = findViewById(R.id.searchViewEjerc)
        mRecyclerView = findViewById(R.id.recyclerViewEjercicio)
        imageButton = findViewById(R.id.btnVolverExercise)
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        imageButton.setOnClickListener {
            finish()
        }
        //database = Firebase.firestore


        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(qString: String): Boolean {
                buscar(qString)
                return true
            }

            override fun onQueryTextSubmit(qString: String): Boolean {
                return false
            }
        })


    }

    //Buscar un ejerccio en la base de datos
    private fun buscar(s: String){

        userListSearches = mutableListOf()

        for (test:Exercise in userList){
            //userListSearch.clear()
            if (test.nombre.toLowerCase().contains(s.toLowerCase())){
                val testDatos = Exercise(test.descripcion, test.nombre, test.id)
                userListSearches.add(testDatos)
            }
        }

        adapter(userListSearches)


    }

    //Recuperar los ejerccicios de firestore
    private fun conexionFirestore() {
        val database = Firebase.firestore

        userList = mutableListOf()

        database.collection("ejercicios").get().addOnSuccessListener { document ->
            userList.clear()
            for (getdatos in document) {
                val nombre = getdatos.getString("nombre")
                val descripcion = getdatos.getString("descripcion")
                val id = getdatos.id
                val testDatos = Exercise(descripcion!!, nombre!!, id)
                userList.add(testDatos)
            }
            adapter(userList)
        }.addOnFailureListener { exception ->

        }
    }

    //Mostrar los ejerccicios en el activty
    private fun adapter(muteable:MutableList<Exercise>) {
        adapter = AdapterExercise(muteable, {
            val extras = Bundle()
            extras.putString("id", it.id)
            extras.putString("nombre", it.nombre)
            //extras.putString("url", it.contenido)

            val intent = Intent(this, VideoEjercicioActivity::class.java)
            intent.putExtras(extras)
            startActivity(intent)

        }, true)
        mRecyclerView.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}