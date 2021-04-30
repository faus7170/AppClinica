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
import com.example.appclinica.ui.exercise.model.GetDatosExercise
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ExerciseFree : Fragment() {

    lateinit var adapter: AdapterExercise
    //lateinit var adapterPsicologo: RealtimeAdapterUser
    lateinit var mRecyclerView: RecyclerView
    lateinit var userList: MutableList<GetDatosExercise>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_exercise_free, container, false)

        mRecyclerView = view!!.findViewById(R.id.recyclerExerciceFree)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)

        val db = Firebase.firestore
        userList = mutableListOf()

        db.collection("ejercicios").get().addOnSuccessListener { document ->

            userList.clear()
            for (getdatos in document) {
                if (getdatos.getBoolean("isfree") as Boolean){
                    val nombre = getdatos.getString("nombre")
                    val descripcion = getdatos.getString("descripcion")
                    val id = getdatos.id
                    val getDatos = GetDatosExercise(descripcion!!, nombre!!, id!!)

                    userList.add(getDatos)
                }

            }


            adapter = AdapterExercise(userList) {

                val extras = Bundle()
                extras.putString("id", it.id)
                extras.putString("nombre", it.nombre)

                //extras.putBoolean("HABILITADO", true)
                val intent = Intent(activity, PasosActivity::class.java)
                intent.putExtras(extras)
                startActivity(intent)

            }
            mRecyclerView.adapter = adapter

        }.addOnFailureListener { exception ->

        }


        return view
    }


}