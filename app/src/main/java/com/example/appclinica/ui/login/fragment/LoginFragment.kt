package com.example.appclinica.ui.login.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appclinica.R
import com.example.appclinica.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference

/**
 *@author David Aguinsaca
 * Fragmento de autenticacion de usuario con un correo y clave
 **/

class LoginFragment : Fragment(){

    lateinit var auth: FirebaseAuth
    lateinit var btnIngresrCorreo: Button
    lateinit var txtCorreo : EditText
    lateinit var txtClave : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        txtCorreo = view!!.findViewById(R.id.txtCorreo)
        txtClave = view!!.findViewById(R.id.txtClave)
        btnIngresrCorreo = view!!.findViewById(R.id.btnIngresarCorreo)

        btnIngresrCorreo.setOnClickListener{
            loginUser(txtCorreo.text.toString(),txtClave.text.toString())
        }

        return view
    }

    //Verificar los campos de correo y password

    fun checkCredentials(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(activity, "Llenar los campos", Toast.LENGTH_LONG).show()
            return false
        }else if (!email.contains("@") || email.length < 6) {
            Toast.makeText(activity, "Verificar que el correo", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    //Conexion con firebase para validar si la cuenta existe en caso de que si verifica que el correo y la clave concidadan
    fun loginUser(email: String, password: String) {

        if (checkCredentials(email, password)) {

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            updateUI(user)
                            /*if (!user.isEmailVerified) {
                                Toast.makeText(activity, "En la espera de confirmacion del correo", Toast.LENGTH_SHORT).show()
                            } else {
                                updateUI(user)
                            }*/
                        } else {
                            Toast.makeText(activity, "Error al cargar", Toast.LENGTH_SHORT).show()
                        }
                    }

        }

    }

    //Cargar el siguiente activity para la configuracion del perfil

    fun updateUI(currentUser: FirebaseUser?) { //send current user to next activity
        if (currentUser == null) return
        val intent = Intent(activity, HomeActivity::class.java)
        startActivity(intent)
        //savePrefsData()
        requireActivity().finish()
    }

    /*private fun savePrefsData() {
        val pref = requireActivity().getSharedPreferences("introConf",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = pref.edit()
        editor.putBoolean("isConfiguracion", true)
        editor.apply()
    }*/



}