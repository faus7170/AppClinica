package com.example.appclinica.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.appclinica.R
import com.example.appclinica.ui.chat.controlador.FragmentAdapterChat
import com.google.android.material.tabs.TabLayout

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

}