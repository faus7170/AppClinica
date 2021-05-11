package com.example.appclinica.ui.comunidad.controlador

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.ui.comunidad.ComentActivity
import com.example.appclinica.ui.comunidad.model.SetPregunt
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

open class ReadPublicacionHistorial: Fragment(), TestAdapterComunidad.onClickLister {

    lateinit var textPregunta: EditText
    lateinit var btnPublicar: Button
    val database = Firebase.database
    lateinit var adapter: TestAdapterComunidad
    lateinit var recyclerView: RecyclerView

    val TOPIC = "/topics/myTopic2"


    fun readPublicaciones(fragmen:String, uid:String) {
        val mutableList: MutableList<SetPregunt> = mutableListOf()

        val myRef = database.getReference("publicacion")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mutableList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue<SetPregunt>()

                    if (fragmen.equals("publicacion")){
                        val pregunta = post!!.pregunta
                        val uid = post.uid
                        val nombre = post.nombre
                        val foto = post.foto
                        val id = postSnapshot.key.toString()

                        mutableList.add(SetPregunt(pregunta, nombre, foto, "2021", id,uid))
                    }else if (fragmen.equals("historial")){
                        if(uid.equals(post!!.uid)){
                            val pregunta = post!!.pregunta
                            val uid = post.uid
                            val nombre = post.nombre
                            val foto = post.foto
                            val id = postSnapshot.key.toString()
                            mutableList.add(SetPregunt(pregunta, nombre, foto, "2021", id,uid))
                        }


                    }else{
                        Log.d("Conexion","Clase no encontrada")
                    }


                }

                mutableList.reverse()
                adapter = TestAdapterComunidad(mutableList, true, this@ReadPublicacionHistorial)
                recyclerView.adapter = adapter

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
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
    }
}