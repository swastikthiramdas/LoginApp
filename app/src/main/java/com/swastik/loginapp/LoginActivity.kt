package com.swastik.loginapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPref = getSharedPreferences("email", MODE_PRIVATE)
        auth = FirebaseAuth.getInstance()

        login_login.setOnClickListener {
            if (check()){

                var email = email_login.text.toString()
                var password = password_login.text.toString()

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                    task->
                    if (task.isSuccessful){

                        val editor = sharedPref.edit()
                        editor.putString("email",email)
                        editor.apply()
                    val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this,"Wrong Details",Toast.LENGTH_SHORT).show()
                    }

                }
            }
            else{

                Toast.makeText(this,"Please Enter Details",Toast.LENGTH_SHORT).show()

            }
        }

        sign_login.setOnClickListener {

            val intent = Intent(this,SignActivity::class.java)
            startActivity(intent)

        }
    }

    private fun check(): Boolean {
        if (email_login.text.toString().trim{it<=' '}.isNotEmpty()
            &&
                password_login.text.toString().trim{it<=' '}.isNotEmpty()){
            return true
        }
        return false
    }

}