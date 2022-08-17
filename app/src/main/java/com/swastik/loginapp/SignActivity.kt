package com.swastik.loginapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign.*

class SignActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        sharedPref = getSharedPreferences("email", MODE_PRIVATE)
        signin_signin.setOnClickListener {
            if (check()) {

                var email = email_signin.text.toString()
                var password = password_signin.text.toString()
                var name = name_signin.text.toString()
                var phone = phone_signin.text.toString()
                val users = hashMapOf(
                    "name" to name,
                    "phone" to phone,
                    "email" to email
                )
                val collection = db.collection("USERS")
                val query = collection.whereEqualTo("email",email).get()
                    .addOnSuccessListener {
                        task->
                        if (task.isEmpty){
                            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){
                                task->
                                if (task.isSuccessful){

                                    val editor = sharedPref.edit()
                                    editor.putString("email",email)
                                    editor.apply()
                                    collection.document(email).set(users)
                                    val intent = Intent(this,MainActivity::class.java)
                                    startActivity(intent)
                                    finish()

                                }
                                else{

                                    Toast.makeText(this,"Make Sure Your Internet Connection is on",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        else{
                            Toast.makeText(this,"User Already Registered",Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }


            } else {

                Toast.makeText(this, "Please Enter Details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun check(): Boolean {

        if (
            email_signin.text.toString().trim { it <= ' ' }.isNotEmpty()
            &&
            password_signin.text.toString().trim { it <= ' ' }.isNotEmpty()
            &&
            name_signin.text.toString().trim { it <= ' ' }.isNotEmpty()
            &&
            phone_signin.text.toString().trim { it <= ' ' }.isNotEmpty()
        ) {
            return true
        }
        return false
    }
}