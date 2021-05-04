package com.example.appclinica.ui.comunidad.controlador

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.ui.comunidad.ComentActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

open class TestComunidadFunciones(): Fragment(), TestAdapterComunidad.onClickLister {

    val database = Firebase.database
    lateinit var adapter: TestAdapterComunidad
    lateinit var recyclerView: RecyclerView
    val db = Firebase.firestore


    fun sendPublicacion(sender: String, question: String){

        db.collection("usuarios").document(sender).get().addOnSuccessListener { getdatos ->
            val nombre = getdatos.getString("nombre")
            val foto = getdatos.getString("foto")

            val myRefprueba = database.getReference("publicacion")

            val hashMap: HashMap<String, String> = hashMapOf()
            hashMap.put("uid", sender)
            hashMap.put("pregunta", question)
            hashMap.put("nombre", nombre!!)
            hashMap.put("foto", foto!!)

            myRefprueba.push().setValue(hashMap)


        }.addOnFailureListener { exception ->

        }


    }


    override fun onComentar(id: String, nombre: String, pregunta: String, foto: String) {
        val extras = Bundle()
        extras.putString("id", id)
        extras.putString("pregunta",pregunta)
        extras.putString("nombre", nombre)
        extras.putString("foto", foto)
        val intent = Intent(activity, ComentActivity::class.java)
        intent.putExtras(extras)
        startActivity(intent)
    }

    override fun onBorrar(id: String) {
        database.getReference("publicacion").child(id).removeValue()
        Log.d("borar","valor "+id)
    }


}