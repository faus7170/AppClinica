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
import com.example.appclinica.R
import com.example.appclinica.PrincipalActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference

class LoginFragment : Fragment(){

    private lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var btnIngresrCorreo: Button
    lateinit var txtCorreo : EditText
    lateinit var txtClave : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_login, container, false)

        txtCorreo = view!!.findViewById(R.id.txtCorreo)
        txtClave = view!!.findViewById(R.id.txtClave)
        btnIngresrCorreo = view!!.findViewById(R.id.btnIngresarCorreo)

        btnIngresrCorreo.setOnClickListener{

            loginUser(txtCorreo.text.toString(),txtClave.text.toString())
        }


        return view
    }

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

    fun updateUI(currentUser: FirebaseUser?) { //send current user to next activity
        if (currentUser == null) return
        val intent = Intent(activity, PrincipalActivity::class.java)
        startActivity(intent)
        //finish()
    }



}