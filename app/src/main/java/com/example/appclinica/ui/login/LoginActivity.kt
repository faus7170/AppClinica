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
import com.facebook.login.LoginManager
import com.facebook.login.widget.LoginButton
import com.facebook.FacebookCallback
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap


class LoginActivity : AppCompatActivity() {


    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var database: DatabaseReference
    private var callbackManager = CallbackManager.Factory.create()
    private val RC_SIGN_IN = 123
    private lateinit var auth: FirebaseAuth
    private val createNewAccount = true
    private val TAG = "LoginActivity"

    lateinit var btn_registrarGoogle: Button
    lateinit var btn_registrarFacebook: Button
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

    private fun declarValores() {

        btn_registrarGoogle = findViewById(R.id.btn_registrarGoogle)
        btn_anonimo = findViewById<Button>(R.id.btn_ingresoAnonimo)
        btn_registrarFacebook = findViewById<Button>(R.id.btn_registroFacebook)

    }

    private fun setListener() {

        btn_registrarGoogle.setOnClickListener{
            loginWithGoogle()
        }

        btn_anonimo.setOnClickListener {
            loginAnonimo()
        }
        btn_registrarFacebook.setOnClickListener {
            loginWithFacebook()

        }
    }

    //Ingresar con facebook

    private fun loginWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken)

            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
                // ...
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
                // ...
            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                }

                // ...
            }
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

        callbackManager.onActivityResult(requestCode, resultCode, data)

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
                    val hasMap = HashMap<String, String>()
                    hasMap.put("nombre", name)
                    hasMap.put("correo", email)
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
                        Toast.makeText(
                                applicationContext,
                                "No se pudo iniciar sesion",
                                Toast.LENGTH_LONG
                        ).show()

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
