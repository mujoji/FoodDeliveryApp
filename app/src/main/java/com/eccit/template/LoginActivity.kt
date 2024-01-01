package com.eccit.template

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    lateinit var btnLogin : Button
    lateinit var etEmail : EditText
    lateinit var etPassword : EditText
    lateinit var txtRegister : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)


        btnLogin = findViewById(R.id.btn_login)
        etEmail = findViewById(R.id.edt_email_login)
        etPassword = findViewById(R.id.edt_password_login)

        txtRegister = findViewById(R.id.tv_to_register)

        txtRegister.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            this.auth(etEmail.text.toString(), etPassword.text.toString()) { isValid ->
                if (!isValid) {
                    Toast.makeText(
                        applicationContext,
                        "Username atau password salah!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@auth
                }

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun auth(email: String, password: String, checkResult: (isValid: Boolean) -> Unit) {
        val db = Firebase.firestore
        db.collection("users").whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                var isValid = false

                for (document in documents) {
                    var pass = document.data.get("password").toString()

                    if (!pass.equals(PasswordHelper.md5(password).toString() )) {
                        break
                    }

                    val sharedPreference =  getSharedPreferences(
                        "app_preference", Context.MODE_PRIVATE
                    )

                    var editor = sharedPreference.edit()
                    editor.putString("id", document.id.toString())
                    editor.putString("name", document.data.get("name").toString())
                    editor.putString("email", document.data.get("email").toString())
                    editor.commit()

                    isValid = true
                }

                checkResult.invoke(isValid)
            }

    }
}