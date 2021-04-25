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
import com.example.appclinica.ui.chat.controlador.TestAdapter
import com.example.appclinica.ui.chat.modelo.ChatsList
import com.example.appclinica.ui.chat.modelo.GetDatosUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class FragmentContact : Fragment() {

    lateinit var adapterUser: TestAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var mUser: MutableList<GetDatosUser>
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

        mUser = mutableListOf()

        val database = Firebase.database
        val myRef = database.getReference("user")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mUser.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue<GetDatosUser>()

                    for(chatList : ChatsList in userList){
                        if (post!!.id.equals(chatList.id)){
                            mUser.add(post)
                        }
                    }

                }

                adapterUser = TestAdapter(mUser,{
                    val intent = Intent(activity, SalaDeChatActivity::class.java)
                    intent.putExtra("id", it.id)
                    startActivity(intent)
                }, true)
                recyclerView.adapter = adapterUser

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


    }


}