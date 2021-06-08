package com.example.appclinica.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.appclinica.R
import com.example.appclinica.ui.chat.controlador.FragmentAdapterChat
import com.google.android.material.tabs.TabLayout

/**
 *@author David Aguinsaca
 *Activity principal de de chat contiene fragmentos donde muestra a los psicologos y el historial de chat
 *
 **/

class ChatActivity : AppCompatActivity() {

    lateinit var tabLayout : TabLayout
    lateinit var viewPager : ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        tabLayout = findViewById(R.id.idTabLayoutChat)
        viewPager = findViewById(R.id.idViewPagerChat)

        val pageAdapterChat = FragmentAdapterChat(supportFragmentManager)
        viewPager.adapter = pageAdapterChat

        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}