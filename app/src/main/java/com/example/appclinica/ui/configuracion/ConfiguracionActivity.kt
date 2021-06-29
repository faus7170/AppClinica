package com.example.appclinica.ui.configuracion

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appclinica.HomeActivity
import com.example.appclinica.R
import com.example.appclinica.ui.comunidad.model.SetPregunt
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView

class ConfiguracionActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var imagenCircleImageView: CircleImageView
    lateinit var textViewNombre: EditText
    lateinit var textViewApellido: EditText
    lateinit var textViewDescripcion: EditText
    lateinit var btnOmitir: Button
    lateinit var btnGuardar: Button
    lateinit var btnTest: Button
    lateinit var checkBoxH: CheckBox
    lateinit var checkBoxM: CheckBox
    lateinit var checkBoxO: CheckBox
    lateinit var uri: Uri
    lateinit var imagenDefault: String
    lateinit var genero: String
    lateinit var uid : String
    val database = Firebase.database


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion)

        findViewbyid()

        uid = uidShared()

        btnTest = findViewById(R.id.btnTestActualizar)

        btnTest.setOnClickListener {
            //actualizarPublicacion()
            //actualizarComentarios()

        }

    }

    override fun onClick(v: View?) {

        when(v!!.id){
            R.id.btnGuardarDatos -> {
                if (!textViewNombre.text.isEmpty() || !textViewApellido.text.isEmpty()) {
                    if (!checkBoxH.isChecked && !checkBoxM.isChecked && !checkBoxO.isChecked) {
                        Toast.makeText(this, "Seleccionar el genero", Toast.LENGTH_LONG).show()
                    } else {
                        guardarDatos(uid, genero)
                    }

                } else {
                    Toast.makeText(this, "Campos vacios", Toast.LENGTH_LONG).show()
                }
            }
            R.id.btnOmitir -> {
                //setDatos(uid,"Anonimo","default","default",imagenDefault,"default",false)
                staractivity()

            }
            R.id.editImagenProfile -> {
                viewimg()

            }
            R.id.checkBoxM -> {
                genero = "Mujer"
                checkBoxO.isChecked = false
                checkBoxH.isChecked = false
            }
            R.id.checkBoxH -> {
                genero = "Hombre"
                checkBoxO.isChecked = false
                checkBoxM.isChecked = false
            }
            R.id.checkBoxO -> {
                genero = "Otro"
                checkBoxH.isChecked = false
                checkBoxM.isChecked = false
            }
        }
    }

    fun guardarDatos(uid: String, genero: String){

        val pd = ProgressDialog(this)
        pd.setTitle("Guardando datos")
        pd.show()
        val imageRef: StorageReference = FirebaseStorage.getInstance().reference.child("usuarios/foto_perfil/" + uid + ".jpg")
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
                        setDatos(uid, textViewNombre.text.toString() + " " + textViewApellido.text.toString(), textViewDescripcion.text.toString(),
                                "default", uri, genero, false)

                    } else {
                        setDatos(uid, textViewNombre.text.toString() + " " + textViewApellido.text.toString(), textViewDescripcion.text.toString(),
                                "default", imagenDefault, genero, false)

                    }
                }


    }

    fun setDatos(uid: String, nombre: String, descripcion: String, titulo: String, foto: String, genero: String, ispsicologo: Boolean){

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
                    actualizarPublicacion(foto, nombre)
                    actualizarComentarios(foto, nombre)
                    staractivity()
                }.addOnFailureListener {

                }
    }

    fun actualizarPublicacion(foto: String, nombre: String) {
        val mutableList: MutableList<String> = mutableListOf()

        val myRef = database.getReference("publicacion")
        //Log.d("arrayKey","array "+myRef)

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mutableList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue<SetPregunt>()
                    if (uid.equals(post!!.uid)) {

                        mutableList.add(postSnapshot.key.toString())
                    }
                }
                for (i in mutableList.indices) {
                    myRef.child(mutableList[i]).child("nombre").setValue(nombre)
                    myRef.child(mutableList[i]).child("foto").setValue(foto)
                    //myRef.child(mutableList[i]).child("comentarios").child("nombre").setValue(nombre)
                    //myRef.child(mutableList[i]).child("comentarios").child("foto").setValue(foto)
                }
                Log.d("testarray", "s " + mutableList.size)

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

    fun actualizarComentarios(foto: String, nombre: String){
        val mutableList: MutableList<String> = mutableListOf()
        val mutableListref: MutableList<String> = mutableListOf()

        val myRef = database.getReference("publicacion")
        //Log.d("arrayKey","array "+myRef)

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mutableList.clear()
                mutableListref.clear()
                for (postSnapshot in dataSnapshot.children) {
                    //mutableListref.add(postSnapshot.key.toString())
                    for (posttest in postSnapshot.child("comentarios").children) {
                        val post = posttest.getValue<SetPregunt>()
                        if (uid.equals(post!!.uid)) {
                            mutableList.add(posttest.key.toString())
                            mutableListref.add(postSnapshot.key.toString()+"/"+posttest.key.toString())
                        }
                    }
                    //mutableListref.add(postSnapshot.key.toString())
                }

                val hs = HashSet<String>()
                //val hs = HashSet<Any>()
                hs.addAll(mutableListref)
                mutableListref.clear()
                mutableListref.addAll(hs)

                //actualizartest(mutableList)
                for (i in mutableListref.indices) {

                    val test = mutableListref[i].split("/")
                    Log.d("testarray","publi "+test[0]+" comen "+test[1])
                    myRef.child(test[0]).child("comentarios").child(test[1]).child("nombre").setValue(nombre)
                    myRef.child(test[0]).child("comentarios").child(test[1]).child("foto").setValue(foto)
                    //myRef.child(mutableList[i]).child("comentarios").child(mutableList[i]).child("nombre").setValue("")
                    //Log.d("testarray","publi "+mutableListref[i])
                    /*for (it in mutableList.indices) {
                        Log.d("testarray", "publ " + mutableListref[i] + " comen " + mutableList[i])
                        //Log.d("testarray","s "+mutableListref[i])
                    }*/

                }


                /*for (i in mutableList.indices){

                    //myRef.child(mutableList[i]).child("comentarios").child(mutableList[i]).child("nombre").setValue("")
                    Log.d("testarray","comen "+mutableList[i])
                    /*for (it in mutableList.indices){
                        Log.d("testarray","s "+mutableListref[i] + " comen "+mutableList[i])
                        //Log.d("testarray","s "+mutableListref[i])
                    }*/


                }*/


            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

    fun viewimg(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }

    //Cargar la imagen seleccionada al activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==1 && resultCode == Activity.RESULT_OK && data !=null){
            uri = data.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            imagenCircleImageView.setImageBitmap(bitmap)
        }
    }

    //Cargar el siguiente activity para la configuracion del perfil
    private fun staractivity() {
        val loginActivity = Intent(applicationContext, HomeActivity::class.java)
        startActivity(loginActivity)
        finish()
    }

    fun uidShared(): String {
        val pref = applicationContext.getSharedPreferences("dateUser", MODE_PRIVATE)
        return pref.getString("uid", "default")!!
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