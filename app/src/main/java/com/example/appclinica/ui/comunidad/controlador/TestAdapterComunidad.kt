package com.example.appclinica.ui.comunidad.controlador

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appclinica.R
import com.example.appclinica.ui.comunidad.model.SetPregunt
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView


class TestAdapterComunidad(var dataSet: MutableList<SetPregunt>, var isPublicacion: Boolean,
                           var itemOnclicklister: onClickLister, var uidt:String,var testClase:String) : RecyclerView.Adapter<TestAdapterComunidad.EjercHolder>() {

    //, var item: (SetPregunt)-> Unit
    //var dataSet : MutableList<SetPregunt> = mutableListOf()
    lateinit var auth: FirebaseAuth
    lateinit var uid:String
    //lateinit var itemClickListener: OnItemClickListener


    fun addMensaje(msm: SetPregunt){
        dataSet.add(msm)
        dataSet.reverse()
        notifyItemInserted(dataSet.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercHolder {

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.designer_publicacion, parent, false)
        return EjercHolder(view)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: EjercHolder, position: Int) {
        holder.render(dataSet[position])
        //holder.txtComent.setOnClickListener { item(dataSet[position]) }
        //holder.txtLike.setOnClickListener { item(dataSet[position]) }

    }

    override fun getItemCount(): Int = dataSet.size




    inner class EjercHolder(view: View) : RecyclerView.ViewHolder(view) {

        lateinit var viewComentario :TextView
        lateinit var imagenCircle : CircleImageView
        lateinit var txtLike : TextView
        lateinit var txtComent :TextView
        lateinit var button: Button
        lateinit var buttonCancelar: Button
        lateinit var editTextPublicacion:EditText
        lateinit var linearLayout: LinearLayout
        lateinit var linearLayoutEdit: LinearLayout
        lateinit var toolbar: Toolbar

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun render(informacion: SetPregunt){
            
            uid = uidt

            getBy()


            viewComentario.text = informacion.pregunta
            toolbar.setTitle(informacion.nombre)
            Glide.with(itemView.context).load(informacion.foto).into(imagenCircle)

            txtComent.setOnClickListener{itemOnclicklister.onComentar(informacion.id, informacion.nombre, informacion.pregunta, informacion.foto)}

            button.setOnClickListener {
                val readPublicacionHistorial = ReadPublicacionHistorial()
                readPublicacionHistorial.editPublicacion(informacion.id, editTextPublicacion.text.toString(),testClase,informacion.key)
                viewComentario.visibility = View.VISIBLE
                linearLayoutEdit.visibility = View.GONE
            }

            buttonCancelar.setOnClickListener {
                viewComentario.visibility = View.VISIBLE
                linearLayoutEdit.visibility = View.GONE
            }

            if (testClase.equals("publicacion")|| testClase.equals("historial")){
                linearLayout.visibility = View.VISIBLE
            }else{
                linearLayout.visibility = View.GONE
            }

            if (isPublicacion){
                if (informacion.uid.equals(uid)) {
                    toolbar.inflateMenu(R.menu.menu_card_publicacion)
                    toolbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.action_option1 -> {
                                viewComentario.visibility = View.GONE
                                linearLayoutEdit.visibility = View.VISIBLE
                                editTextPublicacion.setText(informacion.pregunta)

                            }
                            R.id.action_option2 -> {
                                Log.d("Test_menu", "option1" + informacion.id)
                                val readPublicacionHistorial = ReadPublicacionHistorial()
                                readPublicacionHistorial.delate(informacion.id, testClase, informacion.key)
                            }
                        }
                        true
                    })
                }
            }



        }


        private fun getBy() {
            viewComentario = itemView.findViewById(R.id.viewComentario) as TextView
            imagenCircle = itemView.findViewById(R.id.viewCirclePersonComent) as CircleImageView
            txtLike = itemView.findViewById(R.id.viewLike) as TextView
            txtComent = itemView.findViewById(R.id.viewComent) as TextView
            linearLayout = itemView.findViewById(R.id.linerLayoutLikeComentar) as LinearLayout
            linearLayoutEdit = itemView.findViewById(R.id.linerlayoutEdit) as LinearLayout
            toolbar = itemView.findViewById(R.id.toolbar2)
            editTextPublicacion = itemView.findViewById(R.id.editPublicacion)
            button = itemView.findViewById(R.id.btnActualizar)
            buttonCancelar = itemView.findViewById(R.id.btnCancelarEdit)
        }

    }

    interface onClickLister{
        fun onComentar(id: String, nombre: String, pregunta: String, foto: String)
        fun onBorrar(id: String)
    }


}