package com.example.appclinica.ui.psicologo

import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView


open class ViewPsiocologo: AppCompatActivity(){

    lateinit var txtNombre: TextView
    lateinit var txtTitulo : TextView
    lateinit var txtDescripcion: TextView
    lateinit var txtGrupos: TextView
    lateinit var imgProfile: CircleImageView

    val db = Firebase.firestore

    fun activityPerfile(valor:String, claseNombre:String){

        val docRef = db.collection("usuarios").document(valor)
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                if (claseNombre.equals("SalaDeChatActivity")){
                    txtNombre.text = document.getString("nombre")
                    Glide.with(this).load(document.getString("foto")).into(imgProfile)
                }else if (claseNombre.equals("DisplayPsicoActivity")){
                    txtNombre.text = document.getString("nombre")
                    txtTitulo.text = document.getString("titulo")
                    txtDescripcion.text = document.getString("descripcion")
                    Glide.with(this).load(document.getString("foto")).into(imgProfile)
                }else{
                    Toast.makeText(applicationContext,"Clase no encontrada "+claseNombre, Toast.LENGTH_LONG).show()

                }

            } else {
                Toast.makeText(applicationContext,"Error al cargar", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener { exception ->

        }
    }


}