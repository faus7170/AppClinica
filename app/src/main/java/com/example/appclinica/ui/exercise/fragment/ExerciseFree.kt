package com.example.appclinica.ui.exercise.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.exercise.PasosActivity
import com.example.appclinica.ui.exercise.controlador.AdapterExercise
import com.example.appclinica.ui.exercise.model.TestDatos
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ExerciseFree() : Fragment() {

    //lateinit var adapter: AdapterExercise
    lateinit var adapter: AdapterExercise
    lateinit var mRecyclerView: RecyclerView
    //lateinit var userList: MutableList<GetDatosExercise>
    lateinit var userList: MutableList<TestDatos>
    lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_exercise_free, container, false)

        mRecyclerView = view.findViewById(R.id.recyclerExerciceFree)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)

        free()

        return view
    }

    private fun free() {

        database = Firebase.firestore
        userList = mutableListOf()

        database.collection("ejercicios").get().addOnSuccessListener { document ->
            userList.clear()
            for (getdatos in document) {
                if (getdatos.getBoolean("isfree") as Boolean) {
                    val nombre = getdatos.getString("nombre")
                    val descripcion = getdatos.getString("descripcion")
                    val id = getdatos.id
                    //val getDatos = GetDatosExercise(descripcion!!, nombre!!,id!!)
                    val testDatos = TestDatos(descripcion!!,nombre!!,id)

                    userList.add(testDatos)
                }
            }
            /*adapter = AdapterExercise(userList) {

                val extras = Bundle()
                extras.putString("id", it.id)
                extras.putString("nombre", it.nombre)

                //extras.putBoolean("HABILITADO", true)
                val intent = Intent(activity, PasosActivity::class.java)
                intent.putExtras(extras)
                startActivity(intent)

            }*/
            adapter = AdapterExercise(userList,{
                val extras = Bundle()
                extras.putString("id", it.id)
                extras.putString("nombre", it.nombre)

                //extras.putBoolean("HABILITADO", true)
                val intent = Intent(activity, PasosActivity::class.java)
                intent.putExtras(extras)
                startActivity(intent)
            },true)
            mRecyclerView.adapter = adapter

        }.addOnFailureListener { exception ->

        }
    }


}