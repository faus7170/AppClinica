package com.example.appclinica.ui.login

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.viewpager.widget.ViewPager
import com.example.appclinica.R
import com.example.appclinica.ui.login.controlador.Autenticacion
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class LoginActivity : Autenticacion(), View.OnClickListener {

    private lateinit var database: DatabaseReference

    lateinit var btnIngresarGoogle: ImageButton
    lateinit var btnIngresarFacebook: ImageButton
    lateinit var tabLayout : TabLayout
    lateinit var viewPager : ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        initGoogleClient()
        declarValores()

        btnIngresarFacebook.setOnClickListener(this)
        btnIngresarGoogle.setOnClickListener(this)

        val pageAdapter = PageAdapter(supportFragmentManager)
        viewPager.adapter = pageAdapter

        tabLayout.setupWithViewPager(viewPager)
        
    }


   override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun declarValores() {
        btnIngresarFacebook = findViewById(R.id.btnLoginFacebook)
        btnIngresarGoogle = findViewById(R.id.btnLoginGoogle)
        tabLayout = findViewById(R.id.idTabLayoutExercise)
        viewPager = findViewById(R.id.idViewPagerExercise)
    }

    override fun onClick(v: View?) {

        when (v!!.id){
            R.id.btnLoginFacebook -> {
                loginWithFacebook()
            }
            R.id.btnLoginGoogle -> {
                loginWithGoogle()
            }

        }
    }

}
