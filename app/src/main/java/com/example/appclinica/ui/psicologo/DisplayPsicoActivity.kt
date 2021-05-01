package com.example.appclinica.ui.psicologo

import android.os.Bundle
import com.example.appclinica.R

class DisplayPsicoActivity : ViewPsiocologo() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_psico)

        val valor = intent.extras!!.getString("id")

        getId()

        activityPerfile(valor.toString(),"DisplayPsicoActivity")

    }

    private fun getId() {
        txtNombre = findViewById(R.id.viewNameProfile)
        txtTitulo = findViewById(R.id.viewTitleProfile)
        txtDescripcion = findViewById(R.id.viewDescriptionProfile)
        txtGrupos = findViewById(R.id.viewGrupProfile)
        imgProfile = findViewById(R.id.imgcircleProfile)
    }
}