package com.example.appclinica.ui.comunidad.controlador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appclinica.R
import com.example.appclinica.ui.comunidad.model.SetPregunt
import de.hdodenhof.circleimageview.CircleImageView


class TestAdapterComunidad(var dataSet: MutableList<SetPregunt>, var item: (SetPregunt)-> Unit, var isPublicacion:Boolean) : RecyclerView.Adapter<TestAdapterComunidad.EjercHolder>() {

    //var dataSet : MutableList<SetPregunt> = mutableListOf()

    fun addMensaje(msm: SetPregunt){
        dataSet.add(msm)
        dataSet.reverse()
        notifyItemInserted(dataSet.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercHolder {

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.designer_publicacion, parent, false)
        return EjercHolder(view)

    }

    override fun onBindViewHolder(holder: EjercHolder, position: Int) {
        holder.render(dataSet[position])
        holder.txtComent.setOnClickListener { item(dataSet[position]) }
        holder.txtLike.setOnClickListener { item(dataSet[position]) }

    }

    override fun getItemCount(): Int = dataSet.size

    inner class EjercHolder(view: View) : RecyclerView.ViewHolder(view) {


        lateinit var viewComentario :TextView
        lateinit var viewName : TextView
        lateinit var imagenCircle : CircleImageView
        lateinit var txtLike : TextView
        lateinit var txtComent :TextView
        lateinit var linearLayout: LinearLayout

        fun render(informacion: SetPregunt){

            viewComentario = itemView.findViewById(R.id.viewComentario) as TextView
            viewName = itemView.findViewById(R.id.viewNameIdUser) as TextView
            imagenCircle = itemView.findViewById(R.id.viewCirclePersonComent) as CircleImageView
            txtLike = itemView.findViewById(R.id.viewLike) as TextView
            txtComent = itemView.findViewById(R.id.viewComent) as TextView
            linearLayout= itemView.findViewById(R.id.linerLayoutLikeComentar) as LinearLayout

            viewComentario.text = informacion.pregunta
            viewName.text = informacion.nombre
            Glide.with(itemView.context).load(informacion.foto).into(imagenCircle)

            if (isPublicacion){
                linearLayout.visibility = View.VISIBLE
            }else{
                linearLayout.visibility = View.GONE
            }

        }

    }


}