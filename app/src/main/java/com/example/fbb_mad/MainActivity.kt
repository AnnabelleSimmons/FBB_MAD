package com.example.fbb_mad

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Enable edge-to-edge layout
        setContentView(R.layout.activity_main)

        // Get the root layout
        val rootView = findViewById<android.view.View>(R.id.activity_main)

        // Adjust padding based on system bars
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Login button click listener
        val loginButton = findViewById<Button>(R.id.loginbutton)
        loginButton.setOnClickListener {
            navigateToLoginScreen()  // Go to LoginScreen
        }

        // Register button click listener
        val registerButton = findViewById<Button>(R.id.registerbutton)
        registerButton.setOnClickListener {
            navigateToRegisterScreen()  // Go to RegisterScreen
        }
    }

    // Navigate to LoginScreen
    private fun navigateToLoginScreen() {
        val intent = Intent(this, LoginScreen::class.java)
        startActivity(intent)
    }

    // Navigate to RegisterScreen
    private fun navigateToRegisterScreen() {
        val intent = Intent(this, RegisterScreen::class.java)
        startActivity(intent)
    }
}
