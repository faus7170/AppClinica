package com.example.appclinica.ui.configuracion

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.appclinica.R
import com.example.appclinica.HomeActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView

class ConfigurarPerfilActivity : AppCompatActivity() {

    lateinit var imagenCircleImageView: CircleImageView
    lateinit var textViewNombre: EditText
    lateinit var textViewApellido: EditText
    lateinit var textViewDescripcion: EditText
    lateinit var btnOmitir:Button
    lateinit var btnGuardar:Button
    val user = Firebase.auth.currentUser
    lateinit var checkBoxH: CheckBox
    lateinit var checkBoxM: CheckBox
    lateinit var checkBoxO: CheckBox

    lateinit var uri:Uri
    lateinit var imagenDefault: String
    lateinit var genero: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configurar_perfil)


        if (true) {
            staractivity()
        }

        findViewbyid()
        val uid = user.uid


        uri = Uri.parse(imagenDefault.toString())


        checkValidacion()

        imagenCircleImageView.setOnClickListener {
            viewimg()
        }

        btnGuardar.setOnClickListener {
            if (!textViewNombre.text.isEmpty() || !textViewApellido.text.isEmpty()){
                if (!checkBoxH.isChecked && !checkBoxM.isChecked && !checkBoxO.isChecked){
                    Toast.makeText(this,"Seleccionar el genero",Toast.LENGTH_LONG).show()
                }else{
                    guardarDatos(uid, genero)
                }

            }else{
                Toast.makeText(this,"Campos vacios",Toast.LENGTH_LONG).show()
            }
        }

        btnOmitir.setOnClickListener {

            setDatos(uid,"Anonimo","default","default",imagenDefault,"default",false)
        }

    }

    fun checkValidacion(){

        /*checkBoxH.setOnCheckedChangeListener { compoundButton, b ->
            checkBoxO.isSelected = false
            checkBoxH.isSelected = false
        }

        checkBoxM.setOnCheckedChangeListener { compoundButton, b ->
            checkBoxO.isSelected = false
            checkBoxH.isSelected = false
        }

        checkBoxO.setOnCheckedChangeListener { compoundButton, b ->
            checkBoxO.isSelected = false
            checkBoxH.isSelected = false
        }*/

        checkBoxM.setOnClickListener {
            genero = "Mujer"
            checkBoxO.isChecked = false
            checkBoxH.isChecked = false
        }

        checkBoxH.setOnClickListener {
            genero = "Hombre"
            checkBoxO.isChecked = false
            checkBoxM.isChecked = false
        }

        checkBoxO.setOnClickListener {
            genero = "Otro"
            checkBoxH.isChecked = false
            checkBoxM.isChecked = false
        }

        /*if (checkBoxM.isChecked){
            genero = "Mujer"
            checkBoxO.isChecked = false
            checkBoxH.isChecked = false

        } else if (checkBoxH.isSelected){
            genero = "Hombre"
            checkBoxO.isChecked = false
            checkBoxM.isChecked = false
            //checkBoxH.isSelected = true
        }else if (checkBoxO.isSelected){
            genero = "Otro"
            checkBoxH.isChecked = false
            checkBoxM.isChecked = false
            //checkBoxH.isSelected = false
        }*/

    }


    fun guardarDatos(uid:String, genero: String){

        val pd = ProgressDialog(this)
        pd.setTitle("Guardando datos")
        pd.show()
        val imageRef: StorageReference = FirebaseStorage.getInstance().reference.child("usuarios/foto_perfil/"+uid+".jpg")
        imageRef.putFile(uri)
                .addOnFailureListener {
                    pd.dismiss()

                // Toast.makeText(this,"Error al guardar",Toast.LENGTH_LONG).show()
                }.addOnSuccessListener { taskSnapshot ->
                    pd.dismiss()
                }.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    imageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    pd.dismiss()
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        val uri = downloadUri.toString()
                        setDatos(uid,textViewNombre.text.toString()+" "+textViewApellido.text.toString(),textViewDescripcion.text.toString(),
                                "default",uri,genero,false)

                    } else {
                        setDatos(uid,textViewNombre.text.toString()+" "+textViewApellido.text.toString(),textViewDescripcion.text.toString(),
                                "default",imagenDefault,genero,false)

                    }
                }


    }

    fun viewimg(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,1)
    }

    fun setDatos(uid: String, nombre: String, descripcion: String, titulo: String, foto: String, genero:String, ispsicologo: Boolean){

        val db = Firebase.firestore

        val datos = hashMapOf(
                    "nombre" to nombre,
                    "descripcion" to descripcion,
                    "titulo" to titulo,
                    "foto" to foto,
                    "genero" to genero,
                    "ispsicologo" to ispsicologo
            )

        db.collection("usuarios").document(uid).set(datos)
                .addOnSuccessListener {
                    staractivity()
                }.addOnFailureListener {

                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==1 && resultCode == Activity.RESULT_OK && data !=null){
            uri = data.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
            imagenCircleImageView.setImageBitmap(bitmap)
        }
    }

    private fun staractivity() {
        val loginActivity = Intent(applicationContext, HomeActivity::class.java)
        startActivity(loginActivity)
        savePrefsData()
        finish()
    }

    private fun restorePrefData(): Boolean {
        val pref = applicationContext.getSharedPreferences("introConf", MODE_PRIVATE)
        return pref.getBoolean("isConfiguracion", false)
    }

    private fun savePrefsData() {
        val pref = applicationContext.getSharedPreferences("introConf", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("isConfiguracion", true)
        editor.apply()
    }

    fun findViewbyid(){
        imagenCircleImageView = findViewById(R.id.editImagenProfile)
        textViewNombre = findViewById(R.id.editTextNombreProfile)
        textViewApellido = findViewById(R.id.editTextApellidoProfile)
        textViewDescripcion = findViewById(R.id.editTextDescripcionProfile)
        btnOmitir = findViewById(R.id.btnOmitir)
        btnGuardar = findViewById(R.id.btnGuardarDatos)
        checkBoxH = findViewById(R.id.checkBoxH)
        checkBoxM = findViewById(R.id.checkBoxM)
        checkBoxO = findViewById(R.id.checkBoxO)

        imagenDefault = "https://www.nicepng.com/png/detail/202-2022264_usuario-annimo-usuario-annimo-user-icon-png-transparent.png"

    }
}
