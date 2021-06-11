package com.example.appclinica.paymantel.controlador

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.exercise.model.Exercise
import com.paymentez.android.model.Card

class MyCardAdapter(val dataSet: MutableList<Card>, val listener: (Card) -> Unit) : RecyclerView.Adapter<MyCardAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_cards,parent,false)
        return Holder(view)

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.render(dataSet[position])
        holder.itemView.setOnClickListener { listener(dataSet[position]) }

        //holder.viewCardNumber.setText("xxx"+dataSet.get(position).last4 +" - statis: "+ dataSet.get(position).status)

        //holder.viewCardHolderName.setText(dataSet.get(position).holderName)

    }



    override fun getItemCount(): Int = dataSet.size

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {


        lateinit var viewCardNumber : TextView
        lateinit var viewCardHolderName : TextView
        lateinit var imagenViewDelated : ImageButton

        fun render (informacion: Card){

            viewCardHolderName = itemView.findViewById(R.id.textViewCardHoldersName)
            viewCardNumber = itemView.findViewById(R.id.textViewCardNumber)
            imagenViewDelated = itemView.findViewById(R.id.imageViewDeleteCard)

            viewCardHolderName.text = informacion.holderName
            viewCardNumber.text = "XXXX."+informacion.last4 +" - status: "+ informacion.status

        }

    }



}