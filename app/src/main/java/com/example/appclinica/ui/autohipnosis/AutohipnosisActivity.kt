package com.example.appclinica.ui.autohipnosis

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appclinica.R
import com.example.appclinica.ui.autohipnosis.model.GetDatosAutohipnosis


class AutohipnosisActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var adapter: AdapterTes
    lateinit var mRecyclerViewDeporte: RecyclerView
    lateinit var mRecyclerViewAdicciones: RecyclerView
    lateinit var mRecyclerViewDormir: RecyclerView
    lateinit var userListDeporte: MutableList<GetDatosAutohipnosis>
    lateinit var userListAdicciones: MutableList<GetDatosAutohipnosis>
    lateinit var userListDormir: MutableList<GetDatosAutohipnosis>
    lateinit var txtDeporte:TextView
    lateinit var textView: TextView
    lateinit var txtAdicciones:TextView
    lateinit var txtDormir:TextView
    var isclick :Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autohipnosis)

        getValores()
        textView = findViewById(R.id.textView2test)

        val pref = applicationContext.getSharedPreferences("dateUser", MODE_PRIVATE)

        textView.text = pref.getString("nombre","null")+" "+pref.getString("descripcion","null")



        txtDeporte.setOnClickListener(this)
        txtAdicciones.setOnClickListener(this)
        txtDormir.setOnClickListener(this)

    }

    private fun adapterdormir() {

        userListDormir = mutableListOf(
                GetDatosAutohipnosis("Duerme con Hipnosis", "Comienza hoy mismo a utilizar la auto hipnosis y visualízate ejercitando tú cuerpo"),
                GetDatosAutohipnosis("Duerme con Hipnosis", "Comienza hoy mismo a utilizar la auto hipnosis y visualízate ejercitando tú cuerpo"),
                GetDatosAutohipnosis("Duerme con Hipnosis", "Comienza hoy mismo a utilizar la auto hipnosis y visualízate ejercitando tú cuerpo"),
                GetDatosAutohipnosis("Duerme con Hipnosis", "Comienza hoy mismo a utilizar la auto hipnosis y visualízate ejercitando tú cuerpo"),
                GetDatosAutohipnosis("Duerme con Hipnosis", "Comienza hoy mismo a utilizar la auto hipnosis y visualízate ejercitando tú cuerpo")
        )
        mRecyclerViewDormir.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false)
        adapter = AdapterTes(userListDormir) {

        }
        mRecyclerViewDormir.adapter = adapter
    }

    private fun adapteradicciones() {
        userListAdicciones = mutableListOf(
                GetDatosAutohipnosis("Rompe tus adicciones con Hipnosis", "Comienza hoy mismo a utilizar la auto hipnosis y visualízate ejercitando tú cuerpo"),
                GetDatosAutohipnosis("Rompe tus adicciones con Hipnosis", "Comienza hoy mismo a utilizar la auto hipnosis y visualízate ejercitando tú cuerpo"),
                GetDatosAutohipnosis("Rompe tus adicciones con Hipnosis", "Comienza hoy mismo a utilizar la auto hipnosis y visualízate ejercitando tú cuerpo")
        )
        mRecyclerViewAdicciones.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false)
        adapter = AdapterTes(userListAdicciones) {

        }
        mRecyclerViewAdicciones.adapter = adapter
    }

    private fun adapterDeporte() {
        userListDeporte = mutableListOf(
                GetDatosAutohipnosis("Ejercítate con Hipnosis", "Comienza hoy mismo a utilizar la auto hipnosis y visualízate ejercitando tú cuerpo"),
                GetDatosAutohipnosis("Ejercítate con Hipnosis", "Comienza hoy mismo a utilizar la auto hipnosis y visualízate ejercitando tú cuerpo"),
                GetDatosAutohipnosis("Ejercítate con Hipnosis", "Comienza hoy mismo a utilizar la auto hipnosis y visualízate ejercitando tú cuerpo"),
                GetDatosAutohipnosis("Ejercítate con Hipnosis", "Comienza hoy mismo a utilizar la auto hipnosis y visualízate ejercitando tú cuerpo"),
                GetDatosAutohipnosis("Ejercítate con Hipnosis", "Comienza hoy mismo a utilizar la auto hipnosis y visualízate ejercitando tú cuerpo")
        )
        mRecyclerViewDeporte.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false)
        adapter = AdapterTes(userListDeporte) {

        }
        mRecyclerViewDeporte.adapter = adapter
    }

    private fun getValores() {
        mRecyclerViewDeporte = findViewById(R.id.hipnosisDeporte)
        mRecyclerViewAdicciones = findViewById(R.id.hipnosisAdicciones)
        mRecyclerViewDormir = findViewById(R.id.hipnosisDormir)
        txtDeporte = findViewById(R.id.txtViewHipnosisDeporte)
        txtAdicciones = findViewById(R.id.txtViewHipnosisAdicciones)
        txtDormir = findViewById(R.id.txtViewHipnosisDormir)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.txtViewHipnosisDeporte -> {
                if (isclick){
                    mRecyclerViewDeporte.visibility = View.VISIBLE
                    mRecyclerViewAdicciones.visibility = View.GONE
                    mRecyclerViewDormir.visibility = View.GONE
                    isclick = false
                    adapterDeporte()
                }else{
                    mRecyclerViewDeporte.visibility = View.GONE
                    isclick = true
                }

            }
            R.id.txtViewHipnosisAdicciones -> {
                if (isclick){
                    mRecyclerViewDeporte.visibility = View.GONE
                    mRecyclerViewAdicciones.visibility = View.VISIBLE
                    mRecyclerViewDormir.visibility = View.GONE
                    adapteradicciones()
                    isclick = false
                }else{
                    mRecyclerViewAdicciones.visibility = View.GONE
                    isclick = true
                }

            }
            R.id.txtViewHipnosisDormir -> {
                if (isclick){
                    mRecyclerViewDeporte.visibility = View.GONE
                    mRecyclerViewAdicciones.visibility = View.GONE
                    mRecyclerViewDormir.visibility = View.VISIBLE
                    adapterdormir()
                    isclick = false
                }else{
                    mRecyclerViewDormir.visibility = View.GONE
                    isclick = true
                }

            }
        }
    }
}