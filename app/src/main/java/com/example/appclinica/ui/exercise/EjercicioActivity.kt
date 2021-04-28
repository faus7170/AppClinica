package com.example.appclinica.ui.exercise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.appclinica.R
import com.example.appclinica.ui.exercise.controlador.PageAdapterExercice
import com.google.android.material.tabs.TabLayout

class EjercicioActivity : AppCompatActivity() {

    lateinit var tabLayout : TabLayout
    lateinit var viewPager : ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio)

        tabLayout = findViewById(R.id.idTabLayoutExercise)
        viewPager = findViewById(R.id.idViewPagerExercise)

        val pageAdapterExercice = PageAdapterExercice(supportFragmentManager)
        viewPager.adapter = pageAdapterExercice

        tabLayout.setupWithViewPager(viewPager)


    }
}