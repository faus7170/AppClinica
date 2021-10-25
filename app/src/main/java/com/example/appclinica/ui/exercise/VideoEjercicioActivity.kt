package com.example.appclinica.ui.exercise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.appclinica.R
import com.example.appclinica.ui.exercise.controlador.AdapterExercise
import com.example.appclinica.ui.exercise.controlador.VideoViewHolder
import com.example.appclinica.ui.exercise.model.Exercise
import com.example.appclinica.ui.exercise.model.VideoViewModelo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class VideoEjercicioActivity : AppCompatActivity() {

    lateinit var viewpager: ViewPager2
    lateinit var userList:MutableList<VideoViewModelo>
    lateinit var videoViewHolder: VideoViewHolder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_ejercicio)

        val bundle = intent.extras
        val key = bundle!!.getString("id")
        viewpager = findViewById(R.id.viewPager)
        getDatosFirestore(key)

        /*mutableList = mutableListOf()
        mutableList.add(VideoViewModelo("Paso 1","https://firebasestorage.googleapis.com/v0/b/clinicaapp-2a450.appspot.com/o/ejercicios%2Fvideos%2FVideoTest.mp4?alt=media&token=a8b72eb7-13b9-48fe-8188-6a609dd0fc51",""))
        mutableList.add(VideoViewModelo("Paso 2","https://firebasestorage.googleapis.com/v0/b/clinicaapp-2a450.appspot.com/o/ejercicios%2Fvideos%2FVideoTest.mp4?alt=media&token=a8b72eb7-13b9-48fe-8188-6a609dd0fc51",""))
        mutableList.add(VideoViewModelo("Paso 3","https://firebasestorage.googleapis.com/v0/b/clinicaapp-2a450.appspot.com/o/ejercicios%2Fvideos%2FVideoTest.mp4?alt=media&token=a8b72eb7-13b9-48fe-8188-6a609dd0fc51",""))
        mutableList.add(VideoViewModelo("Paso 4","https://firebasestorage.googleapis.com/v0/b/clinicaapp-2a450.appspot.com/o/ejercicios%2Fvideos%2FVideoTest.mp4?alt=media&token=a8b72eb7-13b9-48fe-8188-6a609dd0fc51",""))

        //viewpager.setCurrentItem(VideoViewHolder)

        videoViewHolder = VideoViewHolder(mutableList) {

        }

        viewpager.setAdapter(videoViewHolder)*/

    }

    private fun getDatosFirestore(dato: String?) {
        val db = Firebase.firestore
        userList = mutableListOf()

        //var ischeck = false

        db.collection("ejercicios").document(dato.toString()).collection("pasos").get().addOnSuccessListener { document ->
            for (getdatos in document) {

                val ident = getdatos.getString("identificador")
                val contenido = getdatos.getString("contenido")
                val tipo= getdatos.getString("otro")
                val id = getdatos.id

                //val testDatos = Exercise("null","null","null",ident!!,contenido!!, tipo!!)

                val testDatos = VideoViewModelo(ident!!,contenido!!,tipo!!)
                userList.add(testDatos)

            }

            //viewpager.setCurrentItem(VideoViewHolder)

            videoViewHolder = VideoViewHolder(userList) {

            }

            viewpager.setAdapter(videoViewHolder)


        }.addOnFailureListener { exception ->

        }
    }
}