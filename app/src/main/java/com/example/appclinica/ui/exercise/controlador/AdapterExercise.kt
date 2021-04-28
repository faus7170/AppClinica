package com.example.appclinica.ui.exercise.controlador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.exercise.model.GetDatosExercise

class AdapterExercise(val dataSet: MutableList<GetDatosExercise>, val listener: (GetDatosExercise) -> Unit) : RecyclerView.Adapter<AdapterExercise.EjercHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.designer_exercice,parent,false)

        return EjercHolder(view)
    }

    override fun onBindViewHolder(holder: EjercHolder, position: Int) {
        holder.render(dataSet[position])
        holder.itemView.setOnClickListener { listener(dataSet[position]) }

    }

    override fun getItemCount(): Int = dataSet.size

    inner class EjercHolder(view: View) : RecyclerView.ViewHolder(view) {

        var txtNombreEjerccio = view.findViewById(R.id.viewNameExercise) as TextView
        var txtDescripcion = view.findViewById(R.id.viewDescriptionExercise) as TextView

        fun render (informacion : GetDatosExercise){

            txtNombreEjerccio.text = informacion.nombre
            txtDescripcion.text = informacion.descripcion
        }

    }



}