package com.example.appclinica.ui.comunidad.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.comunidad.ComentActivity
import com.example.appclinica.ui.comunidad.controlador.AdapterPreguntas
import com.example.appclinica.ui.comunidad.controlador.TestComunidadFunciones
import com.example.appclinica.ui.comunidad.model.SetPregunt
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlin.collections.HashMap


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

        readPublicacion()

        btnPublicar.setOnClickListener {
            sendPublicacion(uid.uid, textPregunta.text.toString())
            textPregunta.setText("")
        }

        return view

    }



}