package com.example.appclinica.ui.configuracion

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
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

class ConfigurarPerfilActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var imagenCircleImageView: CircleImageView
    lateinit var textViewNombre: EditText
    lateinit var textViewApellido: EditText
    lateinit var textViewDescripcion: EditText
    lateinit var btnOmitir:Button
    lateinit var btnGuardar:Button
    lateinit var checkBoxH: CheckBox
    lateinit var checkBoxM: CheckBox
    lateinit var checkBoxO: CheckBox
    lateinit var uri:Uri
    lateinit var imagenDefault: String
    lateinit var genero: String
    lateinit var uid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configurar_perfil)

        if (restorePrefData()) {
            staractivity()
        }

        findViewbyid()

        val user = Firebase.auth.currentUser
        uid = user.uid

        //checkValidacion()


    }

    override fun onClick(v: View?) {

        when(v!!.id){
            R.id.btnGuardarDatos->{
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
            R.id.btnOmitir->{
                setDatos(uid,"Anonimo","default","default",imagenDefault,"default",false)

            }
            R.id.editImagenProfile ->{
                viewimg()

            }
            R.id.checkBoxM -> {
                genero = "Mujer"
                checkBoxO.isChecked = false
                checkBoxH.isChecked = false
            }
            R.id.checkBoxH ->{
                genero = "Hombre"
                checkBoxO.isChecked = false
                checkBoxM.isChecked = false
            }
            R.id.checkBoxO ->{
                genero = "Otro"
                checkBoxH.isChecked = false
                checkBoxM.isChecked = false
            }
        }
    }

    /*fun checkValidacion(){

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

    }*/

    //Accion del boton guardar datos

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

    //Abrir la galaria para la selccion de imagen
    fun viewimg(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,1)
    }

    //Envia informacion del usuario para guardar en firstore
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

    //Cargar la imagen seleccionada al activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==1 && resultCode == Activity.RESULT_OK && data !=null){
            uri = data.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
            imagenCircleImageView.setImageBitmap(bitmap)
        }
    }

    //Cargar el siguiente activity para la configuracion del perfil
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

        btnGuardar.setOnClickListener(this)
        btnOmitir.setOnClickListener(this)
        imagenCircleImageView.setOnClickListener(this)
        checkBoxM.setOnClickListener(this)
        checkBoxH.setOnClickListener(this)
        checkBoxO.setOnClickListener(this)

        imagenDefault = "https://www.nicepng.com/png/detail/202-2022264_usuario-annimo-usuario-annimo-user-icon-png-transparent.png"

        uri = Uri.parse(imagenDefault)

    }


}
