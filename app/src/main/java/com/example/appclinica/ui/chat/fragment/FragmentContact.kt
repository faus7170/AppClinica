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
import com.example.appclinica.ui.chat.controlador.RealtimeAdapterUser
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

class FragmentContact : Fragment() {

    lateinit var adapterUser: FirestoreAdapterUser
    lateinit var recyclerView: RecyclerView
    //lateinit var mUser: MutableList<GetDatosUser>
    lateinit var mUser: MutableList<GetDatosPsicologo>
    lateinit var userList: MutableList<ChatsList>
    lateinit var auth: FirebaseAuth
    lateinit var uid :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        val idpe = Firebase.auth.currentUser

        uid = idpe.uid


        recyclerView = view!!.findViewById(R.id.recyclerViewContact)

        recyclerView.setHasFixedSize(true)
        val linerLinearLayoutManager = LinearLayoutManager(activity)
        //linerLinearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linerLinearLayoutManager

        //adapterUser = RecyclerAdapterUser()


        userList = mutableListOf()

        val database = Firebase.database
        val myRef = database.getReference("chatslist").child(uid)

        myRef.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue<ChatsList>()

                    userList.add(post!!)

                }

                chatlist()

            }

        })


        return view
    }

    fun chatlist(){

        val db = Firebase.firestore
        //val myRef = database.getReference("user")
        mUser = mutableListOf()

        db.collection("usuarios").get().addOnSuccessListener { document ->
            mUser.clear()

            for (getdatos in document) {
                val nombre = getdatos.getString("nombre")
                val descripcion = getdatos.getString("descripcion")
                val titulo = getdatos.getString("titulo")
                val foto = getdatos.getString("foto")
                val id = getdatos.id

                for (chatList:ChatsList in userList)
                if (id.equals(chatList.id)){
                    val getdatos = GetDatosPsicologo(nombre!!,titulo!!,descripcion!!,foto!!,id!!)
                    mUser.add(getdatos)
                }
                adapterUser = FirestoreAdapterUser(mUser,{
                    val intent = Intent(activity, SalaDeChatActivity::class.java)
                    intent.putExtra("id", it.id)
                    startActivity(intent)
                },true)
                recyclerView.adapter = adapterUser
            }


        }.addOnFailureListener { exception ->

        }


    }


}

