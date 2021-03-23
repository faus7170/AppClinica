package com.example.appclinica.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.appclinica.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {


    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var database: DatabaseReference


    private val RC_SIGN_IN = 123
    private lateinit var auth: FirebaseAuth
    private val createNewAccount = true
    private val TAG = "LoginActivity"

    lateinit var btn_registrarGoogle: Button
    lateinit var txt_usr : EditText
    lateinit var txt_clave: EditText
    lateinit var btn_ingresar: Button

    lateinit var txt_dialogo_correo : EditText
    lateinit var txt_dialogo_clave: EditText
    lateinit var txt_dialogo_nombre: EditText
    lateinit var txt_dialogo_edad: EditText
    lateinit var btn_registrarCorreo: Button
    lateinit var btn_dialogo_registro: Button

    lateinit var btn_anonimo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        declarValores()
        initGoogleClient()
        setListener()

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun setListener() {
        btn_registrarCorreo.setOnClickListener(View.OnClickListener { view: View? ->
            showConfirmationDialogPersonalisado()
        })
        btn_ingresar.setOnClickListener {
            if (checkCredentials(txt_usr.getText().toString(), txt_clave.getText().toString())) {
                loginUser(txt_usr.getText().toString(), txt_clave.getText().toString())
            }

        }
        btn_registrarGoogle.setOnClickListener{
            loginWithGoogle()
        }

        btn_anonimo.setOnClickListener {
            loginAnonimo()
        }
    }

    private fun declarValores() {
        txt_usr = findViewById<EditText>(R.id.id_usr)
        txt_clave = findViewById<EditText>(R.id.id_clave)
        btn_registrarGoogle = findViewById(R.id.btn_registrarGoogle)
        btn_ingresar = findViewById<Button>(R.id.btn_ingresar)
        btn_registrarCorreo = findViewById<Button>(R.id.btn_registrar)
        btn_anonimo = findViewById<Button>(R.id.btn_ingresoAnonimo)

    }

    //Ingesar como anonimo

    private fun loginAnonimo() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }

            }
    }

    //Ingresar con correo

    private fun checkCredentials(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(applicationContext, "Llenar los campos", Toast.LENGTH_LONG).show()
            return false
        }else if (!email.contains("@") || email.length < 6) {
            Toast.makeText(applicationContext, "Verificar que el correo", Toast.LENGTH_LONG).show()
            return false
        } else if (password.length < 6) {
            Toast.makeText(applicationContext, "La contraseña no puede ser menor a 6 digitos", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun registerNewUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener { task: Task<AuthResult?> ->
                    if (task.isSuccessful) { // Sign in success,
                        //Log.d(TAG, "createUserWithEmail:success")
                        val user: FirebaseUser = auth.getCurrentUser()
                        val hasMap = HashMap<String,String>()
                        hasMap.put("nombre",txt_dialogo_nombre.text.toString())
                        hasMap.put("correo",txt_dialogo_correo.text.toString())
                        hasMap.put("clave",txt_dialogo_clave.text.toString())
                        hasMap.put("edad",txt_dialogo_edad.text.toString())
                        val uid = auth.currentUser.uid
                        database.child("usuarios").child(uid).setValue(hasMap)

                        sendEmailVerification(user)

                    } else { // If sign in fails
                        //Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        //error despues de enviar el mensaje de confirmacion
                        Toast.makeText(this@LoginActivity, "No se pudo registrar usuario", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun sendEmailVerification(user: FirebaseUser) {
        //Log.d(TAG, "started Verification")
        user.sendEmailVerification()
                .addOnCompleteListener(this) { task: Task<Void?> ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Verification email sent to " + user.email)
                        showConfirmationDialog(R.string.confirm_email,
                                getString(R.string.please_confirm_email, user.email))
                    } else {
                        Log.e(TAG, "sendEmailVerification", task.exception)
                    }
                }
    }

    private fun showConfirmationDialog(title: Int, msg: String) {
        val dlg = AlertDialog.Builder(this)
        dlg.setMessage(msg)
        dlg.setTitle(title)
        dlg.setPositiveButton(R.string.ok, null)
        dlg.show()
    }

    private fun showConfirmationDialogPersonalisado() {
        val dlg = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.dialog_signin,null)
        dlg.setView(view)

        val alertDialog = dlg.create()
        alertDialog.show()

        btn_dialogo_registro = view.findViewById<Button>(R.id.btn_dialogo_registrar)
        txt_dialogo_clave = view.findViewById(R.id.txt_dialogo_clave)
        txt_dialogo_correo = view.findViewById(R.id.txt_dialogo_correo)
        txt_dialogo_nombre = view.findViewById(R.id.txt_dialogo_nombre)
        txt_dialogo_edad = view.findViewById(R.id.txt_dialogo_edad)

        btn_dialogo_registro.setOnClickListener {
            //Toast.makeText(applicationContext, "Registrar usuario"+txt_dialogo_clave.text+" "+txt_dialogo_usr.text, Toast.LENGTH_SHORT).show()

            if (checkCredentials(txt_dialogo_correo.getText().toString(), txt_dialogo_clave.getText().toString())) {
                if (createNewAccount) { // user wants to create a new account
                    registerNewUser(txt_dialogo_correo.getText().toString(), txt_dialogo_clave.getText().toString())

                } else { // User wants to login in with an existing account
                    Toast.makeText(applicationContext, "La cuenta "+txt_dialogo_correo.text+" ya se encuentra registrada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {  // Sign in success,
                        Log.d(TAG, "signInWithEmail:success")
                        val user: FirebaseUser = auth.getCurrentUser()
                        if (!user.isEmailVerified) {
                            //showConfirmationDialog(R.string.confirm_email,
                            //      getString(R.string.please_confirm_email, user.email))
                            Toast.makeText(this@LoginActivity, "El usuario no confirma el correo", Toast.LENGTH_SHORT).show()
                        } else {
                            updateUI(user)
                        }
                    } else { // If sign in fails
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this@LoginActivity, "Error while login", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    //Ingresar con google

    private fun initGoogleClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private fun loginWithGoogle() {
        mGoogleSignInClient.signOut()

        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)

                val user = Firebase.auth.currentUser
                user?.let {
                    // Name, email address, and profile photo Url
                    val name = user.displayName
                    val email = user.email
                    val hasMap = HashMap<String,String>()
                    hasMap.put("nombre",name)
                    hasMap.put("correo",email)
                    val uid = auth.currentUser.uid
                    database.child("usuarios").child(uid).setValue(hasMap)

                    //Toast.makeText(applicationContext, "name "+photoUrl+ " "+isAnonimo, Toast.LENGTH_LONG).show()
                }

                firebaseAuthWithGoogle(account.idToken!!)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)

                } else {
                    Toast.makeText(applicationContext, "No se pudo iniciar sesion", Toast.LENGTH_LONG).show()

                }

            }
    }

    private fun updateUI(currentUser: FirebaseUser?) { //send current user to next activity
        if (currentUser == null) return
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}