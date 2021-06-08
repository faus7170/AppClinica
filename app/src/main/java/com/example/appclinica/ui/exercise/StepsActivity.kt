package com.example.appclinica.ui.exercise

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.HandlerCompat.postDelayed
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.exercise.controlador.AdapterExercise
import com.example.appclinica.ui.exercise.model.Exercise
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StepsActivity : AppCompatActivity(){

    //lateinit var adapter: AdapterPasosExercise
    lateinit var adapter: AdapterExercise
    lateinit var mRecyclerView: RecyclerView
    //lateinit var userList: MutableList<GetDatosPasosExercise>
    lateinit var userList: MutableList<Exercise>
    lateinit var textView: TextView
    //lateinit var runnable: Runnable
    //var handler: Handler = Handler()
    private var mProgressDialog: ProgressDialog? = null


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

        var ischeck = false

        db.collection("ejercicios").document(dato.toString()).collection("pasos").get().addOnSuccessListener { document ->
            for (getdatos in document) {

                val ident = getdatos.getString("identificador")
                val contenido = getdatos.getString("contenido")
                val id = getdatos.id

                val testDatos = Exercise("null","null","null",ident!!,contenido!!)

                userList.add(testDatos)

            }

            adapter = AdapterExercise(userList,{

                //val pd = ProgressDialog(this)

                /*runnable = object : Runnable {
                    override fun run() {
                        pd.setTitle("Cargando audio ...")
                        pd.show()

                        handler.postDelayed(this,5000)
                    }
                }*/


                val intent = Intent(this, VideoActivity::class.java)
                //intent.putExtra("url", it.contenido)
                startActivity(intent)


            },false)
            mRecyclerView.adapter = adapter
        }.addOnFailureListener { exception ->

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onPrepareDialog(id: Int, dialog: Dialog?) {
        super.onPrepareDialog(id, dialog)
    }



}