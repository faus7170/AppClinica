package com.example.appclinica.ui.autohipnosis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.autohipnosis.model.GetDatosAutohipnosis
import com.example.appclinica.ui.exercise.model.GetDatosExercise
import com.example.appclinica.ui.exercise.model.GetDatosPasosExercise

class AdapterTes(val dataSet: MutableList<GetDatosAutohipnosis>, val listener: (GetDatosAutohipnosis) -> Unit) : RecyclerView.Adapter<AdapterTes.EjercHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.designer_autohipnosis,parent,false)

        return EjercHolder(view)
    }

    override fun onBindViewHolder(holder: EjercHolder, position: Int) {
        holder.render(dataSet[position])
        holder.itemView.setOnClickListener { listener(dataSet[position]) }

    }

    override fun getItemCount(): Int = dataSet.size

    inner class EjercHolder(view: View) : RecyclerView.ViewHolder(view) {

        var txtNombreEjerccio = view.findViewById(R.id.viewNameAutohipnosis) as TextView
        var txtDescripcion = view.findViewById(R.id.viewDescriptionAutohipnosis) as TextView

        fun render (informacion : GetDatosAutohipnosis){

            txtNombreEjerccio.text = informacion.nombre
            txtDescripcion.text = informacion.descripcion
        }

    }



}