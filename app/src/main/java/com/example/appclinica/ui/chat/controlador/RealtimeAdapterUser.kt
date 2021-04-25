package com.example.appclinica.ui.chat.controlador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.chat.modelo.GetDatosMensaje
import com.example.appclinica.ui.chat.modelo.GetDatosUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class RealtimeAdapterUser(val dataSet: MutableList<GetDatosUser>, val listener: (GetDatosUser) -> Unit, var isChat: Boolean) : RecyclerView.Adapter<RealtimeAdapterUser.EjercHolder>() {

    //var dataSet : MutableList<GetDatosUser> = mutableListOf()

    lateinit var viewName: TextView
    lateinit var thelastmessage: String
    lateinit var auth: FirebaseAuth


    fun addUser (user: GetDatosUser){
        dataSet.add(user)
        notifyItemInserted(dataSet.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercHolder {
        val layout = LayoutInflater.from(parent.context)

        return EjercHolder(layout.inflate(R.layout.designer_user_contact,parent,false))


    }

    override fun onBindViewHolder(holder: EjercHolder, position: Int) {
        holder.render(dataSet[position])
        holder.itemView.setOnClickListener { listener(dataSet[position]) }


        if (isChat){
            lastMsm(dataSet[position].id,holder.viewLastmsm)

        }else{
            holder.viewLastmsm.visibility = View.GONE
        }

    }


    override fun getItemCount(): Int = dataSet.size

    inner class EjercHolder(view: View) : RecyclerView.ViewHolder(view) {

        var viewName = view.findViewById(R.id.viewNameUserMsm) as TextView
        var viewLastmsm = view.findViewById(R.id.viewLastMsm) as TextView

        fun render (informacion: GetDatosUser){
            viewName.text = informacion.nombre

        }

    }

    fun lastMsm(userid:String, lastMsm: TextView){


        val uid = Firebase.auth.currentUser.uid

        thelastmessage = "default"

        val database = Firebase.database
        val myRef = database.getReference("chats")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue<GetDatosMensaje>()
                    if (post!!.reciver.equals(uid) && post!!.sender.equals(userid) ||
                        post!!.reciver.equals(userid) && post!!.sender.equals(uid) ){
                        thelastmessage = post.msm

                    }
                }
                when(thelastmessage){
                    "default"->{
                        lastMsm.text = "No message"
                    }else ->
                    lastMsm.text = thelastmessage

                }

                thelastmessage = "default"
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }


}