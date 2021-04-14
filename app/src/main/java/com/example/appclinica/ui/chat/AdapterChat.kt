package com.example.appclinica.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class AdapterChat (private val usermsm:String): RecyclerView.Adapter<AdapterChat.ChatHolder>() {


    //val user = Firebase.auth.currentUser

    //val uid = user.uid


    var dataSet : MutableList<MensajeRecivir> = mutableListOf()
    fun addMensaje (msm: MensajeRecivir){
        dataSet.add(msm)
        notifyItemInserted(dataSet.size)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val layout = LayoutInflater.from(parent.context)
        return ChatHolder(layout.inflate(R.layout.design_msm,parent,false))
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {

        if (usermsm == "usr"){
            holder.cardEmisor.visibility = View.VISIBLE
            holder.cardReceptor.visibility = View.GONE
            holder.txt_mensaje.text = dataSet[position].contenido

        }else{
            holder.cardEmisor.visibility = View.GONE
            holder.cardReceptor.visibility = View.VISIBLE
            holder.txt_mensajeOther.text = dataSet[position].contenido


        }


        val longHora = dataSet[position].map
        val date = Date(longHora)
        //val dateFormat: DateFormat = DateFormat.getDateTimeInstance()
        val simpleDateFormat = SimpleDateFormat ("hh:mm a")

        holder.txt_date.text = simpleDateFormat.format(date)
        holder.txt_dateOther.text = simpleDateFormat.format(date)

    }

    override fun getItemCount(): Int = dataSet.size

    inner class ChatHolder(view: View) : RecyclerView.ViewHolder(view) {

        var txt_mensaje = view.findViewById(R.id.txtMensaje) as TextView
        var txt_date = view.findViewById(R.id.txtDate) as TextView
        var txt_mensajeOther = view.findViewById(R.id.txtMensajeReceptor) as TextView
        var txt_dateOther = view.findViewById(R.id.txtDateRecepetor) as TextView
        var cardEmisor = view.findViewById(R.id.id_card_emisor) as CardView
        var cardReceptor = view.findViewById(R.id.id_card_receptor) as CardView

        fun render (informacion : MensajeRecivir){
            txt_mensaje.text = informacion.contenido
            //txt_date.text = informacion.date
        }

    }

}
