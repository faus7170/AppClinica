package com.example.appclinica.ui.chat.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.appclinica.R
import com.example.appclinica.ui.chat.SalaChatActivity
import com.example.appclinica.ui.exercise.EjercicioActivity

class ChatPsicologos : Fragment() {

    lateinit var cardViewechatBots: CardView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_chat_psicologos, container, false)

        cardViewechatBots = view!!.findViewById(R.id.cardViewChatBot)

        cardViewechatBots.setOnClickListener {

            val intent = Intent(activity, SalaChatActivity::class.java)
            startActivity(intent)
        }


        return view
    }

}