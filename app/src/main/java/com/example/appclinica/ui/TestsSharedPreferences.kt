package com.example.appclinica.ui

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

interface TestsSharedPreferences {

    val context: Context
    val db: FirebaseFirestore

    fun dataUser(uid:String) {
        val pref = context.getSharedPreferences("dateUser", AppCompatActivity.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("uid",uid)
        editor.apply()


    }

    fun returnUid(): String {
        val pref = context.getSharedPreferences("dateUser", AppCompatActivity.MODE_PRIVATE)
        return pref.getString("uid","Valor no encontrado")!!
    }

    fun conexionFirestore(uid:String){

        val pref = context.getSharedPreferences("dateUser", AppCompatActivity.MODE_PRIVATE)
        val editor = pref.edit()


        db.collection("usuarios").document(uid).get()
                .addOnSuccessListener { getdatos ->
                    val nombre = getdatos.getString("nombre")
                    val descripcion = getdatos.getString("descripcion")
                    val titulo = getdatos.getString("titulo")
                    val foto = getdatos.getString("foto")

                    Log.d("firestore","valores "+ nombre+" "+descripcion)

                    editor.putString("uid",uid)
                    editor.putString("nombre",nombre)
                    editor.putString("descripcion",descripcion)
                    editor.putString("titulo",titulo)
                    editor.putString("foto",foto)
                    editor.apply()

        }.addOnFailureListener { exception ->

        }
    }

}