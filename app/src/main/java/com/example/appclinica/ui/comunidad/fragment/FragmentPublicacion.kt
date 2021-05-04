package com.example.appclinica.ui.comunidad.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appclinica.R
import com.example.appclinica.ui.comunidad.controlador.TestAdapterComunidad
import com.example.appclinica.ui.comunidad.controlador.TestComunidadFunciones
import com.example.appclinica.ui.comunidad.model.SetPregunt
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class FragmentPublicacion : TestComunidadFunciones() {

    lateinit var textPregunta: EditText
    lateinit var btnPublicar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_publicacion, container, false)

        var uid = Firebase.auth.currentUser

        textPregunta = view.findViewById(R.id.txtPregunta)
        btnPublicar = view.findViewById(R.id.btnPublicarPregunta)
        recyclerView = view.findViewById(R.id.recyclerViewPreguntas)
        recyclerView.setHasFixedSize(true)
        val linerLinearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linerLinearLayoutManager

        //readPublicacion()

        btnPublicar.setOnClickListener {
            sendPublicacion(uid.uid, textPregunta.text.toString())
            textPregunta.setText("")
        }

        readPublicaciones()

        return view

    }

    private fun readPublicaciones() {
        val mutableList: MutableList<SetPregunt> = mutableListOf()

        val myRef = database.getReference("publicacion")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mutableList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue<SetPregunt>()

                    var pregunta = post!!.pregunta
                    var uid = post.uid
                    var nombre = post.nombre
                    var foto = post.foto
                    var id = postSnapshot.key.toString()

                    mutableList.add(SetPregunt(pregunta, nombre, foto, "2021", id,uid))
                }

                mutableList.reverse()
                adapter = TestAdapterComunidad(mutableList, true, this@FragmentPublicacion)
                recyclerView.adapter = adapter

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }


}