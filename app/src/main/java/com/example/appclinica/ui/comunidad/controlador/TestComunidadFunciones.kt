package com.example.appclinica.ui.comunidad.controlador

import android.content.Intent
import android.os.Bundle
import android.os.health.UidHealthStats
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.ui.chat.SalaDeChatActivity
import com.example.appclinica.ui.chat.controlador.FirestoreAdapterUser
import com.example.appclinica.ui.chat.modelo.ChatsList
import com.example.appclinica.ui.comunidad.ComentActivity
import com.example.appclinica.ui.comunidad.model.SetPregunt
import com.example.appclinica.ui.psicologo.GetDatosPsicologo
import com.example.appclinica.ui.psicologo.ViewPsiocologo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

open class TestComunidadFunciones: Fragment() {

    val database = Firebase.database
    lateinit var adapter: TestAdapterComunidad
    lateinit var recyclerView: RecyclerView
    val db = Firebase.firestore

    fun sendMessege(idpregunta: String, comentario: String,sender:String){

        val database = Firebase.database
        val myRefprueba = database.getReference("preguntas").child(idpregunta).child("comentarios")

        val hashMap: HashMap<String, String> = hashMapOf()
        hashMap.put("idusuario",sender)
        hashMap.put("pregunta", comentario)

        myRefprueba.push().setValue(hashMap)


    }


    fun sendPublicacion(sender: String, question: String){

        db.collection("usuarios").document(sender).get().addOnSuccessListener { getdatos ->
            val nombre = getdatos.getString("nombre")
            val foto = getdatos.getString("foto")

            val myRefprueba = database.getReference("publicacion")

            val hashMap: HashMap<String, String> = hashMapOf()
            hashMap.put("sender", sender)
            hashMap.put("pregunta", question)
            hashMap.put("nombre", nombre!!)
            hashMap.put("foto", foto!!)

            myRefprueba.push().setValue(hashMap)


        }.addOnFailureListener { exception ->

        }


    }

    fun readPublicacion(){

        val mutableList: MutableList<SetPregunt> = mutableListOf()

        val myRef = database.getReference("publicacion")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mutableList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue<SetPregunt>()

                    var pregunta = post!!.pregunta
                    //var sender = post.sender
                    var nombre = post.nombre
                    var foto = post.foto
                    var id = postSnapshot.key.toString()

                    mutableList.add(SetPregunt(pregunta,nombre,foto,"2021",id))
                }

                mutableList.reverse()
                adapter = TestAdapterComunidad(mutableList, {
                    //Toast.makeText(activity, "id: " + it.id, Toast.LENGTH_LONG).show()

                    val extras = Bundle()
                    extras.putString("id", it.id)
                    extras.putString("pregunta", it.pregunta)
                    extras.putString("nombre", it.nombre)
                    extras.putString("foto", it.foto)
                    val intent = Intent(activity, ComentActivity::class.java)
                    intent.putExtras(extras)
                    startActivity(intent)
                },true)
                recyclerView.adapter = adapter

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }



}