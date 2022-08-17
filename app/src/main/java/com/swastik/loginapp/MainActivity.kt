package com.swastik.loginapp


import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = FirebaseFirestore.getInstance()
        sharedPref = getSharedPreferences("email", MODE_PRIVATE)

        var email = sharedPref.getString("email","-1").toString()

        if (email != "-1"){
            db.collection("USERS").document(email).get()
                .addOnSuccessListener {
                        document->
                    name_home.text = document.get("name").toString()
                    phone_home.text = document.get("phone").toString()
                    email_home.text = document.get("email").toString()
                }
        }
        else{

            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        log_out.setOnClickListener {
            sharedPref.edit().remove("email").apply()
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}