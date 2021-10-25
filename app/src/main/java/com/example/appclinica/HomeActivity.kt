package com.example.appclinica

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.appclinica.ui.autohipnosis.AutohipnosisActivity
import com.example.appclinica.ui.chat.ChatActivity
import com.example.appclinica.ui.comunidad.ComunidadActivity
import com.example.appclinica.ui.configuracion.ConfiguracionActivity
import com.example.appclinica.ui.exercise.ExerciseActivity
import com.example.appclinica.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 *@author David Aguinsaca
 * Menu principal de la aplicaccion con los servicios ofreccidos:
 * Ejerccios, Autohipnosis, Chat, Configuracción y Comunidad
 **/


class HomeActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var cardViewEjercicio: CardView
    lateinit var cardVieweAutohipnosis: CardView
    lateinit var cardVieweChat: CardView
    lateinit var cardVieweConfiguracion: CardView
    lateinit var cardViewSignUp: CardView
    lateinit var cardVieweForo: CardView
    lateinit var btnTestNotification: Button
    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        //setTheme(R.style.Theme_AppCompat_Light_NoActionBar)

        val sharedPreferences: SharedPreferences = getSharedPreferences("theme" , Context.MODE_PRIVATE)
        val modo = sharedPreferences.getBoolean("n",false)

        if (modo==false){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        elementsById()
        getDatosUser()
    }

    //Obtener datos del usuario en la base de datos y guardar en la clase SharedPreferences
    private fun getDatosUser() {
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser

        val pref = applicationContext.getSharedPreferences("dateUser", MODE_PRIVATE)
        val editor = pref.edit()

        db.collection("usuarios").document(user.uid).get()
                .addOnSuccessListener { getdatos ->
                    val nombre = getdatos.getString("nombre")
                    val descripcion = getdatos.getString("descripcion")
                    val titulo = getdatos.getString("titulo")
                    val foto = getdatos.getString("foto")

                    editor.putString("uid",user.uid)
                    editor.putString("nombre", nombre)
                    editor.putString("descripcion", descripcion)
                    editor.putString("titulo", titulo)
                    editor.putString("foto", foto)
                    editor.apply()

                }.addOnFailureListener { exception ->

                }
    }

    //Accion para acceder a cada servicio con la clase onClick
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.bankcardExercice -> {
                val intent = Intent(this, ExerciseActivity::class.java)
                startActivity(intent)
                Animatoo.animateSlideLeft(this)
            }
            R.id.bankcardAutohipnosis -> {
                val intent = Intent(this, AutohipnosisActivity::class.java)
                startActivity(intent)
                Animatoo.animateSlideLeft(this)

            }
            R.id.bankcardChat -> {
                val intent = Intent(this, ChatActivity::class.java)
                startActivity(intent)
                Animatoo.animateSlideLeft(this)
            }
            R.id.bankcardConfiguracion-> {
                val intent = Intent(this, ConfiguracionActivity::class.java)
                startActivity(intent)
                //Toast.makeText(applicationContext,"En proceso ...",Toast.LENGTH_LONG).show()
                finish()

            }
            R.id.bankcardForo -> {
                val intent = Intent(this, ComunidadActivity::class.java)
                startActivity(intent)
                Animatoo.animateSlideLeft(this)
            }
            R.id.bankcardSignUp -> {
                Firebase.auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                Animatoo.animateSlideRight(this)
                finish()
            }
            R.id.testNotification ->{
                val intent = Intent(this, TestNotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }


    private fun elementsById() {
        cardViewEjercicio = findViewById(R.id.bankcardExercice)
        cardVieweAutohipnosis = findViewById(R.id.bankcardAutohipnosis)
        cardVieweChat = findViewById(R.id.bankcardChat)
        cardVieweConfiguracion = findViewById(R.id.bankcardConfiguracion)
        cardViewSignUp= findViewById(R.id.bankcardSignUp)
        cardVieweForo = findViewById(R.id.bankcardForo)
        btnTestNotification = findViewById(R.id.testNotification)

        cardViewEjercicio.setOnClickListener(this)
        cardVieweAutohipnosis.setOnClickListener(this)
        cardVieweChat.setOnClickListener(this)
        cardVieweConfiguracion.setOnClickListener(this)
        cardViewSignUp.setOnClickListener(this)
        cardVieweForo.setOnClickListener(this)
        btnTestNotification.setOnClickListener(this)
    }


}