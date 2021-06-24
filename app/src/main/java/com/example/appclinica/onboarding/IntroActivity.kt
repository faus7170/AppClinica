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
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener


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
    var position = 0
    lateinit var btnGetStarted: Button
    lateinit var btnAnim: Animation
    lateinit var tvSkip: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // when this activity is about to be launch we need to check if its openened before or not
        if (restorePrefData()) {
            val mainActivity = Intent(applicationContext, LoginActivity::class.java)
            startActivity(mainActivity)
            finish()
        }
        setContentView(R.layout.activity_intro)


        // ini views
        btnNext = findViewById(R.id.btn_next)
        btnGetStarted = findViewById(R.id.btn_get_started)
        tabIndicator = findViewById(R.id.tab_indicator)
        btnAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.button_animation)

        // fill list screen
        val mList: MutableList<ScreenItem> = ArrayList()
        mList.add(ScreenItem("Fresh Food", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit", R.drawable.chat))
        mList.add(ScreenItem("Fast Delivery", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit", R.drawable.yoga))
        mList.add(ScreenItem("Easy Payment", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit", R.drawable.comunidad))

        // setup viewpager
        screenPager = findViewById(R.id.screen_viewpager)
        introViewPagerAdapter = IntroViewPagerAdapter(this, mList)
        screenPager.setAdapter(introViewPagerAdapter)

        // setup tablayout with viewpager
        tabIndicator.setupWithViewPager(screenPager)

        // next button click Listner
        btnNext.setOnClickListener(View.OnClickListener {
            position = screenPager.getCurrentItem()
            if (position < mList.size) {
                position++
                screenPager.setCurrentItem(position)
            }
            if (position == mList.size - 1) { // when we rech to the last screen

                // TODO : show the GETSTARTED Button and hide the indicator and the next button
                loaddLastScreen()
            }
        })

        // tablayout add change listener


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



        // Get Started button click listener
        btnGetStarted.setOnClickListener(View.OnClickListener { //open main activity
            val mainActivity = Intent(applicationContext, LoginActivity::class.java)
            startActivity(mainActivity)
            // also we need to save a boolean value to storage so next time when the user run the app
            // we could know that he is already checked the intro screen activity
            // i'm going to use shared preferences to that process
            savePrefsData()
            finish()
        })

        // skip button click listener
    }

    private fun restorePrefData(): Boolean {
        val pref = applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
        return pref.getBoolean("isIntroOpnend", false)
    }

    private fun savePrefsData() {
        val pref = applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("isIntroOpnend", true)
        editor.commit()
    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private fun loaddLastScreen() {
        btnNext!!.visibility = View.INVISIBLE
        btnGetStarted!!.visibility = View.VISIBLE
        tabIndicator!!.visibility = View.INVISIBLE
        // TODO : ADD an animation the getstarted button
        // setup animation
        btnGetStarted!!.animation = btnAnim
    }
}