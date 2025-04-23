package com.example.fbb_mad

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth


class registerscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lateinit var auth: FirebaseAuth
        auth = FirebaseAuth.getInstance()


        fun onStart() {
            super.onStart()
            // Check if user is signed in (non-null) and update UI accordingly.
            val currentUser = auth.currentUser
            if (currentUser != null) {
                fun reload() {
                    Toast.makeText(this, "User already logged in!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        enableEdgeToEdge()
        setContentView(R.layout.activity_registerscreen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_registerscreen)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}