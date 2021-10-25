package com.example.appclinica.ui.login

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.viewpager.widget.ViewPager
import com.example.appclinica.R
import com.example.appclinica.ui.login.controlador.Authentication
import com.example.appclinica.ui.login.controlador.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 *@author David Aguinsaca
 * Activity donde contiene fragmento del registro y de autenticacion
 **/

class LoginActivity : Authentication(), View.OnClickListener {

    private lateinit var database: DatabaseReference

    lateinit var btnIngresarGoogle: ImageButton
    lateinit var btnIngresarFacebook: ImageButton
    lateinit var tabLayout : TabLayout
    lateinit var viewPager : ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initGoogleClient()
        elementById()

        
    }

    //Verificar si ya hay una cuenta que haya iniciado sesion previamente
   override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
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

    private fun elementById() {
        btnIngresarFacebook = findViewById(R.id.btnLoginFacebook)
        btnIngresarGoogle = findViewById(R.id.btnLoginGoogle)
        tabLayout = findViewById(R.id.tabLayoutLogin)
        viewPager = findViewById(R.id.viewPagerLogin)

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        btnIngresarFacebook.setOnClickListener(this)
        btnIngresarGoogle.setOnClickListener(this)

        val pageAdapter = PagerAdapter(supportFragmentManager)
        viewPager.adapter = pageAdapter

        tabLayout.setupWithViewPager(viewPager)
    }

}
