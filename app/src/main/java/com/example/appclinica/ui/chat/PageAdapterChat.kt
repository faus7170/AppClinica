package com.example.appclinica.ui.chat

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.appclinica.ui.chat.fragment.ChatPsicologos
import com.example.appclinica.ui.chat.fragment.ChatSala
import com.example.appclinica.ui.exercise.fragment.ExerciseFree
import com.example.appclinica.ui.exercise.fragment.ExerciseToPay


class PageAdapterChat(fm: FragmentManager): FragmentPagerAdapter(fm) {


    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {

        when(position){
            0 -> {
                return ChatPsicologos()
            }
            else -> {
                return ChatSala()
            }

        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> {
                "Psicologos"
            }
            else -> {
                return "Chats"
            }

        }
    }
}