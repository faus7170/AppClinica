package com.example.appclinica.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.appclinica.R
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*


class LoginActivity : Autenticacion() , View.OnClickListener{

    private lateinit var database: DatabaseReference

    lateinit var btn_registrarGoogle: Button
    lateinit var btn_registrarFacebook: Button
    lateinit var btn_ingresoAnonimo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        declarValores()
        initGoogleClient()

        btn_registrarFacebook.setOnClickListener(this)
        btn_registrarGoogle.setOnClickListener(this)
        btn_ingresoAnonimo.setOnClickListener(this)

    }

    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */


    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun declarValores() {

        btn_registrarGoogle = findViewById(R.id.btn_registrarGoogle)
        btn_ingresoAnonimo = findViewById(R.id.btn_ingresoAnonimo)
        btn_registrarFacebook = findViewById(R.id.btn_registroFacebook)

    }

    override fun onClick(v: View?) {

        when (v!!.id){
            R.id.btn_registroFacebook -> {
                loginWithFacebook()
            }
            R.id.btn_registrarGoogle -> {
                loginWithGoogle()
            }
            R.id.btn_ingresoAnonimo -> {
                loginAnonimo()
            }
        }
    }



}
