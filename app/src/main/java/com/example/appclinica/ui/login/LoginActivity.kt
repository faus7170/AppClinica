package com.example.appclinica.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.example.appclinica.R
import com.example.appclinica.data.Autenticacion
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class LoginActivity : Autenticacion() , View.OnClickListener{

    private lateinit var database: DatabaseReference

    lateinit var btn_registrarGoogle: ImageButton
    lateinit var btn_registrarFacebook: ImageButton
    lateinit var btn_ingresoAnonimo: Button
    lateinit var btn_ingresrCorreo: Button
    lateinit var btn_resetPasswordLogin: Button
    lateinit var btn_ingresrRegistrarCorreo: Button
    lateinit var txt_correo : EditText
    lateinit var txt_clave : EditText

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
        btn_ingresrCorreo.setOnClickListener(this)
        btn_ingresrRegistrarCorreo.setOnClickListener(this)
        btn_resetPasswordLogin.setOnClickListener(this)


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

        btn_registrarGoogle = findViewById(R.id.btn_img_google)
        btn_ingresoAnonimo = findViewById(R.id.btnIngresoAnonimo)
        btn_registrarFacebook = findViewById(R.id.btn_img_facebook)
        btn_ingresrCorreo = findViewById(R.id.btnIngresar)
        btn_ingresrRegistrarCorreo = findViewById(R.id.btnRegistrarPorCorreo)
        btn_resetPasswordLogin = findViewById(R.id.btnresetPassword)
        txt_correo = findViewById(R.id.txtCorreo)
        txt_clave = findViewById(R.id.txtClave)

    }

    override fun onClick(v: View?) {

        when (v!!.id){
            R.id.btn_img_facebook -> {
                loginWithFacebook()
            }
            R.id.btn_img_google -> {
                loginWithGoogle()
            }
            R.id.btnIngresoAnonimo -> {
                loginAnonimo()
            }
            R.id.btnIngresar ->{
                loginUser(txt_correo.text.toString(),txt_clave.text.toString())
            }
            R.id.btnRegistrarPorCorreo ->{
                showConfirmationDialogPersonalisado()
            }
            R.id.btnresetPassword ->{
                showConfirmationDialogPersonalisadoResetPassword()
            }
        }
    }



}
