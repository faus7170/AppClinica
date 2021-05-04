package com.example.appclinica.ui.configuracion

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.appclinica.R
import com.example.appclinica.ui.PrincipalActivity
import com.example.appclinica.ui.psicologo.GetDatosPsicologo
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
    lateinit var storage:StorageReference

    val user = Firebase.auth.currentUser
    val db = Firebase.firestore

    var token= String


    lateinit var uri:Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configurar_perfil)

        if (restorePrefData()) {
            staractivity()
        }

        findViewbyid()

        val uid = user.uid

        imagenCircleImageView.setOnClickListener {
            viewimg()
        }

        btnGuardar.setOnClickListener {
            subirImagen(uid)
        }

        btnOmitir.setOnClickListener {

            var imagen = "https://www.nicepng.com/png/detail/202-2022264_usuario-annimo-usuario-annimo-user-icon-png-transparent.png"

            setDatos(uid,"Anonimo","default","default",imagen,false)
        }

    }


    fun subirImagen(uid:String){
        if (uri!=null){
            var pd = ProgressDialog(this)
            pd.setTitle("Guardando datos")
            pd.show()

            var imageRef: StorageReference = FirebaseStorage.getInstance().reference.child("usuarios/foto_perfil/"+uid+".jpg")
            imageRef.putFile(uri)
            .addOnFailureListener {
                pd.dismiss()
                Toast.makeText(this,"Error al guardar",Toast.LENGTH_LONG).show()
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
                        if (task.isSuccessful) {
                            val downloadUri = task.result
                            var uri = downloadUri.toString()

                            setDatos(uid,textViewNombre.text.toString()+" "+textViewApellido.text.toString(),textViewDescripcion.text.toString(),
                            "default",uri,false)

                            /*val washingtonRef = db.collection("usuarios").document(uid)
                            washingtonRef
                                    .update("foto",  uri)
                                    .addOnSuccessListener {
                                        staractivity()
                                    }
                                    .addOnFailureListener {

                                    }*/

                        } else {
                            // Handle failures
                            // ...
                        }
                    }

        }
    }

    fun viewimg(){
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent,1)
    }
    private fun staractivity() {
        val loginActivity = Intent(applicationContext, PrincipalActivity::class.java)
        startActivity(loginActivity)
        savePrefsData()
        finish()
    }

    fun setDatos(uid: String, nombre: String, descripcion: String, titulo: String, foto: String, ispsicologo: Boolean){

        val db = Firebase.firestore

        val datos = hashMapOf(
                    "nombre" to nombre,
                    "descripcion" to descripcion,
                    "titulo" to titulo,
                    "foto" to foto,
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
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
            imagenCircleImageView.setImageBitmap(bitmap)
        }
    }

    private fun restorePrefData(): Boolean {
        val pref = applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
        return pref.getBoolean("isConfiguracion", false)
    }

    private fun savePrefsData() {
        val pref = applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("isConfiguracion", true)
        editor.commit()
    }

    fun findViewbyid(){
        imagenCircleImageView = findViewById(R.id.editImagenProfile)
        textViewNombre = findViewById(R.id.editTextNombreProfile)
        textViewApellido = findViewById(R.id.editTextApellidoProfile)
        textViewDescripcion = findViewById(R.id.editTextDescripcionProfile)
        btnOmitir = findViewById(R.id.btnOmitir)
        btnGuardar = findViewById(R.id.btnGuardarDatos)
    }
}
