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

class LoginScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Enable edge-to-edge layout
        setContentView(R.layout.activity_login_screen)

        // Adjust padding based on system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_login_screen)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val loginButton = findViewById<Button>(R.id.submitButton2)  // Get login button
        val emailEditText = findViewById<EditText>(R.id.registeremail)  // Get email input
        val passwordEditText = findViewById<EditText>(R.id.registerpassword)  // Get password input

        // Set click listener for login button
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Check if email and password are entered
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Attempt login with Firebase
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // On success, navigate to home screen
                            val intent = Intent(this, HomeBaseScreen::class.java)
                            startActivity(intent)
                            finish()  // Close login screen
                        } else {
                            // On failure, show authentication error
                            Toast.makeText(baseContext, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Prompt user if fields are empty
                Toast.makeText(this, "Please enter both email and password.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
