package com.example.appclinica.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.appclinica.R
import com.example.appclinica.ui.autohipnosis.AutohipnosisActivity
import com.example.appclinica.ui.chat.ChatActivity
import com.example.appclinica.ui.comunidad.ComunidadActivity
import com.example.appclinica.ui.configuracion.ConfigurarPerfilActivity
import com.example.appclinica.ui.exercise.EjercicioActivity
import com.example.appclinica.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class PrincipalActivity : AppCompatActivity(), View.OnClickListener, TestsSharedPreferences {

    lateinit var cardViewEjercicio: CardView
    lateinit var cardVieweAutohipnosis: CardView
    lateinit var cardVieweChat: CardView
    lateinit var cardVieweConfiguracion: CardView
    lateinit var cardVieweForo: CardView
    lateinit var cardViewePremium: CardView
    lateinit var btnCerrarSesion: Button
    lateinit var auth: FirebaseAuth
    lateinit var texttest: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)


        findById()

        val user = Firebase.auth.currentUser


        val pref = applicationContext.getSharedPreferences("dateUser", MODE_PRIVATE)
        val editor = pref.edit()

        db.collection("usuarios").document(user.uid).get()
                .addOnSuccessListener { getdatos ->
                    val nombre = getdatos.getString("nombre")
                    val descripcion = getdatos.getString("descripcion")
                    val titulo = getdatos.getString("titulo")
                    val foto = getdatos.getString("foto")



                    //editor.putString("uid",user.uid)
                    editor.putString("nombre", nombre)
                    editor.putString("descripcion", descripcion)
                    editor.putString("titulo", titulo)
                    editor.putString("foto", foto)
                    editor.apply()

                    /*val nombretest = pref.getString("nombre","default")
                    val descripciontest = pref.getString("descripcion","default")

                    //texttest.text = nombretest +" "+descripciontest
                    Log.d("firestoretest","valores "+ nombretest+" "+descripciontest)*/

                }.addOnFailureListener { exception ->

                }

        /*val pref = applicationContext.getSharedPreferences("dateUser", MODE_PRIVATE)

        val nombretest = pref.getString("nombre","default")
        val descripciontest = pref.getString("descripcion","default")

        texttest.text = nombretest +" "+descripciontest

        Log.d("firestoretestdos","valores "+ nombretest+" "+descripciontest)*/

        //dataUser(user.uid)
        //conexionFirestore(user.uid)

        //val pref = applicationContext.getSharedPreferences("dateUser", MODE_PRIVATE)



    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.bankcardEjercicio -> {
                val intent = Intent(this, EjercicioActivity::class.java)
                startActivity(intent)
            }
            R.id.bankcardAutohipnosis -> {
                val intent = Intent(this, AutohipnosisActivity::class.java)
                startActivity(intent)
            }
            R.id.bankcardChat -> {
                //Toast.makeText(applicationContext,"En mantenimiento",Toast.LENGTH_LONG).show()
                val intent = Intent(this, ChatActivity::class.java)
                startActivity(intent)
            }
            R.id.bankcardConfiguracion -> {
                val intent = Intent(this, ConfigurarPerfilActivity::class.java)
                startActivity(intent)
            }
            R.id.bankcardPremium -> {
                Toast.makeText(applicationContext, "En mantenimiento", Toast.LENGTH_LONG).show()
            }
            R.id.bankcardForo -> {
                val intent = Intent(this, ComunidadActivity::class.java)
                startActivity(intent)
            }
            R.id.btnCerrarSesion -> {
                Firebase.auth.signOut()
                clearData()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun clearData() {
        val preftest = applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
        preftest.edit().remove("isConfiguracion").apply()


        val pref = applicationContext.getSharedPreferences("dateUser", MODE_PRIVATE)
        val editor = pref.edit()
        editor.clear().apply()
    }

    override val context = this
    override val db = Firebase.firestore


    private fun findById() {
        cardViewEjercicio = findViewById(R.id.bankcardEjercicio)
        cardVieweAutohipnosis = findViewById(R.id.bankcardAutohipnosis)
        cardVieweChat = findViewById(R.id.bankcardChat)
        cardVieweConfiguracion = findViewById(R.id.bankcardConfiguracion)
        cardVieweForo = findViewById(R.id.bankcardForo)
        cardViewePremium = findViewById(R.id.bankcardPremium)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        texttest = findViewById(R.id.txttest)

        cardViewEjercicio.setOnClickListener(this)
        cardVieweAutohipnosis.setOnClickListener(this)
        cardVieweChat.setOnClickListener(this)
        cardVieweConfiguracion.setOnClickListener(this)
        cardViewePremium.setOnClickListener(this)
        cardVieweForo.setOnClickListener(this)
        btnCerrarSesion.setOnClickListener(this)
    }


}