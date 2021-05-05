package com.example.appclinica.ui.ejercicio.controlador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.ejercicio.model.TestDatos

class AdapterExercise(val dataSet: MutableList<TestDatos>, val listener: (TestDatos) -> Unit, var isExercise:Boolean) : RecyclerView.Adapter<AdapterExercise.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        if (isExercise){
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.designer_exercice,parent,false)
            return Holder(view)

        }else{
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.designer_pasos,parent,false)
            return Holder(view)
        }

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.render(dataSet[position])
        holder.itemView.setOnClickListener { listener(dataSet[position]) }

    }

    override fun getItemCount(): Int = dataSet.size

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {


        lateinit var viewNombreEjerccio : TextView
        lateinit var viewDescripcionEjercicio :TextView
        lateinit var viewPaso : TextView

        fun render (informacion : TestDatos){

            if (isExercise){
                viewNombreEjerccio = itemView.findViewById(R.id.viewNameExercise)
                viewDescripcionEjercicio = itemView.findViewById(R.id.viewDescriptionExercise)

                viewNombreEjerccio.text = informacion.nombre
                viewDescripcionEjercicio.text = informacion.descripcion

            }else{

                viewPaso = itemView.findViewById(R.id.viewNamePaso)
                viewPaso.text = informacion.identificador
            }

        }

    }



}