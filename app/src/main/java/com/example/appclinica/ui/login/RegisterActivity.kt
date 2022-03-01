package com.example.appclinica.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appclinica.HomeActivity
import com.example.appclinica.R
import com.example.appclinica.notification.Alert
import com.example.appclinica.notification.Constants
import com.example.appclinica.paymantel.BackendService
import com.example.appclinica.paymantel.modelo.CreateChargeResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.gson.GsonBuilder
import com.paymentez.android.Paymentez
import com.paymentez.android.model.Card
import com.paymentez.android.rest.TokenCallback
import com.paymentez.android.rest.model.ErrorResponse
import com.paymentez.android.rest.model.PaymentezError
import com.paymentez.android.view.CardMultilineWidget
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.DateFormat

class RegisterActivity : AppCompatActivity() {
    lateinit var imagenDefault: String

    lateinit var btnRegisterUser: Button
    lateinit var editEmail : EditText
    lateinit var editPass : EditText
    lateinit var editRepitPass: EditText
    lateinit var cardWidget: CardMultilineWidget
    lateinit var backendService: BackendService
    lateinit var checkBoxMensual: CheckBox
    lateinit var checkBoxSemestral: CheckBox
    lateinit var checkBoxAnual: CheckBox
    var valorPagar = 0.00
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view=setContentView(R.layout.activity_register)

        Paymentez.setEnvironment(Constants.PAYMENTEZ_IS_TEST_MODE, Constants.PAYMENTEZ_CLIENT_APP_CODE, Constants.PAYMENTEZ_CLIENT_APP_KEY);

        elementsById()
        title="Autenticación"
        //val uid: String = com.paymentez.examplestore.utils.Constants.USER_ID
        //val email: String = com.paymentez.examplestore.utils.Constants.USER_EMAIL
        setUp()

    }
    fun setUp (){
        btnRegisterUser.setOnClickListener{
            println("==========Se grabo =========== ")
            println(valorPagar)
            println(checkBoxSemestral.isChecked)
            println("============================== ")
            registerNewUser(editEmail.text.toString(),editPass.text.toString(),editRepitPass.text.toString())
        }
        checkBoxMensual.setOnClickListener{
            valorPagar = 4.99
            checkBoxSemestral.isChecked = false
            checkBoxAnual.isChecked = false
        }
        checkBoxSemestral.setOnClickListener{
            valorPagar = 16.99
            checkBoxMensual.isChecked = false
            checkBoxAnual.isChecked = false
        }
        checkBoxAnual.setOnClickListener{
            valorPagar = 24.99
            checkBoxMensual.isChecked = false
            checkBoxSemestral.isChecked = false
        }
    }
    fun registerNewUser(email: String, password: String, passwordRepit: String) {
        //val auth: FirebaseAuth = FirebaseAuth.getInstance()
        if (checkCredentials(email,password,passwordRepit)){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        println("Guardado")
                        val user =FirebaseAuth.getInstance().currentUser
                        //updateUI(user)
                        addCard()
                    } else {
                        val s=task.exception.toString()
                        println("Error: $s")

                        //---Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }
                }
        }
    }
    private fun updateUI(currentUser: FirebaseUser?) { //send current user to next activity
        if (currentUser == null) return
        val intent = Intent(this, HomeActivity::class.java).apply{
        }
        startActivity(intent)
        // savePrefsData()
        // requireActivity().finish()
    }
    fun checkCredentials(email: String, password: String, passwordRepit: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(applicationContext, "Llenar los campos", Toast.LENGTH_LONG).show()
            return false
        }else if (!email.contains("@") || email.length < 6) {
            Toast.makeText(applicationContext, "Verificar que el correo", Toast.LENGTH_LONG).show()
            return false
        }else if(!password.equals(passwordRepit)){
            Toast.makeText(applicationContext, "Claves no conciden", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }
    private fun elementsById() {
        editEmail = findViewById(R.id.editEmail)
        editPass = findViewById(R.id.txtRegisterClave)
        editRepitPass = findViewById(R.id.txtConfirmarClave)
        btnRegisterUser = findViewById(R.id.btnRegistrarUser)
        checkBoxMensual = findViewById(R.id.checkBoxMes)
        checkBoxSemestral = findViewById(R.id.checkBoxSemestral)
        checkBoxAnual = findViewById(R.id.checkBoxAnual)
        cardWidget =findViewById(R.id.card_multiline_widget)
        //checkBoxMensual.setOnClickListener()
        //checkBoxSemestral.setOnClickListener(this)
        //checkBoxAnual.setOnClickListener(this)

        imagenDefault = "https://www.nicepng.com/png/detail/202-2022264_usuario-annimo-usuario-annimo-user-icon-png-transparent.png"
    }
   fun addCard(){
       println("Control ==> ADD1")
      // cardWidget = findViewById<View>(R.id.card_multiline_widget) as CardMultilineWidget
        val cardToSave = cardWidget.card
       println("Control ==> ADD2")
        if (cardToSave == null) {
            println("Control ==> ADD3")
            println("Tarjeta invalida")
            /*Alert.show(applicationContext,
                "Error",
                "Tarjeta invalida"
            )*/
            return
        } else {
            println("Control ==> ADD4")
            Paymentez.addCard(applicationContext, Constants.USER_ID, Constants.USER_EMAIL, cardToSave, object :
                TokenCallback {
                override fun onSuccess(card: Card) {
                    val status=card.status.toString()
                    println("Control 5 ==> $status")
                    val message=card.message.toString()
                    println("Control 6 ==> $message")
                    if (card.status == "valid") {
                        println("Control 7 ==> ADD5")
                        pago(card.token)

                      /*  Alert.show(
                            applicationContext,
                            "La tarjeta fue valida",
                            "status: " + card.status + "\n" +
                                    "Card Token: " + card.token + "\n" +
                                    "transaction_reference: " + card.transactionReference
                        )*/

                    } else if ((card.status == "review")) {
                        println("Control 8 ==> ADD5")
                        val s=card.status.toString()
                        println("Control ==> $s")
                        val t=card.token .toString()
                        println("Control ==> $s Token $t")
                        /*Alert.show(
                            applicationContext,
                            "Tarjeta ya esta registrada",
                            ("status: " + card.status + "\n" +
                                    "Card Token: " + card.token + "\n" +
                                    "transaction_reference: " + card.transactionReference)
                        )*/
                    } else {
                        println("Control 9 ==> ADD5")
                        val t=card.status.toString()
                        val m= card.message.toString()
                        println("Control ==> $t Token $m")
                       /* Alert.show(
                            applicationContext,
                            "Error",
                            ("status: " + card.status + "\n" +
                                    "message: " + card.message)
                        )*/
                    }

                    //TODO: Create charge or Save Token to your backend
                }
                override fun onError(error: PaymentezError) {
                    println("Control 10 ==> ADD5")
                    val t=error.type.toString()
                    val h= error.help.toString()
                    val d= error.description.toString()
                    println("Control Type:$t Help:$h Descripcion:$d")
                    println("============================")
                    //Log.d("test_TokenD","token"+error.type)
                    /*Alert.show(
                        applicationContext,
                        "Error",
                        ("Type: " + error.type + "\n" +
                                "Help: " + error.help + "\n" +
                                "Description: " + error.description)
                    )*/

                }
            })
        }
    }

    fun pago(CARD_TOKEN: String){

        Log.d("test_Token", "valor " + CARD_TOKEN)
        println("Control ==> ADD6")
        if (CARD_TOKEN == "") {
            Alert.show(
                applicationContext,
                "Error",
                "Necesitas seleccionar una tarjeta"
            )
        } else {
            println("Control ==> ADD7")
            val ORDER_AMOUNT = 10.5
            //val ORDER_ID = "" + System.currentTimeMillis()
            val ORDER_ID = "" + millisToDate(System.currentTimeMillis())
            val ORDER_DESCRIPTION = "ORDER #$ORDER_ID"
            backendService.createCharge(
                Constants.USER_ID, Paymentez.getSessionId(applicationContext),
                CARD_TOKEN, ORDER_AMOUNT, ORDER_ID, ORDER_DESCRIPTION
            )!!.enqueue(object : Callback<CreateChargeResponse?> {

                override fun onResponse(
                    call: Call<CreateChargeResponse?>,
                    response: Response<CreateChargeResponse?>
                ) {
                    println("Control ==> ADD8")
                    val createChargeResponse: CreateChargeResponse? = response.body()
                    if (response.isSuccessful() && createChargeResponse != null && createChargeResponse.transaction != null) {
                        Alert.show(
                            applicationContext,
                            "Successful Charge",
                            """
                            date: ${createChargeResponse.transaction.payment_date.toString()}
                            date metodo: ${ORDER_ID}
                            status_detail: ${createChargeResponse.transaction.status.toString()}
                            message: ${createChargeResponse.transaction.message.toString()}
                            transaction_id:${createChargeResponse.transaction.id}
                            """.trimIndent()
                        )
                        //sendMail(createChargeResponse.transaction.id, createChargeResponse.transaction.amount,
                        //  ORDER_ID, createChargeResponse.transaction.status)
                    } else {
                        val gson = GsonBuilder().create()
                        try {
                            val errorResponse = gson.fromJson(
                                response.errorBody()!!.string(),
                                ErrorResponse::class.java
                            )
                            Alert.show(
                                applicationContext,
                                "Error",
                                errorResponse.error.type
                            )
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<CreateChargeResponse?>, e: Throwable) {
                    Alert.show(
                        applicationContext,
                        "Error",
                        e.localizedMessage
                    )
                }
            })
        }
    }

    private fun millisToDate(millis: Long): String? {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(millis)
        //You can use DateFormat.LONG instead of SHORT
    }

    //Verificar los campos de correo y password
    /*fun checkCredentials(email: String, password: String, passwordRepit: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(activity, "Llenar los campos", Toast.LENGTH_LONG).show()
            return false
        }else if (!email.contains("@") || email.length < 6) {
            Toast.makeText(activity, "Verificar que el correo", Toast.LENGTH_LONG).show()
            return false
        }else if(!password.equals(passwordRepit)){
            Toast.makeText(activity, "Claves no conciden", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }
*/
    //Conexion con firebase para el registro de una nueva cuenta
  /*  fun registerNewUser(email: String, password: String, passwordRepit: String) {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        if (checkCredentials(email,password,passwordRepit)){
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        //setDatos(user.uid)
                        updateUI(user, user?.uid.toString())
                        //sendEmailVerification(user)
                    } else {
                        Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }
                }
        }


    }
*/
    //Enviar un mensaje de verificacion para activiar la cuenta
    /*fun sendEmailVerification(user: FirebaseUser) {
        //Log.d(TAG, "started Verification")
        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //showConfirmationDialog(R.string.confirm_email, getString(R.string.please_confirm_email, user.email))
                    Toast.makeText(activity, "Mensaje de autenticacion enviado al correo", Toast.LENGTH_SHORT).show()

                }else {
                    //Log.e(TAG, "sendEmailVerification", task.exception)
                }
            }
    }
*/
    //Mostrar alerta
  /*  fun showConfirmationDialog(title: Int, msg: String) {
        val dlg = AlertDialog.Builder(requireActivity())
        dlg.setMessage(msg)
        dlg.setTitle(title)
        dlg.setPositiveButton(R.string.ok, null)
        dlg.show()

    }*/

    //Cargar el siguiente activity para la configuracion del perfil
  /*  fun updateUI(currentUser: FirebaseUser?, uid:String) { //send current user to next activity
        if (currentUser == null) return
        val intent = Intent(activity, HomeActivity::class.java)
        setDatos(uid,"Anonimo","default","default",imagenDefault,"default",false)
        startActivity(intent)
        requireActivity().finish()
    }*/

   /* fun setDatos(uid: String, nombre: String, descripcion: String, titulo: String, foto: String, genero:String, ispsicologo: Boolean){
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
                Toast.makeText(requireActivity(),"Usuario registrado con exito", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {

            }
    }
*/
   /* private fun elementsById() {
        editEmail = findViewById(R.id.editEmail)
        editPass = findViewById(R.id.txtRegisterClave)
        editRepitPass = findViewById(R.id.txtConfirmarClave)
        btnRegisterUser = findViewById(R.id.btnRegistrarCorreo)
        checkBoxMensual = findViewById(R.id.checkBoxMes)
        checkBoxSemestral = findViewById(R.id.checkBoxSemestral)
        checkBoxAnual = findViewById(R.id.checkBoxAnual)

        checkBoxMensual.setOnClickListener(this)
        checkBoxSemestral.setOnClickListener(this)
        checkBoxAnual.setOnClickListener(this)

        imagenDefault = "https://www.nicepng.com/png/detail/202-2022264_usuario-annimo-usuario-annimo-user-icon-png-transparent.png"
    }
*/
}