package com.example.appclinica.ui.chat.controlador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.chat.modelo.DatosMensaje
import com.example.appclinica.ui.chat.modelo.MessageReciver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(val dataSet: MutableList<MessageReciver>) : RecyclerView.Adapter<MessageAdapter.EjercHolder>() {

    var type_left :Int = 0
    var type_right :Int = 1

    lateinit var auth: FirebaseAuth
    lateinit var uid :String

    //lateinit var firebaseUser: FirebaseUser

    //var dataSet : MutableList<GetDatosMensaje> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercHolder {

        if (viewType == type_right){
            val layout = LayoutInflater.from(parent.context)
            return EjercHolder(layout.inflate(R.layout.chat_right,parent,false))
        }else{
            val layout = LayoutInflater.from(parent.context)
            return EjercHolder(layout.inflate(R.layout.chat_left,parent,false))
        }


    }

    override fun onBindViewHolder(holder: EjercHolder, position: Int) {
        holder.render(dataSet[position])

        val longHora = dataSet[position].hora
        val date = Date(longHora)
        val simpleDateFormat = SimpleDateFormat ("hh:mm a")
        holder.txt_date.text = simpleDateFormat.format(date)
    }


    override fun getItemCount(): Int = dataSet.size

    override fun getItemViewType(position: Int): Int {

        //val uid = Firebase.auth.currentUser

        uid = Firebase.auth.currentUser.uid


        if (dataSet.get(position).sender.equals(uid)){
            return type_right
        }else
            return type_left

    }


    inner class EjercHolder(view: View) : RecyclerView.ViewHolder(view) {

        var showmsm = view.findViewById(R.id.showMessege) as TextView
        var txt_date = view.findViewById(R.id.viewHora) as TextView


        fun render (informacion: DatosMensaje){
            showmsm.text = informacion.msm

        }

    }


}