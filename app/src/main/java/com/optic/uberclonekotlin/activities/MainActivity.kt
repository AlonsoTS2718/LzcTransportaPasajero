package com.optic.uberclonekotlin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast

import com.optic.uberclonekotlin.databinding.ActivityMainBinding
import com.optic.uberclonekotlin.providers.AuthProvider

class MainActivity : AppCompatActivity() {



    //variable binding para cceder a bonotnes con id
    private lateinit var binding: ActivityMainBinding
    val authProvider = AuthProvider()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Con esta variable podemos llamar a todo boton que tenga id
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ///asdas
        //Encabezado y pie transparente
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        //Si se preciona boton registrarse
        binding.btnRegister.setOnClickListener { goToRegister() }
        //Si se presiona botono iniciar sesion
        binding.btnLogin.setOnClickListener { login() }


    }


    private fun login() {
        //Varible que guarda el correo
        val email = binding.textFieldEmail.text.toString()
        //Varible que guarda la contraseña
        val password = binding.textFieldPassword.text.toString()

        //Comprabar llenado de campos
        if (isValidForm(email, password)) {
            authProvider.login(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    //Va
                    goToMap()
                }
                else {
                    Toast.makeText(this@MainActivity, "Error iniciando sesion", Toast.LENGTH_SHORT).show()
                    Log.d("FIREBASE", "ERROR: ${it.exception.toString()}")
                }
            }
        }
    }

    //Funcion par
    private fun goToMap() {
        val i = Intent(this, MapActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }


    //Funcion para retornar que no haya campos vacios: bool
    private fun isValidForm(email: String, password: String): Boolean {

        //Muestra mensaje de correo vacio
        if (email.isEmpty()) {
            Toast.makeText(this, "Ingresa tu correo electronico", Toast.LENGTH_SHORT).show()
            return false
        }
        //Muestra mensaje de caontraseña vacia
        if (password.isEmpty()) {
            Toast.makeText(this, "Ingresa tu contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        
        return true
    }

    //Funcion ir a pantalla registrar
    private fun goToRegister() {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }

    override fun onStart() {
        super.onStart()
        if (authProvider.existSession()) {
            goToMap()
        }
    }


}