package com.example.appclinica.data

import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.appclinica.R
import com.example.appclinica.ui.login.MainActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


open class Autenticacion : AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    var callbackManager = CallbackManager.Factory.create()
    val RC_SIGN_IN = 123
    lateinit var auth: FirebaseAuth


    lateinit var txt_dialogo_correo : EditText
    lateinit var txt_dialogo_clave: EditText
    lateinit var txt_dialogo_nombre: EditText
    lateinit var txt_dialogo_correo_reset: EditText
    lateinit var txt_dialogo_edad: EditText
    lateinit var btn_resetPassword: Button
    lateinit var btn_dialogo_registro: Button


    //Registrar con facebook

    fun loginWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException) {

            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                        ).show()

                    }

                    // ...
                }
    }

    //Registrar como invitado

    fun loginAnonimo() {
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

    //Registrar con google

    fun initGoogleClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    fun loginWithGoogle() {
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
                firebaseAuthWithGoogle(account.idToken!!)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        }

        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d(TAG, "signInWithCredential:success")
                        val user = auth.currentUser
                        updateUI(user)

                    } else {
                        Toast.makeText(
                                applicationContext,
                                "No se pudo iniciar sesion",
                                Toast.LENGTH_LONG
                        ).show()

                    }

                }
    }

    fun updateUI(currentUser: FirebaseUser?) { //send current user to next activity
        if (currentUser == null) return
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    //Ingresar con correo

    fun checkCredentials(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(applicationContext, "Llenar los campos", Toast.LENGTH_LONG).show()
            return false
        }else if (!email.contains("@") || email.length < 6) {
            Toast.makeText(applicationContext, "Verificar que el correo", Toast.LENGTH_LONG).show()
            return false
        } else if (password.length < 6) {
            Toast.makeText(
                    applicationContext,
                    "La contraseña no puede ser menor a 6 digitos",
                    Toast.LENGTH_LONG
            ).show()
            return false
        }

        return true
    }

    fun registerNewUser(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        val user = auth.currentUser

                        sendEmailVerification(user)
                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
    }


    fun sendEmailVerification(user: FirebaseUser) {
        //Log.d(TAG, "started Verification")

        user!!.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showConfirmationDialog(
                                R.string.confirm_email,
                                getString(R.string.please_confirm_email, user.email)
                        )
                    }else {
                        //Log.e(TAG, "sendEmailVerification", task.exception)
                    }
                }
    }

    fun showConfirmationDialog(title: Int, msg: String) {
        val dlg = AlertDialog.Builder(this)
        dlg.setMessage(msg)
        dlg.setTitle(title)
        dlg.setPositiveButton(R.string.ok, null)
        dlg.show()

    }

    fun showConfirmationDialogPersonalisadoResetPassword() {
        val dlg = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.dialog_resetpassword, null)
        dlg.setView(view)

        val alertDialog = dlg.create()
        alertDialog.show()

        btn_resetPassword = view.findViewById(R.id.btnComprobar)
        txt_dialogo_correo_reset = view.findViewById(R.id.txtCorreoResetPassword)


        btn_resetPassword.setOnClickListener {

            if(!txt_dialogo_correo_reset.text.isEmpty()){
                resetPasword(txt_dialogo_correo_reset.text.toString())
            }else{
                Toast.makeText(applicationContext, "Llenar el campo", Toast.LENGTH_SHORT).show()

            }
        }
    }

    fun loginUser(email: String, password: String) {

        if (checkCredentials(email, password)) {

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user: FirebaseUser = auth.getCurrentUser()
                            if (!user.isEmailVerified) {
                                //showConfirmationDialog(R.string.confirm_email,
                                //      getString(R.string.please_confirm_email, user.email))
                                Toast.makeText(applicationContext, "El usuario no confirma el correo", Toast.LENGTH_SHORT).show()
                            } else {
                                updateUI(user)

                            }
                        } else {
                            Toast.makeText(applicationContext, "Error while login", Toast.LENGTH_SHORT)
                                    .show()
                        }
                    }

        }

    }

    fun resetPasword(emailAddress:String){

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext, "Mensaje enviado", Toast.LENGTH_SHORT).show()
                    }
                }
    }


    fun showConfirmationDialogPersonalisado() {
        val dlg = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.dialog_signin, null)
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
                registerNewUser(txt_dialogo_correo.getText().toString(), txt_dialogo_clave.getText().toString())
            }
        }
    }
    //Autenticar un usuario anonimo con facebook

    private fun handleFacebookAccessTokenAnonimo(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.currentUser!!.linkWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        Toast.makeText(baseContext, "Autenticacion correcta.",
                                Toast.LENGTH_SHORT).show()
                        val user = task.result?.user
                        updateUI(user)
                    } else {

                        Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()

                    }

                    // ...
                }
    }

}