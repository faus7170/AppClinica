package com.example.appclinica.ui.chat.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.chat.SalaDeChatActivity
import com.example.appclinica.ui.chat.controlador.FirestoreAdapterUser
import com.example.appclinica.ui.chat.controlador.TestDatosUsuarios
import com.example.appclinica.ui.chat.modelo.ChatsList
import com.example.appclinica.ui.psicologo.GetDatosPsicologo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FragmentContact : TestDatosUsuarios() {

    //lateinit var adapterUser: FirestoreAdapterUser
    //lateinit var mRecyclerView: RecyclerView
    //lateinit var mUser: MutableList<GetDatosUser>
    //lateinit var mUser: MutableList<GetDatosPsicologo>
    //lateinit var userList: MutableList<ChatsList>
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        val uid = Firebase.auth.currentUser


        mRecyclerView = view!!.findViewById(R.id.recyclerViewContact)

        mRecyclerView.setHasFixedSize(true)
        val linerLinearLayoutManager = LinearLayoutManager(activity)
        //linerLinearLayoutManager.stackFromEnd = true
        mRecyclerView.layoutManager = linerLinearLayoutManager

        //adapterUser = RecyclerAdapterUser()


        obtenerContactos(uid.uid)

        return view
    }




}

