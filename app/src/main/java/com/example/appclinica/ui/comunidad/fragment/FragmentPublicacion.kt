package com.example.appclinica.ui.comunidad.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appclinica.R
import com.example.appclinica.ui.chat.controlador.ReadPublicacionHistorial



class FragmentPublicacion : ReadPublicacionHistorial() {

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
        val view = inflater.inflate(R.layout.fragment_publicacion, container, false)

        val pref = requireActivity().getSharedPreferences("dateUser", AppCompatActivity.MODE_PRIVATE)
        val uid = pref.getString("uid", "default")!!
        val nombre = pref.getString("nombre", "default")!!
        val foto = pref.getString("foto", "default")!!


        findByid(view)

        //readPublicacion()

        btnPublicar.setOnClickListener {
            if(!textPregunta.text.toString().isEmpty()){
                sendPublicacion(uid, textPregunta.text.toString(),nombre,foto)
                textPregunta.setText("")
            }

        }

        readPublicaciones("publicacion","default")

        return view

    }

    fun sendPublicacion(uid: String, question: String,nombre: String,foto: String){

        val myRefprueba = database.getReference("publicacion")

        val hashMap: HashMap<String, String> = hashMapOf()
        hashMap.put("uid", uid)
        hashMap.put("pregunta", question)
        hashMap.put("nombre", nombre)
        hashMap.put("foto", foto)

        myRefprueba.push().setValue(hashMap)


    }

    /*private fun readPublicaciones() {
        val mutableList: MutableList<SetPregunt> = mutableListOf()

        val myRef = database.getReference("publicacion")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mutableList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue<SetPregunt>()

                    val pregunta = post!!.pregunta
                    val uid = post.uid
                    val nombre = post.nombre
                    val foto = post.foto
                    val id = postSnapshot.key.toString()

                    mutableList.add(SetPregunt(pregunta, nombre, foto, "2021", id,uid))
                }

                mutableList.reverse()
                adapter = TestAdapterComunidad(mutableList, true, this@FragmentPublicacion)
                recyclerView.adapter = adapter

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    override fun onComentar(id: String, nombre: String, pregunta: String, foto: String) {
        val extras = Bundle()
        extras.putString("id", id)
        extras.putString("pregunta",pregunta)
        extras.putString("nombre", nombre)
        extras.putString("foto", foto)
        val intent = Intent(activity, ComentActivity::class.java)
        intent.putExtras(extras)
        startActivity(intent)
    }

    override fun onBorrar(id: String) {
        database.getReference("publicacion").child(id).removeValue()
    }*/

    private fun findByid(view: View) {
        textPregunta = view.findViewById(R.id.txtPregunta)
        btnPublicar = view.findViewById(R.id.btnPublicarPregunta)
        recyclerView = view.findViewById(R.id.recyclerViewPreguntas)
        recyclerView.setHasFixedSize(true)
        val linerLinearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linerLinearLayoutManager
    }

}