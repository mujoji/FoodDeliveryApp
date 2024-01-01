package com.eccit.template

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {

    lateinit var btnRegister : Button
    lateinit var edtEmail : EditText
    lateinit var edtName : EditText
    lateinit var edtPassword : EditText
    lateinit var edtPasswordConfirmation : EditText
    lateinit var txt_to_login : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        btnRegister = findViewById(R.id.btn_register)
        edtEmail = findViewById(R.id.edt_email_register)
        edtName = findViewById(R.id.edt_name_register)
        edtPassword = findViewById(R.id.edt_password_register)
        edtPasswordConfirmation = findViewById(R.id.edt_passwordc_register)
        txt_to_login = findViewById(R.id.tv_to_login)

        txt_to_login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener {
            var userModel = UserModel(
                Email = edtEmail.text.toString(),
                Name = edtName.text.toString(),
                Password = PasswordHelper.md5(edtPassword.text.toString())
            )

            checkUser(edtEmail.text.toString()) { isSuccess, isRegistered ->
                if (!isSuccess) {
                    Toast.makeText(
                        applicationContext,
                        "Terjadi kesalahan",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@checkUser
                } else if (isRegistered) {
                    Toast.makeText(
                        applicationContext,
                        "Akun dengan email ${edtEmail.text.toString()} sudah terdaftar!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@checkUser
                }

                this.registerUser(userModel)
            }
        }
    }

    private fun checkUser(email: String, checkResult: (isSuccess: Boolean, isRegistered: Boolean) -> Unit) {
        val db = Firebase.firestore
        db.collection("users").whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->

                var isSuccess = true
                var isRegistered = false

                if (!documents.isEmpty) {
                    isRegistered = true
                }
                checkResult.invoke(isSuccess, isRegistered)
            }
            .addOnFailureListener { exception ->
                checkResult.invoke(false, false)
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    fun registerUser(userModel: UserModel) {
        val db = Firebase.firestore
        db.collection("users")
            .add(userModel)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(
                    applicationContext,
                    "Berhasil melakukan registrasi!",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}