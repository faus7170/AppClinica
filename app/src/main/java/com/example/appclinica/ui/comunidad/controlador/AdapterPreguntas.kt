package com.example.appclinica.ui.comunidad.controlador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.comunidad.model.SetPregunt


class AdapterPreguntas(var dataSet: MutableList<SetPregunt>,var item: (SetPregunt)-> Unit) : RecyclerView.Adapter<AdapterPreguntas.EjercHolder>() {

    //var dataSet : MutableList<SetPregunt> = mutableListOf()

    fun addMensaje(msm: SetPregunt){
        dataSet.add(msm)
        dataSet.reverse()
        notifyItemInserted(dataSet.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.designer_pregunta, parent, false)

        return EjercHolder(view)
    }

    override fun onBindViewHolder(holder: EjercHolder, position: Int) {
        holder.render(dataSet[position])
        holder.txtComent.setOnClickListener { item(dataSet[position]) }
        holder.txtLike.setOnClickListener { item(dataSet[position]) }

    }

    override fun getItemCount(): Int = dataSet.size

    inner class EjercHolder(view: View) : RecyclerView.ViewHolder(view) {

        var txtNombreEjerccio = view.findViewById(R.id.txtComentario) as TextView
        var txtLike = view.findViewById(R.id.viewLike) as TextView
        var txtComent = view.findViewById(R.id.viewComent) as TextView

        fun render(informacion: SetPregunt){

            txtNombreEjerccio.text = informacion.pregunta

        }

    }


}