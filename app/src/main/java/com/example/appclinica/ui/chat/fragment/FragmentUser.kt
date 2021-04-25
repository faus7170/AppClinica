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
import com.example.appclinica.ui.chat.modelo.GetDatosUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class FragmentUser : Fragment() {

    //lateinit var adapterPsicologo: AdapterPsicologo
    lateinit var adapterPsicologo: TestAdapter
    lateinit var mRecyclerView: RecyclerView
    lateinit var userList: MutableList<GetDatosUser>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user, container, false)


        mRecyclerView = view!!.findViewById(R.id.recyclerViewUser)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)


        val database = Firebase.database
        val myRef = database.getReference("user")
        userList = mutableListOf()


        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue<GetDatosUser>()
                    userList.add(post!!)

                }
                adapterPsicologo = TestAdapter(userList,{
                    val intent = Intent(activity, SalaDeChatActivity::class.java)
                    intent.putExtra("id", it.id)
                    startActivity(intent)
                }, false)
                mRecyclerView.adapter = adapterPsicologo
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })






        return view


    }


}