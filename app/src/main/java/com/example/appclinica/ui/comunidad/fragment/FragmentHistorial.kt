package com.example.appclinica.ui.comunidad.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appclinica.R
import com.example.appclinica.ui.comunidad.controlador.ReadPublicacionHistorial

class FragmentHistorial : ReadPublicacionHistorial() {

    /*lateinit var textPregunta: EditText
    lateinit var btnPublicar: Button
    val database = Firebase.database
    lateinit var adapter: TestAdapterComunidad
    lateinit var recyclerView: RecyclerView*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mis_preguntas, container, false)

        val pref = requireActivity().getSharedPreferences("dateUser", AppCompatActivity.MODE_PRIVATE)
        val uid = pref.getString("uid", "default")!!

        recyclerView = view.findViewById(R.id.recyclerViewHistorial)
        recyclerView.setHasFixedSize(true)
        val linerLinearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linerLinearLayoutManager

        readPublicaciones("historial",uid)
        return view
    }


    /*private fun readHistorial(uid:String) {
        val mutableList: MutableList<SetPregunt> = mutableListOf()

        val myRef = database.getReference("publicacion")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mutableList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue<SetPregunt>()
                    if(uid.equals(post!!.uid)){
                        val pregunta = post!!.pregunta
                        val uid = post.uid
                        val nombre = post.nombre
                        val foto = post.foto
                        val id = postSnapshot.key.toString()
                        mutableList.add(SetPregunt(pregunta, nombre, foto, "2021", id,uid))
                    }


                }

                mutableList.reverse()
                adapter = TestAdapterComunidad(mutableList, true, this@FragmentHistorial)
                recyclerView.adapter = adapter

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    override fun onComentar(id: String, nombre: String, pregunta: String, foto: String) {
        TODO("Not yet implemented")
    }

    override fun onBorrar(id: String) {
        TODO("Not yet implemented")
    }*/

}