package com.example.appclinica.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.appclinica.R
import com.example.appclinica.ui.login.LoginActivity
import com.google.android.material.tabs.TabLayout/**/

/**
 *@author David Aguinsaca
 *El usuario descarga la aplicacion por primera vez muestra
 *travez de un ViewPager y Fragment los servicios que ofrece la aplicacion
 *
 **/

class IntroActivity : AppCompatActivity() {

    lateinit var screenPager: ViewPager
    lateinit var introViewPagerAdapter: IntroViewPagerAdapter
    lateinit var tabIndicator: TabLayout
    lateinit var btnNext: Button
    lateinit var btnGetStarted: Button
    lateinit var btnAnim: Animation
    var position = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        if (restorePrefData()) {
            val loginActivity = Intent(applicationContext, LoginActivity::class.java)
            startActivity(loginActivity)
            finish()
        }
        findById()

        intro()

    }

    //Declaracion de valores
    private fun intro() {

        val mList: List<ScreenItem> = listOf(ScreenItem("Inteligencia Emocional", "En las últimas décadas en el ámbito de la psicología la inteligencia emocioanl tomado mucho relevancia", R.drawable.inteligenciaemocional),
                ScreenItem("Autohipnosis", "Activar tu capacidad de curación, regulación y confianza, resolver conflictos internos y mejorar tu bienestar, o conseguir soluciones creativas a cosas del día a día", R.drawable.yoga),
                ScreenItem("Chat", "Comunicación en tiempo real con psicolgos profesionales y entre varios usuarios ", R.drawable.chat),
                ScreenItem("Comunidad", "Espacio de comunicación asíncrona en el que los personas de un tema en específico", R.drawable.comunidad))


        introViewPagerAdapter = IntroViewPagerAdapter(this, mList)
        screenPager.adapter = introViewPagerAdapter

        tabIndicator.setupWithViewPager(screenPager)

        btnNext.setOnClickListener {
            position = screenPager.currentItem
            if (position < mList.size) {
                position++
                screenPager.currentItem = position
            }

            if (position == mList.size - 1) {
                loaddLastScreen()
            }
        }

        btnGetStarted.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            savePrefsData()
            finish()
        }


        listenerTab(mList)
    }


    //Implementacion de la clase SharedPreference para recuperar dato de si la aplicacion fue iniciada anteriormente
    private fun restorePrefData(): Boolean {
        val pref = applicationContext.getSharedPreferences("introConf", MODE_PRIVATE)
        return pref.getBoolean("isIntroOpnend", false)
    }

    //Implementacion de la clase SharedPreference para guardar el inicio de la aplicacion
    private fun savePrefsData() {
        val pref = applicationContext.getSharedPreferences("introConf", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("isIntroOpnend", true)
        editor.apply()
    }

    //Mostrar los Fragments
    private fun listenerTab(mList: List<ScreenItem>) {
        tabIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                if (tab.getPosition() == (mList.size) - 1) {

                    loaddLastScreen()

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })
    }

    private fun loaddLastScreen() {
        btnNext.visibility = View.INVISIBLE
        btnGetStarted.visibility = View.VISIBLE
        //tvSkip.visibility = View.INVISIBLE
        tabIndicator.visibility = View.INVISIBLE
        btnGetStarted.animation = btnAnim
    }

    fun findById(){
        screenPager = findViewById(R.id.screen_viewpager)
        tabIndicator = findViewById(R.id.tab_indicator)
        btnNext = findViewById(R.id.btn_next)
        btnGetStarted = findViewById(R.id.btn_get_started)
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);
        //tvSkip = findViewById(R.id.tv_skip)
    }
}