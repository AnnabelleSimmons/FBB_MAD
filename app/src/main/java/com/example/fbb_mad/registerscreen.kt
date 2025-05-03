package com.example.fbb_mad

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class RegisterScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Enables edge-to-edge display
        setContentView(R.layout.activity_registerscreen)

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_registerscreen)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val submitButton = findViewById<Button>(R.id.submitButton)
        val emailEditText = findViewById<EditText>(R.id.registeremail)
        val passwordEditText = findViewById<EditText>(R.id.registerpassword)

        // Submit button click listener
        submitButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Check if both fields are filled
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Registration successful, go to home screen
                            val intent = Intent(this, HomeBaseScreen::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // Registration failed, show a toast
                            Toast.makeText(baseContext, "Registration Failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Display error if fields are empty
                Toast.makeText(this, "Please enter both email and password.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
