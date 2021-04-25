package com.example.appclinica.ui.chat.fragment

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.example.appclinica.ui.psicologo.DisplayPsicoActivity
import com.example.appclinica.ui.psicologo.ViewPsiocologo
import com.example.appclinica.ui.psicologo.GetDatosPsicologo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FragmentUser : Fragment() {

    lateinit var adapterPsicologo: FirestoreAdapterUser
    //lateinit var adapterPsicologo: RealtimeAdapterUser
    lateinit var mRecyclerView: RecyclerView
    lateinit var userList: MutableList<GetDatosPsicologo>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)


        mRecyclerView = view!!.findViewById(R.id.recyclerViewUser)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)

        val db = Firebase.firestore
        userList = mutableListOf()

        db.collection("usuarios").get().addOnSuccessListener { document ->
            for (getdatos in document) {

                if (getdatos.getBoolean("ispsicologo") as Boolean){

                    val nombre = getdatos.getString("nombre")
                    val descripcion = getdatos.getString("descripcion")
                    val titulo = getdatos.getString("titulo")
                    val foto = getdatos.getString("foto")
                    val id = getdatos.id

                    val getDatos = GetDatosPsicologo(nombre!!,titulo!!,descripcion!!,foto!!,id!!)

                    userList.add(getDatos)
                }

            }


            adapterPsicologo = FirestoreAdapterUser(userList,{
                /*val intent = Intent(activity, SalaDeChatActivity::class.java)
                intent.putExtra("id", it.id)
                startActivity(intent)*/

                option(it.id)

            },false)
            mRecyclerView.adapter = adapterPsicologo

        }.addOnFailureListener { exception ->

        }


        return view

    }

    private fun option(id: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        //builder.setTitle("Opciones")
        //builder.setMessage("Te interesa: ")

        builder.setPositiveButton(
            "Enviar mensaje", DialogInterface.OnClickListener { dialog, which ->

                /*Pasar un valor de una fragment A a un fragment B
                val bundle = Bundle()
                bundle.putString("id",id)
                parentFragmentManager.setFragmentResult("key",bundle)*/

                val intent = Intent(activity, SalaDeChatActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
            })

        builder.setNegativeButton("Ver perfil", DialogInterface.OnClickListener { dialog, which ->
            val intent = Intent(activity, DisplayPsicoActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)

        })
        builder.setNeutralButton("Cancelar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


}