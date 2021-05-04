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
import androidx.appcompat.app.AlertDialog
import com.example.appclinica.R
import com.example.appclinica.ui.PrincipalActivity
import com.example.appclinica.ui.chat.controlador.FirestoreAdapterUser
import com.example.appclinica.ui.configuracion.ConfigurarPerfilActivity
import com.example.appclinica.ui.psicologo.GetDatosPsicologo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var btnRegistarCorreo: Button
    lateinit var txtCorreo : EditText
    lateinit var txtClave : EditText
    lateinit var txtConfirmarClave : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_register, container, false)

        txtCorreo = view!!.findViewById(R.id.txtCorreo)
        txtClave = view!!.findViewById(R.id.txtRegisterClave)
        txtConfirmarClave = view!!.findViewById(R.id.txtConfirmarClave)
        btnRegistarCorreo = view!!.findViewById(R.id.btnRegistrarCorreo)


        btnRegistarCorreo.setOnClickListener{
            registerNewUser(txtCorreo.text.toString(),txtClave.text.toString(),txtConfirmarClave.text.toString())
        }

        return view
    }

    fun checkCredentials(email: String, password: String, passwordRepit: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(activity, "Llenar los campos", Toast.LENGTH_LONG).show()
            return false
        }else if (!email.contains("@") || email.length < 6) {
            Toast.makeText(activity, "Verificar que el correo", Toast.LENGTH_LONG).show()
            return false
        }else if(!password.equals(passwordRepit)){
            Toast.makeText(activity, "Claves no conciden", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    fun registerNewUser(email: String, password: String, passwordRepit: String) {

        if (checkCredentials(email,password,passwordRepit)){
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser

                            //setDatos(user.uid)

                            updateUI(user)
                            //sendEmailVerification(user)
                        } else {
                            Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                            //updateUI(null)
                        }
                    }
        }


    }

    fun sendEmailVerification(user: FirebaseUser) {
        //Log.d(TAG, "started Verification")

        user!!.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //showConfirmationDialog(R.string.confirm_email, getString(R.string.please_confirm_email, user.email))
                        Toast.makeText(activity, "Mensaje de autenticacion enviado al correo", Toast.LENGTH_SHORT).show()

                    }else {
                        //Log.e(TAG, "sendEmailVerification", task.exception)
                    }
                }
    }

    fun showConfirmationDialog(title: Int, msg: String) {
        val dlg = AlertDialog.Builder(requireActivity())
        dlg.setMessage(msg)
        dlg.setTitle(title)
        dlg.setPositiveButton(R.string.ok, null)
        dlg.show()

    }

    fun updateUI(currentUser: FirebaseUser?) { //send current user to next activity
        if (currentUser == null) return
        val intent = Intent(activity, ConfigurarPerfilActivity::class.java)
        startActivity(intent)
        //finish()
    }

    fun setDatos(uid:String){

        val db = Firebase.firestore

        val datos = hashMapOf(
                "nombre" to "Anononimo",
                "descripcion" to "default",
                "titulo" to "defeult",
                "foto" to "https://www.nicepng.com/png/detail/202-2022264_usuario-annimo-usuario-annimo-user-icon-png-transparent.png",
                "ispsicologo" to false
        )

        db.collection("usuarios").document(uid).set(datos)
                .addOnSuccessListener {
                    Toast.makeText(activity,"Registro con exito",Toast.LENGTH_LONG).show()
                }.addOnFailureListener {

        }
    }

}


