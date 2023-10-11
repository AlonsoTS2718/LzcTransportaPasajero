package com.optic.uberclonekotlin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.optic.uberclonekotlin.databinding.ActivityRegisterBinding
import com.optic.uberclonekotlin.models.Client
import com.optic.uberclonekotlin.providers.AuthProvider
import com.optic.uberclonekotlin.providers.ClientProvider

class RegisterActivity : AppCompatActivity() {

    //Acedder a botones mediante binding
    private lateinit var binding: ActivityRegisterBinding
    private val authProvider = AuthProvider()
    private val clientProvider = ClientProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Con esta variable podemos llamar a todo boton que tenga id
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Encabezado y pie transparentes
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

      //  binding.btnGoToLogin.setOnClickListener { goToLogin() }
        binding.btnRegister.setOnClickListener { register() }
    }
    
    private fun register() {
        //Variable que guarda el nombre
        val name = binding.textFieldName.text.toString()
        //Variable que guarda el apellido
        val lastname = binding.textFieldLastname.text.toString()
        //Variable que guarda el correo
        val email = binding.textFieldEmail.text.toString()
        //Variable que guarda el telefono
        val phone = binding.textFieldPhone.text.toString()
        //Variable que guarda la contraseña
        val password = binding.textFieldPassword.text.toString()
        //variable que guarda la confirmacion de contraseña
        val confirmPassword = binding.textFieldConfirmPassword.text.toString()
        
        if (isValidForm(name, lastname, email, phone, password, confirmPassword)) {
            authProvider.register(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val client = Client(
                        id = authProvider.getId(),
                        name = name,
                        lastname = lastname,
                        phone = phone,
                        email = email
                    )
                    clientProvider.create(client).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                            goToMap()
                        }
                        else {
                            Toast.makeText(this@RegisterActivity, "Hubo un error Almacenado los datos del usuario ${it.exception.toString()}", Toast.LENGTH_SHORT).show()
                            Log.d("FIREBASE", "Error: ${it.exception.toString()}")
                        }
                    }
                }
                else {
                    Toast.makeText(this@RegisterActivity, "Registro fallido ${it.exception.toString()}", Toast.LENGTH_LONG).show()
                    Log.d("FIREBASE", "Error: ${it.exception.toString()}")
                }
            }
        }
        
    }

    private fun goToMap() {
        val i = Intent(this, MapActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }

    //Funcion para retornar que no haya campos vacios: bool
    private fun isValidForm(
        name: String, 
        lastname: String, 
        email: String, 
        phone: String, 
        password: String, 
        confirmPassword: String
    ): Boolean {
        
        if (name.isEmpty()) {
            Toast.makeText(this, "Debes ingresar tu nombre", Toast.LENGTH_SHORT).show()
            return false
        }
        if (lastname.isEmpty()) {
            Toast.makeText(this, "Debes ingresar tu apellido", Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Debes ingresar tu correo electronico", Toast.LENGTH_SHORT).show()
            return false
        }
        if (phone.isEmpty()) {
            Toast.makeText(this, "Debes ingresar tu telefono", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Debes ingresar la contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        if (confirmPassword.isEmpty()) {
            Toast.makeText(this, "Debes ingresar la confirmacion de la contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "las contraseñas deben coincidir", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(this, "la contraseña deben tener al menos 6 caracteres", Toast.LENGTH_LONG).show()
            return false
        }
        
        return true
        
    }


    /*
    private fun goToLogin() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }*/
}