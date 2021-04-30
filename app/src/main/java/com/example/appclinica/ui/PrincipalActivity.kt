package com.example.appclinica.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.appclinica.R
import com.example.appclinica.ui.autohipnosis.AutohipnosisActivity
import com.example.appclinica.ui.chat.ChatActivity
import com.example.appclinica.ui.comunidad.ComunidadActivity
import com.example.appclinica.ui.exercise.EjercicioActivity
import com.example.appclinica.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class PrincipalActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var cardViewEjercicio: CardView
    lateinit var cardVieweAutohipnosis: CardView
    lateinit var cardVieweChat: CardView
    lateinit var cardVieweConfiguracion: CardView
    lateinit var cardVieweForo: CardView
    lateinit var cardViewePremium: CardView
    lateinit var btnCerrarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        cardViewEjercicio = findViewById(R.id.bankcardEjercicio)
        cardVieweAutohipnosis= findViewById(R.id.bankcardAutohipnosis)
        cardVieweChat = findViewById(R.id.bankcardChat)
        cardVieweConfiguracion = findViewById(R.id.bankcardConfiguracion)
        cardVieweForo = findViewById(R.id.bankcardForo)
        cardViewePremium = findViewById(R.id.bankcardPremium)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)

        cardViewEjercicio.setOnClickListener(this)
        cardVieweAutohipnosis.setOnClickListener(this)
        cardVieweChat.setOnClickListener(this)
        cardVieweConfiguracion.setOnClickListener(this)
        cardViewePremium.setOnClickListener(this)
        cardVieweForo.setOnClickListener(this)
        btnCerrarSesion.setOnClickListener(this)


    }

    private fun salir() { //send current user to next activity
        Firebase.auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.bankcardEjercicio ->{
                //Toast.makeText(applicationContext,"Ejercicio",Toast.LENGTH_LONG).show()
                val intent = Intent(this,EjercicioActivity::class.java)
                startActivity(intent)
            }
            R.id.bankcardAutohipnosis ->{
                val intent = Intent(this, AutohipnosisActivity::class.java)
                startActivity(intent)
            }
            R.id.bankcardChat ->{
                //Toast.makeText(applicationContext,"En mantenimiento",Toast.LENGTH_LONG).show()
                val intent = Intent(this, ChatActivity::class.java)
                startActivity(intent)
            }
            R.id.bankcardConfiguracion ->{
                Toast.makeText(applicationContext,"En mantenimiento",Toast.LENGTH_LONG).show()
            }
            R.id.bankcardPremium ->{
                Toast.makeText(applicationContext,"En mantenimiento",Toast.LENGTH_LONG).show()
            }
            R.id.bankcardForo -> {
                val intent = Intent(this, ComunidadActivity::class.java)
                startActivity(intent)
            }
            R.id.btnCerrarSesion ->{
                Firebase.auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

            }
        }
    }


}