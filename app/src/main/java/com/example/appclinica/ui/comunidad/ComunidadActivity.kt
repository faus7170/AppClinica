package com.example.appclinica.ui.comunidad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.appclinica.R
import com.example.appclinica.ui.comunidad.controlador.PageAdapterComunidad
import com.google.android.material.tabs.TabLayout

class ComunidadActivity : AppCompatActivity() {

    lateinit var tabLayout : TabLayout
    lateinit var viewPager : ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comunidad)

        tabLayout = findViewById(R.id.idTabLayoutComunidad)
        viewPager = findViewById(R.id.idViewPagerComunidad)

        val pageAdapterComunidad = PageAdapterComunidad(supportFragmentManager)
        viewPager.adapter = pageAdapterComunidad

        tabLayout.setupWithViewPager(viewPager)

    }
}