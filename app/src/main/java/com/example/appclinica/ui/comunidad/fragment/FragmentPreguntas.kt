package com.example.appclinica.ui.comunidad.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.comunidad.ComentActivity
import com.example.appclinica.ui.comunidad.controlador.AdapterPreguntas
import com.example.appclinica.ui.comunidad.model.SetPregunt
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlin.collections.HashMap


class FragmentPreguntas : Fragment() {

    lateinit var textPregunta: EditText
    lateinit var btnPublicar: Button
    lateinit var uid:String
    lateinit var adapter: AdapterPreguntas
    lateinit var recyclerView:RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_preguntas, container, false)

        textPregunta = view.findViewById(R.id.txtPregunta)
        btnPublicar = view.findViewById(R.id.btnPublicarPregunta)
        recyclerView = view.findViewById(R.id.recyclerViewPreguntas)

        val user = Firebase.auth.currentUser
        uid = user.uid

        recyclerView.setHasFixedSize(true)
        val linerLinearLayoutManager = LinearLayoutManager(activity)
        //linerLinearLayoutManager.stackFromEnd = true

        recyclerView.layoutManager = linerLinearLayoutManager
        readMessege()

        btnPublicar.setOnClickListener {
            sendMessege(uid, textPregunta.text.toString())
            textPregunta.setText("")
        }

        return view

    }

    fun sendMessege(sender: String, question: String){

        val database = Firebase.database
        val myRefprueba = database.getReference("preguntas")

        val hashMap: HashMap<String, String> = hashMapOf()
        hashMap.put("sender", sender)
        hashMap.put("pregunta", question)

        myRefprueba.push().setValue(hashMap)

        //chatslist


    }

    fun readMessege(){

        var mutableList: MutableList<SetPregunt>

        mutableList = mutableListOf()

        val database = Firebase.database
        val myRef = database.getReference("preguntas")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mutableList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue<SetPregunt>()

                    var pregunta = post!!.pregunta
                    var sender = "default"
                    var id = postSnapshot.key.toString()

                    mutableList.add(SetPregunt(pregunta,sender,id))

                }

                mutableList.reverse()
                adapter = AdapterPreguntas(mutableList) {
                    //Toast.makeText(activity, "id: " + it.id, Toast.LENGTH_LONG).show()

                    val extras = Bundle()
                    extras.putString("id", it.id)
                    extras.putString("pregunta", it.pregunta)
                    val intent = Intent(activity, ComentActivity::class.java)
                    intent.putExtras(extras)
                    startActivity(intent)
                }
                recyclerView.adapter = adapter

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }
    fun setScroll(){
        recyclerView.scrollToPosition(adapter.itemCount - 1)
    }



}